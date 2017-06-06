package card.blink.com.httpdemo.tool;

import java.util.ArrayList;

import card.blink.com.httpdemo.activity.Task;

/**
 * Created by Administrator on 2017/6/6.
 */
public interface DownloadCallback {
    void progress(ArrayList<Task> list);
}
