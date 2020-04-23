package com.example.bigtalk.bigtalk.frontpage.bean;

import com.example.bigtalk.bigtalk.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class Topics extends BmobObject {
    private String topic_time;
    private String topic_name;
    private String topic_type;
    private User topic_author;
    private String topic_intro;
    private String censor_status;
    private String posi_opi;
    private String nega_opi;
    private Integer news_like;


    public String getPosi_opi() {
        return posi_opi;
    }

    public void setPosi_opi(String posi_opi) {
        this.posi_opi = posi_opi;
    }

    public String getNega_opi() {
        return nega_opi;
    }

    public void setNega_opi(String nega_opi) {
        this.nega_opi = nega_opi;
    }



    public Integer getNews_like() {
        return news_like;
    }

    public void setNews_like(Integer news_like) {
        this.news_like = news_like;
    }



    public String getTopic_status() {
        return topic_status;
    }

    public void setTopic_status(String topic_status) {
        this.topic_status = topic_status;
    }

    private String topic_status;

    public String getCensor_status() {
        return censor_status;
    }

    public void setCensor_status(String censor_status) {
        this.censor_status = censor_status;
    }





    public String getTopic_intro() {
        return topic_intro;
    }

    public void setTopic_intro(String topic_intro) {
        this.topic_intro = topic_intro;
    }



    public String getTopic_time() {
        return topic_time;
    }

    public void setTopic_time(String topic_time) {
        this.topic_time = topic_time;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public User getTopic_author() {
        return topic_author;
    }

    public void setTopic_author(User topic_author) {
        this.topic_author = topic_author;
    }
}
