package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.UrlBean;
import com.xdjd.storebox.dao.UrlDao;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.BeanCallback;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.operation.L_RequestParamsCopy;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lijipei on 2017/5/9.
 */

public class SystemConfigurationActivity extends BaseActivity {


    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_three_domain_name)
    EditText mEtThreeDomainName;
    private UrlDao mUrlDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_configuration);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData() {
        mTitleBar.setTitle("系统配置");
        //mTitleBar.leftBack(this);
        mTitleBar.setRightText("提交");
        mTitleBar.setRightTextColor(R.color.black);
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtThreeDomainName.getText())) {
                    showToast("请填写服务网站名称");
                    return;
                }
                verifyDomainName();
            }
        });
        mUrlDao = new UrlDao(this);
        mEtThreeDomainName.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止输入空字符串
     */
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source.equals(" "))
                return "";
            else
                return null;
        }
    };

    private void verifyDomainName() {

        /*OkHttpUtils.
                post().
                tag(this).
                url("http://" + mEtThreeDomainName.getText().toString() +*//*".sqkx.net" +*//* M_Url.validateUrl).
                params(L_RequestParamsCopy.validateUrl()).
                build().execute(new BeanCallback<UrlBean>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(UrlBean response, int id) {

            }
        });*/

        AsyncHttpUtil<UrlBean> httpUtil = new AsyncHttpUtil<>(this, UrlBean.class, new IUpdateUI<UrlBean>() {
            @Override
            public void updata(UrlBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    jsonBean.setDomain_name("http://" + mEtThreeDomainName.getText().toString() /*+ ".sqkx.net"*/);
                    mUrlDao.insert(jsonBean);
                    LogUtils.e("url", mUrlDao.queryList().get(0).getDomain_name());
                    LogUtils.e("url2", mUrlDao.queryList().size() + "--");
                    //需要后台网路验证
                    UserInfoUtils.setDomainName(SystemConfigurationActivity.this, jsonBean.getDomain_name());
                    startActivity(LoginActivity.class);
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
        httpUtil.post("http://" + mEtThreeDomainName.getText().toString() + /*".sqkx.net" +*/ M_Url.validateUrl,
                L_RequestParams.validateUrl(), true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrlDao.destroy();
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN) {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}




