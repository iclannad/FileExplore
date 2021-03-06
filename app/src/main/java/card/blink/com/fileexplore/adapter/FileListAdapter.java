package card.blink.com.fileexplore.adapter;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.tools.Tools;

/**
 * Created by Administrator on 2017/5/27.
 */
public class FileListAdapter extends BaseAdapter {
    private static final String TAG = FileListAdapter.class.getSimpleName();
    private Context context;
    private List<Pair<String, Integer>> list;


    public FileListAdapter(Context context, List<Pair<String, Integer>> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null)
            holder = (ViewHolder) convertView.getTag();
        else {
            convertView = View.inflate(context, R.layout.simple_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.image_view_list_item);
            holder.name = (TextView) convertView
                    .findViewById(R.id.text_view_list_item);

            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
            holder.size = (TextView) convertView.findViewById(R.id.tv_file_size);
            holder.time = (TextView) convertView.findViewById(R.id.tv_file_alter_time);

            convertView.setTag(holder);
        }
        Pair<String, Integer> pair = list.get(position);
        if (pair != null) {
            // A中存放在是全路径
            String[] item_names = pair.getA().split("/");
            String item_name = item_names[item_names.length - 1];

            long timel = pair.getTime();
            String time = Tools.getTime(timel);

            String size = pair.getSize();
            boolean upan = pair.isUpan();
            //Log.v(TAG, "upan===" + upan);

            //　打印每个条目的信息
            //Log.d("run", "item_name=" + item_name + " pair==" + pair.getB());
            // 根据B设置图标
            switch (pair.getB()) {
                // 盘
                case Protocol.PAN:
                    holder.icon.setImageResource(R.mipmap.item_pan);
                    holder.name.setText(item_name);

                    holder.ll.setVisibility(View.GONE);
                    break;
                case Protocol.DIR:
                    // 文件夹
                    holder.icon.setImageResource(R.mipmap.icon_folder);
                    holder.name.setText(item_name);

                    if (upan) {
                        holder.ll.setVisibility(View.VISIBLE);
                        holder.size.setVisibility(View.INVISIBLE);
                        holder.time.setText(time + "");
                    } else {
                        holder.ll.setVisibility(View.GONE);
                    }

                    break;
                case Protocol.FL:
                    // 文件
                    holder.icon.setImageResource(Mime.getFileIconId(item_name));
                    holder.name.setText(item_name);

                    if (upan) {
                        holder.ll.setVisibility(View.VISIBLE);
                        holder.size.setVisibility(View.VISIBLE);
                        holder.size.setText(size + "");
                        holder.time.setText(time + "");
                    } else {
                        holder.ll.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }

        return convertView;
    }

    /**
     * 更新界面
     */
    public void setList(List<Pair<String, Integer>> l) {
        this.list = l;
        notifyDataSetChanged();
    }

    public final class ViewHolder {
        public TextView name;
        public ImageView icon;

        public LinearLayout ll;
        public TextView size;
        public TextView time;
    }

    /**
     * 内部类
     */
    public static class Pair<A, B> {
        private A a;
        private B b;

        private boolean isUpan;

        public boolean isUpan() {
            return isUpan;
        }

        public void setUpan(boolean upan) {
            isUpan = upan;
        }

        private long time;
        private String size;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public Pair() {

        }

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getA() {
            return a;
        }

        public B getB() {
            return b;
        }

        public void setA(A a) {
            this.a = a;
        }

        public void setB(B b) {
            this.b = b;
        }

        public String toString() {
            return "(" + a + ", " + b + ")";
        }
    }
}
