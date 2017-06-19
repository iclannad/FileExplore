package card.blink.com.fileexplore.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.lzy.okserver.upload.UploadInfo;
import com.wyk.greendaodemo.greendao.gen.UploadTaskDao;

import java.io.File;
import java.io.IOError;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.activity.base.UploadAndDownloadBaseActivity;
import card.blink.com.fileexplore.model.UploadTask;
import card.blink.com.fileexplore.service.UploadService;
import card.blink.com.fileexplore.tools.Comment;
import card.blink.com.fileexplore.upload.GreenDaoManager;
import card.blink.com.fileexplore.upload.UploadListener;
import card.blink.com.fileexplore.upload.UploadManager;
import card.blink.com.fileexplore.view.NumberProgressBar;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/13.
 */

public class UploadManagerActivity extends UploadAndDownloadBaseActivity {

    private static final String TAG = UploadManagerActivity.class.getSimpleName();
    private List<UploadTask> allTask;
    private MyAdapter adapter;
    private UploadManager uploadManager;

    @Bind(R.id.listView)
    ListView listView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_manager);
        initToolBar(toolbar, true, "上传管理");

        uploadManager = UploadService.getUploadManager();
        allTask = uploadManager.getAllTask();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.removeAll)
    public void removeAll() {
        Log.v(TAG, "removeAll");
        UploadManager.getInstance().clearAllTask();
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.pauseAll)
    public void pauseAll() {
        Log.v(TAG, "pauseAll");
        UploadManager.getInstance().pauseAllTask();
        adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.startAll)
    public void startAll() {
        Log.v(TAG, "startAll");
        UploadManager.getInstance().startAllTask();
        // 开启服务
        startService(new Intent(this, UploadService.class));
        adapter.notifyDataSetChanged();
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allTask.size();

        }

        @Override
        public UploadTask getItem(int position) {
            return allTask.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UploadTask uploadTask = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(UploadManagerActivity.this, R.layout.item_upload_manager, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(uploadTask.name);

            String name = uploadTask.name;
            long index = uploadTask.index;
            long count = uploadTask.count;
            Log.v(TAG, "name==" + name + "---index==" + index + "---count==" + count);


            holder.refresh(uploadTask);
            if (uploadTask.status == UploadManager.FINISH) {
                holder.netSpeed.setText("已完成");
                holder.tvProgress.setText("100.0%");
                File file = new File(uploadTask.fromUrl);
                long size = file.length();
                Log.v(TAG, "size==" + size);
                String uploaded = Formatter.formatFileSize(UploadManagerActivity.this, size);
                holder.uploadSize.setText(uploaded + "/" + uploaded);
                holder.pbProgress.setMax(1);
                holder.pbProgress.setProgress(1);
            } else if (uploadTask.status == UploadManager.PAUSE) {
                if (uploadTask.count != 0) {
                    holder.pbProgress.setMax((int) uploadTask.count);
                    holder.pbProgress.setProgress((int) uploadTask.index);
                }
            }

            // 条目的按钮的点击事件
            holder.upload.setOnClickListener(holder);
            holder.remove.setOnClickListener(holder);

            UploadListener uploadListener = new MyUploadListener();
            uploadListener.setUserTag(holder);
            uploadTask.uploadListener = uploadListener;
            return convertView;
        }
    }


    private class ViewHolder implements View.OnClickListener {

        private UploadTask uploadTask;
        private ImageView icon;
        private TextView name;
        private TextView uploadSize;
        private TextView tvProgress;
        private TextView netSpeed;
        private NumberProgressBar pbProgress;
        private Button upload;
        private Button remove;


        public ViewHolder(View convertView) {
            icon = (ImageView) convertView.findViewById(R.id.icon);
            name = (TextView) convertView.findViewById(R.id.name);
            uploadSize = (TextView) convertView.findViewById(R.id.uploadSize);
            tvProgress = (TextView) convertView.findViewById(R.id.tvProgress);
            netSpeed = (TextView) convertView.findViewById(R.id.netSpeed);
            pbProgress = (NumberProgressBar) convertView.findViewById(R.id.pbProgress);
            upload = (Button) convertView.findViewById(R.id.start);
            remove = (Button) convertView.findViewById(R.id.remove);

        }

        public void refresh(UploadTask uploadTask) {
            this.uploadTask = uploadTask;
            refresh();
        }

        private void refresh() {
            if (uploadTask.status == UploadManager.DELETE) {
                Log.v(TAG, "任务已经被删除");
                return;
            }
            if (uploadTask.status == UploadManager.PAUSE) {
                upload.setText("上传");
                netSpeed.setText("暂停中");
                return;
            }
            if (uploadTask.status == UploadManager.RUNING) {
                upload.setText("暂停");
            } else if (uploadTask.status == UploadManager.FINISH) {
                upload.setText("上传");
            }


            float progress = uploadTask.index * 1.0f / uploadTask.count;
            String jd = (Math.round(progress * 10000) * 1.0f / 100) + "%";
            Log.v(TAG, "当前上传的进度：" + jd);
            tvProgress.setText(jd);
            pbProgress.setMax((int) uploadTask.count);
            pbProgress.setProgress((int) uploadTask.index);

            String fileSize = "0M";
            if (uploadTask.fileSize != 0) {
                fileSize = Formatter.formatFileSize(UploadManagerActivity.this, uploadTask.fileSize);
            }
            Log.v(TAG, "fileSize===" + fileSize);
            String uploaded = "--M/--M";
            String netspeed = "---K/s";

            if (uploadTask.index == uploadTask.count && uploadTask.index != 0) {
                uploaded = fileSize + "/" + fileSize;
                netspeed = "上传完成";
            } else {
                uploaded = uploadTask.index * 5 + "M/" + fileSize;
                long speed;
                try {
                    speed = (5 * 1024 * 1024) / uploadTask.time;
                } catch (RuntimeException e) {
                    speed = 0;
                }
                netspeed = Formatter.formatFileSize(UploadManagerActivity.this, speed) + "/s";
            }

            Log.v(TAG, "uploaded===" + uploaded);
            uploadSize.setText(uploaded);
            Log.v(TAG, "netspeed===" + netspeed);
            netSpeed.setText(netspeed);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == upload.getId()) {
                Log.v(TAG, "upload");
                if (uploadTask.status == UploadManager.RUNING) {
                    uploadTask.status = UploadManager.PAUSE;
                    Log.v(TAG, "PAUSE");
                    uploadTask.switch_status = UploadManager.RUNNING_TO_PAUSE;
                    upload.setText("上传");
                } else if (uploadTask.status == UploadManager.PAUSE) {
                    uploadTask.status = UploadManager.RUNING;
                    Log.v(TAG, "RUNNING");
                    if (uploadTask.switch_status == UploadManager.RUNNING_TO_PAUSE) {
                        uploadTask.switch_status = UploadManager.PAUSE_TO_RUNNING;
                        synchronized (uploadTask) {
                            uploadTask.notify();
                        }
                    } else if (uploadTask.switch_status == UploadManager.WAIT_TO_PAUSE) {
                        uploadTask.status = UploadManager.WAIT;
                        uploadTask.switch_status = UploadManager.PAUSE_TO_WAIT;
                    }

                    upload.setText("暂停");
                    startService(new Intent(UploadManagerActivity.this, UploadService.class));
                }

                // 更新数据库里面的状态
                UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();
                uploadTaskDao.update(uploadTask);

            } else if (v.getId() == remove.getId()) {
                Log.v(TAG, "remove");
                if (uploadTask.status == UploadManager.RUNING) {
                    // 任务在运行时删除
                    UploadManager.getInstance().removeTask(uploadTask);
                    uploadTask.status = UploadManager.DELETE;
                    uploadTask.switch_status = UploadManager.RUNNING_TO_DELETE;
                } else if (uploadTask.status == UploadManager.WAIT) {
                    // 任务在等待时删除
                    UploadManager.getInstance().removeTask(uploadTask);
                    uploadTask.status = UploadManager.DELETE;
                    uploadTask.switch_status = UploadManager.WAIT_TO_DELETE;
                } else if (uploadTask.status == UploadManager.PAUSE) {
                    // 任务在暂停时删除
                    Log.v(TAG, "暂停过程中删除");
                    uploadTask.status = UploadManager.DELETE;
                    uploadTask.switch_status = UploadManager.PAUSE_TO_DELETE;
                    synchronized (uploadTask) {
                        uploadTask.notify();
                    }
                    UploadManager.getInstance().removeTask(uploadTask);
                } else if (uploadTask.status == UploadManager.FINISH) {
                    // 任务下载完成时删除
                    if (uploadTask.switch_status == UploadManager.RUNNING_TO_FINISH) {
                        UploadManager.getInstance().removeTask(uploadTask);
                    }
                }
                // 更新数据库里面的状态
                UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();
                uploadTaskDao.delete(uploadTask);

                adapter.notifyDataSetChanged();

            }
        }
    }


    public class MyUploadListener extends UploadListener {

        /**
         * 上传进行时回调
         *
         * @param uploadTask
         */
        @Override
        public void onProgress(UploadTask uploadTask) {
            Log.v(TAG, "onProgress");
            if (getUserTag() == null) return;
            ViewHolder holder = (ViewHolder) getUserTag();
            holder.refresh();
        }
    }


}
