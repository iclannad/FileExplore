package card.blink.com.httpdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import card.blink.com.httpdemo.MainActivity;
import card.blink.com.httpdemo.activity.Task;
import card.blink.com.httpdemo.tool.Comment;
import card.blink.com.httpdemo.tool.DownloadCallback;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/6/5.
 */
public class DownloadingService extends Service {
    private static final String TAG = DownloadingService.class.getSimpleName();
    private static final int UPLOADING = 1;

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

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOADING:
                    // 回调给传输界面
                    if (DownloadingService.downloadCallback != null) {
                        DownloadingService.downloadCallback.progress(Comment.list);
                    }
                    break;
                default:
                    Log.i(TAG, "default");
                    break;
            }
        }
    };


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
        String url2 = "http://192.168.16.1:8080/media/sdb1/myjd5.rar";
        downloadFile(url2);

        String name = "jd.rar";
        uploadFile(name);
        String name2 = "myjd.rar";
        uploadFile(name2);
    }

    public void uploadFile(String name) {
        uploadBigFile(name);
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
                        //DownloadingService.this.stopSelf();
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

    public void uploadBigFile(String name) {
        Log.v(TAG, "上传大文件");
        final File file = new File(Environment.getExternalStorageDirectory().toString(), name);
        String fileAbsolutePath = file.getAbsolutePath();
        Log.i(TAG, "fileAbsolutePath==" + fileAbsolutePath);
        long length = file.length();
        Log.i(TAG, "length==" + length);

        new Thread() {
            @Override
            public void run() {
                postUploadBigFileRequest(file);
            }
        }.start();
        //postUploadBigFileRequest(file);

        //btn9();
        //btn10();
        //btn11();

    }

    /**
     * 上传大文件 子线程
     *
     * @param file
     */
    private void postUploadBigFileRequest(File file) {
        final int SIZE = 5 * 1024 * 1024;
        long length = file.length();
        Log.v(TAG, "jd.rar的长度 length==" + length);
        long count = length / SIZE;

        if (length % SIZE == 0) {

        } else {
            count++;
        }
        Log.v(TAG, "需要分割成的块数是 count===" + count);

        Task task = new Task();
        String name = file.getName();
        task.name = name;
        Comment.list.add(task);

        int index = 1;
        while (index <= count) {
            // 请求下载index块
            Log.v(TAG, "index==" + index + "---count==" + count);
            uploading(file, index, count);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double jd = (double) index / (double) count;
            int progress = (int) (jd * 100);
            task.progress = progress + "";
            Log.i(TAG, "progress==" + progress);
            if (index == count) {
                Log.v(TAG, "上传完毕");
                Comment.list.remove(task);
            }

            Message msg = Message.obtain();
            msg.what = UPLOADING;
            handler.sendMessage(msg);

            index++;
        }

    }

    private void uploading(File file, int index, long count) {
        byte[] content = getDataFromLocalBigFile(file, index, count);
        Log.i(TAG, "btn");
        OkHttpClient client = new OkHttpClient();

        // 生成body
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, content);

        Request request = new Request.Builder()
                .url("http://192.168.16.1:8080/cgi-bin/upload_files.cgi")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("authorization", "Basic YWRtaW46YWRtaW4=")
                .addHeader("Content-Length", content.length + "")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.v(TAG, "response==" + response.toString());
            int code = response.code();
            Log.i(TAG, "code===" + code);

            ResponseBody responseBody = response.body();
            Log.v(TAG, "responseBody==" + responseBody.toString());
            String string = responseBody.string();
            Log.v(TAG, "responseBody.string()==" + string);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] getDataFromLocalBigFile(File file, int index, long count) {
        long length = file.length();
        String name = file.getName();
        Log.v(TAG, "getDataFromLocalBigFile name==" + name);

        // empty
        byte[] postmsg = new byte[280];

        //String path = "/sdb1/mytest/7.rar";
        String path = "/sdb1/mytest/" + "2" + name;

        byte[] pathBytes = path.getBytes();
        // 填充路径
        for (int i = 0; i < pathBytes.length; i++) {
            postmsg[i] = pathBytes[i];
        }
        Log.i(TAG, "postmsg===" + Arrays.toString(postmsg));

        long block = index;
        long blocksize = count;
        long filesize = length;

        byte[] indexs = longToBytes(index);
        byte[] counts = longToBytes(count);
        byte[] filesizes = longToBytes(filesize);

        //Log.v(TAG, "indexs==" + Arrays.toString(indexs));
        indexs = MySort(indexs);
        //Log.v(TAG, "indexs==" + Arrays.toString(indexs));

        //Log.v(TAG, "counts==" + Arrays.toString(counts));
        counts = MySort(counts);
        //Log.v(TAG, "counts==" + Arrays.toString(counts));

        //Log.v(TAG, "filesizes==" + Arrays.toString(filesizes));
        filesizes = MySort(filesizes);
        //Log.v(TAG, "filesizes==" + Arrays.toString(filesizes));

        for (int i = 0; i < indexs.length; i++) {
            postmsg[256 + i] = indexs[i];
        }
        for (int i = 0; i < counts.length; i++) {
            postmsg[256 + 8 + i] = counts[i];
        }
        for (int i = 0; i < filesizes.length; i++) {
            postmsg[256 + 8 + 8 + i] = filesizes[i];
        }

        byte[] fSize = readBigFile(file, index, count);
        int size = postmsg.length + fSize.length;
        Log.i(TAG, "size==" + size + "---postmg.length==" + postmsg.length + "---fSize.length===" + fSize.length);
        byte[] content = new byte[size];
        for (int i = 0; i < content.length; i++) {
            content[i] = i < postmsg.length ? postmsg[i] : fSize[i - postmsg.length];
        }
        //Log.i(TAG, "content===" + Arrays.toString(content));


        return content;
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    private byte[] MySort(byte[] array) {
        int length = array.length;
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (array[i] != 0) {
                break;
            }
            index++;
        }


        byte[] content = new byte[(length - index)];


        for (int i = index; i < array.length; i++) {
            content[i - index] = array[i];

        }


        return content;
    }

    private byte[] readBigFile(File file, int index, long count) {
        final int SIZE = 5 * 1024 * 1024;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek((index - 1) * SIZE);
            byte[] data;
            if (index == count) {
                int size = (int) (file.length() % SIZE);
                Log.v(TAG, "size==" + size);
                data = new byte[size];
            } else {
                data = new byte[SIZE];
                Log.v(TAG, "SIZE==" + SIZE);
            }


            int read = raf.read(data);
            Log.i(TAG, "read==" + read);
            raf.close();

            return data;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return null;
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
    }


}
