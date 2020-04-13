package com.xdjd.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.xdjd.view.CustomProgress;


public class ProgressUtils {
    private Dialog mDialog;

    // 加载进度条
    public void showDialog(Context mContext) {

//		mDialog = new Dialog(mContext, R.style.dialog);
//		View view = LayoutInflater.from(mContext).inflate(
//				R.layout_date_select_style.loading_process_dialog_anim, null);
//		// 添加布局
//		mDialog.show();
//		mDialog.setContentView(view);
////		mDialog.setCancelable(false); // 点击空白不消失
//		// 按返回键是否取消
//		mDialog.setCancelable(true);
        if (mContext != null && !((Activity)mContext).isFinishing()){
            CustomProgress.show(mContext, "加载中...", true, null);
        }
    }

    // 加载进度条
    public void showDialog(Context mContext, String str) {
        CustomProgress.show(mContext, str, true, null);
    }

    // 完成时消失
    public void dismissDialog(Context context) {
//		if (mDialog != null) {
//			mDialog.dismiss();
//		}
        CustomProgress.hideProgress(context);
    }
}
