package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.CustomerBalanceBean;
import com.xdjd.distribution.event.OpenMallEvent;
import com.xdjd.distribution.fragment.ClientOrderListFragment;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.AnimatedExpandableListView;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.roundedimage.RoundedImageView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ClientDetailActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.client_img)
    RoundedImageView mClientImg;
    @BindView(R.id.client_name)
    TextView mClientName;
    @BindView(R.id.client_contacts_name)
    TextView mClientContactsName;
    @BindView(R.id.client_balance)
    TextView mClientBalance;
    @BindView(R.id.client_tel)
    TextView mClientTel;
    @BindView(R.id.message_ll)
    LinearLayout mMessageLl;
    @BindView(R.id.client_message_ll)
    LinearLayout mClientMessageLl;
    @BindView(R.id.elv_order_goods)
    AnimatedExpandableListView mElvOrderGoods;
    @BindView(R.id.rl_wx_open)
    RelativeLayout mRlWxOpen;
    @BindView(R.id.rl_mobile_open)
    RelativeLayout mRlMobileOpen;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.rl_alert_main)
    RelativeLayout mRlAlertMain;
    @BindView(R.id.tv_go_binding_wx)
    TextView mTvGoBindingWx;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.tv_add_remark)
    TextView mTvAddRemark;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;
    //1.订单;2.订货;3.促销活动;4.应收款;5.收付款;6.拜访记录;7铺货查询
    private int indexType = 1;

    private ArrayList<Fragment> fragments;
    private List<String> list = new ArrayList<>();

    private MyOrderListAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_client_detail;
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
        EventBus.getDefault().register(this);
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("客户信息卡");
        mTitleBar.setRightImageResource(R.drawable.setting);
        mTitleBar.setRightLeftImageLayout(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientDetailActivity.this, CustomerInformationActivity.class);
                intent.putExtra("customerId", clientBean.getCc_id());
                startActivity(intent);
            }
        });

        //从客户列表跳转过来的
        clientBean = (ClientBean) getIntent().getSerializableExtra("customer");

        if (clientBean == null) {//如果没有客户信息,则是从首页跳转过来的
            clientBean = UserInfoUtils.getClientInfo(this);
            //            if ("Y".equals(userBean.getIsShopAccount())) {
            //                mTvOpenMall.setVisibility(View.VISIBLE);
            //            }
        } else {
            //            mTvOpenMall.setVisibility(View.GONE);
        }

        mClientName.setText(clientBean.getCc_name());
        mClientContactsName.setText(clientBean.getCc_contacts_name());
        mClientTel.setText(clientBean.getCc_contacts_mobile());

        if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
            Glide.with(this).load(clientBean.getCc_image())
                    .error(R.mipmap.customer_img)
                    .into(mClientImg);
        } else {
            mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
        }

        /*if ("Y".equals(userBean.getIsShopAccount())) {
            mTvOpenMall.setVisibility(View.VISIBLE);
            if ("Y".equals(clientBean.getCc_isaccount())) {
                mTvOpenMall.setText("已开通");
                mTvOpenMall.setEnabled(false);
            } else {
                mTvOpenMall.setText("开通商城");
                mTvOpenMall.setEnabled(true);
            }
        }*/
        getCustomerInfo(clientBean.getCc_id());
        getCustomerBalance();

        list.add("订单");
        list.add("订货");
        list.add("促销活动");
        list.add("应收款");
        list.add("收付款");
        list.add("拜访记录");
        list.add("铺货");

        fragments = new ArrayList<>();
        fragments.add(new ClientOrderListFragment(Comon.CD_ORDER));
        fragments.add(new ClientOrderListFragment(Comon.CD_DH));
        fragments.add(new ClientOrderListFragment(Comon.CD_ACTION));
        fragments.add(new ClientOrderListFragment(Comon.CD_YS));
        fragments.add(new ClientOrderListFragment(Comon.CD_SFK));
        fragments.add(new ClientOrderListFragment(Comon.CD_VISIT));
        fragments.add(new ClientOrderListFragment(Comon.CD_PH));

        adapter = new MyOrderListAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setAdapter(adapter);

        initMagicIndicator();
    }

    private void initMagicIndicator() {
        mMagicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(UIUtils.getColor(R.color.text_gray_b3b3));
                simplePagerTitleView.setSelectedColor(UIUtils.getColor(R.color.text_blue));
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(UIUtils.getColor(R.color.color_blue));
                return linePagerIndicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewpager);
    }


    class MyOrderListAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private FragmentManager fm;


        public MyOrderListAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyOrderListAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
        }

        /**
         * 重写此方法,返回页面标题,用于viewpagerIndicator的页签显示
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
            return fragment;
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            if (this.fragments != null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.fragments) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
            this.fragments = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            fragment = fragments.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("id", "" + position);
            fragment.setArguments(bundle);
            return fragment;
            //			return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //            super.destroyItem(container, position, object);
            Fragment fragment = fragments.get(position);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        }
    }


    private void getCustomerBalance() {
        AsyncHttpUtil<CustomerBalanceBean> httpUtil = new AsyncHttpUtil<>(this, CustomerBalanceBean.class, new IUpdateUI<CustomerBalanceBean>() {
            @Override
            public void updata(CustomerBalanceBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    UserInfoUtils.setCustomerBalance(ClientDetailActivity.this, jsonBean.getBalance());
                    UserInfoUtils.setAfterAmount(ClientDetailActivity.this, jsonBean.getGcb_after_amount());
                    UserInfoUtils.setSafetyArrearsNum(ClientDetailActivity.this, jsonBean.getCc_safety_arrears_num());
                    mClientBalance.setText(jsonBean.getBalance());
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getCustomerBalance, L_RequestParams.
                getCustomerBalance(clientBean.getCc_id()), false);
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo(String cusId) {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    clientBean.setCc_openid(jsonBean.getCc_openid());
                    clientBean.setCc_isaccount(jsonBean.getCc_isaccount());
                    /*if ("Y".equals(userBean.getIsShopAccount())) {
                        mTvOpenMall.setVisibility(View.VISIBLE);
                        if ("Y".equals(clientBean.getCc_isaccount())) {
                            mTvOpenMall.setText("已开通");
                            mTvOpenMall.setEnabled(false);

                            if (clientBean.getCc_openid() == null || "".equals(clientBean.getCc_openid())){
                                mTvGoBindingWx.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mTvOpenMall.setText("开通商城");
                            mTvOpenMall.setEnabled(true);
                        }
                    }*/
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(this) + "",
                cusId), true);
    }

    @OnClick({R.id.tv_open_mall, R.id.tv_add_remark,R.id.rl_wx_open, R.id.rl_mobile_open, R.id.iv_close, R.id.rl_alert_main, R.id.tv_go_binding_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_remark://添加备注

                break;
            case R.id.tv_open_mall://开通商城
                if (mRlAlertMain.getVisibility() == View.INVISIBLE) {
                    mRlAlertMain.setVisibility(View.VISIBLE);
                    startOpenAnimation(true);
                } else {
                    startOpenAnimation(false);
                }
                break;
            case R.id.rl_wx_open://微信开通商城
                startOpenAnimation(false);
                showQrcodeImg();
                break;
            case R.id.rl_mobile_open://手机开通商城
                if (clientBean.getCc_contacts_mobile() == null || "".equals(clientBean.getCc_contacts_mobile())) {
                    DialogUtil.showCustomDialog(this, "提示", "请先去完善客户手机号码,再开通商城!", "确定", null, null);
                    return;
                }
                Intent intent = new Intent(this, OpenMallActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_close:
                startOpenAnimation(false);
                break;
            case R.id.rl_alert_main:
                startOpenAnimation(false);
                break;
            case R.id.tv_go_binding_wx:
                DialogUtil.showCustomDialog(this, "提示", "是否去绑定微信?",
                        "去绑定", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                showQrcodeImg();
                            }

                            @Override
                            public void no() {
                            }
                        });
                break;
        }
    }

    private void showQrcodeImg() {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    Intent intent = new Intent(ClientDetailActivity.this, CommonWebActivity.class);
                    intent.putExtra("title", "绑定微信");
                    intent.putExtra("url", jsonBean.getImgUrl());
                    startActivity(intent);
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
        httpUtil.post(M_Url.showQrcodeImg, L_RequestParams.showQrcodeImg(clientBean.getCc_id()), true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(OpenMallEvent event) {
        clientBean = event.getClientBean();
        mClientName.setText(clientBean.getCc_name());
        mClientContactsName.setText(clientBean.getCc_contacts_name());
        mClientTel.setText(clientBean.getCc_contacts_mobile());

        if (clientBean.getCc_image() != null && !"".equals(clientBean.getCc_image())) {
            Glide.with(this).load(clientBean.getCc_image())
                    .error(R.mipmap.customer_img)
                    .into(mClientImg);
        } else {
            mClientImg.setImageDrawable(UIUtils.getDrawable(R.mipmap.customer_img));
        }
        /*if ("Y".equals(clientBean.getCc_isaccount())) {
            mTvOpenMall.setText("已开通");
            mTvOpenMall.setEnabled(false);
        } else {
            mTvOpenMall.setText("开通商城");
            mTvOpenMall.setEnabled(true);
        }*/
    }

    private void startOpenAnimation(final boolean b) {
        if (b) {
            if (mRlWxOpen.getVisibility() == View.INVISIBLE) {
                mRlWxOpen.setVisibility(View.VISIBLE);
                startTranstateAnimation(mRlWxOpen, true, 2);
            }
        } else {
            mRlAlertMain.setEnabled(false);
            startTranstateAnimation(mRlWxOpen, false, 2);
            startTranstateAnimation(mRlMobileOpen, false, 2);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
            alphaAnimation.setDuration(600);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRlAlertMain.setEnabled(true);
                    mRlAlertMain.setVisibility(View.INVISIBLE);

                    mRlWxOpen.setVisibility(View.INVISIBLE);
                    mRlMobileOpen.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mRlAlertMain.startAnimation(alphaAnimation);
        }
    }


    private void startTranstateAnimation(final View v, boolean open, int position) {
        if (open) {
            LogUtils.e("startTranstateAnimation", "Open.getY()=" + mRlWxOpen.getY() + ",v.getY()=" + v.getY());
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "y",
                    mRlWxOpen.getY(),
                    v.getY() - UIUtils.dp2px(20),
                    v.getY() + UIUtils.dp2px(10),
                    v.getY()).setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (v.getId() == mRlWxOpen.getId()) {
                        if (mRlMobileOpen.getVisibility() == View.INVISIBLE) {
                            mRlMobileOpen.setVisibility(View.VISIBLE);
                            startTranstateAnimation(mRlMobileOpen, true, 3);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mIvClose.setEnabled(true);
        } else {
            mIvClose.setEnabled(false);
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_PARENT, 0.4f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(500);
            animationSet.addAnimation(translateAnimation);
            animationSet.setFillAfter(false);
            v.startAnimation(animationSet);
        }
    }
}
