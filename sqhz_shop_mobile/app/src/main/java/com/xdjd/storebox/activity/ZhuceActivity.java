package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.bean.RegisterInfoBean;
import com.xdjd.storebox.bean.ShopArea;
import com.xdjd.utils.Validation;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.operation.Register_Params;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.runtimepermissions.PermissionUtils;
import com.xdjd.view.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/18.
 */

public class ZhuceActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.new_register_Tel)
    EditText newRegisterTel;
    @BindView(R.id.new_register_name)
    EditText newRegisterName;
    @BindView(R.id.new_register_shopname)
    EditText newRegisterShopname;
    @BindView(R.id.new_register_area)
    TextView newRegisterArea;
    @BindView(R.id.new_register_address)
    EditText newRegisterAddress;
    @BindView(R.id.new_register_resphone)
    EditText newRegisterResphone;
    @BindView(R.id.new_register_invite)
    EditText newRegisterInvite;
    @BindView(R.id.zc_next)
    Button zcNext;
    @BindView(R.id.mobile_del)
    LinearLayout mobileDel;
    @BindView(R.id.shopName_del)
    LinearLayout shopNameDel;
    @BindView(R.id.address_del)
    LinearLayout addressDel;
    @BindView(R.id.name_del)
    LinearLayout nameDel;
    @BindView(R.id.spare_del)
    LinearLayout spareDel;
    @BindView(R.id.invite_del)
    LinearLayout inviteDel;

    private Register_Params bean;

    private List<ShopArea> list = new ArrayList<>();
    private OptionsPickerView pickerView;
    private String str1;
    private String id1;
    private String flag;
    private String userId;
    private int shopId = 0;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_register);
        flag = getIntent().getStringExtra("flag");

        ButterKnife.bind(this);
        titleBar.leftBack(this);
        if (flag.equals("0")) {
            titleBar.setTitle("注册");
        } else {
            titleBar.setTitle("绑定微信");
        }
        zcNext.setSelected(true);
        flag = getIntent().getStringExtra("flag");
        /*手机号是否注册和审核*/
        newRegisterTel.setSelection(newRegisterTel.getText().length());//光标移动到文本行
        if (newRegisterTel.getText().length() > 0) {
            mobileDel.setVisibility(View.VISIBLE);
        } else {
            mobileDel.setVisibility(View.GONE);
        }
        newRegisterTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //判断手机号格式
                if (editable.toString().length() > 0) {
                    mobileDel.setVisibility(View.VISIBLE);
                    if (editable.toString().length() == 11) {
                        if (!Validation.isPhoneNum(editable.toString())) {
                            showToast("手机号格式错误！");
                        } else {
                            Log.e("手机号验证", editable.toString());
                            CheckRegisterMobile(editable.toString());
                        }
                    }
                    else{
                        newRegisterName.setText("");//姓名
                        newRegisterShopname.setText("");//超市名称
                        newRegisterArea.setText("");//所在地区
                        newRegisterAddress.setText("");//详细地址
                        newRegisterResphone.setText("");//备用电话
                        newRegisterInvite.setText("");
                    }
                } else {
                    mobileDel.setVisibility(View.GONE);
                    newRegisterName.setText("");//姓名
                    newRegisterShopname.setText("");//超市名称
                    newRegisterArea.setText("");//所在地区
                    newRegisterAddress.setText("");//详细地址
                    newRegisterResphone.setText("");//备用电话
                    newRegisterInvite.setText("");
                }
            }
        });
        newRegisterTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && newRegisterTel
                        .getText().length() > 0) {//获得焦点
                    mobileDel.setVisibility(View.VISIBLE);
                } else {//失去焦点
                    mobileDel.setVisibility(View.GONE);
                }
            }

        });
        editListener(newRegisterName,nameDel);
        editListener(newRegisterShopname, shopNameDel);
        editListener(newRegisterAddress, addressDel);
        editListener(newRegisterResphone,spareDel);
        editListener(newRegisterInvite,inviteDel);

        /*地区选择*/
        pickerView = new OptionsPickerView(ZhuceActivity.this);
        pickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                str1 = list.get(options1).getMs_title();
                id1 = list.get(options1).getMs_id();
                newRegisterArea.setText(str1);
                Log.e("id1", id1);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        str1 = intent.getStringExtra("name");
        id1 = intent.getStringExtra("id");
        newRegisterArea.setText(str1);
    }

    /*文本框获取焦点和文本内容监听*/
    private void editListener(final EditText editText, final LinearLayout linearLayout) {
        if (editText.getText().length() > 0) {
            linearLayout.setVisibility(View.INVISIBLE);
            editText.setSelection(editText.getText().length());
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    Log.e("change","c");
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && editText.getText().length() > 0) {//获得焦点
                    linearLayout.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    Log.e("Focus","F");
                } else {//失去焦点
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.zc_next, R.id.new_register_area, R.id.mobile_del,R.id.name_del,R.id.shopName_del,R.id.address_del,
    R.id.spare_del,R.id.invite_del})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zc_next://
                Log.e("fag", "下一步");//下一步，跳转页面同时请求验证码接口
                if (checkInputContent() != 1) {
                    Intent intent = new Intent(ZhuceActivity.this, ZhuceActivity_next.class);
                    intent.putExtra("bean", bean);
                    startActivity(intent);
                }
                break;
            case R.id.new_register_area://选择中心仓
//                select_shopArea();

                PermissionUtils.requstActivityLocation(ZhuceActivity.this, 1000 , new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        startActivity(CenterShopListActivity.class);
                    }

                    @Override
                    public void onDilogCancal() {

                    }
                });
//                startActivity(CenterShopListActivity.class);
                break;
            case R.id.mobile_del:
                newRegisterTel.setText("");
                break;
            case R.id.name_del:
                newRegisterName.setText("");break;
            case R.id.shopName_del:
                newRegisterShopname.setText("");break;
            case R.id.address_del:
                newRegisterAddress.setText("");break;
            case  R.id.spare_del:newRegisterResphone.setText("");break;
            case R.id.invite_del:newRegisterInvite.setText("");break;
            default:
                break;
        }
    }

    public int getRegisterParams() {
        bean = new Register_Params();//new 对象
        bean.setUserTel(newRegisterTel.getText().toString());
        bean.setUserName(newRegisterName.getText().toString());
        bean.setShopName(newRegisterShopname.getText().toString());
        //bean.setShopArea(newRegisterArea.getText().toString());
        bean.setShopArea(id1);//地区
        bean.setShopAddress(newRegisterAddress.getText().toString());
        bean.setExtension(newRegisterInvite.getText().toString());
        bean.setSecondPhone(newRegisterResphone.getText().toString());

        if (flag.equals("0")) {
            bean.setUnionid(" ");
        } else {
            bean.setUnionid(getIntent().getStringExtra("unionid"));//微信
        }
        if (userId.equals("0"))
            bean.setUserId(" ");
        else
            bean.setUserId(userId);
        if (bean.getUserName().isEmpty() || bean.getShopName().isEmpty() || bean.getShopArea().isEmpty()
                || (bean.getShopAddress().length() < 5)) {
            showToast("输入信息有空或详细地址少于5个字符！");
           /* DialogUtil.showCustomDialog(this, "输入信息为空或详细地址少于5个字符!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return 1;
        }
        return 0;
    }

    /*判断手机号和输入内容是否有空*/
    private int checkInputContent() {
        if (!Validation.isPhoneNum(newRegisterTel.getText().toString())) {
            showToast("手机号格式错误！");
            /*DialogUtil.showCustomDialog(this, "手机号格式错误!", "确定", null, new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                }
                @Override
                public void no() {
                }
            });*/
            return 1;
        }
        if (getRegisterParams() == 1) {
            return 1;
        }
        return 0;
    }

    /*请求可选择地区*/
    private void select_shopArea() {
        AsyncHttpUtil<ShopArea> httpUtil = new AsyncHttpUtil<>(this, ShopArea.class, new IUpdateUI<ShopArea>() {
            @Override
            public void updata(ShopArea shopArea) {
                if (shopArea.getRepCode().equals("00")) {
                    Log.e("area", shopArea.getRepMsg());
                    list.clear();
                    list = shopArea.getListData();
                    ArrayList<String> listStr = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        listStr.add(list.get(i).getMs_title());
                    }
                    pickerView.setPicker(listStr);
                    pickerView.setCyclic(false);//是否循环
                    pickerView.show();
                }
            }

            public void sendFail(ExceptionType s) {

            }

            public void finish() {
            }
        });
        httpUtil.post(M_Url.ShopArea, L_RequestParams.getRegisterArea(), true);
    }

    /*注册手机号校验*/
    private void CheckRegisterMobile(String mobile) {
        AsyncHttpUtil<RegisterInfoBean> httpUtil = new AsyncHttpUtil<>(this, RegisterInfoBean.class, new IUpdateUI<RegisterInfoBean>() {
            @Override
            public void updata(RegisterInfoBean bean) {
                if (bean.getRepCode().equals("00")) {//用户存在
                    //需判断手机号是否改变，回传的手机号和文本的手机号是否一致
                    userId = bean.getUserId();
                    if (bean.getCheckStatus() == 2) {
                        showToast("此手机号已注册！");
                        newRegisterTel.setText("");
                        newRegisterName.setText("");//姓名
                        newRegisterShopname.setText("");//超市名称
                        newRegisterArea.setText("");//所在地区
                        newRegisterAddress.setText("");//详细地址
                        newRegisterResphone.setText("");//备用电话
                        newRegisterInvite.setText("");
                    } else {
                        newRegisterName.setText(bean.getNickName());//姓名
                        newRegisterShopname.setText(bean.getShopName());//超市名称
                        newRegisterArea.setText(bean.getCenterShopName());//所在地区
                        id1 = String.valueOf(bean.getShopId());
                        newRegisterAddress.setText(bean.getAddress());//详细地址
                        newRegisterResphone.setText(bean.getSpareTel());//备用电话
                        newRegisterInvite.setText("");
                        mobileDel.setVisibility(View.GONE);
                        nameDel.setVisibility(View.GONE);
                        shopNameDel.setVisibility(View.GONE);
                        addressDel.setVisibility(View.GONE);
                        spareDel.setVisibility(View.GONE);
                        inviteDel.setVisibility(View.GONE);
                    }
                } else {
                    newRegisterName.setText("");//姓名
                    newRegisterShopname.setText("");//超市名称
                    newRegisterArea.setText("");//所在地区
                    newRegisterAddress.setText("");//详细地址
                    newRegisterResphone.setText("");//备用电话
                    userId = "0";
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
        httpUtil.post(M_Url.CheckRegisterMobile, L_RequestParams.CheckRegisterMobile(mobile), true);
    }

}




