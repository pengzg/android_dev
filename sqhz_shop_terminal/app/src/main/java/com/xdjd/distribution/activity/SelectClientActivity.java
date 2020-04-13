package com.xdjd.distribution.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.ClassifyClientOneAdapter;
import com.xdjd.distribution.adapter.ClassifyClientTwoAdapter;
import com.xdjd.distribution.adapter.ClientDatumAdapter;
import com.xdjd.distribution.adapter.ClientDatumSearchAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.LineClientBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 选择客户
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SelectClientActivity extends BaseActivity implements ItemOnListener,
        ClassifyClientTwoAdapter.ItemTwoOnListener, ClientDatumAdapter.ItemClientOnListener, LocationListener, ClientDatumSearchAdapter.ItemClientOnListener {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.classify_one_lv)
    ListView mClassifyOneLv;
    @BindView(R.id.classify_two_lv)
    ListView mClassifyTwoLv;
    @BindView(R.id.client_lv)
    ListView mClientLv;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.lv_client_search)
    ListView mLvClientSearch;
    @BindView(R.id.ll_client_datum)
    LinearLayout mLlClientDatum;
    @BindView(R.id.ll_select_client)
    LinearLayout mLlSelectClient;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_pull_hint)
    TextView mTvPullHint;

    private ClassifyClientOneAdapter adapterOne;
    private ClassifyClientTwoAdapter adapterTwo;
    private ClientDatumAdapter adapterClient;

    private ClientDatumSearchAdapter adapterSearch;

    private List<LineClientBean> list = new ArrayList<>();
    /**
     * 根据定位或者类别获取客户列表
     */
    private List<ClientBean> listCus = new ArrayList<>();
    /**
     * 搜索客户bean
     */
    private List<ClientBean> listClient = new ArrayList<>();

    /**
     * 搜索列表选中的下标
     */
    private int searchIndex = -1;

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
    /**
     * 1.附近 2未定位3类别
     */
    private String categorytype = "";
    /**
     * 客户id
     */
    private String categoryid = "";

    private LocationService locationService;
    private MyLocationUtil locationUtil;

    private ClientBean clientBean;

    private UserBean userBean;

    private boolean isSelectClient = true;//防止选择客户多次点击

    private int mCode = 0; //是否是客户签到 0是获取列表 1是签到 2是查询客户列表

    private VaryViewHelper helper = null;

    private int page = 1;
    private int mFlag = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCode != 0) {
                disProgressDialog(SelectClientActivity.this);
            }
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                sign(clientBean);
            } else if (msg.what == PublicFinal.SUCCESS) {
                getLineClient();
            } else if (msg.what == 2) {
                getLineCsSearch();
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_select_client;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("选择客户");

        userBean = UserInfoUtils.getUser(this);

        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);

        adapterOne = new ClassifyClientOneAdapter(this);
        mClassifyOneLv.setAdapter(adapterOne);

        adapterTwo = new ClassifyClientTwoAdapter(this);
        mClassifyTwoLv.setAdapter(adapterTwo);

        adapterClient = new ClientDatumAdapter(this);
        mClientLv.setAdapter(adapterClient);
        adapterClient.setData(listCus);

        adapterSearch = new ClientDatumSearchAdapter(this);
        mLvClientSearch.setAdapter(adapterSearch);

        helper = new VaryViewHelper(mLlClientDatum);
        helper.showLoadingView();

        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        //        showProgressDialog(this);

        mPullScroll.setRightLayout(false);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mPullScroll);

        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (categoryid != null && !"".equals(categoryid)) {
                    page = 1;
                    mFlag = 1;
                    listCus.clear();
                    adapterClient.notifyDataSetChanged();
                    getCustomerToCategory();
                } else {
                    mPullScroll.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (categoryid != null && !"".equals(categoryid)) {
                    page++;
                    mFlag = 2;
                    getCustomerToCategory();
                } else {
                    mPullScroll.onRefreshComplete();
                }
                mTvPullHint.setVisibility(View.GONE);
            }
        });


        mEtSearch.setHint("按客户名称,联系人,电话进行查询");
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mLvClientSearch.setVisibility(View.GONE);
                    mLlClientDatum.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                if (listClient != null) {
                    listClient.clear();
                }

                showProgressDialog(this);
                adapterSearch.notifyDataSetChanged();
                mCode = 2;
                locationService.start();// 定位SDK
                mLlClientDatum.setVisibility(View.GONE);
                mLvClientSearch.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 获取客户通讯录
     */
    private void getLineCsSearch() {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(SelectClientActivity.this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    if (jsonBean.getDataList().size() > 0) {
                        listClient = jsonBean.getDataList();
                        adapterSearch.setData(listClient);
                    } else {
                        showToast("没有搜索到相对应的客户信息!");
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                helper.showErrorView(new OnErrorListener());
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getLineCsSearch, L_RequestParams.getLineCsSearch(
                userBean.getUserId(), UserInfoUtils.getLineId(this) + "",
                userBean.getOrgid(), longtitude, latitude, "Y", mEtSearch.getText().toString()), true);
    }

    /**
     * 根据线路加载客户列表
     */
    private void getLineClient() {
        AsyncHttpUtil<LineClientBean> httpUtil = new AsyncHttpUtil<>(SelectClientActivity.this, LineClientBean.class, new IUpdateUI<LineClientBean>() {
            @Override
            public void updata(LineClientBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    list = bean.getDataList();
                    adapterOne.setData(list);

                    if (list != null && list.size() > 0) {
                        onItem(0);
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
                helper.showDataView();
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
                helper.showErrorView(new OnErrorListener());
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getLineCategory,
                L_RequestParams.getLineCategory(userBean.getUserId(), UserInfoUtils.getLineId(this) + "", userBean.getOrgid(),
                        longtitude, latitude), false);
    }

    /**
     * 失败点击事件
     */
    class OnErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            helper.showLoadingView();
            //            getLineClient();
            locationService.start();
        }
    }

    @Override
    public void onItem(int position) {
        adapterTwo.setData(list.get(position).getSubList());
        if (list.get(position).getSubList() != null && list.get(position).getSubList().size() > 0) {
            onItemTwo(0);
        } else {
            adapterClient.setData(null);
        }
    }

    @Override
    public void onItemTwo(int position) {
        categorytype = list.get(adapterOne.getIndex()).getSubList().get(position).getCc_type();
        categoryid = list.get(adapterOne.getIndex()).getSubList().get(position).getCc_id();
        page = 1;
        mFlag = 1;
        listCus.clear();
        adapterClient.notifyDataSetChanged();
        getCustomerToCategory();
    }

    @Override
    public void onItemClient(final int position) {
        clientBean = listCus.get(position);
        getCustomerInfo(position, clientBean.getCc_id());
    }

    @Override
    public void onItemClientSearch(final int position) {
        clientBean = adapterSearch.getItem(position);
        searchIndex = position;
        getCustomerInfo(position, clientBean.getCc_id());
    }

    private void getCustomerToCategory() {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {

                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        listCus.addAll(jsonBean.getDataList());
                        mClientLv.setSelection(0);
                        adapterClient.notifyDataSetChanged();
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            page--;
                        }
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
                isSelectClient = true;
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getCustomerToCategory, L_RequestParams.getCustomerToCategory(UserInfoUtils.getLineId(this) + "",
                userBean.getOrgid(), longtitude, latitude, categorytype, categoryid, String.valueOf(page)), true);
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo(final int position, String cusId) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    clientBean.setCc_contacts_name(jsonBean.getCc_contacts_name());//联系人
                    clientBean.setCc_contacts_mobile(jsonBean.getCc_contacts_mobile());//联系电话
                    clientBean.setCc_categoryid(jsonBean.getCc_categoryid());//类别
                    clientBean.setCc_categoryid_nameref(jsonBean.getCc_categoryid_nameref());//类别名称
                    clientBean.setCc_channelid(jsonBean.getCc_channelid());//渠道
                    clientBean.setCc_channelid_nameref(jsonBean.getCc_channelid_nameref());

                    clientBean.setCc_depotid(jsonBean.getCc_depotid());
                    clientBean.setCc_depotid_nameref(jsonBean.getCc_depotid_nameref());
                    clientBean.setCc_image(jsonBean.getCc_image());

                    clientBean.setCc_isaccount(jsonBean.getCc_isaccount());
                    clientBean.setCc_openid(jsonBean.getCc_openid());

                    clientBean.setCc_goods_gradeid(jsonBean.getCc_goods_gradeid());
                    clientBean.setCc_goods_gradeid_nameref(jsonBean.getCc_goods_gradeid_nameref());

                    selectCustomer(clientBean, position);
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
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(this) + "",
                 cusId), true);
    }

    /**
     * 选择客户签到方法
     */
    private void selectCustomer(final ClientBean clientBean, final int position) {
        LogUtils.e("clientBean", clientBean.toString());
        if (userBean.getIsAllowSign().equals("1") &&
                clientBean.getCc_islocation().equals("Y")) {//限制500米以内签到且必须是已经定位

            BigDecimal distance;
            if (TextUtils.isEmpty(clientBean.getDistance()) || clientBean.getDistance() == null) {
                distance = BigDecimal.ZERO;
            } else {
                distance = new BigDecimal(clientBean.getDistance());
            }

            boolean isReLocation = false;
            if ("1".equals(userBean.getIsReLocation())) {//是否允许重新定位
                isReLocation = true;
            }

            if (distance.intValue() > UserInfoUtils.getSignDistance(this)) {
                DialogUtil.showCustomDialog(this, "提示", "您已超出店铺距离范围,不能进行销售!",
                        isReLocation ? "重新定位" : null, isReLocation ? "取消" : "确定", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(SelectClientActivity.this, PositionLocationActivity.class);
                                intent.putExtra("isHome", 3);
                                intent.putExtra("customer", clientBean);
                                intent.putExtra("index", position);
                                startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                            }

                            @Override
                            public void no() {
                            }
                        });
                return;
            }
        }

        if (clientBean.getCc_islocation().equals("N")) {
            DialogUtil.showCustomDialog(this, "提示", "这个客户还没有定位,请先去定位!", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                    Intent intent = new Intent(SelectClientActivity.this, PositionLocationActivity.class);
                    intent.putExtra("isHome", 3);
                    intent.putExtra("customer", clientBean);
                    intent.putExtra("index", position);
                    startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                }

                @Override
                public void no() {
                }
            });
            return;
        }
        if (userBean.getInPhoto().equals("1")) {//拍照退出
            PermissionUtils.requstActivityCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                @Override
                public void onSuccess() {//CaptureActivity
                    //进店签到
                    Intent intent = new Intent();
                    intent.setClass(SelectClientActivity.this, TakingPicturesActivity.class);
                    intent.putExtra("type", PublicFinal.SIGN);
                    intent.putExtra("customer", clientBean);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longtitude",longtitude);
                    if (address == null || address.length() == 0){
                        intent.putExtra("address",clientBean.getCc_address());
                    }else{
                        intent.putExtra("address",address);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onDilogCancal() {
                    showToast("获取相机权限失败!");
                }
            });
        } else {
            if (isSelectClient) {
                latitude = "";
                longtitude = "";
                address = "";
                mCode = 1;

                if (!locationService.client.isStarted()){
                    locationService.registerListener(locationUtil);
                    locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                }

                locationService.start();
                isSelectClient = false;
                showProgressDialog(this);
            }
        }
    }

    //把bitmap转换成String
    public String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.e("d-d", "压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 签到
     */
    private void sign(final ClientBean bean) {
        if ("".equals(latitude) || latitude == null) {
            showToast("位置获取失败");
            return;
        }

//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_sign);
//        String signImg = bitmapToString(bm);//521066     118163--73553--120426

        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    UserInfoUtils.setTaskId(SelectClientActivity.this,jsonBean.getTaskId());//客户签到id
                    UserInfoUtils.setClientInfo(SelectClientActivity.this, bean);

                    //将客户余额、欠款重置
                    UserInfoUtils.setCustomerBalance(SelectClientActivity.this, null);
                    UserInfoUtils.setSafetyArrearsNum(SelectClientActivity.this, null);
                    UserInfoUtils.setAfterAmount(SelectClientActivity.this, null);

                    //"cc_name":"小李","lineId":"85","lineName":"阴飞虎3线"
                    if (!TextUtils.isEmpty(bean.getLineId()) && !bean.getLineId().equals(UserInfoUtils.getLineId(SelectClientActivity.this))) {
                        UserInfoUtils.setLineId(SelectClientActivity.this, bean.getLineId());
                        UserInfoUtils.setLineName(SelectClientActivity.this, bean.getLineName());
                    }
                    EventBus.getDefault().post(new SignClientEvent());
                    finishActivity();
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
                isSelectClient = true;
            }
        });
        httpUtil.post(M_Url.sign, L_RequestParams.sign(userBean.getUserId(), UserInfoUtils.getLineId(this) + "",
                userBean.getOrgid(), bean.getCc_id(), longtitude, latitude, userBean.getIsScan(), "2", address, null), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Comon.QR_GOODS_REQUEST_CODE && resultCode == Comon.QR_GOODS_RESULT_CODE) {//给客户重新定位反回客户信息
            int index = data.getIntExtra("index", -1);
            if (index == -1) {
                adapterClient.notifyDataSetInvalidated();
            } else {
                ClientBean bean = (ClientBean) data.getSerializableExtra("customer");

                if (mLvClientSearch.getVisibility() == View.VISIBLE) {//如果是搜索列表
                    listClient.set(searchIndex, bean);
                    adapterSearch.setData(listClient);
                } else {//正常三级列表
                    listCus.set(index, bean);
                    adapterClient.setData(listCus);
                }

                clientBean = bean;

                if (userBean.getInPhoto().equals("1")) {//拍照退出
                    PermissionUtils.requstActivityCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                        @Override
                        public void onSuccess() {//CaptureActivity
                            //进店签到
                            Intent intent = new Intent();
                            intent.setClass(SelectClientActivity.this, TakingPicturesActivity.class);
                            intent.putExtra("type", PublicFinal.SIGN);
                            intent.putExtra("customer", clientBean);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        @Override
                        public void onDilogCancal() {
                            showToast("获取相机权限失败!");
                        }
                    });
                } else {

                    if (userBean.getInPhoto().equals("1")) {//拍照退出
                        PermissionUtils.requstActivityCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                            @Override
                            public void onSuccess() {//CaptureActivity
                                //进店签到
                                Intent intent = new Intent();
                                intent.setClass(SelectClientActivity.this, TakingPicturesActivity.class);
                                intent.putExtra("type", PublicFinal.SIGN);
                                intent.putExtra("customer", clientBean);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            @Override
                            public void onDilogCancal() {
                                showToast("获取相机权限失败!");
                            }
                        });
                    } else {
                        if (isSelectClient) {
                            latitude = "";
                            longtitude = "";
                            address = "";
                            mCode = 1;

                            locationService = ((App) getApplication()).locationService;
                            locationService.registerListener(locationUtil);
                            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                            locationService.start();
                            isSelectClient = false;
                            showProgressDialog(this);
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();
        LogUtils.e("location22", "selectClient:" + location.getAddrStr() + location.getLatitude());
        if (mCode == 1) {
            //签到定位
            mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        } else if (mCode == 0) {
            //获取列表定位
            mHandler.sendEmptyMessage(PublicFinal.SUCCESS);
        } else if (mCode == 2) {
            mHandler.sendEmptyMessage(2);
        }

        locationService.stop();
        disProgressDialog(this);
    }

    @Override
    public void locationError(final BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        isSelectClient = true;
        locationService.stop();
        mHandler.sendEmptyMessage(3);
        disProgressDialog(this);
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
