package com.ruoqian.lib.bean;

import java.util.List;

public class IdphotoPrintListsBean {
    private int stateCode;
    private String msg;
    private List<IdphotoPrintBean> data;

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

    public List<IdphotoPrintBean> getData() {
        return data;
    }

    public void setData(List<IdphotoPrintBean> data) {
        this.data = data;
    }
}
