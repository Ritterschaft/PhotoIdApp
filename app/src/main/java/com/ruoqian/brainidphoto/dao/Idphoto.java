package com.ruoqian.brainidphoto.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "idphoto")
public class Idphoto {
    @Id(autoincrement = true)//设置自增长
    @Property(nameInDb = "id")
    private Long id;

    @Index
    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "mWidth")
    private Integer mWidth;

    @Property(nameInDb = "mHeight")
    private Integer mHeight;

    @Property(nameInDb = "pWidth")
    private Integer pWidth;

    @Property(nameInDb = "pHeight")
    private Integer pHeight;

    @Property(nameInDb = "dpi")
    private Integer dpi;

    @Property(nameInDb = "price")
    private float price;

    @Property(nameInDb = "ephoto")
    private Integer ephoto;

    @Property(nameInDb = "typesetting")
    private Integer typesetting;

    @Property(nameInDb = "bgColor")
    private String bgColor;

    @Property(nameInDb = "fileSize")
    private String fileSize;

    @Property(nameInDb = "fileFormat")
    private String fileFormat;

    @Property(nameInDb = "isPrint")
    private Integer isPrint;

    @Property(nameInDb = "pId")
    private Integer pId;

    @Property(nameInDb = "createTime")
    private Long createTime;

    @Property(nameInDb = "recommendTime")
    private Long recommendTime;

    @Property(nameInDb = "cloudUpdateTime")
    private Long cloudUpdateTime;

    @Index
    @Property(nameInDb = "cloudId")
    private Long cloudId;

    @Generated(hash = 1437820478)
    public Idphoto(Long id, String name, Integer mWidth, Integer mHeight,
            Integer pWidth, Integer pHeight, Integer dpi, float price,
            Integer ephoto, Integer typesetting, String bgColor, String fileSize,
            String fileFormat, Integer isPrint, Integer pId, Long createTime,
            Long recommendTime, Long cloudUpdateTime, Long cloudId) {
        this.id = id;
        this.name = name;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        this.dpi = dpi;
        this.price = price;
        this.ephoto = ephoto;
        this.typesetting = typesetting;
        this.bgColor = bgColor;
        this.fileSize = fileSize;
        this.fileFormat = fileFormat;
        this.isPrint = isPrint;
        this.pId = pId;
        this.createTime = createTime;
        this.recommendTime = recommendTime;
        this.cloudUpdateTime = cloudUpdateTime;
        this.cloudId = cloudId;
    }

    @Generated(hash = 116629368)
    public Idphoto() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMWidth() {
        return this.mWidth;
    }

    public void setMWidth(Integer mWidth) {
        this.mWidth = mWidth;
    }

    public Integer getMHeight() {
        return this.mHeight;
    }

    public void setMHeight(Integer mHeight) {
        this.mHeight = mHeight;
    }

    public Integer getPWidth() {
        return this.pWidth;
    }

    public void setPWidth(Integer pWidth) {
        this.pWidth = pWidth;
    }

    public Integer getPHeight() {
        return this.pHeight;
    }

    public void setPHeight(Integer pHeight) {
        this.pHeight = pHeight;
    }

    public Integer getDpi() {
        return this.dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getEphoto() {
        return this.ephoto;
    }

    public void setEphoto(Integer ephoto) {
        this.ephoto = ephoto;
    }

    public Integer getTypesetting() {
        return this.typesetting;
    }

    public void setTypesetting(Integer typesetting) {
        this.typesetting = typesetting;
    }

    public String getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Integer getIsPrint() {
        return this.isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    public Integer getPId() {
        return this.pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getRecommendTime() {
        return this.recommendTime;
    }

    public void setRecommendTime(Long recommendTime) {
        this.recommendTime = recommendTime;
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
