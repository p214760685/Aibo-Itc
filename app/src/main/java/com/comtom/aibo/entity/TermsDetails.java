package com.comtom.aibo.entity;

import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;

import java.io.Serializable;
import java.util.List;

public class TermsDetails extends BaseBean implements Serializable {

    private int ID;
    private String Name;
    private String IP;
    private int Status;
    private int WorkStatus;
    private int CallStatus;
    private int Sid;
    private int Number;
    private TermsDetails SessionInfo;
    private List<TermsDetails> Terms;
    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getWorkStatus() {
        return WorkStatus;
    }

    public void setWorkStatus(int workStatus) {
        WorkStatus = workStatus;
    }

    public int getCallStatus() {
        return CallStatus;
    }

    public void setCallStatus(int callStatus) {
        CallStatus = callStatus;
    }

    public int getSid() {
        return Sid;
    }

    public void setSid(int sid) {
        Sid = sid;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public TermsDetails getSessionInfo() {
        return SessionInfo;
    }

    public void setSessionInfo(TermsDetails sessionInfo) {
        SessionInfo = sessionInfo;
    }

    public List<TermsDetails> getTerms() {
        return Terms;
    }

    public void setTerms(List<TermsDetails> terms) {
        Terms = terms;
    }
}
