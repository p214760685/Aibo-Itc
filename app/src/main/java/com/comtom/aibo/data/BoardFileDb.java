package com.comtom.aibo.data;

import com.comtom.aibo.entity.ProgInfoBean;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.utils.MusicConverent;
import com.comtom.aibo.utils.TermsDetailsConverent;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BoardFileDb {

    @Id(autoincrement = true)
    private Long boardId ;//数据库id

    private String name;//任务名称

    private int Sid;//文件广播会话ID

    private int Status;// 状态：0-stop1-play2-pause

    private int volume ;//音量

    private int type ;//播放模式 1：顺序播放，2：循环播放，3：随机播放


    @Convert(converter = TermsDetailsConverent.class,columnType =String.class)
    private List<TermsDetails> termsList;//存储终端集合

    @Convert(converter = MusicConverent.class,columnType =String.class)
    private List<ProgInfoBean> musicList;//存储曲目集合

    @Generated(hash = 228688665)
    public BoardFileDb(Long boardId, String name, int Sid, int Status, int volume,
            int type, List<TermsDetails> termsList, List<ProgInfoBean> musicList) {
        this.boardId = boardId;
        this.name = name;
        this.Sid = Sid;
        this.Status = Status;
        this.volume = volume;
        this.type = type;
        this.termsList = termsList;
        this.musicList = musicList;
    }

    @Generated(hash = 343682974)
    public BoardFileDb() {
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSid() {
        return this.Sid;
    }

    public void setSid(int Sid) {
        this.Sid = Sid;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public List<TermsDetails> getTermsList() {
        return this.termsList;
    }

    public void setTermsList(List<TermsDetails> termsList) {
        this.termsList = termsList;
    }

    public List<ProgInfoBean> getMusicList() {
        return this.musicList;
    }

    public void setMusicList(List<ProgInfoBean> musicList) {
        this.musicList = musicList;
    }

}
