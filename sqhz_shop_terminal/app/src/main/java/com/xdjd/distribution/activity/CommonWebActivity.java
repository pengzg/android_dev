package com.xdjd.distribution.activity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;

/**
 * 公共webview界面
 * Created by lijipei on 2016/8/29.
 */
public class CommonWebActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.common_webview)
    WebView mCommonWebview;
    private String url, content;
    private boolean isOpenWx;//是否开通微信

    @Override
    protected int getContentView() {
        return R.layout.activity_common_web;
    }

    @Override
    protected void initData() {
        super.initData();
        titleBar.setTitle(getIntent().getStringExtra("title") == null ? "" : getIntent().getStringExtra("title"));
        titleBar.leftBack(this);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(20);
                finishActivity();
            }
        });
        url = getIntent().getStringExtra("url");
        content = getIntent().getStringExtra("content");
        isOpenWx = getIntent().getBooleanExtra("isOpenWx",false);

        WebSettings webSettings = mCommonWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放



        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }


        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        if (url == null && content != null) {
            mCommonWebview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        } else {
            mCommonWebview.loadUrl(url);
            mCommonWebview.setWebViewClient(new WebViewClient());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCommonWebview.canGoBack()) {
            mCommonWebview.goBack();// 返回前一个页面
            return true;
        }
        setResult(20);
        return super.onKeyDown(keyCode, event);
    }
}
