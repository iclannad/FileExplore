package com.example.administrator.ui_sdk;

/**
 * Created by Soft on 2016/7/9.
 */
public interface ItemClick {

    /**
     * item的子控件点击事件
     *
     * @param position 子控件在列表中属于第几个
     * @param View     子控件的序号靠最左边为第0个
     */
    public void OnClick(int position, int View);

    public interface RreshInterface {

        /**
         * 下拉刷新界面的接口
         */
        public void RreshData();

    }
}