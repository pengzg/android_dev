package com.xdjd.distribution.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.event.OpenMallEvent;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/2
 *     desc   : 编辑客户信息activity
 *     version: 1.0
 * </pre>
 */

public class CustomerInformationActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.rl_alert_main)
    RelativeLayout mRlAlertMain;
    @BindView(R.id.rl_wx_open)
    RelativeLayout mRlWxOpen;
    @BindView(R.id.rl_mobile_open)
    RelativeLayout mRlMobileOpen;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.tv_cc_name)
    TextView mTvCcName;
    @BindView(R.id.mobile_del)
    LinearLayout mMobileDel;
    @BindView(R.id.cs_image)
    CircleImageView mCsImage;
    @BindView(R.id.cs_shop_photo)
    LinearLayout mCsShopPhoto;
    @BindView(R.id.tv_shop_account)
    TextView mTvShopAccount;
    @BindView(R.id.ll_shop_account)
    LinearLayout mLlShopAccount;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.ll_wx)
    LinearLayout mLlWx;
    @BindView(R.id.tv_store)
    TextView mTvStore;
    @BindView(R.id.tv_category)
    TextView mTvCategory;
    @BindView(R.id.tv_channel)
    TextView mTvChannel;
    @BindView(R.id.ll_main)
    RelativeLayout mLlMain;
    @BindView(R.id.ll_open_store)
    LinearLayout mLlOpenStore;
    @BindView(R.id.ll_store_main)
    LinearLayout mLlStoreMain;
    @BindView(R.id.tv_open_mall)
    LinearLayout mTvOpenMall;
    @BindView(R.id.tv_isopen_store)
    TextView mTvIsopenStore;
    @BindView(R.id.iv_right_isopen)
    ImageView mIvRightIsopen;
    @BindView(R.id.tv_contacts_name)
    TextView mTvContactsName;
    @BindView(R.id.tv_contacts_mobile)
    TextView mTvContactsMobile;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.scroll)
    ScrollView mScroll;
    @BindView(R.id.iv_wx_right_arrow)
    ImageView mIvWxRightArrow;
    @BindView(R.id.tv_price_grade)
    TextView mTvPriceGrade;

    /**
     * 选择的客户信息
     */
    private ClientBean customerBean;
    private UserBean userBean;

    private String customerId;

    private VaryViewHelper mVaryViewHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_information;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        super.initData();
        customerId = getIntent().getStringExtra("customerId");
        userBean = UserInfoUtils.getUser(this);
        mVaryViewHelper = new VaryViewHelper(mScroll);

        mTitleBar.leftBack(this);
        mTitleBar.setTitle("客户信息");
        if (UserInfoUtils.getClientInfo(this) != null
                && UserInfoUtils.getClientInfo(this).getCc_id().equals(customerId)) {//只有签到客户才可以编辑信息
            mTitleBar.setRightText("编辑客户资料");
            mTitleBar.setRightTextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CustomerInformationActivity.this, EditCustomerInformationActivity.class);
                    intent.putExtra("clientBean", customerBean);
                    startActivity(intent);
                }
            });
        }
        /*mTvCcName.setText(customerBean.getCc_name());
        mTvContactsName.setText(customerBean.getCc_contacts_name());
        mTvContactsMobile.setText(customerBean.getCc_contacts_mobile());
        if (customerBean.getCc_image() != null && customerBean.getCc_image().length() > 0) {
            Glide.with(this).
                    load(customerBean.getCc_image()).
                    error(R.mipmap.photo).
                    into(mCsImage);
        }
        mTvStore.setText(customerBean.getCc_depotid_nameref());
        mTvCategory.setText(customerBean.getCc_categoryid_nameref());
        mTvChannel.setText(customerBean.getCc_channelid_nameref());
        mTvAddress.setText(customerBean.getCc_address());

        if ("Y".equals(userBean.getIsShopAccount())) {
            mTvOpenMall.setVisibility(View.VISIBLE);
            if ("Y".equals(customerBean.getCc_isaccount())) {//是否开通商城
                mTvIsopenStore.setText("已开通");
                mIvRightIsopen.setVisibility(View.INVISIBLE);
                mLlStoreMain.setVisibility(View.VISIBLE);
                if (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0) {
                    mTvWx.setText("已绑定");
                } else {
                    mTvWx.setText("未绑定");
                }
            } else {
                mTvIsopenStore.setText("开通商城");
                mLlStoreMain.setVisibility(View.GONE);
            }
        }*/

        getCustomerInfo();
    }

    @OnClick({R.id.rl_wx_open, R.id.rl_mobile_open, R.id.iv_close, R.id.rl_alert_main,
            R.id.ll_open_store, R.id.ll_wx})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_wx_open://微信开通商城
                startOpenAnimation(false);
                showQrcodeImg();
                break;
            case R.id.rl_mobile_open://手机开通商城
                if (customerBean == null)
                    return;
                if (customerBean.getCc_contacts_mobile() == null || "".equals(customerBean.getCc_contacts_mobile())) {
                    DialogUtil.showCustomDialog(this, "提示", "请先去完善客户手机号码,再开通商城!", "确定", null, null);
                    return;
                }
                intent = new Intent(this, OpenMallActivity.class);
                intent.putExtra("clientBean", customerBean);
                startActivity(intent);
                break;
            case R.id.iv_close:
                startOpenAnimation(false);
                break;
            case R.id.rl_alert_main:
                startOpenAnimation(false);
                break;
            case R.id.ll_open_store://开通商城
                if (userBean.getSu_usertype().equals("5")) {//管理员禁止点击
                    return;
                }
                if (UserInfoUtils.getClientInfo(this) == null
                        || !UserInfoUtils.getClientInfo(this).getCc_id().equals(customerId))
                    return;
                if (!"Y".equals(customerBean.getCc_isaccount())) {
                    if (mRlAlertMain.getVisibility() == View.INVISIBLE) {
                        mRlAlertMain.setVisibility(View.VISIBLE);
                        startOpenAnimation(true);
                    } else {
                        startOpenAnimation(false);
                    }
                } else {
                    showToast("已经开通商城!");
                }
                break;
            case R.id.ll_wx:
                if (userBean.getSu_usertype().equals("5")) {//管理员禁止点击
                    return;
                }
                if (customerBean != null && (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0)) {
                    showQrcodeImg();
                }
                break;
        }
    }

    private void showQrcodeImg() {
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    Intent intent = new Intent(CustomerInformationActivity.this, CommonWebActivity.class);
                    intent.putExtra("title", "绑定微信");
                    intent.putExtra("url", jsonBean.getImgUrl());
                    intent.putExtra("isOpenWx", true);
                    startActivityForResult(intent, 100);
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
        httpUtil.post(M_Url.showQrcodeImg, L_RequestParams.showQrcodeImg(customerBean.getCc_id()), true);
    }

    /**
     * 获取客户详情接口
     */
    private void getCustomerInfo() {
        mVaryViewHelper.showLoadingView();
        AsyncHttpUtil<ClientBean> httpUtil = new AsyncHttpUtil<>(this, ClientBean.class, new IUpdateUI<ClientBean>() {
            @Override
            public void updata(ClientBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    /*mTvContactsMobile.setText(jsonBean.getCc_contacts_mobile());
                    customerBean.setCc_image(jsonBean.getCc_image());
                    customerBean.setCc_openid(jsonBean.getCc_openid());
                    customerBean.setCc_isaccount(jsonBean.getCc_isaccount());
                    UserInfoUtils.setClientInfo(CustomerInformationActivity.this, customerBean);
                    EventBus.getDefault().post(new OpenMallEvent());
                    if ("Y".equals(userBean.getIsShopAccount())) {
                        mTvOpenMall.setVisibility(View.VISIBLE);
                        if ("Y".equals(customerBean.getCc_isaccount())) {//是否开通商城
                            mTvIsopenStore.setText("已开通");
                            mIvRightIsopen.setVisibility(View.INVISIBLE);
                            mLlStoreMain.setVisibility(View.VISIBLE);
                            if (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0) {
                                mTvWx.setText("未绑定");
                            } else {
                                mTvWx.setText("已绑定");
                            }
                        } else {
                            mTvIsopenStore.setText("开通商城");
                            mLlStoreMain.setVisibility(View.GONE);
                        }
                    }*/
                    mVaryViewHelper.showDataView();
                    customerBean = jsonBean;

                    mTvCcName.setText(jsonBean.getCc_name());
                    mTvContactsName.setText(jsonBean.getCc_contacts_name());
                    mTvContactsMobile.setText(jsonBean.getCc_contacts_mobile());
                    if (jsonBean.getCc_image() != null && jsonBean.getCc_image().length() > 0) {
                        Glide.with(CustomerInformationActivity.this).
                                load(jsonBean.getCc_image()).
                                error(R.mipmap.photo).
                                into(mCsImage);
                    }
                    mTvStore.setText(jsonBean.getCc_depotid_nameref());
                    mTvCategory.setText(jsonBean.getCc_categoryid_nameref());
                    mTvChannel.setText(jsonBean.getCc_channelid_nameref());
                    mTvAddress.setText(jsonBean.getCc_address());

                    mTvPriceGrade.setText(jsonBean.getCc_goods_gradeid_nameref());

                    if ("Y".equals(userBean.getIsShopAccount())) {
                        mTvOpenMall.setVisibility(View.VISIBLE);
                        if ("Y".equals(jsonBean.getCc_isaccount())) {//是否开通商城
                            mTvIsopenStore.setText("已开通");
                            mIvRightIsopen.setVisibility(View.INVISIBLE);
                            mLlStoreMain.setVisibility(View.VISIBLE);
                            if (jsonBean.getCc_openid() == null || jsonBean.getCc_openid().length() == 0) {
                                mTvWx.setText("未绑定");
                            } else {
                                mTvWx.setText("已绑定");
                                mIvWxRightArrow.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            mTvIsopenStore.setText("开通商城");
                            mLlStoreMain.setVisibility(View.GONE);
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getCustomerInfo();
                    }
                });
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getCustomerInfo, L_RequestParams.getCustomerInfo(UserInfoUtils.getLineId(this) + "",
                customerId), false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 20) {
            getCustomerInfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(OpenMallEvent event) {
        customerBean = event.getClientBean();
        mTvCcName.setText(customerBean.getCc_name());
        mTvContactsName.setText(customerBean.getCc_contacts_name());
        mTvContactsMobile.setText(customerBean.getCc_contacts_mobile());

        if (customerBean.getCc_image() != null && customerBean.getCc_image().length() > 0) {
            Glide.with(CustomerInformationActivity.this).
                    load(customerBean.getCc_image()).
                    error(R.mipmap.photo).
                    into(mCsImage);
        }
        mTvStore.setText(customerBean.getCc_depotid_nameref());
        mTvCategory.setText(customerBean.getCc_categoryid_nameref());
        mTvChannel.setText(customerBean.getCc_channelid_nameref());
        mTvPriceGrade.setText(customerBean.getCc_goods_gradeid_nameref());
        mTvAddress.setText(customerBean.getCc_address());

        if ("Y".equals(userBean.getIsShopAccount())) {
            mTvOpenMall.setVisibility(View.VISIBLE);
            if ("Y".equals(customerBean.getCc_isaccount())) {//是否开通商城
                mTvIsopenStore.setText("已开通");
                mIvRightIsopen.setVisibility(View.INVISIBLE);
                mLlStoreMain.setVisibility(View.VISIBLE);
                if (customerBean.getCc_openid() == null || customerBean.getCc_openid().length() == 0) {
                    mTvWx.setText("未绑定");
                } else {
                    mTvWx.setText("已绑定");
                    mIvWxRightArrow.setVisibility(View.INVISIBLE);
                }
            } else {
                mTvIsopenStore.setText("开通商城");
                mLlStoreMain.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
