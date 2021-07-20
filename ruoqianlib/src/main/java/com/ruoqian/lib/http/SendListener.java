package com.ruoqian.lib.http;

import retrofit2.Call;

/**
 * 发送请求
 */

public interface SendListener<T> {
    void sendParams(Call<T> call, ReceiveListener listener, int loading);
}
