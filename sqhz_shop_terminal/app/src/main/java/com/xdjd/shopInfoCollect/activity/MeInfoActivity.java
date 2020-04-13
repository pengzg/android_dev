package com.xdjd.shopInfoCollect.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.steward.bean.CustomerNumBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/29.
 */

public class MeInfoActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.person_info)
    LinearLayout personInfo;
    @BindView(R.id.today_num)
    TextView todayNum;
    @BindView(R.id.total_num)
    TextView totalNum;
    private String startDate;
    private String endDate;
    private UserBean userBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initData() {
        super.initData();
        userBean = UserInfoUtils.getUser(this);
        mobile.setText(userBean.getUserName());
        titleBar.leftBack(this);
        titleBar.setTitle("个人信息");
        startDate = DateUtils.getDataTime(DateUtils.dateFormater4);
        endDate = DateUtils.getDataTime(DateUtils.dateFormater4);
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void loadData(){
        AsyncHttpUtil<CustomerNumBean> httpUtil = new AsyncHttpUtil<>(MeInfoActivity.this, CustomerNumBean.class, new IUpdateUI<CustomerNumBean>() {
            @Override
            public void updata(CustomerNumBean bean) {
                if(bean.getRepCode().equals("00")){
                    todayNum.setText("今日采集数："+String.valueOf(bean.getNewCustomerNum()));
                    totalNum.setText("累计采集数："+String.valueOf(bean.getTotalCustomerNum()));
                }else{
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
        httpUtil.post(M_Url.getCustomerNum, G_RequestParams.getCustomerNum(startDate.replace(".","-"), endDate.replace(".","-")), true);
    }
}
