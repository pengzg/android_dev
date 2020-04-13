package com.xdjd.utils.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xdjd.storebox.activity.LoginActivity;
import com.xdjd.utils.JsonUtils;
import com.xdjd.utils.LogUtils;
import com.xdjd.utils.ProgressUtils;
import com.xdjd.utils.UIUtils;
import com.xdjd.utils.UserInfoUtils;

import org.json.JSONObject;

import java.io.File;

/**
 * http 请求 权限<uses-permission android:name="android.permission.INTERNET" />
 * 
 * @author slg
 * 
 */
public class AsyncHttpUtil<T> {

	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

	static {
		client.setTimeout(25*1000); // 设置链接超时，如果不设置，默认为10s
//		client.setMaxRetriesAndTimeout(2, 30*1000);
	}

	/**
	 * 取消请求
	 * @param context
	 * @param b
	 */
	public static void cancelRequests(Context context , boolean b){
		client.cancelRequests(context, b);
	}

	/**
	 *
	 * @param uString
	 * @param bHandler
	 *            用法（2）：返回字节 HttpUtil.get(url, new BinaryHttpResponseHandler() {
	 * @Override
	 *
	 *           public void onSuccess(byte[] arg0) {
	 *           super.onSuccess(arg0);
	 *         	  File file = Environment.getExternalStorageDirectory();
	 *          File  file2 = new File(file, "cat");
	 *           file2.mkdir();
	 *           file2 = new  File(file2, "cat.jpg");
	 *           try {
	 *           FileOutputStream oStream = new FileOutputStream(file2);
	 *            oStream.write(arg0);
	 *            oStream.flush();
	 *           oStream.close();
				textView.setText("可爱的猫咪已经保存在sdcard里面");
				}
	 *           catch (Exception e)
	 *           { e.printStackTrace();
	 *            Log.i("hck",
	 *           e.toString()); } } });
	 */

	/**
	 * 上传文件
	 * @param uString
	 * @param filePath
	 * @param params
	 * @param aHandler
     */
	public static void postFile(String uString, String filePath, RequestParams params, AsyncHttpResponseHandler aHandler) {

		File file = new File(filePath);
//			RequestParams params = new RequestParams();
//			params.put(fileTag, file);
		client.post(uString, params, aHandler);

	}

	public static AsyncHttpClient getClient()
	{
		return client;
	}

	private Activity mContext;
	private IUpdateUI mCallback;
	private boolean isShowNoNet = false;
	private Class<T> clazz;
	private T mT;
	ProgressUtils pu;

	public AsyncHttpUtil(Activity context, Class<T> clazz , IUpdateUI callback){
		mContext=context;
		this.mCallback=callback;
		this.clazz = clazz;
		pu = new ProgressUtils();
	}
	/** 判断是否要显示网络提示 */
	public void setIsShowNoNet(boolean isShowNoNet){
		this.isShowNoNet = isShowNoNet;
	}
	/** POST请求 */
	public  void post(String url, RequestParams params, final boolean isShowDailog){
		post(url,params,isShowDailog,null);
	}

	public static void post(String uString, String filePath, RequestParams params, AsyncHttpResponseHandler aHandler){
		client.post(uString, params, aHandler);
	}
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param isShowDailog
	 * @param dialog
	 */

	public void post(String url, RequestParams params, final boolean isShowDailog, final String dialog){
		LogUtils.e("url-->",url);
		if(mCallback == null){
			return;
		}

		if(clazz == null){
			mCallback.sendFail(ExceptionType.ParamsException);
			return;
		}

		if (!NetUtils.isConnected(mContext)){
			if(isShowNoNet){
				Toast.makeText(mContext,ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
			}
			mCallback.sendFail(ExceptionType.NoNetworkException);
			return;
		}

		client.post(mContext, url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO 加载框显示
				super.onStart();
				if (isShowDailog) {
					//显示加载框
					if (dialog == null){
						pu.showDialog(mContext);
					}else{
						pu.showDialog(mContext,dialog);
					}
				}
			}

			@Override
			public void onSuccess(int statusCode,
								  String responseString) {
				LogUtils.e("---TAG", "statusCode-->" + statusCode);
				LogUtils.e("---TAG", "post-->" + responseString);
				if (statusCode==200 && responseString != null) {
					try {
						//如果是黑名单客户直接退出登录
						JSONObject obj = new JSONObject(responseString);
						if ("88".equals(obj.getString("repCode"))){
							UserInfoUtils.setTagList(mContext, null);
							UserInfoUtils.setId(mContext, "0");
							UserInfoUtils.setCenterShopId(mContext,"");

							Intent intent = new Intent(mContext,LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							mContext.startActivity(intent);
							return;
						}else if ("30".equals(obj.getString("repCode"))){
							UserInfoUtils.setTagList(mContext, null);
							UserInfoUtils.setId(mContext, "0");
							UserInfoUtils.setCenterShopId(mContext,"");

							Intent intent = new Intent(mContext,LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							mContext.startActivity(intent);
							UIUtils.Toast(obj.getString("repMsg"));
							return;
						}

						if (clazz == String.class) {
							mT = (T) responseString;
						} else {
							mT = JsonUtils.parseObject(responseString, clazz);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						mCallback.sendFail(ExceptionType.JsonParseException);
					} catch (Exception e) {
						mCallback.sendFail(ExceptionType.Exception);
					}
					if (mT == null) {
						mCallback.sendFail(ExceptionType.Exception);
					} else {
						mCallback.updata(mT);
					}
				}else{
					LogUtils.e("---TAG","----"+statusCode);
					mCallback.sendFail(ExceptionType.RequestFailException);
				}
			}

			@Override
			public void onFailure(Throwable throwable,
								  String responseString) {
				mCallback.sendFail(ExceptionType.RequestFailException);
			}

			@Override
			public void onFinish() {
				// TODO 加载结束
				super.onFinish();
				mCallback.finish();
				if (isShowDailog) {
					//取消加载框
					pu.dismissDialog();
				}
			}
		});
	}

	public  void get(String url, final boolean isShowDailog){
		get(url,null,isShowDailog);
	}

	/** GET请求 */
	public  void get(String url, RequestParams params, final boolean isShowDailog){
		if(mCallback == null){
			return;
		}

		if(clazz == null){
			mCallback.sendFail(ExceptionType.ParamsException);
			return;
		}

		if (!NetUtils.isConnected(mContext)){
			if(isShowNoNet){
				Toast.makeText(mContext,ExceptionType.NoNetworkException.getDetail(), Toast.LENGTH_SHORT).show();
			}
			mCallback.sendFail(ExceptionType.NoNetworkException);
			return;
		}
		client.get(mContext, url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO 加载框显示
				super.onStart();
				if(isShowDailog){
					//显示加载框
					pu.showDialog(mContext);
				}
			}
			@Override
			public void onSuccess(int statusCode,
								  String responseString) {
				LogUtils.i("---TAG","get-->"+responseString);
				if (statusCode==200 && responseString != null) {
					try {
						if (clazz == String.class) {
							mT = (T) responseString;
						} else {
							mT = JsonUtils.parseObject(responseString, clazz);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						mCallback.sendFail(ExceptionType.JsonParseException);
					} catch (Exception e) {
						mCallback.sendFail(ExceptionType.Exception);
					}
					if (mT == null) {
						mCallback.sendFail(ExceptionType.Exception);
					} else {
						mCallback.updata(mT);
					}
				}else{
					LogUtils.e("---TAG","----"+statusCode);
					mCallback.sendFail(ExceptionType.RequestFailException);
				}
			}
			@Override
			public void onFailure( Throwable throwable,
								  String responseString) {
				mCallback.sendFail(ExceptionType.RequestFailException);
			}
			@Override
			public void onFinish() {
				// TODO 加载结束
				super.onFinish();
				if(isShowDailog){
					//取消加载框
					pu.dismissDialog();
				}
			}
		});
	}

}
