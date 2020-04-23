package com.example.bigtalk.bigtalk.contest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.Topics;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserJoinContestSuccessActivity extends AppCompatActivity {

    @Bind(R.id.lv_contestinfo)
    ListView lvContestinfo;
    @Bind(R.id.success_back)
    Button goback;

    private String contestID;
    List<Contest> contestInfo;
    private ArrayAdapter arr_aAdapter;
    private ArrayList<String> contestlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_join_contest_success);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");


        Intent i = getIntent();
        contestID = i.getStringExtra("contestIdSent");

        contestInfo = new ArrayList<>();

        BmobQuery<Contest> query = new BmobQuery<Contest>();
        query.addWhereEqualTo("contest_id", contestID);
        query.include("topic");
        query.setLimit(50);
        query.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            contestInfo.add(list.get(i));
                            Log.i("bmob", "房间id" + contestInfo.get(i).getContest_id());
                            Log.i("bmob", "房间名" + contestInfo.get(i).getContest_room());


                            contestlist.add(contestInfo.get(0).getTopic().getTopic_name());
                            contestlist.add(contestInfo.get(0).getContest_date());
                            contestlist.add(contestInfo.get(0).getContest_time());

                            arr_aAdapter = new ArrayAdapter(UserJoinContestSuccessActivity.this,android.R.layout.simple_list_item_1, contestlist);
                            lvContestinfo.setAdapter(arr_aAdapter);

                        }
                    } else {
                        Toast.makeText(UserJoinContestSuccessActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserJoinContestSuccessActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserJoinContestSuccessActivity.this, FrameActivity.class);
                startActivity(intent);

            }
        });


    }


}
