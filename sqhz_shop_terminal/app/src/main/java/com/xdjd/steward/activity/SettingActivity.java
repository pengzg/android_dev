package com.xdjd.steward.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.ShareDownloadActivity;
import com.xdjd.distribution.activity.SystemActivity;
import com.xdjd.distribution.base.BaseActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.NewVersionBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.AppManager;
import com.xdjd.utils.AppUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.utils.update.DownloadUtil;
import com.xdjd.view.EaseTitleBar;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_account)
    TextView mTvAccount;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_tel)
    TextView mTvTel;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;
    @BindView(R.id.ll_bbh)
    LinearLayout mLlBbh;
    @BindView(R.id.ll_xt)
    LinearLayout mLlXt;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    @BindView(R.id.title_bar)
    EaseTitleBar mTitleBar;
    @BindView(R.id.ll_zs_share)
    LinearLayout mLlZsShare;
    @BindView(R.id.ll_dh_share)
    LinearLayout mLlDhShare;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.leftBack(this);
        mTitleBar.setTitle("设置");

        UserBean userBean = UserInfoUtils.getUser(this);

        mTvAccount.setText("账号:" + UserInfoUtils.getAccount(this));
        mTvName.setText("姓名:" + userBean.getBud_name());
        mTvTel.setText("电话:" + userBean.getMobile());

        mTvVersionName.setText("v" + SystemUtil.getVersion(this));
    }

    @OnClick({R.id.ll_bbh, R.id.ll_xt, R.id.btn_exit,R.id.ll_zs_share,R.id.ll_dh_share})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_bbh:
                PermissionUtils.requstAcivityStorage(this, 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        getNewVersion();
                    }

                    @Override
                    public void onDilogCancal() {
                        showToast("获取读取sd权限失败!");
                    }
                });
                break;
            case R.id.ll_xt:
                startActivity(SystemActivity.class);
                break;
            case R.id.btn_exit:
                DialogUtil.showCustomDialog(this, "提示", "是否退出程序", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        UserInfoUtils.setLoginState(SettingActivity.this, "0");
                        finishActivity();
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        am.killBackgroundProcesses(getPackageName());
                        AppManager.getInstance().finishAllActivity();
                        System.exit(0);
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
            case R.id.ll_zs_share://助手分享
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "盒子分销平台");
//                intent.putExtra(Intent.EXTRA_TEXT, "推荐分享：【盒子助手】，一款专注外勤人员智能办公，提高工作效率的APP；业务人员手机移动端下单，" +
//                        "自动化流程实时流转，经销商高效配送，提升终端订单效率，提升客户满意度；电商时代，外勤智能好助手！~下载地址:"
//                        + UserInfoUtils.getDomainName(this) + "/appdown/appdown.html");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, "share"));
                intent = new Intent(this,ShareDownloadActivity.class);
                intent.putExtra("shareType","1");
                startActivity(intent);
                break;
            case R.id.ll_dh_share://订货分享
                intent = new Intent(this,ShareDownloadActivity.class);
                intent.putExtra("shareType","2");
                startActivity(intent);
                break;
        }
    }

    private void getNewVersion() {
        AsyncHttpUtil<NewVersionBean> httpUtil = new AsyncHttpUtil<>(this, NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (bean.getNewVersion() != null && !"".equals(bean.getNewVersion())) {
                        BigDecimal newVersion = new BigDecimal(bean.getNewVersion());
                        BigDecimal oldVersion = new BigDecimal(SystemUtil.getVersionCode(SettingActivity.this));
                        if (newVersion.compareTo(oldVersion) == 0) {
                            showToast("当前已是最新版本");
                        } else {
                            if (bean.getFlag().equals("1")) {
                                updateDialog(bean);
                            } else if (bean.getFlag().equals("2")) {
                                //updateDialog(bean);
                                int type = AppUtil.apkInfo(BaseConfig.installPath, SettingActivity.this);
                                //系统返回的版本号
                                int versionCode = Integer.parseInt(bean.getNewVersion());
                                if (type == AppUtil.Overdue || type < versionCode) {
                                    updateDialog(bean);
                                } else if (type == versionCode) {
                                    DialogUtil.showVersionDialog(SettingActivity.this, "0", bean.getNewVersionName(), bean.getUpdateContent(),
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
        int versionCode = SystemUtil.getVersionCode(this);
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(UserInfoUtils.getId(this), String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        DialogUtil.showVersionDialog(this, bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        DownloadUtil.updataVersion(
                                "社区盒子",
                                SettingActivity.this,
                                bean.getDownloadUrl(), "hezifenxiao",
                                "hezifenxiao.apk", true, bean.getNewVersionName(), bean.getUpdateContent());
                    }

                    @Override
                    public void no() {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
