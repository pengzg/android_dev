package com.xdjd.steward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.adapter.LineListAdapter;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.distribution.bean.EquipmentBean;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.dao.LineDao;
import com.xdjd.distribution.popup.EquipmentListingPopup;
import com.xdjd.distribution.popup.LineSelectPopup;
import com.xdjd.steward.main.StewardActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/9
 *     desc   : 编辑线路界面
 *     version: 1.0
 * </pre>
 */

public class EditCustomerLineActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.ll_add_line)
    LinearLayout mLlAddLine;
    @BindView(R.id.lv_line)
    NoScrollListView mLvLine;
    @BindView(R.id.btn_binding)
    Button mBtnBinding;
    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.tv_contact_name)
    TextView mTvContactName;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.ll_customer)
    LinearLayout mLlCustomer;

    //线路选择列表
    private LineSelectPopup popupLine;

    private LineListAdapter adapter;
    private Intent intent;

    private AddressListBean customer;

    private List<LineBean> listLineOld = new ArrayList<>();//原有的线路列表
    private List<LineBean> listLineNew = new ArrayList<>();//线路列表编辑容器

    /**
     * 线路dao
     */
    private LineDao lineDao;
    private List<LineBean> listLine;//所有线路id

    //    private List<EquipmentBean> listNoEquipment;//未关联设备列表

    private String addIdStr = "";//添加的店铺id,用,好分割
    private String delIdsStr = "";//删除的线路id

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_customer_line;
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
        mTitleBar.setTitle("店铺线路信息");

        customer = (AddressListBean) getIntent().getSerializableExtra("customer");

        adapter = new LineListAdapter(new ItemOnListener() {
            @Override
            public void onItem(final int position) {
                DialogUtil.showCustomDialog(EditCustomerLineActivity.this, "提示", "确定删除这条线路?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        listLineNew.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void no() {

                    }
                });
            }
        });
        mLvLine.setAdapter(adapter);

        mTvShopName.setText("店铺名称:" + customer.getCc_name());
        mTvContactName.setText("联系人:" + customer.getCc_contacts_name());
        mTvMobile.setText("联系电话:" + customer.getCc_contacts_mobile());

        lineDao = new LineDao(this);
        listLine = lineDao.query();

        initPopupLine();
        getCustomerLines();
    }

    /**
     * 获取店铺的线路列表
     */
    private void getCustomerLines() {
        AsyncHttpUtil<LineBean> httpUtil = new AsyncHttpUtil<>(this, LineBean.class, new IUpdateUI<LineBean>() {
            @Override
            public void updata(LineBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    listLineOld.clear();
                    listLineNew.clear();
                    listLineOld.addAll(jsonBean.getLineList());//关联的线路列表
                    listLineNew.addAll(jsonBean.getLineList());//关联的线路列表

                    adapter.setData(listLineNew);
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
        httpUtil.post(M_Url.getCustomerLines, L_RequestParams.getCustomerLines(customer.getCc_id()), true);
    }

    //更改客户线路接口
    private void updateCustomerLines() {
        AsyncHttpUtil<LineBean> httpUtil = new AsyncHttpUtil<>(this, LineBean.class, new IUpdateUI<LineBean>() {
            @Override
            public void updata(LineBean jsonBean) {
                if ("00".equals(jsonBean.getRepCode())) {
                    showToast(jsonBean.getRepMsg());
                    finishActivity();
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
        httpUtil.post(M_Url.updateCustomerLines, L_RequestParams.updateCustomerLines(customer.getCc_id(),addIdStr), true);
    }

    @OnClick({R.id.ll_add_line, R.id.btn_binding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_line:
                if (listLine==null||listLine.size()==0){
                    getUserLineOrSettingInfo();
                } else {
                    showPopupLine();
                }
                break;
            case R.id.btn_binding:
                addIdStr = "";
//                delIdsStr = "";
                //添加增加的设备id
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listLineNew.size(); i++) {
                    sb.append(listLineNew.get(i).getBl_id());
                    if (i != listLineNew.size() - 1) {
                        sb.append(",");
                    }
                }
                addIdStr = sb.toString();

                //添加删除的设备id
                /*StringBuilder sbDel = new StringBuilder();
                for (int i = 0; i < listLineOld.size(); i++) {//添加删除的设备
                    boolean isHas = false;//是否有这个,默认没有
                    for (int k = 0; k < listLineNew.size(); k++) {
                        if (listLineOld.get(i).getBl_id().equals(listLineNew.get(k).getBl_id())) {
                            isHas = true;
                        }
                    }
                    if (!isHas) {
                        sbDel.append(listLineOld.get(i).getBl_id());
                        sbDel.append(",");
                    }
                }
                if (sbDel.length() > 0) {
                    sbDel.deleteCharAt(sbDel.length() - 1);
                }
                delIdsStr = sbDel.toString();*/

                DialogUtil.showCustomDialog(this, "提示", "是否提交?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        updateCustomerLines();
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    /**
     * 获取用户配置信息
     */
    private void getUserLineOrSettingInfo() {
        AsyncHttpUtil<UserBean> httpUtil = new AsyncHttpUtil<>(this, UserBean.class, new IUpdateUI<UserBean>() {
            @Override
            public void updata(UserBean bean) {
                if ("00".equals(bean.getRepCode())) {
                    if (bean.getLineList()==null || bean.getLineList().size()==0){
                        showToast("没有线路信息");
                    }else{
                        lineDao.batchInsert(bean.getLineList());//添加线路信息
                        listLine = bean.getLineList();
                        popupLine.setData(listLine);
                        showPopupLine();
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

            }
        });
        httpUtil.post(M_Url.getUserLineOrSettingInfo, L_RequestParams.getUserLineOrSettingInfo(), true);
    }

    private void initPopupLine() {
            popupLine = new LineSelectPopup(this, getResources().getDisplayMetrics().heightPixels, new ItemOnListener() {
            @Override
            public void onItem(int position) {
                LineBean bean = popupLine.listLine.get(position);
                for (LineBean bean1 : listLineNew) {
                    if (bean1.getBl_id().equals(bean.getBl_id())) {
                        showToast("该条线路已经在线路列表中存在!");
                        return;
                    }
                }

                listLineNew.add(popupLine.listLine.get(position));
                adapter.setData(listLineNew);
                popupLine.dismiss();
            }
        });
            popupLine.setData(listLine);
    }

    private void showPopupLine() {
        popupLine.showPopup();
        popupLine.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lineDao.destroy();
    }

}
