package com.ruoqian.lib.http;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Retrofit请求
 */
public class RetrofitModel<T> implements SendListener {

    /**
     * 发送请求
     * @param call
     * @param listener
     */
    @Override
    public void sendParams(Call call, final ReceiveListener listener, final int loading) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {//请求成功
                if (listener!=null){
                    listener.onSuccess(response,loading);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {//请求失败
                t.printStackTrace();
                if (listener!=null){
                    listener.onFailure(t.getMessage(),loading);
                }
            }
        });
    }
}
