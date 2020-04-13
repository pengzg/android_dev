package com.xdjd.steward.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.ClientDetailActivity;
import com.xdjd.distribution.activity.CustomerAddressListActivity;
import com.xdjd.distribution.adapter.CustomerCategoryAdapter;
import com.xdjd.distribution.adapter.LineAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.steward.activity.EditCustomerLineActivity;
import com.xdjd.steward.adapter.CustomerListingAdapter;
import com.xdjd.steward.adapter.LineSelectAdapter;
import com.xdjd.steward.listener.AnimListenerBuilder;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/9
 *     desc   : 客户信息列表fragment
 *     version: 1.0
 * </pre>
 */

public class CustomerInformationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.tv_page_count)
    TextView mTvPageCount;
    @BindView(R.id.rv_customer)
    RecyclerView mRvCustomer;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.tv_customer_total_num)
    TextView mTvCustomerTotalNum;
    @BindView(R.id.tv_all)
    TextView mTvAll;
    @BindView(R.id.ll_all)
    LinearLayout mLlAll;
    @BindView(R.id.tv_line)
    TextView mTvLine;
    @BindView(R.id.ll_line)
    LinearLayout mLlLine;
    @BindView(R.id.tv_customer_category)
    TextView mTvCustomerCategory;
    @BindView(R.id.ll_customer_category)
    LinearLayout mLlCustomerCategory;
    @BindView(R.id.ll_select)
    LinearLayout mLlSelect;

    private int page = 1;

    private int PAGE_PER = 1;//总共多少页
    private int PAGE_SIZE = 20;//每页的数量
    private int COUNT = 0;//总的条数,默认是0


    private List<AddressListBean> list = new ArrayList<>();
    private CustomerListingAdapter adapterCustomer;

    /**
     * 客户条件选择popup
     */
    private PopupWindow mPwSelect;
    private View mPwView;
    private ListView mLvLeft, mLvRight;

    private String categoryId = "";//客户类别id
    private String lineId = "";//客户线路id

    /**
     * 线路dao
     */
    private LineDao lineDao;
    private List<LineBean> listName = new ArrayList<>();
    private LineSelectAdapter adapterLine;

    private List<AddInfoBean> listCategory;//客户类别
    private List<AddInfoBean> listCategoryTwo;//客户类别二级
    private CustomerCategoryAdapter adapterCategoryOne;//客户类别
    private CustomerCategoryAdapter adapterCategoryTwo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_information, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mEtSearch.setHint("按店铺名称、客户姓名和电话进行查询");

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(UIUtils.getColor(R.color.color_blue));

        mRvCustomer.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterCustomer = new CustomerListingAdapter(list);
        adapterCustomer.setOnLoadMoreListener(this, mRvCustomer);
        adapterCustomer.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRvCustomer.setAdapter(adapterCustomer);

        final AnimListenerBuilder builder = new AnimListenerBuilder();

        mRvCustomer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int page;

                    if(((LinearLayoutManager) layoutManager).getChildCount()==0){
                        page = 0;
                        PAGE_PER = 0;
                    }else{
                        if (lastVisiblePosition % PAGE_SIZE == 0) {
                            page = lastVisiblePosition / PAGE_SIZE;
                        } else {
                            page = lastVisiblePosition / PAGE_SIZE + 1;
                        }
                    }

                    mTvPageCount.setText(page + "/" + PAGE_PER);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                builder.setNewState(newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && builder.isAnimFinish()) {
                    //如果是IDLE状态，并且显示动画执行完毕，再执行隐藏动画，避免出现动画闪烁
                    //如果快速简短滑动，可能导致出现IDLE状态，但是动画未执行完成。因此无法执行隐藏动画。所以保存当前newState，在onAnimationEnd中增加判断。
                    AnimUtils.hide(mTvPageCount);
                } else if (mTvPageCount.getVisibility() != View.VISIBLE) {
                    AnimUtils.show(mTvPageCount, builder.build());
                }
            }
        });

        adapterCustomer.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                String hint = list.get(position).getCc_name() + "\n" + list.get(position).getCc_contacts_mobile();
                hint.replace("\\n", "\n");

                DialogUtil.showCustomDialog(getActivity(), "提示",
                        hint, "查看详情", "查看线路信息", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                AddressListBean bean = list.get(position);

                                ClientBean clientBean = new ClientBean();
                                clientBean.setCc_id(bean.getCc_id());
                                clientBean.setCc_image(bean.getCc_image());
                                clientBean.setCc_name(bean.getCc_name());
                                clientBean.setCc_contacts_name(bean.getCc_contacts_name());
                                clientBean.setCc_contacts_mobile(bean.getCc_contacts_mobile());
                                clientBean.setCc_address(bean.getCc_address());

                                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                                intent.putExtra("customer", clientBean);
                                startActivity(intent);
                            }

                            @Override
                            public void no() {
                                Intent intent = new Intent(getActivity(),EditCustomerLineActivity.class);
                                intent.putExtra("customer",list.get(position));
                                startActivity(intent);
                            }
                        });

            }
        });

        lineDao = new LineDao(getActivity());
        listName = lineDao.query();

        //线路适配器
        adapterLine = new LineSelectAdapter(listName, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                lineId = listName.get(position).getBl_id();
                mTvLine.setText(listName.get(position).getBl_name());
                selectTvStyle(1);
                initializesRequest();
            }
        });

        adapterCategoryOne = new CustomerCategoryAdapter(new ItemOnListener() {
            @Override
            public void onItem(int position) {
                if (listCategory.get(position).getSubList() == null ||
                        listCategory.get(position).getSubList().size() == 0) {
                    categoryId = listCategory.get(position).getCc_id();
                    mTvCustomerCategory.setText(listCategory.get(position).getCc_name());
                    selectTvStyle(2);
                    initializesRequest();
                } else {
                    listCategoryTwo = listCategory.get(position).getSubList();
                    adapterCategoryTwo.setId(categoryId);
                    adapterCategoryTwo.setData(listCategoryTwo);
                }
            }
        });

        adapterCategoryTwo = new CustomerCategoryAdapter(new ItemOnListener() {
            @Override
            public void onItem(int position) {
                categoryId = listCategoryTwo.get(position).getCc_id();
                mTvCustomerCategory.setText(listCategoryTwo.get(position).getCc_name());
                selectTvStyle(2);
                initializesRequest();
            }
        });

        getCustomerList();
        selectPwCategory();
    }

    private void initializesRequest() {
        if (mPwSelect.isShowing()) {
            translateAnimOut(mPwView, mPwSelect);
        }
        list.clear();
        adapterCustomer.getData().clear();
        adapterCustomer.notifyDataSetChanged();
        page = 1;
        getCustomerList();
    }

    @OnClick({R.id.ll_search, R.id.ll_all, R.id.ll_line, R.id.ll_customer_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                page = 1;
                mSwipeLayout.setEnabled(false);
                adapterCustomer.setEnableLoadMore(false);
                list.clear();
                adapterCustomer.getData().clear();
                adapterCustomer.notifyDataSetChanged();
                getCustomerList();
                break;
            case R.id.ll_all:
                lineId = "";
                categoryId = "";
                selectTvStyle(0);
                initializesRequest();
                break;
            case R.id.ll_line://线路
                showPwCategoryLeft(1);
                break;
            case R.id.ll_customer_category://客户类别
                showPwCategoryLeft(2);
                break;
        }
    }

    /**
     * 获取客户通讯录
     */
    private void getCustomerList() {
        AsyncHttpUtil<AddressListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), AddressListBean.class, new IUpdateUI<AddressListBean>() {
            @Override
            public void updata(AddressListBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    COUNT = jsonBean.getTotal();
                    if (COUNT % PAGE_SIZE == 0) {
                        PAGE_PER = COUNT / PAGE_SIZE;
                    } else {
                        PAGE_PER = COUNT / PAGE_SIZE + 1;
                    }

                    mTvCustomerTotalNum.setText("客户总数量:" + jsonBean.getTotal());
                    if (jsonBean.getDataList() != null && jsonBean.getDataList().size() > 0) {
                        adapterCustomer.addData(jsonBean.getDataList());
                        adapterCustomer.loadMoreComplete();
                    } else {
                        page--;
                        if (page == 0) {
                            page = 1;
                            adapterCustomer.getData().clear();
                            adapterCustomer.notifyDataSetChanged();
                        }else{
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            adapterCustomer.loadMoreEnd(false);
                        }
                    }
                } else {
                    showToast(jsonBean.getRepMsg());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
            }

            @Override
            public void finish() {
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(true);
                adapterCustomer.setEnableLoadMore(true);
            }
        });
        httpUtil.post(M_Url.getCustomerList, L_RequestParams.getCustomerList(
                UserInfoUtils.getId(getActivity()), lineId, String.valueOf(page), String.valueOf(PAGE_SIZE), mEtSearch.getText().toString()
                , "", "", categoryId,"1"), true);
    }

    @Override
    public void onRefresh() {
        // 下拉刷新
        mSwipeLayout.setEnabled(false);
        adapterCustomer.setEnableLoadMore(false);
        adapterCustomer.getData().clear();
        list.clear();
        page = 1;
        adapterCustomer.notifyDataSetChanged();
        getCustomerList();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeLayout.setEnabled(false);
        if (adapterCustomer.getData().size() < PAGE_SIZE) {
            //第一页数据就小于pageSize的时候
            adapterCustomer.loadMoreEnd(false);
            mSwipeLayout.setEnabled(true);
        } else {
            page++;
            getCustomerList();
        }
    }

    /**
     * 全部分类的popupwindow
     */
    private void selectPwCategory() {
        mPwSelect = new PopupWindow(getActivity());
        mPwSelect.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPwSelect.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPwSelect.setBackgroundDrawable(new BitmapDrawable());
        mPwView = LayoutInflater.from(getActivity()).inflate(R.layout.pw_group_category_layout, null);
        mPwSelect.setContentView(mPwView);
        mPwSelect.setFocusable(true);
        mLvLeft = (ListView) mPwView.findViewById(R.id.type_listview_left);
        mLvRight = (ListView) mPwView.findViewById(R.id.type_listview_right);
        mPwView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateAnimOut(mPwView, mPwSelect);
            }
        });
    }

    /**
     * 显示全部分类左边的数据
     */
    private void showPwCategoryLeft(final int type) {
        switch (type) {
            case 1://线路
                mLvLeft.setVisibility(View.GONE);
                adapterLine.setLineId(lineId);
                mLvRight.setAdapter(adapterLine);

                if (mPwSelect.isShowing()) {
                    translateAnimOut(mPwView, mPwSelect);
                    return;
                }
                if (Build.VERSION.SDK_INT < 24) {
                    mPwSelect.showAsDropDown(mLlSelect, 0, 5);
                } else {
                    int[] a = new int[2];
                    mLlSelect.getLocationInWindow(a);
                    mPwSelect.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, mLlSelect.getHeight() + a[1] + 5);
                    mPwSelect.update();
                }
                translateAnimIn(mPwView);
                break;
            case 2://客户类别
                mLvLeft.setVisibility(View.VISIBLE);
                mLvLeft.setAdapter(adapterCategoryOne);
                mLvRight.setAdapter(adapterCategoryTwo);
                getAddInfo();
                break;
            default:
                break;
        }
    }

    private void getAddInfo() {
        AsyncHttpUtil<AddInfoBean> httpUtil = new AsyncHttpUtil<>(getActivity()
                , AddInfoBean.class, new IUpdateUI<AddInfoBean>() {
            @Override
            public void updata(AddInfoBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listCategory = jsonBean.getCategoryList();
                    if (listCategory != null && listCategory.size() != 0) {
                        adapterCategoryOne.setId(categoryId);
                        adapterCategoryOne.setData(jsonBean.getCategoryList());

                        if (listCategory.get(0).getSubList() != null && listCategory.get(0).getSubList().size() > 0) {
                            listCategoryTwo = listCategory.get(0).getSubList();
                            adapterCategoryTwo.setId(categoryId);
                            adapterCategoryTwo.setData(listCategoryTwo);
                        }

                        if (mPwSelect.isShowing()) {
                            translateAnimOut(mPwView, mPwSelect);
                            return;
                        }

                        if (Build.VERSION.SDK_INT < 24) {
                            mPwSelect.showAsDropDown(mLlSelect, 0, 5);
                        } else {
                            int[] a = new int[2];
                            mLlSelect.getLocationInWindow(a);
                            mPwSelect.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, mLlSelect.getHeight() + a[1] + 5);
                            mPwSelect.update();
                        }
                        translateAnimIn(mPwView);
                    } else {
                        showToast("没有相关数据");
                    }
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
        httpUtil.post(M_Url.getAddInfo, L_RequestParams.getAddInfo(), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lineDao.destroy();
    }

    private void selectTvStyle(int type) {
        //0:全部,1:线路;2.客户类别
        switch (type) {
            case 0:
                mTvAll.setTextColor(UIUtils.getColor(R.color.text_blue));
                mTvLine.setTextColor(UIUtils.getColor(R.color.text_gray));
                mTvCustomerCategory.setTextColor(UIUtils.getColor(R.color.text_gray));
                mTvLine.setText("线路");
                mTvCustomerCategory.setText("客户类别");
                break;
            case 1:
                mTvAll.setTextColor(UIUtils.getColor(R.color.text_gray));
                mTvLine.setTextColor(UIUtils.getColor(R.color.text_blue));
                break;
            case 2:
                mTvAll.setTextColor(UIUtils.getColor(R.color.text_gray));
                mTvCustomerCategory.setTextColor(UIUtils.getColor(R.color.text_blue));
                break;
        }
    }

    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim_in);
        view.startAnimation(animation);
    }

    /**
     * PopupWindow消失的动画
     */
    private void translateAnimOut(View view, final PopupWindow pw) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_anim_out);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pw.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
