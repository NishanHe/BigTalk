package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Topics  extends BmobObject{
    private String topic_status;
    private String topic_intro;
    private String topic_name;
    private String posi_opi;
    private String nega_opi;
    private String topic_id;
    private String topic_type;
    private String censor_status;
    private User topic_author;


    private BmobFile example_answer;

    public User getTopic_author() {
        return topic_author;
    }

    public void setTopic_author(User topic_author) {
        this.topic_author = topic_author;
    }

    public String getTopic_intro() {
        return topic_intro;
    }

    public void setTopic_intro(String topic_intro) {
        this.topic_intro = topic_intro;
    }

    public String getCensor_status() {
        return censor_status;
    }

    public void setCensor_status(String censor_status) {
        this.censor_status = censor_status;
    }

    public String getTopic_status() {
        return topic_status;
    }

    public void setTopic_status(String topic_status) {
        this.topic_status = topic_status;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
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

    public String getPosi_opi() {
        return posi_opi;
    }

    public void setPosi_opi(String posi_opi) {this.posi_opi = posi_opi;}

    public String getNega_opi() {
        return nega_opi;
    }

    public void setNega_opi(String nega_opi) {this.nega_opi = nega_opi;}

    public BmobFile getExample_answer() {
        return example_answer;
    }

    public void setExample_answer(BmobFile example_answer) {
        this.example_answer = example_answer;
    }

}
