package com.example.administrator.ui_sdk.Other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ui_sdk.ListView_Object;
import com.example.administrator.ui_sdk.MyOnClickInterface;
import com.example.administrator.ui_sdk.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/15.
 */
public class ChatBaseAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<ListView_Object> list = null;
    private ListView_Object object = null;

    private View.OnLongClickListener longClickListener = null;
    private MyOnClickInterface.Chat_interface chat = null;

    public ChatBaseAdapter(Context context, ArrayList<ListView_Object> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getMemsg()) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        object = list.get(position);
        boolean flag = object.getMemsg();
        if (convertView == null) {
            if (flag) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_right, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_left, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.resid = (ImageView) convertView.findViewById(R.id.item_image_resid);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.item_text_msg);
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_chat_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!object.getTime().equals("")) {
            viewHolder.time.setText(object.getTime());
        }

        viewHolder.resid.setImageDrawable(object.getResid());
        viewHolder.msg.setText(object.getContent());
        viewHolder.msg.setOnLongClickListener(longClickListener);
        viewHolder.resid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat != null) {
                    chat.IconClick(position);
                }
            }
        });
        //点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat != null) {
                    chat.Click(position);
                }
            }
        });
        return convertView;
    }

    public void setClick(MyOnClickInterface.Chat_interface chat) {
        this.chat = chat;
    }

    public void setIconClick(MyOnClickInterface.Chat_interface chat) {
        this.chat = chat;
    }

    private static class ViewHolder {
        ImageView resid = null;
        TextView msg = null;
        TextView time = null;
        ImageView bg = null;
    }

    public void setLongClick(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
