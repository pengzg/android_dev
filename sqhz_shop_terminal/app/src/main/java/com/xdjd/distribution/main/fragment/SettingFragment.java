package com.xdjd.distribution.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xdjd.distribution.R;
import com.xdjd.distribution.activity.PrintSettingActivity;
import com.xdjd.distribution.activity.ShareDownloadActivity;
import com.xdjd.distribution.activity.SystemActivity;
import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.base.BaseFragment;
import com.xdjd.distribution.bean.BaseBean;
import com.xdjd.distribution.bean.NewVersionBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.AppUtil;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.SystemUtil;
import com.xdjd.utils.UserInfoUtils;
import com.xdjd.utils.YesOrNoLoadingOnstart;
import com.xdjd.utils.http.AsyncHttpUtil;
import com.xdjd.utils.http.ExceptionType;
import com.xdjd.utils.http.IUpdateUI;
import com.xdjd.utils.http.operation.L_RequestParams;
import com.xdjd.utils.http.url.M_Url;
import com.xdjd.utils.permissions.PermissionUtils;
import com.xdjd.utils.update.DownloadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijipei on 2016/8/22.
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.system_version)
    TextView mSystemVersion;
    @BindView(R.id.rl_cssz)
    RelativeLayout mRlCssz;
    @BindView(R.id.rl_cshsz)
    RelativeLayout mRlCshsz;
    @BindView(R.id.rl_dyjsz)
    RelativeLayout mRlDyjsz;
    @BindView(R.id.rl_fx)
    RelativeLayout mRlFx;
    @BindView(R.id.rl_bbh)
    RelativeLayout mRlBbh;
    @BindView(R.id.rl_xt)
    RelativeLayout mRlXt;
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;
    @BindView(R.id.tv_staff_name)
    TextView mTvStaffName;
    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.tv_card_num)
    TextView mTvCardNum;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    @BindView(R.id.rl_fx_shop)
    RelativeLayout mRlFxShop;
    private View view = null;

    private String headImgUrl = "http://test.sqkx.net/app/user/updateUserInfo.action";

    private UserBean userBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {

        userBean = UserInfoUtils.getUser(getActivity());

        mTvVersionName.setText("版本号v" + SystemUtil.getVersion(getActivity()));

        mTvAccount.setText("账号:" + UserInfoUtils.getAccount(getActivity()));
        mTvStaffName.setText("姓名:" + userBean.getBud_name());
        mTvLineName.setText("线路:" + UserInfoUtils.getLineName(getActivity()));
        if (!"".equals(userBean.getSu_storeid_name())) {
            mTvCardNum.setText("车辆:" + userBean.getSu_storeid_name());
        } else {
            mTvCardNum.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //判断是否要运行此方法里的内容
        if (!(YesOrNoLoadingOnstart.INDEX = true && YesOrNoLoadingOnstart.INDEX_ID == 3)) {
            return;
        }

        mTvAccount.setText("账号:" + UserInfoUtils.getAccount(getActivity()));
        mTvStaffName.setText("姓名:" + userBean.getBud_name());
        mTvLineName.setText("线路:" + UserInfoUtils.getLineName(getActivity()));
        if (!"".equals(userBean.getSu_storeid_name())) {
            mTvCardNum.setText("车辆:" + userBean.getSu_storeid_name());
        } else {
            mTvCardNum.setText("");
        }

    }

    // 提交到服务器
    private void toService(String uri) {
        Log.e("tag", uri);
        //        if (!indexOfString(uri, ".")) {
        //            uri = uri + ".jpg";
        //        }
        //deviceType=android&reqCode=0126&sign=&device=866769028593617&user=48055&iconImg=FILE
        RequestParams params = new RequestParams();
        params.put("reqCode", "0126");
        params.put("device", "866769028593617");
        params.put("deviceType", "android");
        params.put("user", "48055");
        params.put("sign", "");
        File f = new File(uri);
        try {
            params.put("iconImg", f);
        } catch (FileNotFoundException e) {
        }

        AsyncHttpUtil.postFile(headImgUrl,
                uri, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0, arg1);
                        JsonUtils.parseObject(arg1,
                                BaseBean.class);
                        //                        showToast(bean.getRepMsg());

                    }

                    @Override
                    public void onFailure(Throwable arg0, String arg1) {
                        // TODO Auto-generated method stub
                        super.onFailure(arg0, arg1);
                    }
                });

    }

    @OnClick({R.id.rl_cssz, R.id.rl_cshsz, R.id.rl_dyjsz, R.id.rl_fx, R.id.rl_bbh, R.id.rl_xt,R.id.rl_fx_shop})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_cssz:
                break;
            case R.id.rl_cshsz://客户反馈
                //                startActivity(CustomerFeedbackActivity.class);
                break;
            case R.id.rl_dyjsz:
                startActivity(PrintSettingActivity.class);
                break;
            case R.id.rl_fx:
                intent = new Intent(getActivity(),ShareDownloadActivity.class);
                intent.putExtra("shareType","1");
                startActivity(intent);
                break;
            case R.id.rl_bbh:
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
            case R.id.rl_xt:
                startActivity(SystemActivity.class);
                break;
            case R.id.rl_fx_shop:
                intent = new Intent(getActivity(),ShareDownloadActivity.class);
                intent.putExtra("shareType","2");
                startActivity(intent);
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
        httpUtil.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(userBean.getUserId(), String.valueOf(versionCode)), false);
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
