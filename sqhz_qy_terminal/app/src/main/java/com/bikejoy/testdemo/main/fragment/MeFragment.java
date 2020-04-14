package com.bikejoy.testdemo.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.bikejoy.utils.AppManager;
import com.bikejoy.utils.AppUtil;
import com.bikejoy.utils.DialogUtil;
import com.bikejoy.utils.FileUtils;
import com.bikejoy.utils.ImageLoader;
import com.bikejoy.utils.LogUtils;
import com.bikejoy.utils.SystemUtil;
import com.bikejoy.utils.UIUtils;
import com.bikejoy.utils.UserInfoUtils;
import com.bikejoy.utils.http.AsyncHttpUtil;
import com.bikejoy.utils.http.ExceptionType;
import com.bikejoy.utils.http.IUpdateUI;
import com.bikejoy.utils.http.NetUtils;
import com.bikejoy.utils.http.operation.L_RequestParams;
import com.bikejoy.utils.http.url.M_Url;
import com.bikejoy.utils.version.DownloadUtil;
import com.bikejoy.view.roundedimage.RoundedImageView;
import com.bikejoy.testdemo.R;
import com.bikejoy.testdemo.base.BaseConfig;
import com.bikejoy.testdemo.base.BaseFragment;
import com.bikejoy.testdemo.base.Common;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/16
 *     desc   : 首页
 *     version: 1.0
 * </pre>
 */

public class MeFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.ll_edit_mobile)
    LinearLayout mLlEditMobile;
    @BindView(R.id.ll_edit_password)
    LinearLayout mLlEditPassword;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    @BindView(R.id.iv_head)
    RoundedImageView mIvHead;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_info_desc)
    TextView mTvInfoDesc;
    @BindView(R.id.ll_system)
    LinearLayout mLlSystem;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;
    @BindView(R.id.ll_version_name)
    LinearLayout mLlVersionName;
    @BindView(R.id.ll_share_coupon)
    LinearLayout mLlShareCoupon;
    @BindView(R.id.tv_role_name)
    TextView mTvRoleName;
    @BindView(R.id.ll_role)
    LinearLayout mLlRole;
    @BindView(R.id.tv_leader_name)
    TextView mTvLeaderName;
    @BindView(R.id.tv_my_car)
    TextView mTvMyCar;
    @BindView(R.id.ll_car)
    LinearLayout mLlCar;


    private UserBean userBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void OnActCreate(Bundle savedInstanceState) {
        userBean = UserInfoUtils.getUser(getActivity());
        mTvVersionName.setText("v" + SystemUtil.getVersion(getActivity()));

        mTvName.setText(userBean.getName());
        mTvMobile.setText(userBean.getLogincode());
        mTvInfoDesc.setText(userBean.getShopName());
        if (userBean.getRoleCode() == null || userBean.getRoleCode().length() == 0) {
            mLlRole.setVisibility(View.GONE);
        } else {
            mLlRole.setVisibility(View.VISIBLE);
            mTvRoleName.setText(userBean.getRoleName());
        }

        if(TextUtils.isEmpty(userBean.getCarid())){
            mLlCar.setVisibility(View.GONE);
        }else{
            mLlCar.setVisibility(View.VISIBLE);
            mTvMyCar.setText(userBean.getCarid_nameref());
        }

        switch (userBean.getRoleCode()) {
            case Common.ROLE3001:
                if (TextUtils.isEmpty(userBean.getMbw_leader_workid_nameref())) {
                    mTvLeaderName.setVisibility(View.GONE);
                } else {
                    mTvLeaderName.setVisibility(View.VISIBLE);
                    mTvLeaderName.setText("我的主管:" + userBean.getMbw_leader_workid_nameref());
                }
                break;
            default:
                mTvLeaderName.setVisibility(View.GONE);
                break;
        }

        ImageLoader.imageLoader(getActivity(), userBean.getAvatar(), mIvHead,
                R.mipmap.customer_img, R.mipmap.customer_img);
    }

    @OnClick({R.id.ll_edit_mobile, R.id.ll_edit_password, R.id.btn_exit, R.id.ll_system, R.id.ll_share, R.id.ll_version_name, R.id.ll_share_coupon, R.id.ll_role})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_edit_mobile:
                startActivity(EditMobileActivity.class);
                break;
            case R.id.ll_edit_password:
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.btn_exit:
                DialogUtil.showCustomDialog(getActivity(), "提示", "是否退出?",
                        "退出", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                UserInfoUtils.setUser(getActivity(), null);
                                finishActivity();
                                AppManager.getInstance().finishAllActivity();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                /*ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                                am.killBackgroundProcesses(getActivity().getPackageName());
                                System.exit(0);*/
                                PushAgent mPushAgent = PushAgent.getInstance(getActivity());
                                //先删除旧的别名,再添加新的别名
                                mPushAgent.deleteAlias(userBean.getWorkid(), BaseConfig.Alias_Type, new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String message) {
                                        LogUtils.e("先删除别名", "isSuccess:" + isSuccess + "--message:" + message);
                                    }
                                });
                            }

                            @Override
                            public void no() {
                            }
                        });
                break;
            case R.id.ll_system:
                startActivity(SystemActivity.class);
                break;
            case R.id.ll_share:
                Intent intent = new Intent(getActivity(), ShareDownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_version_name:
                /*PermissionUtils.requstAcivityStorage(getActivity(), 1000, new PermissionUtils.OnRequestCarmerCall() {
                    @Override
                    public void onSuccess() {
                        getNewVersion();
                    }

                    @Override
                    public void onDilogCancal() {
                        showToast("获取读取sd权限失败!");
                    }
                });*/
                startActivity(SystemActivity.class);
                break;
            case R.id.ll_share_coupon:
                startActivity(ShareCouponActivity.class);
                break;
            case R.id.ll_role:
                queryRoleList(userBean);
                break;
        }
    }

    /**
     * 获取用户角色列表
     */
    private void queryRoleList(final UserBean userBean) {
        AsyncHttpUtil<RoleListBean> httpUtil = new AsyncHttpUtil<>(getActivity(), RoleListBean.class, new IUpdateUI<RoleListBean>() {
            @Override
            public void updata(final RoleListBean bean) {
                if ("200".equals(bean.getCode())) {
                    final List<RoleListBean> list = bean.getData();
                    if (list == null || list.size() == 0) {
                        showToast("暂无其他角色信息");
                        return;
                    } else if (list.size() == 1) {
                        showToast("你当前只有一种角色信息");
                        return;
                    } else {
                        DialogUtil.showRoleList(getActivity(), "请选择角色", bean.getData(), new DialogUtil.MyCustomDialogListener3() {
                            @Override
                            public void item(int i) {
                                //添加角色类型
                                userBean.setRoleCode(list.get(i).getSr_code());
                                userBean.setRoleName(list.get(i).getSr_name());

                                userBean.setUsertype(list.get(i).getSr_code());
                                UserInfoUtils.setUser(getActivity(), userBean);

                                AppManager.getInstance().finishAllActivity();
                                finishActivity();
                                startActivity(SplashActivity.class);
                            }
                        });
                    }
                } else {
                    showToast(bean.getDesc());
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
        httpUtil.post(M_Url.queryRoleList, L_RequestParams.queryRoleList(userBean), true);
    }

    private void getNewVersion() {
        AsyncHttpUtil<NewVersionBean> httpNewVersion = new AsyncHttpUtil<>(getActivity(), NewVersionBean.class, new IUpdateUI<NewVersionBean>() {
            @Override
            public void updata(NewVersionBean jsonBean) {
                //LogUtils.e("版本信息"+SystemUtil.getVersion(MainActivity.this),"--"+bean.getFlag());
                if ("200".equals(jsonBean.getCode()) && jsonBean.getData() != null) {
                    NewVersionBean bean = jsonBean.getData();
                    //判断版本
                    if (SystemUtil.getVersionCode(getActivity()) < bean.getBv_version1()) {
                        if ("1".equals(bean.getBv_upgrade())) {//强制更新
                            updateDialog(bean);
                        } else if ("2".equals(bean.getBv_upgrade())) {
                            //本地已下载的安装包版本号
                            int SD_Version_code = AppUtil.apkInfo(BaseConfig.installPath, getActivity());
                            //系统返回的版本号
                            final int SYSTEM_VersionCode = bean.getBv_version1();
                            if (UserInfoUtils.getVersion_code(getActivity()) == SYSTEM_VersionCode) {
                                //最新的版本号,当点击取消后,不会再弹框了
                            } else {
                                if (SD_Version_code == AppUtil.Overdue || SD_Version_code < SYSTEM_VersionCode) {
                                    updateDialog(bean);
                                } else if (SD_Version_code == SYSTEM_VersionCode) {
                                    DialogUtil.showVersionDialog(getActivity(), "0", bean.getBv_version_name(), bean.getBv_desc(),
                                            "安装", "取消", new DialogUtil.MyCustomDialogListener2() {
                                                @Override
                                                public void ok() {
                                                    File file = new File(BaseConfig.installPath);
                                                    Intent i = new Intent();
                                                    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    i.setAction(Intent.ACTION_VIEW);
                                                    i.setDataAndType(FileUtils.getUriForFile(getActivity(), file),
                                                            "application/vnd.android.package-archive");
                                                    startActivity(i);
                                                }

                                                @Override
                                                public void no() {
                                                    UserInfoUtils.setVersion_code(getActivity(), SYSTEM_VersionCode);
                                                }
                                            });
                                }
                            }
                        }
                    } else {
                        showToast("当前已是最新版本!");
                    }
                } else {
                    showToast("当前已是最新版本!");
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
        httpNewVersion.post(M_Url.getNewVersion, L_RequestParams.getNewVersion(String.valueOf(versionCode)), false);
    }

    private void updateDialog(final NewVersionBean bean) {
        if (UserInfoUtils.getWifi(getActivity()) && NetUtils.isWifi(getActivity()) && !"1".equals(bean.getBv_upgrade())) {
            DownloadUtil.updataVersion(
                    UIUtils.getString(R.string.app_name),
                    getActivity(),
                    bean.getBv_down_url(), "sss",
                    "sss.apk", false, bean.getBv_version_name(), bean.getBv_desc());
        } else {
            if ("1".equals(bean.getBv_upgrade())) {
                DialogUtil.showVersionDialog(getActivity(), bean.getBv_upgrade(), bean.getBv_version_name(), bean.getBv_desc(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                if (!NetUtils.isConnected(getActivity())) {
                                    Toast.makeText(getActivity(), ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                showProgressDialog(getActivity(), "下载中...");
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        getActivity(),
                                        bean.getBv_down_url(), "sss",
                                        "sss.apk", true, bean.getBv_version_name(), bean.getBv_desc());
                            }

                            @Override
                            public void no() {

                            }
                        });
            } else {
                DialogUtil.showVersionDialog(getActivity(), bean.getBv_upgrade(), bean.getBv_version_name(), bean.getBv_desc(),
                        "下载更新", "取消", new DialogUtil.MyCustomDialogListener2() {
                            @Override
                            public void ok() {
                                DownloadUtil.updataVersion(
                                        UIUtils.getString(R.string.app_name),
                                        getActivity(),
                                        bean.getBv_down_url(), "sss",
                                        "sss.apk", false, bean.getBv_version_name(), bean.getBv_desc());
                            }

                            @Override
                            public void no() {

                            }
                        });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
