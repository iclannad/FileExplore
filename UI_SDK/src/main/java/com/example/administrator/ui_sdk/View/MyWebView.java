package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.administrator.ui_sdk.MyWebClient;
import com.example.administrator.ui_sdk.Other.MyWebChromeClient;
import com.example.administrator.ui_sdk.Other.MyWebJSInterface;
import com.example.administrator.ui_sdk.Other.MyWebViewClient;


/**
 * Created by Administrator on 2016/1/6.
 */
public class MyWebView extends WebView {

    private Context context = null;
    private String url = null;
    private WebSettings settings = null;
    private MyWebClient myWebClient = null;

    public MyWebView(Context context) {
        super(context);
        this.context = context;
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setWebView(String url, MyWebClient myWebClient) {
        this.url = url;
        this.myWebClient = myWebClient;
        inti();
    }


    private void inti() {
        this.setWebViewClient(new MyWebViewClient());
        this.setWebChromeClient(new MyWebChromeClient(myWebClient));
        //添加链接
        this.loadUrl(url);
        settings = this.getSettings();
        //设置webview支持javascript
        settings.setJavaScriptEnabled(true);

        // 设置可以使用localStorage
        settings.setDomStorageEnabled(true);
        // 应用可以有数据库
        settings.setDatabaseEnabled(true);
        // 应用可以有缓存
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);   // 默认使用缓存
        settings.setAllowFileAccess(true);   // 可以读取文件缓存(manifest生效)


        //设置webview与js实现交互
        this.addJavascriptInterface(new MyWebJSInterface(context), "JavaScriptInterface");

    }

    /**
     * 监控返回按钮。不是直接退出而是返回上一页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
            this.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
