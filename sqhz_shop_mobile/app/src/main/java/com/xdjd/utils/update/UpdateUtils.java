package com.xdjd.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.xdjd.storebox.R;
import com.xdjd.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UpdateUtils {
	// 自定义下载进度条  
			//public static ProgressBarView progressBar;
			// 是否终止下载  
			public static boolean isInterceptDownload = false;  
			//进度条显示数值  
			public static int progress = 0; 
			
			public static Dialog back_dialog;
			public static Context mContext;
	
	
	
	public static Handler handler = new Handler() {  
		public void handleMessage(Message msg) {  
			switch (msg.what) {  
			case 1:  
				// 更新进度情况
				LogUtils.e("下载进度",progress+"");
				//progressBar.setProgress(progress);
				break;  
			case 0:  
				  
				// 安装apk文件  
				installApk(mContext);  
				break;  
			default:  
				break;  
			}  
		};  
	};  

	
	
	
	
	
	
	/**
	 * 版本更新
	 * 下载APK
	 * @author zhangguoqing
	 */
	public static void doloadAPK(final String address,Context context){
		mContext = context;
		showUpdateDialog(context);
		LogUtils.e("下载版本","走过");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {  
					//服务器上新版apk地址  
					URL url = new URL(address);  
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
					conn.connect();  
					int length = conn.getContentLength();  
					InputStream is = conn.getInputStream();  
					File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/");  
					if(!file.exists()){  
						//如果文件夹不存在,则创建  
						file.mkdir();  
					}  
					//下载服务器中新版本软件（写文件）  
					String apkFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" + "newapk";  
					File ApkFile = new File(apkFile);  
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(ApkFile);  
					int count = 0;  
					byte buf[] = new byte[1024];  
					do{  
						int numRead = is.read(buf);  
						count += numRead;  
						//更新进度条  
						//progress = (int) (((float) count / length) * 100);
						handler.sendEmptyMessage(1);  
						if(numRead <= 0){  
							//下载完成通知安装  
							handler.sendEmptyMessage(0);  
							break;  
						}  
						fos.write(buf,0,numRead);  
						//当点击取消时，则停止下载  
					}while(!isInterceptDownload);  
				} catch (MalformedURLException e) {  
					e.printStackTrace();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  

		}).start();
	}
	public static void showUpdateDialog(Context context){
		back_dialog = new Dialog(context);//实例化
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_prigressbar_layout, null);//加载View
		//progressBar = (ProgressBarView) view.findViewById(R.id.tasks_view);
		back_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		back_dialog.setContentView(view);
		back_dialog.show();
//		back_dialog.setCancelable(false);
		back_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//		dialogOperationUpdate(back_dialog);
	}
//	private void dialogOperationUpdate(Dialog dialog){
//		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//		WindowManager wm = this.getWindowManager();
////
////		int width = wm.getDefaultDisplay().getWidth();
//		int heigh = wm.getDefaultDisplay().getHeight();
//		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
////		params.width = width/10*5;
//		params.height = heigh/10*10;
//		dialog.getWindow().setAttributes(params);
//	}
	/**
	 * 安装APK
	 */
	public static void installApk(Context context) {  
		// 获取当前sdcard存储路径  
		File apkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" + "newapk");  
		if (!apkfile.exists()) {  
			return;  
		}  
		Intent i = new Intent(Intent.ACTION_VIEW);  
		// 安装，如果签名不一致，可能出现程序未安装提示  
		i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())), "application/vnd.android.package-archive");   
		context.startActivity(i);  
		//progressBar.setVisibility(View.INVISIBLE);
		back_dialog.dismiss();
	}

}
