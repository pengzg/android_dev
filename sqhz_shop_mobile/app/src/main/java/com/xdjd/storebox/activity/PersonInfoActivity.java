package com.xdjd.storebox.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.mob.MobSDK;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;
import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.base.BaseConfig;
import com.xdjd.storebox.bean.PersonInfoBean;
import com.xdjd.storebox.wxapi.LoginApi;
import com.xdjd.storebox.wxapi.OnLoginListener;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.StringUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.tool.Tool;
import com.xdjd.utils.tool.UserInfo;
import com.xdjd.view.CircleImageView;
import com.xdjd.view.CustomProgress;
import com.xdjd.view.EaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by freestyle_hong on 2016/11/30.
 */

public class PersonInfoActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.shop_name)
    LinearLayout shopNameL;
    @BindView(R.id.user_name)
    LinearLayout userName;
    @BindView(R.id.user_brityday)
    LinearLayout userBrithday;
    @BindView(R.id.user_phone_num)
    LinearLayout userPhoneNum;
    @BindView(R.id.user_receive_address)
    LinearLayout userReceiveAddress;
    @BindView(R.id.user_modify_password)
    LinearLayout userModifyPassword;
    @BindView(R.id.exit_account)
    Button exitAccount;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.brityday)
    TextView brithday;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.person_image)
    LinearLayout personImage;
    @BindView(R.id.connect_status)
    TextView connectStatus;
    @BindView(R.id.connect_weixin)
    LinearLayout connectWeixin;

    private TimePickerView pwTime;
    private final int CHOOSE_PHOTO = 3;
    private Platform pfWX;
    private String unionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("个人信息");
        MobSDK.init(getApplicationContext());
        pfWX = new Wechat();
        GetUserInfo(UserInfoUtils.getId(this),UserInfoUtils.getId(this));
        initViewTime();
    }

    private void initViewTime() {
        //选择器
        pwTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pwTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));
        pwTime.setTime(new Date());
        pwTime.setCyclic(false);
        pwTime.setCancelable(true);
        //选择后回调
        pwTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                /*
                * 判断选择的日期是否大于当前的日期
                * */
                if (date.after(new Date())) {
                    showToast("出生日期不能在当前时间之后");
                } else {
                    brithday.setText(StringUtils.getTime(date));//日期格式转换
                    updateData();
                }
            }
        });
    }

    private void updateData() {
        SetBirthday(UserInfoUtils.getId(this), brithday.getText().toString());
    }

    @OnClick({R.id.shop_name, R.id.user_name, R.id.user_phone_num, R.id.user_receive_address, R.id.user_modify_password,
            R.id.exit_account, R.id.user_brityday, R.id.person_image, R.id.connect_weixin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_image:
                //动态申请权限
                /*DialogUtil.showCustomDialog(this, "修改图像", "从相册选择", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        PermissionUtils.requstAcivityStorage(PersonInfoActivity.this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                intent.setType("image*//*");
                                startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
                            }

                            @Override
                            public void onDilogCancal() {}
                        });
                    }

                    @Override
                    public void no() {

                    }
                });*/
                break;
            case R.id.shop_name://修改店铺名称
                Intent intent = new Intent(PersonInfoActivity.this, ChangeShopNameActivity.class);
                intent.putExtra("shopName", shopName.getText().toString());
                startActivity(intent);
                break;
            case R.id.user_name:/*修改昵称*/
                Intent intent1 = new Intent(PersonInfoActivity.this, ChangePersonNameActivity.class);
                intent1.putExtra("nickName", nickName.getText().toString());
                startActivity(intent1);
                break;
            case R.id.user_brityday:
                pwTime.show();
                break;
            case R.id.user_phone_num:/*修改手机号*/
                startActivity(ChangePhoneNumActivity.class);
                break;
            case R.id.user_receive_address:/*收货地址*/
                Intent intent2 = new Intent(PersonInfoActivity.this, Address_main.class);
                intent2.putExtra("nickName", nickName.getText().toString());
                intent2.putExtra("mobile", phone.getText().toString());
                startActivity(intent2);
                break;
            case R.id.user_modify_password:/*修改密码*/
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.exit_account:/*退出登录首页*/
                DialogUtil.showCustomDialog(this, "确定退出登录?", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        //--------友盟推送代码--------
                        PushAgent mPushAgent = PushAgent.getInstance(PersonInfoActivity.this);
                        //退出时解除别名的绑定
                        mPushAgent.removeAlias(UserInfoUtils.getId(PersonInfoActivity.this), BaseConfig.Alias_Type,
                                new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String s) {
                                    }
                                });
                        //删除所有标签
                        mPushAgent.getTagManager().reset(new TagManager.TCallBack() {
                            @Override
                            public void onMessage(boolean b, ITagManager.Result result) {

                            }
                        });

                        AppManager.getInstance().finishAllActivity();

                        UserInfoUtils.setTagList(PersonInfoActivity.this, null);
                        UserInfoUtils.setId(PersonInfoActivity.this, "0");
                        UserInfoUtils.setCenterShopId(PersonInfoActivity.this,"");
                        UserInfoUtils.setLoginName(PersonInfoActivity.this,"0");
                        UserInfoUtils.setLoginPwd(PersonInfoActivity.this,"0");
                        UserInfoUtils.setCompanyId(PersonInfoActivity.this,"0");

                        Intent intent3 = new Intent(PersonInfoActivity.this, LoginActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        finish_info();
                    }

                    @Override
                    public void no() {
                    }
                });


                break;
            case R.id.connect_weixin:
                ConnectWX();
                break;
            default:
                break;
        }
    }

    private void finish_info() {
        finish();
    }

    /*获取用户信息*/
    private void GetUserInfo(String uid,String userId) {
        AsyncHttpUtil<PersonInfoBean> httpUtil = new AsyncHttpUtil<>(this, PersonInfoBean.class, new IUpdateUI<PersonInfoBean>() {
            @Override
            public void updata(PersonInfoBean bean) {
                if (bean.getRepCode().equals("00")) {
                    nickName.setText(bean.getCc_contacts_name());
                    phone.setText(bean.getCc_contacts_mobile());
                    shopName.setText(bean.getCc_name());
                    LogUtils.e("shopname",bean.getCc_name().toString());
                    Glide.with(PersonInfoActivity.this).load(bean.getCc_image()).dontAnimate().error(R.drawable.head_bg).into(image);
                    /*if (!bean.getBirthDay().toString().isEmpty()) {
                        brithday.setText(bean.getBirthDay());
                    }*/
                    if (bean.getUnionId()!=null&&bean.getUnionId().length()>0) {
                        connectStatus.setText("已绑定");
                        unionId = bean.getUnionId();
                    } else {
                        connectStatus.setText("绑定微信");
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
        httpUtil.post(M_Url.GetuserInfo, L_RequestParams.getUserInfo(uid,userId), true);
    }

    /*设置生日*/
    private void SetBirthday(String uid, String birthday) {

        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    //showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        showToast("生日设置成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "异常");
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
        httpUtil.post(M_Url.ModifyuserInfo, L_RequestParams.ModifyUserInfo(uid, "", birthday, " "), true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {//4.4以上系统
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是doccument类型的Uri，则通过doccument id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果不是documents类型的uri，使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bitmap);
            //toService(imagePath);//上传给后台
        } else {
            showToast("fail");
        }
        Log.e("路径：", imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /*上传图片接口*/
    private void toService(String uri) {
        CustomProgress.show(this, "上传中", true, null);
        File file = new File(uri);
        RequestParams params = new RequestParams();
        try {
            params.put("imageFile", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpUtil.postFile(M_Url.PersonImage, uri, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
            }
        });
    }

    /*绑定微信*/
    private void ConnectWX() {
        if (connectStatus.getText().toString().equals("已绑定")) {
            DialogUtil.showCustomDialog(this, "提示", "您是否要解除绑定?", "解除绑定", "取消",
                    new DialogUtil.MyCustomDialogListener2() {
                @Override
                public void ok() {
                    clearWxData();
                }

                @Override
                public void no() {
                }
            });
        }else{
            if (!Tool.canGetUserInfo(pfWX)) {
                showToast("请安装微信客户端");
                return;
            }
            LoginApi api = new LoginApi();
            api.setPlatform(pfWX.getName());
            api.setOnLoginListener(new OnLoginListener() {
                @Override
                public boolean onLogin(String platform, HashMap<String, Object> res) {
                    String key = "unionid";
                    unionId = res.get(key).toString();
                    isPass(UserInfoUtils.getId(PersonInfoActivity.this));
                    return false;
                }

                @Override
                public boolean onRegister(com.xdjd.storebox.wxapi.UserInfo info) {
                    return false;
                }
            });
            api.login(this);
        }
    }

    private void isPass(String uid) {
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        connectStatus.setText("已绑定");
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
        httpUtil.post(M_Url.ConnectWX, L_RequestParams.ConnectWX(uid, unionId), true);
    }

    /**
     * 解除微信绑定
     */
    private void clearWxData(){
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try {
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if (obj.getString("repCode").equals("00")) {
                        unionId="";
                        connectStatus.setText("绑定微信");
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
        httpUtil.post(M_Url.clearWxData, L_RequestParams.clearWxData(UserInfoUtils.getId(PersonInfoActivity.this)), true);
    }
}
