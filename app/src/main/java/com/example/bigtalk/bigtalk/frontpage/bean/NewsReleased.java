package com.example.bigtalk.bigtalk.frontpage.bean;

import com.example.bigtalk.bigtalk.latest.bean.Article;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class NewsReleased  extends BmobObject {
    private BmobRelation user;
    private Article news;

    public BmobRelation getUser() {
        return user;
    }

    public void setUser(BmobRelation user) {
        this.user = user;
    }

    public Article getNews() {
        return news;
    }

    public void setNews(Article news) {
        this.news = news;
    }



}

