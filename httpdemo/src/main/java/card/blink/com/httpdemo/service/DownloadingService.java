package card.blink.com.httpdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import card.blink.com.httpdemo.MainActivity;
import card.blink.com.httpdemo.activity.Task;
import card.blink.com.httpdemo.tool.Comment;
import card.blink.com.httpdemo.tool.DownloadCallback;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/5.
 */
public class DownloadingService extends Service {
    private static final String TAG = DownloadingService.class.getSimpleName();

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public DownloadingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DownloadingService.this;
        }
    }

    //--------------------------------------
    public static DownloadCallback downloadCallback;

    public static void setProgress(DownloadCallback downloadCallback) {
        DownloadingService.downloadCallback = downloadCallback;

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        String url = "http://192.168.16.1:8080/media/sdb1/jd.rar";
        downloadFile(url);
        String url2 = "http://192.168.16.1:8080/media/sdb1/myjd.rar";
        downloadFile(url2);
    }


    public void downloadFile(final String url) {
        Log.v(TAG, "downloadFile");

        String[] strings = url.split("/");
        final String name = strings[strings.length - 1];

        final Task task = new Task();
        task.name = name;
        Comment.list.add(task);

        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "jd.jar")//
                {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.i(TAG, "response===" + response.getAbsolutePath());
                        Log.v(TAG, "下载完成关闭服务");
                        if (MainActivity.tv != null) {
                            MainActivity.tv.setText("下载完毕");
                        }
                        DownloadingService.this.stopSelf();
                        Comment.list.remove(task);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        //Log.v(TAG, "inProgress===" + progress * 100);
//                        if (call != null) {
//                            call.progress((int) (progress * 100));
//                        }
                        if (MainActivity.tv != null) {
                            MainActivity.tv.setText("当前下载进度：" + progress * 100);
                        }
                        // 回调给传输界面
                        if (DownloadingService.downloadCallback != null) {
                            task.progress = progress * 100 + "";

                            DownloadingService.downloadCallback.progress(Comment.list);
                        }
                    }
                });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

//    public void setCall(DownloadingCallback call) {
//        this.call = call;
//    }
//
//    DownloadingCallback call;
//
//    public interface DownloadingCallback {
//        void progress(int t);
//    }


}
