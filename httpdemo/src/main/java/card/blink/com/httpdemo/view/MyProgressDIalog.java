package card.blink.com.httpdemo.view;

import android.content.Context;


/**
 * 提示框
 */
public class MyProgressDIalog {
    private Context context;
    private static com.gc.materialdesign.widgets.ProgressDialog mProgressDialog;
    private static MyProgressDIalog instance = null;

    public MyProgressDIalog(Context context) {
        this.context = context;
    }

    public static synchronized MyProgressDIalog getInstance(Context context) {
        if (instance == null) {
            instance = new MyProgressDIalog(context);
        }
        if (context != null) {
            instance = new MyProgressDIalog(context);
        }

        return instance;
    }

    public MyProgressDIalog setContent(String content) {
        mProgressDialog = new com.gc.materialdesign.widgets.ProgressDialog(context, content);
        return this;
    }

    /**
     * 显示对话框
     */
    public void showProgressDialog() {
        mProgressDialog.setCancelable(false);
        try {
            mProgressDialog.show();
        } catch (Exception e) {

        }
    }

    /**
     * 关闭对话框
     */
    public void dissmissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}
