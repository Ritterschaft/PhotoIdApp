package com.ruoqian.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期转时间戳
     */
    public static long dateToStamp(String dateStr) {
        try {
            Date date = simpleDateFormat.parse(dateStr);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前时间
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前日期
     */
    public static String getCurrentDate() {
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }
}
