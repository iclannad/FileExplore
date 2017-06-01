package com.example.administrator.ui_sdk.MyBaseActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.R;

/**
 * Created by Administrator on 2016/3/10.
 * 这个是模拟WIN8的风格Activity也是继承BaseActivity
 */
public abstract class WinActivity extends BaseActivity {

    private LinearLayout base_linear, base_linear1, base_linear2, base_linear3, base_linear4, base_linear6, base_linear7, base_linear8, base_linear9, base_linear10, base_linear11, base_linear12, base_linear13, base_linear14, base_linear15, base_linear16, base_Linear17 = null;
    private Context context = null;
    private Activity activity = null;
    private LinearLayout base_linear5 = null;

    private View view = null;


    //这个是必须重写的方法这个是初始化程序
    public abstract void Wininti();

    @Override
    public void init() {
        context = this;
        activity = (Activity) context;


        //将默认的标题隐藏
        setTileBar(0);

        view = LayoutInflater.from(context).inflate(R.layout.win, null);

        //设置动态的WIN界面，可以使用各个屏幕大小
        setContentState(view);
        setContent(view);


        Wininti();
    }

    private int tmp = R.id.base_linear;

    //重写父类的点击事件的接口
    @Override
    public void Click(View v) {
        if (v.getId() == R.id.base_linear)
            Linear1Click();
        if (v.getId() == R.id.base_linear3)
            Linear2Click();
        if (v.getId() == R.id.base_linear4)
            Linear3Click();
        if (v.getId() == R.id.base_linear6)
            Linear4Click();
        if (v.getId() == R.id.base_linear9)
            Linear5Click();
        if (v.getId() == R.id.base_linear12)
            Linear6Click();
        if (v.getId() == R.id.base_linear13)
            Linear7Click();
        if (v.getId() == R.id.base_linear15)
            Linear8Click();
        if (v.getId() == R.id.base_linear16)
            Linear9Click();
//        WinClick(v);
    }

    /**
     * 设置win布局
     *
     * @param view
     */
    private void setContentState(View view) {
        base_linear = (LinearLayout) view.findViewById(R.id.base_linear);
        base_linear1 = (LinearLayout) view.findViewById(R.id.base_linear1);
        base_linear2 = (LinearLayout) view.findViewById(R.id.base_linear2);
        base_linear3 = (LinearLayout) view.findViewById(R.id.base_linear3);
        base_linear4 = (LinearLayout) view.findViewById(R.id.base_linear4);
        base_linear5 = (LinearLayout) view.findViewById(R.id.base_linear5);
        base_linear6 = (LinearLayout) view.findViewById(R.id.base_linear6);
        base_linear7 = (LinearLayout) view.findViewById(R.id.base_linear7);
        base_linear8 = (LinearLayout) view.findViewById(R.id.base_linear8);
        base_linear9 = (LinearLayout) view.findViewById(R.id.base_linear9);
        base_linear10 = (LinearLayout) view.findViewById(R.id.base_linear10);
        base_linear11 = (LinearLayout) view.findViewById(R.id.base_linear11);
        base_linear12 = (LinearLayout) view.findViewById(R.id.base_linear12);
        base_linear13 = (LinearLayout) view.findViewById(R.id.base_linear13);
        base_linear14 = (LinearLayout) view.findViewById(R.id.base_linear14);
        base_linear15 = (LinearLayout) view.findViewById(R.id.base_linear15);
        base_linear16 = (LinearLayout) view.findViewById(R.id.base_linear16);

        int height = BaseActivity.height - DensityUtil.dip2px(context, 3) * 2, width = BaseActivity.width;
        //设置win8界面
        DensityUtil.setLinearSize(base_linear, width, height / 3);
        DensityUtil.setLinearSize(base_linear3, width / 3 * 2, height / 6 - DensityUtil.dip2px(context, 3), 0, DensityUtil.dip2px(context, 3), 0, 0);
        DensityUtil.setLinearSize(base_linear4, width / 3 * 2, height / 6);
        DensityUtil.setLinearSize(base_linear6, width / 3, height / 3);

        DensityUtil.setLinearSize(base_linear9, width / 3, height / 3);
        DensityUtil.setLinearSize(base_linear12, width / 3 - DensityUtil.dip2px(context, 3), height / 6 - DensityUtil.dip2px(context, 3), 0, DensityUtil.dip2px(context, 3), 0, 0);
        DensityUtil.setLinearSize(base_linear13, width / 3 - DensityUtil.dip2px(context, 3), height / 6);
        DensityUtil.setLinearSize(base_linear15, width / 3, height / 6 - DensityUtil.dip2px(context, 3), 0, DensityUtil.dip2px(context, 3), 0, 0);
        DensityUtil.setLinearSize(base_linear16, width / 3, height / 6);


        base_linear.setOnClickListener(this);
        base_linear3.setOnClickListener(this);
        base_linear4.setOnClickListener(this);
        base_linear5.setOnClickListener(this);
        base_linear9.setOnClickListener(this);
        base_linear12.setOnClickListener(this);
        base_linear13.setOnClickListener(this);
        base_linear15.setOnClickListener(this);
    }


    //-----------------下面就是设置各个布局可以在里面添加各种各样的布局---------------------//
    //-----------------这个些布局的排序是从上往下，从左往右 , 刚开始是分开三部分上中下之后就按照上下左右的方式对应每一个模块//

    /**
     * 这个是添加第一个布局
     *
     * @param resid
     */
    public void setBase_linear1(int resid) {
        NavaddView(base_linear, resid);
    }

    /**
     * 这个是添加第二个布局
     *
     * @param resid
     */
    public void setBase_linear2(int resid) {
        NavaddView(base_linear3, resid);
    }

    /**
     * 这个是添加第三个布局
     *
     * @param resid
     */
    public void setBase_linear3(int resid) {
        NavaddView(base_linear4, resid);
    }

    /**
     * 这个是添加第四个布局
     *
     * @param resid
     */
    public void setBase_linear4(int resid) {
        NavaddView(base_linear6, resid);
    }

    /**
     * 这个是添加第五个布局
     *
     * @param resid
     */
    public void setBase_linear5(int resid) {
        NavaddView(base_linear9, resid);
    }

    /**
     * 这个是添加第六个布局
     *
     * @param resid
     */
    public void setBase_linear6(int resid) {
        NavaddView(base_linear12, resid);
    }

    /**
     * 这个是添加第七个布局
     *
     * @param resid
     */
    public void setBase_linear7(int resid) {
        NavaddView(base_linear13, resid);
    }

    /**
     * 这个是添加第八个布局
     *
     * @param resid
     */
    public void setBase_linear8(int resid) {
        NavaddView(base_linear15, resid);
    }

    /**
     * 这个是添加第九个布局
     *
     * @param resid
     */
    public void setBase_linear9(int resid) {
        NavaddView(base_linear16, resid);
    }

    //--------------------下面是设置一些配置的方法

    /**
     * 这个是可以设置背景的图片
     * 这个格式是资源int类型
     *
     * @param resid
     */
    public void setWinContentDrawable(int resid) {
        setContentDrawable(resid);
    }

    /**
     * 这个也是可以设置背景图片
     * 这个格式资源是bitmap类型
     *
     * @param drawable
     */
    public void setWinContentDrawable(Drawable drawable) {
        setContentDrawable(drawable);
    }

    /**
     * 设置背景的颜色
     *
     * @param resid
     */
    public void setWinContentColor(int resid) {
        setContentColor(resid);
    }

    //-------下面是设置每个模块的颜色

    /**
     * 这个是设置第一个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor1(int resid) {
        base_linear.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第二个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor2(int resid) {
        base_linear3.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第三个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor3(int resid) {
        base_linear4.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第四个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor4(int resid) {
        base_linear6.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第五个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor5(int resid) {
        base_linear9.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第六个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor6(int resid) {
        base_linear12.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第七个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor7(int resid) {
        base_linear13.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第八个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor8(int resid) {
        base_linear15.setBackgroundResource(resid);
    }

    /**
     * 这个是设置第九个模块的颜色
     *
     * @param resid
     */
    public void setLinearColor9(int resid) {
        base_linear16.setBackgroundResource(resid);
    }


    //---------下面是每个模块的点击事件
    public void Linear1Click() {
    }

    public void Linear2Click() {
    }

    public void Linear3Click() {
    }

    public void Linear4Click() {
    }

    public void Linear5Click() {

    }

    public void Linear6Click() {

    }

    public void Linear7Click() {

    }

    public void Linear8Click() {

    }

    public void Linear9Click() {

    }


    /**
     * 这个是用来添加布局文件到组件的方法
     *
     * @param v
     * @param resid
     */
    public void NavaddView(LinearLayout v, int resid) {
        try {
            v.addView(LayoutInflater.from(context).inflate(resid, null));
        } catch (Exception e) {
            Log.e("Ruan", "添加布局文件出错，检查是否是布局文件有问题");
        }
    }
}
