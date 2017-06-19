package card.blink.com.fileexplore.model;

import android.os.Handler;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.security.PublicKey;

import card.blink.com.fileexplore.service.UploadTaskCallback;
import card.blink.com.fileexplore.upload.UploadListener;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/9.
 */
@Entity
public class UploadTask {

    @Id
    public Long id;
    public int status;
    public int switch_status;
    public String name;
    public String fromUrl;
    public String toUrl;
    public long index;
    public long count;

    @Transient
    public long fileSize;
    @Transient
    public long time;
    @Transient
    public Handler handler;
    @Transient
    public UploadListener uploadListener;
    @Transient
    public UploadTaskCallback uploadTaskCallback;
    public long getIndex() {
        return this.index;
    }
    public void setIndex(long index) {
        this.index = index;
    }
    public String getToUrl() {
        return this.toUrl;
    }
    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }
    public String getFromUrl() {
        return this.fromUrl;
    }
    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSwitch_status() {
        return this.switch_status;
    }
    public void setSwitch_status(int switch_status) {
        this.switch_status = switch_status;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getCount() {
        return this.count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    @Generated(hash = 766294870)
    public UploadTask(Long id, int status, int switch_status, String name,
            String fromUrl, String toUrl, long index, long count) {
        this.id = id;
        this.status = status;
        this.switch_status = switch_status;
        this.name = name;
        this.fromUrl = fromUrl;
        this.toUrl = toUrl;
        this.index = index;
        this.count = count;
    }
    @Generated(hash = 976210108)
    public UploadTask() {
    }

}
