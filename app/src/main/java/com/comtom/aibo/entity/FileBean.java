package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

public class FileBean extends BaseBean {
    private String FileId ;
    private String FtpUrl ;
    private String FtpUsr ;
    private String FtpPwd ;

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    public String getFtpUrl() {
        return FtpUrl;
    }

    public void setFtpUrl(String ftpUrl) {
        FtpUrl = ftpUrl;
    }

    public String getFtpUsr() {
        return FtpUsr;
    }

    public void setFtpUsr(String ftpUsr) {
        FtpUsr = ftpUsr;
    }

    public String getFtpPwd() {
        return FtpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        FtpPwd = ftpPwd;
    }
}
