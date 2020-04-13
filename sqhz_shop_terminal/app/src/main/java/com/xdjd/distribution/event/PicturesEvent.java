package com.xdjd.distribution.event;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/30
 *     desc   : 传递店铺门头图片event
 *     version: 1.0
 * </pre>
 */

public class PicturesEvent {

    private String resultPath;//半截的url地址
    private String url;

    public PicturesEvent(String resultPath, String url) {
        this.resultPath = resultPath;
        this.url = url;
    }

    public String getResultPath() {
        return resultPath;
    }

    public String getUrl() {
        return url;
    }
}
