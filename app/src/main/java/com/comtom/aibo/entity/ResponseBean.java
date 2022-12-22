package com.comtom.aibo.entity;

import com.google.gson.annotations.SerializedName;
import com.haozi.dev.smartframe.rxhttp.request.base.BaseResponse;

/**
 * 描述：基础实体类（请求体最外层）
 */
public class ResponseBean<E> implements BaseResponse<E> {

    @SerializedName(value = "Ret", alternate = {"status"})
    private String code;
    @SerializedName(value = "data", alternate = {"result"})
    private E data;
    @SerializedName(value = "Remark", alternate = {"message"})
    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public E getData() {
        return data;
    }

    @Override
    public void setData(E data) {
        this.data = data;
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public void setMsg(String msg) {
        this.message = msg;
    }
}
