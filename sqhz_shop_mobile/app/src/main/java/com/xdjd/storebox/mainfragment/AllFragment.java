package com.xdjd.storebox.mainfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.CartActivity;
import com.xdjd.storebox.activity.CouponActivity;
import com.xdjd.storebox.activity.MyCollectActivity;
import com.xdjd.storebox.activity.MyOrderActivity;
import com.xdjd.storebox.activity.OftenBuyActivity;
import com.xdjd.storebox.activity.SalesActivity;
import com.xdjd.storebox.activity.ScanningPlacetheorderActivity;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.GridItemBean;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollGridView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部功能fragment
 * Created by lijipei on 2016/11/16.
 */

public class AllFragment extends BaseFragment {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    //    @BindView(R.id.all_rl)
    //    RelativeLayout mAllRl;
    @BindView(R.id.retail_gridview)
    NoScrollGridView mRetailGridview;
    @BindView(R.id.purchase_gridview)
    NoScrollGridView mPurchaseGridview;
    @BindView(R.id.centerShopName_tv)
    TextView mCenterShopNameTv;
    @BindView(R.id.spread_name)
    TextView mSpreadName;
    @BindView(R.id.spread_mobile)
    TextView mSpreadMobile;
    @BindView(R.id.spread_mobile_ll)
    LinearLayout mSpreadMobileLl;
    @BindView(R.id.spread_ll)
    LinearLayout mSpreadLl;
    @BindView(R.id.retail_ll)
    LinearLayout mRetailLl;
    //    @BindView(R.id.centerShopName_ll)
    //    LinearLayout mCenterShopNameLl;

    private MainActivity mMainActivity;

    /**
     * 零售
     */
    private String[] retailName = new String[]{"待送货", "待取货", "送货中", "已完成",
            "已取消", "我的收益", "顾客评价", "运营报表", "商品管理", "库存盘点"};//"收银对账"
    private int[] retailImg = new int[]{R.drawable.retail_for_delivery, R.drawable.retail_for_pickup, R.drawable.in_the_delivery,
            R.drawable.retail_complete, R.drawable.retail_cancel, R.drawable.retail_earnings, R.drawable.retail_customer_evaluation,
            R.drawable.retail_operating_reports, R.drawable.retail_goods_mange, R.drawable.retail_stock_taking};//, R.drawable.retail_cashier_check
    private RetailGridAdapter retailAdapter;

    /**
     * 零售数据列表
     */
    private List<GridItemBean> listRetail = new ArrayList<>();

    /**
     * 在线采购
     */
    private String[] purchaseName = new String[]{"统配进货", "预售进货", "热门活动", "优惠券",
            "待付款", "待收货", "已完成", "购物车", "我常买","扫码下单", "我的收藏"};
    private int[] purchaseImg = new int[]{R.drawable.stock, R.drawable.in_the_delivery, R.drawable.popular_activities,
            R.drawable.coupon, R.drawable.for_the_payment, R.drawable.for_the_goods, R.drawable.purchase_complete,
            R.drawable.cart, R.drawable.wochangmai,R.mipmap.qr_place_an_order, R.drawable.my_collect};
    private PurchaseGridAdapter purchaseAdapter;

    /**
     * 在线采购数据列表
     */
    private List<GridItemBean> listPurchase = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mTitleBar.setTitle("全部功能");
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        //判断是否要运行此方法里的内容
        if (!(YesOrNoLoadingOnstart.INDEX = true && YesOrNoLoadingOnstart.INDEX_ID == 4)) {
            return;
        }

        if (!UserInfoUtils.getSpreadName(getActivity()).equals("")) {
            mSpreadLl.setVisibility(View.VISIBLE);
            mSpreadName.setText(UserInfoUtils.getSpreadName(getActivity()));
            mSpreadMobile.setText(UserInfoUtils.getSpreadMobile(getActivity()));
        } else {
            mSpreadLl.setVisibility(View.GONE);
        }

        if (!UserInfoUtils.getCenterShopName(getActivity()).equals("")) {
            //            mCenterShopNameLl.setVisibility(View.VISIBLE);
            mCenterShopNameTv.setText(UserInfoUtils.getCenterShopName(getActivity()));
            mCenterShopNameTv.setVisibility(View.VISIBLE);
        } else {
            mCenterShopNameTv.setVisibility(View.GONE);
        }

        if (UserInfoUtils.getUserShopId(getActivity()).equals("0")){
            mRetailLl.setVisibility(View.GONE);
        }else{
            mRetailLl.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAllRl
        //                    .getLayoutParams();
        //            params.height = (int) (UIUtils.getStatusBarHeight());
        //            mAllRl.setLayoutParams(params);
        //            //mAllRl.setPadding(0, UIUtils.getStatusBarHeight(), 0, 0);
        //        }

        mMainActivity = (MainActivity) getActivity();

        retailAdapter = new RetailGridAdapter();
        mRetailGridview.setAdapter(retailAdapter);

        purchaseAdapter = new PurchaseGridAdapter();
        mPurchaseGridview.setAdapter(purchaseAdapter);

        if (!UserInfoUtils.getSpreadName(getActivity()).equals("")) {
            mSpreadLl.setVisibility(View.VISIBLE);
            mSpreadName.setText(UserInfoUtils.getSpreadName(getActivity()));
            mSpreadMobile.setText(UserInfoUtils.getSpreadMobile(getActivity()));
        } else {
            mSpreadLl.setVisibility(View.GONE);
        }

        if (!UserInfoUtils.getCenterShopName(getActivity()).equals("")) {
            //            mCenterShopNameLl.setVisibility(View.VISIBLE);
            mCenterShopNameTv.setText(UserInfoUtils.getCenterShopName(getActivity()));
            mCenterShopNameTv.setVisibility(View.VISIBLE);
        } else {
            mCenterShopNameTv.setVisibility(View.GONE);
        }

        if (UserInfoUtils.getUserShopId(getActivity()).equals("0")){
            mRetailLl.setVisibility(View.GONE);
        }else{
            mRetailLl.setVisibility(View.VISIBLE);
        }


    }

    @OnClick(R.id.spread_mobile_ll)
    public void onClick() {
        DialogUtil.showCustomDialog(getActivity(), UserInfoUtils.getSpreadName(getActivity()),
                UserInfoUtils.getSpreadMobile(getActivity()), "拨打",
                "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + UserInfoUtils.getSpreadMobile(getActivity())));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void no() {
                    }
                });

    }

    /**
     * 零售适配器
     */
    public class RetailGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return retailName.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_grid, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
                AutoUtils.auto(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.mAllGridImg.setImageResource(retailImg[i]);
            holder.mAllGridName.setText(retailName[i]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0:
                            break;
                    }
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView mAllGridImg;
            @BindView(R.id.all_grid_name)
            TextView mAllGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /**
     * 采购适配器
     */
    public class PurchaseGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return purchaseName.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_grid, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
                AutoUtils.auto(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.mAllGridImg.setImageResource(purchaseImg[i]);
            holder.mAllGridName.setText(purchaseName[i]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0:
                            mMainActivity.toPurchase(0);
                            break;
                        case 1:
                            mMainActivity.toPurchase(1);
                            break;
                        case 2://活动列表
                            startActivity(SalesActivity.class);
                            break;
                        case 3://优惠券
                            startActivity(CouponActivity.class);
                            break;
                        case 4://代付款
                            tiao(1);
                            break;
                        case 5://待收货
                            tiao(2);
                            break;
                        case 6://已完成
                            tiao(3);
                            break;
                        case 7://修改为跳转购物车
                            startActivity(CartActivity.class);
                            break;
                        case 8://我常买
                            startActivity(OftenBuyActivity.class);
                            break;
                        case 10://我的收藏
                            startActivity(MyCollectActivity.class);
                            break;
                        case 9://扫码下单
                            Intent intent = new Intent(getActivity(),ScanningPlacetheorderActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            });
            return view;
        }

        private void tiao(int index) {
            Intent intent = new Intent(getActivity(), MyOrderActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }

        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView mAllGridImg;
            @BindView(R.id.all_grid_name)
            TextView mAllGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


}
