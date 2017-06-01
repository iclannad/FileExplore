package com.example.administrator.ui_sdk.Other;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/1/6.
 */
public class MyWebViewClient extends WebViewClient {

    /**
     * 加载网页直接调用内核浏览器
     *
     * @param view
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        if (url.startsWith("http:") || url.startsWith("https:")) {
//            return false;
//        }
//        view.loadUrl(url);
//        return true;
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 控制手机按钮调用方法
     *
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    /**
     * 页面加载时调用的方法
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    /**
     * 页面加载完成是加载的方法
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

}
