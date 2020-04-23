package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Yesq on 2018/5/5.
 */

public class ContestPosiUser extends BmobObject {
    private String contest_id;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private String position_user1;
    private String position_user2;
    private String position_user3;
    private String position_user4;

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getUser3() {
        return user3;
    }

    public void setUser3(User user3) {
        this.user3 = user3;
    }

    public User getUser4() {
        return user4;
    }

    public void setUser4(User user4) {
        this.user4 = user4;
    }

    public String getPosition_user1() {
        return position_user1;
    }

    public void setPosition_user1(String position_user1) {
        this.position_user1 = position_user1;
    }

    public String getPosition_user2() {
        return position_user2;
    }

    public void setPosition_user2(String position_user2) {
        this.position_user2 = position_user2;
    }

    public String getPosition_user3() {
        return position_user3;
    }

    public void setPosition_user3(String position_user3) {
        this.position_user3 = position_user3;
    }

    public String getPosition_user4() {
        return position_user4;
    }

    public void setPosition_user4(String position_user4) {
        this.position_user4 = position_user4;
    }
}
