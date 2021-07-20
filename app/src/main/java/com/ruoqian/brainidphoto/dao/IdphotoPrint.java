package com.ruoqian.brainidphoto.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "idphoto_print")
public class IdphotoPrint {
    @Id(autoincrement = true)//设置自增长
    @Property(nameInDb = "id")
    private Long id;


    @Property(nameInDb = "pNum")
    private Integer pNum;

    @Property(nameInDb = "pPrice")
    private float pPrice;

    @Property(nameInDb = "createTime")
    private Long createTime;

    @Property(nameInDb = "cloudUpdateTime")
    private Long cloudUpdateTime;

    @Index
    @Property(nameInDb = "cloudId")
    private Long cloudId;

    @Generated(hash = 1830279655)
    public IdphotoPrint(Long id, Integer pNum, float pPrice, Long createTime,
            Long cloudUpdateTime, Long cloudId) {
        this.id = id;
        this.pNum = pNum;
        this.pPrice = pPrice;
        this.createTime = createTime;
        this.cloudUpdateTime = cloudUpdateTime;
        this.cloudId = cloudId;
    }

    @Generated(hash = 787251075)
    public IdphotoPrint() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPNum() {
        return this.pNum;
    }

    public void setPNum(Integer pNum) {
        this.pNum = pNum;
    }

    public float getPPrice() {
        return this.pPrice;
    }

    public void setPPrice(float pPrice) {
        this.pPrice = pPrice;
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
