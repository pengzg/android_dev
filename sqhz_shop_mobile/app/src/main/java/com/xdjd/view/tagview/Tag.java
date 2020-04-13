package com.xdjd.view.tagview;

import java.io.Serializable;

public class Tag implements Serializable {

	/**
	 * 
	 */

	// id
	private int id;
	// 标题
	private String title;
	// 背景色id
	private int backgroundResId;
	// 是否是最后一个
	private boolean isLast;
	// 字体颜色id
	private int textResId;
	// 是否选中
	private boolean isChecked;
	// 左边图片
	private int leftDrawableResId;
	// 右边图片
	private int rightDrawableResId;
	// 字体大小
	private float textSize;

	public Tag() {

	}

	public Tag(int paramInt, String paramString) {
		this.id = paramInt;
		this.title = paramString;
	}

	public Tag(int paramInt, String paramString, boolean paramBoolean,
			   int backgroundResId, int textResId) {
		this.id = paramInt;
		this.title = paramString;
		this.isLast = paramBoolean;
		this.backgroundResId = backgroundResId;
		this.textResId = textResId;
	}

	public int getBackgroundResId() {
		return this.backgroundResId;
	}

	public int getId() {
		return this.id;
	}

	public int getLeftDrawableResId() {
		return this.leftDrawableResId;
	}

	public int getRightDrawableResId() {
		return this.rightDrawableResId;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public void setBackgroundResId(int paramInt) {
		this.backgroundResId = paramInt;
	}

	public void setChecked(boolean paramBoolean) {
		this.isChecked = paramBoolean;
	}

	public void setId(int paramInt) {
		this.id = paramInt;
	}

	public void setLeftDrawableResId(int paramInt) {
		this.leftDrawableResId = paramInt;
	}

	public void setRightDrawableResId(int paramInt) {
		this.rightDrawableResId = paramInt;
	}

	public void setTitle(String paramString) {
		this.title = paramString;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public int getTextResId() {
		return textResId;
	}

	public void setTextResId(int textResId) {
		this.textResId = textResId;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

}
