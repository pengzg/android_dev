package com.xdjd.distribution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.PaymentAdapter;
import com.xdjd.distribution.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PaymentFragment extends BaseFragment {

    @BindView(R.id.lv_payment)
    ListView mLvPayment;
    @BindView(R.id.category_tv)
    TextView mCategoryTv;
    @BindView(R.id.iv_print)
    ImageView mIvPrint;
    @BindView(R.id.ll_print)
    LinearLayout mLlPrint;
    @BindView(R.id.tv_buy_total_price)
    TextView mTvBuyTotalPrice;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;

    private PaymentAdapter adapter;

    private boolean isPrint = true;//默认打印

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        adapter = new PaymentAdapter();
        mLvPayment.setAdapter(adapter);

        if (isPrint){
            mIvPrint.setImageResource(R.drawable.check_true);
        }
    }

    @OnClick({R.id.ll_print, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_print:
                if (isPrint){
                    isPrint = false;
                }else{
                    isPrint = true;
                }

                if (isPrint){
                    mIvPrint.setImageResource(R.drawable.check_true);
                }else{
                    mIvPrint.setImageResource(R.drawable.check_false);
                }
                break;
            case R.id.tv_submit:
                break;
        }
    }
}
