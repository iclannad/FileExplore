package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.example.administrator.ui_sdk.R;


/**
 * 自定义Button
 * Created by Administrator on 2016/1/7.
 */
public class MyButton extends Button {

    private Context context = null;
    public MyButton(Context context) {
        super(context);
        this.context = context;
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    /**
     * 这个是禁止按钮的点击事件
     *
     * @param clickable
     */
    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        if (clickable)
            this.setBackground(this.getBackground());
        else {
            Log.e("Ruan", "" + this.isClickable() + "--------" + this.isEnabled());
            this.setBackgroundResource(R.drawable.button_down_blue);
        }
    }

    /**
     * 这个是禁止按钮点击颜色变颜色
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled)
            this.setBackgroundResource(R.drawable.button_selector_blue);
        else {
            this.setBackgroundResource(R.drawable.button_down_blue);
        }
    }
}
