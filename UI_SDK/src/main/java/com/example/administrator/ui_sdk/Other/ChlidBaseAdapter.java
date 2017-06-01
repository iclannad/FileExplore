package com.example.administrator.ui_sdk.Other;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.ui_sdk.ChlidData;
import com.example.administrator.ui_sdk.R;
import com.example.administrator.ui_sdk.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/22.
 */
public class ChlidBaseAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<ChlidData> list = null;
    private ViewHolder viewHolder = null;

    public ChlidBaseAdapter(Context context, ArrayList<ChlidData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.childadapter, null);

            viewHolder.child_linear1 = (LinearLayout) convertView.findViewById(R.id.child_linear1);
            viewHolder.child_linear1_image = (ImageView) convertView.findViewById(R.id.child_linear1_image);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HandlerData(viewHolder, list.get(position));

        return convertView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void HandlerData(ViewHolder viewHolder, ChlidData list) {
        if (list.getBitmap() != null) {
            viewHolder.child_linear1.setVisibility(View.VISIBLE);
            viewHolder.child_linear1_image.setImageBitmap(list.getBitmap());
        }
    }
}
