package com.example.bigtalk.bigtalk.bean;

import com.example.bigtalk.bigtalk.bean.User;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Jacqueline on 2018/3/1.
 */

public class Article extends BmobObject {

    private String type; //contestReport, newTopic, currentEvent, debateTechnique
    private String title;
    private String content;
    private String intro;
    private Date news_time;
    private User news_author;
    private BmobRelation news_comment;
    private Integer news_like;

    public Article() {}

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public Date getNews_time(){
        return news_time;
    }
    public void setNews_time(Date news_time){
        this.news_time = news_time;
    }
    public User getNews_author(){
        return news_author;
    }
    public void setNews_author(User news_author){
        this.news_author = news_author;
    }
    public BmobRelation getNews_comment(){
        return news_comment;
    }
    public void setNews_comment(BmobRelation news_comment){
        this.news_comment = news_comment;
    }
    public Integer getNews_like(){
        return news_like;
    }
    public void setNews_like(Integer news_like){
        this.news_like = news_like;
    }
}
