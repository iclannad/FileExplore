package com.example.administrator.ui_sdk;

/**
 * Created by 19820 on 2016/5/7.
 */
public interface SorViewInterface {


    /**
     * 这个点击事件就是右边滑动的字母的滑动时间
     * @param s         返回滑动到的字母
     * @param postion   返回滑动到第几个字母
     */
    public void onItemClick(String s, int postion);

}
