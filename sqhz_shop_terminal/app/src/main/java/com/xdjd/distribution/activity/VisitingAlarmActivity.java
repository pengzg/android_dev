package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.StatusBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.fragment.VisitingAlarmFragment;
import com.xdjd.distribution.fragment.VisitingBeyondFragment;
import com.xdjd.distribution.popup.SelectOrderStatusPopup;
import com.xdjd.distribution.popup.SelectSalesmanPopup;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisitingAlarmActivity extends BaseActivity implements SelectSalesmanPopup.SalesmanSearchListener,
        SelectOrderStatusPopup.ItemOnListener{

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.bg_view)
    View mBgView;
    @BindView(R.id.tv_left)
    TextView mTvLeft;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_salesman)
    TextView mTvSalesman;
    @BindView(R.id.ll_select_salesman)
    LinearLayout mLlSelectSalesman;
    @BindView(R.id.tv_category)
    TextView mTvCategory;
    @BindView(R.id.ll_select_category)
    LinearLayout mLlSelectCategory;
    @BindView(R.id.ll_salesman)
    LinearLayout mLlSalesman;

    private UserBean userBean;

    private FragmentManager fm;
    private int currentTab = 0; // 当前Tab页面索引

    private List<Fragment> fragments;
    private VisitingAlarmFragment mAlarmFragment;
    private VisitingBeyondFragment mListFragment;

    /**
     * 业务员列表
     */
    private List<SalesdocListBean> listSalesman;

    /**
     * 业务员选择popup
     */
    private SelectSalesmanPopup popupSalesman;

    /**
     * 业务员
     */
    public String salesid = "";
    /**
     * 业务员名称
     */
    private String salesName = "";

    /**
     * 是否从管理端跳转过来的
     */
    private boolean isManage = false;
    public int phType = 0;//0全部 1已铺货 2.未铺货

    private SelectOrderStatusPopup popupOrderStatus;
    private List<StatusBean> listStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_visiting_alarm;
    }

    @Override
    protected void initData() {
        super.initData();
        isManage = getIntent().getBooleanExtra("isManage", false);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("拜访提醒列表");

        /*if (isManage) {
            mTitleBar.setRightImageResource(R.mipmap.search);
            mTitleBar.setRightLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listSalesman == null || listSalesman.size() == 0) {
                        querySalesmanList(true, 1, "");
                    } else {
                        showPwSalesman();
                    }
                }
            });
        }*/
        if (isManage) {
            mLlSalesman.setVisibility(View.VISIBLE);
        }
        //0全部 1已铺货 2.未铺货
        listStatus.add(new StatusBean(0, "全部"));
        listStatus.add(new StatusBean(1, "已铺货"));
        listStatus.add(new StatusBean(2, "未铺货"));

        userBean = UserInfoUtils.getUser(this);

        mAlarmFragment = new VisitingAlarmFragment();
        mListFragment = new VisitingBeyondFragment();
        fragments = new ArrayList<>();
        fragments.add(mAlarmFragment);
        fragments.add(mListFragment);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //        if (!"".equals(userBean.getSu_storeid()) && userBean.getSu_usertype().equals(BaseConfig.userTypeCardSell)) {
        ft.add(R.id.fl_comment, fragments.get(0));
        currentTab = 0;
        //        } else {
        //            ft.add(R.id.fl_comment, fragments.get(1));
        //            currentTab = 1;
        //        }

        ft.commit();

        mLlSelectCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlSelectCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPhTypePopup();
            }
        });
        initSalesmanPopup();
    }

    @OnClick({R.id.tv_left, R.id.tv_right, R.id.ll_select_salesman, R.id.ll_select_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                mTvLeft.setTextColor(UIUtils.getColor(R.color.white));
                mTvRight.setTextColor(UIUtils.getColor(R.color.text_gray));

                if (!fragments.get(0).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(0));
                    ft.commit();
                } else {
                    showTab(0);
                }
                moveAnimation(mTvLeft);
                break;
            case R.id.tv_right:
                mTvRight.setTextColor(UIUtils.getColor(R.color.white));
                mTvLeft.setTextColor(UIUtils.getColor(R.color.text_gray));

                if (!fragments.get(1).isAdded()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fl_comment, fragments.get(1));
                    ft.commit();
                } else {
                    showTab(1);
                }
                moveAnimation(mTvRight);
                break;
            case R.id.ll_select_salesman:
                if (listSalesman == null || listSalesman.size() == 0) {
                    querySalesmanList(true, 1, "");
                } else {
                    showPwSalesman();
                }
                break;
            case R.id.ll_select_category:
                showPhTypePopup();
                break;
        }
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = fm.beginTransaction();

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    private void moveAnimation(TextView tv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBgView, "translationX", mBgView.getTranslationX(), tv.getLeft());
        animator.addListener(animatorListener);
        animator.setDuration(400).start();
    }

    private MyAnimatorListener animatorListener = new MyAnimatorListener();

    public class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animator.removeListener(animatorListener);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }


    /**
     * 获取业务员列表接口
     */
    private void querySalesmanList(final boolean isDialog, int salesmanType, String searchStr) {
        AsyncHttpUtil<SalesdocListBean> httpUtil = new AsyncHttpUtil<>(this, SalesdocListBean.class, new IUpdateUI<SalesdocListBean>() {
            @Override
            public void updata(SalesdocListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listSalesman = jsonBean.getDataList();
                    if (listSalesman != null && listSalesman.size() > 0) {
                        SalesdocListBean bean = new SalesdocListBean();
                        bean.setSu_id("");
                        bean.setSu_name("全部");
                        listSalesman.add(0,bean);
                        showPwSalesman();
                    } else {
                        showToast("没有业务员数据");
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
            }
        });
        httpUtil.post(M_Url.getSalesdocList,
                L_RequestParams.getSalesdocList(searchStr, String.valueOf(salesmanType)), isDialog);
    }

    /**
     * 初始化业务员popup
     */
    private void initSalesmanPopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupSalesman = new SelectSalesmanPopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                salesid = listSalesman.get(position).getSu_id();
                salesName = listSalesman.get(position).getSu_name();
                mTvSalesman.setText(salesName);
                popupSalesman.dismiss();

                if (salesid == null) {
                    salesid = "";
                }

                mAlarmFragment.updateDate(salesid);
                if (fragments.get(1).isAdded()) {
                    mListFragment.updateDate(salesid);
                }
            }
        }, this);
    }

    /**
     * 显示员工popup
     */
    private void showPwSalesman() {
        if (!popupSalesman.isShowing()) {
            popupSalesman.setData(listSalesman);
            popupSalesman.setId(salesid);
            // 显示窗口
            popupSalesman.showAtLocation(mLlMain,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        } else {
            popupSalesman.setData(listSalesman);
            popupSalesman.setId(salesid);
        }
    }

    @Override
    public void onSearch(int salesmanType, String searchStr) {
        //搜索员工
        querySalesmanList(true, salesmanType, searchStr);
    }

    private void initPhTypePopup() {
        popupOrderStatus = new SelectOrderStatusPopup(this, mLlSelectCategory, this);
        popupOrderStatus.setData(listStatus);
    }

    private void showPhTypePopup() {
        popupOrderStatus.setData(listStatus);
        popupOrderStatus.showAsDropDown(mLlSelectCategory, 0, UIUtils.dp2px(2));
    }

    @Override
    public void onItem(int i) {
        popupOrderStatus.dismiss();
        phType = listStatus.get(i).getType();
        mTvCategory.setText(listStatus.get(i).getTypeName());

        mAlarmFragment.updatePhType(phType);
        if (fragments.get(1).isAdded()) {
            mListFragment.updatePhType(phType);
        }
    }

}
