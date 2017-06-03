package card.blink.com.fileexplore.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.adapter.FileListAdapter;
import card.blink.com.fileexplore.adapter.Protocol;
import card.blink.com.fileexplore.gson.FileListData;
import card.blink.com.fileexplore.tools.Comment;
import card.blink.com.fileexplore.tools.Tools;
import card.blink.com.fileexplore.view.MyProgressDIalog;
import card.blink.com.fileexplore.view.PullToRefreshListView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<FileListAdapter.Pair<String, Integer>> list;


    @InjectView(R.id.lv)
    PullToRefreshListView lv;

    private FileListAdapter fileListAdapter;
    public String currentPath = "/";
    public static final int ERROR = -1;
    public static final int SUCCESS = 1;
    public static final int UPLOAD_SUCCESS = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ERROR:
                    Log.i(TAG, "获取数据失败");
                    int result = (int) msg.obj;
                    handlerFailEvent(result);
                    break;
                case SUCCESS:
                    // 关闭对话框
                    MyProgressDIalog.getInstance(MainActivity.this).dissmissProgress();
                    if (lv != null) {
                        lv.onRefreshComplete();
                    }
                    Log.i(TAG, "获取数据成功");
                    FileListData data = (FileListData) msg.obj;
                    handlerSuccessEvent(data);
                    break;
                case UPLOAD_SUCCESS:
                    Log.i(TAG, "上传文件成功");
                    String fileName = (String) msg.obj;
                    Toast.makeText(MainActivity.this, "文件：" + fileName + "上传成功", Toast.LENGTH_SHORT).show();
                    break;
                default:

                    break;
            }
        }
    };


    /**
     * 处理获取列表数据失败的事件
     */
    private void handlerFailEvent(int result) {

    }

    /**
     * 处理获取列表成功的事件
     */
    private void handlerSuccessEvent(FileListData data) {
        list.clear();
        // 获取文件夹和文件
        FileListData.Folder[] folders = data.folder;
        FileListData.Document[] documents = data.document;
        FileListData.Disk[] disks = data.disk;

        // 遍历文件夹
        if (folders != null) {
            for (int i = 0; i < folders.length; i++) {
                FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
                pair.setB(Protocol.DIR);
                FileListData.Folder folder = folders[i];
                String name = folder.name;
                pair.setA(name);
                list.add(pair);
            }
        }
        // 遍历文件
        if (documents != null) {
            for (int i = 0; i < documents.length; i++) {
                FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
                pair.setB(Protocol.FL);
                FileListData.Document document = documents[i];
                String name = document.name;
                pair.setA(name);
                list.add(pair);
            }
        }

        // 是否显示上传文件功能
        if (currentPath.endsWith("/")) {
            Log.i(TAG, "不显示上传功能");
            ShowUpload(false);
        } else {
            Log.i(TAG, "显示上传功能");
            ShowUpload(true);
        }


        // 如果两个都为null，说明当前处于根目录
        if (folders == null && documents == null) {
            if (disks != null) {
                for (int i = 0; i < disks.length; i++) {
                    FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
                    pair.setB(Protocol.PAN);
                    FileListData.Disk disk = disks[i];
                    String name = disk.name;
                    pair.setA(name);
                    list.add(pair);
                }
            }
        }
        // 更新界面
        if (fileListAdapter != null) {
            fileListAdapter.setList(list);
        }
    }


    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        setContent(view);

        // 注解框架
        ButterKnife.inject(this, view);

        setTitle("文件浏览");
        setLeftTitle("返回");
        setRightTitle("上传文件");
        setLeftTitleColor(R.color.White);
        setRightTitleColor(R.color.White);
        setTopTitleColor(R.color.White);
        setTopColor(R.color.logincolor);

        Log.i(TAG, "不显示上传功能");
        ShowUpload(false);


        //lv = (ListView) view.findViewById(R.id.lv);
        list = new ArrayList<>();
        fileListAdapter = new FileListAdapter(this, list);
        lv.setAdapter(fileListAdapter);
        lv.setOnItemClickListener(this);
        lv.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "下拉成功，刷新操作开始");
                //lv.onRefreshComplete();
                MyProgressDIalog.getInstance(MainActivity.this).setContent("获取文件信息……").showProgressDialog();
                postRequest(currentPath);
            }
        });

        AllFile();

    }

    /**
     * 左边的点击事件重写
     */
    @Override
    public void setLeftCLick() {
        Log.v(TAG, "左边的点击事件");

        if (currentPath.equals("/")) {
            //Log.i(TAG, "已经到达了根目录");
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this,
                        "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // 退出当前的程序
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }

            return;
        }

        int lastIndexOf = currentPath.lastIndexOf("/");
        currentPath = currentPath.substring(0, lastIndexOf);
        if (currentPath.equals("")) {
            currentPath = "/";
        }

        MyProgressDIalog.getInstance(this).setContent("获取文件信息……").showProgressDialog();
        postRequest(currentPath);

    }


    /**
     * 这个是右边文字的点击事件 刷新操作
     *
     * @param v
     */
    @Override
    public void setRightTextClick(View v) {
        startActivityForResult(new Intent(this, ChooseUploadFileActivity.class), 1);
    }

    /**
     * 向服务器请求数据
     */
    private void AllFile() {
        // 第一次请求数据不用弹出对话框
        postRequest(currentPath);
    }

    // "{\"type\":\"getpath\",\"path\":\"/\"}"
    private void postRequest(final String position) {
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
                    Log.i(TAG, result + "");
                    if (result == 0) {
                        Message msg = Message.obtain();
                        msg.what = SUCCESS;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = ERROR;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    handler.sendEmptyMessage(ERROR);

                }
            }
        }.start();

    }


    /**
     * 点击文件的操作
     *
     * @param position
     */
    private void onclickfile(int position) {
        FileListAdapter.Pair<String, Integer> pair = list.get(position);
        String name = pair.getA();
        Log.v(TAG, "name===" + name);
        int type = pair.getB();
        Log.i(TAG, "type===" + type);
        switch (type) {
            case Protocol.DIR:
                currentPath = currentPath + "/" + name;
                Log.i(TAG, "currentPath===" + currentPath);
                MyProgressDIalog.getInstance(this).setContent("获取文件信息……").showProgressDialog();
                postRequest(currentPath);
                break;
            case Protocol.FL:
                Log.i(TAG, "点击直接下载文件 name == " + name);
                String urlDownload = currentPath + "/" + name;
                Log.i(TAG, "urlDownload===" + urlDownload);
                if (urlDownload.endsWith(".mp4")) {
                    Log.i(TAG, "在线播放");
                    playOnlineFile(urlDownload);

                } else {
                    Log.i(TAG, "直接下载文件");
                    downloadFile(urlDownload);
                }

                break;
            case Protocol.PAN:
                currentPath = currentPath + name;
                Log.i(TAG, "currentPath===" + currentPath);
                MyProgressDIalog.getInstance(this).setContent("获取文件信息……").showProgressDialog();
                postRequest(currentPath);
                break;
            default:
                break;
        }
    }

    /**
     * 处理在线播放,现在只支持mp4格式
     *
     * @param url
     */
    private void playOnlineFile(String url) {
        url = Comment.HOST + url;
        Log.e(TAG, "downloadFile url===" + url);

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        Uri data = Uri.parse(url);
        intent.setDataAndType(data, "video/mp4");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理下载文件，保存路径在根目录
     *
     * @param urlDownload
     */
    private void downloadFile(String urlDownload) {
        String url = Comment.HOST + urlDownload;
        Log.e(TAG, "downloadFile url===" + url);
        String[] strings = url.split("/");
        final String fileName = strings[strings.length - 1];
        Log.i(TAG, "fileName===" + fileName);
        Toast.makeText(this, "文件：" + fileName + "开始下载", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(MainActivity.this, "文件：" + fileName + "下载完毕", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Log.d(TAG, "inProgress===" + progress * 100);
                    }
                });
    }

    /**
     * 将手机中的文件上传至u盘中, 在子线程中传输
     *
     * @param path
     * @param uploadPath
     */
    public void uploadFileToExternalStorage(String path, final String uploadPath) {
        final File file = new File(path);
        new Thread() {
            @Override
            public void run() {
                postUploadFileRequest(file, uploadPath);
            }
        }.start();
    }

    /**
     * 发生上传文件的请求
     *
     * @param file
     * @param uploadPath
     */
    private void postUploadFileRequest(File file, String uploadPath) {
        final int SIZE = 5 * 1024 * 1024;
        long length = file.length();
        Log.v(TAG, "jd.rar的长度 length==" + length);
        long count = length / SIZE;

        if (length % SIZE == 0) {

        } else {
            count++;
        }
        Log.v(TAG, "需要分割成的块数是 count===" + count);

        int index = 1;
        while (index <= count) {
            // 请求上传index块
            uploading(file, index, count, uploadPath);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            index++;
        }

    }

    private void uploading(File file, int index, long count, String uploadPath) {
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

            // 发送上传成功信息
            Message msg = Message.obtain();
            msg.what = UPLOAD_SUCCESS;
            String[] strings = uploadPath.split("/");
            String name = strings[strings.length - 1];
            Log.v(TAG, "name===" + name);
            msg.obj = name;
            handler.sendMessage(msg);
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
    private byte[] getDataFromLocalFile(File file, int index, long count, String uploadPath) {
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
        int size = postmsg.length + fSize.length;
        Log.i(TAG, "size==" + size + "---postmg.length==" + postmsg.length + "---fSize.length===" + fSize.length);
        byte[] content = new byte[size];
        for (int i = 0; i < content.length; i++) {
            content[i] = i < postmsg.length ? postmsg[i] : fSize[i - postmsg.length];
        }
        Log.i(TAG, "content===" + Arrays.toString(content));

        return content;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "requestCode==" + requestCode);
        Log.v(TAG, "resultCode==" + resultCode);
        if (requestCode == 1 && resultCode == 1) {
            String path = data.getStringExtra("path");
            Log.v(TAG, "path==" + path);
            Log.d(TAG, "请求码和结果码都正确，可以向服务器上传数据");
            Log.v(TAG, "currentPath===" + currentPath);
            String[] strings = path.split("/");
            String uploadPath = currentPath + "/" + strings[strings.length - 1];
            Log.v(TAG, "uploadPath===" + uploadPath);
            Toast.makeText(this, "开始上传文件:" + strings[strings.length - 1], Toast.LENGTH_SHORT).show();
            uploadFileToExternalStorage(path, uploadPath);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 1;
        Log.v(TAG, "position==" + position);
        onclickfile(position);
    }

    /**
     * 是否显示右边的上传功能
     *
     * @param isShow
     */
    public void ShowUpload(boolean isShow) {
        setRightTitleVisiable(isShow);
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,
                    "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // 退出当前的程序
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    }
}
