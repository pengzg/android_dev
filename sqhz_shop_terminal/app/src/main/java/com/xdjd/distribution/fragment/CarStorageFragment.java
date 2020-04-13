package com.xdjd.distribution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.CarStoreAdapter;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.CarStorageBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.NoScrollListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CarStorageFragment extends BaseFragment {

    @BindView(R.id.lv_car_store)
    NoScrollListView mLvCarStore;
    @BindView(R.id.pull_scroll)
    PullToRefreshScrollView mPullScroll;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.ll_is_zero_store)
    LinearLayout mLlIsZeroStore;
    @BindView(R.id.tv_select_num)
    TextView mTvSelectNum;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;

    private View view;

    private UserBean userBean;

    private CarStoreAdapter adapter;

    public List<CarStorageBean> list = new ArrayList<>();

    private int mFlag = 0;
    private int page = 1;

    private String type = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car_storage, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());

        initRefresh(mPullScroll);
        mPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        mPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 1;
                page = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getStockGoods("",false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFlag = 2;
                page++;
                getStockGoods("",false);
            }
        });

        adapter = new CarStoreAdapter();
        mLvCarStore.setAdapter(adapter);

        if (type.equals("1")){//是否有0库存		 1 有  2无
            mIvIcon.setImageResource(R.drawable.check_true);
        }else{
            mIvIcon.setImageResource(R.drawable.check_false);
        }

        if (!"".equals(userBean.getSu_storeid())){
            getStockGoods("",true);
        }
    }

    @OnClick({R.id.ll_is_zero_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_is_zero_store:
                if (type.equals("1")){//是否有0库存		 1 有  2无
                    type = "2";
                    mIvIcon.setImageResource(R.drawable.check_false);
                }else{
                    type = "1";
                    mIvIcon.setImageResource(R.drawable.check_true);
                }
                page = 1;
                mFlag = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                getStockGoods("",true);
                break;
        }
    }

    private void getStockGoods(String searchKey,boolean isDialog) {
        AsyncHttpUtil<CarStorageBean> httpUtil = new AsyncHttpUtil<>(getActivity(), CarStorageBean.class, new IUpdateUI<CarStorageBean>() {
            @Override
            public void updata(CarStorageBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getListData() != null && bean.getListData().size() > 0) {
                        list.addAll(bean.getListData());
                        adapter.setData(list);
                    } else {
                        if (mFlag == 0 || mFlag == 1) {
                            adapter.notifyDataSetChanged();
                        } else {
                            page--;
                            showToast("没有更多数据了");
                        }
                    }
                } else {
                    showToast(bean.getRepMsg());
                }
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
        httpUtil.post(M_Url.getStockGoods, L_RequestParams.
                getStockGoods(userBean.getUserId(), userBean.getSu_storeid(), String.valueOf(page), searchKey,type), isDialog);
    }

    private void PriceAndNum(){
        if (list != null && list.size() > 0){
            mTvSelectNum.setText("已选("+list.size()+")件");
        }else{
            mTvSelectNum.setText("已选(0)件");
        }

        if (list.size() > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (CarStorageBean bean : list) {
                BigDecimal bdPrice = new BigDecimal(bean.getGoodsAmount());
                sum = sum.add(bdPrice);
            }
            mTvTotalPrice.setText("¥:" + sum.setScale(2).doubleValue() + "元");
        } else {
            mTvTotalPrice.setText("¥:0.00元");
        }
    }

    public void searchGoods(String s) {
        page = 1;
        list.clear();
        mFlag = 1;
        adapter.notifyDataSetInvalidated();
        getStockGoods(s,true);
    }
}
