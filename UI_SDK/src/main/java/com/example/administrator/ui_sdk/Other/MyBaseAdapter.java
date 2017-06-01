package com.example.administrator.ui_sdk.Other;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.ListView_Object;
import com.example.administrator.ui_sdk.MyOnClickInterface;
import com.example.administrator.ui_sdk.R;
import com.example.administrator.ui_sdk.View.MyGridView;
import com.example.administrator.ui_sdk.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/31.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyBaseAdapter extends BaseAdapter {


    private Context context = null;
    private ArrayList<View> list = null;
    //获取listview的长度
    private int length = 0;

    //如果标志为1则显示，否则不显示
    private int GONE = 0;
    private int VISIABLE = 1;


    private View view = null;

    private TextView item_content = null;
    private TextView item_msg = null;
    private TextView item_text = null;
    private TextView item_interval_title = null;
    private ImageView item_icon = null;
    private Button item_but = null;
    private LinearLayout listview_item_linear = null;
    private LinearLayout item_linear1 = null;
    private RelativeLayout item_linear2 = null;
    private ImageView linear1_image = null;
    private TextView linear1_text = null;
    private View item_setting_close = null;
    private View item_setting_open = null;
    private ImageView item_image = null;

    private View.OnClickListener onClickListener = null;
    private ArrayList<Object> alist = null;
    private ListView_Object object = null;
    private MyOnClickInterface myOnClickInterface = null;
    private int maker = 0;
    private ViewHolder viewHolder = null;

    public MyBaseAdapter(Context context, ArrayList<Object> alist, int maker) {
        this.context = context;
        this.alist = alist;
        this.maker = maker;
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public ListView_Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (alist.get(position).getPrompt()) {
//            return 0;
//        } else {
//            return 1;
//        }
//        return super.getItemViewType(position);
//    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        object = (ListView_Object) alist.get(position);
        viewHolder = null;
        if (convertView == null) {
//            Log.e("hello", position + ":" + object.getContent());
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);

            viewHolder = new ViewHolder();
            viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
            viewHolder.item_msg = (TextView) convertView.findViewById(R.id.item_msg);
            viewHolder.item_text = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.listview_item_linear = (LinearLayout) convertView.findViewById(R.id.listview_item_linear);
            viewHolder.item_image = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.item_but = (Button) convertView.findViewById(R.id.item_but);
            viewHolder.item_setting_close = convertView.findViewById(R.id.item_setting_close);
            viewHolder.item_setting_open = convertView.findViewById(R.id.item_setting_open);
            viewHolder.item_interval_title = (TextView) convertView.findViewById(R.id.item_interval_title);
            viewHolder.item_linear3 = (LinearLayout) convertView.findViewById(R.id.item_linear3);
            viewHolder.item_linear2 = (RelativeLayout) convertView.findViewById(R.id.item_linear2);
            viewHolder.item_image1 = (ImageView) convertView.findViewById(R.id.item_image1);

            viewHolder.linear1_image = (ImageView) convertView.findViewById(R.id.linear1_image);
            viewHolder.linear1_text = (TextView) convertView.findViewById(R.id.linear1_text);
            viewHolder.item_linear1 = (LinearLayout) convertView.findViewById(R.id.item_linear1);

            viewHolder.item_linear4_icon = (ImageView) convertView.findViewById(R.id.item_linear4_icon);
            viewHolder.item_linear4_linear = (LinearLayout) convertView.findViewById(R.id.item_linear4_linear);
            viewHolder.item_linear4_linear_icon = (ImageView) convertView.findViewById(R.id.item_linear4_linear_icon);
            viewHolder.item_linear4_name = (TextView) convertView.findViewById(R.id.item_linear4_name);
            viewHolder.item_linear4_msg = (TextView) convertView.findViewById(R.id.item_linear4_msg);
            viewHolder.item_linear4_girdview = (MyGridView) convertView.findViewById(R.id.item_linear4_girdview);
            viewHolder.item_linear4_linear_title = (TextView) convertView.findViewById(R.id.item_linear4_linear_title);


            convertView.setTag(viewHolder);
        } else {
//            Log.e("hello", position + "--:" + object.getContent());
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HandlerData(viewHolder, object);
//        Log.e("hello", position + ":" + list.getRight_title());

//        if (list.getPrompt()) {
////            viewHolder.view = Interval(list.getPromptContent() , list.getHeight());
//            convertView = Interval(list.getPromptContent(), list.getHeight());
//        } else {
        viewHolder.item_linear4_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnClickInterface != null) {
                    myOnClickInterface.ItemOnClick(position);
                }
            }
        });

//        }

        //判断是否为空行
//        if (list.getPrompt()) {
//            Log.e("hello" , "你好");
////            viewHolder.item_interval_title.setText(list.getPromptContent());
//            viewHolder.item_interval_title.setHeight(list.getHeight());
//            convertView = viewHolder.view;
//        }
//        if (list.getPrompt()) {
//            return view;
//        } else {
        return convertView;
//        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void HandlerData(ViewHolder viewHolder, ListView_Object list) {
//        if (maker == 3) {
//            viewHolder.item_linear2.setVisibility(View.GONE);
//            viewHolder.item_linear1.setVisibility(View.VISIBLE);
//
//            if (!list.getContent_center().equals("")) {
//                viewHolder.linear1_text.setText(list.getContent_center());
//            } else {
//                viewHolder.linear1_text.setVisibility(View.GONE);
////                DensityUtil.setHeight(viewHolder.item_linear1, list.getResid_center1_height());
////                viewHolder.item_linear1.setBackground(list.getResid_center1());
//            }
//            if (list.getResid_center() != null) {
//                viewHolder.linear1_image.setVisibility(View.VISIBLE);
//                viewHolder.linear1_image.setImageBitmap(list.getResid_center());
//                DensityUtil.setHeight(viewHolder.item_linear1, list.getResid_center_height());
//            } else {
//                viewHolder.linear1_image.setVisibility(View.GONE);
//            }
//        } else
        if (list.getPrompt()) {
            viewHolder.item_linear2.setVisibility(View.GONE);
            viewHolder.item_linear3.setVisibility(View.VISIBLE);
            viewHolder.item_interval_title.setText(list.getPromptContent());
            viewHolder.item_interval_title.setHeight(list.getHeight());
        } else {
            viewHolder.item_linear2.setVisibility(View.VISIBLE);
            viewHolder.item_linear3.setVisibility(View.GONE);
            if (!list.getContent().equals("")) {
                viewHolder.item_content.setText(list.getContent());
                viewHolder.item_content.setVisibility(View.VISIBLE);
            }
            if (!list.getSubtitle().equals("")) {
                viewHolder.item_msg.setText(list.getSubtitle());
                viewHolder.item_msg.setVisibility(View.VISIBLE);
            }
            if (!list.getRight_title().equals("")) {
                viewHolder.item_text.setText(list.getRight_title());
                viewHolder.item_text.setVisibility(View.VISIBLE);
                viewHolder.item_but.setVisibility(View.GONE);
                viewHolder.item_image.setVisibility(View.GONE);
                viewHolder.item_image1.setVisibility(View.GONE);
            }
            if (list.getResid() != null) {
                viewHolder.item_icon.setImageDrawable(list.getResid());
                viewHolder.item_icon.setVisibility(View.VISIBLE);
            }
            if (list.getResid_right() != null) {
                viewHolder.item_image.setImageDrawable(list.getResid_right());
                viewHolder.item_image.setVisibility(View.VISIBLE);
                viewHolder.item_but.setVisibility(View.GONE);
                viewHolder.item_text.setVisibility(View.GONE);
                viewHolder.item_image1.setVisibility(View.GONE);
            }
            if (list.getItem_height() != 0) {
                DensityUtil.setItemHeight(viewHolder.listview_item_linear, list.getItem_height());
            }

            if (list.getResidHeight() != 0){
                DensityUtil.setLinearSize(viewHolder.item_icon, list.getResidHeight(), list.getResidHeight());
            }

            //判断该item是否能点击
            if (!list.getClick()) {
                viewHolder.listview_item_linear.setBackgroundResource(R.color.White);
            } else {
                viewHolder.listview_item_linear.setBackgroundResource(R.drawable.listview_item_selector);
            }

            if (list.getSetting() != -1) {
                if (list.getSetting() == 0) {
                    viewHolder.item_setting_close.setVisibility(View.VISIBLE);
                    viewHolder.item_setting_open.setVisibility(View.GONE);
                } else {
                    viewHolder.item_setting_open.setVisibility(View.VISIBLE);
                    viewHolder.item_setting_close.setVisibility(View.GONE);
                }
            }
            if (list.getResid_right1() != null) {
                viewHolder.item_image1.setVisibility(View.VISIBLE);
                viewHolder.item_image1.setImageDrawable(list.getResid_right1());
                viewHolder.item_but.setVisibility(View.GONE);
                viewHolder.item_text.setVisibility(View.GONE);
                viewHolder.item_image.setVisibility(View.GONE);
            }
            //判断右边的按钮是否可见
            if (list.getSwitch() == VISIABLE) {
                viewHolder.item_but.setVisibility(View.VISIBLE);
            }

            //操作显示新闻内容的
            //显示新闻链接的头图片
            if (list.getIcon() != null) {
                viewHolder.item_linear4_linear_icon.setVisibility(View.VISIBLE);
                viewHolder.item_linear4_linear_icon.setImageDrawable(list.getIcon());
                viewHolder.item_linear4_linear.setVisibility(View.VISIBLE);
            } else {
                viewHolder.item_linear4_linear_icon.setVisibility(View.GONE);
                viewHolder.item_linear4_linear.setVisibility(View.GONE);
            }

            //显示新闻发布人的头像
            if (list.getNewsIcon() != null) {
                viewHolder.item_linear4_icon.setVisibility(View.VISIBLE);
                viewHolder.item_linear4_icon.setImageDrawable(list.getNewsIcon());
            } else {
                viewHolder.item_linear4_icon.setVisibility(View.GONE);
            }

            //显示新闻的内容
            if (!"".equals(list.getNewsContent())) {
                viewHolder.item_linear4_msg.setVisibility(View.VISIBLE);
                viewHolder.item_linear4_msg.setText(list.getNewsContent());
            } else {
                viewHolder.item_linear4_msg.setVisibility(View.GONE);
            }

            //显示新闻的名字
            if (!"".equals(list.getNewsname())) {
                viewHolder.item_linear4_name.setVisibility(View.VISIBLE);
                viewHolder.item_linear4_name.setText(list.getNewsname());
            } else {
                viewHolder.item_linear4_name.setVisibility(View.GONE);
            }

            //判断有没有新闻内容
            if (!"".equals(list.getNewsTitle())) {
                viewHolder.item_linear4_linear_title.setVisibility(View.VISIBLE);
                viewHolder.item_linear4_linear_title.setText(list.getNewsTitle());
                viewHolder.item_linear4_linear.setVisibility(View.VISIBLE);
            } else {
                viewHolder.item_linear4_linear_title.setVisibility(View.GONE);
                viewHolder.item_linear4_linear.setVisibility(View.GONE);
            }

            //判断是否拥有gridview
            if (list.getList() != null) {
                viewHolder.item_linear4_girdview.setVisibility(View.VISIBLE);
                ChlidBaseAdapter adapter = new ChlidBaseAdapter(context, list.getList());
                viewHolder.item_linear4_girdview.setAdapter(adapter);
            } else {
                viewHolder.item_linear4_girdview.setVisibility(View.GONE);
            }
        }
    }

    public void setClick(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 更新listview
     *
     * @param alist
     */
    public void ChangeData(ArrayList<Object> alist) {
        this.alist = alist;
        this.notifyDataSetChanged();
    }

    /**
     * 获取item的标志
     */
    public void setIconClick(MyOnClickInterface myOnClickInterface) {
        this.myOnClickInterface = myOnClickInterface;
    }

//    public void setRight_IconSize(int width, int height) {
//        DensityUtil.setAbsSize(viewHolder.item_image1, width, height);
//    }
}
