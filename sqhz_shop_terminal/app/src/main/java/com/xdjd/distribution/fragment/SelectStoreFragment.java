package com.xdjd.distribution.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.OrderDeclareActivity;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.StorehouseBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.SelectStorePopup;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/17
 *     desc   : 选择仓库fragment
 *     version: 1.0
 * </pre>
 */

public class SelectStoreFragment extends BaseFragment {

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
    private String storehouseId = "74";
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_store, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        mTitleBar.leftBack(getActivity());
        mTitleBar.setTitle("选择发货仓库");

        userBean = UserInfoUtils.getUser(getActivity());

        clientBean = UserInfoUtils.getClientInfo(getActivity());
        mTvName.setText(clientBean.getCc_name());
        deliveryTime = StringUtils.getDate2();
        mTvDistributionTime.setText(deliveryTime);

        listStore = new ArrayList<>();

        initStorePopup();
    }

    @OnClick({R.id.tv_distribution_time, R.id.tv_select_store, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_distribution_time://选择日期
                // 实例化日历类
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2,
                                          int arg3) {
                        deliveryTime = arg1 + "-" + (arg2 + 1) + "-" + arg3;
                        mTvDistributionTime.setText(deliveryTime);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DATE)).show();
                break;
            case R.id.tv_select_store://选择仓库
                queryStorehouseList();
                break;
            case R.id.btn_submit:
                if (storehouseId == null || "".equals(storehouseId)){
                    showToast("请选择仓库");
                    return;
                }
                Intent intent = new Intent(getActivity(), OrderDeclareActivity.class);
                intent.putExtra("storehouseId", storehouseId);
                intent.putExtra("deliveryTime", deliveryTime);
                intent.putExtra("note",note);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取仓库列表接口
     */
    private void queryStorehouseList() {
        AsyncHttpUtil<StorehouseBean> httpUtil = new AsyncHttpUtil<>(getActivity(), StorehouseBean.class, new IUpdateUI<StorehouseBean>() {
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
                L_RequestParams.queryStorehouseList(clientBean.getCc_id(),"","4"), true);
    }

    /**
     * 初始化仓库popup
     */
    private void initStorePopup() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        popupStore = new SelectStorePopup(getActivity(), dm.heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                storehouseId = listStore.get(position).getBs_id();
                storehouseName = listStore.get(position).getBs_name();
                mTvSelectStore.setText(listStore.get(position).getBs_name());
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
}
