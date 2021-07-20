package com.ruoqian.brainidphoto.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 文件表
 */
@Entity(nameInDb = "search_record")
public class SearchRecord {

    @Id(autoincrement = true)//设置自增长
    @Property(nameInDb = "id")
    private Long id;

    @Index
    @Property(nameInDb = "keyWord")
    private String keyWord;

    @Property(nameInDb = "type") // 0证件照
    private int type = 0;

    @Property(nameInDb = "createTime")
    private Long createTime;

    @Generated(hash = 759676894)
    public SearchRecord(Long id, String keyWord, int type, Long createTime) {
        this.id = id;
        this.keyWord = keyWord;
        this.type = type;
        this.createTime = createTime;
    }

    @Generated(hash = 839789598)
    public SearchRecord() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
