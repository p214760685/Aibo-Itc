package com.comtom.aibo.data;

import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.utils.TermsDetailsConverent;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.List;

@Entity
public class TermDb {

    @Id(autoincrement = true)
    private Long tmId ;//数据库id

    private String Name;//存储记录名称

    private int volume ;//音量

    @Convert(converter = TermsDetailsConverent.class,columnType =String.class)
    private List<TermsDetails> termsList;//存储终端集合


    @Generated(hash = 1317307521)
    public TermDb() {
    }

    @Generated(hash = 1978634925)
    public TermDb(Long tmId, String Name, int volume,
            List<TermsDetails> termsList) {
        this.tmId = tmId;
        this.Name = Name;
        this.volume = volume;
        this.termsList = termsList;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Long getTmId() {
        return this.tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }


    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public List<TermsDetails> getTermsList() {
        return termsList;
    }

    public void setTermsList(List<TermsDetails> termsList) {
        this.termsList = termsList;
    }
}
