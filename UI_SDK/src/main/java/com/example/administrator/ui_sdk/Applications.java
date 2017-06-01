package com.example.administrator.ui_sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2015/12/21.
 */
public class Applications extends Application{

    private ArrayList<Activity> list = new ArrayList<>();
    private static Applications instance;

    private Applications() {

    }

    /**
     * 单例模式中获取唯一的 Application
     */
    public static Applications getInstance() {
        if (null == instance) {
            instance = new Applications();
        }
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void addActivity(Activity activity) {
        list.add(activity);

//        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//                if (isBackground)
//                    Log.e("Ruan" , "-----------");
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//
//            }
//        });
    }

    /**
     * 销毁全部activity
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }

//    private boolean isBackground = false;
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        //判断是不是切换成后台
//        Log.e("Ruan" , "-----------");
//        if (level == TRIM_MEMORY_UI_HIDDEN){
//            isBackground = true;
//        }
//    }


    public String toString(){
        return list.toString();
    }

    public void removeActivity() {
        for (int i = list.size() - 1; i >= 1; i--) {
            Activity activity = list.get(i);
            activity.finish();
            list.remove(i);
        }
    }

    public void removeOneActivity(Activity activity) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == activity) {
                list.get(i).finish();
                list.remove(i);
            }
        }
    }

    public void removeOthers(Activity activity) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != activity) {
                list.get(i).finish();
                list.remove(i);
            }
        }
    }


    public ArrayList<Activity> getList(){
        return list;
    }

    /**
     * 判断该Activity有没有销毁
     *
     * @param activity
     * @return
     */
    public boolean getActivityOnline(Activity activity) {
        for (Activity activity1 : list) {
            if (activity1 == activity) {
                return true;
            }
        }
        return false;
    }
}
