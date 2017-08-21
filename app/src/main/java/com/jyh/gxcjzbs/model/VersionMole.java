package com.jyh.gxcjzbs.model;

/**
 * Created by Administrator on 2017/8/21.
 */

public class VersionMole {

    /**
     * versionCode : 1
     * versionName : 1.0
     * url :
     * description : 直播室1.0
     */

    private int versionCode;
    private String versionName;
    private String url;
    private String description;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
