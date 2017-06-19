package card.blink.com.fileexplore.upload;

import android.graphics.Paint;

import com.wyk.greendaodemo.greendao.gen.UploadTaskDao;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import card.blink.com.fileexplore.model.UploadTask;
import card.blink.com.fileexplore.tools.Comment;

/**
 * Created by Administrator on 2017/6/14.
 */

public class UploadManager {


    // 记录状态转换
    public static final int NONE_TO_WAIT = 0;
    public static final int WAIT_TO_RUNNING = 1;
    public static final int RUNNING_TO_FINISH = 2;
    public static final int WAIT_TO_DELETE = 3;
    public static final int RUNNING_TO_DELETE = 4;
    public static final int FINISH_TO_DELETE = 5;
    public static final int WAIT_TO_PAUSE = 6;
    public static final int RUNNING_TO_PAUSE = 7;
    public static final int PAUSE_TO_DELETE = 8;
    public static final int PAUSE_TO_WAIT = 9;
    public static final int PAUSE_TO_RUNNING = 10;

    // 定义上传变量
    public static final int WAIT = 11;
    public static final int RUNING = 12;
    public static final int FINISH = 13;
    public static final int PAUSE = 14;
    public static final int DELETE = 15;


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
    public ArrayList<UploadTask> getAllTask() {
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
        Comment.TASK_ARRAY_LIST.remove(uploadTask);
    }

    /**
     * 暂停指定的任务
     *
     * @param uploadTask
     */
    public void pauseTask(UploadTask uploadTask) {
        uploadTask.status = UploadManager.PAUSE;
    }

    /**
     * 开始指定的任务
     */
    public void startTask(UploadTask uploadTask) {
        uploadTask.status = WAIT;
    }

    /**
     * 清除上传列表中所有任务
     * <p>
     * 在删除上传任务时，此时任务的状态有几种：
     * 等待，进行，完成，暂停，需要对这几种情况分别处理
     */
    public void clearAllTask() {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }
        for (int i = 0; i < Comment.TASK_ARRAY_LIST.size(); i++) {
            UploadTask uploadTask = Comment.TASK_ARRAY_LIST.get(i);
            if (uploadTask.status == WAIT) {
                uploadTask.status = DELETE;
                uploadTask.switch_status = WAIT_TO_DELETE;
            } else if (uploadTask.status == RUNING) {
                uploadTask.status = DELETE;
                uploadTask.switch_status = RUNNING_TO_DELETE;
                synchronized (uploadTask) {
                    uploadTask.notify();
                }
            } else if (uploadTask.status == FINISH) {
                uploadTask.status = DELETE;
                uploadTask.switch_status = FINISH_TO_DELETE;
            } else if (uploadTask.status == PAUSE) {
                uploadTask.status = DELETE;
                uploadTask.switch_status = PAUSE_TO_DELETE;
                synchronized (uploadTask) {
                    uploadTask.notify();
                }
            }
        }
        Comment.TASK_ARRAY_LIST.clear();
        // 更新数据库里面的状态
        UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();
        uploadTaskDao.deleteAll();
    }


    /**
     * 暂停所有的任务
     * <p>
     * 在暂停所有上传任务时，需要对几种情况进行处理：
     * 等待，进行，完成，暂停
     */
    public void pauseAllTask() {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }

        // 更新数据库里面的状态
        UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();

        for (int i = 0; i < Comment.TASK_ARRAY_LIST.size(); i++) {
            UploadTask uploadTask = Comment.TASK_ARRAY_LIST.get(i);
            if (uploadTask.status == WAIT) {
                uploadTask.status = PAUSE;
                uploadTask.switch_status = WAIT_TO_PAUSE;
            } else if (uploadTask.status == RUNING) {
                uploadTask.status = PAUSE;
                uploadTask.switch_status = RUNNING_TO_PAUSE;
            } else if (uploadTask.status == FINISH) {
                // 不处理
            } else if (uploadTask.status == PAUSE) {
                // 不处理
            }
            uploadTaskDao.update(uploadTask);
        }

    }

    /**
     * 开始所有的任务
     * 在开始所有上传任务时，需要对几种情况进行处理：
     * 等待，进行，完成，暂停
     */
    public void startAllTask() {
        if (Comment.TASK_ARRAY_LIST.isEmpty()) {
            return;
        }
        // 更新数据库里面的状态
        UploadTaskDao uploadTaskDao = GreenDaoManager.getInstance().getSession().getUploadTaskDao();

        for (int i = 0; i < Comment.TASK_ARRAY_LIST.size(); i++) {
            UploadTask uploadTask = Comment.TASK_ARRAY_LIST.get(i);
            if (uploadTask.status == WAIT) {
                // 不处理
            } else if (uploadTask.status == RUNING) {
                // 不处理
            } else if (uploadTask.status == FINISH) {
                // 不处理
            } else if (uploadTask.status == PAUSE) {
                if (uploadTask.switch_status == UploadManager.WAIT_TO_PAUSE) {
                    uploadTask.status = WAIT;
                    uploadTask.switch_status = PAUSE_TO_WAIT;
                } else if (uploadTask.switch_status == UploadManager.RUNNING_TO_PAUSE) {
                    uploadTask.status = RUNING;
                    uploadTask.switch_status = PAUSE_TO_RUNNING;
                    synchronized (uploadTask) {
                        uploadTask.notify();
                    }
                }
            }

            // 更新状态值
            uploadTaskDao.update(uploadTask);
        }
    }


    /**
     * 更新上传任务列表
     */
    public void updateUploadTaskList(ArrayList<UploadTask> list) {
        Comment.TASK_ARRAY_LIST.clear();
        if (list == null) {
            return;
        }
        Comment.TASK_ARRAY_LIST.addAll(list);
    }
}
