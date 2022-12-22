package com.comtom.aibo.utils;

import android.content.Context;

import com.haozi.dev.smartframe.utils.tool.UtilLog;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class MyFtpListener implements FTPDataTransferListener {
    private Context context ;
    private IDialog iDialog ;

    public MyFtpListener(Context context){
        this.context = context;
        iDialog = DialogBuilder.loadingDialog0(context).create();
    }

    @Override
    public void started() {
        iDialog.show();
    }

    @Override
    public void transferred(int i) {
    }

    @Override
    public void completed() {
        iDialog.dismiss();
    }

    @Override
    public void aborted() {
    }

    @Override
    public void failed() {
        UtilToast.showToast(context,"上传出错，请重试");
    }
}
