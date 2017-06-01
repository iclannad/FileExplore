package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/11.
 * <p/>
 * 这是一个重写ImageView 的类，就是实现点击按下会出现变暗的效果
 * <p/>
 * GestureDetector是监听手势操作的接口
 */
public class MyImageView extends ImageView implements GestureDetector.OnGestureListener {

    //监听手势
    private GestureDetector gestureDetector = null;


    public MyImageView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, this);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, this);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(context, this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在cancel里将滤镜取消，注意不要捕获cacncel事件,mGestureDetector里有对cancel的捕获操作
        //在滑动GridView时，AbsListView会拦截掉Move和UP事件，直接给子控件返回Cancel
        if (event.getAction() == MotionEvent.ACTION_UP) {
            removeFilter();
        }
        return gestureDetector.onTouchEvent(event);
    }


    /**
     * 获取imageView上面的图片
     *
     * @return
     */
    private Drawable getViewDrawable() {
        //获取imageView的src的图片
        Drawable drawable = this.getDrawable();
        //如果src的图片为空
        if (drawable == null) {
            //则获取imgaView的背景图片
            drawable = this.getBackground();
        }
        return drawable;
    }

    /**
     * 设置滤镜
     */
    private void setFilter() {
        try {
            getViewDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } catch (Exception e) {
            Log.e("Ruan", "ImageView没有设置背景图片获取没有设置图片");
        }
    }

    /**
     * 清除滤镜
     */
    private void removeFilter() {
        try {
            getViewDrawable().clearColorFilter();
        } catch (Exception e) {
            Log.e("Ruan", "ImageView没有设置背景图片获取没有设置图片");
        }
    }


    //这个是手势按下的操作
    @Override
    public boolean onDown(MotionEvent e) {
        //当按下就设置滤镜
        setFilter();
        return true;
    }

    //按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，那么onShowPress就会执行
    @Override
    public void onShowPress(MotionEvent e) {

    }

    //手势轻点的操作
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        removeFilter();
        performClick();
        return false;
    }

    //手势滑动的操作
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    //手势长按的操作
    @Override
    public void onLongPress(MotionEvent e) {
        //长安时，手动触发长安事件
        performLongClick();
    }

    //滑屏，用户按下触摸屏、快速移动后松开
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
