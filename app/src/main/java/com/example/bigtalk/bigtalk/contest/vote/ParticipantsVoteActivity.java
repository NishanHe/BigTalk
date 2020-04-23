package com.example.bigtalk.bigtalk.contest.vote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.DebaterVoteRecord;
import com.example.bigtalk.bigtalk.bean.User;



import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ParticipantsVoteActivity extends AppCompatActivity {


    List<Contest> contestInfo;


    @Bind(R.id.btn_submit)
    Button btnSubmit;


    User cUser;
    int checkedItem = 0;
    String voterType, contestID;
    User debater1, debater2;
    Contest contest;
    @Bind(R.id.cv_posi_1)
    CheckedTextView cvPosi1;
    @Bind(R.id.cv_posi_2)
    CheckedTextView cvPosi2;
    @Bind(R.id.cv_posi_3)
    CheckedTextView cvPosi3;
    @Bind(R.id.cv_posi_4)
    CheckedTextView cvPosi4;
    @Bind(R.id.cv_nega_1)
    CheckedTextView cvNega1;
    @Bind(R.id.cv_nega_2)
    CheckedTextView cvNega2;
    @Bind(R.id.cv_nega_3)
    CheckedTextView cvNega3;
    @Bind(R.id.cv_nega_4)
    CheckedTextView cvNega4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_vote);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        Intent intent = getIntent();
        contestID = intent.getStringExtra("contestIdSent");
        voterType = intent.getStringExtra("voterType");


//        cUser = BmobUser.getCurrentUser(User.class);

        //测试用,是01比赛的nega_4
//        contestID = "01";
//        voterType = "辩手";
        User testUser = new User();
        testUser.setObjectId("1dva555R");
        testUser.setUsername("test3");
        cUser = testUser;

        initData();


    }

    public void initData() {
        contestInfo = new ArrayList<>();


        //根据contestid查询辩手信息
        BmobQuery<Contest> query = new BmobQuery<>();
        query.addWhereEqualTo("contest_id", contestID);
        query.include("posi_1,posi_2,posi_3,posi_4,nega_1,nega_2,nega_3,nega_4");
        query.setLimit(500);
        query.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            contestInfo.add(list.get(i));

                        }
                        initContestData(contestInfo);

                    } else {
                        Toast.makeText(ParticipantsVoteActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ParticipantsVoteActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cvPosi1.isChecked()) {
                    checkedItem += 1;
                    debater1 = contestInfo.get(0).getPosi_1();
                }
                if (cvPosi2.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getPosi_2();
                    } else {
                        debater2 = contestInfo.get(0).getPosi_2();
                    }
                }
                if (cvPosi3.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getPosi_3();
                    } else {
                        debater2 = contestInfo.get(0).getPosi_3();
                    }
                }
                if (cvPosi4.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getPosi_4();
                    } else {
                        debater2 = contestInfo.get(0).getPosi_4();
                    }
                }
                if (cvNega1.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getNega_1();
                    } else {
                        debater2 = contestInfo.get(0).getNega_1();
                    }
                }
                if (cvNega2.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getNega_2();
                    } else {
                        debater2 = contestInfo.get(0).getNega_2();
                    }
                }
                if (cvNega3.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getNega_3();
                    } else {
                        debater2 = contestInfo.get(0).getNega_3();
                    }
                }
                if (cvNega4.isChecked()) {
                    checkedItem += 1;
                    if (debater1 == null) {
                        debater1 = contestInfo.get(0).getNega_4();
                    } else {
                        debater2 = contestInfo.get(0).getNega_4();
                    }
                }

                if (checkedItem > 2) {
                    Toast.makeText(ParticipantsVoteActivity.this, "您的选择不能多于两人！", Toast.LENGTH_SHORT).show();
                } else if (checkedItem <= 0) {
                    Toast.makeText(ParticipantsVoteActivity.this, "没有选择辩手！", Toast.LENGTH_SHORT).show();
                } else {
                    voteDebater(voterType, cUser, debater1, contest);
                    if (debater2 != null) {
                        voteDebater(voterType, cUser, debater2, contest);
                    }
                }

            }
        });

    }

    public void initContestData(final List<Contest> contestInfo) {
        if (contestInfo.size() > 0) {
            cvNega1.setText(contestInfo.get(0).getNega_1().getUsername());
            cvNega2.setText(contestInfo.get(0).getNega_2().getUsername());
            cvNega3.setText(contestInfo.get(0).getNega_3().getUsername());
            cvNega4.setText(contestInfo.get(0).getNega_4().getUsername());
            cvPosi1.setText(contestInfo.get(0).getPosi_1().getUsername());
            cvPosi2.setText(contestInfo.get(0).getPosi_2().getUsername());
            cvPosi3.setText(contestInfo.get(0).getPosi_3().getUsername());
            cvPosi4.setText(contestInfo.get(0).getPosi_4().getUsername());

            cvPosi1.setOnClickListener(new MyListener());
            cvPosi2.setOnClickListener(new MyListener());
            cvPosi3.setOnClickListener(new MyListener());
            cvPosi4.setOnClickListener(new MyListener());
            cvNega1.setOnClickListener(new MyListener());
            cvNega2.setOnClickListener(new MyListener());
            cvNega3.setOnClickListener(new MyListener());
            cvNega4.setOnClickListener(new MyListener());

//
//            ArrayList<String> mStrings = new ArrayList<String>();
//            mStrings.add(contestInfo.get(0).getPosi_1().getUsername());
//            mStrings.add(contestInfo.get(0).getPosi_2().getUsername());
//            mStrings.add(contestInfo.get(0).getPosi_3().getUsername());
//            mStrings.add(contestInfo.get(0).getPosi_4().getUsername());
//            mStrings.add(contestInfo.get(0).getNega_1().getUsername());
//            mStrings.add(contestInfo.get(0).getNega_2().getUsername());
//            mStrings.add(contestInfo.get(0).getNega_3().getUsername());
//            mStrings.add(contestInfo.get(0).getNega_4().getUsername());

//            if (cvPosi1.getC() != CHOICE_MODE_NONE && mCheckStates != null) {
//                if (child instanceof Checkable) {
//                    ((Checkable) child).setChecked(mCheckStates.get(position));
//                }
//            }

            if (cUser.getUsername().equals(cvPosi1.getText())) {
                cvPosi1.setClickable(false);
                Log.i("UIWatching","cvPosi1不能点啦");
            } else if (cUser.getUsername().equals(cvPosi2.getText())) {
                cvPosi2.setClickable(false);
                Log.i("UIWatching","cvPosi2不能点啦");
            } else if (cUser.getUsername().equals(cvPosi3.getText())) {
                cvPosi3.setClickable(false);
                Log.i("UIWatching","cvPosi3不能点啦");
            } else if (cUser.getUsername().equals(cvPosi4.getText())) {
                cvPosi4.setClickable(false);
                Log.i("UIWatching","cvPosi4不能点啦");
            } else if (cUser.getUsername().equals(cvNega1.getText())) {
                cvNega1.setClickable(false);
                Log.i("UIWatching","cvNega1不能点啦");
            } else if (cUser.getUsername().equals(cvNega2.getText())) {
                cvNega2.setClickable(false);
                Log.i("UIWatching","cvNega2不能点啦");
            } else if (cUser.getUsername().equals(cvNega3.getText())) {
                cvNega3.setClickable(false);
                Log.i("UIWatching","cvNega3不能点啦");
            } else if (cUser.getUsername().equals(cvNega4.getText())) {
                cvNega4.setClickable(false);
                Log.i("UIWatching","cvNega4不能点啦");
            }

            contest = new Contest();
            contest.setObjectId(contestInfo.get(0).getObjectId());

        } else {
            Log.i("bmob", "size并没有大于0");
        }

    }

    public void voteDebater(String voterType, User voter, User Debater, Contest contest) {
        DebaterVoteRecord voteUpdate = new DebaterVoteRecord();
        voteUpdate.setVoter(voter);
        voteUpdate.setContest(contest);
        voteUpdate.setDebater(Debater);
        voteUpdate.setVoterType(voterType);
        voteUpdate.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ParticipantsVoteActivity.this);
                    builder.setMessage(R.string.debaterVoterCallBack);
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(ParticipantsVoteActivity.this, "投票失败", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "添加失败:" + e.getMessage());
                }

            }
        });

    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == cvPosi1){
                cvPosi1.toggle();
            }else if (v == cvPosi2 ){
                cvPosi2.toggle();
            }else if (v ==cvPosi3 ){
                cvPosi3.toggle();
            }else if (v ==cvPosi4 ){
                cvPosi4.toggle();
            }else if (v ==cvNega4 ){
                cvNega4.toggle();
            }else if (v ==cvNega3 ){
                cvNega3.toggle();
            }else if (v == cvNega2){
                cvNega2.toggle();
            }else if (v == cvNega1){
                cvNega1.toggle();
            }
        }
    }

}
