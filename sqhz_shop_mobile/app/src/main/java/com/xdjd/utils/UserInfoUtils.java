package com.xdjd.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class UserInfoUtils {
	/** SharedPreferences名称  */
	public static final String SHARED_PREFERENCES = "Zhsh";
	/*用户头像*/
	public static final String USER_ICON="user_icon";
	/** 用户id */
	public static final String ID = "id";

	/*用户手机账户*/
	public static final String Mobile= "mobile";
	/** 城市id */
	public static final String CITY_ID = "city_id";
	/** 城市名称 */
	public static final String CITY_NAME = "city_name";
	/** 当前纬度 */
	public static final String LAT = "lat";
	/** 当前精度 */
	public static final String LNG = "lng";
	/** 自己选择纬度 */
	public static final String SELECT_LAT = "select_lat";
	/** 自己选择精度 */
	public static final String SELECT_LNG = "select_lng";
	/** 是否第一次进入APP */
	public static final String IS_FRIST = "is_frist";
	/** 是否自动更新客户端 */
	public static final String IS_WIFI = "is_wifi";
	/** 推送的tag标签 */
	public static final String TAG_LIST = "tag_list";

	/** 专销员名字*/
	public static final String SPREAD_NAME = "spread_name";
	/** 专销员电话 */
	public static final String SPREAD_MOBILE = "spreadMobile";
	/** 中心仓名称 */
	public static final String CENTER_SHOP_NAME = "centerShopName";

	/** 用户店铺id */
	public static final String USER_SHOP_ID = "userShopId";

	/** 绑定微信的unionid */
	public static final String WeiXinUnionID = "unionId";
	/*欢迎页点击*/
	public static final String Welcome_btn = "welcome_btn";
	/** 中心仓id */
	public static final String CENTER_SHOP_ID = "center_shopid";
	/** 下载H5界面 */
	public static final String DOWNLOAD_URL_H5 = "DOWNLOAD_URL_H5";

	/** 记录版本最新版本号 */
	public static final String VERSION_CODE = "VERSION_CODE";

	/*手势密码*/
	public static final String Lock_code = "lock";

	/** 积分规则url */
	public static final String  INTE_RULE_URL= "inte_rule_url";

	// 错误日志文件的位置
	private static final String ERRORFILE = "ERRORFILE";
	// 错误日志信息
	private static final String ERRORSTR = "ERRORSTR";
	// 是否有错误日志
	private static final String ISERRORFILE = "ISERRORFILE";

	//orgPhone
	private static final String orgPhone = "orgPhone";
	//仓库id
	private static final String StoreHouseId = "storeHouseId";
	//负库存
	private static final String BsIsNegative = "bsIsNegative";
	//虚拟库存
	private static final String BsIsVirtual = "bsIsVirtual";
	/** 是否自动获取IP */
	public static final String IS_AUTOMATIC = "IS_AUTOMATIC";
	/*登录账号*/
	public static final String LoginName = "Login_Name";
	/*登录密码*/
	public static final String LoginPwd = "Login_Pwd";
	/*公司id*/
	public static final String CompanyId = "Company_Id";
	/*是否显示切换公司*/
	public static final String YesOrNoShowChange = "YesOrNoShowChange";
	/** 域名 */
	public static final String DOMAIN_NAME = "domain_name";


	/**域名*/
	public static void setDomainName(Context mContext, String domainName) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(DOMAIN_NAME, domainName);
		editor.commit();
	}
	public static String getDomainName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(DOMAIN_NAME, "http://order.sqkx.net");//"http://182.92.1.181:8087");//"http://hzdh.sqkx.net");//默认是这个域名
	}

	public static void setChangeCompanyFlag(Context mContext, String flag) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(YesOrNoShowChange, flag);
		editor.commit();
	}
	public static String getChangeCompanyFlag(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(YesOrNoShowChange, "0");
	}

	/*存储公司id */
	public static void setCompanyId(Context mContext, String companyId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CompanyId, companyId);
		editor.commit();
	}
	public static String getCompanyId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CompanyId, "0");
	}
	/** 存储用户登录账号 */
	public static void setLoginName(Context mContext, String name) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(LoginName, name);
		editor.commit();
	}
	public static String getLoginName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LoginName, "0");
	}
	/** 存储用户登录密码 */
	public static void setLoginPwd(Context mContext, String pwd) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(LoginPwd, pwd);
		editor.commit();
	}
	public static String getLoginPwd(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LoginPwd, "0");
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

	/*是否虚拟库存*/
	public static void setBsIsVirtual(Context mContext, String phone) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(BsIsVirtual, phone);
		editor.commit();
	}
	public static String getBsIsVirtual(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(BsIsVirtual, "0");
	}
	/*是否允许负库存*/
	public static void setBsIsNegative(Context mContext, String phone) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(BsIsNegative, phone);
		editor.commit();
	}
	public static String getBsIsNegative(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(BsIsNegative, "0");
	}

	/*存储发货仓库id*/
	public static void setStoreHouseId(Context mContext, String phone) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(StoreHouseId, phone);
		editor.commit();
	}
	public static String getStoreHouseId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(StoreHouseId, "0");
	}
	/** 存储公司电话 */
	public static void setOrgPhone(Context mContext, String phone) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(orgPhone, phone);
		editor.commit();
	}
	public static String getOrgPhone(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(orgPhone, "0");
	}

	/**
	 * 错误日志文件的位置
	 *
	 * @param path
	 */
	public static void saveErrorFile(Context mContext,String path) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(ERRORFILE, path).commit();
	}

	public static String readErrorFile(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(ERRORFILE, "");
	}

	/**
	 * 错误日志信息
	 *
	 * @param path
	 */
	public static void saveError(Context mContext,String path) {
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
	public static void saveIsErrorFile(Context mContext,String isError) {
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
	/*用户手机账号*/
	public static void setMobile(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(Mobile, id);
		editor.commit();
	}
	public static String getMobile(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(Mobile, "0");
	}


	/*手势密码*/
	public static void setLock(Context mContext ,String id){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(Lock_code, id);
		editor.commit();
	}
	public static String getLock(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(Lock_code, "0");
	}


	/** 存储用户的头像 */
	public static void setIcon(Context mContext, String icon) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(USER_ICON, icon);
		editor.commit();
	}
	public static String getIcon(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		String icon = preferences.getString(USER_ICON, "-1");
		return icon;
	}
	/** 选择的城市id */
	public static void setCityId(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CITY_ID, id);
		editor.commit();
	}
	public static String getCityId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CITY_ID, "");
	}
	/** 选择的城市名称 */
	public static void setCityName(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CITY_NAME, id);
		editor.commit();
	}
	public static String getCityName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CITY_NAME, "");
	}
	/** 存储用户的精度 */
	public static void saveLng(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(LNG, id);
		editor.commit();
	}
	public static String getLng(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LNG, "116.422462");
	}
	/** 存储用户的纬度 */
	public static void saveLat(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(LAT, id);
		editor.commit();
	}
	public static String getLat(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LAT, "40.041826");
	}
	/** 存储用户选择的精度 */
	public static void saveSelectLng(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SELECT_LNG, id);
		editor.commit();
	}
	public static String getSelectLng(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SELECT_LNG, "");
	}
	/** 存储用户选择的纬度 */
	public static void saveSelectLat(Context mContext, String id) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SELECT_LAT, id);
		editor.commit();
	}
	public static String getSelectLat(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SELECT_LAT, "");
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

	/** 推送taglist集合标签 */
	public static void setTagList(Context mContext, Set<String> taglist) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putStringSet(TAG_LIST, taglist);
		editor.commit();
	}
	public static Set<String> getTagList(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getStringSet(TAG_LIST,null);
	}

	/** 清空用户信息 */
	public static void clearAll(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	/** 专属业务员名字 */
	public static void setSpreadName(Context mContext,String spread_name){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SPREAD_NAME, spread_name);
		editor.commit();
	}

	public static String getSpreadName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SPREAD_NAME, "");
	}

	/** 专属业务员电话 */
	public static void setSpreadMobile(Context mContext,String spread_mobile){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SPREAD_MOBILE, spread_mobile);
		editor.commit();
	}

	public static String getSpreadMobile(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SPREAD_MOBILE, "");
	}

	/** 中心仓名称 */
	public static void setCenterShopName(Context mContext,String center_shop_name){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CENTER_SHOP_NAME, center_shop_name);
		editor.commit();
	}

	public static String getCenterShopName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CENTER_SHOP_NAME, "");

	}


	/** 用户店铺id */
	public static void setUserShopId(Context mContext,String center_shop_name){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(USER_SHOP_ID, center_shop_name);
		editor.commit();
	}

	public static String getUserShopId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(USER_SHOP_ID, "0");

	}

	/*绑定微信用户unionid*/
	public static void setWeiXinUnionID(Context mContext,String unionid){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(WeiXinUnionID,unionid );
		editor.commit();
	}

	/** 中心仓id */
	public static void setCenterShopId(Context mContext,String center_shop_id){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CENTER_SHOP_ID, center_shop_id);
		editor.commit();
	}

	public static String getCenterShopId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CENTER_SHOP_ID, "");
	}

	public static String getWeinXinUnionID(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(WeiXinUnionID, "");
	}


	/*欢迎页点击*/

	public static void setWelcome_btn(Context mContext,String btn) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(Welcome_btn,btn);
		editor.commit();
	}
	public static String getWelcome_btn(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(Welcome_btn, "");
	}


	public static void setDownload_Url(Context mContext,String btn) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(DOWNLOAD_URL_H5,btn);
		editor.commit();
	}
	public static String getDownload_Url(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(DOWNLOAD_URL_H5, "");
	}

	/** 积分规则url */
	public static void setInteRuleUrl(Context mContext,String btn) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(INTE_RULE_URL,btn);
		editor.commit();
	}
	/** 积分规则url */
	public static String getInteRuleUrl(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(INTE_RULE_URL, "");
	}
}
