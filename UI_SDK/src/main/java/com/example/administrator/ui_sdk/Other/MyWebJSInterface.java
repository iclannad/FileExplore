package com.example.administrator.ui_sdk.Other;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/6.
 */
public class MyWebJSInterface {

    private Context context = null;

    public MyWebJSInterface(Context context) {
        this.context = context;
    }

    //webview中调用toast原生组件
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }
}
