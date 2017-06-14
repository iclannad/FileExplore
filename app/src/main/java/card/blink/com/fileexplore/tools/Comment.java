package card.blink.com.fileexplore.tools;

import java.util.ArrayList;

import card.blink.com.fileexplore.model.UploadTask;

/**
 * Created by Administrator on 2017/6/1.
 */
public class Comment {
    // url
    public final static String HOST = "http://192.168.16.1:8080/media";

    // 任务列表
    public final static ArrayList<UploadTask> TASK_ARRAY_LIST = new ArrayList<>();

    // 任务的状态,分别为开始前，进行中，完成后
    public final static int BEFORE = 0;
    public final static int RUNNING = 1;
    public final static int AFTER = 2;

    public static final int ERROR = -1;
    public static final int SUCCESS = 1;
    public static final int UPLOAD_SUCCESS = 2;


}
