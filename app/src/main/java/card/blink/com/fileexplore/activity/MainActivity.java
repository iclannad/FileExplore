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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.adapter.FileListAdapter;
import card.blink.com.fileexplore.adapter.Protocol;
import card.blink.com.fileexplore.gson.FileListData;
import card.blink.com.fileexplore.tools.Comment;
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
     * 将手机中的文件上传至u盘中
     *
     * @param path
     */
    public void uploadFile(String path) {

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
            uploadFile(path);
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
