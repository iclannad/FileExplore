package card.blink.com.fileexplore.upload;

import java.util.List;

import card.blink.com.fileexplore.model.UploadTask;
import card.blink.com.fileexplore.tools.Comment;

/**
 * Created by Administrator on 2017/6/14.
 */

public class UploadManager {

    //定义上传状态常量
    public static final int NONE = 0;         //无状态  --> 等待
    public static final int WAITING = 1;      //等待    --> 上传，暂停
    public static final int UPLOADING = 2;  //上传中  --> 暂停，完成，错误
    public static final int PAUSE = 3;        //暂停    --> 等待，上传
    public static final int FINISH = 4;       //完成    --> 重新上传
    public static final int ERROR = 5;        //错误    --> 等待

    private static UploadManager mInstance;


    /**
     * 单例模式
     *
     * @return
     */
    public static UploadManager getInstance() {
        if (null == mInstance) {
            synchronized (UploadManager.class) {
                if (null == mInstance) {
                    mInstance = new UploadManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 私有构造方法
     */
    private UploadManager() {

    }


    /**
     * 返回上传任务列表
     *
     * @return
     */
    public List<UploadTask> getAllTask() {
        return Comment.TASK_ARRAY_LIST;
    }

    /**
     * 添加任务到上传列表中
     */
    public void addTask(UploadTask uploadTask) {
        Comment.TASK_ARRAY_LIST.add(uploadTask);
    }

    /**
     * 清除上传列表中所有任务
     */
    public void clearAllTask() {
        Comment.TASK_ARRAY_LIST.clear();
    }
}
