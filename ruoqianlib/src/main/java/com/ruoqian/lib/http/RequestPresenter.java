package com.ruoqian.lib.http;

import retrofit2.Call;

/**
 * 请求控制
 */

public class RequestPresenter<T> {
    private SendListener sendListener;
    private ReceiveListener receiveListener;

    public RequestPresenter(ReceiveListener receiveListener){
        this.receiveListener=receiveListener;
        sendListener=new RetrofitModel();
    }

    public void sendParams(Call<T> call, int loading){
        sendListener.sendParams(call,receiveListener,loading);
    }
}
