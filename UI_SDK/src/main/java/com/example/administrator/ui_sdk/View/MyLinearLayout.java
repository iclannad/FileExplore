package com.example.administrator.ui_sdk.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.ui_sdk.MyOnClickInterface;
import com.example.administrator.ui_sdk.R;


/**
 * Created by Administrator on 2015/12/8.
 */
public class MyLinearLayout extends LinearLayout {

    public static ViewDragHelper dragHelper;
    private int dragRange;
    private View dragContentView;
    private View topView;

    private int contentTop;
    private int topViewHeight;
    private float ratio;
    private boolean isRefreshing;
    private boolean shouldIntercept = true;

    private float refreshRatio = 1.5f;
    private boolean overDrag = true;
    private int collapseOffset;
    private int topViewId = -1;
    private int dragContentViewId = -1;
    private boolean captureTop = true;

    private boolean dispatchingChildrenDownFaked = false;
    private boolean dispatchingChildrenContentView = false;
    private float dispatchingChildrenStartedAtY = Float.MAX_VALUE;

    private PanelState panelState = PanelState.EXPANDED;

    public ListView listview = null;

    private int topheight = 0;

    private ProgressBar progress = null;
    private TextView text = null;
    private ImageView image = null;
    private int STATE = 0;
    //1下拉状态 2.放开及刷新状态 3.刷新状态

//    private View footView = null;
    private Context context = null;
    private LinearLayout bottom = null;


    //创建刷新的接口
    private MyOnClickInterface.Push myOnClickInterface = null;

    private boolean flag = true;


    public static enum PanelState {
        COLLAPSED(0),
        EXPANDED(1),
        SLIDING(2);
        private int asInt;

        PanelState(int i) {
            this.asInt = i;
        }
    }

    public MyLinearLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        dragHelper = ViewDragHelper.create(this, 1.0f, callback);
    }

    private boolean scroll = true;

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
//        footView = LayoutInflater.from(context).inflate(R.layout.listview_bottom, null);
//        bottom = (LinearLayout) footView.findViewById(R.id.listview_bottom);
        topView = getChildAt(0);
        dragContentView = getChildAt(1);

//        listview = (SideListView) dragContentView;
        progress = (ProgressBar) topView.findViewById(R.id.progress);
        text = (TextView) topView.findViewById(R.id.text);
        image = (ImageView) topView.findViewById(R.id.image);
//        listview.addFooterView(footView);
//        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (listview.getFirstVisiblePosition() == 0) {
//                    scroll = true;
//                } else {
//                    scroll = false;
//                }

                // 判断滚动到底部
//                if (listview.getLastVisiblePosition() == (listview.getCount() - 1)) {
//                    if (flag) {
//                        bottom.setVisibility(View.VISIBLE);
//                        myOnClickInterface.highpullflush();
//                        flag = false;
//                    }
//                }
//            }

//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            }
//        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        dragRange = getHeight();
        int contentTopTemp = contentTop;
        resetContentHeight();
        topView.layout(left, Math.min(topView.getPaddingTop(), contentTop - topViewHeight), right,
                contentTop);
        dragContentView.layout(left, contentTopTemp, right,
                contentTopTemp + dragContentView.getHeight());
    }

    //动态布局
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取头部布局的高度
        topheight = topView.getMeasuredHeight();
    }


    private void resetContentHeight() {
        if (dragContentView != null && dragContentView.getHeight() != 0) {
            ViewGroup.LayoutParams layoutParams = dragContentView.getLayoutParams();
            layoutParams.height = getHeight() - collapseOffset;
            dragContentView.setLayoutParams(layoutParams);
        }
    }

    private void calculateRatio(float top) {
        ratio = (top - collapseOffset) / (topViewHeight - collapseOffset);
        if (dispatchingChildrenContentView) {
            resetDispatchingContentView();
        }
    }

    private void updatePanelState() {
        if (contentTop <= getPaddingTop() + collapseOffset) {
            panelState = PanelState.COLLAPSED;
        } else if (contentTop >= topView.getHeight()) {
            panelState = PanelState.EXPANDED;
        } else {
            panelState = PanelState.SLIDING;
        }
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (child == topView && captureTop) {
                dragHelper.captureChildView(dragContentView, pointerId);
                return false;
            }
            return child == dragContentView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            contentTop = top;
            requestLayout();
            calculateRatio(contentTop);
            updatePanelState();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return dragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //判断Listview第一项在顶部
            if (scroll) {
                //如果下拉的高度达到一定的时候就更新界面
                if (top > topheight - 20) {
                    STATE = 2;
                } else {
                    STATE = 1;
                }
                resetView();

                //界面刷新
                if (top > 200) {
                    return 200;
                }
                if (overDrag) {
                    return Math.max(top, getPaddingTop() + collapseOffset);
                } else {
                    return Math.min(topViewHeight, Math.max(top, getPaddingTop() + collapseOffset));
                }
            } else {
                //不允许滑动
                return 0;
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            // yvel > 0 Fling down || yvel < 0 Fling up
            int top;
            if (STATE == 1) {
                dragHelper.smoothSlideViewTo(dragContentView, 0, 0);
            } else {
                STATE = 3;
                dragHelper.smoothSlideViewTo(dragContentView, 0, topheight + 20);
                //刷新的接口
//                myOnClickInterface.dropdownflush();
            }
            postInvalidate();
            resetView();

        }


        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }
    };

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            boolean intercept = shouldIntercept && dragHelper.shouldInterceptTouchEvent(ev);
            return intercept;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (!dispatchingChildrenContentView) {
            try {
                dragHelper.processTouchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (action == MotionEvent.ACTION_MOVE && ratio == 0.0f) {
            dispatchingChildrenContentView = true;
            if (!dispatchingChildrenDownFaked) {
                dispatchingChildrenStartedAtY = event.getY();
                event.setAction(MotionEvent.ACTION_DOWN);
                dispatchingChildrenDownFaked = true;
            }
            dragContentView.dispatchTouchEvent(event);
        }

        if (dispatchingChildrenContentView && dispatchingChildrenStartedAtY < event.getY()) {
            resetDispatchingContentView();
        }

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            resetDispatchingContentView();
            dragContentView.dispatchTouchEvent(event);
        }
        return true;
    }

    private void resetDispatchingContentView() {
        dispatchingChildrenDownFaked = false;
        dispatchingChildrenContentView = false;
        dispatchingChildrenStartedAtY = Float.MAX_VALUE;
    }

    //更新界面的方法
    private void resetView() {
        switch (STATE) {
            case 1:
                text.setText("下拉即可刷新");
                image.setVisibility(VISIBLE);
                image.setImageResource(R.mipmap.down);
                progress.setVisibility(GONE);
                break;
            case 2:
                text.setText("释放即可刷新");
                image.setVisibility(VISIBLE);
                image.setImageResource(R.mipmap.up);
                progress.setVisibility(GONE);
                break;
            case 3:
                text.setText("正在刷新");
                image.setVisibility(GONE);
                progress.setVisibility(VISIBLE);
                break;
        }
    }

    public void setflushListener(MyOnClickInterface.Push myOnClickInterface) {
        this.myOnClickInterface = myOnClickInterface;
    }

    /**
     * 刷新完成隐藏头部文件
     */
    public void setrest() {
        dragHelper.smoothSlideViewTo(dragContentView, 0, 0);
    }

    /**
     * 加载完成隐藏尾部文件
     */
    public void setbottom() {
        bottom.setVisibility(View.GONE);
        flag = true;
    }
}
