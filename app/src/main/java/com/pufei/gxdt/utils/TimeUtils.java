package com.pufei.gxdt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wangwenzhang on 2017/6/8.
 */
public class TimeUtils {
    public static String longTime(long time) {
        String timer = null;
        if (time < 3600000) {
            if (time / 60000 == 0) {
                timer = "刚刚";
            } else {
                timer = time / 60000 + "分钟前";
            }
        } else if (3600000 <= time & time < 86400000) {
            timer = time / 3600000 + "小时前";
        } else {
            timer = time / 86400000 + "天前";
        }
        return timer;
    }

    public static int millionsToSeconds(int time) {
        time = time / 1000;
        return time;
    }

    public static String getDateToString(long milSecond, int type) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = format.format(date);
        try {
            Date d = format.parse(strDate);
            if(type == 0) {
                return getFriendlytime(d);
            }else if(type == 2) {
                return getBrowsingTime(d);
            }else {
                return getStudyDays(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStudyDays(Date d) {
        long delta = (new Date().getTime() - d.getTime()) / 1000;
        if (delta / (60 * 60 * 24) > 0) return delta / (60 * 60 * 24) + "";
        return "1";
    }

    public static String secToTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(time);
        return hms;
    }


    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }



    public static String getFriendlytime(Date d) {
        long delta = (new Date().getTime() - d.getTime()) / 1000;
        if (delta <= 0) return d.toLocaleString();
        if (delta / (60 * 60 * 24 * 365) > 0) return delta / (60 * 60 * 24 * 365) + "年前";
        if (delta / (60 * 60 * 24 * 30) > 0) return delta / (60 * 60 * 24 * 30) + "个月前";
        if (delta / (60 * 60 * 24 * 7) > 0) return delta / (60 * 60 * 24 * 7) + "周前";
        if (delta / (60 * 60 * 24) > 0) return delta / (60 * 60 * 24) + "天前";
        if (delta / (60 * 60) > 0) return delta / (60 * 60) + "小时前";
        if (delta / (60) > 0) return delta / (60) + "分钟前";
        return "刚刚";
    }

    public static String getBrowsingTime(Date d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long delta = (new Date().getTime() - d.getTime()) / 1000;
        if (delta / (60 * 60 * 24) == 1) return "昨天";
        if (delta / (60 * 60 * 24) > 0) return format.format(d);
        return "今天";
    }

    public static String dateToString(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(date);
    }

}
