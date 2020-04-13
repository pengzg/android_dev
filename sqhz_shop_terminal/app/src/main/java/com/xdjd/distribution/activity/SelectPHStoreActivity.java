package com.xdjd.distribution.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.event.RolloutGoodsFinishEvent;
import com.xdjd.distribution.popup.SelectStorePopup;
import com.xdjd.utils.DateUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SelectPHStoreActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_distribution_time)
    TextView mTvDistributionTime;
    @BindView(R.id.tv_select_store)
    TextView mTvSelectStore;
    @BindView(R.id.ll_store_main)
    LinearLayout mLlStoreMain;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    /**
     * 选择的客户信息
     */
    private ClientBean clientBean;

    private UserBean userBean;

    /**
     * 仓库选择popup
     */
    private SelectStorePopup popupStore;

    private List<StorehouseBean> listStore;

    /**
     * 仓库id
     */
    private String storehouseId;
    /**
     * 仓库名称
     */
    private String storehouseName;

    /**
     * 发货时间
     */
    private String deliveryTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 订单复制参数
     */
    private GoodsBean beanCopy;

    /**
     * 复制的订单商品
     */
    private List<GoodsBean> listGoods;

    private DatePickerDialog dateDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_ph_store;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("选择发货仓库");
        EventBus.getDefault().register(this);

        userBean = UserInfoUtils.getUser(this);

        clientBean = UserInfoUtils.getClientInfo(this);
        mTvName.setText(clientBean.getCc_name());

        deliveryTime = DateUtils.getTomorrowDay(new Date(), "yyyy-MM-dd");
        mTvDistributionTime.setText(deliveryTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringUtils.toDateFormater2(mTvDistributionTime.getText().toString()));
        dateDialog = new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        listStore = new ArrayList<>();

        initStorePopup();

        //订单复制参数
        beanCopy = (GoodsBean) getIntent().getSerializableExtra("beanCopy");
        if (beanCopy != null) {
            storehouseId = beanCopy.getOm_storeid();
            storehouseName = beanCopy.getOm_storeId_name();
            mTvSelectStore.setText(storehouseName);
        } else {
            queryStorehouseList(false);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_distribution_time, R.id.tv_select_store, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_distribution_time://选择日期
                dateDialog.show();
                break;
            case R.id.tv_select_store://选择仓库
                queryStorehouseList(true);
                break;
            case R.id.btn_submit:
                if (storehouseId == null || "".equals(storehouseId)) {
                    showToast("请选择仓库");
                    return;
                }
                Intent intent = new Intent(this, RolloutGoodsDeclareActivity.class);
                if (beanCopy != null) {//复制参数对象
                    intent.putExtra("beanCopy", beanCopy);
                }
                intent.putExtra("storehouseId", storehouseId);
                intent.putExtra("deliveryTime", deliveryTime);
                startActivity(intent);
                break;
        }
    }

    /**
     * 日选择回调接口
     */
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
            String date = arg1 + "-" + (arg2 + 1) + "-" + arg3;

            if (DateUtils.calDateDifferent(DateUtils.getDate2(), date) == -1) {
                showToast("发货时间不能小于今天");
                return;
            }

            deliveryTime = DateUtils.getCurTimeStr(date);
            mTvDistributionTime.setText(deliveryTime);

            Calendar cStrar = Calendar.getInstance();
            cStrar.setTime(StringUtils.toDateFormater2(mTvDistributionTime.getText().toString()));
            dateDialog.updateDate(cStrar.get(Calendar.YEAR),
                    cStrar.get(Calendar.MONTH), cStrar.get(Calendar.DAY_OF_MONTH));
        }
    };

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
                            showPwLines();
                        } else {
                            //                            if (UserInfoUtils.getStoreId(SelectStoreActivity.this).equals("")) {
                            storehouseId = listStore.get(0).getBs_id();
                            storehouseName = listStore.get(0).getBs_name();
                            //                                UserInfoUtils.setStoreName(SelectStoreActivity.this, storehouseName);
                            //                                UserInfoUtils.setStoreId(SelectStoreActivity.this, storehouseId);
                            mTvSelectStore.setText(storehouseName);
                        }
                        //                        }
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
                L_RequestParams.queryStorehouseList(clientBean.getCc_id(), "", "3"), isDialog);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(this, dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(final int position) {

                if (beanCopy != null) {
                    if (!storehouseId.equals(listStore.get(position).getBs_id())) {
                        DialogUtil.showCustomDialog(SelectPHStoreActivity.this, "提示",
                                "切换仓库会清除再来一单的商品信息,是否继续?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                                    @Override
                                    public void ok() {
                                        storehouseId = listStore.get(position).getBs_id();
                                        storehouseName = listStore.get(position).getBs_name();
                                        mTvSelectStore.setText(listStore.get(position).getBs_name());
                                        beanCopy = null;
                                    }

                                    @Override
                                    public void no() {
                                    }
                                });
                    }
                }else{
                    storehouseId = listStore.get(position).getBs_id();
                    storehouseName = listStore.get(position).getBs_name();
                    //                UserInfoUtils.setStoreName(SelectStoreActivity.this, storehouseName);
                    //                UserInfoUtils.setStoreId(SelectStoreActivity.this, storehouseId);
                    mTvSelectStore.setText(listStore.get(position).getBs_name());
                }

                popupStore.dismiss();
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
        popupStore.showAtLocation(mLlStoreMain,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void onEventMainThread(RolloutGoodsFinishEvent event) {
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
