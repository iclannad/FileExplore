package card.blink.com.fileexplore.model;

import android.os.Handler;

import card.blink.com.fileexplore.service.UploadTaskCallback;
import card.blink.com.fileexplore.upload.UploadListener;

/**
 * Created by Administrator on 2017/6/9.
 */
public class UploadTask {

    public int id;
    public int status;
    public String name;
    public String fromUrl;
    public String toUrl;
    public Handler handler;
    public UploadListener uploadListener;

    public long index;
    public long count;
    public long fileSize;

    public long time;


    public UploadTaskCallback uploadTaskCallback;


}
