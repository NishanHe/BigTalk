package com.example.bigtalk.bigtalk.myspace.bean;

//import com.example.bigtalk.bigtalk.chat.bean.User;

import com.example.bigtalk.bigtalk.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2018/2/17.
 */

public class Fan extends BmobObject {

    private User user;
    private User fanUser;
//    private String user_id;
//    private String fan_id;

    public Fan(){}

    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getFan(){
        return fanUser;
    }
    public void setFan(User fanUser){
        this.fanUser = fanUser;
    }
}
