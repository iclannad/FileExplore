package card.blink.com.fileexplore.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 作用：文件传输的服务，可以在后台下载或者上传文件
 */
public class FileTransportService extends Service {


    private static final String TAG = FileTransportService.class.getSimpleName();

    public static boolean isRunningTask = false;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        // 管理任务列表的线程


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");

        return null;
    }


    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }


}
