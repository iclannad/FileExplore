package com.example.administrator.ui_sdk;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ListView_Object {

    //左边图片的ID
    private Drawable resid = null;
    //页面的主要内容
    private String content = "";
    //页面的副标题
    private String subtitle = "";
    //右边的标题
    private String right_title = "";
    //判断是否为空行
    private boolean Prompt;
    //空行的标题
    private String PromptContent = "";
    //空行的高度
    private int height = 0;
    //右边的按钮
    private int Switch = 0;
    //右边的图标
    private Drawable resid_right = null;
    //中间的图标
    private Drawable resid_center = null;
    private Drawable resid_center1 = null;
    //中间的文字
    private String content_center = "";
    //判断是否允许点击
    private boolean click = true;
    //判断是否允许显示设置按钮
    private int setting = -1;
    // 提供设置item的高度
    private int item_height = 0;
    //判断是否是自己发的信息
    private boolean memsg = false;
    //消息的时间
    private String time = "";
    //右边第二个图标
    private Drawable resid_right1 = null;
    //消息ID
    private String chancalid = "";
    //用户名字
    private String Name = "";
    //用户ID
    private String id = "";
    //
    private int resid_center1_width = 0;
    private int resid_center1_height = 0;
    //以下是新闻格式的变量
    private String newsTitle = "";
    private Drawable newsIcon = null;
    private String newsname = "";
    private String newsContent = null;
    private Drawable icon = null;

    private int icon_height = 0;

    private ArrayList<ChlidData> list = null;

    public void setResid(Drawable resid) {
        this.resid = resid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setRight_title(String right_title) {
        this.right_title = right_title;
    }

    public void setPrompt(String Prompt) {
        this.Prompt = Prompt.equals("-");
    }

    public void setPromptContent(String PromptContent) {
        this.PromptContent = PromptContent;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSwitch(int Switch) {
        this.Switch = Switch;
    }

    public void setResid_right(Drawable resid_right) {
        this.resid_right = resid_right;
    }

    public void setResid_center(Drawable resid_center) {
        this.resid_center = resid_center;
    }

    public void setContent_center(String content_center) {
        this.content_center = content_center;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public void setSetting(int setting) {
        this.setting = setting;
    }

    public void setItem_height(int item_height) {
        this.item_height = item_height;
    }

    public void setMemsg(boolean memsg) {
        this.memsg = memsg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setResid_right1(Drawable resid_right1) {
        this.resid_right1 = resid_right1;
    }

    public void setResid_center1(Drawable resid_center1) {
        this.resid_center1 = resid_center1;
    }

    public void setChancalid(String chancalid) {
        this.chancalid = chancalid;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResid_center_width(int resid_center1_width) {
        this.resid_center1_width = resid_center1_width;
    }

    public void setResid_center_height(int resid_center1_height) {
        this.resid_center1_height = resid_center1_height;
    }

    public void setResidHeight(int icon_height) {
        this.icon_height = icon_height;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setNewsIcon(Drawable newsIcon) {
        this.newsIcon = newsIcon;
    }

    public void setNewsname(String newsname) {
        this.newsname = newsname;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setList(ArrayList<ChlidData> list) {
        this.list = list;
    }


    public Drawable getResid() {
        return resid;
    }

    public String getContent() {
        return content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getRight_title() {
        return right_title;
    }

    public boolean getPrompt() {
        return Prompt;
    }

    public String getPromptContent() {
        return PromptContent;
    }

    public int getHeight() {
        return height;
    }

    public Drawable getResid_right() {
        return resid_right;
    }

    public int getSwitch() {
        return Switch;
    }

    public Drawable getResid_center() {
        return resid_center;
    }

    public String getContent_center() {
        return content_center;
    }

    public boolean getClick() {
        return click;
    }

    public int getSetting() {
        return setting;
    }

    public int getItem_height() {
        return item_height;
    }

    public boolean getMemsg() {
        return memsg;
    }

    public String getTime() {
        return time;
    }

    public Drawable getResid_right1() {
        return resid_right1;
    }

    public Drawable getResid_center1() {
        return resid_center1;
    }

    public String getChancalid() {
        return chancalid;
    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return id;
    }


    public int getResid_center_width() {
        return resid_center1_width;
    }

    public int getResid_center_height() {
        return resid_center1_height;
    }


    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsname() {
        return newsname;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public Drawable getNewsIcon() {
        return newsIcon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public ArrayList<ChlidData> getList() {
        return list;
    }

    public int getResidHeight() {
        return icon_height;
    }

}
