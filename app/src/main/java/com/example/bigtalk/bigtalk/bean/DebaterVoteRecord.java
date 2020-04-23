package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Yesq on 2018/4/6.
 */

public class DebaterVoteRecord extends BmobObject {
    private Contest contest;
    private User voter,debater;
    private String voterType;

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public User getDebater() {
        return debater;
    }

    public void setDebater(User debater) {
        this.debater = debater;
    }

    public String getVoterType() {
        return voterType;
    }

    public void setVoterType(String voterType) {
        this.voterType = voterType;
    }
}
