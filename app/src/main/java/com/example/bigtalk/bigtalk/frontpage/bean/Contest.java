package com.example.bigtalk.bigtalk.frontpage.bean;

import com.example.bigtalk.bigtalk.bean.*;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class Contest extends BmobObject{
    private String contest_id;
    private String contest_result;
    private String contest_time;

    private String contest_date;

    private String contest_room;
    private String posi_room_id;
    private String nega_room_id;
    private com.example.bigtalk.bigtalk.bean.Topics topic;
    private String mode;
    private User mvp;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getContest_date() {
        return contest_date;
    }

    public void setContest_date(String contest_date) {
        this.contest_date = contest_date;
    }

    public String getContest_room() {
        return contest_room;
    }

    public void setContest_room(String contest_room) {
        this.contest_room = contest_room;
    }

    public String getPosi_room_id() {
        return posi_room_id;
    }

    public void setPosi_room_id(String posi_room_id) {
        this.posi_room_id = posi_room_id;
    }

    public String getNega_room_id() {
        return nega_room_id;
    }

    public void setNega_room_id(String nega_room_id) {
        this.nega_room_id = nega_room_id;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    public String getContest_result() {
        return contest_result;
    }

    public void setContest_result(String contest_result) {
        this.contest_result = contest_result;
    }

    public String getContest_time() {
        return contest_time;
    }

    public void setContest_time(String contest_time) {
        this.contest_time = contest_time;
    }

    public User getMvp() {
        return mvp;
    }

    public void setMvp(User mvp) {
        this.mvp = mvp;
    }

    public com.example.bigtalk.bigtalk.bean.Topics getTopic() {
        return topic;
    }

    public void setTopic(com.example.bigtalk.bigtalk.bean.Topics topic) {
        this.topic = topic;
    }


}
