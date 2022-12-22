package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

public class LoginBean extends BaseBean {
    private String JSessionID ;

    public String getJSessionID() {
        return JSessionID;
    }

    public void setJSessionID(String JSessionID) {
        this.JSessionID = JSessionID;
    }
}
