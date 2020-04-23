package com.example.bigtalk.bigtalk.contest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Topics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TopicsIntroActivity extends AppCompatActivity {

    @Bind(R.id.lv_topicIntro)
    ListView lvTopicIntro;

    List<Topics> topicInfo;

    String topicID;
    private SimpleAdapter arr_aAdapter;

    private List<Map<String, String>> data = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_intro);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        Intent i = getIntent();
        topicID = i.getStringExtra("topicID");
        Log.i("bmob", "辩题id" + topicID);

        topicInfo = new ArrayList<>();


        final Map<String, String> map1 = new HashMap<String, String>();

        BmobQuery<Topics> query = new BmobQuery<>();
        query.addWhereEqualTo("topic_id", topicID);
        query.include("topic_author");
        query.setLimit(50);
        query.findObjects(new FindListener<Topics>() {
            @Override
            public void done(List<Topics> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            topicInfo.add(list.get(i));
                            Log.i("bmob", "辩题id" + topicInfo.get(i).getTopic_id());
                            Log.i("bmob", "作者" + topicInfo.get(i).getTopic_author().getUsername());
                        }
                        map1.put("title", topicInfo.get(0).getTopic_name());
                        map1.put("intro", topicInfo.get(0).getTopic_intro());
                        data.add(map1);

                        arr_aAdapter = new SimpleAdapter(TopicsIntroActivity.this,data,android.R.layout.simple_list_item_2,
                                new String[]{"title","intro"}, new int[]{android.R.id.text1,android.R.id.text2});
                        lvTopicIntro.setAdapter(arr_aAdapter);

                    } else {
                        Toast.makeText(TopicsIntroActivity.this, "没有查询到相关辩题信息", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TopicsIntroActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });





    }
}
