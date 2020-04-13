package com.xdjd.utils.update;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xdjd.storebox.R;


public class VersionDialog implements OnClickListener {
	/** Dialog */
	private Dialog mDialog;
	private Activity mActivity;
	private String url;
	public VersionDialog(Activity mActivity, String url){
		this.mActivity = mActivity;
		this.url = url;
	}

	@SuppressWarnings("deprecation")
	public void showDialog() {
		View rootView = LayoutInflater.from(mActivity).
				inflate(R.layout.dialog_version_layout, null);
		rootView.findViewById(R.id.dialog_tv_cancel).setOnClickListener(this);
		rootView.findViewById(R.id.dialog_tv_update).setOnClickListener(this);
		mDialog = new Dialog(mActivity);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.show();
		// 添加布局
		dialogOperation(mDialog);
		mDialog.setContentView(rootView);
		mDialog.setCancelable(true);  // false点击空白不消失

	}
	/**
	 * 改变Dialog的宽度和高度
	 * @param dialog
	 */
	private void dialogOperation(Dialog dialog){
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		WindowManager wm = mActivity.getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = width / 10 * 7;
		dialog.getWindow().setAttributes(params);
	}
	//完成时消失
	public void dismissDialog(){
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.dialog_tv_cancel:
			mDialog.dismiss();
			break;
		case R.id.dialog_tv_update:
			mDialog.dismiss();
			if(TextUtils.isEmpty(url)){
				toast("暂无下载地址",mActivity);
				return ;
			}
			if(!url.substring(url.length()-4, url.length()).equals(".apk")){
				toast( "下载地址有误"+url.substring(url.length()-4, url.length()),mActivity);
				return ;
			}
			UpdateUtils.doloadAPK(url, mActivity);
			break;
		default:
			break;
		}
	}


	/** 显示Toast */
	public void toast(String toast, Context context) {
		Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
	}
}

