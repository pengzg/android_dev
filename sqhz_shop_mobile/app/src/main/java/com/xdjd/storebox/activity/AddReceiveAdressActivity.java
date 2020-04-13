package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
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

/**
 * Created by Administrator on 2016/12/1.
 */

public class AddReceiveAdressActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.add_enter)
    Button addEnter;
    @BindView(R.id.receive_name)
    EditText receiveName;
    @BindView(R.id.receive_address)
    EditText receiveAddress;
    @BindView(R.id.receive_phone)
    EditText receivePhone;
    @BindView(R.id.mobile_del)
    LinearLayout mobileDel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_receive_address);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("新增收货地址");
        receiveName.setText(getIntent().getStringExtra("nickName"));
        receivePhone.setText(getIntent().getStringExtra("mobile"));
        receiveName.setSelection(receiveName.getText().length());
        receivePhone.setSelection(receivePhone.getText().length());
        if (receivePhone.getText().length() > 0) {
            mobileDel.setVisibility(View.VISIBLE);
        } else {
            mobileDel.setVisibility(View.GONE);
        }
        receivePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mobileDel.setVisibility(View.VISIBLE);
                } else {
                    mobileDel.setVisibility(View.GONE);
                }
            }
        });
        receivePhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && receivePhone.getText().length() > 0) {//获得焦点
                    mobileDel.setVisibility(View.VISIBLE);
                    //editText.setSelection(editText.getText().length());
                    Log.e("Focus","F");
                } else {//失去焦点
                   mobileDel.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.add_enter,R.id.mobile_del})
    public void onClick(View view) {
        switch (view.getId()) {
        /*新增收货地址*/
            case R.id.add_enter:
                AddReceiveAddress(UserInfoUtils.getId(this), receiveName.getText().toString(),
                        receivePhone.getText().toString(), receiveAddress.getText().toString());
                break;
            case R.id.mobile_del:receivePhone.setText("");break;
            default:
                break;
        }

    }

    /*新增收货地址接口请求*/
    private void AddReceiveAddress(String uid, String name, String mobile, String address) {
        /*收货姓名*/
        if (receiveName.getText().toString().equals("")) {
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
        if (receiveAddress.getText().toString().equals("") || receiveAddress.getText().toString().length() < 5) {
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
                    //Log.e("tag", obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        showToast(obj.getString("repMsg"));
                        setResult(100);
                        finishActivity();
                        //Intent intent = new Intent(AddReceiveAdressActivity.this, Address_main.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        //Log.e("tag", obj.getString("repMsg"));
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
        httpUtil.post(M_Url.AddAndModifyAddress, L_RequestParams.UserAddressAddModify(uid,
                name, mobile, address, "", "2"), true);

    }

}
