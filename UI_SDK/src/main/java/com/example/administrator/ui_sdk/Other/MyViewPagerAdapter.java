package com.example.administrator.ui_sdk.Other;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/7.
 */
public class MyViewPagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> list = null;

    public MyViewPagerAdapter(ArrayList<ImageView> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position % list.size()));

    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(list.get(position % list.size()));
        return list.get(position % list.size());
    }

}
