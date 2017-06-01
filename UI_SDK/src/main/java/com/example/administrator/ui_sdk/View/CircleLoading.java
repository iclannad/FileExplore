package com.example.administrator.ui_sdk.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyCircleLoading;
import com.example.administrator.ui_sdk.R;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CircleLoading extends View {

    private Paint paint = null;
    private Paint paintCircle = null;
    private Canvas canvas = null;
    private double nowSweepAngle = 0;

    private int time = 0;
    //设置默认转速为25ms
    private int speed = 5;
    private int nowTime = 0;
    //控制是否停止动画
    private boolean stop = false;

    public final static int NOW = 0;
    public final static int END = 1;
    public final static int START = 2;

    private Context context = null;

    private int cX, cY;
    private double radius;

    private MyCircleLoading myCircleLoading = null;


    //默认5.0宽度
    private float size = 5.0f;
    //默认黑色
    private int paintcolor = 0xff000000;
    //默认是灰色
    private int colorBack = 0xffc0c0c0;

    //默认是蓝色
    private int backColor = 0xff88BEE3;

    private int downX, downY;
    private double nowRadius = 0;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public CircleLoading(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #(Context, AttributeSet, int)
     */
    public CircleLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getWindows();
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #(Context, AttributeSet)
     */
    public CircleLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getWindows();
    }

    private void getWindows() {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    private int width, height;
    private int left, top, right, bottom;
    private double sweepAngle = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        setSize(width / 2 - DensityUtil.dip2px(context, 30),
                height - DensityUtil.dip2px(context, 180),
                width / 2 + DensityUtil.dip2px(context, 30),
                height - DensityUtil.dip2px(context, 120));

    }

    private Paint paintBack = null;
    private Paint paintWhite = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;
        paint = new Paint();
        paintCircle = new Paint();
        paintBack = new Paint();
        paintWhite = new Paint();

        paintWhite.setAntiAlias(true);                       //设置画笔为无锯齿
        paintWhite.setColor(getResources().getColor(R.color.White));
        paintWhite.setTextSize(DensityUtil.dip2px(context, textSize));

        paint.setAntiAlias(true);                       //设置画笔为无锯齿
        paint.setColor(getResources().getColor(R.color.Red));                    //设置画笔颜色
//        canvas.drawColor(getResources().getColor(R.color.WhiteSmoke));                  //白色背景
        paint.setStrokeWidth(size);              //线宽
        paint.setStyle(Paint.Style.STROKE);

        paintBack.setAntiAlias(true);                       //设置画笔为无锯齿
        paintBack.setColor(colorBack);                    //设置画笔颜色
//        canvas.drawColor(Color.WHITE);                  //白色背景
        paintBack.setStrokeWidth(size);              //线宽
        paintBack.setStyle(Paint.Style.STROKE);

        paintCircle.setAntiAlias(true);                       //设置画笔为无锯齿
        paintCircle.setColor(getResources().getColor(R.color.Blue1));
        paintCircle.setStrokeWidth(size);              //线宽

        this.canvas.drawColor(getResources().getColor(android.R.color.transparent));

        drawA();
        drawC();

        Rect bounds = new Rect();
        paintWhite.getTextBounds(msg, 0, msg.length(), bounds);
        canvas.drawText(msg, getMeasuredWidth() / 2 - bounds.width() / 2, cY + DensityUtil.dip2px(context, textSize / 2), paintWhite);

    }

    private int textSize = 14;

    private void drawC() {
        canvas.drawCircle(cX, cY, (int) nowRadius, paintCircle);
    }

    private void drawA() {
        RectF oval = new RectF();                     //RectF对象
        oval.left = left;                              //左边
        oval.top = top;                                   //上边
        oval.right = right;                                     //右边
        oval.bottom = bottom;
        canvas.drawArc(oval, 0, (float) sweepAngle, false, paintBack);

        canvas.drawArc(oval, 0, (float) -nowSweepAngle, false, paint);
    }

    /**
     * 设置圆的位置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setSize(int left, int top, int right, int bottom) {
        this.left = left - 8;
        this.top = top - 8;
        this.right = right + 8;
        this.bottom = bottom + 8;

        cX = left + (right - left) / 2;
        cY = top + (bottom - top) / 2;
        radius = (right - left) / 2;
        nowRadius = radius;
    }

    /**
     * 设置转动的宽度
     *
     * @param size
     */
    public void setPaintSize(float size) {
        this.size = size;
    }


    private String msg = null;

    public void setTextCircle(String msg) {
        this.msg = msg;
    }

    /**
     * 设置画笔的颜色
     *
     * @param paintcolor
     */
//    public void setPaintColor(int paintcolor) {
//        this.paintcolor = paintcolor;
//    }
//
//    public void setPaintBackColor(int colorBack) {
//        this.colorBack = colorBack;
//    }
//
//    public void setBackColor(int backColor) {
//        this.backColor = backColor;
//    }


    /**
     * 设置转动的角度
     *
     * @param sweepAngle
     */
    public void setSweepAngle(double sweepAngle) {
        this.sweepAngle = sweepAngle;
        this.nowSweepAngle = sweepAngle;
    }


    /**
     * 设置转动时间
     *
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
        this.nowTime = this.time / speed;
    }

    public void setStart() {
        new Thread(new MyRunnableRestart()).start();
    }


    public void setStop(int state) {
        switch (state) {
            case NOW:
                stop = true;
                break;
            case END:
                nowTime = 100;
                break;
            case START:
                nowTime = 150;
                break;
        }
    }

//    private boolean clickable = true;

//    public void setClickable(boolean clickable) {
//        this.clickable = clickable;
//        if (!clickable)
//            setBackColor(R.color.GreyDeep);
//        else
//            setBackColor(backColor);
//        invalidate();
//    }

    /**
     * 获取当前状态
     *
     * @return
     */
    public boolean getNowState() {
        return state;
    }

    private boolean state = false;

    private class MyRunnableStart implements Runnable {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            nowTime = time / speed;
            while (nowSweepAngle <= sweepAngle) {
                state = true;
                nowSweepAngle += sweepAngle / nowTime;
                nowRadius += radius / nowTime;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                handler.sendMessage(msg);
            }
            state = false;
        }
    }


    private class MyRunnableRestart implements Runnable {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            while (nowSweepAngle >= 0) {
                setStop(CircleLoading.START);
                nowSweepAngle -= sweepAngle / nowTime;
                nowRadius -= radius / nowTime;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                handler.sendMessage(msg);
            }
            new Thread(new MyRunnableStart()).start();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            invalidate();
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
//                if (clickable) {
                if (isClick() && myCircleLoading != null)
                    myCircleLoading.circleClick(this);
//                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setClick(MyCircleLoading myCircleLoading) {
        this.myCircleLoading = myCircleLoading;
    }


    private boolean isClick() {
        if ((downX >= left && downX <= right) && (downY >= top && downY <= bottom))
            return true;
        return false;
    }
}
