package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.AddressInfoAdapter;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ReceiveAddressBean;
import com.xdjd.storebox.event.AddressDeleteEvent;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.recycleview.VaryViewHelper;
import com.xdjd.view.EaseTitleBar;
import com.xdjd.view.NoScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2016/12/1.
 */

public class Address_main extends BaseActivity implements AddressInfoAdapter.AddressListener {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    @BindView(R.id.add_address)
    Button addAddress;
    @BindView(R.id.main_scroll)
    ScrollView mMainScroll;
    //List<ReceiveAddressBean> list = new ArrayList<ReceiveAddressBean>();
    private AddressInfoAdapter adapter = new AddressInfoAdapter(this);//关联回调
    @BindView(R.id.mainaddress_list)
    NoScrollListView mainaddressList;
    private int flag = 1;
    private VaryViewHelper mVaryViewHelper = null;
    private List<ReceiveAddressBean> list_address = new ArrayList<>();
    private ReceiveAddressBean addressBean = new ReceiveAddressBean();

    private String addressId;
    private boolean isConfirm;//是否是确认订单跳转过来的
    private String nickName;
    private String mobile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_address_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        titleBar.leftBack(this);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                boolean isYes = false;
                if (null == list_address || list_address.size() == 0) {
                    intent.putExtra("addressId", "0");
                    intent.putExtra("isYes", false);
                } else {
                    if (null != list_address && list_address.size() != 0) {
                        for (int k = 0; k < list_address.size(); k++) {
                            if (list_address.get(k).getUsa_id().equals(addressId)) {
                                isYes = true;
                                intent.putExtra("addressId", list_address.get(k).getUsa_id());
                                intent.putExtra("isYes", isYes);
                            }
                        }
                    }
                    if (!isYes) {
                        intent.putExtra("addressId", "0");
                        intent.putExtra("isYes", false);
                    }
                }
                setResult(1000, intent);
                finish();
            }
        });
        titleBar.setTitle("收货地址");
        titleBar.setRightText("编辑");
        titleBar.setRightTextColor(R.color.text_df1122);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //titleBar.setRightText("取消");//同时在地址信息上弹X删除，点击进去直接修改收货地址界面
                if (adapter.flag == 1) {
                    titleBar.setRightText("取消");
                    adapter.flag = 2;
                } else {
                    titleBar.setRightText("编辑");
                    adapter.flag = 1;
                }
                adapter.notifyDataSetChanged();
            }
        });
        titleBar.setRightTextViewVisibility(View.GONE);

        addressId = getIntent().getStringExtra("addressId");
        isConfirm = getIntent().getBooleanExtra("isConfirm", false);

        nickName = getIntent().getStringExtra("nickName");
        mobile = getIntent().getStringExtra("mobile");
        adapter.flag = 1;
        mVaryViewHelper = new VaryViewHelper(mMainScroll);
        mainaddressList.setAdapter(adapter);
        GetUserReceiveAddressList(UserInfoUtils.getId(this).toString());
    }


    @OnClick(R.id.add_address)
    public void onClick() {
        Intent intent = new Intent(this, AddReceiveAdressActivity.class);
        intent.putExtra("nickName",nickName);
        intent.putExtra("mobile",mobile);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void item(int i) {
        if (isConfirm) {
            Intent intent = new Intent();
            intent.putExtra("addressId", list_address.get(i).getUsa_id());
            intent.putExtra("isYes", true);
            setResult(1000, intent);
            finish();
        }/*else{
            Intent intent = new Intent(Address_main.this,ModifyReceiveAddress.class);
            addressBean.setUsa_id(list_address.get(i).getUsa_id());
            addressBean.setUsa_receiver_name(list_address.get(i).getUsa_receiver_name());
            addressBean.setUsa_mobile(list_address.get(i).getUsa_mobile());
            addressBean.setUsa_address(list_address.get(i).getUsa_address());
            //addressBean.setUsa_isDefault(list_address.get(i).getUsa_isDefault());
            intent.putExtra("addressbean",addressBean);
            startActivity(intent);
        }*/
    }

    @Override
    public void edit(int i) {
        Intent intent = new Intent(Address_main.this, ModifyReceiveAddress.class);
        addressBean.setUsa_id(list_address.get(i).getUsa_id());
        addressBean.setUsa_receiver_name(list_address.get(i).getUsa_receiver_name());
        addressBean.setUsa_mobile(list_address.get(i).getUsa_mobile());
        addressBean.setUsa_address(list_address.get(i).getUsa_address());
        addressBean.setUsa_is_default(list_address.get(i).getUsa_is_default()); //setUsa_isDefault(list_address.get(i).getUsa_isDefault());
        intent.putExtra("addressbean", addressBean);
        startActivityForResult(intent, 100);
        //DeleteUserReceiveAddress(UserInfoUtils.getId(this),list_address.get(i).getUsa_id().toString(),i);
    }

    /*@Override
    public void setDefault(int i) {
        if(list_address.get(i).getUsa_isDefault() == 1){
            //list_address.get(i).setUsa_isDefault(0);
        }else{
            list_address.get(i).setUsa_isDefault(1);
        }
        adapter.notifyDataSetChanged() ;
        SetDefaultAddress(UserInfoUtils.getId(this),list_address.get(i).getUsa_id().toString(),i);
    }*/

    /*用户地址列表*/
    private void GetUserReceiveAddressList(String uid) {

        AsyncHttpUtil<ReceiveAddressBean> httpUtil = new AsyncHttpUtil<>(this, ReceiveAddressBean.class,
                new IUpdateUI<ReceiveAddressBean>() {
                    @Override
                    public void updata(ReceiveAddressBean addressBean) {
                        if (addressBean.getRepCode().equals("00")) {
                            mVaryViewHelper.showDataView();
                            list_address.clear();
                            list_address.addAll(addressBean.getAddressList());

                            if (list_address == null || list_address.size() == 0) {
                                mVaryViewHelper.showEmptyView();

                                adapter.flag = 1;
                                titleBar.setRightTextViewVisibility(View.GONE);
                            }else{
                                titleBar.setRightTextViewVisibility(View.VISIBLE);
                            }
                            adapter.setData(list_address);
                        } else {
                            showToast(addressBean.getRepMsg());
                        }
                    }

                    @Override
                    public void sendFail(ExceptionType s) {

                    }

                    @Override
                    public void finish() {

                    }
                });
        httpUtil.post(M_Url.UserAddrssList, L_RequestParams.GetUserAddressList(uid), true);
    }

    /*删除用户地址*/
    private void DeleteUserReceiveAddress(String uid, String usa_id, final int i) {
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    if (obj.getString("repCode").equals("00")) {
                        //showToast(obj.getString("repMsg"));
                        list_address.remove(i);
                        adapter.notifyDataSetChanged();
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
        httpUtil.post(M_Url.DeleteUserReceiveAddress, L_RequestParams.DeleteUserAddress(uid, usa_id), true);
    }

    /*设置默认地址*/
    private void SetDefaultAddress(String uid, String usaId, int i) {
        for (int k = 0; k < list_address.size(); k++) {
            list_address.get(k).setUsa_is_default(0);
        }
        list_address.get(i).setUsa_is_default(1);
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        adapter.notifyDataSetChanged();
                        //GetUserReceiveAddressList(UserInfoUtils.getId(Address_main.this).toString());
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
        httpUtil.post(M_Url.SetDefaultAddress, L_RequestParams.SetDefaultAddress(uid, usaId), true);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        boolean isYes = false;
        if (null == list_address || list_address.size() == 0) {
            intent.putExtra("addressId", "0");
            intent.putExtra("isYes", false);
        } else {
            if (null != list_address && list_address.size() != 0) {
                for (int k = 0; k < list_address.size(); k++) {
                    if (list_address.get(k).getUsa_id().equals(addressId)) {
                        isYes = true;
                        intent.putExtra("addressId", list_address.get(k).getUsa_id());
                        intent.putExtra("isYes", isYes);
                    }
                }
            }
            if (!isYes) {
                intent.putExtra("addressId", "0");
                intent.putExtra("isYes", false);
            }
        }
        setResult(1000, intent);
        finish();
    }

    public void onEventMainThread(AddressDeleteEvent event) {
//        for (int i=0;i<list_address.size();i++){
//            if (event.getId().equals(list_address.get(i).getUsa_id())){
//                list_address.remove(i);
//            }
//        }
//        adapter.notifyDataSetChanged();
        GetUserReceiveAddressList(UserInfoUtils.getId(this).toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                GetUserReceiveAddressList(UserInfoUtils.getId(this).toString());
                break;
        }
    }
}
