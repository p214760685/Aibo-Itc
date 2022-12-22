package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

public class HandleBean extends BaseBean {

    private int Handle;//创建媒体库返回的任务句柄
    private int Status;//0-未完成创建 1-已完成创建

    public int getHandle() {
        return Handle;
    }

    public void setHandle(int handle) {
        Handle = handle;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
