package card.blink.com.httpdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import card.blink.com.httpdemo.R;
import card.blink.com.httpdemo.adapter.TransportAdapter;
import card.blink.com.httpdemo.service.DownloadingService;
import card.blink.com.httpdemo.tool.Comment;
import card.blink.com.httpdemo.tool.DownloadCallback;

/**
 * Created by Administrator on 2017/6/6.
 */
public class TransportActivity extends Activity implements DownloadCallback{

    ArrayList<Task> list;

    @InjectView(R.id.lv)
    ListView lv;
    private TransportAdapter transportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.transport);
        ButterKnife.inject(this);

        list = Comment.list;

        transportAdapter = new TransportAdapter(list, this);
        lv.setAdapter(transportAdapter);
        DownloadingService.setProgress(this);

    }

    @Override
    public void progress(ArrayList<Task> list) {
        transportAdapter.setList(list);
    }
}
