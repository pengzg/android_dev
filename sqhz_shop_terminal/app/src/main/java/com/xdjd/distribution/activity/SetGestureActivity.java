package com.xdjd.distribution.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.main.MainActivity;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.StatusChange;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.view.CircleImageView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wuhui on 2016/12/15.
 */

public class SetGestureActivity extends BaseActivity {
    public static final int launcherAct = 0;
    public static final int settingAct = 1;
    public static int activityNum;
    public static StatusChange statusChange = new StatusChange();
    @BindView(R.id.forgot_lock)
    LinearLayout forgotLock;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.user_account)
    TextView tv_userAccount;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String userAccount;

    @Override
    protected int getContentView() {
        return R.layout.activity_set_gesture;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ButterKnife.bind(this);
        //CollectorActivity.addActivity(this);
        Intent intent = getIntent();
        activityNum = intent.getIntExtra("activityNum", 0);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        final TextView gestureText = (TextView) findViewById(R.id.gesture_text);
        final TextView prompt = (TextView) findViewById(R.id.prompt);
        final CircleImageView userHeader = (CircleImageView) findViewById(R.id.user_header);

        switch (activityNum) {
            case settingAct://1设置页面
                forgotLock.setVisibility(View.GONE);
                statusChange.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent event) {
                        if ((int) event.getNewValue() == 2) {
                            //第二次设置成功
                            UserInfoUtils.setLock(SetGestureActivity.this, "1");//设置成功
                            Intent intent1 = new Intent(SetGestureActivity.this, GestureLockActivity.class);
                           intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           startActivity(intent1);
                        } else if ((int) event.getNewValue() == 4) {
                            UserInfoUtils.setLock(SetGestureActivity.this, "0");//设置不成功
                            tv_userAccount.setVisibility(View.GONE);
                            gestureText.setText("再次绘制以确认");
                            userHeader.setVisibility(View.GONE);
                            prompt.setVisibility(View.VISIBLE);
                            prompt.setText("请与上次绘制保持一致");
                        }
                    }
                });

                break;
            case launcherAct://0解锁页面
                if (!TextUtils.isEmpty(preferences.getString("gesture", ""))) {
                    gestureText.setVisibility(View.GONE);
                    userHeader.setVisibility(View.VISIBLE);
                    userHeader.setImageResource(R.drawable.header);
                    prompt.setVisibility(View.VISIBLE);
                    prompt.setText("请绘制手势密码解锁");
                    prompt.setTextColor(UIUtils.getColor(R.color.text_black_212121));
                    tv_userAccount.setVisibility(View.VISIBLE);
                    //tv_userAccount.setText(UserInfoUtils.getMobile(this));//手机账号
                    forgotLock.setVisibility(View.VISIBLE);
                    statusChange.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent event) {
                            if ((int) event.getNewValue() == 2) {
                                //showToast("密码正确");
                                startActivity(MainActivity.class);
                                finish();
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (activityNum == launcherAct) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //CollectorActivity.removeActivity(this);
    }

    @OnClick(R.id.forgot_lock)
    public void onClick() {
        DialogUtil.showCustomDialog(this, "您确定要重新登录吗？重新登录后密码锁失效", "确定", "取消",
                new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        startActivity(LoginActivity.class);
                        editor.remove("gesture");//修改时都要清空缓存
                        editor.commit();
                        UserInfoUtils.setLock(SetGestureActivity.this, "0");
                        UserInfoUtils.setId(SetGestureActivity.this, "0");
                        finish();
                    }

                    @Override
                    public void no() {
                    }
                });
    }
}
