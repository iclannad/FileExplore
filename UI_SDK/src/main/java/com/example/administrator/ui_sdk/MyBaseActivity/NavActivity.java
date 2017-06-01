package com.example.administrator.ui_sdk.MyBaseActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.R;


/**
 * Created by Administrator on 2016/3/10.
 * <p/>
 * 这个是一个拥有导航栏的Activity这个是继承BaseActivity
 * 不但拥有最基础的标题栏还有拥有自己的导航栏
 */
public abstract class NavActivity extends BaseActivity {

    public Activity activity = null;
    public Context context = null;

    private View view = null;
    private LinearLayout nav_nav = null;
    private LinearLayout nav_contentView = null;
    private LinearLayout nav_LeftView = null;
    private View NavView = null;

    //以下就是默认导航的组件
    private RelativeLayout nav1, nav2, nav3, nav4 = null;
    private ImageView nav_1_image, nav_2_image, nav_3_image, nav_4_image = null;
    private TextView nav_1_text, nav_2_text, nav_3_text, nav_4_text = null;
    private LinearLayout nav_origin1_linear1, nav_origin2_linear2, nav_origin3_linear3, nav_origin4_linear4 = null;
    private LinearLayout nav_main = null;


    private Drawable DNav1Select, DNav1Unselect, DNav2Select, DNav2Unselect, DNav3Select, DNav3Unselect, DNav4Select, DNav4Unselect;
    private int TNav1Select, TNav1Unselect, TNav2Select, TNav2Unselect, TNav3Select, TNav3Unselect, TNav4Select, TNav4Unselect;

    /**
     * 这个方式实现将导航的布局文件添加到指定的布局当中
     *
     * @param resid
     */
    public void setNav(int resid) {
        if (resid != 0) {
            //将标题栏的布局显示出来
            nav_nav.setVisibility(View.VISIBLE);
            //移除nav_nav的所有组件
            nav_nav.removeAllViews();
            //设置默认高度
            setNavHeight(nav_nav, height / 12);
            //获取nav这个布局
            NavView = LayoutInflater.from(context).inflate(R.layout.nav, null);

            //判断是不是默认的导航栏
            if (resid == R.layout.nav) {
                setNavColor(android.R.color.transparent);

                nav1 = (RelativeLayout) NavView.findViewById(R.id.nav1);
                nav2 = (RelativeLayout) NavView.findViewById(R.id.nav2);
                nav3 = (RelativeLayout) NavView.findViewById(R.id.nav3);
                nav4 = (RelativeLayout) NavView.findViewById(R.id.nav4);
                nav_1_image = (ImageView) NavView.findViewById(R.id.nav_1_image);
                nav_2_image = (ImageView) NavView.findViewById(R.id.nav_2_image);
                nav_3_image = (ImageView) NavView.findViewById(R.id.nav_3_image);
                nav_4_image = (ImageView) NavView.findViewById(R.id.nav_4_image);
                nav_1_text = (TextView) NavView.findViewById(R.id.nav_1_text);
                nav_2_text = (TextView) NavView.findViewById(R.id.nav_2_text);
                nav_3_text = (TextView) NavView.findViewById(R.id.nav_3_text);
                nav_4_text = (TextView) NavView.findViewById(R.id.nav_4_text);
                nav_origin1_linear1 = (LinearLayout) NavView.findViewById(R.id.nav_origin1_linear1);
                nav_origin2_linear2 = (LinearLayout) NavView.findViewById(R.id.nav_origin2_linear2);
                nav_origin3_linear3 = (LinearLayout) NavView.findViewById(R.id.nav_origin3_linear3);
                nav_origin4_linear4 = (LinearLayout) NavView.findViewById(R.id.nav_origin4_linear4);
                nav_main = (LinearLayout) NavView.findViewById(R.id.nav_main);


                //给导航设置默认的属性
//                DNav1Unselect = ContextCompat.getDrawable(this, R.mipmap.house_up);
//                DNav2Unselect = ContextCompat.getDrawable(this, R.mipmap.shopping_up);
//                DNav3Unselect = ContextCompat.getDrawable(this, R.mipmap.speech_up);
//                DNav4Unselect = ContextCompat.getDrawable(this, R.mipmap.basket_up);
//                DNav1Select = ContextCompat.getDrawable(this, R.mipmap.house);
//                DNav2Select = ContextCompat.getDrawable(this, R.mipmap.shopping);
//                DNav3Select = ContextCompat.getDrawable(this, R.mipmap.speech);
//                DNav4Select = ContextCompat.getDrawable(this, R.mipmap.basket);

                DNav1Unselect = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                DNav2Unselect = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                DNav3Unselect = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                DNav4Unselect = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);

                TNav1Select = 0xff28beff;
                TNav2Select = 0xff28beff;
                TNav3Select = 0xff28beff;
                TNav4Select = 0xff28beff;
                TNav1Unselect = 0xff6b6c6e;
                TNav2Unselect = 0xff6b6c6e;
                TNav3Unselect = 0xff6b6c6e;
                TNav4Unselect = 0xff6b6c6e;


                nav1.setOnClickListener(this);
                nav2.setOnClickListener(this);
                nav3.setOnClickListener(this);
                nav4.setOnClickListener(this);
                //这个是设置导航的宽高度
                DensityUtil.setLinearSize(nav_main, width, height / 12);

            }

            nav_nav.addView(NavView);
        } else {
            nav_nav.setVisibility(View.GONE);
        }
    }

    private boolean isClick = true;

    @Override
    public void Click(View v) {
        NacClick(v);
        if (v == nav1) {
            setOnScrollClick(0);
            setNavClick(0);
            return;
        }
        if (v == nav2) {
            setOnScrollClick(1);
            setNavClick(1);
            return;
        }
        if (v == nav3) {
            setOnScrollClick(2);
            setNavClick(2);
            return;
        }
        if (v == nav4) {
            setOnScrollClick(3);
            setNavClick(3);
            return;
        }
    }

    public void NacClick(View v) {
    }

    private int selectTxt = 0, unselectTxt = 0;
    private int[] selectPic, unselectPic;
    private Drawable[] DselectPic, DunselectPic;

    /**
     * 设置点击选中和不选中的颜色
     *
     * @param selectTxt
     * @param unselectTxt
     */
    public void setNavTxt(int selectTxt, int unselectTxt) {
        this.selectTxt = selectTxt;
        this.unselectTxt = unselectTxt;
        unsetSelect();
        setSelect(0);
    }

    /**
     * 设置点击选中和不选中的图片
     *
     * @param selectPic
     * @param unselectPic
     */
    public void setNavPic(int[] selectPic, int[] unselectPic) {
        this.selectPic = selectPic;
        this.unselectPic = unselectPic;
        unsetSelect();
        setSelect(0);
    }

    /**
     * 选中那个就设置那个颜色
     *
     * @param position
     */
    public void setNavPosition(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    /**
     * 设置点击选中和不选中的图片
     *
     * @param selectPic
     * @param unselectPic
     */
    public void setNavPic(Drawable[] selectPic, Drawable[] unselectPic) {
        this.DselectPic = selectPic;
        this.DunselectPic = unselectPic;
        unsetSelect();
        setSelect(0);
    }


    /**
     * 这个是四个导航的点击事件
     * 这个是抽象方法不需要在主类实现
     *
     * @param position
     */
    public abstract void setNavClick(int position);

    /**
     * 这个也是一个抽象的方法不需要主类实现
     * 这个是程序的初始化的方法
     */
    public abstract void Nav();

    private DrawerLayout activity_drawer_layout = null;

    /**
     * 这个方法是实现BaseActivity的 抽象方法
     * 有点类似Activity的onCreate是这个程序的开始
     */
    @Override
    public void init() {
        context = this;
        activity = (Activity) context;
        //将布局文件获取
        view = LayoutInflater.from(context).inflate(R.layout.nav_main, null);


        nav_nav = (LinearLayout) view.findViewById(R.id.nav_nav);
        nav_contentView = (LinearLayout) view.findViewById(R.id.nav_contentView);
        nav_LeftView = (LinearLayout) view.findViewById(R.id.nav_LeftView);
        activity_drawer_layout = (DrawerLayout) view.findViewById(R.id.activity_drawer_layout);

        setNav(R.layout.nav);

        setContent(view);
        //这里调用方法的实现
        Nav();

        setNav1Image(DNav1Select);
        setNav1Color(TNav1Select);
        setNav2Image(DNav2Unselect);
        setNav2Color(TNav2Unselect);
        setNav3Image(DNav3Unselect);
        setNav3Color(TNav3Unselect);
        setNav4Image(DNav4Unselect);
        setNav4Color(TNav4Unselect);
    }


    public void setNavTitle(int resid) {
        setTileBar(resid);
        if (resid == 0)
            setSystemWindows(true);
    }

//    public void setNavColor(int resid){
//        nav_nav.setBackgroundResource(resid);
//    }

    /**
     * 这个是将布局文件添加都指定位置
     *
     * @param view
     */
    public void setNavContent(View view) {
        //将这个布局组件清空一下
//        nav_contentView.removeAllViews();
        //将组件添加到文件当中
        nav_contentView.addView(view);
    }

    /**
     * 给左边菜单添加布局
     *
     * @param view
     */
    public void setNavLeftContent(View view) {
        nav_LeftView.addView(view);
    }

    /**
     * 关闭菜单
     */
    public void setCloseMenu() {
        activity_drawer_layout.closeDrawers();
    }

    /**
     * 打开菜单
     */
    public void setOpenMenu() {
        activity_drawer_layout.openDrawer(Gravity.LEFT);
    }

    /**
     * 设置菜单的宽度
     *
     * @param width
     */
    public void setMenuWidth(int width) {
        DensityUtil.setDrawSize(nav_LeftView, width, BaseActivity.height);
    }

    /**
     * 设置菜单背景颜色
     *
     * @param resid
     */
    public void setMenuBackColor(int resid) {
        nav_LeftView.setBackgroundResource(resid);
    }

    /**
     * 设置导航的高度
     *
     * @param height
     */
    public void setNavHeight(View v, int height) {
        //设置标题的高度
        DensityUtil.setRelHeight(v, height);
    }

    /**
     * 设置导航颜色
     *
     * @param resid
     */
    public void setNavColor(int resid) {
        NavView.setBackgroundResource(resid);
    }

    /**
     * 标题栏1的字体
     *
     * @param msg
     */
    public void setNav1(String msg) {
        nav_1_text.setText(msg);
    }

    /**
     * 标题栏1的字体颜色
     *
     * @param resid
     */
    public void setNav1Color(int resid) {
        nav_1_text.setTextColor(resid);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav1Unselect(int resid) {
        this.TNav1Unselect = resid;
    }


    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav1Select(int resid) {
        this.TNav1Select = resid;
    }

    /**
     * 标题栏1的照片
     *
     * @param drawable
     */
    public void setNav1Image(Drawable drawable) {
        nav_1_image.setImageDrawable(drawable);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param drawable
     */
    public void setNav1ColorUnselect(Drawable drawable) {
        this.DNav1Unselect = drawable;
    }

    /**
     * 设置点击时候的图片
     *
     * @param drawable
     */
    public void setNav1ColorSelect(Drawable drawable) {
        this.DNav1Select = drawable;
    }


    /**
     * 标题栏2的字体
     *
     * @param msg
     */
    public void setNav2(String msg) {
        nav_2_text.setText(msg);
    }

    /**
     * 标题栏2的字体颜色
     *
     * @param resid
     */
    public void setNav2Color(int resid) {
        nav_2_text.setTextColor(resid);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav2Unselect(int resid) {
        this.TNav2Unselect = resid;
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav2Select(int resid) {
        this.TNav2Select = resid;
    }

    /**
     * 标题栏2的照片
     *
     * @param drawable
     */
    public void setNav2Image(Drawable drawable) {
        nav_2_image.setImageDrawable(drawable);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param drawable
     */
    public void setNav2ColorUnselect(Drawable drawable) {
        this.DNav2Unselect = drawable;
    }

    /**
     * 设置点击时候的图片
     *
     * @param drawable
     */
    public void setNav2ColorSelect(Drawable drawable) {
        this.DNav2Select = drawable;
    }


    /**
     * 标题栏3的字体
     *
     * @param msg
     */
    public void setNav3(String msg) {
        nav_3_text.setText(msg);
    }

    /**
     * 标题栏3的字体颜色
     *
     * @param resid
     */
    public void setNav3Color(int resid) {
        nav_3_text.setTextColor(resid);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav3Unselect(int resid) {
        this.TNav3Unselect = resid;
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav3Select(int resid) {
        this.TNav3Select = resid;
    }

    /**
     * 标题栏3的照片
     *
     * @param drawable
     */
    public void setNav3Image(Drawable drawable) {
        nav_3_image.setImageDrawable(drawable);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param drawable
     */
    public void setNav3ColorUnselect(Drawable drawable) {
        this.DNav3Unselect = drawable;
    }

    /**
     * 设置点击时候的图片
     *
     * @param drawable
     */
    public void setNav3ColorSelect(Drawable drawable) {
        this.DNav3Select = drawable;
    }


    /**
     * 标题栏4的字体
     *
     * @param msg
     */
    public void setNav4(String msg) {
        nav_4_text.setText(msg);
    }

    /**
     * 标题栏4的字体颜色
     *
     * @param resid
     */
    public void setNav4Color(int resid) {
        nav_4_text.setTextColor(resid);
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav4Unselect(int resid) {
        this.TNav4Unselect = resid;
    }

    /**
     * 设置不点击时候的图片
     *
     * @param resid
     */
    public void setNav4Select(int resid) {
        this.TNav4Select = resid;
    }

    /**
     * 标题栏4的照片
     *
     * @param drawable
     */
    public void setNav4Image(Drawable drawable) {
        nav_4_image.setImageDrawable(drawable);
    }


    /**
     * 设置不点击时候的图片
     *
     * @param drawable
     */
    public void setNav4ColorUnselect(Drawable drawable) {
        this.DNav4Unselect = drawable;
    }

    /**
     * 设置点击时候的图片
     *
     * @param drawable
     */
    public void setNav4ColorSelect(Drawable drawable) {
        this.DNav4Select = drawable;
    }

    /**
     * 这个是第一个导航栏的点击事件
     */
    private void setNav1Click() {
        setNav1Image(DNav1Select);
        setNav1Color(TNav1Select);
        setNav2Image(DNav2Unselect);
        setNav2Color(TNav2Unselect);
        setNav3Image(DNav3Unselect);
        setNav3Color(TNav3Unselect);
        setNav4Image(DNav4Unselect);
        setNav4Color(TNav4Unselect);
    }

    /**
     * 这个是第二个导航栏的点击事件
     */
    private void setNav2Click() {
        setNav1Image(DNav1Unselect);
        setNav1Color(TNav1Unselect);
        setNav2Image(DNav2Select);
        setNav2Color(TNav2Select);
        setNav3Image(DNav3Unselect);
        setNav3Color(TNav3Unselect);
        setNav4Image(DNav4Unselect);
        setNav4Color(TNav4Unselect);
    }

    /**
     * 这个是第三个导航栏的点击事件
     */
    private void setNav3Click() {
        setNav1Image(DNav1Unselect);
        setNav1Color(TNav1Unselect);
        setNav2Image(DNav2Unselect);
        setNav2Color(TNav2Unselect);
        setNav3Image(DNav3Select);
        setNav3Color(TNav3Select);
        setNav4Image(DNav4Unselect);
        setNav4Color(TNav4Unselect);
    }

    /**
     * 这个是第四个导航栏的点击事件
     */
    private void setNav4Click() {
        setNav1Image(DNav1Unselect);
        setNav1Color(TNav1Unselect);
        setNav2Image(DNav2Unselect);
        setNav2Color(TNav2Unselect);
        setNav3Image(DNav3Unselect);
        setNav3Color(TNav3Unselect);
        setNav4Image(DNav4Select);
        setNav4Color(TNav4Select);
    }

    /**
     * 当页面出现页面切换时界面的改变
     *
     * @param position
     */
    public void setOnScrollClick(int position) {
        unsetSelect();
        setSelect(position);
        switch (position) {
            case 0:
                setNav1Click();
                break;
            case 1:
                setNav2Click();
                break;
            case 2:
                setNav3Click();
                break;
            case 3:
                setNav4Click();
                break;
        }
    }

    private void setSelect(int position) {
        switch (position) {
            case 0:
                if (selectPic != null)
                    DNav1Select = getResources().getDrawable(selectPic[0]);
                if (DselectPic != null)
                    DNav1Select = DselectPic[0];
                if (selectTxt != 0)
                    TNav1Select = selectTxt;
                break;
            case 1:
                if (selectPic != null)
                    DNav2Select = getResources().getDrawable(selectPic[1]);
                if (DselectPic != null)
                    DNav2Select = DselectPic[1];
                if (selectTxt != 0)
                    TNav2Select = selectTxt;
                break;
            case 2:
                if (selectPic != null)
                    DNav3Select = getResources().getDrawable(selectPic[2]);
                if (DselectPic != null)
                    DNav3Select = DselectPic[2];
                if (selectTxt != 0)
                    TNav3Select = selectTxt;
                break;
            case 3:
                if (selectPic != null)
                    DNav4Select = getResources().getDrawable(selectPic[3]);
                if (DselectPic != null)
                    DNav4Select = DunselectPic[3];
                if (selectTxt != 0)
                    TNav4Select = selectTxt;
                break;
        }
    }

    private void unsetSelect() {
        if (unselectPic != null) {
            DNav1Unselect = getResources().getDrawable(unselectPic[0]);
            DNav2Unselect = getResources().getDrawable(unselectPic[1]);
            DNav3Unselect = getResources().getDrawable(unselectPic[2]);
            DNav4Unselect = getResources().getDrawable(unselectPic[3]);
        }
        if (DunselectPic != null) {
            DNav1Unselect = DunselectPic[0];
            DNav2Unselect = DunselectPic[1];
            DNav3Unselect = DunselectPic[2];
            DNav4Unselect = DunselectPic[3];
        }

        if (unselectTxt != 0) {
            TNav1Unselect = unselectTxt;
            TNav2Unselect = unselectTxt;
            TNav3Unselect = unselectTxt;
            TNav4Unselect = unselectTxt;
        }
    }


}
