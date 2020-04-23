package com.example.bigtalk.bigtalk.train;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Train;
import com.example.bigtalk.bigtalk.tools.LoginActivity;
import com.example.bigtalk.bigtalk.train.tool.AudioRecoderUtils;
import com.example.bigtalk.bigtalk.train.tool.AudioRecorder;
import com.example.bigtalk.bigtalk.train.tool.TimeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

//import static com.example.bigtalk.bigtalk.train.tool.AudioRecoderUtils.BuildDev;

public class TrainActivity extends AppCompatActivity {

//    public static final int BuildDev.RECORD_AUDIO = 0;
    private String topicName,trainid,savepath;
    private TextView topicname,totalTime,recordTime,recordHint;
    private Button micro,finish;
    private AudioRecoderUtils mAudioRecoderUtils;
    //private AudioRecorder audioRecorder;
    private int recLen = 11;
    private TextView txtView;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");


        Intent intent = getIntent();
        topicName = intent.getStringExtra("user_topic");
        trainid = intent.getStringExtra("train_id");
        //userPart = intent.getStringExtra("user_part");


        topicname = (TextView) findViewById(R.id.train_text_topicname);
//        part = (TextView) findViewById(R.id.train_text_part);
        totalTime = (TextView) findViewById(R.id.train_text_totaltime);
        recordTime = (TextView) findViewById(R.id.train_label_time);
        recordHint = (TextView) findViewById(R.id.train_text_hint);

        micro = (Button) findViewById(R.id.train_btn_micro);
        finish = (Button) findViewById(R.id.train_btn_finish);


        if (topicName.length() != 0) {
            topicname.setText(topicName);
        }
//        if (userPart.length() != 0) {
//            if (userPart.equals("start")) {
//                part.setText("总起陈词");
//            } else if (userPart.equals("end")) {
//                part.setText("总起陈词");
//            }
//
//        }

        mAudioRecoderUtils = new AudioRecoderUtils();
        //audioRecorder = new AudioRecorder();
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
                //mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
                System.out.print("已录音时长"+time);
                String speakingtime = formatter.format(time);
                recordTime.setText(speakingtime);
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                //Toast.makeText(TrainActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                //savepath = savepath;
                savepath = filePath;
                final BmobFile bmobFile = new BmobFile(new File(filePath));
                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址

                            //Toast.makeText(TrainActivity.this, "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_LONG).show();
                        }else{
                            //Toast.makeText(TrainActivity.this,"上传文件失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                });

                Train train = new Train();
                train.setUserAnswer(bmobFile);
                if(trainid != null){
                    train.update(trainid, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("bmob","TrainActivity:上传用户训练音频成功");
                            }else{
                                Log.i("bmob","TrainActivity:上传用户训练音频失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }

            }
        });



        //倒计时，3分钟到了自动停止
         timer = new CountDownTimer(180000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mAudioRecoderUtils.stopRecord();
                //停止录音
                //audioRecorder.stopRecord();

                Intent intent = new Intent(TrainActivity.this, TrainFinishActivity.class);
                System.out.print("TrainActivity: 要跳转了！");
                intent.putExtra("train_id",trainid);

                startActivity(intent);
            }
        };

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recordHint.setVisibility(View.INVISIBLE);
                micro.setVisibility(View.INVISIBLE);
                finish.setVisibility(View.VISIBLE);
                String newTime = " / ".concat(getResources().getString(R.string.train_totaltime));
                totalTime.setText(newTime);

                mAudioRecoderUtils.startRecord();
//                savepath = Environment.getExternalStorageDirectory()+"/BigTalkRecord/"+trainid;
//                audioRecorder.startRecord(savepath);
                timer.start();
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioRecoderUtils.stopRecord();

                //Toast.makeText(TrainActivity.this, "音频文件保存路径"+savepath, Toast.LENGTH_LONG).show();

                //audioRecorder.stopRecord();
                Intent intent = new Intent(TrainActivity.this, TrainFinishActivity.class);
                System.out.print("TrainActivity: 要跳转了！");
                intent.putExtra("train_id",trainid);

                startActivity(intent);


            }
        });
    }

}
