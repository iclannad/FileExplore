package card.blink.com.fileexplore.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.activity.base.UploadAndDownloadBaseActivity;
import card.blink.com.fileexplore.service.UploadService;
import card.blink.com.fileexplore.upload.UploadManager;
import card.blink.com.fileexplore.view.NumberProgressBar;

/**
 * Created by Administrator on 2017/6/13.
 */

public class UploadManagerActivity extends UploadAndDownloadBaseActivity {

    private static final String TAG = UploadManagerActivity.class.getSimpleName();

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
    }

    @OnClick(R.id.pauseAll)
    public void pauseAll() {
        Log.v(TAG, "pauseAll");
    }

    @OnClick(R.id.stopAll)
    public void stopAll() {
        Log.v(TAG, "stopAll");
    }

    @OnClick(R.id.startAll)
    public void startAll() {
        Log.v(TAG, "startAll");
    }


    private class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return 5;

        }


        @Override
        public Object getItem(int position) {

            return null;
        }


        @Override
        public long getItemId(int position) {

            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(UploadManagerActivity.this, R.layout.item_upload_manager, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }
    }


    private class ViewHolder implements View.OnClickListener {

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

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

        }
    }


}
