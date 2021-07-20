package com.ruoqian.lib.bean;

import java.util.List;

/**
 *证件照列表
 */
public class IdphotoListsBean {
    private int stateCode;
    private String msg;
    private List<IdphotoBean> data;

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<IdphotoBean> getData() {
        return data;
    }

    public void setData(List<IdphotoBean> data) {
        this.data = data;
    }
}
