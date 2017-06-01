package com.example.administrator.ui_sdk.View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.ui_sdk.Other.SystemBarTintManager;


/**
 * Created by Administrator on 2015/12/22.
 */
public class SystemManager {
    protected static SystemBarTintManager mTintManager;

    public static boolean TitleStatc = true;

    // 设置沉寂式状态栏
    public static void setTitleBarColor(int resid, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(TitleStatc, activity);
        }
        mTintManager = new SystemBarTintManager(activity);
        // 设置颜色
        // mTintManager.setStatusBarDarkMode(true, this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintColor(resid);
    }


    /**
     * 设置沉寂式状态栏
     *
     * @param view
     * @param activity
     */
    public static void setWindowColor(View view, Activity activity) {
        //判断android版本大于4.4才能执行沉寂式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                ViewColor(view, activity);
            } catch (Exception e) {
                Log.e("Activity" + activity, "设置的沉寂式状态栏没有背景颜色，请设置背景颜色");
            }
        }
    }

    @SuppressLint("InlinedApi")
    public static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 设置状态栏的颜色
     * 此方法是获取组件的背景颜色
     * 该方法为私有，现在仅提供内部使用
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void ViewColor(View view, Activity activity) {
        int resid = 0;
        try {
            //获取组件的背景颜色
            Drawable drawable = view.getBackground();
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            resid = colorDrawable.getColor();
        } catch (Exception e) {
//            resid = R.color.Black;
            Log.e("Ruan", "该组件没有背景颜色所以沉寂式状态设置不成功 , 请检查是否拥有背景颜色");
        }
        //将背景颜色设置成为沉寂式状态栏
        setTitleBarColor(resid , activity);
    }
}
