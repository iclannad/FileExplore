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
     * 从任务列表中删除任务
     *
     * @param uploadTask
     */
    public void removeTask(UploadTask uploadTask) {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }
        // 在任务暂停过程中删除任务
        if (uploadTask.status == Comment.PAUSE) {
            uploadTask.status = Comment.DELETE;
            synchronized (uploadTask) {
                uploadTask.notify();
            }
        }

        uploadTask.status = Comment.DELETE;
        Comment.TASK_ARRAY_LIST.remove(uploadTask);
    }

    /**
     * 暂停指定的任务
     * @param uploadTask
     */
    public void pauseTask(UploadTask uploadTask) {
        uploadTask.status = Comment.PAUSE;
    }

    /**
     * 开始指定的任务
     */
    public void startTask(UploadTask uploadTask) {
        uploadTask.status = Comment.RUNNING;
    }

    /**
     * 清除上传列表中所有任务
     */
    public void clearAllTask() {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }

        for (int i = 0; i < Comment.TASK_ARRAY_LIST.size(); i++) {
            UploadTask uploadTask = Comment.TASK_ARRAY_LIST.get(i);
            uploadTask.status = Comment.DELETE;
        }
        Comment.TASK_ARRAY_LIST.clear();
    }


    /**
     * 暂停所有的任务
     */
    public void pauseAllTask() {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }

        for (int i = 0; i < Comment.TASK_ARRAY_LIST.size(); i++) {
            UploadTask uploadTask = Comment.TASK_ARRAY_LIST.get(i);

            if (uploadTask.status == Comment.PAUSE) {
                break;
            }

            if (uploadTask.status == Comment.RUNNING) {
                uploadTask.status = Comment.PAUSE;
                break;
            }

        }
    }

    /**
     * 开始所有的任务
     */
    public void startAllTask() {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }

        for (int i = 0; i < Comment.TASK_ARRAY_LIST.size(); i++) {
            UploadTask uploadTask = Comment.TASK_ARRAY_LIST.get(i);
            if (uploadTask.status == Comment.PAUSE) {
                synchronized (uploadTask) {
                    uploadTask.status = Comment.RUNNING;
                    uploadTask.notify();
                    break;
                }
            }
        }
    }


}
