package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.IntegralStoreAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.IntegralGoodsBean;
import com.xdjd.storebox.bean.IntegralGradeBean;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.PublicFinal;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2017/2/23.
 */

public class IntegralStoreActivity extends BaseActivity implements IntegralStoreAdapter.detailListener {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.lv_integral_store)
    PullToRefreshListView lvIntegralStore;
    @BindView(R.id.filter_l)
    LinearLayout filterL;
    @BindView(R.id.tv_integral_num)
    TextView tvIntegralNum;
    private IntegralStoreAdapter adapter;
    private  VaryViewHelper mVaryViewHelper = null;
    private int pageNo = 1;
    private int mFlag = 0;
    private String searchType = "";

    private OptionsPickerView pickerView;
    private List<IntegralGradeBean> list = new ArrayList<>();
    private List<IntegralGoodsBean> goodsList=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_integral_store);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("积分商城");
        //titleBar.setRightText("搜索");
        titleBar.setRightTextColor(R.color.text_black_212121);
        mVaryViewHelper = new VaryViewHelper(lvIntegralStore);
        adapter = new IntegralStoreAdapter(this);
        lvIntegralStore.setAdapter(adapter);

        pickerView = new OptionsPickerView(this);
        pickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                tvIntegralNum.setText(list.get(options1).getPriceDesc());
                searchType = list.get(options1).getPriceId();
                pageNo = 1;
                mFlag =0;
                loadData(PublicFinal.TWO);
            }
        });

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("onResume","onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e("onRestart","onRestart");
    }

    /*上下拉刷新数据*/
    private void initView(){
        lvIntegralStore.setMode(PullToRefreshBase.Mode.BOTH);
        initRefresh(lvIntegralStore);
        lvIntegralStore.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 1;
                pageNo = 1;
                goodsList.clear();
                adapter.notifyDataSetChanged();
                loadData(PublicFinal.TWO);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mFlag = 2;
                pageNo++;
                loadData(PublicFinal.TWO);
            }
        });
        adapter = new IntegralStoreAdapter(this);
        lvIntegralStore.setAdapter(adapter);
        loadData(PublicFinal.FIRST);
    }

    /*获取积分商品*/
    private void loadData(int isFirst){
        if(isFirst == PublicFinal.FIRST ){
            mVaryViewHelper.showLoadingView();
        }
        AsyncHttpUtil<IntegralGoodsBean> httpUtil = new AsyncHttpUtil<>(this, IntegralGoodsBean.class, new IUpdateUI<IntegralGoodsBean>() {
            @Override
            public void updata(IntegralGoodsBean bean) {
                if (bean.getRepCode().equals("00")){
                    if (null!= bean.getListData()&& bean.getListData().size() > 0){
                        if (mFlag == 0 || mFlag == 1){
                        }
                        goodsList.clear();
                        goodsList.addAll(bean.getListData());
                        adapter.setData(goodsList) ;
                        adapter.notifyDataSetChanged();
                        mVaryViewHelper.showDataView();
                    }else{
                        if (mFlag == 2){
                            showToast("没有更多数据了！");
                            pageNo--;
                        }else{
                            mVaryViewHelper.showEmptyView("还没有积分商品");
                        }
                    }

                }else{
                    showToast(bean.getRepMsg());
                }
            }
            @Override
            public void sendFail(ExceptionType s) {
                //mVaryViewHelper.showErrorView(new ActionActivity.OnErrorListener());
            }
            @Override
            public void finish() {
                lvIntegralStore.onRefreshComplete();
            }
        });
        httpUtil.post(M_Url.getIntegralGoodsList, L_RequestParams.GetIntegralGoodsList(
                UserInfoUtils.getId(IntegralStoreActivity.this),String.valueOf(pageNo),"10",searchType) ,false);
    }

    @Override
    public void item(int wig_id,int wig_stock) {
        Intent intent = new Intent(IntegralStoreActivity.this, IntegralGoodsDetailActivity.class);
        intent.putExtra("gpId", wig_id);//商品id 112
        intent.putExtra("gs",wig_stock);
        startActivity(intent);
    }

    @OnClick(R.id.filter_l)
    public void onClick() {
        loadDataGrade();
    }

    /*获取积分档次
     */
    private void loadDataGrade(){
        AsyncHttpUtil<IntegralGradeBean> httpUtil = new AsyncHttpUtil<>(this, IntegralGradeBean.class, new IUpdateUI<IntegralGradeBean>() {
            @Override
            public void updata(IntegralGradeBean bean){
                if(bean.getRepCode().equals("00")){
                    ArrayList<String> gradeList = new ArrayList<>();
                    list.clear();
                    if(bean.getListData() != null && bean.getListData().size() > 0){
                        list.addAll(bean.getListData());
                    }
                    for(int i= 0; i< list.size();i++){
                        gradeList.add(list.get(i).getPriceDesc());
                    }
                    pickerView.setPicker(gradeList);
                    pickerView.setCyclic(false);//是否循环
                    pickerView.show();
                }else{
                    showToast(bean.getRepMsg());
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
        httpUtil.post(M_Url.getIntegralGrage,L_RequestParams.GetIngetralGrade(UserInfoUtils.getId(this)),false);
    }



}
