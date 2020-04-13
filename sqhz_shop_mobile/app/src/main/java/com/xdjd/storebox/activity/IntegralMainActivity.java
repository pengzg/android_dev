package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.base.BaseConfig;
import com.xdjd.storebox.bean.PersonBean;
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
 * 积分主界面
 * Created by lijipei on 2017/2/24.
 */

public class IntegralMainActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.integral_tv)
    TextView mIntegralTv;
    @BindView(R.id.integral_detail_ll)
    LinearLayout mIntegralDetailLl;
    @BindView(R.id.integral_store_ll)
    LinearLayout mIntegralStoreLl;
    @BindView(R.id.integral_award_ll)
    LinearLayout mIntegralAwardLl;
    @BindView(R.id.integral_order_ll)
    LinearLayout mIntegralOrderLl;
    @BindView(R.id.beat_percent)
    TextView beatPercent;
    private String totalI;//总积分

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_main);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData(){
        titleBar.leftBack(this);
        titleBar.setTitle("我的积分");
        titleBar.setRightText("积分规则");
        titleBar.setRightTextColor(R.color.text_black_212121);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntegralMainActivity.this,CommonWebActivity.class);
                intent.putExtra("title","积分规则");
                intent.putExtra("url", BaseConfig.URL+"/"+UserInfoUtils.getInteRuleUrl(IntegralMainActivity.this));
                startActivity(intent);
                //LogUtils.e("url",BaseConfig.URL+"/"+UserInfoUtils.getInteRuleUrl(IntegralMainActivity.this));
                //startActivity(IntegralConfirmOrderActivity.class);
            }
        });
    }


    @OnClick({R.id.integral_detail_ll, R.id.integral_store_ll, R.id.integral_award_ll, R.id.integral_order_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.integral_detail_ll://积分明细
                Intent intent = new Intent(IntegralMainActivity.this,MyIntegralActivity.class);
                intent.putExtra("totalI",totalI);
                startActivity(intent);
                break;
            case R.id.integral_store_ll://积分商城
                startActivity(IntegralStoreActivity.class);
                break;
            case R.id.integral_award_ll://积分抽奖
                break;
            case R.id.integral_order_ll://积分订单
                startActivity(IntegralOrderActivity.class);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetMeInfo(UserInfoUtils.getId(this),UserInfoUtils.getId(this));
    }

    /*个人信息请求*/
    private void GetMeInfo(String uid,String userId) {
        AsyncHttpUtil<PersonBean> httpUtil = new AsyncHttpUtil<>(this, PersonBean.class, new IUpdateUI<PersonBean>() {
            @Override
            public void updata(PersonBean bean ) {
                if(bean.getRepCode().equals("00")){
                    mIntegralTv.setText(bean.getIntegrate());//总积分
                    beatPercent.setText(bean.getBeat_percent());//打败比例
                    totalI = bean.getIntegrate();
                }else{
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.GetuserInfo, L_RequestParams.getUserInfo(uid,userId), false);
    }

}
