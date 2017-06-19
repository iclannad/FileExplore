package card.blink.com.fileexplore.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import java.util.ArrayList;
import java.util.List;

import card.blink.com.fileexplore.model.UploadTask;
import card.blink.com.fileexplore.tools.Comment;
import card.blink.com.fileexplore.tools.FileTransportUtils;
import card.blink.com.fileexplore.upload.UploadManager;

/**
 * 作用：文件传输的服务，可以在后台下载或者上传文件
 */
public class UploadService extends Service implements UploadTaskCallback{


    private static final String TAG = UploadService.class.getSimpleName();
    public static boolean isUploading = false;

    private static UploadManager UPLOAD_MANAGER;

    /**
     * start 方式开启服务，保存全局的上传管理对象
     */
    public static UploadManager getUploadManager() {
        Context context = OkGo.getContext();
//        if (!UploadService.isServiceRunning(context))
//            context.startService(new Intent(context, UploadService.class));
        if (UploadService.UPLOAD_MANAGER == null)
            UploadService.UPLOAD_MANAGER = UploadManager.getInstance();
        return UPLOAD_MANAGER;
    }

    public static boolean isServiceRunning(Context context) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() == 0) return false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(DownloadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public void startTask() {
        ArrayList<UploadTask> allUploadTasks = UploadManager.getInstance().getAllTask();
        for (int i = 0; i < allUploadTasks.size(); i++) {
            UploadTask uploadTask = allUploadTasks.get(i);
            if (uploadTask.status == UploadManager.WAIT) {
                Log.v(TAG, "有任务需要开始 task: " + uploadTask.name);
                if (uploadTask.switch_status == UploadManager.NONE_TO_WAIT
                        || uploadTask.switch_status == UploadManager.PAUSE_TO_WAIT) {
                    uploadTask.status = UploadManager.RUNING;
                    uploadTask.switch_status = UploadManager.WAIT_TO_RUNNING;
                    uploadTask.uploadTaskCallback = this;
                    FileTransportUtils.uploadFileToExternalStorage(uploadTask);
                }
                return;
            }
        }
        Log.v(TAG, "所有等待中的任务都开启完毕，此时可以关闭服务和清空任和列表");
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        startTask();
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


    /**
     * 任务上传开始前
     */
    @Override
    public void start() {

    }

    /**
     * 任务上传中
     *
     * @param index 表示当前已经上传了第几块
     * @param total 表示总共有几块
     */
    @Override
    public void uploading(int index, int total) {

    }

    /**
     * 任务上传结束
     */
    @Override
    public void finished(UploadTask uploadTask) {
        startTask();
    }
}
