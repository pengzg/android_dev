package com.xdjd.utils;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.xdjd.distribution.base.BaseConfig;
import com.xdjd.distribution.bean.ClientBean;
import com.xdjd.distribution.bean.UserBean;

import java.util.Set;

public class UserInfoUtils {
	/** SharedPreferences名称  */
	public static final String SHARED_PREFERENCES = "distribution";

	/**
	 * 用户信息
	 */
	public static final String USER = "User";

	/** 是否第一次进入APP */
	public static final String IS_FRIST = "is_frist";

	/** 域名 */
	public static final String DOMAIN_NAME = "domain_name";

	/** 登录状态 1.是登录 0是未登录 */
	public static final String LOGIN_STATE = "LOGIN_STATE";

	/** 业务员id */
	public static final String BUD_ID = "bud_id";

	/** 用户类型 1 车销,2 跑单,4 配送员,5 管理员 */
//	public static final String usertype = "USER_TYPE";

	/** sign */
	public static final String SIGN = "sign";

	/** 用户id */
	public static final String ID = "id";
	/**用户账户*/
	public static final String ACCOUNT= "ACCOUNT";
	/** 用户密码 */
	public static final String PASSWORD = "PASSWORD";
	/** 默认定位距离 */
	public static final String SIGN_DISTANCE = "signDistance";
	/**userName	登录用户名*/
//	public static final String userName= "userName";
	/**公司id*/
	public static final String ORGID = "ORGID";
	/**员工名 */
//	public static final String BUD_NAME = "bud_name";

	/** 员工联系电话 */
//	public static final String MOBILE = "MOBILE";

	/** 签到方式-是否扫卡 1扫 2手动 */
//	public static final String ISSCAN = "isScan";
	/** 进店签到方式-是否拍照 1是 2否 */
//	public static final String ISPHOTO = "isPhoto";
	/** 离店签到方式-是否拍照 1是 2否 */
//	public static final String OUTPHOTO = "OUTPHOTO";
	/** 车牌号 */
//	public static final String STOREID_NAME = "su_storeid_name";

	/** 客户余额 */
	public static final String CUSTOMER_BALANCE = "CUSTOMER_BALANCE";
	/** 客户应收金额 */
	public static final String AFTER_AMOUNT = "after_amount";
	/** 安全欠款金额 */
	public static final String SAFETY_ARREARS_NUM = "safety_arrears_num";


	/** 是否变价	1 是2不是 */
//	public static final String ISCHANGPRICE = "isChangPrice";
	/** 是否变价(退货）	1是2不是 */
//	public static final String ISCHANGETHPRICE = "IsChangeThPrice";

	/** su_storeid	业务员关联的仓库 */
//	public static final String SU_STOREID = "SU_STOREID";
	/** bud_customerid 关联的业务员 */
//	public static final String BUD_CUSTOMERID = "BUD_CUSTOMERID";

	/**线路id*/
	public static final String LINE_ID = "LINE_ID";
	/**线路名字*/
	public static final String LINE_NAME = "LINE_NAME";

	/** 仓库名称 */
	public static final String STORE_NAME = "STORE_NAME";
	/** 仓库id */
	public static final String STORE_ID = "STORE_ID";

	/** 当前签到的客户信息 */
	public static final String ClientInfo = "ClientInfo";

	/** 是否自动获取IP */
	public static final String IS_AUTOMATIC = "IS_AUTOMATIC";

	public static final String TASK_ID = "TASK_ID";

	/** 签到id	 如果签到则返回 缓存到本地 */
	public static final String CUD_ID = "CUD_ID";
	/** 登录日期 */
	public static final String loginDate = "loginDate";
	/** 刷新签到店铺信息 */
	public static final String UPDATE_SHOP = "UPDATE_SHOP";

	/** 是否使用刷卡 */
//	public static final String skType = "skType";
	/** 退货模式	 1商品  2订单 3 商品+订单 */
	public static final String REFUND_MODE = "REFUND_MODE";

	/** 蓝牙设备缓存地址 */
	public static final String deviceAddress = "deviceAddress";

	/** 小票头部信息 */
	public static final String TicketmsgHead = "TicketmsgHead";
	/** 小票尾部信息 */
	public static final String TicketmsgBottom = "TicketmsgBottom";

	/** 现金日报打印方式 */
	public static final String CashReportType = "cashReportType";

	/** 打印机类型 */
	public static final String PtinterType = "PtinterType";

	/** 是否自动更新客户端 */
	public static final String IS_WIFI = "is_wifi";
	/*欢迎页点击*/
	public static final String Welcome_btn = "welcome_btn";
	/** app 当前的版本号 */
	public static final String NOW_VERSION = "NOW_VERSION";
	/** 记录版本最新版本号 */
	public static final String VERSION_CODE = "VERSION_CODE";
	// 错误日志信息
	private static final String ERRORSTR = "ERRORSTR";
	// 微信unionId
	private static final String UNION_ID = "UNION_ID";
	// 是否有错误日志
	private static final String ISERRORFILE = "ISERRORFILE";
	/*手势密码*/
	public static final String Lock_code = "lock";

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

	/**域名*/
	public static void setDomainName(Context mContext, String domainName) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(DOMAIN_NAME, domainName);
		editor.commit();
	}
	public static String getDomainName(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(DOMAIN_NAME, "http://sjfx.sqkx.net");//"http://182.92.1.181:8087");//"http://sjfx.sqkx.net");//默认是这个域名
	}

	/** 用户登陆的状态 */
	public static void setLoginState(Context mContext, String loginState) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(LOGIN_STATE, loginState);
		editor.commit();
	}
	public static String getLoginState(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LOGIN_STATE, "0");
	}

	/** 员工退货模式 */
	public static void setRefundMode(Context mContext, String refundMode) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(REFUND_MODE, refundMode);
		editor.commit();
	}
	public static String getRefundMode(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(REFUND_MODE, "1");
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

	/*客户签到id*/
	public static void setTaskId(Context mContext, String CudId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(TASK_ID, CudId);
		editor.commit();
	}
	public static String getTaskId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(TASK_ID, "");
	}

	/*签到id	 如果签到则返回 缓存到本地*/
	public static void setCudId(Context mContext, String CudId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CUD_ID, CudId);
		editor.commit();
	}
	public static String getCudId(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CUD_ID, "");
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

	/*是否刷新签到店铺信息*/
	public static void setUpdateShop(Context mContext, String LoginDate) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(UPDATE_SHOP, LoginDate);
		editor.commit();
	}
	public static String getUpdateShop(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(UPDATE_SHOP, "0");
	}

	/*是否使用刷卡*/
//	public static void setSkType(Context mContext, String sk) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(skType, sk);
//		editor.commit();
//	}
//	public static String getSkType(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(skType, "N");
//	}

	/** 蓝牙对象bean */
	public static void setDeviceAddress(Context mContext, String deviceAddressStr) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(deviceAddress, deviceAddressStr);
		editor.commit();
	}
	public static String getDeviceAddress(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(deviceAddress, "");
	}


	/** 小票打印头部信息 */
	public static void setTicketmsgHead(Context mContext, String ticketmsg1) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(TicketmsgHead, ticketmsg1);
		editor.commit();
	}
	public static String getTicketmsgHead(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(TicketmsgHead, "");
	}

	/** 小票打印尾部信息 */
	public static void setTicketmsgBottom(Context mContext, String ticketmsg2) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(TicketmsgBottom, ticketmsg2);
		editor.commit();
	}
	public static String getTicketmsgBottom(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(TicketmsgBottom, "");
	}

	/** 现金日报打印方式 1.简单;2.精简;3.详细*/
	public static void setCashReportType(Context mContext, String cashReportType) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CashReportType, cashReportType);
		editor.commit();
	}
	public static String getCashReportType(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CashReportType, "1");
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

	/*登录用户名*/
//	public static void setUserName(Context mContext, String UserName) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(userName, UserName);
//		editor.commit();
//	}
//	public static String getUserName(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(userName, "");
//	}

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

	/**员工名*/
//	public static void setBudName(Context mContext, String budName) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(BUD_NAME, budName);
//		editor.commit();
//	}
//	public static String getBudName(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(BUD_NAME, "");
//	}

	/**员工名联系电话*/
//	public static void setMobile(Context mContext, String mobile) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(MOBILE, mobile);
//		editor.commit();
//	}
//	public static String getMobile(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(MOBILE, "");
//	}

	/**是否扫卡*/
//	public static void setIsScan(Context mContext, String IsScan) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(ISSCAN, IsScan);
//		editor.commit();
//	}
//	public static String getIsScan(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(ISSCAN, "2");//默认手动
//	}

	/**进店是否拍照*/
//	public static void setInPhoto(Context mContext, String inPhoto) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(ISPHOTO, inPhoto);
//		editor.commit();
//	}
//	public static String getInPhoto(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(ISPHOTO, "");
//	}

	/**离店是否拍照*/
//	public static void setOutPhoto(Context mContext, String isPhoto) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(OUTPHOTO, isPhoto);
//		editor.commit();
//	}
//	public static String getOutPhoto(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(OUTPHOTO, "");
//	}

	/**客户余额*/
	public static void setCustomerBalance(Context mContext, String customerBalance) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CUSTOMER_BALANCE, customerBalance);
		editor.commit();
	}
	public static String getCustomerBalance(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(CUSTOMER_BALANCE, null);
	}

	/**客户应收金额*/
	public static void setAfterAmount(Context mContext, String str) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(AFTER_AMOUNT, str);
		editor.commit();
	}
	public static String getAfterAmount(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(AFTER_AMOUNT, null);
	}

	/**客户安全欠款预警金额*/
	public static void setSafetyArrearsNum(Context mContext, String str) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(SAFETY_ARREARS_NUM, str);
		editor.commit();
	}
	public static String getSafetyArrearsNum(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(SAFETY_ARREARS_NUM, null);
	}

	/**默认定位距离*/
	public static void setSignDistance(Context mContext, int signDistance) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(SIGN_DISTANCE, signDistance);
		editor.commit();
	}
	public static int getSignDistance(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getInt(SIGN_DISTANCE, 1200);
	}


	/**是否允许变价*/
//	public static void setIsChangPrice(Context mContext, String isChangPrice) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(ISCHANGPRICE, isChangPrice);
//		editor.commit();
//	}
//	public static String getIsChangPrice(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(ISCHANGPRICE, "2");
//	}

	/**是否变价(退货）	1是2不是*/
//	public static void setIsChangeThPrice(Context mContext, String isChangeThPrice) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(ISCHANGETHPRICE, isChangeThPrice);
//		editor.commit();
//	}
//	public static String getIsChangeThPrice(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(ISCHANGETHPRICE, "2");
//	}

	/**打印机类型*/
	public static void setPtinterType(Context mContext, int ptinterType) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(PtinterType, ptinterType);
		editor.commit();
	}
	public static int getPtinterType(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getInt(PtinterType, BaseConfig.printer80);
	}


	/**
	 * 业务员关联的仓库id
	 * @param suStoreid
	 */
//	public static void setSuStoreid(Context mContext,String suStoreid) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		preferences.edit().putString(SU_STOREID, suStoreid).commit();
//	}
//	public static String getSuStoreid(Context mContext) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(SU_STOREID, "");
//	}

	/**
	 * 关联的业务员id
	 * @param bud_customerid
	 */
//	public static void setBudCustomerid(Context mContext,String bud_customerid) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		preferences.edit().putString(BUD_CUSTOMERID, bud_customerid).commit();
//	}
//	public static String getBudCustomerid(Context mContext) {
//		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return preferences.getString(BUD_CUSTOMERID, "");
//	}

	/**
	 * 选择的线路id
	 * @param lineId
	 */
	public static void setLineId(Context mContext,String lineId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(LINE_ID, lineId).commit();
	}
	public static String getLineId(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LINE_ID, "");
	}

	/**
	 * 选择的线路name
	 * @param lineName
	 */
	public static void setLineName(Context mContext,String lineName) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(LINE_NAME, lineName).commit();
	}
	public static String getLineName(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(LINE_NAME, "");
	}

	/**
	 * 选择的仓库name
	 * @param storeName
	 */
	public static void setStoreName(Context mContext,String storeName) {
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
	public static void setStoreId(Context mContext,String storeId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(STORE_ID, storeId).commit();
	}
	public static String getStoreId(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(STORE_ID, "");
	}


	/**
	 * 业务员id
	 * @param budId
	 */
	public static void setBudId(Context mContext,String budId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(BUD_ID, budId).commit();
	}
	public static String getBudId(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(BUD_ID, "");
	}


	/**
	 * 当前签到的顾客信息
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
	 * 当前签到的顾客信息
	 * @param bean
	 */
	public static void setClientInfo(Context mContext,ClientBean bean) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		if (bean != null) {
			String beanJson = JsonUtils.toJSONString(bean);
			LogUtils.e("beanJson",beanJson);
			preferences.edit().putString(ClientInfo, beanJson).commit();
		} else {
			preferences.edit().putString(ClientInfo, "").commit();
		}
	}
	public static ClientBean getClientInfo(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		String beanJson = preferences.getString(ClientInfo, "");
		ClientBean bean = JsonUtils.parseObject(beanJson,ClientBean.class);
		return bean;
	}

	/**
	 * 微信绑定unionId
	 *
	 * @param unionId
	 */
	public static void setUnionId(Context mContext,String unionId) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		preferences.edit().putString(UNION_ID, unionId).commit();
	}

	public static String getUnionId(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getString(UNION_ID, "");
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

	/** 记录当前的版本号 */
	public static void setNowVersion(Context mContext, int versioncode) {
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(NOW_VERSION, versioncode);
		editor.commit();
	}
	public static int getNowVersion(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return preferences.getInt(NOW_VERSION, 0);
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


}
