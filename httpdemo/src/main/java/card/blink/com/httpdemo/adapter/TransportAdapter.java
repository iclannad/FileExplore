package card.blink.com.httpdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import card.blink.com.httpdemo.R;
import card.blink.com.httpdemo.activity.Task;

/**
 * Created by Administrator on 2017/6/6.
 */
public class TransportAdapter extends BaseAdapter {

    ArrayList<Task> list;
    Context context;

    public TransportAdapter(ArrayList<Task> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(ArrayList<Task> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null)
            holder = (ViewHolder) convertView.getTag();
        else {
            convertView = View.inflate(context, R.layout.transport_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.progress = (TextView) convertView
                    .findViewById(R.id.tv_progress);
            convertView.setTag(holder);
        }

        Task task = list.get(position);
        holder.name.setText(task.name);
        holder.progress.setText(task.progress + "");

        return convertView;
    }

    public final class ViewHolder {
        public TextView name;
        public TextView progress;
    }
}
