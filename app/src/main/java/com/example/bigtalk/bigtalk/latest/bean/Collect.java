package com.example.bigtalk.bigtalk.latest.bean;

import com.example.bigtalk.bigtalk.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2018/3/2.
 */

public class Collect extends BmobObject {
    private User user;
    private Article news;
    private String collect_type;
//    private Contest contest;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getNews() {
        return news;
    }

    public void setNews(Article news) {
        this.news = news;
    }

    public String getCollect_type() {
        return collect_type;
    }

    public void setCollect_type(String collect_type) {
        this.collect_type = collect_type;
    }

//    public Contest getContest() {
//        return contest;
//    }
//
//    public void setContest(Contest contest) {
//        this.contest = contest;
//    }


}
