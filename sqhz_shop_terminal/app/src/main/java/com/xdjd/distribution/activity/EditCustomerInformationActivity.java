package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.MapPositionEvent;
import com.xdjd.distribution.event.OpenMallEvent;
import com.xdjd.distribution.event.PicturesEvent;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.distribution.popup.SelectGoodsPriceGradePopup;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.EaseTitleBar;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/2
 *     desc   : 编辑客户信息activity
 *     version: 1.0
 * </pre>
 */

public class EditCustomerInformationActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.rl_alert_main)
    RelativeLayout mRlAlertMain;
    @BindView(R.id.rl_wx_open)
    RelativeLayout mRlWxOpen;
    @BindView(R.id.rl_mobile_open)
    RelativeLayout mRlMobileOpen;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.et_cc_name)
    EditText mEtCcName;
    @BindView(R.id.et_contacts_name)
    EditText mEtContactsName;
    @BindView(R.id.et_contacts_mobile)
    EditText mEtContactsMobile;
    @BindView(R.id.mobile_del)
    LinearLayout mMobileDel;
    @BindView(R.id.cs_image)
    CircleImageView mCsImage;
    @BindView(R.id.cs_shop_photo)
    LinearLayout mCsShopPhoto;
    @BindView(R.id.tv_shop_account)
    TextView mTvShopAccount;
    @BindView(R.id.ll_shop_account)
    LinearLayout mLlShopAccount;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.ll_wx)
    LinearLayout mLlWx;
    @BindView(R.id.tv_store)
    TextView mTvStore;
    @BindView(R.id.tv_category)
    TextView mTvCategory;
    @BindView(R.id.tv_channel)
    TextView mTvChannel;
    @BindView(R.id.tv_position_str)
    TextView mTvPositionStr;
    @BindView(R.id.ll_location)
    LinearLayout mLlLocation;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.ll_main)
    RelativeLayout mLlMain;
    @BindView(R.id.ll_open_store)
    LinearLayout mLlOpenStore;
    @BindView(R.id.ll_store_main)
    LinearLayout mLlStoreMain;
    @BindView(R.id.tv_open_mall)
    LinearLayout mTvOpenMall;
    @BindView(R.id.tv_isopen_store)
    TextView mTvIsopenStore;
    @BindView(R.id.tv_price_grade)
    TextView mTvPriceGrade;

    /**
     * 选择的客户信息
     */
    private ClientBean customerBean;
    private UserBean userBean;

    private Bitmap bitmap;
    private String imagePath = "";//门店图片

    private String cc_categoryid = "";//	类别id
    private String cc_categoryid_nameref = "";//	类别名
    private String cc_channelid = "";//	渠道id
    private String cc_channelid_nameref = "";//	渠道名

    private String cc_depotid = "";//	客户仓库id
    private String cc_depotid_nameref = "";//	客户仓库名

    private String gradeId;//价格id
    private String gradeName;//价格方案名称

    /**
     * 仓库列表
     */
    private List<StorehouseBean> listStore;
    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;

    /**
     * 价格等级popup
     */
    private SelectGoodsPriceGradePopup gradePopup;
    private List<AddInfoBean> listGrade;

    /**
     * 纬度
     */
    private String latitude = "";
    /**
     * 经度
     */
    private String longtitude = "";
    /**
     * 定位后的地址
     */
    private String address = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_customer_information;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("编辑客户信息");
    }

    @Override
    protected void initData() {
        super.initData();
        customerBean = (ClientBean) getIntent().getSerializableExtra("clientBean");//UserInfoUtils.getClientInfo(this);
        userBean = UserInfoUtils.getUser(this);

        mEtCcName.setText(customerBean.getCc_name());
        mEtContactsName.setText(customerBean.getCc_contacts_name());
        mEtContactsMobile.setText(customerBean.getCc_contacts_mobile());
        if (customerBean.getCc_image() != null && customerBean.getCc_image().length() > 0) {
            Glide.with(this).
                    load(customerBean.getCc_image()).
                    error(R.mipmap.photo).
                    into(mCsImage);
            imagePath = customerBean.getCc_image().substring(customerBean.getCc_image().lastIndexOf("image") + "image".length());
            LogUtils.e("imagePath", imagePath);
        }

        mTvStore.setText(customerBean.getCc_depotid_nameref());
        mTvCategory.setText(customerBean.getCc_categoryid_nameref());
        mTvChannel.setText(customerBean.getCc_channelid_nameref());
        mTvPriceGrade.setText(customerBean.getCc_goods_gradeid_nameref());
        mEtAddress.setText(customerBean.getCc_address());

        latitude = customerBean.getCc_latitude();
        longtitude = customerBean.getCc_longitude();
        address = customerBean.getCc_address();
        cc_categoryid = customerBean.getCc_categoryid();
        cc_categoryid_nameref = customerBean.getCc_categoryid_nameref();
        cc_channelid = customerBean.getCc_channelid();
        cc_channelid_nameref = customerBean.getCc_channelid_nameref();
        cc_depotid = customerBean.getCc_depotid();
        cc_depotid_nameref = customerBean.getCc_depotid_nameref();

        gradeId = customerBean.getCc_goods_gradeid();
        gradeName = customerBean.getCc_goods_gradeid_nameref();

        if ("Y".equals(userBean.getIsShopAccount())) {
            mTvOpenMall.setVisibility(View.VISIBLE);
            if ("Y".equals(customerBean.getCc_isaccount())) {//是否开通商城
                mTvIsopenStore.setText("已开通");
                mLlStoreMain.setVisibility(View.VISIBLE);
                if (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0) {
                    mTvWx.setText("未绑定");
                } else {
                    mTvWx.setText("已绑定");
                }
            } else {
                mTvIsopenStore.setText("未开通");
                mLlStoreMain.setVisibility(View.GONE);
            }
        }

        initStorePopup();
        initPwPriceGrade();

        getAddInfo(false);
        queryStorehouseList(false);

        //        getCustomerInfo(customerBean.getCc_id(), false);
    }

    @OnClick({R.id.rl_wx_open, R.id.rl_mobile_open, R.id.iv_close, R.id.rl_alert_main,
            R.id.tv_store, R.id.tv_category, R.id.tv_channel, R.id.btn_submit, R.id.cs_shop_photo, R.id.ll_location
            , R.id.ll_open_store, R.id.ll_wx,R.id.tv_price_grade})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_wx_open://微信开通商城
                startOpenAnimation(false);
                showQrcodeImg();
                break;
            case R.id.rl_mobile_open://手机开通商城
                if (customerBean.getCc_contacts_mobile() == null || "".equals(customerBean.getCc_contacts_mobile())) {
                    DialogUtil.showCustomDialog(this, "提示", "请先去完善客户手机号码,再开通商城!", "确定", null, null);
                    return;
                }
                intent = new Intent(this, OpenMallActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_close:
                startOpenAnimation(false);
                break;
            case R.id.rl_alert_main:
                startOpenAnimation(false);
                break;
            case R.id.cs_shop_photo://客户门店照片
                PermissionUtils.requstActivityCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(EditCustomerInformationActivity.this, PicturesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type", 3);
                        startActivity(intent);
                    }

                    @Override
                    public void onDilogCancal() {
                        showToast("获取相机权限失败!");
                    }
                });
                break;
            /*case R.id.ll_open_store://开通商城
                if (!"Y".equals(customerBean.getCc_isaccount())) {
                    if (mRlAlertMain.getVisibility() == View.INVISIBLE) {
                        mRlAlertMain.setVisibility(View.VISIBLE);
                        startOpenAnimation(true);
                    } else {
                        startOpenAnimation(false);
                    }
                } else {
                    showToast("已经开通商城!");
                }
                break;*/
            case R.id.tv_category://客户类别
                intent = new Intent(this, SelectCategoryChannelActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_channel:
                intent = new Intent(this, SelectCategoryChannelActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 200);
                break;
            case R.id.ll_location://定位
                intent = new Intent(this, MapLocationActivity.class);
                startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                break;
            case R.id.tv_store://选择仓库
                if (listStore == null || listStore.size() == 0) {
                    queryStorehouseList(true);
                } else {
                    showPwStore();
                }
                break;
            case R.id.tv_price_grade://选择价格方案
                if (listStore == null || listStore.size() == 0) {
                    getAddInfo(true);
                } else {
                    showPwPriceGrade();
                }
                break;
            case R.id.btn_submit:
                updateLocation();
                break;
           /* case R.id.ll_wx:
                if (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0) {
                    showQrcodeImg();
                }
                break;*/
        }
    }

    private void showQrcodeImg() {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    Intent intent = new Intent(EditCustomerInformationActivity.this, CommonWebActivity.class);
                    intent.putExtra("title", "绑定微信");
                    intent.putExtra("url", jsonBean.getImgUrl());
                    startActivityForResult(intent, 100);
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.showQrcodeImg, L_RequestParams.showQrcodeImg(customerBean.getCc_id()), true);
    }

    /**
     * 更新定位
     */
    private void updateLocation() {
        if ("".equals(latitude) || latitude == null) {
            showToast("请重新定位");
            return;
        }

        if ("".equals(cc_categoryid)) {
            showToast("请选择客户类型信息!");
            return;
        }

        if ("".equals(cc_channelid)) {
            showToast("请选择客户渠道信息!");
            return;
        }

        if (TextUtils.isEmpty(mEtContactsName.getText())) {
            showToast("请输入联系人姓名");
            return;
        }

        if (TextUtils.isEmpty(mEtContactsMobile.getText())) {
            showToast("请输入联系人电话");
            return;
        }

//        if (cc_depotid == null || "".equals(cc_depotid)) {
//            showToast("请选择客户默认出货仓库");
//            return;
//        }

        if (gradeId == null || "".equals(gradeId)) {
            showToast("请选择价格方案");
            return;
        }

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {

                    customerBean.setCc_name(mEtCcName.getText().toString());
                    customerBean.setCc_latitude(latitude);
                    customerBean.setCc_longitude(longtitude);
                    customerBean.setCc_address(mEtAddress.getText().toString());
                    customerBean.setCc_contacts_name(mEtContactsName.getText().toString());//客户姓名
                    customerBean.setCc_contacts_mobile(mEtContactsMobile.getText().toString());//客户电话
                    customerBean.setCc_categoryid(cc_categoryid);
                    customerBean.setCc_categoryid_nameref(cc_categoryid_nameref);
                    customerBean.setCc_channelid(cc_channelid);
                    customerBean.setCc_channelid_nameref(cc_channelid_nameref);
                    customerBean.setCc_depotid(cc_depotid);
                    customerBean.setCc_depotid_nameref(cc_depotid_nameref);

                    customerBean.setCc_goods_gradeid(gradeId);
                    customerBean.setCc_goods_gradeid_nameref(gradeName);

                    /*if (UserInfoUtils.getClientInfo(EditCustomerInformationActivity.this).
                            getCc_id().equals(customerBean.getCc_id())){
                        UserInfoUtils.setClientInfo(EditCustomerInformationActivity.this, customerBean);
                    }*/
                    EventBus.getDefault().post(new OpenMallEvent(customerBean));
                    EventBus.getDefault().post(new SignClientEvent());
                    finishActivity();
                    showToast(jsonBean.getRepMsg());
                    //                    getCustomerInfo(customerBean.getCc_id(), true);
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.updateLocation, L_RequestParams.updateLocation(mEtCcName.getText().toString(),
                UserInfoUtils.getLineId(this), customerBean.getCc_id(), longtitude, latitude, address,
                mEtContactsName.getText().toString(), mEtContactsMobile.getText().toString(), cc_categoryid, cc_channelid, cc_depotid,
                mEtAddress.getText().toString(), imagePath,gradeId), true);
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo(String cusId, final boolean isFinish) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mEtContactsMobile.setText(jsonBean.getCc_contacts_mobile());
                    customerBean.setCc_image(jsonBean.getCc_image());
                    customerBean.setCc_openid(jsonBean.getCc_openid());
                    customerBean.setCc_isaccount(jsonBean.getCc_isaccount());
                    //                    UserInfoUtils.setClientInfo(EditCustomerInformationActivity.this, customerBean);
                    EventBus.getDefault().post(new OpenMallEvent(customerBean));
                    if ("Y".equals(userBean.getIsShopAccount())) {
                        mTvOpenMall.setVisibility(View.VISIBLE);
                        if ("Y".equals(customerBean.getCc_isaccount())) {//是否开通商城
                            mTvIsopenStore.setText("已开通");
                            mLlStoreMain.setVisibility(View.VISIBLE);
                            if (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0) {
                                mTvWx.setText("未绑定");
                            } else {
                                mTvWx.setText("已绑定");
                            }
                        } else {
                            mTvIsopenStore.setText("未开通");
                            mLlStoreMain.setVisibility(View.GONE);
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                if (isFinish) {
                    EventBus.getDefault().post(new SignClientEvent());
                    finishActivity();
                }
            }
        });
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(this) + "",
                cusId), true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Comon.QR_GOODS_REQUEST_CODE == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {
            address = data.getStringExtra("address");
            latitude = data.getStringExtra("latitude");
            longtitude = data.getStringExtra("longtitude");
            //            mTvLng.setText(longtitude);
            //            mTvLat.setText(latitude);
            mEtAddress.setText(address);
        } else if (resultCode == 10) {
            AddInfoBean bean = (AddInfoBean) data.getSerializableExtra("addInfo");
            switch (requestCode) {
                case 100:
                    cc_categoryid = bean.getCc_id();
                    cc_categoryid_nameref = bean.getCc_name();
                    mTvCategory.setText(bean.getCc_name());
                    break;
                case 200:
                    cc_channelid = bean.getCct_id();
                    cc_channelid_nameref = bean.getCct_name();
                    mTvChannel.setText(bean.getCct_name());
                    break;
            }
        } else if (resultCode == 20) {
            getCustomerInfo(customerBean.getCc_id(), false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(MapPositionEvent event) {
        mTvPositionStr.setText("重新定位");
    }

    /**
     * 传递图片信息event
     *
     * @param event
     */
    public void onEventMainThread(PicturesEvent event) {
        try {
            imagePath = event.getResultPath();
            byte[] b = Base64.decodeBase64(event.getUrl().getBytes("UTF-8"));
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
            mCsImage.setImageBitmap(bitmap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取仓库列表接口
     */
    private void queryStorehouseList(final boolean isDialog) {
        AsyncHttpUtil<StorehouseBean> httpUtil = new AsyncHttpUtil<>(this, StorehouseBean.class, new IUpdateUI<StorehouseBean>() {
            @Override
            public void updata(StorehouseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listStore = jsonBean.getListData();
                    if (listStore != null && listStore.size() > 0) {
                        if (isDialog) {
                            showPwStore();
                        } else {
                            //                            if (cc_depotid == null || cc_depotid.length() == 0){
                            //                                cc_depotid = listStore.get(0).getBs_id();
                            //                                cc_depotid_nameref = listStore.get(0).getBs_name();
                            //                                mTvStore.setText(cc_depotid_nameref);
                            //                            }
                        }
                    }
                } else {
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.queryStorehouseList,
                L_RequestParams.queryStorehouseList("", "", "3"), isDialog);
    }

    /**
     * 获取价格等级接口
     */
    private void getAddInfo(final boolean isDialog) {
        AsyncHttpUtil<AddInfoBean> httpUtil = new AsyncHttpUtil<>(this, AddInfoBean.class, new IUpdateUI<AddInfoBean>() {
            @Override
            public void updata(AddInfoBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listGrade = jsonBean.getPriceGradeList();
                    if (listGrade != null && listGrade.size() > 0) {
                        if (isDialog) {
                            showPwPriceGrade();
                        } else {
                        }
                    } else {
                    }
                } else {
                    if (isDialog) {
                        showToast(jsonBean.getRepMsg());
                    }
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getAddInfo, L_RequestParams.getAddInfo(), isDialog);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                cc_depotid = listStore.get(position).getBs_id();
                cc_depotid_nameref = listStore.get(position).getBs_name();
                mTvStore.setText(cc_depotid_nameref);
                popupStore.dismiss();
            }
        });
    }

    /**
     * 显示仓库popup
     */
    private void showPwStore() {
        popupStore.setData(listStore);
        popupStore.setId(cc_depotid);
        // 显示窗口
        popupStore.showAtLocation(mLlMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    /**
     * 初始化价格等级popup
     */
    private void initPwPriceGrade() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        gradePopup = new SelectGoodsPriceGradePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                gradeId = listGrade.get(position).getGpg_id();
                gradeName = listGrade.get(position).getGpg_title();
                mTvPriceGrade.setText(gradeName);
                gradePopup.dismiss();
            }
        });
        gradePopup.setData(listGrade);
    }

    /**
     * 显示价格等级popup
     */
    private void showPwPriceGrade() {
        gradePopup.setId(gradeId);
        gradePopup.setData(listGrade);
        // 显示窗口
        gradePopup.showAtLocation(mLlMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void startOpenAnimation(final boolean b) {
        if (b) {
            if (mRlWxOpen.getVisibility() == View.INVISIBLE) {
                mRlWxOpen.setVisibility(View.VISIBLE);
                startTranstateAnimation(mRlWxOpen, true, 2);
            }
        } else {
            mRlAlertMain.setEnabled(false);
            startTranstateAnimation(mRlWxOpen, false, 2);
            startTranstateAnimation(mRlMobileOpen, false, 2);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
            alphaAnimation.setDuration(600);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRlAlertMain.setEnabled(true);
                    mRlAlertMain.setVisibility(View.INVISIBLE);

                    mRlWxOpen.setVisibility(View.INVISIBLE);
                    mRlMobileOpen.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mRlAlertMain.startAnimation(alphaAnimation);
        }
    }


    private void startTranstateAnimation(final View v, boolean open, int position) {
        if (open) {
            LogUtils.e("startTranstateAnimation", "Open.getY()=" + mRlWxOpen.getY() + ",v.getY()=" + v.getY());
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "y",
                    mRlWxOpen.getY(),
                    v.getY() - UIUtils.dp2px(20),
                    v.getY() + UIUtils.dp2px(10),
                    v.getY()).setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (v.getId() == mRlWxOpen.getId()) {
                        if (mRlMobileOpen.getVisibility() == View.INVISIBLE) {
                            mRlMobileOpen.setVisibility(View.VISIBLE);
                            startTranstateAnimation(mRlMobileOpen, true, 3);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mIvClose.setEnabled(true);
        } else {
            mIvClose.setEnabled(false);
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_PARENT, 0.4f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(500);
            animationSet.addAnimation(translateAnimation);
            animationSet.setFillAfter(false);
            v.startAnimation(animationSet);
        }
    }

}
