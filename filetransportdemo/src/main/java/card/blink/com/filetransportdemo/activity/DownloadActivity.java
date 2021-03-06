package card.blink.com.filetransportdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import card.blink.com.filetransportdemo.R;
import card.blink.com.filetransportdemo.base.BaseActivity;
import card.blink.com.filetransportdemo.base.BaseRecyclerAdapter;
import card.blink.com.filetransportdemo.base.DividerItemDecoration;
import card.blink.com.filetransportdemo.model.ApkModel;

/**
 * Created by Administrator on 2017/6/10.
 */

public class DownloadActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.targetFolder)
    TextView targetFolder;
    @Bind(R.id.tvCorePoolSize)
    TextView tvCorePoolSize;
    @Bind(R.id.sbCorePoolSize)
    SeekBar sbCorePoolSize;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.openManager)
    Button openManager;

    private ArrayList<ApkModel> apks;
    private DownloadManager downloadManager;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initToolBar(toolbar, true, "下载管理");

        initData();
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");

        targetFolder.setText("下载路径: " + downloadManager.getTargetFolder());
        sbCorePoolSize.setMax(5);
        sbCorePoolSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                downloadManager.getThreadPool().setCorePoolSize(progress);
                tvCorePoolSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbCorePoolSize.setProgress(3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new MainAdapter(this);
        recyclerView.setAdapter(adapter);
        openManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DownloadManagerActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private class MainAdapter extends BaseRecyclerAdapter<ApkModel, ViewHolder> {

        public MainAdapter(Context context) {
            super(context, apks);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_download_details, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ApkModel apkModel = mDatas.get(position);
            holder.bind(apkModel);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.download)
        Button download;

        private ApkModel apkModel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ApkModel apkModel) {
            this.apkModel = apkModel;
            if (downloadManager.getDownloadInfo(apkModel.getUrl()) != null) {
                download.setText("已在队列");
                download.setEnabled(false);
            } else {
                download.setText("下载");
                download.setEnabled(true);
            }
            name.setText(apkModel.getName());
            Glide.with(getApplicationContext()).load(apkModel.getIconUrl()).error(R.mipmap.ic_launcher).into(icon);
            download.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.download) {
                if (downloadManager.getDownloadInfo(apkModel.getUrl()) != null) {
                    Toast.makeText(getApplicationContext(), "任务已经在下载列表中", Toast.LENGTH_SHORT).show();
                } else {
//                    GetRequest request = OkGo.get(apkModel.getUrl())//
//                            .headers("headerKey1", "headerValue1")//
//                            .headers("headerKey2", "headerValue2")//
//                            .params("paramKey1", "paramValue1")//
//                            .params("paramKey2", "paramValue2");
                    GetRequest request = OkGo.get(apkModel.getUrl());
                    downloadManager.addTask(apkModel.getUrl(), apkModel, request, null);
                    download.setText("已在队列");
                    download.setEnabled(false);
                }
            } else {
//                Intent intent = new Intent(getApplicationContext(), DesActivity.class);
//                intent.putExtra("apk", apkModel);
//                startActivity(intent);
            }
        }
    }

    private void initData() {
        apks = new ArrayList<>();
        ApkModel apkInfo1 = new ApkModel();
        apkInfo1.setName("jd.rar");
        apkInfo1.setIconUrl("http://pic3.apk8.com/small2/14325422596306671.png");
        //apkInfo1.setUrl("http://download.apk8.com/d2/soft/meilijia.apk");
        apkInfo1.setUrl("http://192.168.16.1:8080/media/sdb1/jd.rar");

        apks.add(apkInfo1);
        ApkModel apkInfo2 = new ApkModel();
        apkInfo2.setName("test.mp4");
        apkInfo2.setIconUrl("http://pic3.apk8.com/small2/14313175771828369.png");
        apkInfo2.setUrl("http://192.168.16.1:8080/media/sdb1/test.mp4");
        apks.add(apkInfo2);
        ApkModel apkInfo3 = new ApkModel();
        apkInfo3.setName("code.zip");
        apkInfo3.setIconUrl("http://pic3.apk8.com/small2/14308183888151824.png");
        apkInfo3.setUrl("http://192.168.16.1:8080/media/sdb1/code.zip");
        apks.add(apkInfo3);
        ApkModel apkInfo4 = new ApkModel();
        apkInfo4.setName("Aegean_Sea.mp4");
        apkInfo4.setIconUrl("http://pic3.apk8.com/small2/14302008166714263.png");
        apkInfo4.setUrl("http://192.168.16.1:8080/media/sdb1/Aegean_Sea.mp4");
        apks.add(apkInfo4);

        ApkModel apkInfo6 = new ApkModel();
        apkInfo6.setName("快的打车");
        apkInfo6.setIconUrl("http://up.apk8.com/small1/1439955061264.png");
        apkInfo6.setUrl("http://download.apk8.com/soft/2015/%E5%BF%AB%E7%9A%84%E6%89%93%E8%BD%A6.apk");
        apks.add(apkInfo6);

    }
}
