package com.xdjd.shopInfoCollect.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bumptech.glide.Glide;
import com.xdjd.distribution.App;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.MapLocationActivity;
import com.xdjd.distribution.activity.PicturesActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.callback.LocationListener;
import com.xdjd.distribution.location.LocationService;
import com.xdjd.shopInfoCollect.main.ShopCollectMainActivity;
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

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/9/29.
 */

public class CollectClientActivity extends BaseActivity implements LocationListener {
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
    @BindView(R.id.syt_num)
    EditText sytNum;

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
    private Bitmap bitmap;
    private String imagePath = "";//门店图片
    private NearbyShopBean bean;
    private Boolean imageFlag = false;
    private String ccId;
    private Integer type;

    private MyLocationUtil locationUtil;

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
        return R.layout.activity_collect_client;
    }

    @Override
    protected void initData() {
        super.initData();
        type = getIntent().getIntExtra("type", 0);
        mTitleBar.leftBack(this);
        if (type == 0) {
            mTitleBar.setTitle("定位店铺");
        } else {
            mTitleBar.setTitle("采集店铺");
        }
        locationUtil = new MyLocationUtil();
        locationUtil.setListener(this);
        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationUtil);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request


        if (type == 0) {
            bean = (NearbyShopBean) getIntent().getSerializableExtra("bean");
            if (bean.getCcl_name() != null && !"".equals(bean.getCcl_name())) {
                setCustomerInfor(bean);
            }
        }
        editListener(mEtContactsMobile,ll_mobile_del);
    }

    /*文本框获取焦点和文本内容监听*/
    private void editListener(final EditText editText, final LinearLayout linearLayout) {
        if (editText.getText().length() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
            editText.setSelection(editText.getText().length());
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    if(editable.toString().length() == 11 ){
                        if(!Validation.isPhoneNum(editable.toString())){
                            showToast("手机号格式错误！");
                        }else{
                            //getCustomerInfor(editable.toString());
                        }
                    }
                } else {
                    linearLayout.setVisibility(View.GONE);
                   // clearCustomerInfor();
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && editText.getText().length() > 0) {//获得焦点
                    linearLayout.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    Log.e("Focus","F");
                } else {//失去焦点
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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

    @OnClick({R.id.btn_add_client, R.id.ll_location,
            R.id.cs_shop_photo, R.id.mobile_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_client://提交
                Log.e("click:","提交");
                addCollectShop();
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapLocationActivity.class);
                startActivityForResult(intent, Comon.QR_GOODS_REQUEST_CODE);
                break;
            case R.id.cs_shop_photo://客户门店照片
                PermissionUtils.requstActivityCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(CollectClientActivity.this, PicturesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type", 1);
                        startActivityForResult(intent, Comon.ADD_CUSTOMER_CODE);
                    }

                    @Override
                    public void onDilogCancal() {
                        showToast("获取相机权限失败!");
                    }
                });
                break;
            case R.id.mobile_del:
                mEtContactsMobile.setText("");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Comon.QR_GOODS_REQUEST_CODE == requestCode && Comon.QR_GOODS_RESULT_CODE == resultCode) {
            address = data.getStringExtra("address");
            mEtAddress.setText(address);
            latitude = data.getStringExtra("latitude");
            longtitude = data.getStringExtra("longtitude");
        }
        if (Comon.ADD_CUSTOMER_CODE == requestCode && Comon.ADD_CUSTOMER_RESULT_CODE == resultCode) {
            try {
                imagePath = data.getStringExtra("resultPath");
                byte[] b = Base64.decodeBase64(data.getStringExtra("url").getBytes("UTF-8"));
                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
                csImage.setImageBitmap(bitmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*添加采集店铺和定位未定位店铺*/
    private void addCollectShop() {
        /*if (!Validation.isPhoneNum(mEtContactsMobile.getText().toString())) {
            //showToast("手机号格式错误！");
            UIUtils.Toast("手机号格式错误!");
            return;
        }*/
        if (TextUtils.isEmpty(mEtCcName.getText())) {
            showToast("请输入店名！");
            return;
        }
        if (TextUtils.isEmpty(mEtContactsName.getText())) {
            showToast("请输入联系人姓名！");
            return;
        }
        if(imagePath == null || "".equals(imagePath)&&imageFlag == false){
            showToast("请拍摄门店照片！");
            return;
        }
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(CollectClientActivity.this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean bean) {
                if (bean.getRepCode().equals("00")) {
                    Intent intent = new Intent(CollectClientActivity.this, ShopCollectMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.collectShopAndLocateShop, L_RequestParams.CollectAndLocateShop(UserInfoUtils.getId(CollectClientActivity.this),
                longtitude,latitude,ccId,mEtCcName.getText().toString(),mEtContactsName.getText().toString(),mEtContactsMobile.getText().toString(),
                mEtAddress.getText().toString(),sytNum.getText().toString(),imagePath),false);
    }

    /**
     * 得到客户信息
     *
     * @param bean
     */
    private void setCustomerInfor(NearbyShopBean bean) {
        mEtContactsMobile.setText(bean.getCcl_contacts_mobile());
        mEtCcName.setText(bean.getCcl_name());//店名
        mEtContactsName.setText(bean.getCcl_contacts_name());//联系人
        if (!"".equals(bean.getCcl_img()) && null != bean.getCcl_img()) {
            Glide.with(CollectClientActivity.this).load(bean.getCcl_img()).into(csImage);
            imagePath = bean.getCcl_img().replaceAll("http://m.shequkuaixian.com/static/upload/image","");
            imageFlag = true;
        } else {
            imageFlag = false;
            csImage.setImageResource(R.drawable.photo);
        }
        ccId = bean.getCcl_id();//客户id
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
        //mEtAddress.setText("");//地址
        ccId = "";
    }

}
