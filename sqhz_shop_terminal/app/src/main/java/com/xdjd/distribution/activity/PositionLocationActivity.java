package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bumptech.glide.Glide;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.event.CustomerAddressListEvent;
import com.xdjd.distribution.event.MapPositionEvent;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.distribution.popup.SelectGoodsPriceGradePopup;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.roundedimage.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PositionLocationActivity extends BaseActivity implements LocationListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_customer_lng)
    TextView mTvCustomerLng;
    @BindView(R.id.tv_customer_lat)
    TextView mTvCustomerLat;
    @BindView(R.id.tv_customer_address)
    TextView mTvCustomerAddress;
    @BindView(R.id.ll_location)
    LinearLayout mLlLocation;
    @BindView(R.id.tv_lng)
    TextView mTvLng;
    @BindView(R.id.tv_lat)
    TextView mTvLat;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.iv_head)
    RoundedImageView mIvHead;
    @BindView(R.id.tv_name)
    EditText mTvName;
    @BindView(R.id.et_contacts_name)
    EditText mEtContactsName;
    @BindView(R.id.et_contacts_mobile)
    EditText mEtContactsMobile;
    @BindView(R.id.tv_position_str)
    TextView mTvPositionStr;
    @BindView(R.id.category_tv)
    TextView mCategoryTv;
    @BindView(R.id.channel_tv)
    TextView mChannelTv;
    @BindView(R.id.tv_store)
    TextView mTvStore;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_price_grade)
    TextView mTvPriceGrade;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;

    /**
     * 选择的客户信息
     */
    private ClientBean customerBean;
    /**
     * 从通讯录跳转过来的客户信息
     */
    private AddressListBean addressBean;

    private String customerId;//客户id
    private UserBean userBean;

    private LocationService locationService;

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

    private MyLocationUtil locationUtil;

    /**
     * 是否是首页跳转过来的,1:首页;2:通讯录;3:选择客户列表
     */
    private int isHome;
    /**
     * 是否扫描客户档案卡过来的,true是,false不是
     */
    private boolean isScan = false;

    private VaryViewHelper mVaryViewHelper;

    /**
     * 从通讯录或选择客户中跳转过来的集合下标
     */
    private int index;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                mTvLng.setText(longtitude);
                mTvLat.setText(latitude);
                mTvAddress.setText(address);
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_position_location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("位置定位");

        userBean = UserInfoUtils.getUser(this);

        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);

        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        locationService.start();// 定位SDK

        mVaryViewHelper = new VaryViewHelper(mScrollview);

        isHome = getIntent().getIntExtra("isHome", 1);
        if (isHome == 1) {//是首页跳转过来的
            customerBean = (ClientBean) getIntent().getSerializableExtra("customer");
            isScan = getIntent().getBooleanExtra("isScan", false);
            customerId = customerBean.getCc_id();

            getCustomerInfo();
        } else if (isHome == 2) {//通讯录
            addressBean = (AddressListBean) getIntent().getSerializableExtra("customer");
            index = getIntent().getIntExtra("index", -1);
            customerId = addressBean.getCc_id();

            getCustomerInfo();
        } else if (isHome == 3) {//选择客户界面
            customerBean = (ClientBean) getIntent().getSerializableExtra("customer");
            index = getIntent().getIntExtra("index", -1);

            mTvName.setText(customerBean.getCc_name());
            mTvCustomerLng.setText(customerBean.getCc_longitude());
            mTvCustomerLat.setText(customerBean.getCc_latitude());
            mTvCustomerAddress.setText(customerBean.getCc_address());
            mEtContactsName.setText(customerBean.getCc_contacts_name());
            mEtContactsMobile.setText(customerBean.getCc_contacts_mobile());
            mCategoryTv.setText(customerBean.getCc_categoryid_nameref());
            mChannelTv.setText(customerBean.getCc_channelid_nameref());
            mTvStore.setText(customerBean.getCc_depotid_nameref());
            mTvPriceGrade.setText(customerBean.getCc_goods_gradeid_nameref());

            cc_depotid = customerBean.getCc_depotid();
            cc_depotid_nameref = customerBean.getCc_depotid_nameref();

            cc_categoryid = customerBean.getCc_categoryid();
            cc_categoryid_nameref = customerBean.getCc_categoryid_nameref();
            cc_channelid = customerBean.getCc_channelid();
            cc_channelid_nameref = customerBean.getCc_channelid_nameref();

            gradeId = customerBean.getCc_goods_gradeid();
            gradeName = customerBean.getCc_goods_gradeid_nameref();
        }

        initStorePopup();
        initPwPriceGrade();

        getAddInfo(false);
        queryStorehouseList(false);
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo() {
        mVaryViewHelper.showLoadingView();
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    mVaryViewHelper.showDataView();
                    customerBean = jsonBean;

                    if (customerBean.getCc_image() != null && customerBean.getCc_image().length() > 0) {
                        Glide.with(PositionLocationActivity.this).
                                load(customerBean.getCc_image()).
                                asBitmap().
                                error(R.mipmap.customer_img).
                                placeholder(R.mipmap.customer_img).
                                into(mIvHead);
                    }

                    mTvName.setText(customerBean.getCc_name());
                    mTvCustomerLng.setText(customerBean.getCc_longitude());
                    mTvCustomerLat.setText(customerBean.getCc_latitude());
                    mTvCustomerAddress.setText(customerBean.getCc_address());
                    mEtContactsName.setText(customerBean.getCc_contacts_name());
                    mEtContactsMobile.setText(customerBean.getCc_contacts_mobile());
                    mCategoryTv.setText(customerBean.getCc_categoryid_nameref());
                    mChannelTv.setText(customerBean.getCc_channelid_nameref());
                    mTvStore.setText(customerBean.getCc_depotid_nameref());
                    mTvPriceGrade.setText(customerBean.getCc_goods_gradeid_nameref());

                    cc_depotid = customerBean.getCc_depotid();
                    cc_depotid_nameref = customerBean.getCc_depotid_nameref();

                    cc_categoryid = customerBean.getCc_categoryid();
                    cc_categoryid_nameref = customerBean.getCc_categoryid_nameref();
                    cc_channelid = customerBean.getCc_channelid();
                    cc_channelid_nameref = customerBean.getCc_channelid_nameref();

                    gradeId = customerBean.getCc_goods_gradeid();
                    gradeName = customerBean.getCc_goods_gradeid_nameref();
                } else {
                    mVaryViewHelper.showErrorView(jsonBean.getRepMsg(),new OnErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(),new OnErrorListener());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(this) + "",
                customerId), false);
    }

    public class OnErrorListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            getCustomerInfo();
        }
    }

    @OnClick({R.id.ll_location, R.id.tv_store, R.id.category_tv, R.id.channel_tv, R.id.btn_submit, R.id.tv_price_grade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_location://定位
                //                locationService.start();
                Intent intent = new Intent(this, MapLocationActivity.class);
                //                intent.putExtra("address",mEtAddress.getText().toString());
                //                intent.putExtra("latitude",latitude);
                //                intent.putExtra("longtitude",longtitude);
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
            case R.id.category_tv://客户类别
                intent = new Intent(PositionLocationActivity.this, SelectCategoryChannelActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 100);
                break;
            case R.id.channel_tv://渠道
                intent = new Intent(PositionLocationActivity.this, SelectCategoryChannelActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 200);
                break;
            case R.id.btn_submit://提交
                updateLocation();
                break;
        }
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

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(PositionLocationActivity.this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (isHome == 1) {
                        customerBean.setCc_name(mTvName.getText().toString());
                        customerBean.setCc_latitude(latitude);
                        customerBean.setCc_longitude(longtitude);
                        customerBean.setCc_address(address);
                        customerBean.setCc_contacts_name(mEtContactsName.getText().toString());//客户姓名
                        customerBean.setCc_contacts_mobile(mEtContactsMobile.getText().toString());//客户电话
                        customerBean.setCc_categoryid(cc_categoryid);
                        customerBean.setCc_categoryid_nameref(cc_categoryid_nameref);
                        customerBean.setCc_channelid(cc_channelid);
                        customerBean.setCc_channelid_nameref(cc_channelid_nameref);
                        customerBean.setDistance("");
                        customerBean.setCc_islocation("Y");
                        customerBean.setCc_depotid(cc_depotid);
                        customerBean.setCc_depotid_nameref(cc_depotid_nameref);
                        customerBean.setCc_goods_gradeid(gradeId);
                        customerBean.setCc_goods_gradeid_nameref(gradeName);

                        if (isScan) {
                            Intent intent = new Intent();
                            intent.putExtra("result", customerBean);
                            setResult(Comon.QR_GOODS_RESULT_CODE, intent);
                        } else {
                            UserInfoUtils.setClientInfo(PositionLocationActivity.this, customerBean);
                            EventBus.getDefault().post(new SignClientEvent());
                        }
                    } else if (isHome == 2) {
                        addressBean.setCc_name(mTvName.getText().toString());
                        addressBean.setCc_latitude(latitude);
                        addressBean.setCc_longitude(longtitude);
                        addressBean.setCc_address(address);
                        addressBean.setCc_contacts_name(mEtContactsName.getText().toString());//客户姓名
                        addressBean.setCc_contacts_mobile(mEtContactsMobile.getText().toString());//客户电话
                        addressBean.setCc_islocation("Y");

                        EventBus.getDefault().post(new CustomerAddressListEvent(addressBean, index));
                    } else if (isHome == 3) {
                        customerBean.setCc_name(mTvName.getText().toString());
                        customerBean.setCc_latitude(latitude);
                        customerBean.setCc_longitude(longtitude);
                        customerBean.setCc_address(address);
                        customerBean.setCc_contacts_name(mEtContactsName.getText().toString());//客户姓名
                        customerBean.setCc_contacts_mobile(mEtContactsMobile.getText().toString());//客户电话
                        customerBean.setCc_islocation("Y");
                        customerBean.setCc_categoryid(cc_categoryid);
                        customerBean.setCc_categoryid_nameref(cc_categoryid_nameref);
                        customerBean.setCc_channelid(cc_channelid);
                        customerBean.setCc_channelid_nameref(cc_channelid_nameref);
                        customerBean.setDistance("");
                        customerBean.setCc_depotid(cc_depotid);
                        customerBean.setCc_depotid_nameref(cc_depotid_nameref);
                        customerBean.setCc_goods_gradeid(gradeId);
                        customerBean.setCc_goods_gradeid_nameref(gradeName);

                        Intent intent = new Intent();
                        intent.putExtra("index", index);
                        intent.putExtra("customer", customerBean);
                        setResult(Comon.QR_GOODS_RESULT_CODE, intent);
                        finishActivity();
                    }

                    showToast(jsonBean.getRepMsg());
                    finishActivity();
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
        String ccId = "";//客户id
        String lineId = "";//线路id
        if (isHome == 1) {
            ccId = customerBean.getCc_id();
            lineId = UserInfoUtils.getLineId(this);
        } else if (isHome == 2) {
            ccId = addressBean.getCc_id();
            lineId = "";
        } else if (isHome == 3) {
            ccId = customerBean.getCc_id();
            lineId = UserInfoUtils.getLineId(this);
        }
        httpUtil.post(M_Url.updateLocation, L_RequestParams.updateLocation(mTvName.getText().toString(), lineId, ccId,
                longtitude, latitude, mTvAddress.getText().toString(),
                mEtContactsName.getText().toString(), mEtContactsMobile.getText().toString(), cc_categoryid, cc_channelid, cc_depotid,
                address, "", gradeId), true);
    }

    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();

        mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        locationService.stop(); //停止定位服务
    }

    @Override
    public void locationError(BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        locationService.stop(); //停止定位服务
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Comon.QR_GOODS_REQUEST_CODE == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {
            address = data.getStringExtra("address");
            latitude = data.getStringExtra("latitude");
            longtitude = data.getStringExtra("longtitude");

            mTvLng.setText(longtitude);
            mTvLat.setText(latitude);
            mTvAddress.setText(address);
        } else if (resultCode == 10) {
            AddInfoBean bean = (AddInfoBean) data.getSerializableExtra("addInfo");
            switch (requestCode) {
                case 100:
                    cc_categoryid = bean.getCc_id();
                    cc_categoryid_nameref = bean.getCc_name();
                    mCategoryTv.setText(bean.getCc_name());
                    break;
                case 200:
                    cc_channelid = bean.getCct_id();
                    cc_channelid_nameref = bean.getCct_name();
                    mChannelTv.setText(bean.getCct_name());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(MapPositionEvent event) {
        mTvPositionStr.setText("重新定位");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
}
