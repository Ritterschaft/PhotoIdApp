package com.ruoqian.lib.http;

import retrofit2.Response;

/**
 *请求反馈接口
 */

public interface ReceiveListener<T> {
    void onSuccess(Response<T> response, int loading);
    void onFailure(String error, int loading);
}
