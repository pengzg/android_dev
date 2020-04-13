package com.xdjd.storebox.mainfragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.TopTitleListener;
import com.xdjd.storebox.R;
import com.xdjd.storebox.activity.ActionActivity;
import com.xdjd.storebox.activity.ActionActivityGroup;
import com.xdjd.storebox.activity.CaptureActivity;
import com.xdjd.storebox.activity.CommonWebActivity;
import com.xdjd.storebox.activity.GoodsCategoryActivity;
import com.xdjd.storebox.activity.GoodsDetailActivity;
import com.xdjd.storebox.activity.MessageActivity;
import com.xdjd.storebox.activity.MyCollectActivity;
import com.xdjd.storebox.activity.NewRecommendActivity;
import com.xdjd.storebox.activity.OftenBuyActivity;
import com.xdjd.storebox.activity.SalesActivity;
import com.xdjd.storebox.activity.SearchActivity;
import com.xdjd.storebox.activity.ValidActivity;
import com.xdjd.storebox.adapter.HomePageListAdapter;
import com.xdjd.storebox.adapter.HomepageGoodsAdapter;
import com.xdjd.storebox.adapter.RecommendAdapter;
import com.xdjd.storebox.adapter.SalesAdapter;
import com.xdjd.storebox.base.BaseFragment;
import com.xdjd.storebox.bean.GoodsBean;
import com.xdjd.storebox.bean.GoodsCartBean;
import com.xdjd.storebox.bean.HomeActivityBean;
import com.xdjd.storebox.bean.HomeBean;
import com.xdjd.storebox.bean.HomeGoodsBean;
import com.xdjd.storebox.bean.LoginBean;
import com.xdjd.storebox.event.CartEvent;
import com.xdjd.storebox.event.SwitchUpdateEvent;
import com.xdjd.storebox.holder.ActionImageHolderView;
import com.xdjd.storebox.holder.NetworkImageHolderView;
import com.xdjd.storebox.listener.ItemOnListener;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.utils.runtimepermissions.PermissionUtils;
import com.xdjd.view.NoScrollGridView;
import com.xdjd.view.NoScrollListView;
import com.xdjd.view.popup.EditCartNumPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 首页fragment
 * Created by lijipei on 2016/11/16.
 */

public class HomePageFragment extends BaseFragment implements PullToRefreshScrollView.OnPullScrollListener,
        TopTitleListener, ItemOnListener, EditCartNumPopupWindow.editCartNumListener {

    @BindView(R.id.homepage_linbo01)
    ConvenientBanner mHomepageLinbo01;
    @BindView(R.id.homepage_gridview)
    NoScrollGridView mHomepageGridview;
    @BindView(R.id.homepage_linbo02)
    ConvenientBanner mHomepageLinbo02;
    @BindView(R.id.homepage_pullscroll)
    PullToRefreshScrollView mHomepagePullscroll;
    @BindView(R.id.homepage_ll)
    LinearLayout mHomepageLl;
    @BindView(R.id.my_collect)
    LinearLayout mMyCollect;
    @BindView(R.id.sales_promotion)
    LinearLayout mSalesPromotion;
    @BindView(R.id.new_recommend)
    LinearLayout mNewRecommend;
    @BindView(R.id.often_buy)
    LinearLayout mOftenBuy;
    @BindView(R.id.sales_listview)
    NoScrollListView mSalesListview;
    @BindView(R.id.homepage_linbo03)
    ConvenientBanner mHomepageLinbo03;
    @BindView(R.id.line02)
    View mLine02;
    @BindView(R.id.search_ll)
    LinearLayout mSearchLl;
    @BindView(R.id.change_company)
    LinearLayout mMhange_company;
    @BindView(R.id.service_phone_ll)
    LinearLayout mServicePhonell;
    @BindView(R.id.message_ll)
    LinearLayout mMessageLl;
    @BindView(R.id.tuijian_title)
    ImageView mTuijianTitle;
    @BindView(R.id.homepage_main_ll)
    LinearLayout mHomepageMainLl;
    @BindView(R.id.erweima_ll)
    LinearLayout mErweimaLl;
    @BindView(R.id.homepage_list)
    NoScrollGridView mHomepageList;
    @BindView(R.id.sales_ll)
    LinearLayout mSalesLl;
    @BindView(R.id.tuijian_ll)
    LinearLayout mTuijianLl;
    @BindView(R.id.lunbo_ll)
    LinearLayout mLunboLl;
    @BindView(R.id.goods_title_rl)
    RelativeLayout mGoodsTitleRl;
    @BindView(R.id.homepage_goods_gridview)
    NoScrollGridView mHomepageGoodsGridview;
    @BindView(R.id.goods_ll)
    LinearLayout mGoodsLl;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.switch_iv)
    ImageView switchIv;

    //顶部轮播图的高度
    private int imageHeight = UIUtils.dp2px(150);

    private HomePageListAdapter adapterHomePageList;

    /**
     * 商品推荐adapter
     */
    private RecommendAdapter adapter;

    /**
     * 热门活动adapter
     */
    private SalesAdapter adapterSales;

    /**
     * 底部活动集合
     */
    private List<HomeActivityBean> listBottomAction = new ArrayList<>();

    /**
     * 推荐集合
     */
    private List<HomeGoodsBean> tuijianGoodsList = new ArrayList<>();

    /**
     * 首页客服电话
     */
    private String shopTel;

    /**
     * 新品推荐id
     */
    private String tagId;

    private VaryViewHelper mViewHelper = null;

    /**
     * 顶部四个按钮集合
     */
    private List<HomeBean.HomePageListBean> listHomePageList = new ArrayList<>();

    /**
     * 顶部广告数据
     */
    private List<HomeBean> lunbo01 = new ArrayList<>();
    private LunBoTu01 mLunBoTu01;

    /**
     * 中间轮播图活动数据
     */
    private List<HomeActivityBean> lunbo02 = new ArrayList<>();
    private LunBoTu02 mLunBoTu02;

    /**
     * 中间底部轮播图活动数据
     */
    private List<HomeActivityBean> lunbo03 = new ArrayList<>();
    private LunBoTu03 mLunBoTu03;


    /**
     * 商品数据列表
     */
    List<GoodsBean> listGoods = new ArrayList<>();
    private HomepageGoodsAdapter adapterGoods;

    /**
     * 购物车加减popup
     */
    private EditCartNumPopupWindow editCartPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        initView();
        loadFocus(PublicFinal.FIRST);//顶部轮播
        loadBtn();//顶部几个按钮
        loadRecommend();//分区推荐
        loadGoods();//最下面新品推荐
        loadHomeAction(1);//中部活动轮播1
        loadHomeAction(2);//中部活动轮播2
        loadHomeAction(3);//推荐分区下面活动列表
    }

    /**
     * 初始化pop
     */
    private void selectEditCartPwScreen() {
        editCartPopup = new EditCartNumPopupWindow(getActivity(), this);
    }

    private void initView() {
        //透明状态栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mHomepageLl.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));
        mViewHelper = new VaryViewHelper(mHomepageMainLl);
        initRefresh(mHomepagePullscroll);
        mHomepagePullscroll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mHomepagePullscroll.setTopTitleTypeListener(this);
        mHomepagePullscroll.setOnScrollListener(this);
        mHomepagePullscroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadFocus(PublicFinal.FIRST);
                loadBtn();
                loadRecommend();
                loadGoods();
                loadHomeAction(1);//中部
                loadHomeAction(2);//下部
                loadHomeAction(3);//底部
            }
        });
        adapterHomePageList = new HomePageListAdapter();
        mHomepageList.setAdapter(adapterHomePageList);//几个按钮
        adapter = new RecommendAdapter();
        mHomepageGridview.setAdapter(adapter);//推荐商品分区
        adapterGoods = new HomepageGoodsAdapter(this);
        mHomepageGoodsGridview.setAdapter(adapterGoods);//最下面推荐商品
        adapterSales = new SalesAdapter();
        mSalesListview.setAdapter(adapterSales);//热门活动
        mHomepageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int type = listHomePageList.get(i).getType();
                Intent intent;
                //类型：1活动，2商品，3标签，4我常买，5我的收藏，6 新品推荐 7,临期商品 9．h5页面
                switch (type) {
                    case 1:
                        intent = new Intent(getActivity(), SalesActivity.class);
                        intent.putExtra("title", listHomePageList.get(i).getTitle());
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        intent = new Intent(getActivity(), OftenBuyActivity.class);
                        intent.putExtra("title", listHomePageList.get(i).getTitle());
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), MyCollectActivity.class);
                        intent.putExtra("title", listHomePageList.get(i).getTitle());
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), NewRecommendActivity.class);
                        intent.putExtra("title", listHomePageList.get(i).getTitle());
                        startActivity(intent);
                        break;
                    case 7://临期商品
                        intent = new Intent(getActivity(), ValidActivity.class);
                        intent.putExtra("title", listHomePageList.get(i).getTitle());
                        startActivity(intent);
                        break;
                    case 8://跳转签到
                        String url = M_Url.signIn + "user=" + UserInfoUtils.getId(getActivity()) +
                                "&device=1&deviceType=1&shop=" + UserInfoUtils.getCenterShopId(getActivity());
                        intent = new Intent(getActivity(), CommonWebActivity.class);
                        intent.putExtra("title", "签到");
                        intent.putExtra("url", url);
                        startActivity(intent);
                        break;
                    case 9:
                        break;
                }
            }
        });
        /*点击推荐商品分区*/
        mHomepageGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1 列表 2 单个商品
                if (tuijianGoodsList.get(i).getShp_type().equals("1")) {
                    Intent intent = new Intent(getActivity(), GoodsCategoryActivity.class);
                    intent.putExtra("whp_id", tuijianGoodsList.get(i).getShp_id());
                    startActivity(intent);
                } else if (tuijianGoodsList.get(i).getShp_type().equals("2")) {
                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("gpId", tuijianGoodsList.get(i).getShp_ggp_id());
                    startActivity(intent);
                }
            }
        });
        //热门活动
        mSalesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listBottomAction.get(i).getType().equals("5")) {
                    Intent intent1 = new Intent(getActivity(), ActionActivityGroup.class);
                    intent1.putExtra("activityId", listBottomAction.get(i).getActivityId());
                    startActivity(intent1);
                } else {
                    Intent intent = new Intent(getActivity(), ActionActivity.class);
                    intent.putExtra("activityId", listBottomAction.get(i).getActivityId());
                    startActivity(intent);
                }
            }
        });

        mLunBoTu01 = new LunBoTu01();
        mLunBoTu02 = new LunBoTu02();
        mLunBoTu03 = new LunBoTu03();
        selectEditCartPwScreen();//初始化pop
        shopTel = UserInfoUtils.getOrgPhone(getActivity());
        if (UserInfoUtils.getChangeCompanyFlag(getActivity()).equals("1")) {
            mServicePhonell.setVisibility(View.VISIBLE);
            mMhange_company.setVisibility(View.GONE);
        } else {
            mServicePhonell.setVisibility(View.GONE);
            mMhange_company.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        mHomepageLinbo01.startTurning(4000);
        mHomepageLinbo02.startTurning(4000);
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        mHomepageLinbo01.stopTurning();
        mHomepageLinbo02.stopTurning();
    }

    /**
     * 加载顶部广告
     */
    private void loadFocus(int isFirst) {
        if (PublicFinal.FIRST == isFirst) {
            mViewHelper.showLoadingView();
        }
        AsyncHttpUtil<HomeBean> httpUtil = new AsyncHttpUtil<>(getActivity(), HomeBean.class, new IUpdateUI<HomeBean>() {
            @Override
            public void updata(HomeBean bean) {
                if (bean.getRepCode().equals("00")) {
                    lunbo01 = bean.getListTopData();
                    mHomepageLinbo01.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                        @Override
                        public NetworkImageHolderView createHolder() {
                            return new NetworkImageHolderView();
                        }
                    }, lunbo01).setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                            R.drawable.ic_page_indicator_focused})
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                            .setOnItemClickListener(mLunBoTu01);

                    //添加动画代码
                    String transforemerName = RotateUpTransformer.class.getSimpleName();
                    try {
                        Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
                        ABaseTransformer transforemer = (ABaseTransformer) cls.newInstance();
                        mHomepageLinbo01.getViewPager().setPageTransformer(true, transforemer);
                        //部分3D特效需要调整滑动速度
                        if (transforemerName.equals("StackTransformer")) {
                            mHomepageLinbo01.setScrollDuration(1200);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    mHomepageLl.setBackgroundColor(UIUtils.getColor(R.color.transparent));
                    mViewHelper.showDataView();
                } else {
                    mViewHelper.showErrorView(new onErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mViewHelper.showErrorView(new onErrorListener());
            }

            @Override
            public void finish() {
                mHomepagePullscroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getFocus, L_RequestParams.getFocus(UserInfoUtils.getId(getActivity())), false);
    }

    @Override
    public void pullRefreshType(PullToRefreshBase.State mState) {
        switch (mState) {
            case RESET:
                if (mHomepageLl.getVisibility() == View.GONE) {
                    mHomepageLl.setVisibility(View.VISIBLE);
                }
                break;
            case PULL_TO_REFRESH:
                if (mHomepageLl.getVisibility() == View.VISIBLE) {
                    mHomepageLl.setVisibility(View.GONE);
                }
                break;
            case OVERSCROLLING:
                break;
        }
    }

    /**
     * 加载失败点击事件
     */
    class onErrorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadFocus(PublicFinal.FIRST);
            loadBtn();
            loadRecommend();
            loadGoods();
            loadHomeAction(1);
            loadHomeAction(2);
            loadHomeAction(3);
        }
    }

    /**
     * 加载顶部四个按钮
     */
    private void loadBtn() {
        AsyncHttpUtil<HomeBean> httpUtil = new AsyncHttpUtil<>(getActivity(), HomeBean.class, new IUpdateUI<HomeBean>() {
            @Override
            public void updata(HomeBean bean) {
                if (bean.getRepCode().equals("00")) {
                    listHomePageList = bean.getListData();
                    if (listHomePageList.size() == 4) {
                        mHomepageList.setNumColumns(4);
                    } else if (listHomePageList.size() == 5) {
                        mHomepageList.setNumColumns(5);
                    } else if (listHomePageList.size() > 5) {
                        mHomepageList.setNumColumns(5);
                    } else {
                        mHomepageList.setNumColumns(4);
                    }
                    adapterHomePageList.setData(listHomePageList);
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.getHomeIndex, L_RequestParams.getHomeIndex(
                UserInfoUtils.getId(getActivity())), false);
    }


    /**
     * 获取中部轮播、下部轮播和底部活动列表
     */
    private void loadHomeAction(final int localtion) {
        AsyncHttpUtil<HomeActivityBean> httpUtil = new AsyncHttpUtil<>(getActivity(), HomeActivityBean.class, new IUpdateUI<HomeActivityBean>() {
            @Override
            public void updata(HomeActivityBean bean) {
                if (bean.getRepCode().equals("00")) {
                    initActionData(localtion, bean.getListData());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {}
            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.getHomeActivity, L_RequestParams.getHomeActivity(UserInfoUtils.
                getId(getActivity()), String.valueOf(localtion)), false);
    }

    /**
     * 设置活动数据
     */
    private void initActionData(int localtion, List<HomeActivityBean> listAction) {
        switch (localtion) {
            case 1:
                if (null != listAction && listAction.size() != 0) {
                    lunbo02 = listAction;
                    Log.e("活动：", listAction.toString());
                    mHomepageLinbo02.setVisibility(View.VISIBLE);
                    mLine02.setVisibility(View.VISIBLE);
                    mHomepageLinbo02.setPages(new CBViewHolderCreator<ActionImageHolderView>() {
                        @Override
                        public ActionImageHolderView createHolder() {
                            return new ActionImageHolderView();
                        }
                    }, lunbo02)
                            .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                            .setOnItemClickListener(mLunBoTu02);
                } else {
                    Log.e("不显示", "shide ");
                    mHomepageLinbo02.setVisibility(View.GONE);
                    mLine02.setVisibility(View.GONE);
                }
                isGoneLinBoTitle();
                break;
            case 2:
                if (null != listAction && listAction.size() != 0) {
                    lunbo03 = listAction;
                    mHomepageLinbo03.setVisibility(View.VISIBLE);
                    mHomepageLinbo03.setPages(new CBViewHolderCreator<ActionImageHolderView>() {
                        @Override
                        public ActionImageHolderView createHolder() {
                            return new ActionImageHolderView();
                        }
                    }, lunbo03)
                            .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                            .setOnItemClickListener(mLunBoTu03);
                } else {
                    mHomepageLinbo03.setVisibility(View.GONE);
                }
                isGoneLinBoTitle();
                break;
            case 3://热门活动list
                listBottomAction = listAction;
                if (listBottomAction.size() > 0) {
                    mSalesLl.setVisibility(View.VISIBLE);
                    adapterSales.setData(listBottomAction);
                } else {
                    mSalesLl.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 是否隐藏轮播活动标题方法
     */
    private void isGoneLinBoTitle() {
        if (mHomepageLinbo03.getVisibility() == View.GONE && mHomepageLinbo02.getVisibility() == View.GONE) {
            mLunboLl.setVisibility(View.GONE);
        } else {
            mLunboLl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载推荐分类列表
     */
    private void loadRecommend() {
        AsyncHttpUtil<HomeGoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), HomeGoodsBean.class,
                new IUpdateUI<HomeGoodsBean>() {
                    @Override
                    public void updata(HomeGoodsBean bean) {
                        if (bean.getRepCode().equals("00")) {
                            tuijianGoodsList.clear();
                            if (bean.getListData().size() > 0) {
                                mTuijianLl.setVisibility(View.VISIBLE);
                                tuijianGoodsList.addAll(bean.getListData());
                                adapter.setData(tuijianGoodsList);
                            } else {
                                mTuijianLl.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void sendFail(ExceptionType s) {
                    }

                    @Override
                    public void finish() {
                    }
                });
        httpUtil.post(M_Url.getHomeList, L_RequestParams.getHomeList(
                UserInfoUtils.getId(getActivity())), false);
    }

    /**
     * 加载推荐商品(最下面)
     */
    private void loadGoods() {
        AsyncHttpUtil<GoodsBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsBean.class, new IUpdateUI<GoodsBean>() {
            @Override
            public void updata(GoodsBean bean) {
                if (bean.getRepCode().equals("00")) {
                    listGoods.clear();
                    if (null != bean.getListData() && bean.getListData().size() > 0) {
                        listGoods.addAll(bean.getListData());
                        adapterGoods.setData(listGoods);
                        mGoodsLl.setVisibility(View.VISIBLE);
                    } else {
                        mGoodsLl.setVisibility(View.GONE);
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.GoodsList, L_RequestParams.getGoodsList(UserInfoUtils.getId(getActivity()), "3", "",
                "1", "1", "", "", "", "", ""), false);
    }

    @Override
    public void onItem(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("gpId", listGoods.get(position).getGgp_id());
        intent.putExtra("cartNum", listGoods.get(position).getCartnum());
        startActivity(intent);
    }

    @Override
    public void editGoodsCartNumNew(int i, RelativeLayout rl, GoodsBean bean) {
        queryGooodsCartNum(i, bean, rl);
    }

    @Override
    public void editCart(String num, String ggpId, String min_num) {
        if (num.equals(" ")) {
            showToast("请输入订货数量！");
        } else {
            modifyCart(num, ggpId);
        }
    }

    /**
     * 查询商品在购物车中的数量
     *
     * @param i
     * @param
     */
    private void queryGooodsCartNum(final int i, final GoodsBean goodBean, final RelativeLayout rl) {
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    editCartPopup.showPwScreen(rl, bean.getC_goods_num(), bean.getIsFavorite(), goodBean);
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.queryGoodsNums, L_RequestParams.queryGoodsCarNum(UserInfoUtils.getId(getActivity()), goodBean.getGgp_id(), UserInfoUtils.getStoreHouseId(getActivity()),""), false);
    }

    /**
     * 修改购物车
     */
    private void modifyCart(String num, String ggpId) {
        AsyncHttpUtil<GoodsCartBean> httpUtil = new AsyncHttpUtil<>(getActivity(), GoodsCartBean.class, new IUpdateUI<GoodsCartBean>() {
            @Override
            public void updata(GoodsCartBean bean) {
                if (bean.getRepCode().equals("00")) {
                    showToast("已成功添加至购物车！");
                    EventBus.getDefault().post(new CartEvent(bean.getCartNum(), bean.getTotalAmount()));
                    editCartPopup.dismiss();
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        httpUtil.post(M_Url.editCartGoodNum, L_RequestParams.editCart(UserInfoUtils.getId(getActivity()), ggpId, num, UserInfoUtils.getStoreHouseId(getActivity()),
                ""), false);
    }

    @OnClick({R.id.often_buy, R.id.my_collect, R.id.sales_promotion, R.id.new_recommend, R.id.search_ll, R.id.change_company,
            R.id.message_ll, R.id.erweima_ll, R.id.goods_title_rl, R.id.service_phone_ll})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.goods_title_rl://推荐商品(更多)
                intent = new Intent(getActivity(), NewRecommendActivity.class);
                startActivity(intent);
                break;
            case R.id.often_buy://我常买
                startActivity(OftenBuyActivity.class);
                break;
            case R.id.my_collect://我的收藏
                startActivity(MyCollectActivity.class);
                break;
            case R.id.sales_promotion://促销活动
                startActivity(SalesActivity.class);
                break;
            case R.id.new_recommend://新品推荐
                intent = new Intent(getActivity(), NewRecommendActivity.class);
                intent.putExtra("tagId", tagId);
                startActivity(intent);
                break;
            case R.id.search_ll://跳转搜索界面
                startActivity(SearchActivity.class);
                break;
            case R.id.change_company://切换公司
                changeLoginCompany();
                break;
            case R.id.service_phone_ll:
                DialogUtil.showCustomDialog(getActivity(), "客服电话", shopTel, "拨打",
                        "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopTel));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void no() {
                            }
                        });
                break;
            case R.id.message_ll: //消息
                startActivity(MessageActivity.class);
                break;
            case R.id.erweima_ll://二维码
                PermissionUtils.requstFragmentCamera(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {//CaptureActivity
                        Intent intent1 = new Intent(getActivity(), CaptureActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                    }

                    @Override
                    public void onDilogCancal() {

                    }
                });
                break;
        }
    }

    /*切换登录公司*/
    private void changeLoginCompany() {
        AsyncHttpUtil httpUtil = new AsyncHttpUtil<>(getActivity(), LoginBean.class, new IUpdateUI<LoginBean>() {
            @Override
            public void updata(final LoginBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (bean.getListData().size() == 1) {
                        showToast("您没有可切换的公司！");
                    } else {
                        DialogUtil.showDialogList(getActivity(), "选择进货公司", bean.getListData(), new DialogUtil.MyCustomDialogListener4() {
                            @Override
                            public void item(int i) {
                                UserInfoUtils.setId(getActivity(), bean.getListData().get(i).getUserId());//用户id
                                UserInfoUtils.setOrgPhone(getActivity(), bean.getListData().get(i).getOrgid_mobile());//公司电话
                                UserInfoUtils.setStoreHouseId(getActivity(), bean.getListData().get(i).getStorehouseid());//发货仓库id
                                UserInfoUtils.setCompanyId(getActivity(), bean.getListData().get(i).getOrgid());
                                loadFocus(PublicFinal.FIRST);//顶部轮播
                                loadBtn();//顶部几个按钮
                                loadRecommend();//分区推荐
                                loadGoods();//最下面新品推荐
                                loadHomeAction(1);//中部活动轮播1
                                loadHomeAction(2);//中部活动轮播2
                                loadHomeAction(3);//推荐分区下面活动列表
                                EventBus.getDefault().post(new SwitchUpdateEvent());
                            }
                        });
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
            }
        });
        Log.e("密码：", UserInfoUtils.getLoginPwd(getActivity()));
        httpUtil.post(M_Url.login, L_RequestParams.getLogin(getActivity(), UserInfoUtils.getLoginName(getActivity()), UserInfoUtils.getLoginPwd(getActivity())), true);
    }

    /**
     * 顶部广告的点击事件
     */
    public class LunBoTu01 implements OnItemClickListener {
        @Override
        public void onItemClick(int position) {
            //类型	1 h5页面 2 活动 3商品
            String type = lunbo01.get(position).getType();
            if (type.equals("1")) {
                Intent intent = new Intent(getActivity(), CommonWebActivity.class);
                intent.putExtra("url", lunbo01.get(position).getSkip_value());
                //intent.putExtra("title", lunbo01.get(position).getTitle());
                startActivity(intent);
            } else if (type.equals("2")) {
                Intent intent = new Intent(getActivity(), ActionActivity.class);
                intent.putExtra("activityId", lunbo01.get(position).getSkip_value());
                startActivity(intent);
            } else if (type.equals("3")) {
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("gpId", lunbo01.get(position).getSkip_value());
                intent.putExtra("gpsId", "");//价格方案id为空
                startActivity(intent);
            }
        }
    }

    /**
     * 顶部活动轮播图击事件
     */
    public class LunBoTu02 implements OnItemClickListener {
        @Override
        public void onItemClick(int position) {

            if (lunbo02.get(position).getType().equals("5")) {
                Intent intent1 = new Intent(getActivity(), ActionActivityGroup.class);
                intent1.putExtra("activityId", lunbo02.get(position).getActivityId());
                startActivity(intent1);
            } else {
                Intent intent = new Intent(getActivity(), ActionActivity.class);
                intent.putExtra("activityId", lunbo02.get(position).getActivityId());
                startActivity(intent);
            }
        }
    }

    /**
     * 底部轮播图点击事件
     */
    public class LunBoTu03 implements OnItemClickListener {
        @Override
        public void onItemClick(int position) {
            if (lunbo03.get(position).getType().equals("5")) {
                Intent intent1 = new Intent(getActivity(), ActionActivityGroup.class);
                intent1.putExtra("activityId", lunbo03.get(position).getActivityId());
                startActivity(intent1);
            } else {
                Intent intent = new Intent(getActivity(), ActionActivity.class);
                intent.putExtra("activityId", lunbo03.get(position).getActivityId());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onScroll(int scrollY) {
        //设置title的颜色透明度
        if (scrollY <= 0) {
            companyName.setTextColor(UIUtils.getColor(R.color.black));
            switchIv.setImageResource(R.drawable.switchover);
            mHomepageLl.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));//AGB由相关工具获得，或者美工提供
        } else if (scrollY > 0 && scrollY <= imageHeight) {
            float scale = (float) scrollY / imageHeight;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            mHomepageLl.setBackgroundColor(Color.argb((int) alpha, 222, 17, 34));
        } else {
            mHomepageLl.setBackgroundColor(Color.argb((int) 255, 222, 17, 34));
            companyName.setTextColor(UIUtils.getColor(R.color.white));
            switchIv.setImageResource(R.drawable.switchover_white);
        }
    }
}
