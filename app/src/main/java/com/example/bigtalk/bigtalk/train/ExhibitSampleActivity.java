package com.example.bigtalk.bigtalk.train;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.Train;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class ExhibitSampleActivity extends AppCompatActivity {


    String title;
    Toolbar toolbar;
    SeekBar seekBar;
    Button playButton,back;
    BmobFile answer;
    String savepath;
    String trainid,contestid;
    File saveFile;
    public boolean isSelected = false;
    private Context context;

    private MediaPlayer mediaPlayer;//媒体播放器
    private boolean isCellPlay;/*在挂断电话的时候，用于判断是否为是来电时中断*/
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private int initPosition;//当前音乐播放的进度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_sample);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");


        Intent intent = getIntent();
        trainid = intent.getStringExtra("train_id");

        toolbar = (Toolbar) findViewById(R.id.exhibit_sample_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        //实例化媒体播放器
        mediaPlayer = MediaPlayer.create(ExhibitSampleActivity.this, R.raw.m_for);//创建mediaplayer对象

        back = (Button)findViewById(R.id.exhibit_sample_btn_back);
        playButton = (Button)findViewById(R.id.exhibit_sample_btn_playbutton);
        seekBar = (SeekBar)findViewById(R.id.exhibit_sample_playSeekBar);

        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        playButton.setOnClickListener(new PlayListener());

        TelephonyManager phoneyMana = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneyMana.listen(new myPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);


        if(isSelected){

            playButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.answer_pause));

        }else{

            playButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.answer_start));

        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(ExhibitSampleActivity.this,FrameActivity.class);
                show.putExtra("go_train",1);
                startActivity(show);
                finish();
            }
        });



        query();
    }

    public void query(){

        BmobQuery<Train> query = new BmobQuery<Train>();
        query.getObject(trainid, new QueryListener<Train>() {

            @Override
            public void done(Train object, BmobException e) {
                if(e==null){

                    answer = object.getTopic().getExample_answer();
                    savepath = Environment.getExternalStorageDirectory()+"/SampleAnswer/"+object.getObjectId();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        if(answer != null && savepath != null){
            saveFile = new File(savepath, answer.getFilename());
            answer.download(saveFile, new DownloadFileListener() {

                @Override
                public void onStart() {

                }

                @Override
                public void done(String savePath,BmobException e) {
                    if(e==null){
                        Log.i("bmob","保存优秀答案音频文件路径："+savePath);
                    }else{

                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    Log.i("bmob","下载进度："+value+","+newworkSpeed);
                }

            });

        }

    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置

        if (toolbar != null) {
            title = "优秀答案";
            toolbar.setTitle(title);
        }



    }

    /*销毁时释资源*/
    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }


    /*播放或暂停事件处理*/
    private class PlayListener implements View.OnClickListener {
        public void onClick(View v) {

            if(isSelected){

                isSelected = false;

                currentPosition = mediaPlayer.getCurrentPosition();//记录播放的位置
                mediaPlayer.stop();//暂停状态
                //playButton.setBackgroundResource(R.drawable.answer_start);
                //playButton.setText("暂停");
                playButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.answer_start));

            }else{
                isSelected = true;
                playButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.answer_pause));

                play();

            }
        }
    }



    /*播放处理*/
    private void play() {
        Message message = new Message();
        message.what = 1;

        try {

            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
            //mediaPlayer.setDataSource(savepath) ;
            //mediaPlayer.prepareAsync() ;
            mediaPlayer = MediaPlayer.create(ExhibitSampleActivity.this,R.raw.btest);//创建mediaplayer对象
            mediaPlayer.setLooping(false);
//                mediaPlayer.create(this, R.raw.test);//设置mp3数据源
            mediaPlayer.start();
            mediaPlayer.seekTo(currentPosition);
            initPosition = mediaPlayer.getCurrentPosition();

            Log.i("Mediaplayer", "最开始的播放点是" + initPosition + "   位置");
//            playButton.setText("播放");
            playButton.setBackgroundResource(R.drawable.answer_start);
            seekBar.setMax(mediaPlayer.getDuration());
//                mediaPlayer.prepareAsync();//数据缓冲
//                /*监听缓存 事件，在缓冲完毕后，开始播放*/
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    public void onPrepared(MediaPlayer mp) {
//                        mp.start();
//                        mp.seekTo(currentPosition);
//                        playButton.setText("播放");
//                        seekBar.setMax(mediaPlayer.getDuration());
//                    }
//                });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "播放错误", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println(e);
        }


    }

    /*来电事件处理*/
    private class myPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://来电，应当停止音乐
                    if (mediaPlayer.isPlaying() && playButton.getText().toString().equals("播放")) {
                        currentPosition = mediaPlayer.getCurrentPosition();//记录播放的位置
                        mediaPlayer.stop();
                        isCellPlay = true;//标记这是属于来电时暂停的标记
                        playButton.setBackgroundResource(R.drawable.answer_pause);
//                        playButton.setText("暂停");

                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE://无电话状态
                    if (isCellPlay) {
                        isCellPlay = false;
                        mediaPlayer.reset();
                        play();
                    }
                    break;
            }
        }
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }






    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub

        //android.R.id.home对应应用程序图标的id
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
