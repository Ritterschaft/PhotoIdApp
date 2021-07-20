package com.ruoqian.lib.api;

import com.ruoqian.lib.bean.IdphotoPrintListsBean;
import com.ruoqian.lib.bean.IdphotoColorListsBean;
import com.ruoqian.lib.bean.IdphotoListsBean;
import com.ruoqian.lib.bean.IdphotoStrategyBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 接口请求映射
 */

public interface ApiAskService {

    /**
     * 证件照推荐列表
     */
    @GET("v1/idphoto/recommend/lists")
    Call<IdphotoListsBean> idphotoRecommends();

    /**
     * 证件照列表
     */
    @GET("v1/idphoto/lists")
    Call<IdphotoListsBean> idphotoLists();

    /**
     * 打印版面列表
     */
    @GET("v1/idphoto/print/lists")
    Call<IdphotoPrintListsBean> idphotoPrintLists();

    /**
     * 证件照颜色列表
     */
    @GET("v1/idphoto/color/lists")
    Call<IdphotoColorListsBean> idphotoColorLists();

    /**
     * 拍照攻略集合
     */
    @GET("v1/idphoto/camera/lists")
    Call<IdphotoStrategyBean> idphotoStrategys();
}
