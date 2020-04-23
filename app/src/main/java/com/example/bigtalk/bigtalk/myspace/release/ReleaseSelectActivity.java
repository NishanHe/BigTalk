package com.example.bigtalk.bigtalk.myspace.release;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReleaseSelectActivity extends AppCompatActivity {

    Button release_btn_news;
    Button release_btn_topic;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_select);

        release_btn_news = (Button)findViewById(R.id.release_btn_news);
        release_btn_topic = (Button)findViewById(R.id.release_btn_topic);

        toolbar = (Toolbar) findViewById(R.id.ing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        release_btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ReleaseSelectActivity.this, ReleaseNewsActivity.class);
                startActivity(intent1);
            }
        });

        release_btn_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ReleaseSelectActivity.this, ReleaseTopicActivity.class);
                startActivity(intent2);
            }
        });

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle("发布内容");
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
