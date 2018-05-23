package com.pufei.gxdt.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * toast的工具类
 * Created by wangwenzhang on 2017/4/19.
 */
public class ToastUtils {
    public static Toast mToast;
    public static void showShort(Context context, String text) {
        if (mToast == null){
            mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showShort(Context context, int text) {
        if (mToast == null){
            mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showLong(Context context, String text) {
        if (mToast == null){
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }else {
            mToast.setText(text);
        }
        mToast.show();
    }


}
