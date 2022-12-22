package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class GroupBean extends BaseBean {
    private List<GroupBean> Groups;
    private String ID;
    private String Name;
    private int Number;
    private int[] Tids ;
    private TermsDetails termsDetail;
    private List<TermsDetails> termsDetails ;
    private List<ProgInfoBean> Items;
    private boolean isOpen = false;
    private boolean flag = false;

    public GroupBean(){
        Items = new ArrayList<>();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public TermsDetails getTermsDetail() {
        return termsDetail;
    }

    public void setTermsDetail(TermsDetails termsDetail) {
        this.termsDetail = termsDetail;
    }

    public List<TermsDetails> getTermsDetails() {
        return termsDetails;
    }

    public void setTermsDetails(List<TermsDetails> termsDetails) {
        this.termsDetails = termsDetails;
    }

    public List<GroupBean> getGroups() {
        return Groups;
    }

    public void setGroups(List<GroupBean> groups) {
        Groups = groups;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public int[] getTids() {
        return Tids;
    }

    public void setTids(int[] tids) {
        Tids = tids;
    }

    public List<ProgInfoBean> getItems() {
        return Items;
    }

    public void setItems(List<ProgInfoBean> items) {
        Items = items;
    }
}
