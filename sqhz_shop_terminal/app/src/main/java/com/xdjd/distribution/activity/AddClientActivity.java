package com.xdjd.distribution.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bumptech.glide.Glide;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.distribution.event.AddCustomerUpdateEvent;
import com.xdjd.distribution.event.MapPositionEvent;
import com.xdjd.distribution.event.PicturesEvent;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.distribution.popup.LinePopup;
import com.xdjd.distribution.popup.NearbyShopPopup;
import com.xdjd.distribution.popup.PaymentMethodsPopup;
import com.xdjd.distribution.popup.SelectGoodsPriceGradePopup;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.EditTextUtil;
import com.xdjd.utils.MyLocationUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.toast.ToastUtils;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class AddClientActivity extends BaseActivity implements LocationListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.category_tv)
    TextView mCategoryTv;
    @BindView(R.id.add_client_ll)
    LinearLayout mAddClientLl;
    @BindView(R.id.channel_tv)
    TextView mChannelTv;
    @BindView(R.id.et_cc_name)
    EditText mEtCcName;
    @BindView(R.id.et_contacts_name)
    EditText mEtContactsName;
    @BindView(R.id.et_barcode)
    EditText mEtBarcode;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_contacts_mobile)
    EditText mEtContactsMobile;
    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.tv_lines)
    TextView mTvLines;
    @BindView(R.id.btn_add_client)
    Button mBtnAddClient;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.ll_location)
    LinearLayout mLlLocation;
    @BindView(R.id.tv_position_str)
    TextView mTvPositionStr;
    @BindView(R.id.tv_stock_name)
    TextView mTvStockName;
    @BindView(R.id.cs_shop_photo)
    LinearLayout csShopPhoto;
    @BindView(R.id.cs_image)
    CircleImageView csImage;
    @BindView(R.id.mobile_del)
    LinearLayout ll_mobile_del;
    @BindView(R.id.tv_gradeid)
    TextView mTvGradeid;


    private PaymentMethodsPopup popupPaymentMethods;

    private Intent intent;

    private String categoryId;//类别id
    private String channelId;//渠道id
    private String lineIds;//线路id
    private String gradeId;//价格id
    private Bitmap bitmap;
    private String imagePath = "";//门店图片
    private Boolean imageFlag = false;
    private String ccId;//客户id
    private Boolean selectedFlag = false;//选中附近客户
    private int selectedIndex;

    /**
     * 线路popup
     */
    private LinePopup linePopup;
    private List<LineBean> listLine = new ArrayList<>();
    /**
     * 线路dao
     */
    private LineDao lineDao;

    /**
     * 价格等级popup
     */
    private SelectGoodsPriceGradePopup gradePopup;
    private List<AddInfoBean> listGrade;

    /**
     * 正则验证只能输入汉字和英文
     */
    private String nameVerify = "^[a-zA-Z\\u4e00-\\u9fa5]+$";

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

    private LocationService locationService;

    private MyLocationUtil locationUtil;

    private UserBean userBean;

    /**
     * 仓库列表
     */
    private List<StorehouseBean> listStore;
    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;
    /*附近店铺popup*/
    private NearbyShopPopup popupNearShop;
    /**
     * 仓库id
     */
    private String storehouseId = "";
    /**
     * 仓库名称
     */
    private String storehouseName = "";

    private List<NearbyShopBean> nearbyShopList = new ArrayList<NearbyShopBean>();
    //电话编辑监听事件
    private MyMobileTextWatcher mMyMobileTextWatcher;
    //是否开启电话搜索,true开启,false禁用
    private boolean isOpenMobileSearch = true;

    //客户位置id
    private String locationId;


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PublicFinal.LOCATION_SUCCESS) {
                mEtAddress.setText(address);
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_add_client;
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("添加客户");
        mTitleBar.setRightText("附近店铺");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getNearbyShop(longtitude, latitude, "");
            }
        });

        locationId = getIntent().getStringExtra("locationId");

        lineIds = UserInfoUtils.getLineId(this);
        mTvLines.setText(UserInfoUtils.getLineName(this));

        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);

        userBean = UserInfoUtils.getUser(this);

        EditTextUtil.setEditTextInhibitInputSpace(mEtCcName);
        EditTextUtil.setEditTextInhibitInputSpace(mEtContactsName);
        EditTextUtil.setEditTextInhibitInputSpace(mEtAddress);

        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request

        initPwPriceGrade();
        initPwLines();
        initStorePopup();
        initNearbyShopPopup();
        queryStorehouseList(false);
        getAddInfo(false);
        mMyMobileTextWatcher = new MyMobileTextWatcher();
        editListener(mEtContactsMobile, ll_mobile_del);

        if (locationId != null && locationId.length() > 0) {
            getCustomerInfoByLocationid(locationId);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onPause() {
        locationService.unregisterListener(locationUtil); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /*文本框获取焦点和文本内容监听*/
    private void editListener(final EditText editText, final LinearLayout linearLayout) {
        if (editText.getText().length() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
            editText.setSelection(editText.getText().length());
        } else {
            linearLayout.setVisibility(View.GONE);
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b /*&& editText.getText().length() > 0*/) {//获得焦点
                    linearLayout.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    if (isOpenMobileSearch) {
                        editText.addTextChangedListener(mMyMobileTextWatcher);
                    }
                } else {//失去焦点
                    linearLayout.setVisibility(View.GONE);
                    editText.removeTextChangedListener(mMyMobileTextWatcher);
                }
            }
        });
    }

    public class MyMobileTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                ll_mobile_del.setVisibility(View.VISIBLE);
                if (editable.toString().length() == 11) {
                    if (!Validation.isPhoneNum(editable.toString())) {
                        showToast("手机号格式错误！");
                    } else {
                        getCustomerInfor(editable.toString());
                    }
                } else {
                    if (selectedFlag == true) {
                        showToast("手机号格式错误！");
                    }
                    //                    clearCustomerInfor();
                }
            } else {
                ll_mobile_del.setVisibility(View.GONE);
                //                clearCustomerInfor();
            }
        }
    }

    /**
     * 初始化线路popup
     */
    private void initPwLines() {
        lineDao = new LineDao(this);
        listLine = lineDao.query();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        linePopup = new LinePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                lineIds = listLine.get(position).getBl_id();
                mTvLines.setText(listLine.get(position).getBl_name());
                linePopup.dismiss();
            }
        });
        linePopup.setData(listLine);
    }

    /**
     * 显示线路popup
     */
    private void showPwLines() {
        linePopup.setItem(mTvLines.getText().toString());
        linePopup.setId(lineIds);
        // 显示窗口
        linePopup.showAtLocation(mAddClientLl,
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
                mTvGradeid.setText(listGrade.get(position).getGpg_title());
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
        gradePopup.showAtLocation(mAddClientLl,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    @OnClick({R.id.category_tv, R.id.channel_tv, R.id.tv_lines, R.id.btn_add_client, R.id.ll_location,
            R.id.tv_stock_name, R.id.cs_shop_photo, R.id.mobile_del, R.id.tv_gradeid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_tv://客户类别
                intent = new Intent(AddClientActivity.this, SelectCategoryChannelActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 100);
                break;
            case R.id.channel_tv://渠道
                intent = new Intent(AddClientActivity.this, SelectCategoryChannelActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_gradeid://价格方案
                if (listStore == null || listStore.size() == 0) {
                    getAddInfo(true);
                } else {
                    showPwPriceGrade();
                }
                break;
            case R.id.tv_lines://选择线路
                showPwLines();
                break;
            case R.id.btn_add_client://提交
                addCustomer();
                break;
            case R.id.ll_location:
                //                locationService.start();
                Intent intent = new Intent(this, MapLocationActivity.class);
                //                intent.putExtra("address",mEtAddress.getText().toString());
                //                intent.putExtra("latitude",latitude);
                //                intent.putExtra("longtitude",longtitude);
                startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                break;
            case R.id.tv_stock_name://选择仓库
                if (listStore == null || listStore.size() == 0) {
                    queryStorehouseList(true);
                } else {
                    showPwStore();
                }
                break;
            case R.id.cs_shop_photo://客户门店照片
                PermissionUtils.requstActivityCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(AddClientActivity.this, PicturesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }

                    @Override
                    public void onDilogCancal() {
                        showToast("获取相机权限失败!");
                    }
                });
                break;
            case R.id.mobile_del:
                mEtContactsMobile.setText("");
                //                clearCustomerInfor();
                break;
        }
    }

    /**
     * 得到客户信息
     *
     * @param bean
     */
    private void setCustomerInfor(ClientBean bean) {
        if (!isOpenMobileSearch) {//如果关闭电话查询了
            mEtContactsMobile.setText(bean.getCc_contacts_mobile());
        }

        mEtCcName.setText(bean.getCc_name());//店名
        mEtContactsName.setText(bean.getCc_contacts_name());//联系人
        if (!"".equals(bean.getCc_image()) && null != bean.getCc_image()) {
            Glide.with(AddClientActivity.this).load(bean.getCc_image()).into(csImage);
            imagePath = "";
            imageFlag = true;
        } else {
            imageFlag = false;
            csImage.setImageResource(R.drawable.photo);
        }
        if (!"".equals(bean.getCc_barcode()) && null != bean.getCc_barcode()) {///卡号
            mEtBarcode.setText(bean.getCc_barcode());
        }
        if (!"".equals(bean.getCc_code()) && null != bean.getCc_code()) {
            mEtCode.setText(bean.getCc_code());//编码
        }
        mCategoryTv.setText(bean.getCc_categoryid_nameref());//类别
        categoryId = bean.getCc_categoryid();//类别id
        mChannelTv.setText(bean.getCc_channelid_nameref());//渠道
        channelId = bean.getCc_channelid();//渠道id
        ccId = bean.getCc_id();//客户id
        selectedFlag = false;
    }

    /*显示选中附近店铺信息*/
    private void showSelectedShopInfor(NearbyShopBean bean) {
        if (!"".equals(mEtContactsMobile.getText()) && null != mEtContactsMobile.getText()) {
            mEtContactsMobile.setSelection(mEtContactsMobile.getText().length());
        }
        mEtCcName.setText(bean.getCcl_name());//店名
        mEtContactsName.setText(bean.getCcl_contacts_name());//联系人
        csImage.setImageResource(R.drawable.photo);
        imagePath = "";
        imageFlag = false;
        mEtBarcode.setText("");//卡号
        mEtCode.setText("");//编码
        mCategoryTv.setText("");//类别
        categoryId = "";
        mChannelTv.setText("");//渠道
        channelId = "";
        ccId = "";
        selectedFlag = false;
    }

    /**
     * 清空客户信息
     *
     * @param
     */
    private void clearCustomerInfor() {
        mEtCcName.setText("");//店名
        mEtContactsName.setText("");//联系人
        csImage.setImageResource(R.drawable.photo);
        imagePath = "";
        imageFlag = false;
        mEtBarcode.setText("");//卡号
        mEtCode.setText("");//编码
        mCategoryTv.setText("");//类别
        categoryId = "";
        mChannelTv.setText("");//渠道
        channelId = "";
        //mEtAddress.setText("");//地址
        ccId = "";
    }


    /*获取附近店铺列表*/
    private void getNearbyShop(String lng, String lat, String searchKey) {
        AsyncHttpUtil<NearbyShopBean> httpUtil = new AsyncHttpUtil<>(AddClientActivity.this, NearbyShopBean.class, new IUpdateUI<NearbyShopBean>() {
            @Override
            public void updata(NearbyShopBean bean) {
                if (bean.getRepCode().equals("00")) {
                    nearbyShopList.clear();
                    if (bean.getDataList() != null && bean.getDataList().size() > 0) {
                        nearbyShopList.addAll(bean.getDataList());
                        showPwNearbyShop();
                    }
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
        httpUtil.post(M_Url.getNearbyShop, L_RequestParams.getNearbyShop(lng, lat,
                searchKey, "1", "999", "1"), false);
    }

    /*根据手机号获取客户信息*/
    private void getCustomerInfor(String mobile) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(AddClientActivity.this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean bean) {
                if (bean.getRepCode().equals("00")) {
                    //                    setCustomerInfor(bean);
                    DialogUtil.showCustomDialog(AddClientActivity.this, "提示",
                            "输入的手机号码与 店铺:" + bean.getCc_name() + " 手机号码重复", "确定", null, null);
                } else {
                    if (selectedFlag == true) {
                        showSelectedShopInfor(nearbyShopList.get(selectedIndex));
                    } else {
                        //                        clearCustomerInfor();
                    }
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
        httpUtil.post(M_Url.getGetCustomerInfo, L_RequestParams.getCustomerAllInfor(UserInfoUtils.getId(AddClientActivity.this), mobile), false);
    }


    /**
     * 根据位置id获取客户信息
     *
     * @param locationid
     */
    private void getCustomerInfoByLocationid(final String locationid) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(AddClientActivity.this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean bean) {
                isOpenMobileSearch = false;//禁止电话搜索
                if (bean.getRepCode().equals("00")) {
                    locationId = locationid;
                    setCustomerInfor(bean);
                } else {
                    if (locationId != null && locationId.length() > 0) {
                        showToast(bean.getRepMsg());
                    } else {
                        if (selectedFlag == true) {
                            showSelectedShopInfor(nearbyShopList.get(selectedIndex));
                        } else {
                            clearCustomerInfor();
                        }
                    }
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
        httpUtil.post(M_Url.getCustomerInfoByLocationid, L_RequestParams.getCustomerInfoByLocationid(locationid), true);
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
                            //默认选中正常的价格方案(1.00正常)
                            BigDecimal discount;
                            for (int i=0;i<listGrade.size();i++){
                                if (listGrade.get(i).getGpg_discount()!=null &&
                                        listGrade.get(i).getGpg_discount().length()>0){
                                    discount = new BigDecimal(listGrade.get(i).getGpg_discount());
                                }else{
                                    discount = BigDecimal.ZERO;
                                }

                                if (discount.compareTo(BigDecimal.ONE) == 0){
                                    gradeId = listGrade.get(i).getGpg_id();
                                    mTvGradeid.setText(listGrade.get(i).getGpg_title());
                                    break;
                                }
                            }
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
                            //                            storehouseId = listStore.get(0).getBs_id();
                            //                            storehouseName = listStore.get(0).getBs_name();
                            //                            mTvStockName.setText(storehouseName);
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
        httpUtil.post(M_Url.queryStorehouseList,
                L_RequestParams.queryStorehouseList("", "", "3"), isDialog);
    }

    /**
     * 添加客户接口
     */
    private void addCustomer() {
        if (!Validation.isPhoneNum(mEtContactsMobile.getText().toString())) {
            showToast("手机号格式错误！");
            return;
        }
        if (TextUtils.isEmpty(mEtCcName.getText())) {
            showToast("请输入店名！");
            return;
        }
        if (TextUtils.isEmpty(mEtContactsName.getText())) {
            showToast("请输入联系人姓名！");
            return;
        }
        //        if (imagePath == null || "".equals(imagePath) && imageFlag == false) {
        //            showToast("请拍摄门店照片！");
        //            return;
        //        }
        if (categoryId == null || "".equals(categoryId)) {
            showToast("请选择客户类别！");
            return;
        }
        if (channelId == null || "".equals(channelId)) {
            showToast("请选择客户渠道类型！");
            return;
        }

//        if ((storehouseId == null || "".equals(storehouseId))) {
//            showToast("请选择客户默认出货仓库！");
//            return;
//        }

        if (gradeId == null || "".equals(gradeId)) {
            showToast("请选择价格方案");
            return;
        }
        //        if (!Validation.isPhoneNum(mEtContactsMobile.getText().toString())) {
        //            showToast("手机号格式错误！");
        //            return;
        //        }
        //        Pattern p = Pattern.compile(nameVerify);
        /*Matcher m = p.matcher(mEtCcName.getText());
        if (!m.matches()) {
            showToast("店铺名称只能输入汉字和英文");
            return;
        }*/
        /*Matcher mContactsName = p.matcher(mEtContactsName.getText());
        if (!TextUtils.isEmpty(mEtContactsName.getText()) && !mContactsName.matches()) {
            showToast("联系人名字只能输入汉字和英文");
            return;
        }*/

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(AddClientActivity.this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(AddClientActivity.this, jsonBean.getRepMsg());
                    //添加后发送刷新添加客户列表消息
                    EventBus.getDefault().post(new AddCustomerUpdateEvent());
                    Intent intent = new Intent(AddClientActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
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

            }
        });
        httpUtil.post(M_Url.addCustomer, L_RequestParams.addCustomer(userBean.getUserId(), userBean.getOrgid(),
                mEtCode.getText().toString(), ccId, mEtCcName.getText().toString(), categoryId, channelId,
                mEtContactsName.getText().toString(), mEtContactsMobile.getText().toString(), mEtAddress.getText().toString(), mEtBarcode.getText().toString()
                , mEtRemarks.getText().toString(), String.valueOf(lineIds), longtitude, latitude, storehouseId, imagePath, locationId,gradeId), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            AddInfoBean bean = (AddInfoBean) data.getSerializableExtra("addInfo");
            switch (requestCode) {
                case 100:
                    categoryId = bean.getCc_id();
                    mCategoryTv.setText(bean.getCc_name());
                    break;
                case 200:
                    channelId = bean.getCct_id();
                    mChannelTv.setText(bean.getCct_name());
                    break;
            }
        } else if (Comon.QR_GOODS_REQUEST_CODE == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {
            address = data.getStringExtra("address");
            mEtAddress.setText(address);
            latitude = data.getStringExtra("latitude");
            longtitude = data.getStringExtra("longtitude");
        }
        /*if (Comon.ADD_CUSTOMER_CODE == requestCode && Comon.ADD_CUSTOMER_RESULT_CODE == resultCode) {
            try {
                imagePath = data.getStringExtra("resultPath");
                byte[] b = Base64.decodeBase64(data.getStringExtra("url").getBytes("UTF-8"));
                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
                csImage.setImageBitmap(bitmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*显示指定路径图片*/
    private void showLocalImage(ImageView imageView, String path) {
        String localIconNormal = path;
        File localFile;
        FileInputStream localStream = null;
        Bitmap bitmap = null;
        localFile = new File(localIconNormal);
        if (!localFile.exists()) {
            Log.e("err:", "图片不存在！");
        } else {
            try {
                localStream = new FileInputStream(localFile);
                bitmap = BitmapFactory.decodeStream(localStream);
                imageView.setImageBitmap(bitmap);
                if (localStream != null) {
                    localStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void locationSuccess(BDLocation location) {
        latitude = String.valueOf(location.getLatitude());
        longtitude = String.valueOf(location.getLongitude());
        address = location.getAddrStr();
        mHandler.sendEmptyMessage(PublicFinal.LOCATION_SUCCESS);
        locationService.stop();
    }

    @Override
    public void locationError(BDLocation location) {
        showToast("定位获取失败,请检查GPS或网络是否打开");
        locationService.stop(); //停止定位服务
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
            csImage.setImageBitmap(bitmap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                storehouseId = listStore.get(position).getBs_id();
                storehouseName = listStore.get(position).getBs_name();
                mTvStockName.setText(listStore.get(position).getBs_name());
                popupStore.dismiss();
            }
        });
    }

    /*初始化附近店铺popup*/
    private void initNearbyShopPopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupNearShop = new NearbyShopPopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                //                mEtContactsMobile.setText(nearbyShopList.get(position).getCcl_contacts_mobile());//手机号
                //                if(!"".equals(mEtContactsMobile.getText().toString())&& null != mEtContactsMobile.getText().toString()){
                //                    mEtContactsMobile.setSelection(nearbyShopList.get(position).getCcl_contacts_mobile().length());
                //                }

                selectedIndex = position;
                selectedFlag = true;

                clearCustomerInfor();
                getCustomerInfoByLocationid(nearbyShopList.get(position).getCcl_id());

                popupNearShop.dismiss();
            }
        });
    }

    /*显示附近店铺popup*/
    private void showPwNearbyShop() {
        popupNearShop.setData(nearbyShopList);
        popupNearShop.showAtLocation(mAddClientLl, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示仓库popup
     */
    private void showPwStore() {
        popupStore.setData(listStore);
        popupStore.setId(storehouseId);
        // 显示窗口
        popupStore.showAtLocation(mAddClientLl,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }
}
