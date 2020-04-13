package com.xdjd.distribution.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.WinningRecordAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.WinningListBean;
import com.xdjd.distribution.event.ShopAuditEvent;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lijipei on 2017/11/1.
 */

public class ShopAuditDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.tv_no_check)
    TextView mTvNoCheck;
    @BindView(R.id.tv_check)
    TextView mTvCheck;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    private WinningRecordAdapter adapter;
    List<WinningListBean> listNoWinning = new ArrayList<>();//未结算客户兑奖列表
    List<WinningListBean> listWinning = new ArrayList<>();//客户兑奖列表

    private String dateStartStr;
    private String dateEndStr;
    private String shopId;
    private String prizeId;
    private String goodsName;

    private int page = 1;
    private int mFlag = 0;

    private TextView[] tvs;
    public int mIndex = 0;

    private int mwStatus = 1;//1未结算 2已结算

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_audit_detail;
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
        mTitleBar.setTitle("店铺结算详情");

        tvs = new TextView[]{mTvNoCheck, mTvCheck};
        tvs[mIndex].setSelected(true);
        //计算线的宽度
        mLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;

        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        shopId = getIntent().getStringExtra("shopId");
        prizeId = getIntent().getStringExtra("prizeId");
        goodsName = getIntent().getStringExtra("goodsName");

        adapter = new WinningRecordAdapter(null);
        mLvNoScroll.setAdapter(adapter);

        //        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.DISABLED);
        /*mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                listWinning.clear();
                adapter.notifyDataSetChanged();
                getCustomerWinningList();
                mPullScroll.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                getCustomerWinningList();
                mPullScroll.onRefreshComplete();
            }
        });*/

        getCustomerWinningList();
    }

    @OnClick({R.id.tv_no_check, R.id.tv_check,R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_no_check:
                mIndex = 0;
                mwStatus = 1;
                selectTab();

                listNoWinning.clear();
                adapter.setData(listNoWinning);
                getCustomerWinningList();
                break;
            case R.id.tv_check:
                mIndex = 1;
                mwStatus = 2;
                selectTab();
                mLlBottom.setVisibility(View.GONE);

                listWinning.clear();
                adapter.setData(listWinning);
                getCustomerWinningList();
                break;
            case R.id.btn_submit:
                DialogUtil.showCustomDialog(ShopAuditDetailActivity.this, "提示", "本次确定结算"+mTvTotalNum.getText().toString()+"箱商品?",
                        "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                updateCheckState();
                            }

                            @Override
                            public void no() {

                            }
                        });
                break;
        }
    }

    /**
     * 获取已兑奖客户列表
     */
    private void getCustomerWinningList() {
        AsyncHttpUtil<WinningListBean> httpUtil = new AsyncHttpUtil<>(this, WinningListBean.class, new IUpdateUI<WinningListBean>() {
            @Override
            public void updata(WinningListBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getHxList() != null && jsonStr.getHxList().size() > 0) {
                        if (mwStatus == 1) {//未结算商品
                            listNoWinning = jsonStr.getHxList();
                            adapter.setData(listNoWinning);
                            mTvTotalNum.setText(jsonStr.getNum()+jsonStr.getUnit());
                            mLlBottom.setVisibility(View.VISIBLE);
                        } else {
                            listWinning = jsonStr.getHxList();
                            adapter.setData(listWinning);
                        }
                    } else {
                        mLlBottom.setVisibility(View.GONE);
                        showToast("暂无数据");
                    }
                } else {
                    showToast(jsonStr.getRepMsg());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(s.getDetail());
            }

            @Override
            public void finish() {
                mPullScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.queryJsDetailList, L_RequestParams.queryJsDetailList(dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"),
                shopId, String.valueOf(page), "999", String.valueOf(mwStatus), goodsName,prizeId, ""), true);
    }

    private void updateCheckState() {
        if (listNoWinning == null || listNoWinning.size() == 0) {
            showToast("还没有可结算的商品!");
            return;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < listNoWinning.size(); i++) {
            sb.append(listNoWinning.get(i).getMw_id());
            if (i != listNoWinning.size() - 1) {
                sb.append(",");
            }
        }

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    ToastUtils.showToastInCenterSuccess(ShopAuditDetailActivity.this, jsonStr.getRepMsg());
                    EventBus.getDefault().post(new ShopAuditEvent());
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
        httpUtil.post(M_Url.updateCheckState, L_RequestParams.updateCheckState(sb.toString()), true);
    }


    private void selectTab() {
        mLvNoScroll.setAdapter(adapter);

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
