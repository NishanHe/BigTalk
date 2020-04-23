package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Train extends BmobObject {
    private User trainee;
    private Topics topic;
    private String side;
    private String score;
    private String part;
    private BmobFile userAnswer;


    public User getTrainee() {
        return trainee;
    }

    public void setTrainee(User trainee) {
        this.trainee = trainee;
    }

    public Topics getTopic() {
        return topic;
    }

    public void setTopic(Topics topic) {
        this.topic = topic;
    }


    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public BmobFile getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(BmobFile userAnswer) {
        this.userAnswer = userAnswer;
    }


}
