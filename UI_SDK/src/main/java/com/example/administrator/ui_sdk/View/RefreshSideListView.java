//package com.example.administrator.ui_sdk.View;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Build;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.administrator.ui_sdk.DensityUtil;
//import com.example.administrator.ui_sdk.ItemClick;
//import com.example.administrator.ui_sdk.R;
//
///**
// * Created by Soft on 2016/7/9.
// * <p/>
// * 这个类实现侧滑实现删除，编辑的自定义listview类
// */
//public class RefreshSideListView extends ListView {
//
//    //获取屏幕宽度
//    private int mScreenWidth = 0;
//
//    //头部文件布局
//    private View TopView = null;
//
//
//    //默认两个都是不允许滑动
//    //是否可以侧滑滑动
//    private boolean isScroll = false;
//    //是否可以滑动下拉
//    private boolean isDrop = false;
//    //判断当前下拉状态是隐藏还是显示
//    public static boolean isShow = false;
//
//    //侧滑布局是否显示
//    //isShown is true 就是有显示
//    //isShown is false 就是没有显示
//    private boolean isShown;
//
//    //按下的X，Y开始的坐标
//    private int downX, downY = 0;
//    //移动的X, Y的坐标
//    private int nowX, nowY = 0;
//    //移动的长度
//    private int scroll = 0;
//
//    //当前点击listview的item  布局
//    private ViewGroup itemChildView = null;
//
//    //当前显示的布局
//    private LinearLayout.LayoutParams itemLinearLayout = null;
//    //侧滑菜单的宽度
//    private int sideWidth = 0;
//    //下拉的高度
//    private int downHeight = 0;
//    //设置下拉最长的高度和最小的高度
//    private int Max = 0;
//    private int Min = 0;
//
//
//    //当前下拉的高度
//    private int StopHeight = 0;
//
//    //获取点击该item的是第几个
//    private int position = 0;
//    private Context context = null;
//
//    private TextView text = null;
//    private ImageView image = null;
//    private ProgressBar progress = null;
//
//
//    public RefreshSideListView(Context context) {
//        this(context, null);
//    }
//
//    public RefreshSideListView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public RefreshSideListView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        this.context = context;
//        // 获取屏幕宽度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        mScreenWidth = dm.widthPixels;
//
//        Max = DensityUtil.dip2px(context, 100);
//        downHeight = -DensityUtil.dip2px(context, 50);
//        Min = downHeight;
//        StopHeight = downHeight;
//    }
//
//    @Override
//    public void addHeaderView(View v) {
//        super.addHeaderView(v);
//        this.TopView = v;
//        text = (TextView) TopView.findViewById(R.id.text);
//        image = (ImageView) TopView.findViewById(R.id.image);
//        progress = (ProgressBar) TopView.findViewById(R.id.progress);
//
//        this.setPadding(0, downHeight, 0, 0);
//    }
//
//    /**
//     * 重写触摸事件
//     * <p/>
//     * 监控 ACTION_DOWN   ACTION_UP   ACTION_MOVE
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return ActionDown(ev);
//            case MotionEvent.ACTION_MOVE:
//                return ActionMove(ev);
//            case MotionEvent.ACTION_UP:
//                ActionUp(ev);
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//
//    /**
//     * 触摸 手指滑动的事件
//     *
//     * @param ev
//     */
//    private boolean ActionMove(MotionEvent ev) {
//        nowX = (int) ev.getX();
//        nowY = (int) ev.getY();
//        //先判断滑动的范围是不是向下拉动
//        if ((Math.abs(nowY - downY) > Math.abs(nowX - downX) * 2 || isDrop) && !isScroll) {
//            isScroll = false;
//            isDrop = true;
//
//            scroll = (nowY - downY) / 2;
//            scroll += StopHeight;
//            //当滑动距离大于侧滑菜单的宽度的时候设置最大为侧滑的宽度
//            if (scroll >= Max)
//                scroll = Max;
//            //当滑动距离小于0的时候就不能再距离增加
//            if (scroll < Min)
//                scroll = Min;
//
//            //判断下拉距离如果距离大于高度的一半就将当前状态设置为显示状态否则就设置不显示状态
//            if (scroll >= Max / 2) {
//                //下拉时到达一半以后提示放手可以刷新
//                isShow = true;
//                BitmapRotate(180, "松手即可刷新");
//            } else {
//                //当高度回到一定的高度放手则回复原来的样子
//                if (scroll >= 0)
//                    AnimationUp(scroll);
//                else
//                    BitmapRotate(0, "下拉即可刷新");
//                isShow = false;
//            }
//            //保存当前下拉高度
//            this.setPadding(0, (scroll), 0, 0);
////            invalidate();
////            invalidateViews();
//        }
//        //当横移动的幅度大于竖移动的时候才让侧滑菜单滑动
//        if ((Math.abs(nowX - downX) > Math.abs(nowY - downY) * 2 || isScroll) && !isDrop && !isShow) {
//
////            if ()
//            //设置侧滑不允许下拉
//            isScroll = true;
//            isDrop = false;
//
//             /*此段代码是为了避免我们在侧向滑动时同时触发ListView的OnItemClickListener时间*/
//            MotionEvent cancelEvent = MotionEvent.obtain(ev);
//            cancelEvent
//                    .setAction(MotionEvent.ACTION_CANCEL
//                            | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
//            onTouchEvent(cancelEvent);
//
//            //当手指从左往右滑动的时候才让界面滑动
//            if (nowX < downX) {
//                scroll = (nowX - downX) / 2;
//                //当滑动距离大于侧滑菜单的宽度的时候设置最大为侧滑的宽度
//                if (-scroll >= sideWidth)
//                    scroll = -sideWidth;
//                itemLinearLayout.leftMargin = scroll;
//                itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
//            }
//            return true;
//        }
//        return super.onTouchEvent(ev);
//
//    }
//
//    /**
//     * 自动恢复的动画
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void AnimationUp(int scoll) {
//        BitmapRotate((int) ((180 / (Max / 2 - DensityUtil.dip2px(context , 10)) * scoll)), "下拉即可刷新");
//    }
//
//    private void BitmapRotate(int degress, String msg) {
//        if (degress > 180)
//            degress = 180;
//        image.setVisibility(VISIBLE);
//        progress.setVisibility(GONE);
//        text.setText(msg);
//
//        int[] location = new int[2];
//        image.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
//        image.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
//        //        location [0]--->x坐标,location [1]--->y坐标
//
//        Matrix matrix = new Matrix();
//        matrix.setTranslate(location[0], location[1]);
//        matrix.postRotate(degress, location[0], location[1]);
//
//        //创建新图片
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.down);
//        image.setImageBitmap(bitmap);
//        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        //将上面创建的bitmap转换成drawable对象，使其可以使用在ImageView,ImageButton中
//        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
//        image.setAdjustViewBounds(true);
//        image.setImageDrawable(bmd);
//        image.setImageMatrix(matrix);
//    }
//
//    /**
//     * 触摸 手指抬起来的事件
//     *
//     * @param ev
//     */
//    private void ActionUp(MotionEvent ev) {
//        //当侧滑的长度大于侧滑菜单的宽度则就侧滑菜单全部显示否则隐藏
//        //isDrop==true当前滑动为下拉
//        if (isDrop) {
//            if (isShow) {
//                this.setPadding(0, 0, 0, 0);
//                StopHeight = 0;
//                //启动刷新
//                freshData();
//            } else {
//                this.setPadding(0, downHeight, 0, 0);
//                StopHeight = downHeight;
//            }
//            //isScroll==true当前滑动为侧滑
//        } else if (isScroll) {
//            if (-itemLinearLayout.leftMargin >= sideWidth / 2) {
//                itemLinearLayout.leftMargin = -sideWidth;
//                itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
//                isShown = true;
//            } else {
//                ShowNormal();
//            }
//        }
//        isScroll = false;
//        isDrop = false;
//    }
//
//    /**
//     * 刷新数据
//     */
//    private void freshData() {
//        image.setVisibility(GONE);
//        progress.setVisibility(VISIBLE);
//
//        if (reshInterface != null)
//            reshInterface.RreshData();
//    }
//
//    private ItemClick.RreshInterface reshInterface = null;
//
//    /**
//     * 刷新数据的接口
//     *
//     * @param reshInterface
//     */
//    public void setRreshClick(ItemClick.RreshInterface reshInterface) {
//        this.reshInterface = reshInterface;
//    }
//
//    /**
//     * 提供隐藏头部文件的接口
//     */
//    public void setVisiableTopView() {
//        BitmapRotate(0, "下拉即可刷新");
//        this.setPadding(0, downHeight, 0, 0);
//        StopHeight = downHeight;
//        isShow = false;
//        RreshLinearLayout.isShow = false;
//    }
//
//    /**
//     * 触摸 手指按下去的事件
//     *
//     * @param ev
//     */
//    private boolean ActionDown(MotionEvent ev) {
//        if (isShown)
//            ShowNormal();
//
//        downX = (int) ev.getX();
//        downY = (int) ev.getY();
//
//        //如果没有侧滑布局就不能滑动
//        itemChildView = (ViewGroup) getChildAt(pointToPosition(downX, downY) - getFirstVisiblePosition());
//        if (itemChildView == null)
//            return false;
//
//        if (this.getPaddingTop() == Min)
//            Sideinit();
//        if (RreshLinearLayout.isShow)
//            isShow = true;
//        return true;
//    }
//
//    /**
//     * 侧滑操作初始化
//     */
//    private void Sideinit() {
//        //获取侧滑菜单
//        sideWidth = 0;
//        //获取侧滑菜单的宽度
//        for (int i = 1; i < itemChildView.getChildCount(); i++)
//            sideWidth += itemChildView.getChildAt(i).getLayoutParams().width;
//        //获取显示界面的布局LinearLayout
//        itemLinearLayout = (LinearLayout.LayoutParams) itemChildView.getChildAt(0).getLayoutParams();
//        //重写显示界面的宽度
//        itemLinearLayout.width = mScreenWidth;
//        itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
//    }
//
//
//    /**
//     * 恢复正常的显示样式
//     */
//    public void ShowNormal() {
//        //界面滑动会坐标为0,0
//        itemChildView.scrollTo(0, 0);
//        //设置距离左边0
//        itemLinearLayout.leftMargin = 0;
//        itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
//        //将侧滑菜单设置为不显示
//        isShown = false;
//    }
//
//    public int getPosition() {
//        return position;
//    }
//
//
//    public void setSideMenu(boolean isScroll) {
//        this.isScroll = isScroll;
//    }
//}

package com.example.administrator.ui_sdk.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.ItemClick;
import com.example.administrator.ui_sdk.R;


/**
 * Created by Soft on 2016/7/9.
 * <p/>
 * 这个类实现侧滑实现删除，编辑的自定义listview类
 */
public class RefreshSideListView extends ListView {

    //获取屏幕宽度
    private int mScreenWidth = 0;

    //侧滑布局是否显示
    //isShown is true 就是有显示
    //isShown is false 就是没有显示
    private boolean isShown;

    private boolean isRScroll = false;

    //按下的X，Y开始的坐标
    private int downX, downY = 0;
    //移动的X, Y的坐标
    private int nowX, nowY = 0;
    //移动的长度
    private int scroll = 0;

    //当前点击listview的item  布局
    private ViewGroup itemChildView = null;

    //当前显示的布局
    private LinearLayout.LayoutParams itemLinearLayout = null;
    //侧滑菜单的宽度
    private int sideWidth = 0;
    private Context context = null;


    public RefreshSideListView(Context context) {
        this(context, null);
    }

    public RefreshSideListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshSideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

    }


    /**
     * 重写触摸事件
     * <p/>
     * 监控 ACTION_DOWN   ACTION_UP   ACTION_MOVE
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

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
    }


    /**
     * 触摸 手指滑动的事件
     *
     * @param ev
     */
    private boolean ActionMove(MotionEvent ev) {
        nowX = (int) ev.getX();
        nowY = (int) ev.getY();
        //当横移动的幅度大于竖移动的时候才让侧滑菜单滑动
        if ((Math.abs(nowX - downX) > Math.abs(nowY - downY) * 2) && itemLinearLayout != null || isRScroll) {
            PullToRefreshView.isSrcoll = false;
            isRScroll = true;
//             /*此段代码是为了避免我们在侧向滑动时同时触发ListView的OnItemClickListener时间*/
            MotionEvent cancelEvent = MotionEvent.obtain(ev);
            cancelEvent
                    .setAction(MotionEvent.ACTION_CANCEL
                            | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
            onTouchEvent(cancelEvent);

            //当手指从左往右滑动的时候才让界面滑动
            if (nowX < downX) {
                scroll = (nowX - downX) / 2;
                //当滑动距离大于侧滑菜单的宽度的时候设置最大为侧滑的宽度
                if (-scroll >= sideWidth)
                    scroll = -sideWidth;
                itemLinearLayout.leftMargin = scroll;
                itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
            }
            return true;
        }
        return super.onTouchEvent(ev);

    }


    /**
     * 触摸 手指抬起来的事件
     *
     * @param ev
     */
    private void ActionUp(MotionEvent ev) {
        isRScroll = false;
        if (-itemLinearLayout.leftMargin >= sideWidth / 2) {
            itemLinearLayout.leftMargin = -sideWidth;
            itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
            isShown = true;
        } else {
            ShowNormal();
            PullToRefreshView.isSrcoll = true;
        }
    }

    /**
     * 触摸 手指按下去的事件
     *
     * @param ev
     */
    private boolean ActionDown(MotionEvent ev) {
        if (isShown) {

            MotionEvent cancelEvent = MotionEvent.obtain(ev);
            cancelEvent
                    .setAction(MotionEvent.ACTION_CANCEL
                            | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
            onTouchEvent(cancelEvent);


            ShowNormal();
        }

        downX = (int) ev.getX();
        downY = (int) ev.getY();

        //如果没有侧滑布局就不能滑动
        itemChildView = (ViewGroup) getChildAt(pointToPosition(downX, downY) - getFirstVisiblePosition());
        if (itemChildView == null)
            return false;

        Sideinit();
        return true;
    }

    /**
     * 侧滑操作初始化
     */
    private void Sideinit() {
        //获取侧滑菜单
        sideWidth = 0;
        //获取侧滑菜单的宽度
        for (int i = 1; i < itemChildView.getChildCount(); i++) {
            if (itemChildView.getChildAt(i).getVisibility() == VISIBLE)
                sideWidth += itemChildView.getChildAt(i).getLayoutParams().width;
        }
        //获取显示界面的布局LinearLayout
        itemLinearLayout = (LinearLayout.LayoutParams) itemChildView.getChildAt(0).getLayoutParams();
        //重写显示界面的宽度
        itemLinearLayout.width = mScreenWidth;
        itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
    }


    /**
     * 恢复正常的显示样式
     */
    public void ShowNormal() {
        //界面滑动会坐标为0,0
        itemChildView.scrollTo(0, 0);
        //设置距离左边0
        itemLinearLayout.leftMargin = 0;
        itemChildView.getChildAt(0).setLayoutParams(itemLinearLayout);
        //将侧滑菜单设置为不显示
        isShown = false;
    }

}
