package com.bikejoy.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
	private static MCCustomToast centerToast;
	private static Handler mHandler;

	private static Handler getHandler() {
		if (ToastUtils.mHandler == null) {
			ToastUtils.mHandler = new Handler(Looper.getMainLooper());
		}

		return ToastUtils.mHandler;
	}

	/*public static void showToastInCenter(final Context context, final int type, final String info, final int druation) {
		ToastUtils.getHandler().post(new Runnable() {
			@Override
			public void run() {
				if (getCenterToast() == null) {
					setCenterToast(new MCCustomToast(context));
				}
				ToastUtils.getCenterToast().setType(type);
				ToastUtils.getCenterToast().setText(info);
				ToastUtils.getCenterToast().setDuration(druation);
				ToastUtils.getCenterToast().show();
			}
		});
	}*/


	public static void showToastInCenterSuccess(final Context context, final String info) {
		ToastUtils.getHandler().post(new Runnable() {
			@Override
			public void run() {
				if (getCenterToast() == null) {
					setCenterToast(new MCCustomToast(context));
				}
				ToastUtils.getCenterToast().setType(2);
				ToastUtils.getCenterToast().setText(info);
				ToastUtils.getCenterToast().setDuration(Toast.LENGTH_SHORT);
				ToastUtils.getCenterToast().show();
			}
		});
	}

	public static void showToastInCenterFailed(final Context context, final String info) {
		ToastUtils.getHandler().post(new Runnable() {
			@Override
			public void run() {
				if (getCenterToast() == null) {
					setCenterToast(new MCCustomToast(context));
				}
				ToastUtils.getCenterToast().setType(1);
				ToastUtils.getCenterToast().setText(info);
				ToastUtils.getCenterToast().setDuration(Toast.LENGTH_SHORT);
				ToastUtils.getCenterToast().show();
			}
		});
	}

	static MCCustomToast getCenterToast() {
		return ToastUtils.centerToast;
	}

	static void setCenterToast(MCCustomToast centerToast) {
		ToastUtils.centerToast = centerToast;
	}

}
