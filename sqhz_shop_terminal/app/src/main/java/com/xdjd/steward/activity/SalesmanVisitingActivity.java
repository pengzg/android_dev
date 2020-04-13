package com.xdjd.steward.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.PhotoActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.distribution.popup.CalendarSelectPopup;
import com.xdjd.distribution.popup.SelectSalesmanPopup;
import com.xdjd.steward.adapter.LineSelectAdapter;
import com.xdjd.steward.adapter.SalesmanCategoryAdapter;
import com.xdjd.steward.adapter.SalesmanSelectAdapter;
import com.xdjd.steward.adapter.SalesmanVisitingAdapter;
import com.xdjd.steward.bean.CategoryBean;
import com.xdjd.steward.bean.ReportTaskBean;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.TimePickerUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.G_RequestParams;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycle.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesmanVisitingActivity extends BaseActivity implements SelectSalesmanPopup.SalesmanSearchListener, SalesmanVisitingAdapter.OnImgListener
        , CalendarSelectPopup.ItemDateOnListener {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.scroll)
    PullToRefreshScrollView mScroll;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_date_type_select)
    TextView mTvDateTypeSelect;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.ll_date_calendar)
    LinearLayout mLlDateCalendar;
    @BindView(R.id.tv_all_tab)
    TextView mTvAllTab;
    @BindView(R.id.tv_salesman_tab)
    TextView mTvSalesmanTab;
    @BindView(R.id.tv_distribution_tab)
    TextView mTvDistributionTab;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.line)
    RelativeLayout mLine;
    @BindView(R.id.tv_salesman_category)
    TextView mTvSalesmanCategory;
    @BindView(R.id.ll_salesman_category)
    LinearLayout mLlSalesmanCategory;
    @BindView(R.id.tv_line)
    TextView mTvLine;
    @BindView(R.id.ll_line)
    LinearLayout mLlLine;
    @BindView(R.id.ll_select)
    LinearLayout mLlSelect;
    @BindView(R.id.m_view)
    View mMView;

    private DisplayMetrics metric;

    private String dateStartStr;
    private String dateEndStr;

    private final int DATA_REQUEST = 100;

    private Date date = new Date();

    private VaryViewHelper mVaryViewHelper = null;

    private int page = 1;
    private int mFlag = 0;

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
    private String salesid = "";
    /**
     * 业务员名称
     */
    private String salesName = "";

    private SalesmanVisitingAdapter adapter;

    /**
     * 拜访数据
     */
    public List<ReportTaskBean> list = new ArrayList<>();

    private TimePickerUtil mTimePickerUtil;

    private int dateType = 1;

    private int categoryId = 1;//客户类型选择
    private int salesmanType = 1;// 业务员类型		1全部 2.业务员 3配送员
    private String dateTypeStr;//日期区间描述,如:今天

    private String lineId = "";

    /**
     * 员工条件选择popup
     */
    private PopupWindow mPwSelect;
    private View mPwView;
    private ListView mLvLeft, mLvRight;
    private LinearLayout mLlRight, mLlSearch;
    private EditText mEtSearch;

    /**
     * 员工类型adapter
     */
    private SalesmanCategoryAdapter adapterSalesmanCategory;
    private List<CategoryBean> listCategory = new ArrayList<>();//员工类型list

    /**
     * 线路dao
     */
    private LineDao lineDao;
    private List<LineBean> listName = new ArrayList<>();
    private LineSelectAdapter adapterLine;

    private SalesmanSelectAdapter adapterSalesmanSelect;

    @Override
    protected int getContentView() {
        return R.layout.activity_salesman_visiting;
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
        mTitleBar.setTitle("拜访明细");

        mTimePickerUtil = new TimePickerUtil();

        //获取屏幕总宽度
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        //计算宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        mLine.getLayoutParams().width = width / 3;

        dateStartStr = getIntent().getStringExtra("dateStartStr");
        dateEndStr = getIntent().getStringExtra("dateEndStr");
        salesid = getIntent().getStringExtra("salesid");
        dateTypeStr = getIntent().getStringExtra("dateTypeStr");

        if (dateTypeStr != null && !"".equals(dateTypeStr)) {
            mTvDateTypeSelect.setText(dateTypeStr);
        }
        if (dateStartStr == null || "".equals(dateStartStr)) {
            dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
            dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        }

        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        mVaryViewHelper = new VaryViewHelper(mScroll);

        mScroll.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(mScroll);
        mScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData();
            }
        });

        adapter = new SalesmanVisitingAdapter(this);
        mLvNoScroll.setAdapter(adapter);

        mTimePickerUtil.initCustomTimePicker(this, new TimePickerUtil.OnTimePickerListener() {
            @Override
            public void onDateStr(String startDate, String endDate) {
                if (DateUtils.isMoreThanToday(endDate)) {
                    showToast("结束时间不能超过今天");
                    return;
                }

                mTimePickerUtil.calendarPopup.dismiss();

                dateStartStr = startDate.replace("-", ".");
                dateEndStr = endDate.replace("-", ".");

                mTvDate.setText(dateStartStr + "-" + dateEndStr);
                mTvDateTypeSelect.setText("自定义");
                dateType = 4;

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }
        }, this);


        initSelectPw();

        selectTab();
        loadData();
    }

    private void initializesRequest() {
        if (mPwSelect.isShowing()) {
            translateAnimOut(mPwView, mPwSelect);
        }
        list.clear();
        adapter.notifyDataSetChanged();
        page = 1;
        mFlag = 1;
        loadData();
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
                        adapterSalesmanSelect.setSalesmanId(salesid);
                        adapterSalesmanSelect.setData(listSalesman);
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
                mScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getSalesdocList,
                L_RequestParams.getSalesdocList(searchStr, String.valueOf(salesmanType)), isDialog);
    }

    /**
     * 初始化业务员popup
     */
    /*private void initSalesmanPopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupSalesman = new SelectSalesmanPopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                salesid = listSalesman.get(position).getSu_id();
                salesName = listSalesman.get(position).getSu_name();
                //                mTvSalesman.setText(listSalesman.get(position).getSu_name());
                popupSalesman.dismiss();

                if (salesid == null) {
                    salesid = "";
                }

                if (salesid.equals("1")) {
                    //                    salesid = "";
                    salesmanType = 1;
                    selectTab();
                } else if (salesid.equals("2")) {
                    //                    salesid = "";
                    salesmanType = 2;
                    selectTab();
                } else if (salesid.equals("3")) {
                    //                    salesid = "";
                    salesmanType = 3;
                    selectTab();
                } else {
                }
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadData();
            }
        }, this);
    }*/
    /**
     * 显示员工popup
     */
   /* private void showPwSalesman() {
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

    }*/

    /**
     * 加载拜访明细
     */
    private void loadData() {
        AsyncHttpUtil<ReportTaskBean> httpUtil = new AsyncHttpUtil<>(this, ReportTaskBean.class, new IUpdateUI<ReportTaskBean>() {
            @Override
            public void updata(ReportTaskBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size() > 0) {
                        list.addAll(jsonStr.getDataList());
                    } else {
                        if (mFlag == 2) {
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            page--;
                        } else {
                            showToast("暂无拜访数据!");
                        }
                    }
                    adapter.setData(list);
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
                mScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getReportTaskList, G_RequestParams.getReportTaskList(UserInfoUtils.getId(this),
                salesid, String.valueOf(page), dateStartStr.replace(".", "-"), dateEndStr.replace(".", "-"),
                String.valueOf(salesmanType), "8", "1", "", lineId), true);
    }

    @Override
    public void onSearch(int salesmanType, String searchStr) {
        //搜索员工
        querySalesmanList(true, salesmanType, searchStr);
    }

    @OnClick({R.id.tv_date_type_select, R.id.ll_date_calendar, R.id.tv_all_tab, R.id.tv_salesman_tab, R.id.tv_distribution_tab,
            R.id.ll_salesman_category, R.id.ll_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_type_select:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.ll_date_calendar:
                mTimePickerUtil.showTimePicker(mLlMain, dateStartStr, dateEndStr, true);
                break;
            case R.id.tv_all_tab:
                salesid = "";
                salesmanType = 1;
                selectTab();
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadData();
                break;
            case R.id.tv_salesman_tab:
                salesid = "";
                salesmanType = 2;
                selectTab();
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadData();
                break;
            case R.id.tv_distribution_tab:
                salesid = "";
                salesmanType = 3;
                selectTab();
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadData();
                break;
            case R.id.ll_salesman_category://员工类型
                showSelectPw(1);
                break;
            case R.id.ll_line://线路
                showSelectPw(2);
                break;
        }
    }

    /**
     * 选择popupwindow
     */
    private void initSelectPw() {
        mPwSelect = new PopupWindow(this);
        mPwSelect.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPwSelect.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPwSelect.setBackgroundDrawable(new BitmapDrawable());
        mPwView = LayoutInflater.from(this).inflate(R.layout.pw_salesman_select_layout, null);
        mPwSelect.setContentView(mPwView);
        mPwSelect.setFocusable(true);
        mLvLeft = (ListView) mPwView.findViewById(R.id.type_listview_left);
        mLvRight = (ListView) mPwView.findViewById(R.id.type_listview_right);
        mLlRight = (LinearLayout) mPwView.findViewById(R.id.ll_right);
        mLlSearch = (LinearLayout) mPwView.findViewById(R.id.ll_search);
        mEtSearch = (EditText) mPwView.findViewById(R.id.et_search);

        mPwView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateAnimOut(mPwView, mPwSelect);
            }
        });

        mLlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listSalesman != null)
                    listSalesman.clear();
                adapterSalesmanSelect.notifyDataSetChanged();
                querySalesmanList(true, salesmanType, mEtSearch.getText().toString());
            }
        });

        //业务员类型		1全部 2.所有业务员 3.所有配送员 4.业务员 5.配送员
        listCategory.add(new CategoryBean(1, "全部"));
        listCategory.add(new CategoryBean(2, "所有业务员"));
        listCategory.add(new CategoryBean(3, "所有配送员"));
        listCategory.add(new CategoryBean(4, "业务员"));
        listCategory.add(new CategoryBean(5, "配送员"));

        adapterSalesmanCategory = new SalesmanCategoryAdapter(mItemListener);
        adapterSalesmanCategory.setData(listCategory);

        //员工选择适配器
        adapterSalesmanSelect = new SalesmanSelectAdapter(new ItemOnListener() {
            @Override
            public void onItem(int position) {
                salesid = listSalesman.get(position).getSu_id();
                salesName = listSalesman.get(position).getSu_name();
                mTvSalesmanCategory.setText(salesName);
                initializesRequest();
            }
        });
        mLvRight.setAdapter(adapterSalesmanSelect);

        lineDao = new LineDao(this);
        listName = lineDao.query();
        listName.add(0, new LineBean("", "全部线路"));
        //线路适配器
        adapterLine = new LineSelectAdapter(listName, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                lineId = listName.get(position).getBl_id();
                mTvLine.setText(listName.get(position).getBl_name());
                mTvLine.setTextColor(UIUtils.getColor(R.color.text_blue));
                initializesRequest();
            }
        });
    }

    private MyOnItemListener mItemListener = new MyOnItemListener();

    public class MyOnItemListener implements ItemOnListener {
        @Override
        public void onItem(int position) {
            categoryId = listCategory.get(position).getCategoryId();
            switch (listCategory.get(position).getCategoryId()) {
                case 1://全部
                    salesid = "";
                    salesmanType = 1;
                    mTvSalesmanCategory.setText("全部");
                    initializesRequest();
                    break;
                case 2:
                    salesid = "";
                    salesmanType = 2;
                    mTvSalesmanCategory.setText("所有业务员");
                    initializesRequest();
                    break;
                case 3:
                    salesid = "";
                    salesmanType = 3;
                    mTvSalesmanCategory.setText("所有配送员");
                    initializesRequest();
                    break;
                case 4:
                    salesmanType = 2;
                    mEtSearch.setText("");
                    querySalesmanList(true, salesmanType, "");
                    break;
                case 5:
                    salesmanType = 3;
                    mEtSearch.setText("");
                    querySalesmanList(true, salesmanType, "");
                    break;
            }
            adapterSalesmanCategory.setCategoryId(listCategory.get(position).getCategoryId());
        }
    }

    /**
     * 显示不同类型数据,1.员工类型,2.线路类型
     */
    private void showSelectPw(final int type) {
        switch (type) {
            case 1://员工类型
                mLlRight.setVisibility(View.VISIBLE);
                adapterSalesmanCategory.setCategoryId(categoryId);
                mLvLeft.setAdapter(adapterSalesmanCategory);
                if (categoryId > 3) {
                    mItemListener.onItem(categoryId - 1);
                } else {
                    if (listSalesman != null)
                        listSalesman.clear();
                    adapterSalesmanSelect.setSalesmanId(salesid);
                    adapterSalesmanSelect.setData(listSalesman);
                }
                mEtSearch.setText("");
                showPopup();
                break;
            case 2://线路
                mLlRight.setVisibility(View.GONE);
                adapterLine.setLineId(lineId);
                mLvLeft.setAdapter(adapterLine);
                showPopup();
                break;
            default:
                break;
        }
    }

    private void showPopup() {
        //显示popup
        if (mPwSelect.isShowing()) {
            translateAnimOut(mPwView, mPwSelect);
            return;
        }
        if (Build.VERSION.SDK_INT < 24) {
            mPwSelect.showAsDropDown(mLlSelect, 0, 3);
        } else {
            int[] a = new int[2];
            mLlSelect.getLocationInWindow(a);
            mPwSelect.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, mLlSelect.getHeight() + a[1] + 3);
            mPwSelect.update();
        }
        translateAnimIn(mPwView);
    }

    @Override
    public void onImgItem(int i, int k) {
        //图片点击
        Intent intent = new Intent(SalesmanVisitingActivity.this, PhotoActivity.class);
        intent.putExtra("ID", k);
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < list.get(i).getImageList().size(); j++) {
            jsonArray.put(list.get(i).getImageList().get(j));
        }
        intent.putExtra("drr", jsonArray.toString());
        intent.putExtra("isNetwork", 1);
        LogUtils.e("drr", jsonArray.toString());
        startActivityForResult(intent, 101);
    }

    /**
     * 切换按钮状态
     */
    private void selectTab() {
        restoreTab();
        switch (salesmanType) {
            case 1:
                mTvAllTab.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvAllTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvAllTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(0);
                alterWidth(mTvAllTab);
                break;
            case 2:
                mTvSalesmanTab.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvSalesmanTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvSalesmanTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(1);
                alterWidth(mTvSalesmanTab);
                break;
            case 3:
                mTvDistributionTab.setTextColor(UIUtils.getColor(R.color.color_blue));
                mTvDistributionTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mTvDistributionTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                moveAnimation(2);
                alterWidth(mTvDistributionTab);
                break;
        }
    }

    /**
     * 恢复所有选项的样式
     */
    private void restoreTab() {
        mTvAllTab.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvAllTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvAllTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvSalesmanTab.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvSalesmanTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvSalesmanTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        mTvDistributionTab.setTextColor(UIUtils.getColor(R.color.text_gray));
        mTvDistributionTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTvDistributionTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 根据TextView的宽度修改线的宽度
     *
     * @param tv
     */
    private void alterWidth(TextView tv) {
        TextPaint paint = tv.getPaint();
        paint.setTextSize(tv.getTextSize());
        float width = paint.measureText(tv.getText().toString()); //这个方法能把文本所占宽度衡量出来.

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
        lp.width = (int) width + UIUtils.dp2px(5);
        mVLine.setLayoutParams(lp);
    }

    private void moveAnimation(int index) {
        ObjectAnimator
                .ofFloat(
                        mLine,
                        "translationX",
                        mLine.getTranslationX(),
                        getResources().getDisplayMetrics().widthPixels / 3
                                * index).setDuration(300).start();
    }

    @Override
    public void onItemDate(int position, String dateName, String startDate, String endDate) {
        LogUtils.e("dateOnePopup", position + "-" + dateName);
        //1.今日;2.本周;3.本月;5.上月;6.昨天;7.上周
        dateType = position;
        mTvDateTypeSelect.setText(dateName);
        dateStartStr = startDate;
        dateEndStr = endDate;
        mTvDate.setText(dateStartStr + "-" + dateEndStr);

        list.clear();
        adapter.notifyDataSetChanged();
        page = 1;
        mFlag = 1;
        loadData();
    }

    /**
     * PopupWindow显示的动画
     */
    private void translateAnimIn(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim_in);
        view.startAnimation(animation);
    }

    /**
     * PopupWindow消失的动画
     */
    private void translateAnimOut(View view, final PopupWindow pw) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim_out);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        lineDao.destroy();
    }

}
