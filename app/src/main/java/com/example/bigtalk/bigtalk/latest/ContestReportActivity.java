package com.example.bigtalk.bigtalk.latest;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class ContestReportActivity extends AppCompatActivity {

    private TextView votePosi,voteNega,winner,mvp,freeDebate,activeBefore,audiencePeek;
    String contestResult,contestid,mvpText,freeDebateText,activeBeforeText,audiencePeekText;
    private Contest contest;
    Integer posi,nega;
    String title;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_report);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");
        this.hideActionBar();
        this.setFullScreen();

        votePosi = (TextView)findViewById(R.id.contest_report_vote_posi);
        voteNega = (TextView)findViewById(R.id.contest_report_vote_nega);
        winner = (TextView)findViewById(R.id.contest_report_text_winner);
        mvp = (TextView)findViewById(R.id.contest_report_text_mvp);
        freeDebate = (TextView)findViewById(R.id.contest_report_text_free_debate);
        activeBefore = (TextView)findViewById(R.id.contest_report_text_active_before);
        audiencePeek = (TextView)findViewById(R.id.contest_report_text_audience_peek);

        Intent intent = getIntent();
        contestid = intent.getStringExtra("contest_id");

        toolbar = (Toolbar) findViewById(R.id.contest_report_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        if(contestid != null){
            BmobQuery<Contest> query = new BmobQuery<Contest>();
            query.getObject(contestid, new QueryListener<Contest>() {

                @Override
                public void done(Contest object, BmobException e) {
                    if(e==null){

                        title = object.getTopic().getTopic_name();
                        contestResult = object.getContest_report().getContest_result();
                        mvpText = object.getContest_report().getMvpString();
                        freeDebateText = object.getContest_report().getFree_debateString();
                        activeBeforeText = object.getContest_report().getActive_beforeString();
                        audiencePeekText = object.getContest_report().getAudience_peekString();
                        posi = object.getContest_report().getVote_posi();
                        nega = object.getContest_report().getVote_nega();

                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }

        //测试用——————————————————————————————————————————————————————
        votePosi.setText("103");
//        if(posi != null){
//            votePosi.setText(posi);
//        }

        voteNega.setText("98");
//        if(nega != null){
//            voteNega.setText(nega);
//        }

        winner.setText("正方胜");
//        if(contestResult != null){
//            winner.setText(contestResult);
//        }


        mvp.setText("正方二辩");
//        if(mvpText != null){
//            mvp.setText(mvpText);
//        }

        freeDebate.setText("反方三辩");
//        if(freeDebateText != null){
//            freeDebate.setText(freeDebateText);
//        }

        activeBefore.setText("反方四辩");
//        if(activeBeforeText != null){
//            activeBefore.setText(activeBeforeText);
//        }

        audiencePeek.setText("13");
//        if(audiencePeekText != null){
//            audiencePeek.setText(audiencePeekText);
//        }

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

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void setFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
