package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

public class PlayBean extends BaseBean {
    private int TotalTime ;

    public int getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(int totalTime) {
        TotalTime = totalTime;
    }
}
