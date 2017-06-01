package com.example.administrator.ui_sdk.Other;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.administrator.ui_sdk.MyWebClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/1/6.
 */
public class MyWebChromeClient extends WebChromeClient {

    private MyWebClient myWebClient = null;
    private Timer timer = null;
    private boolean isFirst = false;

    public MyWebChromeClient(MyWebClient myWebClient) {
        this.myWebClient = myWebClient;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (myWebClient != null) {
            //加载完成
            if (newProgress == 100) {
                if (isFirst)
                    return;
                if (timer != null)
                    timer.cancel();
                timer = new Timer();
                timer.schedule(new MyTask(), 2000, 2000);
            } else {
                //加载中处理的事件
                myWebClient.Loading();
            }
        }
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            handler.sendMessage(msg);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            isFirst = true;
            timer.cancel();
            myWebClient.Ending();
        }
    };
}
