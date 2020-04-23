package com.example.bigtalk.bigtalk.myspace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ImageLoaderFactory;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

//import cn.bmob.newim.bean.BmobIMUserInfo;


/**
 * 用来显示其他用户个人资料的页面（粉丝、关注的人）
 *
 */

public class UserActivity extends AppCompatActivity {


    TextView user_text_debate_num;
    TextView user_text_fo_num;
    TextView user_text_fan_num;

    ImageView user_img_contest_attended;
    TextView user_text_contest_attended;
    ImageView user_img_contest_attended_getin;

    ImageView user_img_news_released;
    TextView user_text_news_released;
    ImageView user_img_news_released_getin;

    ImageView user_img_collect;
    TextView user_text_collect;
    ImageView user_img_collect_getin;

    //用户
    User stranger,user;
    String userid;
    //用户信息
//    BmobIMUserInfo info;
    String title = "";
    Button addFollow;
    boolean isFollow = false;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        ButterKnife.bind(this);

        user_text_debate_num = (TextView)findViewById(R.id.user_text_debate_num);
        user_text_fo_num = (TextView)findViewById(R.id.user_text_fo_num);
        user_text_fan_num = (TextView)findViewById(R.id.user_text_fan_num);
        user_img_contest_attended = (ImageView)findViewById(R.id.user_img_contest_attended);
        user_text_contest_attended = (TextView)findViewById(R.id.user_text_contest_attended);
        user_img_contest_attended_getin = (ImageView) findViewById(R.id.user_img_contest_attended_getin);
        user_img_news_released = (ImageView) findViewById(R.id.user_img_news_released);
        user_text_news_released = (TextView)findViewById(R.id.user_text_news_released);
        user_img_news_released_getin = (ImageView)findViewById(R.id.user_img_news_released_getin);
        user_img_collect = (ImageView)findViewById(R.id.user_img_collect);
        user_text_collect = (TextView)findViewById(R.id.user_text_collect);
        user_img_collect_getin = (ImageView)findViewById(R.id.user_img_collect_getin);

        toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);


        //导航栏
//        initNaviView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("user_id");
        title = bundle.getString("user_name");

        user = BmobUser.getCurrentUser(User.class);
        //查询传入的user
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(userid, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    stranger = object;
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        addFollow = (Button)findViewById(R.id.user_btn_add_follow);
        showButton();

        addFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFollow == true){
                    //改变button：到未关注
                    //删除一条关注记录
                    BmobRelation relation = new BmobRelation();
                    relation.remove(stranger);
                    user.setFollow(relation);
                    user.update(new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("bmob","关联关系删除成功");
                                addFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style_empty));
                                addFollow.setText("添加关注");
                                addFollow.setTextColor(Color.parseColor("607d8b"));
                            }else{
                                Log.i("bmob","失败："+e.getMessage());
                            }
                        }

                    });

                }else{

                    //加一条关注记录
                    BmobRelation relation = new BmobRelation();
                    relation.add(stranger);

                    user.setFollow(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("bmob","多对多关联添加成功");

                                addFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style_full));
                                addFollow.setText("已关注");
                                addFollow.setTextColor(Color.parseColor("#FFFFFF"));

                            }else{
                                Log.i("bmob","失败："+e.getMessage());
                            }
                        }

                    });

                }
            }
        });

        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
//        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        //加载头像
//        ImageLoaderFactory.getLoader().loadAvator(user_img_avator, stranger.getAvatar(), R.mipmap.head2);


        user_text_debate_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(UserActivity.this, MyAttenedDebateActivity.class);
                intent1.putExtra("user_id",userid);
                startActivity(intent1);
            }
        });

        user_text_fo_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(UserActivity.this, MyFollowActivity.class);
                intent2.putExtra("user_id",userid);
                startActivity(intent2);

            }
        });

        user_text_fan_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(UserActivity.this, MyFanActivity.class);
                intent3.putExtra("user_id",userid);
                startActivity(intent3);
            }
        });
        user_img_contest_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(UserActivity.this, MyAttenedDebateActivity.class);
                intent4.putExtra("user_id",userid);
                startActivity(intent4);
            }
        });
        user_text_contest_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(UserActivity.this, MyAttenedDebateActivity.class);
                intent5.putExtra("user_id",userid);
                startActivity(intent5);
            }
        });
        user_img_contest_attended_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 = new Intent(UserActivity.this, MyAttenedDebateActivity.class);
                intent6.putExtra("user_id",userid);
                startActivity(intent6);
            }
        });
        user_img_news_released.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = new Intent(UserActivity.this, MyReleasedActivity.class);
                intent7.putExtra("user_id",userid);
                startActivity(intent7);
            }
        });
        user_text_news_released.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent8 = new Intent(UserActivity.this, MyReleasedActivity.class);
                intent8.putExtra("user_id",userid);
                startActivity(intent8);
            }
        });
        user_img_news_released_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent9 = new Intent(UserActivity.this, MyReleasedActivity.class);
                intent9.putExtra("user_id",userid);
                startActivity(intent9);
            }
        });
        user_img_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent10 = new Intent(UserActivity.this, MyCollectActivity.class);
                intent10.putExtra("user_id",userid);
                startActivity(intent10);
            }
        });
        user_text_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent11 = new Intent(UserActivity.this, MyCollectActivity.class);
                intent11.putExtra("user_id",userid);
                startActivity(intent11);
            }
        });

        user_img_collect_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent12 = new Intent(UserActivity.this, MyCollectActivity.class);
                intent12.putExtra("user_id",userid);
                startActivity(intent12);
            }
        });




    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }
    //若是关注的用户，显示"已关注"，若是未关注的用户，显示未关注
    public void showButton(){
        BmobQuery<User> followQuery = new BmobQuery<User>();

        followQuery.addWhereRelatedTo("follow", new BmobPointer(user));
        followQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object, BmobException e) {
                if(e==null){
                    Log.i("bmob","查询这个是不是在登陆用户的follow里面的个数："+object.size());

                    for(final User product : object) {
                        if(product == stranger){
                            isFollow = true;
                        }
                    }
                }else{
                    Log.i("bmob","查询follow失败："+e.getMessage());
                }
            }

        });

        if(isFollow == true){
            addFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style_full));
            addFollow.setText("已关注");
            addFollow.setTextColor(Color.parseColor("#FFFFFF"));

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
