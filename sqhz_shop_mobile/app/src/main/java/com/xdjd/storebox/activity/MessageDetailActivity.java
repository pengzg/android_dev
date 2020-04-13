package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 系统消息详情界面
 * Created by lijipei on 2017/2/23.
 */

public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.content_tv)
    TextView mContentTv;

    private String title;
    private String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        mTitleBar.leftBack(this);
        mTitleBar.setTitle(title);

        mContentTv.setText(content);
    }
}

