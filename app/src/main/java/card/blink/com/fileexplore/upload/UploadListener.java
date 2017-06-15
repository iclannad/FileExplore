package card.blink.com.fileexplore.upload;

import card.blink.com.fileexplore.model.UploadTask;

/**
 * Created by Administrator on 2017/6/15.
 */

public abstract class UploadListener {

    private Object userTag;

    /** 上传进行时回调 */
    public abstract void onProgress(UploadTask uploadTask);

    /** 类似View的Tag功能，主要用在listView更新数据的时候，防止数据错乱 */
    public Object getUserTag() {
        return userTag;
    }

    /** 类似View的Tag功能，主要用在listView更新数据的时候，防止数据错乱 */
    public void setUserTag(Object userTag) {
        this.userTag = userTag;
    }

}
