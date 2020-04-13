package com.xdjd.storebox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.main.MainActivity;
import com.xdjd.utils.LogUtils;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * H5显示界面
 * Created by lijipei on 2016/12/19.
 */

public class CommonWebActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.common_webview)
    WebView mCommonWebview;
    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar1;

    private String url;
    private String title;
    private int type;
    //跳转积分商城INTEGRAL_MALL
    private String INTEGRAL_MALL = "INTEGRAL_MALL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        //Log.e("type",String.valueOf(type));
        initView();
    }

    private void go_main() {
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (type != 0)
            go_main();
    }

    private void initView() {
        title = getIntent().getStringExtra("title");

        mTitleBar.setTitle(title == null ? "" : title);
        mTitleBar.leftBack(this);

        if ("签到".equals(title)) {
            mTitleBar.setRightText("积分商城");
            mTitleBar.setRightTextColor(R.color.text_black_212121);
            mTitleBar.setRightTextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(IntegralStoreActivity.class);
                }
            });
        }

        if (type != 0) {
            mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    go_main();
                }
            });
        }
        url = getIntent().getStringExtra("url");
        LogUtils.e("url", url);

        mCommonWebview.getSettings().setJavaScriptEnabled(true);
        // 设置图片适合webView大小
        mCommonWebview.getSettings().setUseWideViewPort(false);
        //缓存模式
        mCommonWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mCommonWebview.loadUrl(url);
        mCommonWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String[] str = url.split("/");
                if (str[str.length - 1].indexOf("INTEGRAL_MALL") != -1) {//包含
                    startActivity(IntegralStoreActivity.class);
                } else {
                    mCommonWebview.loadUrl(url);
                }
                //LogUtils.e("TAG",url);

                return true;
            }
        });

        mCommonWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar1.setVisibility(View.GONE);
                } else {
                    mProgressBar1.setVisibility(View.VISIBLE);
                    mProgressBar1.setProgress(newProgress);
                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCommonWebview.canGoBack()) {
            mCommonWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
