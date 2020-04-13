package com.xdjd.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/22
 *     desc   : 退货仓库选择
 *     version: 1.0
 * </pre>
 */

public class RefundStoreActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.tv_select_store)
    TextView mTvSelectStore;
    @BindView(R.id.ll_store_main)
    LinearLayout mLlStoreMain;


    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;

    private List<StorehouseBean> listStore;

    private UserBean userBean;

    /**
     * 仓库id
     */
    private String storehouseId;
    /**
     * 仓库名称
     */
    private String storehouseName;

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_store;
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
        mTitleBar.setTitle("退货申请");

        userBean = UserInfoUtils.getUser(this);

//        storehouseId = UserInfoUtils.getStoreId(this);
//        storehouseName = UserInfoUtils.getStoreName(this);
//        mTvSelectStore.setText(storehouseName);

        initStorePopup();
        queryStorehouseList(false);
    }

    @OnClick({R.id.tv_select_store, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_store:
                if (listStore == null || listStore.size() == 0){
                    queryStorehouseList(true);
                }else{
                    showPwStore();
                }
                break;
            case R.id.btn_submit:
                if (storehouseId == null || "".equals(storehouseId)){
                    showToast("请选择仓库");
                    return;
                }
                Intent intent = new Intent(this, RefundApplyActivity.class);
                intent.putExtra("storehouseId", storehouseId);
                startActivity(intent);
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
                        if (isDialog) {
                            showPwStore();
                        } else {
//                            if (UserInfoUtils.getStoreId(RefundStoreActivity.this).equals("")) {
                                storehouseId = listStore.get(0).getBs_id();
                                storehouseName = listStore.get(0).getBs_name();
//                                UserInfoUtils.setStoreName(RefundStoreActivity.this, storehouseName);
//                                UserInfoUtils.setStoreId(RefundStoreActivity.this, storehouseId);
                                mTvSelectStore.setText(storehouseName);
//                            }
                        }
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
                L_RequestParams.queryStorehouseList( "","","2"), isDialog);
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
//                UserInfoUtils.setStoreName(RefundStoreActivity.this, storehouseName);
//                UserInfoUtils.setStoreId(RefundStoreActivity.this, storehouseId);
                mTvSelectStore.setText(listStore.get(position).getBs_name());
                popupStore.dismiss();
            }
        });
    }

    /**
     * 显示仓库popup
     */
    private void showPwStore() {
        popupStore.setData(listStore);
        popupStore.setId(storehouseId);
        // 显示窗口
        popupStore.showAtLocation(mLlStoreMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }
}
