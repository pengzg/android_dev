package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerMobileListBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.BindingFacilityEvent;
import com.xdjd.distribution.event.OpenMallEvent;
import com.xdjd.distribution.event.SignClientEvent;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/8
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OpenMallActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_smscode)
    EditText mEtSmscode;
    @BindView(R.id.check_get_smscode)
    CheckBox mCheckGetSmscode;
    @BindView(R.id.btn_sure)
    Button mBtnSure;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.ll_sms)
    LinearLayout mLlSms;

    /**
     * 客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    private boolean isBinding;//是否是绑定客户列表跳转过来的

    @Override
    protected int getContentView() {
        return R.layout.activity_open_mall;
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
        if (getIntent().getBooleanExtra("isEdit",false)){//是否是编辑商城手机号码
            mTitleBar.setTitle("编辑商城手机号");
        }else{
            mTitleBar.setTitle("开通商城");
        }

        userBean = UserInfoUtils.getUser(this);
        isBinding = getIntent().getBooleanExtra("isBinding",false);

        if (!isBinding){
            clientBean = (ClientBean) getIntent().getSerializableExtra("clientBean");//UserInfoUtils.getClientInfo(this);
            if (clientBean == null) {
                finishActivity();
                showToast("客户信息有误!");
                return;
            }
        }else{
            clientBean = (ClientBean) getIntent().getSerializableExtra("clientBean");
        }

        if (userBean.getIsSendSms() != null && "Y".equals(userBean.getIsSendSms())) {
            mLlSms.setVisibility(View.VISIBLE);
        } else {
            mLlSms.setVisibility(View.GONE);
        }

        mTvName.setText("店铺名称:"+clientBean.getCc_name());
        mEtMobile.setText(clientBean.getCc_contacts_mobile());
        mEtMobile.setSelection(clientBean.getCc_contacts_mobile().length());
    }

    /**
     * 开通客户商城信息
     */
    private void openAccount(String sms_code) {
        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    showToast(jsonBean.getRepMsg());

                    clientBean.setCc_isaccount("Y");
                    clientBean.setCc_contacts_mobile(mEtMobile.getText().toString());
                    Intent intent = new Intent(OpenMallActivity.this,OpenMallSuccessActivity.class);
                    if (!isBinding){//是当前签到客户
//                        UserInfoUtils.setClientInfo(OpenMallActivity.this,clientBean);
                        EventBus.getDefault().post(new OpenMallEvent(clientBean));
                    }else{//不是签到客户,是从绑定客户列表跳转过来的
                        intent.putExtra("isBinding",isBinding);
                        EventBus.getDefault().post(new BindingFacilityEvent(clientBean));
                    }
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
        httpUtil.post(M_Url.openAccount, L_RequestParams.openAccount(this, clientBean.getCc_id(),
                mEtMobile.getText().toString(),
                sms_code), true);
    }

    /**
     * 获取验证码接口
     */
    private void sendSms() {
        if (!Validation.isPhoneNum(clientBean.getCc_contacts_mobile())){
            showToast("手机号格式不正确!");
            return;
        }

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    showToast(jsonBean.getRepMsg());
                    //获取注册验证码
                    MyCount mc = new MyCount(30000, 1000);
                    mc.start();
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
        httpUtil.post(M_Url.sendSms, L_RequestParams.sendSms(this, clientBean.getCc_id(),
                clientBean.getCc_contacts_mobile(),""), true);
    }

    @OnClick({R.id.check_get_smscode, R.id.btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_get_smscode:
                sendSms();
                break;
            case R.id.btn_sure:
                String sms_code = "";
                if (!TextUtils.isEmpty(mEtSmscode.getText())) {
                    sms_code = mEtSmscode.getText().toString();
                }
                if (userBean.getIsSendSms() != null && "Y".equals(userBean.getIsSendSms())) {
                    if (sms_code == null || "".equals(sms_code)){
                        showToast("请输入验证码!");
                        return;
                    }
                }

                if (TextUtils.isEmpty(mEtMobile.getText())){
                    showToast("请添加手机号码");
                    return;
                }

                /*StringBuilder sb = new StringBuilder();
                sb.append("当前要开通商城的手机号与以下店铺手机号重复,继续操作将重置以下店铺的手机号:\n");
                sb.append("   店铺:如花店铺"+"\n");
                sb.append("是否继续?");
                DialogUtil.showCustomDialog(OpenMallActivity.this, "提示", sb.toString(), "继续", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                    }

                    @Override
                    public void no() {

                    }
                });*/

                getCustomerListByMobile(sms_code);
                break;
        }
    }

    /**
     * 根据客户手机号获取客户列表
     */
    private void getCustomerListByMobile(final String sms_code){
        final AsyncHttpUtil<CustomerMobileListBean> httpUtil = new AsyncHttpUtil<>(this,CustomerMobileListBean.class , new IUpdateUI<CustomerMobileListBean>() {
            @Override
            public void updata(CustomerMobileListBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())){
                    if (jsonStr.getDataList()!=null && jsonStr.getDataList().size()>0){
                        StringBuilder sb = new StringBuilder();
                        sb.append("以下店铺的手机号信息和当前开通商城手机号重复,如果继续操作,以下店铺的手机信息将重置:\n");
                        sb.append("\n");
                        for (CustomerMobileListBean bean:jsonStr.getDataList()){
                            sb.append("  店铺:"+bean.getCc_name()+"\n");
                        }
                        sb.append("  是否继续?如有问题请联系管理员。");
                        DialogUtil.showCustomDialog(OpenMallActivity.this, "提示", sb.toString(), "继续", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                openAccount(sms_code);
                            }

                            @Override
                            public void no() {
                            }
                        });
                    }else{
                        openAccount(sms_code);
                    }
                }else if ("99".equals(jsonStr.getRepCode())){
                    DialogUtil.showCustomDialog(OpenMallActivity.this,"提示",jsonStr.getRepMsg(),"确定",null,null);
                }else{
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
        httpUtil.post(M_Url.getCustomerListByMobile,L_RequestParams.getCustomerListByMobile(mEtMobile.getText().toString(),clientBean.getCc_id()),true);
    }

    /* 定义一个倒计时的内部类 */
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //getSmscode.setBackgroundResource(R.drawable.background_code );
            //getSmscode.setSelected(false);
            mCheckGetSmscode.setEnabled(true);
            mCheckGetSmscode.setText("获取验证码");
            //getSmscode.setClickable(true);
            mCheckGetSmscode.setChecked(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCheckGetSmscode.setChecked(true);
            mCheckGetSmscode.setEnabled(false);
            mCheckGetSmscode.setText("(" + millisUntilFinished / 1000 + ")秒后重试");
            //getSmscode.setBackgroundResource(R.drawable.background );
        }
    }
}
