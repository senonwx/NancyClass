package com.example.senon.nancyclass.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 公共util
 */
public class ComUtil {

    // 屏幕宽度（像素）
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
