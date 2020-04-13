package com.xdjd.distribution.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/8
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ImagePathBean extends BaseBean implements Serializable{

    private String imgPath;//	图片地址返回

    //-------自定参数------
    private String sdPath;//本地路径
    private Bitmap bmpImg;//图片bitmap
    private String isUploadSuccess;//是否上传图片成功,1.成功;2.失败;

    public String getSdPath() {
        return sdPath;
    }

    public void setSdPath(String sdPath) {
        this.sdPath = sdPath;
    }

    public Bitmap getBmpImg() {
        return bmpImg;
    }

    public void setBmpImg(Bitmap bmpImg) {
        this.bmpImg = bmpImg;
    }

    public String getIsUploadSuccess() {
        return isUploadSuccess;
    }

    public void setIsUploadSuccess(String isUploadSuccess) {
        this.isUploadSuccess = isUploadSuccess;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
