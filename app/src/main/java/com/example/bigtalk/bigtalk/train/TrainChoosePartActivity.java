package com.example.bigtalk.bigtalk.train;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.bean.Train;
import com.example.bigtalk.bigtalk.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class TrainChoosePartActivity extends AppCompatActivity {

    private String topicName,trainid;
    private String part;
    private TextView topic_name;
    private Button start,end,sure;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_choose_part);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        Intent intent = getIntent();
        topicName = intent.getStringExtra("user_topic");
        trainid  = intent.getStringExtra("train_id");



        topic_name = (TextView)findViewById(R.id.train_choose_part_topicname);

        start = (Button)findViewById(R.id.train_choose_part_btn_start);
        end = (Button)findViewById(R.id.train_choose_part_btn_end);
        sure = (Button)findViewById(R.id.train_choose_part_btn_sure);

        if(topicName.length() != 0){
            topic_name.setText(topicName);
        }


        start.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(part == null){
                    part = "start";

                }else if(part.equals("end")){
                    part = "start";
                    end.setBackground(getResources().getDrawable(R.drawable.button_style_empty));
                    end.setTextColor(getResources().getColor(R.color.colorPrimary));
                    end.setClickable(true);
                }
                start.setTextColor(getResources().getColor(R.color.white));
                start.setBackground(getResources().getDrawable(R.drawable.button_style_full));

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                if(part == null){
                    part = "end";

                }else if(part.equals("start")){
                    part = "end";
                    start.setBackground(getResources().getDrawable(R.drawable.button_style_empty));
                    start.setTextColor(getResources().getColor(R.color.colorPrimary));
                    start.setClickable(true);
                }
                end.setTextColor(getResources().getColor(R.color.white));
                end.setBackground(getResources().getDrawable(R.drawable.button_style_full));

            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                sure.setTextColor(getResources().getColor(R.color.colorPrimary));
                //posi.setTextColor();
                Train train = new Train();
                if(part != null){
                    train.setPart(part);
                    if(trainid != null){
                        train.update(trainid, new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Intent intent = new Intent(TrainChoosePartActivity.this, TrainActivity.class);

                                    //intent.putExtra("topic_id",topicid);
                                    intent.putExtra("user_topic",topicName);
                                    intent.putExtra("user_part",part);
                                    intent.putExtra("train_id",trainid);

                                    startActivity(intent);

                                }else{
                                    Log.i("bmob","TrainChoosePartActivity失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });
                    }
                }



            }
        });
    }
}
