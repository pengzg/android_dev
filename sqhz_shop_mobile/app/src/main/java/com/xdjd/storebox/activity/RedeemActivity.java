package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.GiftPrizeDetailBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/10/29.
 * 兑奖核销界面
 */

public class RedeemActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_action_name)
    TextView mTvActionName;
    @BindView(R.id.tv_prize_time)
    TextView mTvPrizeTime;
    @BindView(R.id.tv_prize_name)
    TextView mTvPrizeName;
    @BindView(R.id.tv_redeem_code)
    TextView mTvRedeemCode;
    @BindView(R.id.iv_customer_head)
    CircleImageView mIvCustomerHead;
    @BindView(R.id.btn_redeem)
    Button mBtnRedeem;
    @BindView(R.id.tv_customer_name)
    TextView mTvCustomerName;
    @BindView(R.id.tv_customer_phone)
    TextView mTvCustomerPhone;
    @BindView(R.id.tv_state)
    TextView mTvState;

    private String prizeCode;//中奖码
    private GiftPrizeDetailBean bean = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        ButterKnife.bind(this);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("核销详情");

        prizeCode = getIntent().getStringExtra("prizeCode");
        loadData(prizeCode);
    }


    @OnClick({R.id.btn_redeem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_redeem://奖品核销
                DialogUtil.showCustomDialog(this, "提示", "是否确定进行奖品核销?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        checkGiftWinning();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    private void loadData(String prizeCode) {
        AsyncHttpUtil<GiftPrizeDetailBean> httpUtil = new AsyncHttpUtil<>(this, GiftPrizeDetailBean.class, new IUpdateUI<GiftPrizeDetailBean>() {
            @Override
            public void updata(GiftPrizeDetailBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    bean = jsonStr;
                    setData(jsonStr);
                } else {
                    DialogUtil.showCustomDialog(RedeemActivity.this, "提示", jsonStr.getRepMsg(), "确定", null, new DialogUtil.MyCustomDialogListener2() {
                        @Override
                        public void ok() {
                            finishActivity();
                        }

                        @Override
                        public void no() {

                        }
                    }, false, false);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                DialogUtil.showCustomDialog(RedeemActivity.this, "提示", s.getDetail(), "确定", null, new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        finishActivity();
                    }

                    @Override
                    public void no() {

                    }
                }, false, false);
            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.getGiftPrizeDetailByCode, L_RequestParams.getGiftPrizeDetailByCode(UserInfoUtils.getId(this), prizeCode), true);
    }

    private void setData(GiftPrizeDetailBean bean) {
        mTvActionName.setText("活动名称:"+bean.getMw_activity_name());
        mTvPrizeName.setText(/*bean.getMw_productname() + "x" + bean.getMw_num() +*/ "奖品描述:"+bean.getMw_nums_desc());
        mTvPrizeTime.setText("中奖时间:"+bean.getMw_winningtime());

        mTvRedeemCode.setText(prizeCode);
        mTvState.setText(bean.getMw_state_nameref());

        if (bean.getMw_member_mb_img() != null && bean.getMw_member_mb_img().length() > 0) {
            Glide.with(this).load(bean.getMw_member_mb_img()).
                    asBitmap().into(mIvCustomerHead);
        }
        mTvCustomerName.setText(bean.getMw_member_name());
        mTvCustomerPhone.setText(bean.getMw_member_phone());

        if ("1".equals(bean.getMw_state())) {//领取状态 1未领取 2 已领取 3 过期
            mBtnRedeem.setVisibility(View.VISIBLE);
        } else {
            mBtnRedeem.setVisibility(View.GONE);
        }
    }

    private void checkGiftWinning() {
        AsyncHttpUtil<GiftPrizeDetailBean> httpUtil = new AsyncHttpUtil<>(this, GiftPrizeDetailBean.class, new IUpdateUI<GiftPrizeDetailBean>() {
            @Override
            public void updata(GiftPrizeDetailBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(RedeemActivity.this,jsonStr.getRepMsg());
                    finishActivity();
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
        httpUtil.post(M_Url.checkGiftWinning, L_RequestParams.checkGiftWinning(UserInfoUtils.getId(this), bean.getMw_id()), true);
    }

}
