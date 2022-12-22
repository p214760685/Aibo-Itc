package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

import java.util.List;

public class ProgInfoBean extends BaseBean {
    private String ID;
    private int Type;
    private int Length;
    private String Name;
    private boolean flag = false;
    private List<ProgInfoBean> Items;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<ProgInfoBean> getItems() {
        return Items;
    }

    public void setItems(List<ProgInfoBean> items) {
        Items = items;
    }
}
