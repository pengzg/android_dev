package com.bikejoy.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bikejoy.testdemo.base.BaseConfig;

import java.util.List;

public class UserInfoUtils {
	/** SharedPreferences名称  */
	public static final String SHARED_PREFERENCES = "distribution";

	/**
	 * 用户信息
	 */
	public static final String USER = "User";
	/**
	 * 店铺列表
	 */
	public static final String SHOPLIST = "shopList";
	/** 是否第一次进入APP */
	public static final String IS_FRIST = "is_frist";
	/** 域名 */
	public static final String DOMAIN_NAME = "domain_name";
	/** sign */
	public static final String SIGN = "sign";
	/** 用户id */
	public static final String ID = "id";
	/** 店铺id */
	public static final String SHOP_ID = "shopId";
	/**用户账户*/
	public static final String ACCOUNT= "ACCOUNT";
	/** 用户密码 */
	public static final String PASSWORD = "PASSWORD";
	/**公司id*/
	public static final String ORGID = "ORGID";
	/** 仓库名称 */
	public static final String STORE_NAME = "STORE_NAME";
	/** 仓库id */
	public static final String STORE_ID = "STORE_ID";
	/** 是否自动获取IP */
	public static final String IS_AUTOMATIC = "IS_AUTOMATIC";
	/** 登录日期 */
	public static final String loginDate = "loginDate";
	/** 是否自动更新客户端 */
	public static final String IS_WIFI = "is_wifi";
	/*欢迎页点击*/
	public static final String Welcome_btn = "welcome_btn";
	/** 记录版本最新版本号 */
	public static final String VERSION_CODE = "VERSION_CODE";
	// 错误日志信息
	private static final String ERRORSTR = "ERRORSTR";
	// 是否有错误日志
	private static final String ISERRORFILE = "ISERRORFILE";

	/** 存储用户默认店铺id */
	public static void setShopId(Context mContext, String shopId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SHOP_ID, shopId);
		editor.commit();
	}
	public static String getShopId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SHOP_ID, "");
	}

	/**
	 * 登录用户信息
	 * @param bean
	 */
	public static void setUser(Context mContext,UserBean bean) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		if (bean != null) {
			String beanJson = JsonUtils.toJSONString(bean);
			LogUtils.e("beanJson",beanJson);
			preferences.edit().putString(USER, beanJson).commit();
		} else {
			preferences.edit().putString(USER, "").commit();
		}
	}
	public static UserBean getUser(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		String beanJson = preferences.getString(USER, "");
		UserBean bean = JsonUtils.parseObject(beanJson,UserBean.class);
		return bean;
	}

	/**
	 * 获取店铺列表信息
	 * @param shopList
	 */
	public static void setShopList(Context mContext,List<ShopListBean> shopList) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		if (shopList != null && shopList.size()>0) {
			String listJson = JsonUtils.toJSONString(shopList);
			preferences.edit().putString(SHOPLIST, listJson).commit();
		} else {
			preferences.edit().putString(SHOPLIST, "").commit();
		}
	}
	public static List<ShopListBean> getShopList(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		String listJson = preferences.getString(SHOPLIST, "");
		List<ShopListBean> shopList = JsonUtils.getObjectsList(listJson,ShopListBean.class);
		return shopList;
	}

	/**域名*/
	public static void setDomainName(Context mContext, String domainName) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(DOMAIN_NAME, domainName);
		editor.commit();
	}
	public static String getDomainName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(DOMAIN_NAME, BaseConfig.isOfficial?"https://qy.sqkx.net" : "https://qytest.sqkx.net");//"http://182.92.1.181:8087");//"http://ds.sqkx.net");//默认是这个域名
	}

	/** 存储用户的id */
	public static void setSign(Context mContext, String sign) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SIGN, sign);
		editor.commit();
	}
	public static String getSign(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SIGN, "0");
	}

	/** 存储用户的id */
	public static void setId(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(ID, id);
		editor.commit();
	}
	public static String getId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(ID, "0");
	}

	/*是否自动获取IP*/
	public static void setIsAutomaticIp(Context mContext, boolean is_automatic) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(IS_AUTOMATIC, is_automatic);
		editor.commit();
	}
	public static boolean getIsAutomaticIp(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getBoolean(IS_AUTOMATIC, true);
	}

	/*登录日期*/
	public static void setLoginDate(Context mContext, String LoginDate) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(loginDate, LoginDate);
		editor.commit();
	}
	public static String getLoginDate(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(loginDate, "");
	}

	/*用户账号*/
	public static void setAccount(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(ACCOUNT, id);
		editor.commit();
	}
	public static String getAccount(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(ACCOUNT, "");
	}

	/*用户密码*/
	public static void setPassword(Context mContext, String password) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}
	public static String getPassword(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(PASSWORD, "");
	}

	/**公司id*/
	public static void setOrgid(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(ORGID, id);
		editor.commit();
	}
	public static String getOrgid(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(ORGID, "0");
	}

	/**
	 * 选择的仓库name
	 * @param storeName
	 */
	public static void setStoreName(Context mContext, String storeName) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(STORE_NAME, storeName).commit();
	}
	public static String getStoreName(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(STORE_NAME, "");
	}

	/**
	 * 选择的仓库id
	 * @param storeId
	 */
	public static void setStoreId(Context mContext, String storeId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(STORE_ID, storeId).commit();
	}
	public static String getStoreId(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(STORE_ID, "");
	}

	/**
	 * 错误日志信息
	 *
	 * @param path
	 */
	public static void saveError(Context mContext, String path) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(ERRORSTR, path).commit();
	}

	public static String readError(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(ERRORSTR, "");
	}

	/**
	 * 是否有错误日志 0是没有，1是有
	 *
	 * @param isError
	 */
	public static void saveIsErrorFile(Context mContext, String isError) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(ISERRORFILE, isError).commit();
	}

	public static String readIsErrorFile(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(ISERRORFILE, "0");
	}

	/** 记录最新的版本号 */
	public static void setVersion_code(Context mContext, int versioncode) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(VERSION_CODE, versioncode);
		editor.commit();
	}
	public static int getVersion_code(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getInt(VERSION_CODE, -1);
	}

	/**用户头像**/
	public static void saveTopicUrl(Context mContext, String image) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("u_image", image);
		editor.commit();
	}
	public static String getTopicUrl(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString("u_image", "");
	}

	/** 是否第一次进入APP */
	public static void saveFrist(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(IS_FRIST, id);
		editor.commit();
	}
	public static String getFrist(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(IS_FRIST, "");
	}
	/** 是否自动更新客户端 */
	public static void setWifi(Context mContext, boolean id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(IS_WIFI, id);
		editor.commit();
	}
	public static boolean getWifi(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getBoolean(IS_WIFI, true);
	}

	/** 清空用户信息 */
	public static void clearAll(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	/*欢迎页点击*/

	public static void setWelcome_btn(Context mContext, String btn) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(Welcome_btn,btn);
		editor.commit();
	}
	public static String getWelcome_btn(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(Welcome_btn, "");
	}


}
