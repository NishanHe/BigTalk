package com.example.bigtalk.bigtalk.contest.vote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.ContestVoteRecord;
import com.example.bigtalk.bigtalk.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AudienceVoteActivity extends AppCompatActivity {


    String contestID;
    List<Contest> contestInfo;
    Contest contest;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    TextView tvContestTime;
    @Bind(R.id.positive)
    ImageView positive;
    @Bind(R.id.negative)
    ImageView negative;

    User cUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience_vote);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        Intent intent = getIntent();
        contestID = intent.getStringExtra("contestIdSent");

        //测试用,是01比赛的nega_4
        contestID = "01";

//        cUser = BmobUser.getCurrentUser(User.class);
//测试用
        User testUser = new User();
        testUser.setObjectId("1dva555R");
        testUser.setUsername("test3");
        cUser = testUser;

        initData();

    }

    public void initData() {
        contestInfo = new ArrayList<>();

        //根据contestid查询辩手信息
        BmobQuery<Contest> query = new BmobQuery<>();
        query.addWhereEqualTo("contest_id", contestID);
        query.include("topic");
        query.setLimit(500);
        query.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            contestInfo.add(list.get(i));
                        }
                        initContestData(contestInfo);

                    } else {
                        Toast.makeText(AudienceVoteActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AudienceVoteActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContestVoteRecord voteUpdate = new ContestVoteRecord();
                voteUpdate.setVoter(cUser);
                voteUpdate.setContest(contest);
                voteUpdate.setChoice("正方");
                voteUpdate.setVoterType("观众");
                voteUpdate.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(AudienceVoteActivity.this,"Thanks for voting. Please continue to vote the best debater.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AudienceVoteActivity.this, ParticipantsVoteActivity.class);
                            intent.putExtra("contestIdSent",contestID);
                            intent.putExtra("voterType","观众");
                            startActivity(intent);


                        }else{
                            Toast.makeText(AudienceVoteActivity.this,"投票失败",Toast.LENGTH_SHORT).show();
                            Log.i("bmob", "添加失败:" + e.getMessage());
                        }

                    }
                });

            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContestVoteRecord voteUpdate = new ContestVoteRecord();
                voteUpdate.setVoter(cUser);
                voteUpdate.setContest(contest);
                voteUpdate.setChoice("反方");
                voteUpdate.setVoterType("观众");
                voteUpdate.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(AudienceVoteActivity.this,"Thanks for voting. Please continue to vote the best debater.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AudienceVoteActivity.this, ParticipantsVoteActivity.class);
                            intent.putExtra("contestIdSent",contestID);
                            intent.putExtra("voterType","观众");
                            startActivity(intent);

                        }else{
                            Toast.makeText(AudienceVoteActivity.this,"投票失败",Toast.LENGTH_SHORT).show();
                            Log.i("bmob", "添加失败:" + e.getMessage());
                        }

                    }
                });

            }
        });
    }

    public void initContestData(final List<Contest> contestInfo) {
        if (contestInfo.size() > 0) {
            tvTitle.setText(contestInfo.get(0).getTopic().getTopic_name());
            tvContestTime.setText(contestInfo.get(0).getContest_date() +"  "+ contestInfo.get(0).getContest_time());

            contest = new Contest();
            contest.setObjectId(contestInfo.get(0).getObjectId());


        } else {
            Log.i("bmob", "size并没有大于0");
        }



    }
}
