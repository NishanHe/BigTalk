package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;


public class Contest extends BmobObject {
    private String contest_id;



    private ContestReport contest_report;
    private String contest_time;
    private String contest_date;

    private String contest_room;
    private String posi_room_id;
    private String nega_room_id;
    private Topics topic;
    private String mode;
    private User mvp;
    private User posi_1;
    private User posi_2;
    private User posi_3;
    private User posi_4;
    private User nega_1;
    private User nega_2;
    private User nega_3;
    private User nega_4;



    private String contest_content_zh;

    public User getPosi_1() {
        return posi_1;
    }

    public void setPosi_1(User posi_1) {
        this.posi_1 = posi_1;
    }

    public User getPosi_2() {
        return posi_2;
    }

    public void setPosi_2(User posi_2) {
        this.posi_2 = posi_2;
    }

    public User getPosi_3() {
        return posi_3;
    }

    public void setPosi_3(User posi_3) {
        this.posi_3 = posi_3;
    }

    public User getPosi_4() {
        return posi_4;
    }

    public void setPosi_4(User posi_4) {
        this.posi_4 = posi_4;
    }

    public User getNega_1() {
        return nega_1;
    }

    public void setNega_1(User nega_1) {
        this.nega_1 = nega_1;
    }

    public User getNega_2() {
        return nega_2;
    }

    public void setNega_2(User nega_2) {
        this.nega_2 = nega_2;
    }

    public User getNega_3() {
        return nega_3;
    }

    public void setNega_3(User nega_3) {
        this.nega_3 = nega_3;
    }

    public User getNega_4() {
        return nega_4;
    }

    public void setNega_4(User nega_4) {
        this.nega_4 = nega_4;
    }


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

    public Topics getTopic() {
        return topic;
    }

    public void setTopic(Topics topic) {
        this.topic = topic;
    }

    public ContestReport getContest_report() {
        return contest_report;
    }

    public void setContest_report(ContestReport contest_report) {
        this.contest_report = contest_report;
    }

    public String getContest_content_zh() {
        return contest_content_zh;
    }

    public void setContest_content_zh(String contest_content_zh) {
        this.contest_content_zh = contest_content_zh;
    }
}
