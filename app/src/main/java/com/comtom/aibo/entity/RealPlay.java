package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

public class RealPlay extends BaseBean {
    private int Sid;
    private int DataPort;
    private String DataIP;

    public int getSid() {
        return Sid;
    }

    public void setSid(int sid) {
        Sid = sid;
    }

    public int getDataPort() {
        return DataPort;
    }

    public void setDataPort(int dataPort) {
        DataPort = dataPort;
    }

    public String getDataIP() {
        return DataIP;
    }

    public void setDataIP(String dataIP) {
        DataIP = dataIP;
    }
}
