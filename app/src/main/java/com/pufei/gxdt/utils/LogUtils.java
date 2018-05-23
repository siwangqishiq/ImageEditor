package com.pufei.gxdt.utils;

import android.util.Log;

/**
 * log的工具类
 * 创建者： wangwenzhang 时间： 2017/11/10.
 */

public class LogUtils {
    public static boolean isShow;
    public static void i(String title, String vulue){
        if (isShow)
        Log.i(title,vulue);
    }
    public static void d(String title, String vulue){
        if (isShow)
            Log.d(title,vulue);
    }
    public static void e(String title, String vulue){
        if (isShow)
            Log.d(title,vulue);
    }
}
