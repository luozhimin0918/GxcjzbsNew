package com.jyh.gxcjzbs.model;



public class RedPointBean {
    private String uid;
    private String isShow;

    public RedPointBean() {
    }

    public RedPointBean(String uid, String isShow) {

        this.uid = uid;
        this.isShow = isShow;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
