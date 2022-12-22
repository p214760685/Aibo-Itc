package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

public class TermIDBean extends BaseBean {
    private int[] TermIds;

    public int[] getTermIds() {
        return TermIds;
    }

    public void setTermIds(int[] termIds) {
        TermIds = termIds;
    }
}
