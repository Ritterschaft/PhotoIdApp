package com.ruoqian.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.longtu.base.util.StringUtils;
import com.ruoqian.lib.config.HttpConfig;

public class SharedUtils {
    private static SharedPreferences sharedPreferences;
    private final static String PROJECT = "RUOQIAN";

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 设置项目根地址
     */
    public static void setBaseUrl(Context context, String baseUrl) {
        sharedPreferences = context.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("baseUrl", baseUrl);
        editor.commit();
    }

    /**
     * 获取项目根地址
     */
    public static String getBaseUrl(Context context) {
        sharedPreferences = context.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
        String base_url = sharedPreferences.getString("baseUrl", HttpConfig.BASE_URL);
        return !StringUtils.isEmpty(base_url) ? base_url : HttpConfig.BASE_URL;
    }

    /**
     * 设置拍照攻略
     */
    public static void setStrategy(Context context, String strategy) {
        sharedPreferences = context.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("strategy", strategy);
        editor.commit();
    }

    /**
     * 获取拍照攻略
     */
    public static String getStrategy(Context context) {
        sharedPreferences = context.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
        String strategy = sharedPreferences.getString("strategy", null);
        return strategy;
    }
}
