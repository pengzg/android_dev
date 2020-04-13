package com.xdjd.storebox.bean;

/**
 * 版本更新bean
 * Created by lijipei on 2016/12/19.
 */

public class NewVersionBean extends BaseBean {

    private String flag;//	是否需要更新	1-强制更新 2-可以不更新 3-不需要更新  (自定义type:0--如果是0提示是安装的提示)
    private String downloadUrl;//	下载地址
    private String newVersion;//	最新版本
    private String newVersionName; //最新版本名称
    private String updateContent;//	更新内容

    public String getNewVersionName() {

        return newVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        this.newVersionName = newVersionName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
