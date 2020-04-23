package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Yesq on 2018/3/2.
 */

public class Follow extends BmobObject {
    private User user;
    private User follow;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollow() {
        return follow;
    }

    public void setFollow(User follow) {
        this.follow = follow;
    }



}
