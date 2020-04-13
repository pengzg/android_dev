package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.InventoryGoodsAdapter;
import com.xdjd.distribution.adapter.OrderSettlementAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.InventoryItemStrBean;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class InventorySubmitActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.et_note)
    EditText mEtNote;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.lv_goods)
    ListView mLvGoods;

    List<GoodsBean> listInventry;
    private ClientBean clientBean;
    private String totalPrice = "0.00";

    private List<String> pathList;

    private InventoryGoodsAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_inventory_submit;
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
        mTitleBar.setTitle("客户盘点提交");

        clientBean = UserInfoUtils.getClientInfo(this);
        listInventry = (List<GoodsBean>) getIntent().getSerializableExtra("list");
        totalPrice = getIntent().getStringExtra("totalPrice");
        pathList = (List<String>) getIntent().getSerializableExtra("pathList");

        mTvName.setText(clientBean.getCc_name());
        mTvTotalNum.setText(listInventry.size() + "条");

        adapter = new InventoryGoodsAdapter();
        mLvGoods.setAdapter(adapter);
        adapter.setData(listInventry);
    }

    @OnClick({R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                submitInventory();
                break;
        }
    }

    private void submitInventory() {
        List<InventoryItemStrBean> list = new ArrayList<>();
        for (GoodsBean bean : listInventry) {
            InventoryItemStrBean beanStr = new InventoryItemStrBean();

            beanStr.setCii_price_id(bean.getGgp_id());
            beanStr.setCii_goodsId(bean.getGgp_goodsid());
            beanStr.setCii_goods_amount(bean.getTotalPrice());
            beanStr.setCii_display_quantity(bean.getDisplay_quantity());
            beanStr.setCii_duitou_quantity(bean.getDuitou_quantity());

            beanStr.setCii_goods_name(bean.getGg_title());
            beanStr.setCii_goods_price_max(bean.getMaxPrice());
            beanStr.setCii_goods_price_min(bean.getMinPrice());
            beanStr.setCii_goods_quantity_max(bean.getMaxNum());
            beanStr.setCii_goods_quantity_min(bean.getMinNum());
            beanStr.setCii_create_date(bean.getInventory_date());
            beanStr.setCii_note(bean.getRemarks());

            list.add(beanStr);
        }
        String inventoryItemStr = JsonUtils.toJSONString(list);

        StringBuilder sb = new StringBuilder();
        if (pathList != null && pathList.size() > 0)
            for (int i = 0; i < pathList.size(); i++) {
                sb.append(pathList.get(i));
                if (i != pathList.size())
                    sb.append(",");
            }

        AsyncHttpUtil<BaseBean> httpUtil = new AsyncHttpUtil<>(this, BaseBean.class, new IUpdateUI<BaseBean>() {
            @Override
            public void updata(BaseBean jsonStr) {
                if ("00".equals(jsonStr.getRepCode())) {
                    showToast(jsonStr.getRepMsg());
                    Intent intent = new Intent(InventorySubmitActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finishActivity();
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
            }
        });
        httpUtil.post(M_Url.addInventory, L_RequestParams.addInventory(clientBean.getCc_id(), mEtNote.getText().toString(),
                totalPrice, String.valueOf(listInventry.size()), inventoryItemStr, sb.toString()), true);
    }
}
