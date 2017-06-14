package card.blink.com.fileexplore.service;

/**
 * Created by Administrator on 2017/6/13.
 */

public interface UploadTaskCallback {

    /**
     * 任务上传开始前
     */
    void start();

    /**
     * 任务上传中
     *
     * @param index 表示当前已经上传了第几块
     * @param total 表示总共有几块
     */
    void uploading(int index, int total);

    /**
     * 任务上传结束
     */
    void finished();
}
