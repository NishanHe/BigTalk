package com.example.bigtalk.bigtalk.latest.bean;

import com.example.bigtalk.bigtalk.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2018/2/28.
 */

public class Comment extends BmobObject {

    private Article news;
    private String comment_content;
    private User comment_author;

    public Article getNews(){
        return news;
    }
    public void setNews(Article news){
        this.news = news;
    }
    public String  getComment_content(){
        return comment_content;
    }
    public void setComment_content(String comment_content){
        this.comment_content = comment_content;
    }
    public User getComment_author(){
        return comment_author;
    }
    public void setComment_author(User comment_author){
        this.comment_author = comment_author;
    }

}
