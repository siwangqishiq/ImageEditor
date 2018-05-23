package com.pufei.gxdt.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by wangwenzhang on 2016/12/2.
 */
public class AppInfo {
    private Drawable app_icon;
    //应用名称
    private String app_name;
    //应用版本号
    private String app_version;
    //应用包名
    private String packagename;
    //是否是用户app
    private boolean isUserApp;


    public AppInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    public AppInfo(Drawable app_icon, String app_name, String app_version,
                   String packagename) {
        super();
        this.app_icon = app_icon;
        this.app_name = app_name;
        this.app_version = app_version;
        this.packagename = packagename;
    }
    public Drawable getApp_icon() {
        return app_icon;
    }
    public void setApp_icon(Drawable app_icon) {
        this.app_icon = app_icon;
    }
    public String getApp_name() {
        return app_name;
    }
    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }
    public String getApp_version() {
        return app_version;
    }
    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }
    public String getPackagename() {
        return packagename;
    }
    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }


}
