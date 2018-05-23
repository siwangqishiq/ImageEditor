package com.pufei.gxdt.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wangwenzhang on 2017/1/9.
 */
public class KeyUtil {
    public KeyUtil() {
    }

    public static JSONObject getJson(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);//获取用户应用安装列表
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String timestamp = Long.toString(System.currentTimeMillis());
        String key = "h32nfow45e";
        String vresion = pi.versionName;//获取版本号
        String os = "1";
        String deviceid =null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            deviceid="";
        }
        //deviceid = TelephonyMgr.getDeviceId();//获取唯一辨识码
        deviceid = "123456789";
        String sign = md5(md5(timestamp + key + vresion));
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("sign", sign);
            jsonObject.put("key", key);
            jsonObject.put("deviceid", deviceid);
            jsonObject.put("os", os);
            jsonObject.put("version", vresion);
            jsonObject.put("timestamp", timestamp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    public static String md5(String string) {//MD5加密
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
