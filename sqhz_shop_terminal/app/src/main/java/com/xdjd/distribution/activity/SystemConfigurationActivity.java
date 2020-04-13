package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.UrlBean;
import com.xdjd.distribution.dao.UrlDao;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/5/9.
 */

public class SystemConfigurationActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.et_three_domain_name)
    EditText mEtThreeDomainName;
    @BindView(R.id.tv_two_domain_name)
    TextView mTvTwoDomainName;
    @BindView(R.id.tv_top_domain_name)
    TextView mTvTopDomainName;
    @BindView(R.id.tv_domain)
    TextView mTvDomain;
    @BindView(R.id.tv_ip)
    TextView mTvIp;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.ll_domain)
    LinearLayout mLlDomain;
    @BindView(R.id.et_ip)
    EditText mEtIp;
    @BindView(R.id.ll_ip)
    LinearLayout mLlIp;

    private UrlDao mUrlDao;

    private TextView[] tvs;
    public int mIndex = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_system_configuration;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.setTitle("系统配置");
        mTitleBar.leftBack(this);

        mTitleBar.setRightText("提交");
        mTitleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIndex == 0 && TextUtils.isEmpty(mEtThreeDomainName.getText())) {
                    showToast("请填写服务网站名称");
                    return;
                }else if (mIndex == 1 && TextUtils.isEmpty(mEtIp.getText())){
                    showToast("请填写ip地址");
                    return;
                }

                verifyDomainName();
            }
        });

        mUrlDao = new UrlDao(this);

        mEtThreeDomainName.setFilters(new InputFilter[]{filter});

        tvs = new TextView[]{mTvDomain, mTvIp};
        tvs[mIndex].setSelected(true);
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;
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
        final String mUrl;
        if (mIndex == 0){
            mUrl = "http://" + mEtThreeDomainName.getText().toString() /*+ ".sqkx.net"*/;
        }else{
            mUrl = "http://" + mEtIp.getText().toString();
        }

        AsyncHttpUtil<UrlBean> httpUtil = new AsyncHttpUtil<>(this, UrlBean.class, new IUpdateUI<UrlBean>() {
            @Override
            public void updata(UrlBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    jsonBean.setDomain_name(mUrl);//"http://" + mEtThreeDomainName.getText().toString() + ".sqkx.net");
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
        httpUtil.post(mUrl + M_Url.validateUrl,
                L_RequestParams.validateUrl(), true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrlDao.destroy();
    }

    @OnClick({R.id.tv_domain, R.id.tv_ip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_domain:
                mIndex = 0;
                selectTab();
                mLlIp.setVisibility(View.GONE);
                mLlDomain.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_ip:
                mIndex = 1;
                selectTab();
                mLlIp.setVisibility(View.VISIBLE);
                mLlDomain.setVisibility(View.GONE);
                break;
        }
    }

    private void selectTab() {
        for (int i = 0; i < tvs.length; i++) {
            if (i == mIndex) {
                tvs[i].setSelected(true);
            } else {
                tvs[i].setSelected(false);
            }
        }
        moveAnimation(mIndex);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 2
                                * index).setDuration(300).start();
    }
}
