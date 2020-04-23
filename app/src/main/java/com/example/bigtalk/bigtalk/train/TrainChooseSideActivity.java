package com.example.bigtalk.bigtalk.train;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

public class TrainChooseSideActivity extends AppCompatActivity {


    private String topicid,topicName,posiOpi,negaOpi;
    private String side;
    private String trainTopic,topicIntro;
    private TextView topic_name,posi_opi,nega_opi,topic_intro;
    private Button posi,nega,sure;
    private User user;
    private Topics topic;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_choose_side);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");


        Intent intent = getIntent();
        topicid = intent.getStringExtra("topic_id");
        topicName = intent.getStringExtra("topic_name");
        posiOpi = intent.getStringExtra("posi_opi");
        negaOpi = intent.getStringExtra("nega_opi");
        topicIntro = intent.getStringExtra("topic_intro");
        //System.out.print("TrainChoose: 收到了了topic_id是"+ topicid);

        BmobQuery<Topics> query = new BmobQuery<Topics>();
        if(topicid != null){
            query.getObject(topicid, new QueryListener<Topics>() {

                @Override
                public void done(Topics object, BmobException e) {
                    if(e==null){

                        topic = object;


                        //topicName = object.getTopic_name();
                        //posiOpi = object.getPosi_opi();
                        //negaOpi = object.getNega_opi();

                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());

                    }
                }

            });
        }


        topic_name = (TextView)findViewById(R.id.train_choose_side_topicname);
        posi_opi = (TextView)findViewById(R.id.train_choose_side_text_posiopi);
        nega_opi = (TextView)findViewById(R.id.train_choose_side_text_negaopi);
        topic_intro = (TextView)findViewById(R.id.train_choose_side_text_topicintro);

        posi = (Button)findViewById(R.id.train_choose_side_btn_posi);
        nega = (Button)findViewById(R.id.train_choose_side_btn_nega);
        sure = (Button)findViewById(R.id.train_choose_side_btn_sure);

        if(topicName != null){

            topic_name.setText(topicName);
        }
        if(posiOpi != null){
            posi_opi.setText(posiOpi);
        }
        if(negaOpi != null){
            nega_opi.setText(negaOpi);
        }

        topic_intro.setText("\"古语有言：没有规矩，不成方圆。从君君臣臣，到三纲五常，遵规循矩的思想紧随着中国人的生活，“从心所欲，不逾矩”，更成为了很多人所追求的目标。\n" +
                "\n" +
                "往事千年，史书展册，汗青之间，既有孔子、颜回般不逾矩的君子，亦有太白、嵇康式抒胸臆的豪客，共同谱写着中华文明的灿烂。其实不逾矩也好，抒胸臆也罢，都是一种人生的选择，何者更为可贵，犹待诸君详论。\"\n");
//        if(topicIntro != null){
//            topic_intro.setText(topicIntro);
//        }

        //user = BmobUser.getCurrentUser( User.class);

        posi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(side == null){
                    side = "posi";

                }else if(side.equals("nega")){
                    side = "posi";
                    nega.setBackground(getResources().getDrawable(R.drawable.button_style_full));
                    nega.setClickable(true);
                }
                posi.setBackground(getResources().getDrawable(R.drawable.button_style_full_press));
                trainTopic = posiOpi;

            }
        });

        nega.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(side == null){
                    side = "nega";

                }else if(side.equals("posi")){
                    side = "nega";
                    posi.setBackground(getResources().getDrawable(R.drawable.button_style_full));
                    posi.setClickable(true);
                }
                nega.setBackground(getResources().getDrawable(R.drawable.button_style_full_press));

                trainTopic = negaOpi;

            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                sure.setBackground(getResources().getDrawable(R.drawable.button_style_full_press));
                //posi.setTextColor();
                Train train = new Train();
                train.setTopic(topic);
                train.setTrainee(user);
                train.setSide(side);
                train.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            Intent intent = new Intent(TrainChooseSideActivity.this, TrainActivity.class);

                            //intent.putExtra("topic_id",topicid);
                            intent.putExtra("user_topic",trainTopic);
                            intent.putExtra("train_id",objectId);

                            startActivity(intent);
                        }else{
                            Log.i("bmob","TrainChooseSideActivity失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }
}
