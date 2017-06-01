package com.example.administrator.ui_sdk;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Ruanjiahui on 2016/1/11.
 */
public class DensityUtil {

    /**
     * 获取手机屏幕大小
     */
    private static int getPhone(Context context, int code) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (code == 0)
            return width;
        return height;
    }

    public static int getWidth(Context context) {
        return getPhone(context, 0);
    }

    public static int getHeight(Context context) {
        return getPhone(context, 1);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置组件的高度
     *
     * @param view
     * @param height
     */
    public static void setHeight(View view, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = height;
        view.setLayoutParams(params);
    }

    /**
     * 设置组件的高度
     *
     * @param view
     * @param width
     */
    public static void setWidth(View view, int width) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = width;
        view.setLayoutParams(params);
    }

    /**
     * 设置组件的高度
     *
     * @param view
     * @param height
     */
    public static void setItemHeight(View view, int height) {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = height;
        view.setLayoutParams(params);
    }


    /**
     * 设置Relayou布局的高度
     *
     * @param view
     * @param height
     */
    public static void setRelHeight(View view, int height) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.height = height;
        view.setLayoutParams(params);
    }


    public static void setRelHeight(View view, int height, int[] rules) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.height = height;
        for (int rule : rules)
            params.addRule(rule);
        view.setLayoutParams(params);
    }

    /**
     * 设置Relayou布局的高度
     *
     * @param view
     * @param width
     */
    public static void setRelWidth(View view, int width) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = width;
        view.setLayoutParams(params);
    }


    public static void setAbsSize(View view, int width, int height) {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }


    public static void setLinearSize(View view, int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }


    public static void setDrawSize(View view, int width, int height) {
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        params.gravity = Gravity.START;
        view.setLayoutParams(params);
    }


    public static void setFrameSize(View view, int width, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }


    public static void setRelayoutSize(View view, int width, int height, int Top, int Left, int Bottom, int Right) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        params.setMargins(Left, Top, Right, Bottom);
        view.setLayoutParams(params);
    }


    public static void setRelayoutSize(View view, int width, int height, int Top, int Left, int Bottom, int Right, int[] rules) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        params.setMargins(Left, Top, Right, Bottom);
        for (int rule : rules)
            params.addRule(rule);
        view.setLayoutParams(params);
    }

    public static void setRelatSize(View view, int width, int height) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public static void setRelatSize(View view, int width, int height, int rule) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.height = height;
        params.addRule(rule);
        view.setLayoutParams(params);
    }


    public static void setLinearSize(View view, int width, int height, int up, int down, int left, int right) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.setMargins(left, up, right, down);
        params.height = height;
        view.setLayoutParams(params);
    }

}
