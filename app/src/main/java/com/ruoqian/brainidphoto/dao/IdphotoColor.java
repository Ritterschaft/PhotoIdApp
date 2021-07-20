package com.ruoqian.brainidphoto.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "idphoto_color")
public class IdphotoColor {
    @Id(autoincrement = true)//设置自增长
    @Property(nameInDb = "id")
    private Long id;


    @Property(nameInDb = "val")
    private String val;

    @Property(nameInDb = "createTime")
    private Long createTime;

    @Property(nameInDb = "cloudUpdateTime")
    private Long cloudUpdateTime;

    @Index
    @Property(nameInDb = "cloudId")
    private Long cloudId;

    @Generated(hash = 1688616584)
    public IdphotoColor(Long id, String val, Long createTime, Long cloudUpdateTime,
            Long cloudId) {
        this.id = id;
        this.val = val;
        this.createTime = createTime;
        this.cloudUpdateTime = cloudUpdateTime;
        this.cloudId = cloudId;
    }

    @Generated(hash = 1229310801)
    public IdphotoColor() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVal() {
        return this.val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCloudId() {
        return this.cloudId;
    }

    public void setCloudId(Long cloudId) {
        this.cloudId = cloudId;
    }

    public Long getCloudUpdateTime() {
        return this.cloudUpdateTime;
    }

    public void setCloudUpdateTime(Long cloudUpdateTime) {
        this.cloudUpdateTime = cloudUpdateTime;
    }
}
