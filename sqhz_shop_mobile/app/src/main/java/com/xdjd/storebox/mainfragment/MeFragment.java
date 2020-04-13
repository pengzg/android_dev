package com.xdjd.storebox.mainfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.BalanceActivity;
import com.xdjd.storebox.activity.CaptureActivity;
import com.xdjd.storebox.activity.IntegralMainActivity;
import com.xdjd.storebox.activity.MeFeedbackActivity;
import com.xdjd.storebox.activity.MessageActivity;
import com.xdjd.storebox.activity.MyCollectActivity;
import com.xdjd.storebox.activity.MyOrderActivity;
import com.xdjd.storebox.activity.OftenBuyActivity;
import com.xdjd.storebox.activity.PersonInfoActivity;
import com.xdjd.storebox.activity.RolloutGoodsOrderActivity;
import com.xdjd.storebox.activity.ScanningPlacetheorderActivity;
import com.xdjd.storebox.activity.SetupActivity;
import com.xdjd.storebox.activity.WinningCodeActivity;
import com.xdjd.storebox.activity.WinningRecordActivity;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.PersonBean;
import com.xdjd.storebox.bean.ShareBean;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.NoScrollGridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

@SuppressWarnings("PointlessBooleanExpression")
public class MeFragment extends BaseFragment {
    @BindView(R.id.person_image)
    CircleImageView personImage;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_mobile)
    TextView shopMobile;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.personInfo)
    LinearLayout personInfo;
    @BindView(R.id.setting_ll)
    LinearLayout settingLl;
    @BindView(R.id.my_order_ll)
    LinearLayout myOrderLl;
    @BindView(R.id.wait_payment)
    LinearLayout waitPayment;
    @BindView(R.id.wait_receive)
    LinearLayout waitReceive;
    @BindView(R.id.finish_order)
    LinearLayout finishOrder;
    @BindView(R.id.mgridView)
    NoScrollGridView mgridView;
    @BindView(R.id.integral_ll)
    LinearLayout mIntegralLl;
    @BindView(R.id.me_message)
    LinearLayout mMeMessage;
    @BindView(R.id.me_collect)
    LinearLayout meCollect;
    @BindView(R.id.me_integral_num)
    TextView meIntegralNum;
    @BindView(R.id.me_msg_number)
    TextView meMsgNumber;
    @BindView(R.id.me_collect_num)
    TextView meCollectNum;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.elseGridView)
    NoScrollGridView elseGridView;
    @BindView(R.id.meAccountGridView)
    NoScrollGridView meAccountGridView;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.gv_prize)
    NoScrollGridView mGvPrize;
    @BindView(R.id.ll_prize)
    LinearLayout mLlPrize;
    @BindView(R.id.ll_ph_order)
    LinearLayout mLlPhOrder;
    @BindView(R.id.gv_ph)
    NoScrollGridView mGvPh;

    private MainActivity mMainActivity;
    private int GridNum;
    private int GridNumElse;
    private int GridNumAccount;
    private GridAdapter adapter;
    private MyPhAdapter phAdapter;//铺货适配器
    private elseGridAdapter elseGridAdapter;
    private meAccountGridAdapter meAccountGridAdapter;
    private MyPrizeAdapter mMyPrizeAdapter;//奖品核销
    private String userType;//用户类型
    private String servicePhone;//服务中心电话
    private String[] name = new String[]{"我常买", "我的优惠券", "我的反馈", "我的收藏",
            /*"门店会员",*/ "客服电话", "分享给朋友", "消息"/*, "推广用户", "推广统计", "推广排名", "待处理订单"*/};
    private int[] images = new int[]{R.drawable.usual_buy, R.drawable.me_ticket, R.drawable.me_feedback, R.drawable.me_collect,
            /*R.drawable.shop_vip,*/ R.drawable.service_phone, R.drawable.share_img, R.drawable.message/*, R.drawable.promote_user,
            R.drawable.promote_count, R.drawable.promote_rank, R.drawable.pending_order*/};

    private String[] names = new String[]{"我常买", "我的收藏", "统配进货", "预售进货", "扫码下单"};
    private int[] images1 = new int[]{R.drawable.usual_buy, R.drawable.me_collect, R.drawable.stock, R.drawable.in_the_delivery,
            R.mipmap.qr_place_an_order};

    private String[] phNames = new String[]{"铺货单"};
    private int[] phImages = new int[]{R.drawable.ph_icon};

    private String[] elseName = new String[]{"意见反馈", "客服热线", "分享给朋友", "消息"};
    private int[] elseImage = new int[]{R.drawable.me_feedback, R.drawable.service_phone, R.drawable.share_img, R.drawable.message};

    private String[] meAccount = new String[]{"余额", "积分", "优惠券"};
    private int[] meAccountImage = new int[]{R.drawable.balance, R.drawable.integral, R.drawable.me_ticket};

    private String[] prizeNames = new String[]{"扫码核销", "核销记录", "核销密码"};
    private int[] prizeImages = new int[]{R.drawable.scan_hx, R.drawable.winning_record, R.drawable.me_feedback};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        mMainActivity = (MainActivity) getActivity();
        GridNum = 4;
        GridNumElse = 2;
        GridNumAccount = 3;
        adapter = new GridAdapter();
        phAdapter = new MyPhAdapter();
        elseGridAdapter = new elseGridAdapter();
        meAccountGridAdapter = new meAccountGridAdapter();
        mMyPrizeAdapter = new MyPrizeAdapter();
        mgridView.setAdapter(adapter);
        mGvPh.setAdapter(phAdapter);
        mGvPrize.setAdapter(mMyPrizeAdapter);//奖品核销
        elseGridView.setAdapter(elseGridAdapter);
        meAccountGridView.setAdapter(meAccountGridAdapter);
        mgridView.setNumColumns(4);
        mGvPh.setNumColumns(4);
        mGvPrize.setNumColumns(4);
        elseGridView.setNumColumns(4);
        meAccountGridView.setNumColumns(4);
        servicePhone = UserInfoUtils.getOrgPhone(getActivity());//服务电话
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        //@Override
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void onStart() {
        super.onStart();
        //判断是否要运行此方法里的内容
        if (!(YesOrNoLoadingOnstart.INDEX = true && YesOrNoLoadingOnstart.INDEX_ID == 4)) {
            return;
        }

        GetMeInfo(UserInfoUtils.getId(getActivity()), UserInfoUtils.getId(getActivity()));
    }

    @OnClick({R.id.personInfo, R.id.setting_ll, R.id.my_order_ll, R.id.wait_payment, R.id.wait_receive, R.id.finish_order,
            R.id.integral_ll, R.id.me_message, R.id.me_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personInfo:
                startActivity(PersonInfoActivity.class);
                break;
            case R.id.setting_ll://设置界面
                Intent intent1 = new Intent(getActivity(), SetupActivity.class);
                startActivity(intent1);
                break;
            case R.id.my_order_ll:
                tiao("0");
                break;
            case R.id.wait_payment://已申报
                tiao("1");
                break;
            case R.id.wait_receive://已配货
                tiao("4");
                break;
            case R.id.finish_order://已完成
                tiao("6");
                break;
            case R.id.integral_ll://跳转积分界面
                Intent intent = new Intent(getActivity(), IntegralMainActivity.class);
                startActivity(intent);
                break;
            case R.id.me_message://我的消息
                startActivity(MessageActivity.class);
                break;
            case R.id.me_collect://我的收藏
                startActivity(MyCollectActivity.class);
                break;
        }
    }

    private void tiao(String index) {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    /*个人信息请求*/
    private void GetMeInfo(String uid, String userId) {
        AsyncHttpUtil<PersonBean> httpUtil = new AsyncHttpUtil<>(getActivity(), PersonBean.class, new IUpdateUI<PersonBean>() {
            @Override
            public void updata(PersonBean bean) {
                if (bean.getRepCode().equals("00")) {
                    shopName.setText(bean.getCc_name());//店铺名称
                    shopMobile.setText(bean.getCc_contacts_mobile());//手机号
                    UserInfoUtils.setMobile(getActivity(), bean.getCc_contacts_mobile());//记录手机账号
                    shopAddress.setText(bean.getCc_address());//客户地址
                    /*if (bean.getIs_integrate_show().equals("1")) {//显示积分
                        mIntegralLl.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.VISIBLE);
                        meIntegralNum.setText(bean.getIntegrate());//积分数
                    } else {//不显示积分数
                        mIntegralLl.setVisibility(View.GONE);
                        view1.setVisibility(View.GONE);
                    }*/
                    meMsgNumber.setText(bean.getUnread_message_nums());//未读消息数
                    meCollectNum.setText(bean.getFavorite_nums());//收藏商品数
                    //userType = bean.getUserType();
                    /*if (userType.equals("89") || userType.equals("88")) {
                        GridNum = 12;
                        adapter = new GridAdapter();
                        mgridView.setAdapter(adapter);
                        mgridView.setNumColumns(4);
                    } else {
                        GridNum = 8;
                        adapter = new GridAdapter();
                        mgridView.setAdapter(adapter);
                        mgridView.setNumColumns(4);
                    }*/
                    Glide.with(getContext()).load(bean.getCc_image()).dontAnimate().error(R.drawable.head_bg).into(personImage);
                    UserInfoUtils.setSpreadName(getActivity(), bean.getSpread_name());
                    UserInfoUtils.setSpreadMobile(getActivity(), bean.getSpreadMobile());
                    UserInfoUtils.setCenterShopName(getActivity(), bean.getCenterShopName());

                    if ("".equals(UserInfoUtils.getCenterShopId(getActivity())) ||
                            UserInfoUtils.getCenterShopId(getActivity()) == null) {//如果中心仓id为空
                        UserInfoUtils.setCenterShopId(getActivity(), bean.getShopId());
                    } else {
                        UserInfoUtils.setCenterShopId(getActivity(), bean.getShopId());
                        if (!UserInfoUtils.getCenterShopId(getActivity()).equals(bean.getShopId())) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("currentTab", 4);
                            startActivity(intent);
                        }
                    }

                    UserInfoUtils.setDownload_Url(getActivity(), bean.getAppdown_url());
                    UserInfoUtils.setInteRuleUrl(getActivity(), bean.getInte_rule_url());//积分规则url
                } else if ("04".equals(bean.getRepCode())) {
                    //用户不存在
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
        httpUtil.post(M_Url.GetuserInfo, L_RequestParams.getUserInfo(uid, userId), false);
    }

    /*商品网格适配*/
    public class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return GridNum;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_me_grid, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.allGridImg.setImageResource(images1[i]);
            viewHolder.allGridName.setText(names[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0://我常买
                            startActivity(OftenBuyActivity.class);
                            break;
                        case 1://我的收藏
                            startActivity(MyCollectActivity.class);
                            break;
                        case 2://统配进货
                            mMainActivity.toPurchase(0);
                            break;
                        case 3://预售进货
                            mMainActivity.toPurchase(1);
                            break;
                        case 4://扫码下单
                            Intent intent1 = new Intent(getActivity(), ScanningPlacetheorderActivity.class);
                            startActivity(intent1);
                            break;
                        /*case 5://分享给朋友
                            shareParam();//showShare();
                            break;

                        case 7://积分
                            startActivity(IntegralMainActivity.class);
                            break;
                        case 6://消息
                            startActivity(MessageActivity.class);
                            break;
                       case 8://推广用户
                            startActivity(PromoteUsersActivity.class);
                            break;
                        case 9://推广统计
                            startActivity(PromoteStatisticsActivity.class);
                            break;
                        case 10://推广排名
                            startActivity(PromoteRankingActivity.class);
                            break;
                        case 11://待处理订单
                            startActivity(PendingOrderActivity.class);
                            break;*/
                    }
                }
            });
            return view;
        }


        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView allGridImg;
            @BindView(R.id.all_grid_name)
            TextView allGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /*铺货*/
    public class MyPhAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return phNames.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_me_grid, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.allGridImg.setImageResource(phImages[i]);
            viewHolder.allGridName.setText(phNames[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0://铺货单
                            startActivity(RolloutGoodsOrderActivity.class);
                            break;
                        default:
                            break;
                    }
                }
            });
            return view;
        }


        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView allGridImg;
            @BindView(R.id.all_grid_name)
            TextView allGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /*奖品核销*/
    public class MyPrizeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return prizeNames.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_me_grid, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.allGridImg.setImageResource(prizeImages[i]);
            viewHolder.allGridName.setText(prizeNames[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0://扫码核销
                            Intent intent = new Intent(getActivity(), CaptureActivity.class);
                            intent.putExtra("isPrize", true);
                            intent.putExtra("title", "扫码核销");
                            startActivity(intent);
                            break;
                        case 1://中奖记录
                            startActivity(WinningRecordActivity.class);
                            break;
                        case 2://设置核销密码
                            startActivity(WinningCodeActivity.class);
                            break;
                        default:
                            break;
                    }
                }
            });
            return view;
        }


        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView allGridImg;
            @BindView(R.id.all_grid_name)
            TextView allGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /*其他网格适配*/
    public class elseGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return GridNumElse;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_me_grid, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.allGridImg.setImageResource(elseImage[i]);
            viewHolder.allGridName.setText(elseName[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0://我的反馈
                            Intent intent = new Intent(getActivity(), MeFeedbackActivity.class);
                            intent.putExtra("shopMobile", shopMobile.getText());
                            startActivity(intent);
                            break;
                        case 1://客服电话
                            DialogUtil.showCustomDialog(getActivity(), "客服电话", servicePhone, "拨打",
                                    "取消", new DialogUtil.MyCustomDialogListener2() {
                                        @Override
                                        public void ok() {
                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + servicePhone));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void no() {
                                        }
                                    });
                            break;
                        case 2://分享给朋友
                            shareParam();//showShare();
                            //showShare();
                            break;
                        case 3://消息
                            startActivity(MessageActivity.class);
                        default:
                            break;
                    }
                }
            });
            return view;
        }


        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView allGridImg;
            @BindView(R.id.all_grid_name)
            TextView allGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    /*我的账户adapter*/
    public class meAccountGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return GridNumAccount;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_me_grid, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.allGridImg.setImageResource(meAccountImage[i]);
            viewHolder.allGridName.setText(meAccount[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0://余额
                            Intent intent = new Intent(getActivity(), BalanceActivity.class);
                            startActivity(intent);
                            break;
                        case 1://积分
                            showToast("您还没有积分！");
                            break;
                        case 2://优惠券
                            showToast("您还没有优惠券！");
                            break;
                        case 3:
                        default:
                            break;
                    }
                }
            });
            return view;
        }


        class ViewHolder {
            @BindView(R.id.all_grid_img)
            ImageView allGridImg;
            @BindView(R.id.all_grid_name)
            TextView allGridName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    /**
     * 分享参数请求
     */
    private void shareParam() {
        //bs_code=1;
        AsyncHttpUtil<ShareBean> httpUtil = new AsyncHttpUtil<>(getActivity(), ShareBean.class, new IUpdateUI<ShareBean>() {
            @Override
            public void updata(ShareBean shareBean) {
                if ("00".equals(shareBean.getRepCode())) {
                    showShare(shareBean);
                } else {
                    showToast(shareBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        httpUtil.post(M_Url.shareController,
                L_RequestParams.getShareParam(UserInfoUtils.getId(getActivity()), "1"), false);
    }

    private void showShare(ShareBean shareBean) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(shareBean.getTitle());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(shareBean.getLink());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareBean.getDescribe());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(shareBean.getImage());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareBean.getLink());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(shareBean.getDescribe());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("社区盒子");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareBean.getLink());

        // 启动分享GUI
        oks.show(getActivity());
    }
}
