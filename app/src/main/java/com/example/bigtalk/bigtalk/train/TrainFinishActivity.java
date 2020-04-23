package com.example.bigtalk.bigtalk.train;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Train;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.FrontFragment;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class TrainFinishActivity extends AppCompatActivity {

    TextView scoreText, speedText,nonsenseText, sampleText;
    ImageView sampleImg;
    Button finish;
    String trainid;
    Train train;
    float grade;
    String speed,nonsense;
    boolean isSample;

    Fragment tb,trainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_finish);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        this.hideActionBar();
        this.setFullScreen();

        scoreText = (TextView) findViewById(R.id.train_finish_text_score);
        speedText = (TextView) findViewById(R.id.train_finish_text_speed);
        nonsenseText = (TextView) findViewById(R.id.train_finish_text_nonsense);
        sampleText =  (TextView) findViewById(R.id.train_finish_text_sample);
        sampleImg = (ImageView)findViewById(R.id.train_finish_img_sample);

        finish = (Button) findViewById(R.id.train_finish_btn_finish);

        Intent intent = getIntent();
        trainid = intent.getStringExtra("train_id");

        BmobQuery<Train> query = new BmobQuery<Train>();
        query.getObject(trainid, new QueryListener<Train>() {

            @Override
            public void done(Train object, BmobException e) {
                if (e == null) {
                    //获得playerName的信息
                    train = object;
                } else {
                    Log.i("bmob", "TrainFinishActivity：查询train失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

        if (train != null && train.getScore() != null) {
            scoreText.setText(train.getScore());
        }

        //------------------------------------change------------
        grade = (float) (50 + Math.random()*15);
        scoreText.setText(String.valueOf(grade));


        //test
        speed = "处于";

        if (speed.length() != 0 ) {
            //speed取值：快于，慢于，处于
            String s = "语速"+ speed + "正常水平";
            speedText.setText(s);
        }

        //test
        nonsense = "较多";
        //nonsense取值："较少"，"较多"
        if (nonsense.length() != 0) {
            String n = nonsense + "无意义词汇";
            nonsenseText.setText(n);

        }

        //test
        isSample = true;

        if (isSample) {
            sampleText.setText("恭喜你！你的答案已纳入训练优秀样本库！");
        }else{
            sampleImg.setVisibility(View.INVISIBLE);
            sampleText.setVisibility(View.INVISIBLE);
        }


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainFinishActivity.this, ExhibitSampleActivity.class);
                intent.putExtra("train_id",trainid);
                startActivity(intent);
            }
        });
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }


    private void setFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


}
