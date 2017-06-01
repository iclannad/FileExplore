package com.example.administrator.ui_sdk.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.ItemClick;
import com.example.administrator.ui_sdk.R;

/**
 * Created by Administrator on 2016/7/20.
 */
public class RreshLinearLayout extends LinearLayout {

    //按下的X，Y开始的坐标
    private int downX, downY = 0;
    //移动的X, Y的坐标
    private int nowX, nowY = 0;

    private boolean isDrop = false;
    private int scroll = 0;

    private boolean isScroll = false;
    public static boolean isShow = false;

    private int dropHeight = 0;
    private int StopHeight = 0;

    private int Max = 0;
    private int Min = 0;
    private View TopView = null;

    private RefreshSideListView sideListView = null;

    private Context context = null;

    private TextView text = null;
    private ImageView image = null;
    private ProgressBar progress = null;


    public RreshLinearLayout(Context context) {
        this(context, null);
    }

    public RreshLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RreshLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        Max = DensityUtil.dip2px(context, 100);
        dropHeight = -DensityUtil.dip2px(context , 50);
        StopHeight = dropHeight;
        Min = dropHeight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View v = LayoutInflater.from(context).inflate(R.layout.listview_top, null);
        sideListView = (RefreshSideListView) this.getChildAt(0);
        sideListView.addHeaderView(v);
        TopView = v;
//
//
        text = (TextView) TopView.findViewById(com.example.administrator.ui_sdk.R.id.text);
        image = (ImageView) TopView.findViewById(com.example.administrator.ui_sdk.R.id.image);
        progress = (ProgressBar) TopView.findViewById(com.example.administrator.ui_sdk.R.id.progress);
//
//
//        addView(TopView);

    }

    /**
     * 重写触摸事件
     * <p/>
     * 监控 ACTION_DOWN   ACTION_UP   ACTION_MOVE
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

//        if (!isScroll)
//            return false;
//        else {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return ActionDown(ev);
            case MotionEvent.ACTION_MOVE:
                return ActionMove(ev);
            case MotionEvent.ACTION_UP:
                ActionUp(ev);
                break;
        }
        return super.onTouchEvent(ev);
//        }
    }

    private void ActionUp(MotionEvent ev) {
        if (isShow) {
            sideListView.setPadding(0, 0, 0, 0);
            StopHeight = 0;
            //启动刷新
            freshData();
        } else {
            sideListView.setPadding(0, dropHeight, 0, 0);
            StopHeight = dropHeight;
        }
        isDrop = false;
    }

    private boolean ActionMove(MotionEvent ev) {
        nowX = (int) ev.getX();
        nowY = (int) ev.getY();
        //先判断滑动的范围是不是向下拉动
        if ((Math.abs(nowY - downY) > Math.abs(nowX - downX) * 2 || isDrop) && !isScroll) {
            isScroll = false;
            isDrop = true;

            scroll = (nowY - downY) / 2;
            scroll += StopHeight;
            //当滑动距离大于侧滑菜单的宽度的时候设置最大为侧滑的宽度
            if (scroll >= Max)
                scroll = Max;
            //当滑动距离小于0的时候就不能再距离增加
            if (scroll < Min)
                scroll = Min;

            //判断下拉距离如果距离大于高度的一半就将当前状态设置为显示状态否则就设置不显示状态
            if (scroll >= Max / 2) {
                //下拉时到达一半以后提示放手可以刷新
                isShow = true;
                BitmapRotate(180, "松手即可刷新");
            } else {
                //当高度回到一定的高度放手则回复原来的样子
                if (scroll >= 0)
                    AnimationUp(scroll);
                else
                    BitmapRotate(0, "下拉即可刷新");
                isShow = false;
            }
            //保存当前下拉高度
            sideListView.setPadding(0, (scroll), 0, 0);
//            postInvalidate();
        }
        return super.onTouchEvent(ev);
    }

    private boolean ActionDown(MotionEvent ev) {

        downX = (int) ev.getX();
        downY = (int) ev.getY();

        sideListView.setPadding(0, StopHeight, 0, 0);

        return true;
    }


    /**
     * 自动恢复的动画
     */
    private float degrees = 180;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void AnimationUp(int scoll) {
        BitmapRotate((int) ((180 / (Max / 2 - DensityUtil.dip2px(context , 10)) * scoll)), "下拉即可刷新");
    }

    private void BitmapRotate(int degress, String msg) {
        if (degress > 180)
            degress = 180;
        image.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        text.setText(msg);

        int[] location = new int[2];
        image.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        image.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        //        location [0]--->x坐标,location [1]--->y坐标

        Matrix matrix = new Matrix();
        matrix.setTranslate(location[0], location[1]);
        matrix.postRotate(degress, location[0], location[1]);

        //创建新图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), com.example.administrator.ui_sdk.R.mipmap.down);
        image.setImageBitmap(bitmap);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //将上面创建的bitmap转换成drawable对象，使其可以使用在ImageView,ImageButton中
        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
        image.setAdjustViewBounds(true);
        image.setImageDrawable(bmd);
        image.setImageMatrix(matrix);
    }

    /**
     * 刷新数据
     */
    private void freshData() {
        image.setVisibility(GONE);
        progress.setVisibility(VISIBLE);
        isShow = true;
        StopHeight = dropHeight;

        if (reshInterface != null)
            reshInterface.RreshData();
    }

    private ItemClick.RreshInterface reshInterface = null;

    /**
     * 刷新数据的接口
     *
     * @param reshInterface
     */
    public void setRreshClick(ItemClick.RreshInterface reshInterface) {
        this.reshInterface = reshInterface;
    }

    /**
     * 提供隐藏头部文件的接口
     */
    public void setVisiableTopView() {
        BitmapRotate(0, "下拉即可刷新");
    }
}
