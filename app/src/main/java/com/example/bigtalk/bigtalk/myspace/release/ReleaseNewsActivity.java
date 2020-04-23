package com.example.bigtalk.bigtalk.myspace.release;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.bean.Article;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ReleaseNewsActivity extends AppCompatActivity {


    EditText release_news_edit_title;
    EditText release_news_edit_content;

    Button release_news_btn_confirm;

    RadioButton release_news_rb_currenEvent;
    RadioButton release_news_rb_debateTechnique;
    RadioButton release_news_rb_userNews;

    RadioGroup release_news_rg;

    String newsTitle,newsContent,newsType;
    User user;
    Date news_time;
    Article usernews;
    Context context;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_news);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        ButterKnife.bind(this);
        release_news_edit_title = (EditText)findViewById(R.id.release_news_edit_title);
        release_news_edit_content = (EditText)findViewById(R.id.release_news_edit_content);

        release_news_rb_debateTechnique = (RadioButton)findViewById(R.id.release_news_rb_debateTechnique);
        release_news_rb_userNews = (RadioButton)findViewById(R.id.release_news_rb_userNews);
        release_news_rg = (RadioGroup) findViewById(R.id.release_news_rg);
        release_news_btn_confirm = (Button)findViewById(R.id.release_news_btn_confirm);

        release_news_rg.setOnCheckedChangeListener(new MyRadioButtonListener() );//注意是给RadioGroup绑定监视器

        toolbar = (Toolbar) findViewById(R.id.release_news_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        release_news_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsTitle = release_news_edit_title.getText().toString();
                newsContent = release_news_edit_content.getText().toString();

                user = BmobUser.getCurrentUser( User.class);

                news_time = new Date(System.currentTimeMillis());

                usernews = new Article();
                usernews.setTitle(newsTitle);
                usernews.setContent(newsContent);
                usernews.setNews_author(user);
                usernews.setNews_time(news_time);
                usernews.setType(newsType);
                usernews.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
                            Log.i("RealeaseNews", "用户添加News成功！！！");

//                            AlertDialog dialog = new AlertDialog.Builder(context)
////                            .setIcon(R.mipmap.icon)//设置标题的图片
//                                    .setTitle("提交成功！")//设置对话框的标题
//                                    .setMessage("")//设置对话框的内容
//                                    //设置对话框的按钮
//                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(ReleaseNewsActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
//
//                                            dialog.dismiss();
//                                            finish();
//                                        }
//                                    }).create();
//                            dialog.show();

                            Toast.makeText(ReleaseNewsActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReleaseNewsActivity.this, FrameActivity.class);
                            startActivity(intent);

                        }else{
                            Log.i("RealeaseNews", "用户添加News失败。。。。");

                        }
                    }
                });
            }
        });


    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle("发布文章");
        }
    }
    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.release_news_rb_debateTechnique:
                    Log.i("RealeaseNews", "当前用户选择" + release_news_rb_debateTechnique.getText().toString());
                    newsType = release_news_rb_debateTechnique.getText().toString();
                    break;
                case R.id.release_news_rb_userNews:
                    Log.i("RealeaseNews", "当前用户选择" + release_news_rb_userNews.getText().toString());
                    newsType = release_news_rb_userNews.getText().toString();
                    break;
            }
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
