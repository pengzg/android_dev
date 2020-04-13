package com.xdjd.distribution.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.xdjd.distribution.R;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.ZXingUtil;
import com.xdjd.view.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ShareDownloadActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.iv_code)
    ImageView mIvCode;
    @BindView(R.id.btn_share)
    Button mBtnShare;

    private UserBean mUserBean;
    private String downloadUrl;

    private String shareType;//分享type 1.分享盒子助手;2.分享盒子订货

    @Override
    protected int getContentView() {
        return R.layout.activity_share_download;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        shareType = getIntent().getStringExtra("shareType");
        mUserBean = UserInfoUtils.getUser(this);

        mTitleBar.leftBack(this);
        if ("1".equals(shareType)){
            mTitleBar.setTitle("盒子助手app下载");
            mBtnShare.setText("分享盒子助手app");
            downloadUrl = UserInfoUtils.getDomainName(this) + "/appdown/appdown.html";
        }else{
            mTitleBar.setTitle("订货app下载");
            mBtnShare.setText("分享订货app");
            downloadUrl = UserInfoUtils.getDomainName(ShareDownloadActivity.this)+mUserBean.getHzdh_download_url();
        }

        Bitmap bitmap = null;
        try {
            bitmap = ZXingUtil.createQRCode(downloadUrl, 720);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mIvCode.setImageBitmap(bitmap);
    }

    @OnClick({R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                Intent intent;
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if ("1".equals(shareType)){//盒子助手分享
                    intent.putExtra(Intent.EXTRA_SUBJECT, "盒子分销平台");
                    intent.putExtra(Intent.EXTRA_TEXT, "推荐分享：【盒子助手】，一款专注外勤人员智能办公，提高工作效率的APP；业务人员手机移动端下单，" +
                            "自动化流程实时流转，经销商高效配送，提升终端订单效率，提升客户满意度；电商时代，外勤智能好助手！~下载地址:"
                            + downloadUrl);//UserInfoUtils.getDomainName(this) + "/appdown/appdown.html");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "share"));
                }else{
                    intent.putExtra(Intent.EXTRA_SUBJECT, "盒子订货");
                    intent.putExtra(Intent.EXTRA_TEXT, "推荐分享：【盒子订货】，电商时代订货新模式"+
                            "，一个面向终端零售门店的手机订货App，" +
                            "上手快,页面操作简单，帮助终端店老板提高订货效率。轻松订货，让生意更好！免费下载使用。" +
                            "~下载地址:"+downloadUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "share"));
                }
                break;
        }
    }
}
