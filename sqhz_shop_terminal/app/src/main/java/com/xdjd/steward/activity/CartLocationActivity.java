package com.xdjd.steward.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.SelectSalesmanPopup;
import com.xdjd.steward.bean.ReportTaskBean;
import com.xdjd.steward.bean.SalesLocationBean;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.clusterutil.clustering.Cluster;
import com.xdjd.utils.clusterutil.clustering.ClusterItem;
import com.xdjd.utils.clusterutil.clustering.ClusterManager;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CartLocationActivity extends BaseActivity implements SelectSalesmanPopup.SalesmanSearchListener,BaiduMap.OnMapLoadedCallback {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_company_location)
    TextView mTvCompanyLocation;
    @BindView(R.id.tv_staff_location)
    TextView mTvStaffLocation;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private int currentTab = 0; // 当前Tab页面索引

    private MapStatus ms;

    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;
    private Marker mMarkerCompany;
    //公司经纬度
    private LatLng llCompany = null;
    //公司位置标注
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.img_stock);

    // 普通折线
    Polyline mPolyline;

    private UserBean mUserBean;
    /**
     * 业务员列表
     */
    private List<SalesdocListBean> listSalesman;

    /**
     * 业务员选择popup
     */
    private SelectSalesmanPopup popupSalesman;

    /**
     * 业务员
     */
    private String salesid = "";
    /**
     * 业务员名称
     */
    private String salesName = "";
    //当天时间
    private String dateStr;

    private ClusterManager<MyItem> mClusterManager;
    private List<SalesLocationBean> listSalesLocation;

    @Override
    protected int getContentView() {
        return R.layout.activity_cart_location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("车辆位置");
        mTitleBar.setRightImageResource(R.mipmap.search);
        mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSalesman == null || listSalesman.size() == 0) {
                    querySalesmanList(true, 1, "");
                } else {
                    showPwSalesman();
                }
            }
        });

        mUserBean = UserInfoUtils.getUser(this);
        dateStr = DateUtils.getDataTime(DateUtils.dateFormater2);

//        mBaiduMap = mMap.getMap();
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
//        mBaiduMap.setMapStatus(msu);

        mBaiduMap = mMap.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);

        if (mUserBean.getCompanyLocation() != null && !"".equals(mUserBean.getCompanyLocation())) {
            String[] locatingCompany = mUserBean.getCompanyLocation().split(",");
            double lat = Double.parseDouble(locatingCompany[1].toString());
            double lon = Double.parseDouble(locatingCompany[0].toString());
            llCompany = new LatLng(lat, lon);

            ms = new MapStatus.Builder().target(llCompany).zoom(8).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
//            initOverlay();
//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(llCompany).zoom(18.0f);
//            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        } else {
            showToast("还没有设置公司位置!");
        }

        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
//                Toast.makeText(CartLocationActivity.this,
//                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
//                Toast.makeText(CartLocationActivity.this,
//                        "点击单个Item", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        initSalesmanPopup();
        moveAnimation(mTvStaffLocation);
    }

    /**
     * 初始换公司覆盖物
     */
    private void initOverlay() {
        try {
            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(bd);
            MarkerOptions ooD = new MarkerOptions().position(llCompany).icons(giflist)
                    .zIndex(0).period(10);
            mMarkerCompany = (Marker) (mBaiduMap.addOverlay(ooD));

            MapStatusUpdate u = MapStatusUpdateFactory
                    .newLatLng(llCompany);
            mBaiduMap.setMapStatus(u);

            TextView tvCompany = new Button(getApplicationContext());
            tvCompany.setBackgroundResource(R.mipmap.popup);
            tvCompany.setText(mUserBean.getCompanyName());
            LatLng ll = mMarkerCompany.getPosition();
            mInfoWindow = new InfoWindow(tvCompany, ll, -47);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.tv_company_location, R.id.tv_staff_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_company_location:
                currentTab = 1;

                mTvCompanyLocation.setTextColor(UIUtils.getColor(R.color.white));
                mTvStaffLocation.setTextColor(UIUtils.getColor(R.color.text_gray));

                moveAnimation(mTvCompanyLocation);
                break;
            case R.id.tv_staff_location:
                currentTab = 0;

                mTvStaffLocation.setTextColor(UIUtils.getColor(R.color.white));
                mTvCompanyLocation.setTextColor(UIUtils.getColor(R.color.text_gray));

                moveAnimation(mTvStaffLocation);
                break;
        }
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.addListener(animatorListener);
        animator.setDuration(400).start();
    }


    private MyAnimatorListener animatorListener = new MyAnimatorListener();

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    public class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (currentTab == 0) {
                mBaiduMap.clear();
                getSalesLocation();
            } else {
                mBaiduMap.clear();
                if (mUserBean.getCompanyLocation() != null && !"".equals(mUserBean.getCompanyLocation())) {
                    initOverlay();
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(llCompany).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
            animator.removeListener(animatorListener);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    /**
     * 获取所有业务员的位置
     */
    private void getSalesLocation() {
        AsyncHttpUtil<SalesLocationBean> httpUtil = new AsyncHttpUtil<>(this, SalesLocationBean.class, new IUpdateUI<SalesLocationBean>() {
            @Override
            public void updata(SalesLocationBean bean) {
                if ("00".equals(bean.getRepCode())) {

                    if (bean.getListData()!=null && bean.getListData().size()>0){
                        if (mUserBean.getCompanyLocation() != null && !"".equals(mUserBean.getCompanyLocation())){
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                        }else{
                            ms = new MapStatus.Builder().target(new LatLng(bean.getListData().get(0).getClct_latitude(),
                                    bean.getListData().get(0).getClct_longitude())).zoom(8).build();
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                        }
                        addMarkers(bean.getListData());//绘制点聚合功能方法
                    }

                    /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i = 0; i < bean.getListData().size(); i++) {
                        SalesLocationBean bean1 = bean.getListData().get(i);

                        Marker marker;
                        LatLng llStaff;

                        //添加位置信息接口
                        LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_staff, null);
                        LinearLayout llMain = (LinearLayout) llPopup.findViewById(R.id.ll_main);
                        TextView tvName = (TextView) llPopup.findViewById(R.id.tv_staff_name);
                        TextView tvTime = (TextView) llPopup.findViewById(R.id.tv_time);
                        TextView tvState = (TextView) llPopup.findViewById(R.id.tv_state);

                        tvName.setText(bean1.getClct_userid_nameref());
                        tvTime.setText(StringUtils.getDate(bean1.getClct_arrivetime(), "HH:mm"));
                        tvState.setText("(当前)");

                        BitmapDescriptor bd = BitmapDescriptorFactory
                                .fromView(llPopup);

                        llStaff = new LatLng(bean1.getClct_latitude(), bean1.getClct_longitude());
                        builder = builder.include(llStaff);

                        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
                        giflist.add(bd);
                        MarkerOptions ooD = new MarkerOptions().position(llStaff).icons(giflist)
                                .zIndex(0).period(10);
                        marker = (Marker) (mBaiduMap.addOverlay(ooD));

                    }

                    if (llCompany != null)
                        builder = builder.include(llCompany);

                    LatLngBounds latlngBounds = builder.build();
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMap.getWidth(), mMap.getHeight());
                    mBaiduMap.animateMapStatus(u);*/

//                    initOverlay();
                } else {
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.getSalesLocation, G_RequestParams.getSalesLocation(), true);
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers(List<SalesLocationBean> list) {
        listSalesLocation = list;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        mClusterManager.clearItems();
        List<MyItem> items = new ArrayList<MyItem>();
        for (int i = listSalesLocation.size() - 1; i >= 0; i--) {
            LatLng ll = new LatLng(listSalesLocation.get(i).getClct_latitude(),listSalesLocation.get(i).getClct_longitude());
            items.add(new MyItem(ll, i));

            builder = builder.include(ll);
        }

        if (mUserBean.getCompanyLocation() != null && !"".equals(mUserBean.getCompanyLocation())) {
            items.add(new MyItem(llCompany, -1));
        }

        mClusterManager.addItems(items);

        //将所有的点都显示在屏幕上
        if (llCompany != null)
            builder = builder.include(llCompany);

        LatLngBounds latlngBounds = builder.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMap.getWidth(), mMap.getHeight());
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onSearch(int salesmanType, String searchStr) {
        //搜索员工
        querySalesmanList(true, salesmanType, searchStr);
    }

    /**
     * 获取业务员列表接口
     */
    private void querySalesmanList(final boolean isDialog, int salesmanType, String searchStr) {
        AsyncHttpUtil<SalesdocListBean> httpUtil = new AsyncHttpUtil<>(this, SalesdocListBean.class, new IUpdateUI<SalesdocListBean>() {
            @Override
            public void updata(SalesdocListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSalesman = jsonBean.getDataList();
                    if (listSalesman != null && listSalesman.size() > 0) {
                        showPwSalesman();
                    } else {
                        showToast("没有业务员数据");
                    }
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
        httpUtil.post(M_Url.getSalesdocList,
                L_RequestParams.getSalesdocList(searchStr, String.valueOf(salesmanType)), isDialog);
    }

    /**
     * 初始化业务员popup
     */
    private void initSalesmanPopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupSalesman = new SelectSalesmanPopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                salesid = listSalesman.get(position).getSu_id();
                salesName = listSalesman.get(position).getSu_name();
                popupSalesman.dismiss();

                if (salesid == null) {
                    salesid = "";
                    showToast("该员工信息有误!");
                    return;
                }

                loadData();
            }
        }, this);
    }

    /**
     * 显示员工popup
     */
    private void showPwSalesman() {
        if (!popupSalesman.isShowing()) {
            popupSalesman.setData(listSalesman);
            popupSalesman.setId(salesid);
            // 显示窗口
            popupSalesman.showAtLocation(mLlMain,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        } else {
            popupSalesman.setData(listSalesman);
            popupSalesman.setId(salesid);
        }
    }

    /**
     * 加载拜访明细
     */
    private void loadData() {
        AsyncHttpUtil<ReportTaskBean> httpUtil = new AsyncHttpUtil<>(this, ReportTaskBean.class, new IUpdateUI<ReportTaskBean>() {
            @Override
            public void updata(ReportTaskBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        mBaiduMap.clear();
                        initOverlay();
                        drawLine(jsonStr.getDataList());
                    } else {
                        showToast("员工轨迹数据!");
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
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
        httpUtil.post(M_Url.getReportTaskList, G_RequestParams.getReportTaskList(UserInfoUtils.getId(this),
                salesid, String.valueOf(1), dateStr, dateStr, "1", "9999", "2", "",""), true);
    }

    /**
     * 绘制员工走访线路
     *
     * @param list
     */
    private void drawLine(List<ReportTaskBean> list) {
        List<LatLng> points = new ArrayList<LatLng>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        Marker marker;
        LatLng llStaff;

        if (list.size() == 1) {
            //添加位置信息接口
            LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_staff, null);
            LinearLayout llMain = (LinearLayout) llPopup.findViewById(R.id.ll_main);
            TextView tvName = (TextView) llPopup.findViewById(R.id.tv_staff_name);
            TextView tvTime = (TextView) llPopup.findViewById(R.id.tv_time);
            TextView tvState = (TextView) llPopup.findViewById(R.id.tv_state);

            tvName.setText(list.get(0).getClct_userid_nameref());
            tvTime.setText(StringUtils.getDate(list.get(0).getClct_arrivetime(), "HH:mm"));
            tvState.setText("(当前)");

            BitmapDescriptor bd = BitmapDescriptorFactory
                    .fromView(llPopup);

            llStaff = new LatLng(Double.parseDouble(list.get(0).getClct_latitude()), Double.parseDouble(list.get(0).getClct_longitude()));
            builder = builder.include(llStaff);

            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(bd);
            MarkerOptions ooD = new MarkerOptions().position(llStaff).icons(giflist)
                    .zIndex(0).period(10);
            marker = (Marker) (mBaiduMap.addOverlay(ooD));
        } else {
            for (int i = list.size() - 1; i >= 0; i--) {//后台的数据是按时间倒叙排的
                // 添加普通折线绘制
                llStaff = new LatLng(Double.parseDouble(list.get(i).getClct_latitude()),
                        Double.parseDouble(list.get(i).getClct_longitude()));

                if (i == 0 || i == list.size() - 1) {
                    //添加位置信息接口
                    LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_staff, null);
                    LinearLayout llMain = (LinearLayout) llPopup.findViewById(R.id.ll_main);
                    TextView tvName = (TextView) llPopup.findViewById(R.id.tv_staff_name);
                    TextView tvTime = (TextView) llPopup.findViewById(R.id.tv_time);
                    TextView tvState = (TextView) llPopup.findViewById(R.id.tv_state);

                    tvName.setText(list.get(0).getClct_userid_nameref());
                    tvTime.setText(StringUtils.getDate(list.get(0).getClct_arrivetime(), "HH:mm"));
                    if (i == list.size() - 1) {
                        tvState.setText("(起始)");
                    } else {
                        tvState.setText("(当前)");
                    }

                    BitmapDescriptor bd = BitmapDescriptorFactory
                            .fromView(llPopup);

                    builder = builder.include(llStaff);

                    ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
                    giflist.add(bd);
                    MarkerOptions ooD = new MarkerOptions().position(llStaff).icons(giflist)
                            .zIndex(0).period(10);
                    marker = (Marker) (mBaiduMap.addOverlay(ooD));
                }

                points.add(llStaff);
            }

            OverlayOptions ooPolyline = new PolylineOptions().width(10)
                    .color(UIUtils.getColor(R.color.color_blue)).points(points);
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        }

        if (llCompany != null)//如果公司经纬度不是空
            builder = builder.include(llCompany);

        LatLngBounds latlngBounds = builder.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMap.getWidth(), mMap.getHeight());
        mBaiduMap.animateMapStatus(u);
    }


    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private final int mIndex;

        public MyItem(LatLng latLng,int index) {
            mPosition = latLng;
            mIndex = index;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            if (mIndex == -1){
                TextView tvCompany = new Button(getApplicationContext());
                tvCompany.setBackgroundResource(R.mipmap.popup);
                tvCompany.setText(mUserBean.getCompanyName());

                return BitmapDescriptorFactory
                        .fromView(tvCompany);
            }else{
                //添加位置信息接口
                LinearLayout llPopup = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_staff, null);
                LinearLayout llMain = (LinearLayout) llPopup.findViewById(R.id.ll_main);
                TextView tvName = (TextView) llPopup.findViewById(R.id.tv_staff_name);
                TextView tvTime = (TextView) llPopup.findViewById(R.id.tv_time);
                TextView tvState = (TextView) llPopup.findViewById(R.id.tv_state);

                tvName.setText(listSalesLocation.get(mIndex).getClct_userid_nameref());
                tvTime.setText(StringUtils.getDate(listSalesLocation.get(mIndex).getClct_arrivetime(), "HH:mm"));
                tvState.setText("(当前)");

                return BitmapDescriptorFactory
                        .fromView(llPopup);
            }
        }
    }


    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMap.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMap.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMap.onDestroy();
        super.onDestroy();
        // 回收 bitmap 资源
        bd.recycle();
    }

}
