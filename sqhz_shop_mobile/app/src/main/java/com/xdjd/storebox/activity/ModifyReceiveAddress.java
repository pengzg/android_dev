package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.ReceiveAddressBean;
import com.xdjd.storebox.event.AddressDeleteEvent;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.view.EaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ModifyReceiveAddress extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_address)
    EditText editAddress;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.modify_enter)
    Button modifyEnter;
    @BindView(R.id.modify_default)
    ImageView modifyDefault;
    @BindView(R.id.modify)
    LinearLayout modify;


    private ReceiveAddressBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_receive_address);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("修改收货地址");

        titleBar.setRightText("删除");
        titleBar.setRightTextColor(R.color.text_df1122);
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete_address();
            }
        });



        bean = (ReceiveAddressBean) getIntent().getSerializableExtra("addressbean");
        editName.setText(bean.getUsa_receiver_name());
        editName.setSelection(editName.getText().length());
        editPhone.setText(bean.getUsa_mobile());
        editAddress.setText(bean.getUsa_address());
        if(bean.getUsa_is_default() == 1){
            modifyDefault.setSelected(true);
        }else{
            modifyDefault.setSelected(false);
        }
    }
    private void Delete_address(){
        DialogUtil.showCustomDialog(this, "确定删除?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
            @Override
            public void ok() {
                DeleteUserReceiveAddress(UserInfoUtils.getId(ModifyReceiveAddress.this),bean.getUsa_id());
            }

            @Override
            public void no() {

            }
        });
    }

    @OnClick({R.id.modify_enter,R.id.modify})
    public void onClick(View view ) {
        switch (view.getId()) {
        /*修改收货地址*/
            case R.id.modify_enter:
                if(bean.getUsa_is_default() == 1)
                    ModifyReceiveAddress(UserInfoUtils.getId(this), editName.getText().toString(),
                    editPhone.getText().toString(), editAddress.getText().toString(), bean.getUsa_id().toString(),Integer.toString(1));
                else{
                    ModifyReceiveAddress(UserInfoUtils.getId(this), editName.getText().toString(),
                            editPhone.getText().toString(), editAddress.getText().toString(), bean.getUsa_id().toString(),Integer.toString(2));
                }
                break;
            case R.id.modify://设为默认地址选项

                if(bean.getUsa_is_default() == 1){
                    modifyDefault.setSelected(false);
                    bean.setUsa_is_default(2) ;
                }else{
                    modifyDefault.setSelected(true);
                    bean.setUsa_is_default(1) ;
                }
                break;
            default :break;
        }

    }

    /*修改收货地址*/
    private void ModifyReceiveAddress(String uid, String name, String mobile,
                                      String address, String usa_id,String isDefault) {
        /*收货姓名*/
        if (editName.getText().toString().equals("")) {
            showToast("姓名为空！");
            return;
        }

        if (!Validation.isPhoneNum(mobile)) {
            showToast("手机号格式错误！");
            /*DialogUtil.showCustomDialog(this, "手机号格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return;
        }
        /*收货地址判断*/
        if (editAddress.getText().toString().equals("") || editAddress.getText().toString().length() < 5) {
            showToast("地址为空或少于5个字符！");
            return;
        }
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                //Log.e("tag",s);
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        finishAction();
//                        Intent intent = new Intent(ModifyReceiveAddress.this, Address_main.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
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
        httpUtil.post(M_Url.AddAndModifyAddress, L_RequestParams.UserAddressAddModify(uid, name, mobile, address, usa_id,isDefault), true);
    }


    /*删除用户地址*/
    private void DeleteUserReceiveAddress(String uid, final String usa_id){

        AsyncHttpUtil<String>httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try{
                    obj = new JSONObject(s);
                    if(obj.getString("repCode").equals("00")){
                        EventBus.getDefault().post(new AddressDeleteEvent(usa_id));

                        finishActivity();
                    }else{
                        showToast(obj.getString("repMsg"));
                    }
                }catch(JSONException  e){
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
        httpUtil.post(M_Url.DeleteUserReceiveAddress ,L_RequestParams.DeleteUserAddress(uid,usa_id) ,true);
    }

    private void finishAction(){
        setResult(100);
        finish();
    }

}
