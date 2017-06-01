package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 自定义ViewPager
 * Created by Administrator on 2016/1/7.
 */
public class MyViewPager extends ViewPager {

    private LinearLayout.LayoutParams imagebtn_params = null;
    private boolean scroll = true;

    public MyViewPager(Context context) {
        super(context);
//        inti();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        inti();
    }

//    private void inti() {
//        imagebtn_params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//    }
//
//
//    public void setWHeight(int height, int width) {
//        imagebtn_params.height = height;
//        imagebtn_params.width = width;
//        this.setLayoutParams(imagebtn_params);
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
////            if (scroll) {
////                return super.dispatchTouchEvent(ev);
////            }
//            return scroll;// 禁止Gridview进行滑动
//        }
//        return super.dispatchTouchEvent(ev);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            if (!scroll){
//                return false;
//            }
//        }
//        return scroll;
//    }

//
//    public boolean isScrollble() {
//        return scroll;
//    }
//
//    public void setScrollble(boolean scroll) {
//        this.scroll = scroll;
//    }

}
