package com.xdjd.storebox.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtr.zxing.camera.CameraManager;
import com.dtr.zxing.decode.DecodeThread;
import com.dtr.zxing.utils.BeepManager;
import com.dtr.zxing.utils.CaptureActivityHandler;
import com.dtr.zxing.utils.InactivityTimer;
import com.google.zxing.Result;
import com.umeng.message.PushAgent;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.CartAdapter;
import com.xdjd.storebox.adapter.CartNewAdapter;
import com.xdjd.storebox.adapter.listener.CartListener;
import com.xdjd.storebox.bean.BaseBean;
import com.xdjd.storebox.bean.CartBean;
import com.xdjd.storebox.bean.CartGoodsListBean;
import com.xdjd.storebox.bean.CartListBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.CartUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 扫描下单
 * Created by lijipei on 2017/2/28.
 */
public class ScanningPlacetheorderActivity extends CaptureActivity /*implements SurfaceHolder.Callback, View.OnClickListener, CartListener*/ {

    private static final String TAG = ScanningPlacetheorderActivity.class.getSimpleName();
    TextView mEditSelect;
    ListView mCartListview;

    LinearLayout mCartLl;
    LinearLayout mScanningHintLl;
    EditText mBarCodeEdit;
    Button mAddCartBtn;

    private RelativeLayout mBarCodeEditRl;
    private RelativeLayout mImportSelectRl;

    private EaseTitleBar mTitleBar;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private VaryViewHelper mVaryViewHelper = null;
    private List<CartListBean> list = new ArrayList<>();
    private CartNewAdapter adapter;
    private ClearCartListener mClearCartListener = new ClearCartListener();

    private Intent intent;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scanning_placetheorder);
        ButterKnife.bind(this);

       initView();
        initData();

        PushAgent.getInstance(this).onAppStart();
    }

    private void initView() {
        mTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        mEditSelect = (TextView) findViewById(R.id.edit_select);
        mCartListview = (ListView) findViewById(R.id.cart_listview);
        mCartLl = (LinearLayout) findViewById(R.id.cart_ll);
        mBarCodeEditRl = (RelativeLayout) findViewById(R.id.bar_code_edit_rl);
        mImportSelectRl = (RelativeLayout) findViewById(R.id.import_select_rl);
        mScanningHintLl = (LinearLayout) findViewById(R.id.scanning_hint_ll);

        mBarCodeEdit = (EditText) findViewById(R.id.bar_code_edit);
        mAddCartBtn = (Button) findViewById(R.id.add_cart_btn);

        //mAddCartBtn.setOnClickListener(this);

        mVaryViewHelper = new VaryViewHelper(mCartListview);
    }

    protected void initData() {
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("扫码下单");

        mTitleBar.setRightTextColor(R.color.text_212121);
        mTitleBar.setRightText("编辑");

        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleBar.getRightText().toString().equals(getString(R.string.cart_edit))) {
                    mTitleBar.setRightText("完成");
                    CartNewAdapter.isSuccess = true;
                    mTitleBar.setRightText02("清空购物车");
                    mTitleBar.setRightTextColor02(R.color.text_212121);
                    mTitleBar.setRightTextClickListener02(mClearCartListener);
                } else {
                    CartNewAdapter.isSuccess = false;
                    mTitleBar.setRightText(getString(R.string.cart_edit));
                    mTitleBar.setRightText02Gone();
                }
                adapter.notifyDataSetChanged();
            }
        });

        mTitleBar.setRightTextViewVisibility(View.GONE);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

       /* mBarCodeEditRl.setOnClickListener(this);
        mImportSelectRl.setOnClickListener(this);*/

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        //adapter = new CartNewAdapter(this);
        mCartListview.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.import_select_rl:
                if (mBarCodeEditRl.getVisibility() == View.GONE) {
                    mBarCodeEditRl.setVisibility(View.VISIBLE);
                    mEditSelect.setText("扫码");
                    onPause();
                } else {
                    mBarCodeEditRl.setVisibility(View.GONE);
                    mEditSelect.setText("编辑");
                    onResume();
                }
                break;
            case R.id.add_cart_btn://确定
                if (mBarCodeEdit.getText().toString().equals("") || mBarCodeEdit.getText() == null) {
                    showToast("请输入条形码");
                    return;
                } else {
                    addCart(mBarCodeEdit.getText().toString());
                }
                break;
        }
    }


    /**
     * 扫码添加商品
     */
    private void addCart(String goodsInteCode) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (mBarCodeEditRl.getVisibility() == View.GONE) {
                        loadCartList(PublicFinal.FIRST, false);
                    } else {
                        loadCartList(PublicFinal.TWO, true);
                    }
                } else {
                    UIUtils.Toast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.addCartByScan, L_RequestParams.
                addCartByScan(UserInfoUtils.getId(this), UserInfoUtils.getCenterShopId(this), goodsInteCode), false);
    }

    /**
     * 查询购物商品列表接口
     *
     * @param isFirst
     * @param isDialog
     */
    private void loadCartList(int isFirst, boolean isDialog) {
        if (isFirst == PublicFinal.FIRST) {
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean cartBean) {
                if (cartBean.getRepCode().equals("00")) {
                    mVaryViewHelper.showDataView();

                    EventBus.getDefault().post(new CartEvent(cartBean.getCartNum(), cartBean.getTotalAmount()));
                    list.clear();
                    //list = cartBean.getCartList();
                    if (null == list || list.size() == 0) {

                        mCartLl.setVisibility(View.GONE);
                        mScanningHintLl.setVisibility(View.VISIBLE);
                        mTitleBar.setRightTextViewVisibility(View.GONE);

                        //                        mVaryViewHelper.showEmptyCartView("", new View.OnClickListener() {
                        //                            @Override
                        //                            public void onClick(View view) {
                        //                                Intent intent = new Intent(ScanningPlacetheorderActivity.this, MainActivity.class);
                        //                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        //                                intent.putExtra("currentTab", 1);
                        //                                startActivity(intent);
                        //                                *//**
                        //                                 * enter_anim:要跳转的界面开始的动画
                        //                                 * exit_anim:当前界面销毁的动画
                        //                                 *//*
                        //                                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                        //                            }
                        //                        });
                        //mLayoutCartEmptyMain.setVisibility(View.VISIBLE);
                    } else {
                        mCartLl.setVisibility(View.VISIBLE);
                        mScanningHintLl.setVisibility(View.GONE);
                        mTitleBar.setRightTextViewVisibility(View.VISIBLE);
                    }

                    //adapter.setData(list);

                    if (mBarCodeEditRl.getVisibility() == View.GONE) {
                        // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (handler == null) {
                                    handler = new CaptureActivityHandler(ScanningPlacetheorderActivity.this, cameraManager, DecodeThread.ALL_MODE);
                                }
                                handler.sendEmptyMessage(R.id.restart_preview);
                            }
                        }, 4500);
                    }
                }else if ("30".equals(cartBean.getRepCode())){
                    Intent intent = new Intent(ScanningPlacetheorderActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab",4);
                    startActivity(intent);
                    showToast(cartBean.getRepMsg());
                } else {
                    UIUtils.Toast(cartBean.getRepMsg());
                    //mVaryViewHelper.showErrorView(new onErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(new onErrorListener());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.cartList, L_RequestParams.getCartList(UserInfoUtils.getId(this),UserInfoUtils.getCenterShopId(this)), isDialog);
    }

    /*@Override
    public void cartEnter(final int i) {
        if (CartAdapter.isSuccess) {
            int num = 0;
            StringBuilder goodsCartId = new StringBuilder();

            for (int k = 0; k < list.get(i).getActivityList().size(); k++) {
                for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size(); z++) {
                    if (list.get(i).getActivityList().get(k).getDataList().get(z).getIsChild() == 1) {
                        //goodsCartId.append(list.get(i).getActivityList().get(k).getDataList().get(z).getCartId() + ",");
                        num++;
                    }
                }
            }

            if (goodsCartId.length() > 0) {
                goodsCartId.deleteCharAt(goodsCartId.length() - 1);
            } else {
                //showToast("还没选择要结算的商品");
                return;
            }

            final String str = goodsCartId.toString();

            DialogUtil.showCustomDialog(this, null, "确认要删除这" + num + "中商品吗?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                    deleteCart(i, str);
                }

                @Override
                public void no() {
                }
            });
        } else {
            BigDecimal goodsTotalAmount = new BigDecimal(list.get(i).getAmount());
            BigDecimal goodsFreeAmount = new BigDecimal(list.get(i).getFreeAmount());
            if (goodsTotalAmount.doubleValue() < goodsFreeAmount.doubleValue()) {
                //showToast("商品总价还没有达到起订金额");
                return;
            }
            StringBuilder goodsCartId = new StringBuilder();
            for (int k = 0; k < list.get(i).getActivityList().size(); k++) {
                for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size(); z++) {
                    if (list.get(i).getActivityList().get(k).getDataList().get(z).getIsChild() == 1) {
                        //goodsCartId.append(list.get(i).getActivityList().get(k).getDataList().get(z).getCartId() + ",");
                    }
                }
            }

            if (goodsCartId.length() > 0) {
                goodsCartId.deleteCharAt(goodsCartId.length() - 1);
            } else {
                UIUtils.Toast("还没选择要结算的商品");
                return;
            }

            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra("cartIds", goodsCartId.toString());
            intent.putExtra("type", list.get(i).getModulecode());
            startActivity(intent);
        }
    }

    @Override
    public void cartAll(int i) {
        if (list.get(i).getIsAll() == 0) {
            list.get(i).setIsAll(1);

            for (int k = 0; k < list.get(i).getActivityList().size(); k++) {

                for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size(); z++) {

                    list.get(i).getActivityList().get(k).getDataList().get(z).setIsChild(1);
                }
            }
        } else if (list.get(i).getIsAll() == 1) {
            list.get(i).setIsAll(0);

            for (int k = 0; k < list.get(i).getActivityList().size(); k++) {

                for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size(); z++) {

                    list.get(i).getActivityList().get(k).getDataList().get(z).setIsChild(0);
                }
            }
        }
        calculateCartPrice();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void cartSpell(int i) {
        Intent intent = new Intent(ScanningPlacetheorderActivity.this, MainActivity.class);
        intent.putExtra("currentTab", 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        *//**
         * enter_anim:要跳转的界面开始的动画
         * exit_anim:当前界面销毁的动画
         *//*
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }

    @Override
    public void cartPhone(final int i) {
        DialogUtil.showCustomDialog(this, "客服电话", list.get(i).getShopTel(), "拨打",
                "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:" + list.get(i).getShopTel()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void no() {
                    }
                });
    }

    @Override
    public void cartCollect(int i) {
        int num = 0;

        StringBuilder goodsCartId = new StringBuilder();
        final StringBuilder favoriteIds = new StringBuilder();

        for (int k = 0; k < list.get(i).getActivityList().size(); k++) {
            for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size(); z++) {
                if (list.get(i).getActivityList().get(k).getDataList().get(z).getIsChild() == 1) {
                    *//*goodsCartId.append(list.get(i).getActivityList().get(k).getDataList().get(z).getCartId() + ",");
                    favoriteIds.append(list.get(i).getActivityList().get(k).getDataList().get(z).getGpId() + ",");*//*
                    num++;
                }
            }
        }

        if (goodsCartId.length() > 0) {
            goodsCartId.deleteCharAt(goodsCartId.length() - 1);
            favoriteIds.deleteCharAt(favoriteIds.length() - 1);
        } else {
            //showToast("还没选择要结算的商品");
            return;
        }

        final String goodsStr = goodsCartId.toString();
        final String favoriteStr = favoriteIds.toString();

        DialogUtil.showCustomDialog(this, null, "当前选中的" + num + "种商品关注成功后,将从购物车删除,请您确认", "移入收藏",
                "我再想想", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        addFavoriteBatch(favoriteStr, goodsStr);
                    }

                    @Override
                    public void no() {
                    }
                });
    }

    @Override
    public void cartChildAction(int i, int j) {
        intent = new Intent(ScanningPlacetheorderActivity.this, ActionActivity.class);
        intent.putExtra("activityId", list.get(i).getActivityList().get(j).getActivityId());
        startActivity(intent);
    }

    @Override
    public void cartChildDelete(int i, int j, int k) {
        CartGoodsListBean bean = list.get(i).getActivityList().get(j).getDataList().get(k);

       // alterCart(true, i, j, k, bean.getGoodsId(), bean.getGpId(), "0", bean.getSupplierid());
    }

    @Override
    public void cartPlus(int i, int j, int k) {
        *//*CartGoodsListBean bean = list.get(i).getActivityList().get(j).getDataList().get(k);
        int plusNum = CartUtil.countPlusNum(bean.getGoodsNum(), bean.getGp_minnum(),
                bean.getGp_addnum(), bean.getGp_stock());
        if (plusNum == CartUtil.First) {
            UIUtils.Toast("数量有误");
            return;
        }
        if (plusNum == CartUtil.Two) {
            UIUtils.Toast("数量超出库存范围");
            return;
        } else {
            alterCart(false, i, j, k, bean.getGoodsId(), bean.getGpId(),
                    String.valueOf(plusNum), bean.getSupplierid());
        }*//*
    }

    @Override
    public void cartMinus(int i, int j, int k) {
        *//*CartGoodsListBean bean = list.get(i).getActivityList().get(j).getDataList().get(k);
        if (!bean.getGoodsNum().equals("0")) {
            int minusNum = CartUtil.countMinusNum(bean.getGoodsNum(), bean.getGp_minnum(),
                    bean.getGp_addnum(), bean.getGp_stock());
            alterCart(false, i, j, k, bean.getGoodsId(), bean.getGpId(),
                    String.valueOf(minusNum), bean.getSupplierid());
        }*//*
    }

    @Override
    public void cartEdit(final int i, final int j, final int k) {
        *//*final CartGoodsListBean bean = list.get(i).getActivityList().get(j).getDataList().get(k);
        DialogUtil.showEditCartNum(this, "加入购物车数量", "确定", "取消", bean.getGoodsNum(),
                bean.getGp_minnum(),bean.getGp_addnum(), bean.getGp_stock(), new DialogUtil.MyCustomDialogListener() {
                    @Override
                    public void ok(String str) {
                        alterCart(false, i, j, k, bean.getGoodsId(), bean.getGpId(),
                                str, bean.getSupplierid());
                    }

                    @Override
                    public void no() {
                    }
                });*//*
    }

    @Override
    public void cartLeft(int i, int j, int k) {
        LogUtils.e("cartLeft", "i:" + i + "--j:" + j + "--k:" + k);
        if (list.get(i).getActivityList().get(j).getDataList().get(k).getIsChild() == 0) {
            list.get(i).getActivityList().get(j).getDataList().get(k).setIsChild(1);
        } else if (list.get(i).getActivityList().get(j).getDataList().get(k).getIsChild() == 1) {
            list.get(i).getActivityList().get(j).getDataList().get(k).setIsChild(0);
        }

        boolean all = true;
        for (int l = 0; l < list.get(i).getActivityList().size(); l++) {
            for (int z = 0; z < list.get(i).getActivityList().get(l).getDataList().size(); z++) {
                if (list.get(i).getActivityList().get(l).getDataList().get(z).getIsChild() != 1) {
                    all = false;
                }
            }
        }

        if (all) {
            list.get(i).setIsAll(1);
        } else {
            list.get(i).setIsAll(0);
        }
        calculateCartPrice();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void cartChildItem(int i, int j, int k) {
        CartGoodsListBean bean = list.get(i).getActivityList().get(j).getDataList().get(k);
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        *//*intent.putExtra("gpId", bean.getGpId());
        intent.putExtra("cartNum", bean.getGoodsNum());*//*
        startActivity(intent);
    }
*/
    /**
     * 计算购物车价格
     */
    private void calculateCartPrice() {
        BigDecimal goodsTotalAmount = BigDecimal.ZERO;
        BigDecimal goodsTotalNum = BigDecimal.ZERO;

        for (int i = 0; i < list.size(); i++) {

            for (int k = 0; k < list.get(i).getActivityList().size(); k++) {

                for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size(); z++) {
                    if (list.get(i).getActivityList().get(k).getDataList().get(z).getIsChild() == 1) {
                        //BigDecimal goodsAmount = new BigDecimal(list.get(i).getActivityList().get(k).getDataList().get(z).getGoodsTotalAmount());
                        //goodsTotalAmount = goodsTotalAmount.add(goodsAmount);

                        //BigDecimal goodsNum = new BigDecimal(list.get(i).getActivityList().get(k).getDataList().get(z).getGoodsNum());
                       // goodsTotalNum = goodsTotalNum.add(goodsNum);
                    }
                }
            }

            list.get(i).setAmount(goodsTotalAmount.toString());
            list.get(i).setGoodsNum(goodsTotalNum.toString());
        }
    }

    /**
     * 清空购物车回调
     */
    class ClearCartListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (null == list || list.size() == 0) {
                return;
            } else {
                clearCart();
            }
        }
    }

    /**
     * 修改购物车数量
     *
     * @param goodsId
     * @param goodsNum
     * @param shopId
     */
    private void alterCart(final boolean isDelete, final int i, final int j, final int k,
                           String goodsId, String gp_id, final String goodsNum, String shopId) {
        AsyncHttpUtil<CartBean> httpAddCart = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (isDelete) {
                        //如果是删除商品
                        list.get(i).getActivityList().get(j).getDataList().remove(k);
                    } else {
                        if (bean.getGoodsCartNum().equals("0")) {
                            //如果本商品在购物车中的数量减到 0 了
                            list.get(i).getActivityList().get(j).getDataList().remove(k);
                        } else {
                           /* list.get(i).getActivityList().get(j).getDataList().get(k).setGoodsNum(bean.getGoodsCartNum());
                            list.get(i).getActivityList().get(j).getDataList().get(k).setGoodsTotalAmount(bean.getGoodsTotalAmount());*/
                        }
                    }

                    if (list.get(i).getActivityList().get(j).getDataList().size() == 0) {
                        list.get(i).getActivityList().remove(j);
                    }

                    if (list.get(i).getActivityList().size() == 0) {
                        list.remove(i);
                    }

                    if (list.size() == 0) {
                        CartAdapter.isSuccess = false;
                        mTitleBar.setRightText(getString(R.string.cart_edit));
                        mVaryViewHelper.showEmptyView();
                    } else {
                        //计算选中商品的总价格
                        calculateCartPrice();
                    }
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    adapter.notifyDataSetChanged();
                } else {
                    UIUtils.Toast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpAddCart.post(M_Url.addCart, L_RequestParams.
                alterCart(UserInfoUtils.getId(this), goodsId, gp_id, goodsNum, shopId, "",UserInfoUtils.getCenterShopId(this)), true, "请求中");
    }

    /**
     * 购物车商品移入收藏夹
     */
    private void addFavoriteBatch(String favoriteids, String cartIds) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean bean) {
                if (bean.getRepCode().equals("00")) {
                    //showToast(bean.getRepMsg());
                    loadCartList(PublicFinal.FIRST, false);
                }else if ("30".equals(bean.getRepCode())){
                    Intent intent = new Intent(ScanningPlacetheorderActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab",4);
                    startActivity(intent);
                    showToast(bean.getRepMsg());
                } else {
                    UIUtils.Toast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
       /* httpUtil.post(M_Url.addFavoriteBatch, L_RequestParams.addFavoriteBatch(UserInfoUtils.getId(this),
                "1", favoriteids, "1", cartIds,UserInfoUtils.getCenterShopId(this)), true);*/
    }

    /**
     * 删除购物车商品
     */
    private void deleteCart(final int position, final String cartIds) {
        AsyncHttpUtil<CartBean> httpUtil = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean jsonBean) {
                if (jsonBean.getRepCode().equals("00")) {
                    EventBus.getDefault().post(new CartEvent("", jsonBean.getTotalAmount()));
                    String[] strCartIds = cartIds.split(",");

                    for (int i = 0; i < list.get(position).getActivityList().size(); i++) {

                        for (int z = 0; z < list.get(position).getActivityList().get(i).getDataList().size(); z++) {

                            for (int k = 0; k < strCartIds.length; k++) {

                                /*if (list.get(position).getActivityList().get(i).getDataList().get(z).getCartId().equals(strCartIds[k])) {
                                    list.get(position).getActivityList().get(i).getDataList().remove(z);
                                }*/
                            }
                        }

                        if (list.get(position).getActivityList().get(i).getDataList().size() == 0) {
                            list.get(position).getActivityList().remove(i);
                        }

                    }

                    //                    for (int k = 0; k < list.get(i).getActivityList().size(); k++) {
                    //                        for (int z = 0; z < list.get(i).getActivityList().get(k).getDataList().size();z++){
                    //                            if (list.get(i).getActivityList().get(k).getDataList().get(z).getIsChild() == 1) {
                    //                                goodsCartId.append(list.get(i).getActivityList().get(k).getDataList().get(z).getCartId() + ",");
                    //                                favoriteIds.append(list.get(i).getActivityList().get(k).getDataList().get(z).getGpId() + ",");
                    //                                num++;
                    //                            }
                    //                        }
                    //                    }

                    if (list.get(position).getActivityList().size() == 0) {
                        list.remove(position);
                    }
                    if (list.size() == 0) {
                        CartAdapter.isSuccess = false;
                        mTitleBar.setRightText(getString(R.string.cart_edit));
                        mVaryViewHelper.showEmptyView();
                    } else {
                        //计算选中商品的总价格
                        calculateCartPrice();
                    }
                    adapter.notifyDataSetChanged();
                } else if ("30".equals(jsonBean.getRepCode())){
                    Intent intent = new Intent(ScanningPlacetheorderActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab",4);
                    startActivity(intent);
                    showToast(jsonBean.getRepMsg());
                }else {
                    UIUtils.Toast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        /*httpUtil.post(M_Url.deleteCart, L_RequestParams.getDeleteCart(
                UserInfoUtils.getId(this), cartIds,UserInfoUtils.getCenterShopId(this)), true);*/
    }

    /**
     * 清空购物车
     */
    public void clearCart() {
        AsyncHttpUtil<CartBean> httpCleart = new AsyncHttpUtil<>(this, CartBean.class, new IUpdateUI<CartBean>() {
            @Override
            public void updata(CartBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    loadCartList(PublicFinal.TWO, true);
                } else if ("30".equals(jsonBean.getRepCode())){
                    Intent intent = new Intent(ScanningPlacetheorderActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentTab",4);
                    startActivity(intent);
                    showToast(jsonBean.getRepMsg());
                }else {
                    UIUtils.Toast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                UIUtils.Toast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        /*httpCleart.post(M_Url.clearCart, L_RequestParams.
                getCleartCart(UserInfoUtils.getId(this),UserInfoUtils.getCenterShopId(this)), true);*/
    }

    /**
     * 加载失败点击事件
     */
    class onErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadCartList(PublicFinal.FIRST, false);
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        //UIUtils.Toast(rawResult.getText());
        addCart(rawResult.getText());
        // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                if (handler == null) {
        //                    handler = new CaptureActivityHandler(ScanningPlacetheorderActivity.this, cameraManager, DecodeThread.ALL_MODE);
        //                }
        //                handler.sendEmptyMessage(R.id.restart_preview);
        //            }
        //        }, 5000);
    }


    @Override
    public void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(ScanningPlacetheorderActivity.this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 *//*
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        //int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        //int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        //int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        //int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        //mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
