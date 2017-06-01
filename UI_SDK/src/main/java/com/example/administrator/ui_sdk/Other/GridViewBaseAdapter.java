package com.example.administrator.ui_sdk.Other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.ListView_Object;
import com.example.administrator.ui_sdk.R;
import com.example.administrator.ui_sdk.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/2.
 */
public class GridViewBaseAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<ListView_Object> list = null;

    private ListView_Object object = null;
    private ViewHolder viewHolder = null;

    public GridViewBaseAdapter(Context context, ArrayList<ListView_Object> list) {
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
        object = list.get(position);

        viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.gridviewadapter, null);

            viewHolder.gimageView = (ImageView) convertView.findViewById(R.id.gimageView);
            viewHolder.gtextView = (TextView) convertView.findViewById(R.id.gtextView);
            viewHolder.glinearlayout = (LinearLayout) convertView.findViewById(R.id.glinear);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (object.getResid_center() != null) {
            viewHolder.gimageView.setImageDrawable(object.getResid_center());
        }

        if (!"".equals(object.getContent_center())) {
            viewHolder.gtextView.setText(object.getContent_center());
        }

        if (object.getResid_center_height() != 0){
            DensityUtil.setHeight(viewHolder.glinearlayout, object.getResid_center_height());
        }

        return convertView;
    }
}
