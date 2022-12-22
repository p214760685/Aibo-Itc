package com.comtom.aibo.httpset;

import android.content.Context;

import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.rxhttp.request.exception.ExceptionHandle;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;

public class HttpListen implements RxRequest.RequestListener {
    private  Context context ;
    private IDialog dialog ;

    public HttpListen(Context context){
        this.context = context ;
        dialog = DialogBuilder.loadingDialog0(context).create();
        dialog.getInnerDialog().setCanceledOnTouchOutside(false);//设置点击屏幕不消失
    }


    @Override
    public void onStart() {
        dialog.show();
    }

    @Override
    public void onError(ExceptionHandle handle) {
        if(handle.getMsg() != null)
            UtilToast.showToast(context,handle.getMsg());
    }

    @Override
    public void onFinish() {
        dialog.dismiss();
    }
}
