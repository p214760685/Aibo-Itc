package com.comtom.aibo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class PublicUtil {

    //获取版本渠道名称
    public static String getMetaDate(Context context){
        Object channel = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if(applicationInfo != null && applicationInfo.metaData != null){
                channel = applicationInfo.metaData.get("qudao");
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO: handle exception
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }
        if(channel == null){
            throw new RuntimeException("The name qudao is not defined in the manifest file's meta data");
        }
        Log.i("type",channel+"--->qudao");
        return channel.toString();
    }

    //调用函数缩小图片
    public static BitmapDrawable getNewDrawable(Activity context, int restId, int dstWidth, int dstHeight){
        Bitmap Bmp = BitmapFactory. decodeResource(
                context.getResources(), restId);
        Bitmap bmp = Bmp.createScaledBitmap(Bmp, dstWidth, dstHeight, true);
        BitmapDrawable d = new BitmapDrawable(bmp);
        Bitmap bitmap = d.getBitmap();
        if (bitmap.getDensity() == Bitmap.DENSITY_NONE) {
            d.setTargetDensity(context.getResources().getDisplayMetrics());
        }
        return d;
    }
}
