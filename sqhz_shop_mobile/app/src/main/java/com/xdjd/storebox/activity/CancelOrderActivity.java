package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.CancelOrderReasonAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.CancelOrderReasonBean;
import com.xdjd.storebox.bean.OrderDetailBean;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.tool.UserInfo;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2016/12/15.
 */

public class CancelOrderActivity extends BaseActivity implements CancelOrderReasonAdapter.itemListener{
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.input_reason)
    EditText inputReason;
    @BindView(R.id.reasonList)
    NoScrollListView reasonList;
    private OrderDetailBean bean;
    private String reasonId = "0";
    private CancelOrderReasonAdapter adapter;
    private List<CancelOrderReasonBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        ButterKnife.bind(this);
        titleBar.setTitle("取消订单");
        titleBar.leftBack(this);
        titleBar.setRightText("提交");
        titleBar.setRightTextColor(R.color.color_EC193A);
        bean = (OrderDetailBean) getIntent().getSerializableExtra("bean");
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((reasonId.equals("0") || inputReason.getText().equals(""))) {
                    showToast("请赐予一个理由吧！");
                    return;
                }
                Dilog_cancel();
            }
        });
        adapter = new CancelOrderReasonAdapter(this);
        reasonList.setAdapter(adapter);
        queryCancelReasons();
    }

    void Dilog_cancel() {
        DialogUtil.showCustomDialog(this, "确定取消订单?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                CancelOrder(reasonId, inputReason.getText().toString());
            }

            @Override
            public void no() {
            }
        });
    }

    @Override
    public void item(int i, String cancelId) {
        adapter.setFlag(i);
        reasonId = cancelId;
    }

    /**
     * 获取取消原因
     * @param
     */
    private void queryCancelReasons(){
        AsyncHttpUtil<CancelOrderReasonBean> httpUtil = new AsyncHttpUtil(this, CancelOrderReasonBean.class, new IUpdateUI<CancelOrderReasonBean>() {
            @Override
            public void updata(CancelOrderReasonBean bean) {
                    if(bean.getRepCode().equals("00")){
                        if(bean.getListData() != null && bean.getListData().size() != 0){
                            list.addAll(bean.getListData());
                            adapter.notifyDataSetChanged();
                            adapter.setData(list);
                        }
                    }else{
                        showToast(bean.getRepMsg());
                    }
            }

            @Override
            public void sendFail(ExceptionType s) {
                showToast(bean.getRepMsg());
            }

            @Override
            public void finish() {}
        });
        httpUtil.post(M_Url.queryCancelReasons,L_RequestParams.queryCancelReasons(UserInfoUtils.getId(this)),false);
    }

    //取消订单
    private void CancelOrder(String reasonId, String reason) {
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        Intent intent = new Intent(CancelOrderActivity.this, MyOrderActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        httpUtil.post(M_Url.CancelOrder, L_RequestParams.CancelOrder(UserInfoUtils.getId(this), bean.getOm_id(),
                reasonId, reason, bean.getOm_version()), true);
    }
}
