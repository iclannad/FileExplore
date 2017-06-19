package card.blink.com.dbdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/6/19.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        GreenDaoManager.getInstance();
    }

    public static Context getContext() {
        return mContext;
    }
}