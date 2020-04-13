package com.xdjd.distribution.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.event.BindingFacilityEvent;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OpenMallSuccessActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.to_order_detail)
    Button mToOrderDetail;

    @Override
    protected int getContentView() {
        return R.layout.activity_open_mall_success;
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
        mTitleBar.setTitle("开通商城成功");

    }

    @OnClick({R.id.to_order_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_order_detail:
                finishActivity();
                break;
        }
    }
}
