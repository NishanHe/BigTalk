package com.example.bigtalk.bigtalk.contest.vote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ExpertVoteActivity extends AppCompatActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_contestTime)
    TextView tvContestTime;
    @Bind(R.id.posi_shenti)
    SeekBar posiShenti;
    @Bind(R.id.posi_lunzheng)
    SeekBar posiLunzheng;
    @Bind(R.id.posi_bianbo)
    SeekBar posiBianbo;
    @Bind(R.id.posi_peihe)
    SeekBar posiPeihe;
    @Bind(R.id.posi_bianfeng)
    SeekBar posiBianfeng;
    @Bind(R.id.nega_shenti)
    SeekBar negaShenti;
    @Bind(R.id.nega_lunzheng)
    SeekBar negaLunzheng;
    @Bind(R.id.nega_bianbo)
    SeekBar negaBianbo;
    @Bind(R.id.nega_peihe)
    SeekBar negaPeihe;
    @Bind(R.id.nega_bianfeng)
    SeekBar negaBianfeng;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    String contestID;
    List<Contest> contestInfo;
    Contest contest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_vote);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        Intent intent = getIntent();
        contestID = intent.getStringExtra("contestIdSent");

        initData();
    }

    public void initData(){
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
//                        initContestData(contestInfo);

                    } else {
                        Toast.makeText(ExpertVoteActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ExpertVoteActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });
    }
}
