package com.example.bigtalk.bigtalk.contest;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.latest.ContestReportActivity;
import com.example.bigtalk.bigtalk.latest.TextContestActivity;
import com.tencent.rtmp.TXLiveBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PlayContestActivity extends AppCompatActivity {

    @Bind(R.id.iv_current)
    ImageView ivCurrent;
    @Bind(R.id.iv_current2)
    ImageView ivCurrent2;
    @Bind(R.id.imageView3)
    ImageView ivPosi1;
    @Bind(R.id.imageView4)
    ImageView ivNega1;
    @Bind(R.id.imageView5)
    ImageView ivPosi2;
    @Bind(R.id.imageView6)
    ImageView ivNega2;
    @Bind(R.id.imageView7)
    ImageView ivPosi3;
    @Bind(R.id.imageView8)
    ImageView ivNega3;
    @Bind(R.id.imageView9)
    ImageView ivPosi4;
    @Bind(R.id.imageView10)
    ImageView ivNega4;
    @Bind(R.id.tv_countDown)
    TextView tvCountDown;
    @Bind(R.id.tv_posi)
    TextView tvPosi;
    @Bind(R.id.tv_nega)
    TextView tvNega;

//    @Bind(R.id.tv_title)
//    TextView tvTitle;
    @Bind(R.id.playSeekBar)
    SeekBar seekBar;
    @Bind(R.id.playButton)
    Button playButton;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_mvp)
    TextView tvMvp;
    @Bind(R.id.tv_time)
    TextView tvTime;


    private String contestID, timeStart, timeStartplus,contest_id;
    List<Contest> contestInfo;
    Date dateStart;
    String roomID;


    //contestRoom是当前房间的房间名
    CountDownTimer speak_timer, free_debate_timer, close_debate_timer, close_debate_timer_posi4;
    CountDownTimer speak_timer_nega41, speak_timer_nega1, speak_timer_posi14, speak_timer_posi2,
            speak_timer_nega32, speak_timer_nega2, speak_timer_posi32, speak_timer_nega3, speak_timer_posi3;
    Handler start_handler = new Handler();

    private MediaPlayer mediaPlayer;//媒体播放器
    private boolean isCellPlay;/*在挂断电话的时候，用于判断是否为是来电时中断*/
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private int initPosition;//当前音乐播放的进度

    private Timer timer;
    Toolbar toolbar;
    String title;
    Button report,textDebate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_contest);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        //String sdkver = TXLiveBase.getSDKVersionStr();
        //Log.d("liteavsdk", "!!!!!!体到未liteav sdk version is : " + sdkver);

        //实例化媒体播放器
        mediaPlayer = MediaPlayer.create(PlayContestActivity.this, R.raw.btest);//创建mediaplayer对象


        //获得要进入的比赛的id和当前使用者的身份（观众/辩手）
        Intent i = getIntent();
        contestID = i.getStringExtra("contestIdSent");
        contest_id = i.getStringExtra("contest_id");

        //测试用
        contestID = "05";

        toolbar = (Toolbar) findViewById(R.id.play_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        textDebate = (Button)findViewById(R.id.play_btn_text_debate);
        report = (Button)findViewById(R.id.play_btn_report);

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        contestInfo = new ArrayList<>();

        //根据contestid查询比赛的相关信息：辩题、辩手信息、正反方聊天室id、当前比赛的房间，比赛时间点
        BmobQuery<Contest> query = new BmobQuery<>();
        query.addWhereEqualTo("contest_id", contestID);
        query.include("topic,posi_1,posi_2,posi_3,posi_4,nega_1,nega_2,nega_3,nega_4,mvp");
        query.setLimit(500);
        query.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            contestInfo.add(list.get(i));
                            Log.i("bmob", "房间id" + contestInfo.get(i).getContest_id());
                            Log.i("bmob", "房间名" + contestInfo.get(i).getContest_room());
                        }
                        initContestData(contestInfo);

                    } else {
                        //Toast.makeText(PlayContestActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(PlayContestActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlayContestActivity.this,ContestReportActivity.class);
                intent.putExtra("contest_id",contest_id);
                startActivity(intent);
            }
        });

        textDebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayContestActivity.this,TextContestActivity.class);
                intent.putExtra("contest_id",contest_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            title = "短视频的火爆是精神文化的丰富还是匮乏的体现";
            toolbar.setTitle(title);
        }
    }

    public void initContestData(List<Contest> contestInfo) {

        if (contestInfo.size() > 0) {
            //取出比赛房间的id
            roomID = contestInfo.get(0).getContest_room();

            //取出比赛时间，将time_start转换成时间类型
            timeStart = contestInfo.get(0).getContest_time();
            timeStartplus = contestInfo.get(0).getContest_date();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateStart = formatter.parse(timeStartplus + " " + timeStart);
                Log.i("bmob", "dateStart:    " + dateStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Topics currentTopic = contestInfo.get(0).getTopic();
//            tvTitle.setText(currentTopic.getTopic_name());
            tvPosi.setText(currentTopic.getPosi_opi());
            tvNega.setText(currentTopic.getNega_opi());

            String time = timeStartplus+" "+timeStart;
            tvTime.setText(time);

            //测试用
            //String mvp = "最佳辩手："+contestInfo.get(0).getMvp().getUsername();
            String mvp = "最佳辩手："+"Carbon";
            tvMvp.setText(mvp);
            //String result = "比赛结果："+contestInfo.get(0).getContest_report().getContest_result();
            String result = "比赛结果："+"正方胜";
            tvResult.setText(result);




        } else {
            Log.i("bmob", "size并没有大于0");
        }

        startContest();

        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        playButton.setOnClickListener(new PalyListener());


        TelephonyManager phoneyMana = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneyMana.listen(new myPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);


    }

    public void startContest() {

        start_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        speak_timer.start();
                        ivPosi1.setVisibility(View.INVISIBLE);

                        ivCurrent.setImageResource(R.drawable.pcir1);
                        ivCurrent.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(PlayContestActivity.this, "正方一辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Log.i("start_handler", "第一次handler开始！！！！");
                        break;
                    case 2:
                        ivPosi1.setVisibility(View.INVISIBLE);
                        ivNega4.setVisibility(View.INVISIBLE);
                        ivCurrent.setImageResource(R.drawable.pcir1);
                        ivCurrent2.setImageResource(R.drawable.pcir8);
                        ivCurrent.setVisibility(View.VISIBLE);
                        ivCurrent2.setVisibility(View.VISIBLE);
                        Toast toast1 = Toast.makeText(PlayContestActivity.this, "请反方四辩开始对正方一辩的质询，时间为3分钟", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.CENTER, 0, 0);
                        toast1.show();
                        Log.i("speak", "反四质询正一");
                        speak_timer_nega41.start();
                        break;
//                    case 3:
//                        ivPosi1.setVisibility(View.VISIBLE);
//                        ivNega4.setVisibility(View.VISIBLE);
//                        ivNega1.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir5);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        Toast toast2 = Toast.makeText(PlayContestActivity.this, "请反方一辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
//                        toast2.setGravity(Gravity.CENTER, 0, 0);
//                        toast2.show();
//                        Log.i("speak", "反方一辩开始陈词");
//                        speak_timer_nega1.start();
//                        break;
//                    case 4:
//                        ivNega1.setVisibility(View.INVISIBLE);
//                        ivPosi4.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir5);
//                        ivCurrent2.setImageResource(R.drawable.pcir4);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        ivCurrent2.setVisibility(View.VISIBLE);
//                        Toast toast3 = Toast.makeText(PlayContestActivity.this, "请正方四辩开始对反方一辩的质询，时间为3分钟", Toast.LENGTH_LONG);
//                        toast3.setGravity(Gravity.CENTER, 0, 0);
//                        toast3.show();
//                        Log.i("speak", "请正方四辩开始对反方一辩的质询");
//                        speak_timer_posi14.start();
//                        break;
//                    case 5:
//                        ivNega1.setVisibility(View.VISIBLE);
//                        ivPosi4.setVisibility(View.VISIBLE);
//                        ivPosi2.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir2);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        Toast toast4 = Toast.makeText(PlayContestActivity.this, "请正方二辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
//                        toast4.setGravity(Gravity.CENTER, 0, 0);
//                        toast4.show();
//                        Log.i("speak", "请正方二辩开始陈词");
//                        speak_timer_posi2.start();
//                        break;
//                    case 6:
//                        ivPosi2.setVisibility(View.INVISIBLE);
//                        ivNega3.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir2);
//                        ivCurrent2.setImageResource(R.drawable.pcir7);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        ivCurrent2.setVisibility(View.VISIBLE);
//                        Toast toast5 = Toast.makeText(PlayContestActivity.this, "请反方三辩开始质询正方二辩，时间为3分钟", Toast.LENGTH_LONG);
//                        toast5.setGravity(Gravity.CENTER, 0, 0);
//                        toast5.show();
//                        Log.i("speak", "请反方三辩开始质询正方二辩");
//                        speak_timer_nega32.start();
//                        break;
//                    case 7:
//                        ivPosi2.setVisibility(View.VISIBLE);
//                        ivNega3.setVisibility(View.VISIBLE);
//                        ivNega2.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir6);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        Toast toast6 = Toast.makeText(PlayContestActivity.this, "请反方二辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
//                        toast6.setGravity(Gravity.CENTER, 0, 0);
//                        toast6.show();
//                        Log.i("speak", "请反方二辩开始陈词，时间为3分钟");
//                        speak_timer_nega2.start();
//                        break;
//                    case 8:
//                        ivNega2.setVisibility(View.INVISIBLE);
//                        ivPosi3.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir6);
//                        ivCurrent2.setImageResource(R.drawable.pcir3);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        ivCurrent2.setVisibility(View.VISIBLE);
//                        Toast toast7 = Toast.makeText(PlayContestActivity.this, "请正方三辩开始对反方二辩的质询，时间为3分钟", Toast.LENGTH_LONG);
//                        toast7.setGravity(Gravity.CENTER, 0, 0);
//                        toast7.show();
//                        Log.i("speak", "请正方三辩开始对反方二辩的质询");
//                        speak_timer_posi32.start();
//                        break;
//                    case 9:
//                        ivNega2.setVisibility(View.VISIBLE);
//                        ivPosi3.setVisibility(View.VISIBLE);
//                        ivNega3.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir7);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        Toast toast8 = Toast.makeText(PlayContestActivity.this, "请反方三辩开始质询小结，时间为3分钟", Toast.LENGTH_LONG);
//                        toast8.setGravity(Gravity.CENTER, 0, 0);
//                        toast8.show();
//                        Log.i("speak", "请反方三辩开始质询小结，时间为3分钟");
//                        speak_timer_nega3.start();
//                        break;
//                    case 10:
//                        ivNega3.setVisibility(View.VISIBLE);
//                        ivPosi3.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir3);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        Toast toast9 = Toast.makeText(PlayContestActivity.this, "请正方三辩开始质询小结，时间为3分钟", Toast.LENGTH_LONG);
//                        toast9.setGravity(Gravity.CENTER, 0, 0);
//                        toast9.show();
//                        Log.i("speak", "请正方三辩开始质询小结");
//                        speak_timer_posi3.start();
//                        break;
//                    case 11:
//                        ivPosi3.setVisibility(View.VISIBLE);
//                        Toast toast10 = Toast.makeText(PlayContestActivity.this, "请双方开始自由辩论，总时长为8分钟", Toast.LENGTH_LONG);
//                        toast10.setGravity(Gravity.CENTER, 0, 0);
//                        toast10.show();
//                        ivPosi1.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivPosi2.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivPosi3.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivPosi4.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivNega1.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivNega2.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivNega3.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        ivNega4.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//                        free_debate_timer.start();
//                        break;
//                    case 12:
//                        Toast toast11 = Toast.makeText(PlayContestActivity.this, "请反方四辩开始结辩，时间为4分钟", Toast.LENGTH_LONG);
//                        toast11.setGravity(Gravity.CENTER, 0, 0);
//                        toast11.show();
//                        ivNega4.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir8);
//                        ivCurrent.setVisibility(View.VISIBLE);
//
//                        close_debate_timer.start();
//                        break;
//                    case 13:
//                        Toast toast12 = Toast.makeText(PlayContestActivity.this, "请正方四辩开始结辩，时间为4分钟", Toast.LENGTH_LONG);
//                        toast12.setGravity(Gravity.CENTER, 0, 0);
//                        toast12.show();
//                        ivNega4.setVisibility(View.VISIBLE);
//                        ivPosi4.setVisibility(View.INVISIBLE);
//                        ivCurrent.setImageResource(R.drawable.pcir4);
//                        ivCurrent.setVisibility(View.VISIBLE);
//                        close_debate_timer_posi4.start();
//                        break;
                }
            }
        };

        //1000是指每隔1000毫秒就调用一次onTick方法,这个countdowntimer主要是用来计时发言时间
        //speak_timer = new CountDownTimer(180000, 1000) {
        speak_timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date remain = new Date(millisUntilFinished);
                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
                tvCountDown.setText(timeRemain.substring(3));
            }

            @Override
            public void onFinish() {
                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");

//                Message message = new Message();
//                message.what = 2;
//                start_handler.sendMessage(message);
                ivCurrent.setVisibility(View.INVISIBLE);
//                worker().getRtcEngine().muteLocalAudioStream(false);
//                mRtcEngine.muteLocalAudioStream(true);
//                btnMic.setVisibility(View.GONE);
//                cleanAllFilter();
//                speakInOrder(hold_debater);

            }
        };

//
//        speak_timer_nega41 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 3;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };

//        speak_timer_nega1 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 4;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//        speak_timer_posi14 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 5;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//        speak_timer_posi2 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 6;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//        speak_timer_nega32 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 7;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//        speak_timer_nega2 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 8;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//        speak_timer_posi32 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 9;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//
//        speak_timer_nega3 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 10;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//        speak_timer_posi3 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
//
//                Message message = new Message();
//                message.what = 11;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
////                worker().getRtcEngine().muteLocalAudioStream(false);
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//
//
//        //1000是指每隔1000毫秒就调用一次onTick方法,这个countdowntimer主要是用来计时自由辩论发言时间
//        // free_debate_timer = new CountDownTimer(480000, 1000) {
//        free_debate_timer = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//
//                Message message = new Message();
//                message.what = 12;
//                start_handler.sendMessage(message);
//                Log.i("free_debate_timer", "free_debate_timer准备给handler发送消息！！！！");
////                mRtcEngine.muteLocalAudioStream(true);
////                btnMic.setVisibility(View.GONE);
////                cleanAllFilter();
////                speakInOrder(hold_debater);
//
//            }
//        };
//
//        //1000是指每隔1000毫秒就调用一次onTick方法,这个countdowntimer主要是用来计时结辩发言时间
//        //close_debate_timer = new CountDownTimer(240000, 1000) {
//        close_debate_timer = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                Message message = new Message();
//                message.what = 13;
//                start_handler.sendMessage(message);
//                ivCurrent.setVisibility(View.INVISIBLE);
//                Log.i("close_debate_timer", "close_debate_timer准备给handler发送消息！！！！");
//
//
//            }
//        };
//
//        close_debate_timer_posi4 = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Date remain = new Date(millisUntilFinished);
//                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
//                tvCountDown.setText(timeRemain.substring(3));
//            }
//
//            @Override
//            public void onFinish() {
//                speak_timer.cancel();
//                speak_timer_posi3.cancel();
//                speak_timer_posi32.cancel();
//                speak_timer_nega32.cancel();
//                speak_timer_nega3.cancel();
//                speak_timer_nega1.cancel();
//                speak_timer_nega2.cancel();
//                speak_timer_nega41.cancel();
//                speak_timer_posi2.cancel();
//                speak_timer_posi14.cancel();
//
//                free_debate_timer.cancel();
//                close_debate_timer.cancel();
//                Toast toast = Toast.makeText(PlayContestActivity.this, "比赛结束", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                ivCurrent.setVisibility(View.INVISIBLE);
//                ivPosi4.setVisibility(View.VISIBLE);
//            }
//        };


    }


    public void cleanAllFilter() {
        ivPosi1.clearColorFilter();
        ivPosi2.clearColorFilter();
        ivPosi3.clearColorFilter();
        ivPosi4.clearColorFilter();
        ivNega1.clearColorFilter();
        ivNega2.clearColorFilter();
        ivNega3.clearColorFilter();
        ivNega4.clearColorFilter();
    }


    /*销毁时释资源*/
    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        timer.cancel();
        timer = null;
        mediaPlayer = null;
        super.onDestroy();
    }


    /*播放或暂停事件处理*/
    private class PalyListener implements View.OnClickListener {
        public void onClick(View v) {
            if (playButton.getText().toString().equals("播放")) {


                currentPosition = mediaPlayer.getCurrentPosition();//记录播放的位置
                mediaPlayer.stop();//暂停状态
                playButton.setText("暂停");
                timer.purge();//移除所有任务;
            } else {

                play();
            }
        }
    }


    /*播放处理*/
    private void play() {
        Message message = new Message();
        message.what = 1;
        start_handler.sendMessage(message);

        try {

            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
            mediaPlayer = MediaPlayer.create(PlayContestActivity.this, R.raw.btest);//创建mediaplayer对象
            mediaPlayer.setLooping(false);

//                mediaPlayer.create(this, R.raw.test);//设置mp3数据源
            mediaPlayer.start();
            mediaPlayer.seekTo(currentPosition);
            initPosition = mediaPlayer.getCurrentPosition();

            Log.i("Mediaplayer", "最开始的播放点是" + initPosition + "   位置");
            playButton.setText("播放");
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
            //监听播放时回调函数
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSeekBarChanging) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            }, 0, 50);
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
                        playButton.setText("暂停");
                        timer.purge();//移除定时器任务;
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
