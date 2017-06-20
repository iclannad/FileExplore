package card.blink.com.fileexplore.tools;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyk.greendaodemo.greendao.gen.UploadTaskDao;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import card.blink.com.fileexplore.gson.FileListData;
import card.blink.com.fileexplore.model.UploadTask;
import card.blink.com.fileexplore.service.UploadTaskCallback;
import card.blink.com.fileexplore.upload.GreenDaoManager;
import card.blink.com.fileexplore.upload.UploadManager;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/6/12.
 */

public class FileTransportUtils {

    private static final String TAG = FileTransportUtils.class.getSimpleName();

    /**
     * 处理下载文件，保存路径在根目录
     *
     * @param urlDownload
     */
    public static void downloadFile(final Context context, String urlDownload) {
        String url = Comment.HOST + urlDownload;
        Log.e(TAG, "downloadFile url===" + url);
        String[] strings = url.split("/");
        final String fileName = strings[strings.length - 1];
        Log.i(TAG, "fileName===" + fileName);
        Toast.makeText(context, "文件：" + fileName + "开始下载", Toast.LENGTH_SHORT).show();

        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName)//
                {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.i(TAG, "response===" + response.getAbsolutePath());
                        Toast.makeText(context, "文件：" + fileName + "下载完毕", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Log.d(TAG, "fileName===" + fileName + " inProgress===" + progress * 100);
                    }
                });
    }

    /**
     * 处理在线播放,现在只支持mp4格式
     *
     * @param url
     */
    public static void playOnlineFile(Context context, String url) {
        url = Comment.HOST + url;
        Log.e(TAG, "downloadFile url===" + url);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

//        String link = "rtsp://" + url;
//        Log.e(TAG, "link===" + link);
        //Uri data = Uri.parse(link);

        Uri data = Uri.parse(url);

        intent.setDataAndType(data, "video/*");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    // "{\"type\":\"getpath\",\"path\":\"/\"}"
    public static void postRequest(final String position, final Handler handler) {
        Log.i(TAG, "position===" + position);
        new Thread() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"type\":\"getpath\",\"path\":\"" + position + "\"}");
                Request request = new Request.Builder()
                        .url("http://192.168.16.1:8080/cgi-bin/dir_list.cgi")
                        .post(body)
                        .addHeader("authorization", "Basic YWRtaW46YWRtaW4=")
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();
                    String string = responseBody.string();
                    Log.i(TAG, "responseBody.string()==" + string);
                    // 获取到Json数据的内容
                    String content = string;

                    Gson gson = new Gson();
                    FileListData data = gson.fromJson(content, FileListData.class);
                    Log.d(TAG, data.toString());
                    int result = data.result;
                    Log.i(TAG, "result==" + result + "");
                    if (result == 0) {
                        Message msg = Message.obtain();
                        msg.what = Comment.SUCCESS;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = Comment.UN_NORMAL;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = Comment.ERROR;
                    handler.sendMessage(msg);

                }
            }
        }.start();

    }

    /**
     * 将手机中的文件上传至u盘中, 在子线程中传输
     */
    public static void uploadFileToExternalStorage(final UploadTask uploadTask) {
        final File file = new File(uploadTask.fromUrl);
        uploadTask.fileSize = file.length();
        Log.v(TAG, "fileSize==" + uploadTask.fileSize);
        new Thread() {
            @Override
            public void run() {
                postUploadFileRequest(file, uploadTask);
            }
        }.start();
    }

    /**
     * 发生上传文件的请求
     *
     * @param file
     * @param file, String uploadPath, Handler handler
     */
    private static void postUploadFileRequest(File file, UploadTask uploadTask) {
        final int SIZE = 5 * 1024 * 1024;
        long length = file.length();
        long count = length / SIZE;

        if (length % SIZE == 0) {

        } else {
            count++;
        }

        Log.v(TAG, "需要分割成的块数是 count===" + count);

        int index = 1;
        Log.v(TAG, "uploadTask.index==" + uploadTask.index);
        if (uploadTask.index != 0) {
            index = (int) uploadTask.index;
        }

        while (index <= count) {

            if (uploadTask.status == UploadManager.PAUSE) {
                Log.v(TAG, "PAUSE");
                if (uploadTask.switch_status == UploadManager.RUNNING_TO_PAUSE) {
                    Log.v(TAG, "RUNNING_TO_PAUSE");
                    // 暂停的逻辑
                    synchronized (uploadTask) {
                        try {
                            uploadTask.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // 请求上传index块
            long timeMillisBefore = System.currentTimeMillis();
            uploading(file, index, count, uploadTask.toUrl, uploadTask.handler);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long timeMillisAfter = System.currentTimeMillis();

            Log.v(TAG, "timeMillisAfter - timeMillisBefore===" + (timeMillisAfter - timeMillisBefore));
            uploadTask.time = (timeMillisAfter - timeMillisBefore) / 1000;

            uploadTask.index = index;
            uploadTask.count = count;

            if (index == count && index != 0) {
                // 更新任务的状态为完成
                uploadTask.status = UploadManager.FINISH;
                uploadTask.switch_status = UploadManager.RUNNING_TO_FINISH;
                // 更新数据库里面的状态
                UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();
                uploadTaskDao.update(uploadTask);


                // 回调给service
                UploadTaskCallback callback = uploadTask.uploadTaskCallback;
                if (callback != null) {
                    callback.finished(uploadTask);
                }
            }

            // 如果当任务已经被删除
            if (uploadTask.status == UploadManager.DELETE) {
                Log.v(TAG, "任务已被删除");
                if (uploadTask.switch_status == UploadManager.RUNNING_TO_DELETE) {
                    UploadTaskCallback callback = uploadTask.uploadTaskCallback;
                    if (callback != null) {
                        callback.finished(uploadTask);
                    }
                    break;
                } else if (uploadTask.switch_status == UploadManager.PAUSE_TO_DELETE) {
                    Log.v(TAG, "暂停过程中删除");
                    UploadTaskCallback callback = uploadTask.uploadTaskCallback;
                    if (callback != null) {
                        callback.finished(uploadTask);
                    }
                    break;
                }
            }
            Log.v(TAG, "uploadTask.handler==" + uploadTask.handler);
            if (uploadTask.handler != null) {
                // 发信号到主线程
                Message msg = Message.obtain();
                msg.what = Comment.UPLOADING;
                msg.obj = uploadTask;
                uploadTask.handler.sendMessage(msg);
            }
            index++;
        }

        Log.v(TAG, "退出上传文件");

    }

    private static void uploading(File file, int index, long count, String uploadPath, Handler handler) {
        byte[] content = getDataFromLocalFile(file, index, count, uploadPath);
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
            Log.i(TAG, "response==" + response.toString());
            ResponseBody responseBody = response.body();
            Log.i(TAG, "responseBody==" + responseBody.toString());
            String string = responseBody.string();
            Log.i(TAG, "responseBody.string()==" + string);

            Log.v(TAG, "index===" + index + "   count===" + count);
            // 上传最后一块完成时，文件上传成功
            if (index == count) {
                if (handler != null) {
                    // 发送上传成功信息
                    Message msg = Message.obtain();
                    msg.what = Comment.UPLOAD_SUCCESS;
                    String[] strings = uploadPath.split("/");
                    String name = strings[strings.length - 1];
                    Log.v(TAG, "name===" + name);
                    msg.obj = name;
                    handler.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 封装要上传的数组
     *
     * @param file
     * @param index
     * @param count
     * @param uploadPath
     * @return
     */
    private static byte[] getDataFromLocalFile(File file, int index, long count, String uploadPath) {
        long length = file.length();
        // empty
        byte[] postmsg = new byte[280];

        //String path = "/mount1/myjd4.rar";
        String path = uploadPath;
        Log.v(TAG, "uploadPath == " + uploadPath);
        byte[] pathBytes = path.getBytes();
        // 填充路径
        for (int i = 0; i < pathBytes.length; i++) {
            postmsg[i] = pathBytes[i];
        }
        Log.i(TAG, "postmsg===" + Arrays.toString(postmsg));

        long block = index;
        long blocksize = count;
        long filesize = length;

        byte[] indexs = Tools.longToBytes(index);
        byte[] counts = Tools.longToBytes(count);
        byte[] filesizes = Tools.longToBytes(filesize);

        Log.v(TAG, "indexs==" + Arrays.toString(indexs));
        indexs = Tools.resortBytes(indexs);
        Log.v(TAG, "indexs==" + Arrays.toString(indexs));

        Log.v(TAG, "counts==" + Arrays.toString(counts));
        counts = Tools.resortBytes(counts);
        Log.v(TAG, "counts==" + Arrays.toString(counts));

        Log.v(TAG, "filesizes==" + Arrays.toString(filesizes));
        filesizes = Tools.resortBytes(filesizes);
        Log.v(TAG, "filesizes==" + Arrays.toString(filesizes));

        for (int i = 0; i < indexs.length; i++) {
            postmsg[256 + i] = indexs[i];
        }
        for (int i = 0; i < counts.length; i++) {
            postmsg[256 + 8 + i] = counts[i];
        }
        for (int i = 0; i < filesizes.length; i++) {
            postmsg[256 + 8 + 8 + i] = filesizes[i];
        }

        byte[] fSize = Tools.readFileToBytes(file, index, count);
        // 拼接postmsg和fSize两个数组
        int size = postmsg.length + fSize.length;
        Log.i(TAG, "size==" + size + "---postmg.length==" + postmsg.length + "---fSize.length===" + fSize.length);
        byte[] content = new byte[size];
        for (int i = 0; i < content.length; i++) {
            content[i] = i < postmsg.length ? postmsg[i] : fSize[i - postmsg.length];
        }
        Log.i(TAG, "content===" + Arrays.toString(content));

        return content;
    }

}
