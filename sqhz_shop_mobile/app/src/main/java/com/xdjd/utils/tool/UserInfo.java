package com.xdjd.utils.tool;

/**
 * 第三方登录用户信息类
 * @author lee
 *
 */
public class UserInfo {
	private String userIcon;  //图片信息
	private String userName; //用户昵称
	private Gender userGender; //性别
	private String userNote;  //用户id

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Gender getUserGender() {
		return userGender;
	}

	public void setUserGender(Gender userGender) {
		this.userGender = userGender;
	}

	public String getUserNote() {
		return userNote;
	}

	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}

	public static enum Gender {MALE, FEMALE}
	
}
