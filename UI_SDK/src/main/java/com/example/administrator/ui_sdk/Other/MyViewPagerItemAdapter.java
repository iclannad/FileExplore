package com.example.administrator.ui_sdk.Other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MyViewPagerItemAdapter extends FragmentPagerAdapter {

    //创建接受对象的链表
    private ArrayList<Fragment> list = null;


    public MyViewPagerItemAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);

        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position % list.size());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
//    @Override
//    public Object instantiateItem(View container, int position) {
//        ((ViewPager) container).addView(list.get(position % list.size()));
//        return list.get(position % list.size());
//    }
}
