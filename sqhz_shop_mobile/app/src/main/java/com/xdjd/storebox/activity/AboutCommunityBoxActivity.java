package com.xdjd.storebox.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.base.BaseActivity;
import com.xdjd.storebox.base.BaseConfig;
import com.xdjd.storebox.bean.NewVersionBean;
import com.xdjd.utils.AppUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.QRCodeUtil;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.update.DownloadUtil;
import com.xdjd.view.EaseTitleBar;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by freestyle_hong on 2016/12/20.
 */

public class AboutCommunityBoxActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.Version)
    LinearLayout Version;
    @BindView(R.id.note_version)
    TextView noteVersion;
    @BindView(R.id.qr_code_img)
    ImageView mQrCodeImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_box);
        ButterKnife.bind(this);
        titleBar.leftBack(this);
        titleBar.setTitle("关于社区盒子");
        version.setText(SystemUtil.getVersion(AboutCommunityBoxActivity.this));
        noteVersion.setText("For Android V" + SystemUtil.getVersion(AboutCommunityBoxActivity.this) + " build");

        String url;
        if ("".equals(UserInfoUtils.getDownload_Url(AboutCommunityBoxActivity.this))){
            url = BaseConfig.URL + "/html/b2b/appdown/appdown.html";
        }else{
            url = BaseConfig.URL + UserInfoUtils.getDownload_Url(this);
        }

        LogUtils.e("二维码",url);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap qrBitmap = QRCodeUtil.createImage(url,600,600,bitmap);
        mQrCodeImg.setImageBitmap(qrBitmap);
    }

    @OnClick(R.id.Version)
    public void onClick() {
        getNewVersion();
    }


    private void getNewVersion() {
        AsyncHttpUtil<NewVersionBean> httpUtil = new AsyncHttpUtil<>(this, NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (bean.getNewVersion() != null && !"".equals(bean.getNewVersion())) {
                        BigDecimal newVersion = new BigDecimal(bean.getNewVersion());
                        BigDecimal oldVersion = new BigDecimal(SystemUtil.getVersionCode(AboutCommunityBoxActivity.this));
                        if (newVersion.compareTo(oldVersion) == 0) {
                            showToast("当前已是最新版本");
                        } else {
                            if (bean.getFlag().equals("1")) {
                                updateDialog(bean);
                            } else if (bean.getFlag().equals("2")) {
                                //updateDialog(bean);
                                int type = AppUtil.apkInfo(BaseConfig.installPath,AboutCommunityBoxActivity.this);
                                //系统返回的版本号
                                int versionCode = Integer.parseInt(bean.getNewVersion());
                                if (type == AppUtil.Overdue || type < versionCode){
                                    updateDialog(bean);
                                }else if (type == versionCode){
                                    DialogUtil.showVersionDialog(AboutCommunityBoxActivity.this,"0", bean.getNewVersionName(),bean.getUpdateContent(),
                                            "安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                                @Override
                                                public void ok() {
                                                    File file = new File(BaseConfig.installPath);
                                                    Intent i = new Intent();
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    i.setAction(Intent.ACTION_VIEW);
                                                    i.setDataAndType(Uri.fromFile(file),
                                                            "application/vnd.android.package-archive");
                                                    startActivity(i);
                                                }
                                                @Override
                                                public void no() {

                                                }
                                            });
                                }

                            }
                        }
                    } else {
                        showToast("当前已是最新版本!");
                    }

                }
            }

            @Override
            public void sendFail(ExceptionType s) {

            }

            @Override
            public void finish() {

            }
        });
        int versionCode = SystemUtil.getVersionCode(AboutCommunityBoxActivity.this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(UserInfoUtils.getId(this),
                String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        DownloadUtil.updataVersion(
                                "社区盒子",
                                AboutCommunityBoxActivity.this,
                                bean.getDownloadUrl(), "shequhezi",
                                "shequhezi.apk", true, bean.getNewVersionName(), bean.getUpdateContent());
                    }

                    @Override
                    public void no() {

                    }
                });
    }
}
