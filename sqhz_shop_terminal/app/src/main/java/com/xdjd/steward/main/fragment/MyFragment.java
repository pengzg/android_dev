package com.xdjd.steward.main.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.SystemActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
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
 * Created by lijipei on 2017/6/19.
 */

public class MyFragment extends BaseFragment {

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
    @BindView(R.id.tv_account)
    TextView mTvAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        UserBean userBean = UserInfoUtils.getUser(getActivity());

        mTvAccount.setText("账号:" + UserInfoUtils.getAccount(getActivity()));
        mTvName.setText("姓名:" + userBean.getBud_name());
        mTvTel.setText("电话:" + userBean.getMobile());

        mTvVersionName.setText("v" + SystemUtil.getVersion(getActivity()));
    }

    @OnClick({R.id.ll_bbh, R.id.ll_xt, R.id.btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bbh:
                PermissionUtils.requstAcivityStorage(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
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
                DialogUtil.showCustomDialog(getActivity(), "提示", "是否退出程序", "确定", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        UserInfoUtils.setLoginState(getActivity(), "0");
                        finishActivity();
                        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                        am.killBackgroundProcesses(getActivity().getPackageName());
                        AppManager.getInstance().finishAllActivity();
                        System.exit(0);
                    }

                    @Override
                    public void no() {

                    }
                });
                break;
        }
    }

    private void getNewVersion() {
        AsyncHttpUtil<NewVersionBean> httpUtil = new AsyncHttpUtil<>(getActivity(), NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean bean) {
                if (bean.getRepCode().equals("00")) {
                    if (bean.getNewVersion() != null && !"".equals(bean.getNewVersion())) {
                        BigDecimal newVersion = new BigDecimal(bean.getNewVersion());
                        BigDecimal oldVersion = new BigDecimal(SystemUtil.getVersionCode(getActivity()));
                        if (newVersion.compareTo(oldVersion) == 0) {
                            showToast("当前已是最新版本");
                        } else {
                            if (bean.getFlag().equals("1")) {
                                updateDialog(bean);
                            } else if (bean.getFlag().equals("2")) {
                                //updateDialog(bean);
                                int type = AppUtil.apkInfo(BaseConfig.installPath, getActivity());
                                //系统返回的版本号
                                int versionCode = Integer.parseInt(bean.getNewVersion());
                                if (type == AppUtil.Overdue || type < versionCode) {
                                    updateDialog(bean);
                                } else if (type == versionCode) {
                                    DialogUtil.showVersionDialog(getActivity(), "0", bean.getNewVersionName(), bean.getUpdateContent(),
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
        int versionCode = SystemUtil.getVersionCode(getActivity());
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(UserInfoUtils.getId(getActivity()), String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        DialogUtil.showVersionDialog(getActivity(), bean.getFlag(), bean.getNewVersionName(), bean.getUpdateContent(),
                "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                    @Override
                    public void ok() {
                        DownloadUtil.updataVersion(
                                "社区盒子",
                                getActivity(),
                                bean.getDownloadUrl(), "hezifenxiao",
                                "hezifenxiao.apk", true, bean.getNewVersionName(), bean.getUpdateContent());
                    }

                    @Override
                    public void no() {

                    }
                });
    }
}