package com.example.administrator.ui_sdk;

import android.view.View;

/**
 * Created by Administrator on 2016/8/9.
 */
public interface MyCircleLoading {

    /**
     * 这个是点击事件的接口
     *
     * @param v
     */
    public void circleClick(View v);


    public interface Waiting {

        /**
         * 当前动画是否结束的接口
         *
         * @param state true已经结束false还在进行中
         */
        public void AnimationState(boolean state);
    }

    public interface ShowMemory {

        /**
         * 动画结束
         */
        public void AnimationEnd();
    }
}
