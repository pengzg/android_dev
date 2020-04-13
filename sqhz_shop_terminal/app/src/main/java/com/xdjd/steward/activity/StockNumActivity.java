package com.xdjd.steward.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.SetTimeActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.steward.adapter.StockNumAdapter;
import com.xdjd.steward.bean.GoodsStockBean;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.StringUtils;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2017/7/11.
 */

public class StockNumActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    /*@BindView(R.id.front_ll)
    LinearLayout mFrontLl;
    @BindView(R.id.tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.tv_end_date)
    TextView mTvEndDate;
    @BindView(R.id.ll_date_section)
    LinearLayout mLlDateSection;
    @BindView(R.id.backwards_ll)
    LinearLayout mBackwardsLl;*/
    @BindView(R.id.lv_no_scroll)
    NoScrollListView mLvNoScroll;
    @BindView(R.id.scroll)
    PullToRefreshScrollView mScroll;
    @BindView(R.id.rl_select_stock)
    RelativeLayout mRlSelectStock;
    @BindView(R.id.tv_stock)
    TextView mTvStock;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    private DisplayMetrics metric;

    private String dateStartStr;
    private String dateEndStr;

    private VaryViewHelper mVaryViewHelper = null;

    private int page = 1;
    private int mFlag = 0;

    /**
     * 仓库列表
     */
    private List<StorehouseBean> listStore;

    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;

    /**
     * 仓库id
     */
    private String storehouseId;
    /**
     * 仓库名称
     */
    private String storehouseName;

    private StockNumAdapter adapter;

    /**
     * 商品库存数量数据集合
     */
    public List<GoodsStockBean> list = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_stock_num;
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
        mTitleBar.setTitle("库存数量");

        //获取屏幕总宽度
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        dateStartStr = DateUtils.getDataTime(DateUtils.dateFormater4);
        dateEndStr = DateUtils.getDataTime(DateUtils.dateFormater4);

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
                loadData(PublicFinal.TWO,true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                mFlag = 2;
                loadData(PublicFinal.TWO,true);
            }
        });

        adapter = new StockNumAdapter();
        mLvNoScroll.setAdapter(adapter);

        initStorePopup();
        loadData(PublicFinal.FIRST,false);
    }

    @OnClick({R.id.rl_select_stock})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_select_stock://选择仓库
                if (listStore == null || listStore.size() == 0){
                    queryStorehouseList(true);
                }else{
                    showPwLines();
                }
                break;
        }
    }

    /**
     * 获取仓库列表接口
     */
    private void queryStorehouseList(final boolean isDialog) {
        AsyncHttpUtil<StorehouseBean> httpUtil = new AsyncHttpUtil<>(this, StorehouseBean.class, new IUpdateUI<StorehouseBean>() {
            @Override
            public void updata(StorehouseBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listStore = jsonBean.getListData();
                    if (listStore != null && listStore.size() > 0) {
                        showPwLines();
                    } else {
                        showToast("没有仓库数据");
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
        httpUtil.post(M_Url.queryStorehouseList,
                L_RequestParams.queryStorehouseList("", "1","4"), isDialog);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                storehouseId = listStore.get(position).getBs_id();
                storehouseName = listStore.get(position).getBs_name();
                mTvStock.setText(listStore.get(position).getBs_name());
                popupStore.dismiss();

                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                mFlag = 1;
                loadData(PublicFinal.FIRST,false);
            }
        });
    }

    /**
     * 显示仓库popup
     */
    private void showPwLines() {
        popupStore.setData(listStore);
        popupStore.setId(storehouseId);
        // 显示窗口
        popupStore.showAtLocation(mLlMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    /**
     * 加载库存数量接口
     */
    private void loadData(int isFirst,boolean isDialog){
        /*if (storehouseId == null || storehouseId.equals("")){
            showToast("请选择仓库!");
            mScroll.onRefreshComplete();
            return;
        }*/
        if (isFirst== PublicFinal.FIRST){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<GoodsStockBean> httpUtil = new AsyncHttpUtil<>(this,GoodsStockBean.class , new IUpdateUI<GoodsStockBean>() {
            @Override
            public void updata(GoodsStockBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())){
                    if (jsonStr.getDataList() != null && jsonStr.getDataList().size()>0){
                        list.addAll(jsonStr.getDataList());
                        mVaryViewHelper.showDataView();
                    }else{
                        if (mFlag == 2){
                            showToast(UIUtils.getString(R.string.on_pull_remind));
                            page--;
                            mVaryViewHelper.showDataView();
                        }else{
                            mVaryViewHelper.showEmptyView();
                        }
                    }
                    adapter.setData(list);
                }else{
                    showToast(jsonStr.getRepMsg());
                    mVaryViewHelper.showErrorView(jsonStr.getRepMsg(),new OnErrorListener());
                }
            }

            @Override
            public void sendFail(ExceptionType s) {
                mVaryViewHelper.showErrorView(s.getDetail(),new OnErrorListener());
            }

            @Override
            public void finish() {
                mScroll.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getGoodsStock, G_RequestParams.getGoodsStock(UserInfoUtils.getId(this),
                storehouseId,"",String.valueOf(page),"8"),isDialog);
    }

    public class OnErrorListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            list.clear();
            adapter.notifyDataSetChanged();
            page = 1;
            mFlag = 1;
            loadData(PublicFinal.FIRST,false);
        }
    }

    /**
     * 日选择回调接口
     */
    /*private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
            String date = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            date = DateUtils.getCurTimeStr(date);
            if (isStart) {
                int flag = DateUtils.calDateDifferent(mTvEndDate.getText().toString(), date);
                if (flag == 1) {
                    showToast("不能高于结束时间");
                    return;
                }

                mTvStartDate.setText(date);
                Calendar cStrar = Calendar.getInstance();
                cStrar.setTime(StringUtils.toDateFormater2(mTvStartDate.getText().toString()));
                startDateDialog.updateDate(cStrar.get(Calendar.YEAR),
                        cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
            } else {
                if (DateUtils.isMoreThanToday(date)) {
                    showToast("结束时间不能超过今天");
                    return;
                }

                if (DateUtils.calDateDifferent(mTvStartDate.getText().toString(), date) == -1) {
                    showToast("不能低于开始时间");
                    return;
                }

                mTvEndDate.setText(date);
                Calendar cEnd = Calendar.getInstance();
                cEnd.setTime(StringUtils.toDateFormater2(mTvEndDate.getText().toString()));
                endDateDialog.updateDate(cEnd.get(Calendar.YEAR),
                        cEnd.get(Calendar.MONTH), cEnd.get(Calendar.DAY_OF_MONTH));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                dateStartStr = data.getStringExtra("startData");
                dateEndStr = data.getStringExtra("endData");

                mTvStartDate.setText(dateStartStr);
                mTvEndDate.setText(dateEndStr);

                dateStartNum = 0;
                dateEndNum = 0;

                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadData();
                break;
        }
    }*/

}
