package card.blink.com.fileexplore.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.adapter.FileListAdapter;
import card.blink.com.fileexplore.adapter.Protocol;
import card.blink.com.fileexplore.gson.FileListData;
import card.blink.com.fileexplore.view.MyProgressDIalog;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<FileListAdapter.Pair<String, Integer>> list;


    @InjectView(R.id.lv)
    ListView lv;

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
        setRightTitle("刷新");
        setLeftTitleColor(R.color.White);
        setRightTitleColor(R.color.White);
        setTopTitleColor(R.color.White);
        setTopColor(R.color.Blue);


        //lv = (ListView) view.findViewById(R.id.lv);
        list = new ArrayList<>();
        fileListAdapter = new FileListAdapter(this, list);
        lv.setAdapter(fileListAdapter);
        lv.setOnItemClickListener(this);

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
        MyProgressDIalog.getInstance(this).setContent("获取文件信息……").showProgressDialog();
        postRequest(currentPath);
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
                Log.v(TAG, "暂时不处理点击文件的操作");
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onclickfile(position);
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