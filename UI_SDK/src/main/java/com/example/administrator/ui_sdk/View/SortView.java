package com.example.administrator.ui_sdk.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ui_sdk.R;
import com.example.administrator.ui_sdk.SorViewInterface;


/**
 * Created by 19820 on 2016/5/7.
 */
public class SortView extends View {


    private String[] b = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private TextView textView = null;
    private Context context = null;

    private SorViewInterface sorViewInterface = null;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public SortView(Context context) {
        this(context, null);
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
     * @see #View(Context, AttributeSet, int)
     */
    public SortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
     * @see #View(Context, AttributeSet)
     */
    public SortView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取点击的高度和宽度
        int width = getWidth();
        int height = getHeight();
        //获取一个字符的高度
        int singleheight = height / b.length;

        Paint paint = new Paint();


        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(R.color.Black));
            paint.setAntiAlias(true);
            paint.setTextSize(dip2px(context, 12));

            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleheight * i + singleheight;

            canvas.drawText(b[i], xPos, yPos, paint);
        }


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();


        //这个计算出点击的高度知道点击是第几个字符
        int c = (int) (y / getHeight() * b.length);
        if (c >= b.length)
            c = b.length - 1;


        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                textView.setVisibility(GONE);
                setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;

            default:
                sorViewInterface.onItemClick(b[c], c);
                textView.setVisibility(VISIBLE);
                textView.setText(b[c]);
                setBackgroundColor(getResources().getColor(R.color.GreyDeep));
                break;
        }


        return true;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public void setOnItemClick(SorViewInterface sorViewInterface) {
        this.sorViewInterface = sorViewInterface;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
