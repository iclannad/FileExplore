package card.blink.com.fileexplore.activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.wyk.greendaodemo.greendao.gen.UploadTaskDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.adapter.FileListAdapter;
import card.blink.com.fileexplore.adapter.Protocol;
import card.blink.com.fileexplore.gson.FileListData;
import card.blink.com.fileexplore.model.ApkModel;
import card.blink.com.fileexplore.model.UploadTask;
import card.blink.com.fileexplore.service.UploadService;
import card.blink.com.fileexplore.tools.Comment;
import card.blink.com.fileexplore.tools.FileTransportUtils;
import card.blink.com.fileexplore.upload.GreenDaoManager;
import card.blink.com.fileexplore.upload.UploadManager;
import card.blink.com.fileexplore.view.MyProgressDIalog;
import card.blink.com.fileexplore.view.PullToRefreshListView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<FileListAdapter.Pair<String, Integer>> list;


    @Bind(R.id.lv)
    PullToRefreshListView lv;
    private DownloadManager downloadManager;


    private FileListAdapter fileListAdapter;
    public String currentPath = "/";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (lv != null) {
                lv.onRefreshComplete();
            }
            switch (msg.what) {
                case Comment.ERROR:
                    MyProgressDIalog.getInstance(MainActivity.this).dissmissProgress();
                    Log.i(TAG, "获取数据失败");
                    handlerFailEvent();
                    break;
                case Comment.UN_NORMAL:
                    MyProgressDIalog.getInstance(MainActivity.this).dissmissProgress();
                    int result = (int) msg.obj;
                    handlerUnNormal(result);
                    break;
                case Comment.SUCCESS:
                    // 关闭对话框
                    MyProgressDIalog.getInstance(MainActivity.this).dissmissProgress();
                    Log.i(TAG, "获取数据成功");
                    FileListData data = (FileListData) msg.obj;
                    handlerSuccessEvent(data);
                    break;
                case Comment.UPLOAD_SUCCESS:
                    Log.i(TAG, "上传文件成功");
                    String fileName = (String) msg.obj;
                    Toast.makeText(MainActivity.this, "文件：" + fileName + "上传成功", Toast.LENGTH_SHORT).show();
                    break;
                case Comment.UPLOADING:
                    Log.v(TAG, "任务上传的过程中");
                    UploadTask uploadTask = (UploadTask) msg.obj;
                    if (uploadTask.uploadListener != null) {
                        Log.v(TAG, "我现在在主线程");
                        uploadTask.uploadListener.onProgress(uploadTask);
                    }
                    break;
                case Comment.UPLOADING_EXISTS:
                    Log.v(TAG, "存在上传文件");
                    UploadTask uploadTaskExists = (UploadTask) msg.obj;
                    Toast.makeText(MainActivity.this, "文件：" + uploadTaskExists.name + "已存在", Toast.LENGTH_SHORT).show();
                    break;
                default:

                    break;
            }
        }
    };


    /**
     * 处理获取列表数据失败的事件
     */
    private void handlerFailEvent() {
        Log.v(TAG, "获取数据失败");
        Toast.makeText(this, "获取数据失败，请检查连接是否正确", Toast.LENGTH_SHORT).show();
        MainActivity.this.finish();
    }

    /**
     * 处理无硬盘挂载的逻辑
     */
    private void handlerUnNormal(int reuslt) {
        if (reuslt == 2) {
            Toast.makeText(this, "获取数据失败，请检查硬盘是否正确加载", Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
        } else if (reuslt == 4) {
            Toast.makeText(this, "获取数据失败，请求路径不正确", Toast.LENGTH_SHORT).show();
        }


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

                long time = folder.time;
                pair.setTime(time);

                pair.setUpan(true);
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

                long time = document.time;
                pair.setTime(time);
                String size = document.size;
                pair.setSize(size);

                pair.setUpan(true);
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
                    FileListData.Disk disk = disks[i];
                    Log.i(TAG, "disk.free==" + disk.free);
                    FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
                    pair.setB(Protocol.PAN);
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

        // 注解框架
        ButterKnife.bind(this, view);

        setContent(view);

        setTitle("文件浏览");
        setLeftTitle("返回");
        setRightTitle("上传文件");
        setLeftTitleColor(R.color.White);
        setRightTitleColor(R.color.White);
        setTopTitleColor(R.color.White);
        setTopColor(R.color.logincolor);

        initData();

        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");

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
                FileTransportUtils.postRequest(currentPath, handler);
            }
        });
        MyProgressDIalog.getInstance(MainActivity.this).setContent("检查环境……").showProgressDialog();
        AllFile();

    }

    /**
     * 初始化数据库
     */
    private void initData() {
        Log.v(TAG, "从数据库中获取任务列表");
        ArrayList<UploadTask> uploadTaskList = (ArrayList<UploadTask>) GreenDaoManager.getInstance().getSession().getUploadTaskDao().queryBuilder().list();
        Log.v(TAG, "uploadTaskList.size()===" + uploadTaskList.size());
        for (int i = 0; i < uploadTaskList.size(); i++) {
            UploadTask uploadTask = uploadTaskList.get(i);
            Long id = uploadTask.id;
            int status = uploadTask.status;
            int switch_status = uploadTask.switch_status;
            long index = uploadTask.index;
            long count = uploadTask.count;
            String toUrl = uploadTask.toUrl;
            String fromUrl = uploadTask.fromUrl;
            Log.v(TAG, "id==" + id + " status==" + status + " switch_status==" + switch_status);
            Log.v(TAG, "index==" + index + " count==" + count + " toUrl==" + toUrl + " fromUrl==" + fromUrl);
            uploadTask.handler = handler;
        }
        UploadManager.getInstance().updateUploadTaskList(uploadTaskList);
    }

    @OnClick(R.id.btn_download)
    public void openDownloadActivity() {
        Log.v(TAG, "openDownloadActivity");
        startActivity(new Intent(this, DownloadManagerActivity.class));

    }

    @OnClick(R.id.btn_upload)
    public void openUploadActivity() {
        Log.v(TAG, "openUploadActivity");
        startActivity(new Intent(this, UploadManagerActivity.class));
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
        FileTransportUtils.postRequest(currentPath, handler);

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
        FileTransportUtils.postRequest(currentPath, handler);
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
                FileTransportUtils.postRequest(currentPath, handler);
                break;
            case Protocol.FL:
                Log.v(TAG, "点击直接下载文件 name == " + name);
                String urlDownload = currentPath + "/" + name;
                Log.v(TAG, "urlDownload===" + urlDownload);
                if (urlDownload.endsWith(".mp4")) {
                    Log.v(TAG, "在线播放");
                    FileTransportUtils.playOnlineFile(this, urlDownload);
                } else {
                    // 开始服务
                    Log.v(TAG, "添加任务到任务列表");
                    //FileTransportUtils.downloadFile(this, urlDownload);
                    urlDownload = Comment.HOST + urlDownload;
                    ApkModel apkModel = new ApkModel(name, urlDownload, null);
                    GetRequest request = OkGo.get(apkModel.getUrl());
                    downloadManager.addTask(apkModel.getUrl(), apkModel, request, null);
                }

                break;
            case Protocol.PAN:
                currentPath = currentPath + name;
                Log.i(TAG, "currentPath===" + currentPath);
                MyProgressDIalog.getInstance(this).setContent("获取文件信息……").showProgressDialog();
                FileTransportUtils.postRequest(currentPath, handler);
                break;
            default:
                break;
        }
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

            //uploadFileToExternalStorage(path, uploadPath);
            //FileTransportUtils.uploadFileToExternalStorage(path, uploadPath, handler);
            Log.v(TAG, "在服务上传上传文件");
            UploadTask uploadTask = new UploadTask();
            uploadTask.isUploadTaskPauseOnRunning = true;
            uploadTask.status = UploadManager.WAIT;
            uploadTask.switch_status = UploadManager.NONE_TO_WAIT;
            uploadTask.name = strings[strings.length - 1];
            uploadTask.fromUrl = path;
            uploadTask.toUrl = uploadPath;
            uploadTask.handler = handler;
            //Comment.TASK_ARRAY_LIST.add(uploadTask);
            UploadManager.getInstance().addTask(uploadTask);
            // 将上传任务添加到数据库
            UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();
            long insert = uploadTaskDao.insert(uploadTask);
            Log.v(TAG, "insert==" + insert);

            startService(new Intent(this, UploadService.class));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 1;
        Log.v(TAG, "position==" + position);
        onclickfile(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 1;
        Log.v(TAG, "position==" + position);
        return false;
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
            // 程序关闭前可以在此处将数据保存至数据库
            Log.v(TAG, "关闭app");

            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // 退出当前的程序
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }
}
