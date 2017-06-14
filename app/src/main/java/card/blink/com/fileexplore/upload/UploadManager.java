package card.blink.com.fileexplore.upload;

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

    public static UploadManager getInstance() {

        return null;
    }
}
