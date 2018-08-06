package com.huangr.waveviewdemo.utils;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by huangr on 2018/8/6.
 * ClassName  :
 * Description  :
 */


public class UIFactory {

    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }
}
