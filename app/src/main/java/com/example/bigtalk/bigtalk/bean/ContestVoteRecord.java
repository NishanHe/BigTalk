package com.example.bigtalk.bigtalk.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Yesq on 2018/4/7.
 */

public class ContestVoteRecord extends BmobObject {
    private  Contest contest;
    private User voter;
    private String voterType;
    private String choice;

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

    public String getVoterType() {
        return voterType;
    }

    public void setVoterType(String voterType) {
        this.voterType = voterType;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
