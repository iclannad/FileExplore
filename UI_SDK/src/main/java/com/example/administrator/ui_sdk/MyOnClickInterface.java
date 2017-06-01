package com.example.administrator.ui_sdk;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Administrator on 2016/1/7.
 */
public interface MyOnClickInterface {


    /**
     * 这边是对话框Item点击事件的接口
     */
    public interface ItemClick {

        public void OnItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     * 这边是对话框按钮点击事件的接口
     */
    public interface Click {
        //右边按钮的点击事件
        public void OnRightClick(int FLAG);
        //左边按钮的点击事件
        public void OnLeftClick(int FLAG);
    }

    /**
     * This is 下拉组件的下拉更新的接口
     */
    public interface Push {
        //下拉刷新
        public void dropdownflush();
        //上拉加载更多
        public void highpullflush();
    }

    public void ItemOnClick(int position);

    public interface Chat_interface{

        public void Click(int position);

        public void IconClick(int position);
    }

    /**
     * 回调接口
     *
     * @author <a href="clarkamx@gmail.com">LynK</a>
     *         <p/>
     *         Create on 2012-1-6 上午8:21:05
     */
    public interface OnColorChangedListener {
        /**
         * 回调函数
         *
         * @param color 选中的颜色
         */
        void colorChanged(int color);
    }
}

