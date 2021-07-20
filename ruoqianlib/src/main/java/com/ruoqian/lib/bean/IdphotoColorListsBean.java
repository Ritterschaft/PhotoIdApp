package com.ruoqian.lib.bean;

import java.util.List;

public class IdphotoColorListsBean {
    private int stateCode;
    private String ms;
    private List<IdphotoColorBean> data;

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public List<IdphotoColorBean> getData() {
        return data;
    }

    public void setData(List<IdphotoColorBean> data) {
        this.data = data;
    }
}
