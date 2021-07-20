package com.ruoqian.lib.bean;

/**
 * 打印对象
 */
public class IdphotoPrintBean {

    private long id;
    private int pNum;
    private float pPrice;
    private String updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPNum() {
        return pNum;
    }

    public void setPNum(int pNum) {
        this.pNum = pNum;
    }

    public float getPPrice() {
        return pPrice;
    }

    public void setPPrice(float pPrice) {
        this.pPrice = pPrice;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
