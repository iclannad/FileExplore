package com.example.administrator.ui_sdk.View;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ui_sdk.MyOnClickInterface;
import com.example.administrator.ui_sdk.Other.MyBaseAdapter;
import com.example.administrator.ui_sdk.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/5.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyDialog extends Dialog implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter {


    private TextView dialog_content = null;
    private TextView dialog_left_but = null;
    private TextView dialog_right_but = null;
    private EditText dialog_edit = null;
    private Context context = null;
    private RelativeLayout dialog_linear = null;
    private ListView dialog_listview = null;
    private RelativeLayout dialog1 = null;
    private LinearLayout dialog2 = null;
    private ProgressBar dialog2_progress = null;
    private TextView dialog2_content = null;
    private NumberPicker dialog_numberpicker = null;
    private RelativeLayout dialog3 = null;
    private TextView dialog3_text = null;
    private TextView dialog3_text1 = null;
    private GridView dialog4_gridview = null;
    private RelativeLayout dialog4 = null;
    private RelativeLayout dialog5 = null;
    private LinearLayout dialog_bottom1 = null;
    private DatePicker dialog_datapicker = null;

    private View view = null;

    private int position = 0;
    private int NumberPicker = 0;

    private int year, month, day;

    //实现按钮点击事件的接口对象
    private MyOnClickInterface.Click dialogClick = null;
    //实现Item点击事件的接口对象
    private MyOnClickInterface.ItemClick dialogitemClick = null;

    private int FLAG = 0;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyDialog(Context context, int themeResId , int FLAG) {
        super(context, themeResId);
        this.context = context;
        this.FLAG = FLAG;
        init();
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener , int FLAG) {
        super(context, cancelable, cancelListener);
        this.context = context;
        this.FLAG = FLAG;
        init();
    }

    private View getView() {
        view = LayoutInflater.from(context).inflate(R.layout.mydialog_main, null);

        dialog1 = (RelativeLayout) view.findViewById(R.id.dialog1);
        dialog2 = (LinearLayout) view.findViewById(R.id.dialog2);
        dialog3 = (RelativeLayout) view.findViewById(R.id.dialog3);
        dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        dialog_left_but = (TextView) view.findViewById(R.id.dialog_left_but);
        dialog_right_but = (TextView) view.findViewById(R.id.dialog_right_but);
        dialog_edit = (EditText) view.findViewById(R.id.dialog_edit);
        dialog_linear = (RelativeLayout) view.findViewById(R.id.dialog_linear);
        dialog_listview = (ListView) view.findViewById(R.id.dialog_listview);
        dialog2_progress = (ProgressBar) view.findViewById(R.id.dialog2_progress);
        dialog2_content = (TextView) view.findViewById(R.id.dialog2_content);
        dialog_numberpicker = (NumberPicker) view.findViewById(R.id.dialog_numberpicker);
        dialog3_text = (TextView) view.findViewById(R.id.dialog3_text);
        dialog3_text1 = (TextView) view.findViewById(R.id.dialog3_text1);
        dialog4_gridview = (GridView) view.findViewById(R.id.dialog4_gridview);
        dialog4 = (RelativeLayout) view.findViewById(R.id.mydialog4);
        dialog_bottom1 = (LinearLayout) view.findViewById(R.id.dialog_bottom1);
        dialog_datapicker = (DatePicker) view.findViewById(R.id.dialog_datapicker);
        dialog5 = (RelativeLayout) view.findViewById(R.id.dialog5);


        //左边按钮的点击事件
        dialog_left_but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogClick.OnLeftClick(FLAG);
            }
        });

        //右边按钮的点击事件
        dialog_right_but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogClick.OnRightClick(FLAG);
            }
        });

        //左边按钮的点击事件
        dialog3_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogClick.OnLeftClick(FLAG);
            }
        });

        //右边按钮的点击事件
        dialog3_text1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogClick.OnRightClick(FLAG);
            }
        });

        //listview点击事件
        dialog_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogitemClick.OnItemClick(parent, view, position, id);
            }
        });

        //gridview点击事件
        dialog4_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogitemClick.OnItemClick(parent, view, position, id);
            }
        });

        return view;
    }

    private void init() {
        this.setContentView(getView());
    }

    //实现点击事件的方法
    public void setOnClick(MyOnClickInterface.Click dialog) {
        this.dialogClick = dialog;
    }

    //实现Item点击事件方法
    public void setOnItemClick(MyOnClickInterface.ItemClick dialogitemClick) {
        this.dialogitemClick = dialogitemClick;
    }

    /**
     * 设置对话框是否消失
     * @param isShow
     */
    public void DialogShow(boolean isShow){
        Field field = null;
        try {
            field = this.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true); //设置mShowing值，欺骗android系统
            field.set(this, isShow); //需要关闭的时候将这个参数设置为true 他就会自动关闭了
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 紧紧拥有文字和按钮
     *
     * @param Content_msg
     * @param left_msg
     * @param right_msg
     */
    public void DialogState(String Content_msg, String left_msg, String right_msg) {
        setMessage(Content_msg);
        dialog_left_but.setText(left_msg);
        dialog_right_but.setText(right_msg);
        if (left_msg.equals("")) {
            dialog_left_but.setVisibility(View.GONE);
        }

        if (right_msg.equals("")) {
            dialog_right_but.setVisibility(View.GONE);
        }
    }

    /**
     * 该布局拥有编辑框,内容和两个按钮
     *
     * @param Content_msg
     * @param Edit_msg
     * @param Edit_hint_msg
     * @param left_msg
     * @param right_msg
     */
    public void DialogState(String Content_msg, String Edit_msg, String Edit_hint_msg, String left_msg, String right_msg) {
        dialog_edit.setVisibility(View.VISIBLE);
        setMessage(Content_msg);
        setEditText(Edit_msg);
        setHint(Edit_hint_msg);
        setMessage(Content_msg);
        if (left_msg.equals("")) {
            dialog_left_but.setVisibility(View.GONE);
        }

        if (right_msg.equals("")) {
            dialog_right_but.setVisibility(View.GONE);
        }
    }

    /**
     * 该布局里面嵌套一个listview
     *
     * @param Content_msg
     * @param map
     * @param left_msg
     * @param right_msg
     */
    public void DialogState(String Content_msg, ArrayList<Object> map, int maker, String left_msg, String right_msg) {
        dialog_listview.setVisibility(View.VISIBLE);
        setMessage(Content_msg);
        dialog_listview.setAdapter(new MyBaseAdapter(context, map, maker));
        setLeft_but(left_msg);
        setRight_but(right_msg);
        if (Content_msg.equals("")) {
            dialog_content.setVisibility(View.GONE);
        }
        if (left_msg.equals("")) {
            dialog_left_but.setVisibility(View.GONE);
        }

        if (right_msg.equals("")) {
            dialog_right_but.setVisibility(View.GONE);
        }
    }

    /**
     * 嵌套Gridview
     *
     * @param map
     */
    public void DialogState(ArrayList<Object> map, int maker) {
        dialog1.setVisibility(View.GONE);
        dialog4.setVisibility(View.VISIBLE);
        dialog4_gridview.setAdapter(new MyBaseAdapter(context, map, maker));
    }

    /**
     * 该布局拥有数据选择器
     *
     * @param map
     * @param max
     * @param min
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void DialogState(String[] map, int max, int min, int value) {
        dialog1.setVisibility(View.GONE);
        dialog3.setVisibility(View.VISIBLE);
        dialog_bottom1.setVisibility(View.VISIBLE);

        if (map != null) {
            dialog_numberpicker.setDisplayedValues(map);
        }
        dialog_numberpicker.setMaxValue(max);
        dialog_numberpicker.setMinValue(min);
        dialog_numberpicker.setValue(value);
        dialog_numberpicker.setOnScrollListener(this);
        dialog_numberpicker.setFormatter(this);
        dialog_numberpicker.setOnValueChangedListener(this);
        dialog_numberpicker.setFocusable(false);
    }

    /**
     * 该布局拥有数据选择器
     */
    public void DialogState() {
        dialog1.setVisibility(View.GONE);
        dialog5.setVisibility(View.VISIBLE);
        dialog_bottom1.setVisibility(View.VISIBLE);
//        dialog_datapicker.updateDate(year, month, day);
    }

    /**
     * 该布局仅拥有加载框和文字
     *
     * @param content
     * @param Prompt
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void DialogState(String content, boolean Prompt) {
        dialog2.setVisibility(View.VISIBLE);
        dialog1.setVisibility(View.GONE);

        if (Prompt) {
            dialog2_progress.setVisibility(View.VISIBLE);
        } else {
            dialog2_progress.setVisibility(View.GONE);
        }
        setDialog2_Content(content);
    }

    //设置对话框的高度
    public void setHeight(int height) {
        ViewGroup.LayoutParams params = dialog_linear.getLayoutParams();
        params.height = height;
    }

    //设置对话框的宽度
    public void setWidth(int width) {
        ViewGroup.LayoutParams params = dialog_linear.getLayoutParams();
        params.width = width;
    }

    //设置对话框的内容
    private void setMessage(String msg) {
        dialog_content.setText(msg);
    }

    //设置对话框的编辑框的提示内容
    private void setHint(String msg) {
        dialog_edit.setHint(msg);
    }

    //设置对话框的编辑框的内容
    private void setEditText(String msg) {
        dialog_edit.setText(msg);
    }

    //设置左边的按钮的文字
    private void setLeft_but(String msg) {
        dialog_left_but.setText(msg);
    }

    //设置右边的按钮的文字
    private void setRight_but(String msg) {
        dialog_right_but.setText(msg);
    }

    //获取对话框的编辑框的文字
    public String getEditText() {
        return dialog_edit.getText().toString();
    }

    public int getNumberPicker() {
        return NumberPicker;
    }


    public void setLeftButColor(int resid) {
        dialog_left_but.setTextColor(context.getResources().getColor(resid));
    }

    public void setRightButColor(int resid) {
        dialog_right_but.setTextColor(context.getResources().getColor(resid));
    }

    /**
     * 获取时间日期
     *
     * @return
     */
    public String getData() {
        return dialog_datapicker.getYear() + "-" + (dialog_datapicker.getMonth() + 1) + "-" + dialog_datapicker.getDayOfMonth();
    }


    public void setDialog3_text(String msg) {
        dialog3_text.setText(msg);
    }

    public void setDialog3_text1(String msg) {
        dialog3_text1.setText(msg);
    }

    //设置dialog2的布局内容
    private void setDialog2_Content(String content) {
        dialog2_content.setText(content);
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        NumberPicker = newVal;
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {

    }

    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }
}
