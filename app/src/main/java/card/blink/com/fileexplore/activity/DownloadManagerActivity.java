package card.blink.com.fileexplore.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;
import com.lzy.okserver.task.ExecutorWithListener;

import java.io.File;
import java.util.List;

import butterknife.Bind;

import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.activity.base.UploadAndDownloadBaseActivity;
import card.blink.com.fileexplore.model.ApkModel;
import card.blink.com.fileexplore.tools.ApkUtils;
import card.blink.com.fileexplore.view.NumberProgressBar;


/**
 * Created by Administrator on 2017/6/12.
 */

public class DownloadManagerActivity extends UploadAndDownloadBaseActivity implements View.OnClickListener, ExecutorWithListener.OnAllTaskEndListener {


    private static final String TAG = DownloadManagerActivity.class.getSimpleName();
    private List<DownloadInfo> allTask;
    private MyAdapter adapter;
    private DownloadManager downloadManager;

    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;


//    /**
//     * Start()
//     */
//    @Override
//    public void init() {
//        View view = LayoutInflater.from(context).inflate(R.layout.activity_download_manager, null);
//        ButterKnife.bind(this, view);
//
//        setContent(view);
//
//        setTitle("下载管理");
//        setLeftTitle("返回");
//        setRightTitleVisiable(false);
//        setLeftTitleColor(R.color.White);
//        setRightTitleColor(R.color.White);
//        setTopTitleColor(R.color.White);
//        setTopColor(R.color.MainColorBlue);
//
//        downloadManager = DownloadService.getDownloadManager();
//        allTask = downloadManager.getAllTask();
//        adapter = new MyAdapter();
//        listView.setAdapter(adapter);
//
//        downloadManager.getThreadPool().getExecutor().addOnAllTaskEndListener(this);
//
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download_manager);
        initToolBar(toolbar, true, "下载管理");

        downloadManager = DownloadService.getDownloadManager();
        allTask = downloadManager.getAllTask();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        downloadManager.getThreadPool().getExecutor().addOnAllTaskEndListener(this);
    }

//    /**
//     * 左边的点击事件重写
//     */
//    @Override
//    public void setLeftCLick() {
//        Log.v(TAG, "setLeftCLick");
//        super.setLeftCLick();
//
//    }


    @Override
    public void onAllTaskEnd() {
        for (DownloadInfo downloadInfo : allTask) {
            if (downloadInfo.getState() != DownloadManager.FINISH) {
                Toast.makeText(DownloadManagerActivity.this, "所有下载线程结束，部分下载未完成", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(DownloadManagerActivity.this, "所有下载任务完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得移除，否者会回调多次
        downloadManager.getThreadPool().getExecutor().removeOnAllTaskEndListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击事件就是在这个方法里面实现
     * 这个点击事件是单击 不包括 item点击 长按 等等
     *
     * @param v
     */
    //@Override
    public void Click(View v) {
        Log.v(TAG, "---Click");
        //super.Click(v);
        switch (v.getId()) {
            case R.id.removeAll:
                Log.v(TAG, "removeAll");
                downloadManager.removeAllTask();
                adapter.notifyDataSetChanged();  //移除的时候需要调用
                break;
            case R.id.pauseAll:
                Log.v(TAG, "pauseAll");
                downloadManager.pauseAllTask();
                break;
            case R.id.stopAll:
                Log.v(TAG, "stopAll");
                downloadManager.stopAllTask();
                break;
            case R.id.startAll:
                Log.v(TAG, "startAll");
                downloadManager.startAllTask();
                break;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Click(v);
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return allTask.size();
        }

        @Override
        public DownloadInfo getItem(int position) {
            return allTask.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final DownloadInfo downloadInfo = getItem(position);
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(DownloadManagerActivity.this, R.layout.item_download_manager, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.refresh(downloadInfo);

            //对于非进度更新的ui放在这里，对于实时更新的进度ui，放在holder中
            ApkModel apk = (ApkModel) downloadInfo.getData();
            if (apk != null) {
                Glide.with(DownloadManagerActivity.this).load(apk.getIconUrl()).error(R.mipmap.ic_launcher).into(holder.icon);
                holder.name.setText(apk.getName());
            } else {
                holder.name.setText(downloadInfo.getFileName());
            }
            holder.download.setOnClickListener(holder);
            holder.remove.setOnClickListener(holder);
            holder.restart.setOnClickListener(holder);

            DownloadListener downloadListener = new MyDownloadListener();
            downloadListener.setUserTag(holder);
            downloadInfo.setListener(downloadListener);
            return convertView;
        }
    }

    private class ViewHolder implements View.OnClickListener {
        private DownloadInfo downloadInfo;
        private ImageView icon;
        private TextView name;
        private TextView downloadSize;
        private TextView tvProgress;
        private TextView netSpeed;
        private NumberProgressBar pbProgress;
        private Button download;
        private Button remove;
        private Button restart;


        public ViewHolder(View convertView) {
            icon = (ImageView) convertView.findViewById(R.id.icon);
            name = (TextView) convertView.findViewById(R.id.name);
            downloadSize = (TextView) convertView.findViewById(R.id.downloadSize);
            tvProgress = (TextView) convertView.findViewById(R.id.tvProgress);
            netSpeed = (TextView) convertView.findViewById(R.id.netSpeed);
            pbProgress = (NumberProgressBar) convertView.findViewById(R.id.pbProgress);
            download = (Button) convertView.findViewById(R.id.start);
            remove = (Button) convertView.findViewById(R.id.remove);
            restart = (Button) convertView.findViewById(R.id.restart);

        }

        public void refresh(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            Log.v(TAG, "this.downloadInfo = downloadInfo;");
            refresh();
        }

        //对于实时更新的进度ui，放在这里，例如进度的显示，而图片加载等，不要放在这，会不停的重复回调
        //也会导致内存泄漏
        private void refresh() {
            String downloadLength = Formatter.formatFileSize(DownloadManagerActivity.this, downloadInfo.getDownloadLength());
            String totalLength = Formatter.formatFileSize(DownloadManagerActivity.this, downloadInfo.getTotalLength());
            downloadSize.setText(downloadLength + "/" + totalLength);
            if (downloadInfo.getState() == DownloadManager.NONE) {
                netSpeed.setText("停止");
                download.setText("下载");
            } else if (downloadInfo.getState() == DownloadManager.PAUSE) {
                netSpeed.setText("暂停中");
                download.setText("继续");
            } else if (downloadInfo.getState() == DownloadManager.ERROR) {
                netSpeed.setText("下载出错");
                download.setText("出错");
            } else if (downloadInfo.getState() == DownloadManager.WAITING) {
                netSpeed.setText("等待中");
                download.setText("等待");
            } else if (downloadInfo.getState() == DownloadManager.FINISH) {
                if (ApkUtils.isAvailable(DownloadManagerActivity.this, new File(downloadInfo.getTargetPath()))) {
                    download.setText("卸载");
                } else {
                    download.setText("安装");
                }
                netSpeed.setText("下载完成");
            } else if (downloadInfo.getState() == DownloadManager.DOWNLOADING) {
                String networkSpeed = Formatter.formatFileSize(DownloadManagerActivity.this, downloadInfo.getNetworkSpeed());
                netSpeed.setText(networkSpeed + "/s");
                download.setText("暂停");
            }
            String progress = (Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%";
            int jd = (int) (Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100);
            Log.v(TAG, "jd==" + jd);


            tvProgress.setText((progress));
            int maxInt = (int) downloadInfo.getTotalLength();
            pbProgress.setMax((int) downloadInfo.getTotalLength());
            int progressInt = (int) downloadInfo.getDownloadLength();
            pbProgress.setProgress((int) downloadInfo.getDownloadLength());
            Log.v(TAG, "progressInt===" + progressInt + "---maxInt===" + maxInt);
            //adapter.notifyDataSetChanged();

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == download.getId()) {
                switch (downloadInfo.getState()) {
                    case DownloadManager.PAUSE:
                    case DownloadManager.NONE:
                    case DownloadManager.ERROR:
                        downloadManager.addTask(downloadInfo.getUrl(), downloadInfo.getRequest(), downloadInfo.getListener());
                        break;
                    case DownloadManager.DOWNLOADING:
                        downloadManager.pauseTask(downloadInfo.getUrl());
                        break;
                    case DownloadManager.FINISH:
                        if (ApkUtils.isAvailable(DownloadManagerActivity.this, new File(downloadInfo.getTargetPath()))) {
                            ApkUtils.uninstall(DownloadManagerActivity.this, ApkUtils.getPackageName(DownloadManagerActivity.this, downloadInfo.getTargetPath()));
                        } else {
                            ApkUtils.install(DownloadManagerActivity.this, new File(downloadInfo.getTargetPath()));
                        }
                        break;
                }
                Log.v(TAG, "refresh();");
                refresh();
            } else if (v.getId() == remove.getId()) {
                downloadManager.removeTask(downloadInfo.getUrl());
                adapter.notifyDataSetChanged();
            } else if (v.getId() == restart.getId()) {
                downloadManager.restartTask(downloadInfo.getUrl());
            }
        }
    }

    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            //Log.v(TAG, "onProgress");
            if (getUserTag() == null) return;
            ViewHolder holder = (ViewHolder) getUserTag();
            holder.refresh();  //这里不能使用传递进来的 DownloadInfo，否者会出现条目错乱的问题
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Toast.makeText(DownloadManagerActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null)
                Toast.makeText(DownloadManagerActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
