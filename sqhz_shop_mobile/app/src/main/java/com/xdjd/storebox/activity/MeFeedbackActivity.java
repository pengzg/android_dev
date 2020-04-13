package com.xdjd.storebox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.UserInfoUtils;
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
 * Created by Administrator on 2016/12/14.
 */

public class MeFeedbackActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.feedback_context)
    LinearLayout feedbackContext;
    @BindView(R.id.edit_phone)
    EditText  editPhone;
    @BindView(R.id.phone_context)
    LinearLayout phoneContext;
    @BindView(R.id.feed_btn)
    Button feedBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_feedback);
        ButterKnife.bind(this);
        titleBar.setTitle("意见反馈");
        titleBar.leftBack(this);
        titleBar.setRightText("我的反馈");
        titleBar.setRightTextColor(R.color.text_df1122 );
        titleBar.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FeedbackReplyActivity.class);
            }
        });
        editPhone.setText(getIntent().getStringExtra("shopMobile"));
        editPhone.setSelection(editPhone.length());
    }

    @OnClick({R.id.feed_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feed_btn:
                CommitFeedback(UserInfoUtils.getId(this), editPhone.getText().toString(),editText.getText().toString());
                break;
        }
    }
    /*提交反馈信息*/
    private void CommitFeedback(String uid,String mobile,String content){
        if(editText.getText().toString().isEmpty()){
            showToast("说点什么吧");
            return;
        }
        if(editPhone.getText().toString().isEmpty()){
            showToast("请您填写手机号码！");
            return;
        }
        AsyncHttpUtil<String> httpUtil = new AsyncHttpUtil<>(this, String.class, new IUpdateUI<String>() {
            @Override
            public void updata(String s) {
                JSONObject obj;
                try{
                    obj = new JSONObject(s);
                    showToast(obj.getString("repMsg"));
                    if(obj.getString("repCode").equals("00")){
                        Intent intent = new Intent(MeFeedbackActivity.this,MainActivity.class);
                        intent.putExtra("currentTab",4);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP );
                        startActivity(intent);
                    }
                }catch(JSONException e){
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
        httpUtil.post(M_Url.Feedback, L_RequestParams.CommitFeedback(uid,mobile,content),true);
    }
}
