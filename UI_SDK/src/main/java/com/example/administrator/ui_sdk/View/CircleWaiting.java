package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.example.administrator.ui_sdk.MyCircleLoading;

/**
 * Created by Administrator on 2016/8/10.
 */
public class CircleWaiting extends View {

    private Canvas canvas = null;
    private Context context = null;

    public CircleWaiting(Context context) {
        super(context);
    }

    public CircleWaiting(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CircleWaiting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        canvas.drawColor(Color.WHITE);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        width = wm.getDefaultDisplay().getWidth();
//        height = wm.getDefaultDisplay().getHeight();

        this.width = BaseActivity.width;
        this.height = BaseActivity.height;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        setSize(width / 2 - DensityUtil.dip2px(context, 20),
                height / 2 - DensityUtil.dip2px(context, 20),
                width / 2 + DensityUtil.dip2px(context, 20),
                height / 2 + DensityUtil.dip2px(context, 20));

    }


    private Paint paint = null;
    private Paint paintText = null;
    //设置默认颜色为蓝色
    private int paintColor = 0xff88BEE3;
    private int paintTxtColor = 0xff88BEE3;
    //设置默认画笔的宽度为3
    private float paintSize = 5f;
    private float paintTxtSize = 5f;
    //设置默认的字体大小
    private int textSize = 14;


    private int left, top, right, bottom;
    private int width, height;

    //实时的角度
    private double nowSweepAngle = 0;
    //默认的角度
    private double sweepAngle = 360;
    //默认转速
    private int speed = 10;

    //设置默认时间为20秒
    private int time = 20000;
    //当前实时的时间
    private int nowTime;

    public final static int NOW = 0;
    public final static int END = 1;
    public final static int START = 2;

    private void init() {
        paint = new Paint();
        paintText = new Paint();

        paint.setAntiAlias(true);       //设置没有锯齿
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintSize);              //线宽
        paint.setStyle(Paint.Style.STROKE);

        paintText.setAntiAlias(true);       //设置没有锯齿
        paintText.setColor(paintTxtColor);
        paintText.setStrokeWidth(paintTxtSize);              //线宽
        paintText.setTextSize(DensityUtil.dip2px(context, textSize));

        drawA();

        drawTxt();
    }

    /**
     * 设置画笔的颜色
     *
     * @param color
     */
    public void setPaintColor(int color) {
        paintColor = getResources().getColor(color);
    }

    /**
     * 设置动画的位置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setSize(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * 开始动画
     */
    public void setStart() {
        new Thread(new MyRunnableStart()).start();
    }

    /**
     * 结束动画
     *
     * @param STATE
     */
    public void setStop(int STATE) {
        switch (STATE) {
            case NOW:
                break;
            case END:
                break;
            case START:
                break;
        }
    }

    private double nowNumber = 0;
    private double totalNumber = 100;

    private void drawTxt() {
        canvas.drawText(((int) nowNumber) + "", width / 2 - DensityUtil.dip2px(context, textSize / 2), height / 2 + DensityUtil.dip2px(context, textSize / 2), paintText);
    }


    private void drawA() {
        RectF oval = new RectF();                     //RectF对象
        oval.left = left;                              //左边
        oval.top = top;                                   //上边
        oval.right = right;                                     //右边
        oval.bottom = bottom;
        canvas.drawArc(oval, 0, (float) -nowSweepAngle, false, paint);
    }

    private class MyRunnableStart implements Runnable {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            Message msgWait = new Message();
            nowTime = time / speed;
            //设置当前状态处于正在进行动画状态
            msgWait.arg1 = 0;
            handlerWait.sendMessage(msgWait);
            while (nowSweepAngle <= sweepAngle) {
                nowSweepAngle += sweepAngle / nowTime;
                nowNumber += totalNumber / nowTime;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                handler.sendMessage(msg);
            }
            msgWait = new Message();
            //设置当前状态为结束状态
            msgWait.arg1 = 1;
            handlerWait.sendMessage(msgWait);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            invalidate();
        }
    };


    private Handler handlerWait = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    waiting.AnimationState(false);
                    break;
                case 1:
                    waiting.AnimationState(true);
                    break;
            }
        }
    };


    private MyCircleLoading.Waiting waiting = null;

    public void setAnimationState(MyCircleLoading.Waiting waiting) {
        this.waiting = waiting;
    }


}
