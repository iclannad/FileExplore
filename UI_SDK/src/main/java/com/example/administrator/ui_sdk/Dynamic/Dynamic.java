package com.example.administrator.ui_sdk.Dynamic;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/2.
 */
public class Dynamic {

    /**
     * 动态创建Linearlayout
     *
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static LinearLayout getDynamicLinearyout(Context context, int width, int height) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        return linearLayout;
    }

    /**
     * 动态创建relativeLayout
     *
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static RelativeLayout getDynamicRelativeLayout(Context context, int width, int height) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        return relativeLayout;
    }

    /**
     * 动态创建TextView
     *
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static TextView getDynamicTextView(Context context, int width, int height) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        return textView;
    }
}
