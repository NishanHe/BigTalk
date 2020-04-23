package com.example.bigtalk.bigtalk.contest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.contest.vote.ParticipantsVoteActivity;
import com.example.bigtalk.bigtalk.myspace.MyAttenedDebateActivity;
import com.tencent.TMG.ITMGContext;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sig.AuthBuffer;

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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//public class ContestINGActivity extends AppCompatActivity implements TMGDispatcherBase {
public class ContestINGActivity extends AppCompatActivity {


    private static final String LOG_TAG = ContestINGActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
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
    @Bind(R.id.btn_mic)
    Button btnMic;
    @Bind(R.id.btn_chat)
    Button btnChat;
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
    @Bind(R.id.tv_current)
    TextView tvCurrent;


    Context mContext;

    //开启游戏语音需要的参数
    //接入步骤1：获取相关信息，由腾讯云申请， RoomID为大于等于6位的整数
    String sdkAppId, accountType, roomID;
    String key;
    String identifier;
    String appVersion = "demo_1_1";

    public static final int STATE_NONE = 0x00000000;
    public static final int STATE_AUDIO = 0x00000001;
    public static final int STATE_VIDEO = 0x00000020;
    int micState = 0;
    int speakerState = 0;
    @Bind(R.id.label_posi_1)
    TextView labelPosi1;
    @Bind(R.id.label_nega_1)
    TextView labelNega1;
    @Bind(R.id.label_posi_2)
    TextView labelPosi2;
    @Bind(R.id.label_nega_2)
    TextView labelNega2;
    @Bind(R.id.label_posi_3)
    TextView labelPosi3;
    @Bind(R.id.label_nega_3)
    TextView labelNega3;
    @Bind(R.id.label_posi_4)
    TextView labelPosi4;
    @Bind(R.id.label_nega_4)
    TextView labelNega4;


    private String contestID, userRole, timeStart, timeStartplus;
    List<Contest> contestInfo;
    int hold_debater;
    Date dateStart;

    Toolbar toolbar;
    String title;

    //cRole是用户当前在房间的角色，contestRoom是当前房间的房间名
    private String chatRoomName;
    CountDownTimer speak_timer, free_debate_timer, close_debate_timer, inner_debate_timer;
    Handler start_handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_ing);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");
        mContext = ContestINGActivity.this;


        //获得要进入的比赛的id和当前使用者的身份（观众/辩手）
//        Intent i = getIntent();
//        contestID = i.getStringExtra("contestIdSent");


        //测试用
        contestID = "03";

        toolbar = (Toolbar) findViewById(R.id.ing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();



        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        contestInfo = new ArrayList<>();

        //根据contestid查询比赛的相关信息：辩题、辩手信息、正反方聊天室id、当前比赛的房间，比赛时间点
        BmobQuery<Contest> query = new BmobQuery<>();
        query.addWhereEqualTo("contest_id", contestID);
        query.include("topic,posi_1,posi_2,posi_3,posi_4,nega_1,nega_2,nega_3,nega_4");
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
                        Toast.makeText(ContestINGActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContestINGActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });


    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle("电影制作应以大众需求/艺术价值为导向");
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

            //获取当前用户的id，在contest中查询，获得用户的持方及位置赋值给hold_debater
            User cUser = BmobUser.getCurrentUser(User.class);

            Log.i("Voicing!!!", "当前的user的objectID是" + cUser.getObjectId());

            Log.i("Voicing!!!", "当前的Posi_2是" + contestInfo.get(0).getPosi_2().getObjectId());


            if (cUser.getObjectId().equals(contestInfo.get(0).getPosi_1().getObjectId())) {
                hold_debater = 1;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getPosi_2().getObjectId())) {
                hold_debater = 2;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getPosi_3().getObjectId())) {
                hold_debater = 3;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getPosi_4().getObjectId())) {
                hold_debater = 4;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getNega_1().getObjectId())) {
                hold_debater = -1;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getNega_2().getObjectId())) {
                hold_debater = -2;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getNega_3().getObjectId())) {
                hold_debater = -3;
            } else if (cUser.getObjectId().equals(contestInfo.get(0).getNega_4().getObjectId())) {
                hold_debater = -4;
            } else {
                hold_debater = 0;
            }

            Log.i("Voicing!!!", "当前的hold_debater是" + hold_debater);


            if (hold_debater == 1 || hold_debater == 2 || hold_debater == 3 || hold_debater == 4) {
                chatRoomName = contestInfo.get(0).getPosi_room_id();
            } else if (hold_debater == -1 || hold_debater == -2 || hold_debater == -3 || hold_debater == -4) {
                chatRoomName = contestInfo.get(0).getNega_room_id();
            } else {
                chatRoomName = "";
            }

            Topics currentTopic = contestInfo.get(0).getTopic();
            title = currentTopic.getTopic_name();
            tvPosi.setText(currentTopic.getPosi_opi());
            tvNega.setText(currentTopic.getNega_opi());

            btnMic.setVisibility(View.INVISIBLE);

            btnChat.setVisibility(View.GONE);
            btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent fraintent = new Intent(ContestINGActivity.this, ConversationActivity.class);
//                    fraintent.putExtra("RoomID",chatRoomName);
//                    startActivity(fraintent);
                }
            });


        } else {
            Log.i("bmob", "size并没有大于0");
        }

        startContest();

//        try {
//            initBroadcasting();
//        } catch (ParseException e2) {
//            e2.printStackTrace();
//        }

    }

//    public void initBroadcasting() throws ParseException {
//
//        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
//            sdkAppId = this.getString(R.string.private_app_id);
//            accountType = this.getString(R.string.private_account_type);
//
//            User cUser = BmobUser.getCurrentUser(User.class);
//            identifier = cUser.getIdentifier();
//
//            userRole = "Werewolf";
//
//            key = this.getString(R.string.private_key);
//
//
//            if (TextUtils.isEmpty(sdkAppId)) {
//                throw new RuntimeException("NEED TO use your App ID, get your own ID");
//            }
//            if (TextUtils.isEmpty(accountType)) {
//                throw new RuntimeException("NEED TO use your accountType, get your own accountType");
//            }
//            if (TextUtils.isEmpty(identifier)) {
//                throw new RuntimeException("NEED TO use your identifier, get your own identifier");
//            }
//
//
//            //接入步骤2：用户自己实现回调分发函数
//            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ENTER_ROOM, this);
//
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVNET_TYPE_USER_UPDATE, this);
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_EXIT_ROOM, this);
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ROOM_DISCONNECT, this);
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ENABLE_MIC, this);
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_DISABLE_MIC, this);
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_DISABLE_SPEAKER, this);
////            TMGCallbackDispatcher.getInstance().AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ENABLE_SPEAKER, this);
//
//            //接入步骤3：将自己实现的回调函数注册给SDK
//            ITMGContext.GetInstance(this).SetTMGDelegate(TMGCallbackDispatcher.getInstance().getItmgDelegate());
//
//            //接入步骤4：将步骤1中的信息设置到SDK中
//            ITMGContext.GetInstance(this).SetAppInfo(sdkAppId, accountType, identifier);
//            ITMGContext.GetInstance(this).SetAppVersion(appVersion);
//
//
//            //接入步骤5：生成AuthBuffer，鉴权秘钥
//            long nExpUTCTime = 1800 + System.currentTimeMillis() / 1000L;
//
//
//            Log.i("Voicing", "sdkAppId是 " + sdkAppId + "进房成功！！！！");
//            Log.i("Voicing", "roomID是 " + roomID + "进房成功！！！！");
//            Log.i("Voicing", "identifier是 " + identifier + "进房成功！！！！");
//            Log.i("Voicing", "accountType是 " + accountType + "进房成功！！！！");
//            Log.i("Voicing", "key是 " + key + "进房成功！！！！");
//            Log.i("Voicing", "nExpUTCTime是 " + nExpUTCTime + "进房成功！！！！");
//            Log.i("Voicing", "ITMGContext.ITMG_AUTH_BITS_DEFAULT是 " + ITMGContext.AUTH_BITS_ALL + "进房成功！！！！");
//
//
//            byte[] authBuffer = AuthBuffer.getInstance().genAuthBuffer(Integer.parseInt(sdkAppId), Integer.parseInt(roomID),
//                    identifier, Integer.parseInt(accountType), key, (int) nExpUTCTime, (int) ITMGContext.ITMG_AUTH_BITS_DEFAULT);
//
//            //接入步骤6：用生成的秘钥进房， 会收到ITMG_MAIN_EVENT_TYPE_ENTER_ROOM的回调， 标识进房成功
//            try {
//                ITMGContext.GetInstance(this).EnterRoom(Integer.parseInt(roomID), userRole, authBuffer);
//
//            } catch (Exception e) {
//                Log.e(LOG_TAG, Log.getStackTraceString(e));
//                throw new RuntimeException("NEED TO check TMG sdk init fatal error\n" + Log.getStackTraceString(e));
//            }
//
//        }
//
//
//    }

    public void startContest() {


        start_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (hold_debater == 1) {
//                            mRtcEngine.muteLocalAudioStream(false);
//                            ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(true);
//                            Log.i("onRunning", "case1的声音开了！！！！" + ITMGContext.GetInstance(mContext).GetAudioCtrl().GetMicState());
//                            Log.i("onRunning", "case1的扬声器状态！！！！" + ITMGContext.GetInstance(mContext).GetAudioCtrl().GetSpeakerState());
                            btnMic.setVisibility(View.VISIBLE);
//                            ivPosi1.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
                            speak_timer.start();
                        } else {
//                            mRtcEngine.muteLocalAudioStream(true);
//                            ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(false);
//                            Log.i("onRunning", "case1的声音关了！！！！" + ITMGContext.GetInstance(mContext).GetAudioCtrl().GetMicState());
                            btnMic.setVisibility(View.GONE);
                            speak_timer.start();
                        }
                        ivPosi1.setVisibility(View.INVISIBLE);
                        labelPosi1.setVisibility(View.INVISIBLE);
                        ivCurrent.setImageResource(R.drawable.cir1);
                        ivCurrent.setVisibility(View.VISIBLE);
                        tvCurrent.setText("      正方一辩");
                        Toast toast = Toast.makeText(ContestINGActivity.this, "请正方一辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Log.i("start_handler", "第一次handler开始！！！！");
                        break;
                    case 2:
//                        mRtcEngine.muteLocalAudioStream(true);
//                        ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(false);
//                        Log.i("onRunning", "进入case2的声音关了！！！！" + ITMGContext.GetInstance(mContext).GetAudioCtrl().GetMicState());
                        btnMic.setVisibility(View.GONE);
//                        cleanAllFilter();
                        Log.i("start_handler", "speakTimer的handler开始！！！！");
                        speakInOrder(hold_debater);
                        break;
                    case 3:
//                        mRtcEngine.muteLocalAudioStream(true);
//                        ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(false);
                        ivCurrent.setVisibility(View.INVISIBLE);
                        ivCurrent2.setVisibility(View.INVISIBLE);

//                        Log.i("onRunning", "进入case3的声音:" + ITMGContext.GetInstance(mContext).GetAudioCtrl().GetMicState());
                        btnMic.setVisibility(View.GONE);
                        Log.i("start_handler", "自由辩论的Timer的handler开始！！！！");
//                        cleanAllFilter();
                        speakInOrder(hold_debater);
                        break;
                    case 4:
//                        mRtcEngine.muteLocalAudioStream(true);
//                        ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(false);
                        Log.i("onRunning", "进入case4的声音关了！！！！");
                        btnMic.setVisibility(View.GONE);
                        Log.i("start_handler", "结辩的Timer的handler开始！！！！");
//                        cleanAllFilter();
                        speakInOrder(hold_debater);
                        break;
                    case 5:
//                        mRtcEngine.muteLocalAudioStream(true);
//                        ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(false);
                        Log.i("onRunning", "进入case5的声音关了！！！！");
                        Log.i("start_handler", "结束发言的Timer的handler开始！！！！");
                        btnMic.setVisibility(View.GONE);
                        speakInOrder(hold_debater);
                        break;
                }
            }
        };


//        optional();

        //将time_start转换成时间类型，与当前时间比较，一旦到了比赛开始时间，只剩正方一辩开麦，其余七个人麦克风全部关闭
        Timer start_timer = new Timer();
        TimerTask taskStart = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                start_handler.sendMessage(message);

            }
        };
        start_timer.schedule(taskStart, dateStart);

//        micState = ITMGContext.GetInstance(this).GetAudioCtrl().GetMicState();
//        speakerState = ITMGContext.GetInstance(this).GetAudioCtrl().GetSpeakerState();
//
//        Log.i(LOG_TAG, "麦克风打开了吗？？？？？" + micState);
//        Log.i(LOG_TAG, "扬声器打开了吗？？？？？" + speakerState);


        //1000是指每隔1000毫秒就调用一次onTick方法,这个countdowntimer主要是用来计时发言时间
        //speak_timer = new CountDownTimer(180000, 1000) {
        speak_timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date remain = new Date(millisUntilFinished);
                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
                tvCountDown.setText(timeRemain.substring(3));
            }

            @Override
            public void onFinish() {
                Log.i("speak_timer", "speak_timer准备给handler发送消息！！！！");
                ivCurrent.setVisibility(View.INVISIBLE);
                ivCurrent2.setVisibility(View.INVISIBLE);
                tvCurrent.setVisibility(View.INVISIBLE);

                Message message = new Message();
                message.what = 2;
                start_handler.sendMessage(message);

//                worker().getRtcEngine().muteLocalAudioStream(false);
//                mRtcEngine.muteLocalAudioStream(true);
//                btnMic.setVisibility(View.GONE);
//                cleanAllFilter();
//                speakInOrder(hold_debater);

            }
        };

        //1000是指每隔1000毫秒就调用一次onTick方法,这个countdowntimer主要是用来计时自由辩论发言时间
        // free_debate_timer = new CountDownTimer(480000, 1000) {
        free_debate_timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date remain = new Date(millisUntilFinished);
                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
                tvCountDown.setText(timeRemain.substring(3));
            }

            @Override
            public void onFinish() {

                Message message = new Message();
                message.what = 3;
                start_handler.sendMessage(message);

                Log.i("free_debate_timer", "free_debate_timer准备给handler发送消息！！！！");
//                mRtcEngine.muteLocalAudioStream(true);
//                btnMic.setVisibility(View.GONE);
//                cleanAllFilter();
//                speakInOrder(hold_debater);

            }
        };

        inner_debate_timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date remain = new Date(millisUntilFinished);
                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);

            }

            @Override
            public void onFinish() {
                ivPosi1.setVisibility(View.VISIBLE);
                labelPosi1.setVisibility(View.VISIBLE);
                ivNega3.setVisibility(View.VISIBLE);
                labelNega3.setVisibility(View.VISIBLE);

                ivPosi3.setVisibility(View.INVISIBLE);
                labelPosi3.setVisibility(View.INVISIBLE);
                ivNega4.setVisibility(View.INVISIBLE);
                labelNega4.setVisibility(View.INVISIBLE);

                ivCurrent.setVisibility(View.INVISIBLE);
                ivCurrent2.setImageResource(R.drawable.cop6_34);
                ivCurrent2.setVisibility(View.VISIBLE);
                tvCurrent.setText("正方三辩        反方四辩");



            }
        };

        //1000是指每隔1000毫秒就调用一次onTick方法,这个countdowntimer主要是用来计时结辩发言时间
        //close_debate_timer = new CountDownTimer(240000, 1000) {
        close_debate_timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date remain = new Date(millisUntilFinished);
                String timeRemain = new SimpleDateFormat("HH:mm:ss").format(remain);
                tvCountDown.setText(timeRemain.substring(3));
            }

            @Override
            public void onFinish() {
                Message message = new Message();
                message.what = 4;
                start_handler.sendMessage(message);
                ivCurrent.setVisibility(View.INVISIBLE);
                ivCurrent2.setVisibility(View.INVISIBLE);
                tvCurrent.setVisibility(View.INVISIBLE);


//                ivCurrent2.setVisibility(View.INVISIBLE);
                Log.i("close_debate_timer", "close_debate_timer准备给handler发送消息！！！！");
//                mRtcEngine.muteLocalAudioStream(true);
//                btnMic.setVisibility(View.GONE);
//                cleanAllFilter();
//                speakInOrder(hold_debater);

            }
        };

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = 5;
                start_handler.sendMessage(message);

//                worker().getRtcEngine().muteLocalAudioStream(false);
//                mRtcEngine.muteLocalAudioStream(true);
//                btnMic.setVisibility(View.GONE);
//                speakInOrder(hold_debater);

            }
        });

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

    public void speakInOrder(int hold) {

        int process = 0;

        //long time1 = dateStart.getTime() + 180000;
        long time1 = dateStart.getTime() + 10000;
        Long now = System.currentTimeMillis();
//        Log.i("bmob","dateSTART"+ dateStart.getTime());
//        Log.i("bmob","time1"+ time1);
//        Log.i("bmob","now"+ now);


        //if (now >= time1 && now <= time1 + 180000) {
        if (now >= time1 && now <= time1 + 10000) {
            //反方四辩质询
            process = 1;
            Toast toast = Toast.makeText(ContestINGActivity.this, "请反方四辩开始对正方一辩的质询，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivCurrent.setImageResource(R.drawable.cop1);
            ivCurrent.setVisibility(View.INVISIBLE);
            ivCurrent2.setImageResource(R.drawable.cop1);
            ivCurrent2.setVisibility(View.VISIBLE);
            tvCurrent.setText("正方一辩        反方四辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivPosi1.setVisibility(View.INVISIBLE);
            ivNega4.setVisibility(View.INVISIBLE);
            labelPosi1.setVisibility(View.INVISIBLE);
            labelNega4.setVisibility(View.INVISIBLE);
//            ivPosi1.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
//            ivNega4.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case 1:
                    if_debate(true);
                    break;
                case -4:
                    if_debate(true);
                    break;
                default:
                    if_debate(false);
                    break;
            }
            // else if (now <= time1 + 360000) {
        } else if (now <= time1 + 20000) {
            //反方一辩陈词
            process = 2;
            labelPosi1.setVisibility(View.VISIBLE);
            labelNega4.setVisibility(View.VISIBLE);
            ivPosi1.setVisibility(View.VISIBLE);
            ivNega4.setVisibility(View.VISIBLE);
            ivCurrent.setImageResource(R.drawable.cir5);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("     反方一辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivNega1.setVisibility(View.INVISIBLE);
            labelNega1.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(ContestINGActivity.this, "请反方一辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivNega1.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case -1:
                    if_debate(true);
                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 540000) {
        } else if (now <= time1 + 30000) {
            //正方四辩质询
            process = 3;

            ivCurrent.setVisibility(View.INVISIBLE);
            ivCurrent2.setImageResource(R.drawable.cop2);
            ivCurrent2.setVisibility(View.VISIBLE);
            tvCurrent.setText("正方四辩        反方一辩");
            tvCurrent.setVisibility(View.VISIBLE);
//            ivCurrent.setImageResource(R.drawable.cir5);
//            ivCurrent.setVisibility(View.VISIBLE);
//            ivCurrent2.setImageResource(R.drawable.cir4);
//            ivCurrent2.setVisibility(View.VISIBLE);
            ivPosi4.setVisibility(View.INVISIBLE);
            labelPosi4.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(ContestINGActivity.this, "请正方四辩开始对反方一辩的质询，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivPosi4.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//            ivNega1.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case 4:
                    if_debate(true);
                    break;
                case -1:
                    if_debate(true);
                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 720000) {
        } else if (now <= time1 + 40000) {
            //正方二辩陈词
            process = 4;
            ivNega1.setVisibility(View.VISIBLE);
            labelNega1.setVisibility(View.VISIBLE);
            ivPosi4.setVisibility(View.VISIBLE);
            labelPosi4.setVisibility(View.VISIBLE);

            ivCurrent.setImageResource(R.drawable.cir2);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("      正方二辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivPosi2.setVisibility(View.INVISIBLE);
            labelPosi2.setVisibility(View.INVISIBLE);

            Toast toast = Toast.makeText(ContestINGActivity.this, "请正方二辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivPosi2.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case 2:
                    if_debate(true);
                    Log.i("Voicing", "It's HEllo  speaking!!!!!!!");

                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 900000) {
        } else if (now <= time1 + 50000) {
            //反方三辩质询
            process = 5;
            ivCurrent.setVisibility(View.INVISIBLE);
            ivCurrent2.setImageResource(R.drawable.cop3);
            ivCurrent2.setVisibility(View.VISIBLE);
            tvCurrent.setText("正方二辩        反方三辩");
            tvCurrent.setVisibility(View.VISIBLE);
//            ivCurrent.setImageResource(R.drawable.cir2);
//            ivCurrent.setVisibility(View.VISIBLE);
//            ivCurrent2.setImageResource(R.drawable.cir7);
//            ivCurrent2.setVisibility(View.VISIBLE);
            ivNega3.setVisibility(View.INVISIBLE);
            labelNega3.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(ContestINGActivity.this, "请反方三辩开始质询正方二辩，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivNega3.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//            ivPosi2.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case -3:
                    if_debate(true);

                    break;
                case 2:
                    if_debate(true);

                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 1080000) {
        } else if (now <= time1 + 60000) {
            //反方二辩陈词
            process = 6;
            ivPosi2.setVisibility(View.VISIBLE);
            labelPosi2.setVisibility(View.VISIBLE);
            ivNega3.setVisibility(View.VISIBLE);
            labelNega3.setVisibility(View.VISIBLE);

            ivCurrent.setImageResource(R.drawable.cir6);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("      反方二辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivNega2.setVisibility(View.INVISIBLE);
            labelNega2.setVisibility(View.INVISIBLE);

            Toast toast = Toast.makeText(ContestINGActivity.this, "请反方二辩开始陈词，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivNega2.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case -2:
                    if_debate(true);

                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 1260000) {
        } else if (now <= time1 + 70000) {
            //正方三辩质询
            process = 7;
            ivCurrent.setVisibility(View.INVISIBLE);
            ivCurrent2.setImageResource(R.drawable.cop4);
            ivCurrent2.setVisibility(View.VISIBLE);

            tvCurrent.setText("正方三辩        反方二辩");
            tvCurrent.setVisibility(View.VISIBLE);
//            ivCurrent.setImageResource(R.drawable.cir6);
//            ivCurrent.setVisibility(View.VISIBLE);
//            ivCurrent2.setImageResource(R.drawable.cir3);
//            ivCurrent2.setVisibility(View.VISIBLE);
            ivPosi3.setVisibility(View.INVISIBLE);
            labelPosi3.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(ContestINGActivity.this, "请正方三辩开始对反方二辩的质询，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivPosi3.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
//            ivNega2.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case 3:
                    if_debate(true);
                    break;
                case -2:
                    if_debate(true);
                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 1440000) {
        } else if (now <= time1 + 80000) {
            //反方三辩质询小结
            process = 8;

            ivNega2.setVisibility(View.VISIBLE);
            labelNega2.setVisibility(View.VISIBLE);
            ivPosi3.setVisibility(View.VISIBLE);
            labelPosi3.setVisibility(View.VISIBLE);


            ivCurrent.setImageResource(R.drawable.cir7);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("      反方三辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivNega3.setVisibility(View.INVISIBLE);
            labelNega3.setVisibility(View.INVISIBLE);

            Toast toast = Toast.makeText(ContestINGActivity.this, "请反方三辩开始质询小结，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivNega3.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case -3:
                    if_debate(true);

                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 1620000) {
        } else if (now <= time1 + 90000) {
            //正方三辩质询小结
            process = 9;
            ivNega3.setVisibility(View.VISIBLE);
            labelNega3.setVisibility(View.VISIBLE);

            ivCurrent.setImageResource(R.drawable.cir3);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("      正方三辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivPosi3.setVisibility(View.INVISIBLE);
            labelPosi3.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(ContestINGActivity.this, "请正方三辩开始质询小结，时间为3分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivPosi3.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case 3:
                    if_debate(true);
                    break;
                default:
                    if_debate(false);
                    break;
            }
            //else if (now <= time1 + 1800000) {
        } else if (now <= time1 + 100000) {
            //自由辩论
            process = 10;
//            ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(true);
            Log.i("onRunning", "自由辩论的mRtcEngine声音开了！！！！");
            btnMic.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(ContestINGActivity.this, "请双方开始自由辩论，总时长为8分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            ivPosi3.setVisibility(View.VISIBLE);
            labelPosi3.setVisibility(View.VISIBLE);

            ivPosi1.setVisibility(View.INVISIBLE);
            ivNega3.setVisibility(View.INVISIBLE);
            labelPosi1.setVisibility(View.INVISIBLE);
            labelNega3.setVisibility(View.INVISIBLE);

            ivCurrent.setVisibility(View.INVISIBLE);
            ivCurrent2.setImageResource(R.drawable.cop5_13);
            ivCurrent2.setVisibility(View.VISIBLE);
            tvCurrent.setText("正方一辩        反方三辩");
            tvCurrent.setVisibility(View.VISIBLE);
            free_debate_timer.start();
            inner_debate_timer.start();

//            ivPosi1.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivPosi2.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivPosi3.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivPosi4.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivNega1.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivNega2.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivNega3.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);
//            ivNega4.setColorFilter(getResources().getColor(R.color.base_bg), PorterDuff.Mode.MULTIPLY);

            //else if (now <= time1 + 1980000) {
        } else if (now <= time1 + 110000) {
            //反方四辩结辩
            process = 11;
            ivPosi3.setVisibility(View.VISIBLE);
            labelPosi3.setVisibility(View.VISIBLE);


            ivCurrent.setImageResource(R.drawable.cir8);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("      反方四辩");
            tvCurrent.setVisibility(View.VISIBLE);

            ivNega4.setVisibility(View.INVISIBLE);
            labelNega4.setVisibility(View.INVISIBLE);

            Toast toast = Toast.makeText(ContestINGActivity.this, "请反方四辩开始结辩，时间为4分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivNega4.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case -4:
//                    mRtcEngine.muteLocalAudioStream(false);
//                    ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(true);
                    btnMic.setVisibility(View.VISIBLE);
                    close_debate_timer.start();
                    break;
                default:
                    close_debate_timer.start();
                    break;
            }
            //else if (now <= time1 + 2160000) {
        } else if (now <= time1 + 120000) {
            ivNega4.setVisibility(View.VISIBLE);
            labelNega4.setVisibility(View.VISIBLE);
            ivCurrent.setImageResource(R.drawable.cir4);
            ivCurrent.setVisibility(View.VISIBLE);
            tvCurrent.setText("      正方四辩");
            tvCurrent.setVisibility(View.VISIBLE);
            ivPosi4.setVisibility(View.INVISIBLE);
            labelPosi4.setVisibility(View.INVISIBLE);
            //正方四辩结辩
            process = 12;
            Toast toast = Toast.makeText(ContestINGActivity.this, "请正方四辩开始结辩，时间为4分钟", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ivPosi4.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.MULTIPLY);
            switch (hold) {
                case 4:
//                    mRtcEngine.muteLocalAudioStream(false);
//                    ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(true);
                    btnMic.setVisibility(View.VISIBLE);
                    close_debate_timer.start();
                    break;
                default:
                    close_debate_timer.start();
                    break;
            }
            //else if (now > time1 + 2160000) {
        } else if (now > time1 + 120000) {
            ivPosi4.setVisibility(View.VISIBLE);
            labelPosi4.setVisibility(View.VISIBLE);

            speak_timer.cancel();
            free_debate_timer.cancel();
            close_debate_timer.cancel();
//            worker().getRtcEngine().muteLocalAudioStream(false);
//            mRtcEngine.muteLocalAudioStream(true);
//            ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(false);
            Log.i("onRunning", "比赛结束了的mRtcEngine声音关了！！！！");
            btnMic.setVisibility(View.GONE);

            Toast toast = Toast.makeText(ContestINGActivity.this, "比赛结束", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            ITMGContext.GetInstance(this).ExitRoom();
            Intent intentend = new Intent(this, ParticipantsVoteActivity.class);
            intentend.putExtra("contestIdSent", contestID);
            intentend.putExtra("voterType", "辩手");
            startActivity(intentend);
        }

    }

    public void if_debate(boolean debate) {
        if (debate) {
//            worker().getRtcEngine().muteLocalAudioStream(true);
//            mRtcEngine.muteLocalAudioStream(false);
//            ITMGContext.GetInstance(this).GetAudioCtrl().EnableMic(true);
            Log.i("Voicing", "声音应该开了！！！！！");

            btnMic.setVisibility(View.VISIBLE);
            speak_timer.start();
        } else {
            speak_timer.start();
        }
    }


//    public boolean checkSelfPermission(String permission, int requestCode) {
//        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
//        if (ContextCompat.checkSelfPermission(this,
//                permission)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{permission},
//                    requestCode);
//            return false;
//        }
//        return true;
//    }

    private void optional() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    @Override
    protected void onDestroy() {
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVNET_TYPE_USER_UPDATE, this);
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_EXIT_ROOM, this);
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ROOM_DISCONNECT, this);
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ENABLE_MIC, this);
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_DISABLE_MIC, this);
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_DISABLE_SPEAKER, this);
//        TMGCallbackDispatcher.getInstance().RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_ENABLE_SPEAKER, this);

        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
//        log.info("onBackPressed");
        Intent intent = new Intent(this, MyAttenedDebateActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
//    public void OnEvent(ITMGContext.ITMG_MAIN_EVENT_TYPE type, Intent data) {
//        int nErrCode = TMGCallbackHelper.ParseIntentParams2(data).nErrCode;
//        String strMsg = TMGCallbackHelper.ParseIntentParams2(data).strErrMsg;
//
//        //接入步骤7：收到进房信令， 进房成功， 可以操作设备， 参看RoomActivity
//        if (nErrCode == AVError.AV_OK) {
//
//
////            ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableMic(true);
////
////            micState = ITMGContext.GetInstance(mContext).GetAudioCtrl().GetMicState();
////
////            Log.i("Voicing!!!!!!","checkpoint1,micState(应该是2):"+micState);
////
////
////            ITMGContext.GetInstance(mContext).GetAudioCtrl().EnableSpeaker(true);
////            speakerState = ITMGContext.GetInstance(mContext).GetAudioCtrl().GetSpeakerState();
////            Log.i("Voicing!!!!!!","checkpoint2,speakerState(应该是2):"+speakerState);
//
//
//            ITMGContext.GetInstance(this).GetAudioCtrl().EnableMic(true);
//            ITMGContext.GetInstance(this).GetAudioCtrl().EnableSpeaker(true);
//
//            //此函数获取麦克风状态，返回值 0 为关闭麦克风状态，返回值 1 为打开麦克风状态，返回值 2 为麦克风设备正在操作中，返回值 4 为设备没初始化好。
//
//
//            //此函数用于扬声器状态获取。返回值 0 为关闭扬声器状态，返回值 1 为打开扬声器状态，返回值 2 为扬声器设备正在操作中，返回值 4 为设备没初始化好。
//
//
//            Log.i(LOG_TAG, "进入房间了吗？？？？？" + ITMGContext.GetInstance(this).IsRoomEntered());
//
////            AudioManager audiomanage = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
////            Log.i(LOG_TAG, "检查麦克风？？？？？"+audiomanage.isMicrophoneMute());
////            Log.i(LOG_TAG, "检查扬声器？？？？？"+audiomanage.isSpeakerphoneOn());
//
//
//            startContest();
//
//        } else {
//            Toast.makeText(ContestINGActivity.this, String.format("result=%d, errorInfo=%s", nErrCode, strMsg), Toast.LENGTH_SHORT).show();
//        }
//
////        if (ITMGContext.ITMG_MAIN_EVENT_TYPE.ITMG_MAIN_EVENT_TYPE_EXIT_ROOM == type)
////        {
////            //接入步骤15： 收到退房成功事件
//////            Intent intent = new Intent(this, ParticipantsVoteActivity.class);
//////            this.startActivity(intent);
//////            finish();
////            Toast.makeText(ContestINGActivity.this,"即将离开比赛",Toast.LENGTH_SHORT);
////        }
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
//        //按下的如果是BACK，同时没有重复
//        {
//            ITMGContext.GetInstance(this).ExitRoom();
//            Intent intent = new Intent(this, DebateFragment.class);
//            this.startActivity(intent);
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
