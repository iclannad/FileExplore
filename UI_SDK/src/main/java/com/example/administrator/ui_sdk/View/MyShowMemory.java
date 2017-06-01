package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.example.administrator.ui_sdk.MyCircleLoading;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/8/15.
 */
public class MyShowMemory extends View {


    private Canvas canvas = null;
    private Context context = null;

    private int width, height;

    private int left, top, right, bottom;

    public MyShowMemory(Context context) {
        super(context);
        this.context = context;
    }

    public MyShowMemory(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyShowMemory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.width = BaseActivity.width;
        this.height = BaseActivity.height;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        setSize(width / 2 - DensityUtil.dip2px(context, 80),
                0 + DensityUtil.dip2px(context, 20),
                width / 2 + DensityUtil.dip2px(context, 80),
                DensityUtil.dip2px(context, 180));
    }

    private Paint paint = null;
    private Paint paintBack = null;
    private Paint paintText = null;
    //设置默认颜色为蓝色
    private int paintColor = 0xff88BEE3;
    private int paintTxtColor = 0xff88BEE3;
    private int paintTxtBackColor = 0xffDCDCDC;
    //设置默认画笔的宽度为3
    private float paintSize = 10f;
    private float paintTxtSize = 10f;
    //设置默认的字体大小
    private int textSize = 14;

    private void init() {
        paint = new Paint();
        paintBack = new Paint();
        paintText = new Paint();

        paint.setAntiAlias(true);       //设置没有锯齿
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintSize);              //线宽
        paint.setStyle(Paint.Style.STROKE);

        paintBack.setAntiAlias(true);       //设置没有锯齿
        paintBack.setColor(paintTxtBackColor);
        paintBack.setStrokeWidth(paintSize);              //线宽
        paintBack.setStyle(Paint.Style.STROKE);

        paintText.setAntiAlias(true);       //设置没有锯齿
        paintText.setColor(paintTxtColor);
        paintText.setStrokeWidth(paintTxtSize);              //线宽
        paintText.setTextSize(DensityUtil.dip2px(context, textSize));

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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;
        init();

        drawTxt();
        drawA();
    }

    private String Total = "";
    private String Use = "";

    public void setTextMemory(String Total, String Use) {
        this.Total = Total;
        this.Use = Use;
        setSweepAngle(Total, Use);
    }


    private void drawTxt() {
        canvas.drawText(Use + "/" + Total, width / 2 - DensityUtil.dip2px(context, textSize) * Use.length() / 2, (bottom + top) / 2 + DensityUtil.dip2px(context, textSize / 2), paintText);
    }

    private double nowSweepAngle = 0;
    private float SweepAngle = 360;


    private float SweepAngleTotal = 0;
    private float SweepAngleUse = 0;

    private void setSweepAngle(String Total, String Use) {
        double Proportion = Double.parseDouble(Use.substring(0, Use.length() - 2)) / Double.parseDouble(Total.substring(0, Total.length() - 2));
        SweepAngleUse = SweepAngle - (float) (Proportion * SweepAngle);
    }

    private void drawA() {
        RectF oval = new RectF();                     //RectF对象
        oval.left = left;                              //左边
        oval.top = top;                                   //上边
        oval.right = right;                                     //右边
        oval.bottom = bottom;
        canvas.drawArc(oval, 0, -SweepAngle, false, paintBack);
        canvas.drawArc(oval, -90, SweepAngleUse, false, paint);
    }

    /**
     * 开始清理动画
     */
    private float SweepAngleUses = 0;
    private float SweepAngleFlag = 0;
    private int time = 1000;
    private int speed = 25;
    private MyCircleLoading.ShowMemory showMemory = null;

    public void StartAnimation(MyCircleLoading.ShowMemory showMemory) {
        this.SweepAngleUses = SweepAngleUse;
        SweepAngleFlag = 0;
        this.showMemory = showMemory;
        new Thread(new MyRunnable()).start();
    }


    private class MyRunnable implements Runnable {

        @Override
        public void run() {

            while (SweepAngleUse > SweepAngleFlag) {
                SweepAngleUse -= SweepAngleUses / (time / speed);
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
            new Thread(new MyRunnableRestart()).start();
        }
    }


    private class MyRunnableRestart implements Runnable {

        @Override
        public void run() {
            SweepAngleFlag = SweepAngleUses;
            while (SweepAngleUse <= SweepAngleFlag) {
                SweepAngleUse += SweepAngleUses / (time / speed);
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
        }
    }


    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == 1)
                invalidate();
            if (msg.what == 0)
                showMemory.AnimationEnd();
        }
    };
}
