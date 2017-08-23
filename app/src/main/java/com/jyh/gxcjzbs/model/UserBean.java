package com.jyh.gxcjzbs.model;



public class UserBean {
    private String name;
    private String uid;
    private String rid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public UserBean() {
    }

    public UserBean(String name, String uid, String rid) {

        this.name = name;
        this.uid = uid;
        this.rid = rid;
    }
}
