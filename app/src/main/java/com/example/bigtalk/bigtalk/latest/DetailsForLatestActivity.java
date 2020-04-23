package com.example.bigtalk.bigtalk.latest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ImageLoaderFactory;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.bean.Article;
import com.example.bigtalk.bigtalk.latest.bean.Collect;
import com.example.bigtalk.bigtalk.myspace.UserActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsForLatestActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    String newsid,authorid,authorname,content,title,type;
    Integer newslike;
    String newstime;
    Article article;
    User user,author;

//    TextView textTitle;
    TextView textAuthor;
    TextView textContent;
    TextView textTime;
    BottomNavigationBar main_bottom_navigation;
    CircleImageView news_img_avatar;
    boolean isLiked = false;

    BottomNavigationItem likeItem, commentItem,collectItem;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_for_latest);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        newsid = bundle.getString("news_id");
        authorid = bundle.getString("news_author_id");
        content = bundle.getString("news_content");
        title = bundle.getString("title");
        type = bundle.getString("type");
        authorname = bundle.getString("news_author");
        newslike = bundle.getInt("news_like");
        newstime = bundle.getString("news_time");

        textAuthor = (TextView)findViewById(R.id.latest_details_text_author);
        textContent = (TextView)findViewById(R.id.detail_content);
        textTime = (TextView)findViewById(R.id.latest_details_text_time);
//        textTitle = (TextView)findViewById(R.id.detail_title);

        main_bottom_navigation = (BottomNavigationBar)findViewById(R.id.main_bottom_navigation);
        news_img_avatar = (CircleImageView)findViewById(R.id.news_img_avatar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(authorid, new QueryListener<User>() {

            @Override
            public void done(User a, BmobException e) {
                if(e==null){
                    //获得数据的objectId信息
                    a.getObjectId();
                    a.getUsername();
                    author = a;

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        if(author != null && authorname != null && !authorname.equals("")){
            textAuthor.setText(authorname);
        }

        textAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsForLatestActivity.this, UserActivity.class);
                intent.putExtra("user_id",authorid);
                startActivity(intent);
            }
        });


        if(newstime != null && !newstime.equals("")){
//            newstime = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//            textTime.setText(sdf.format(newstime));
            textTime.setText(newstime);
        }

        if(content != null && !content.equals("")){
            textContent.setText(content);
        }

        textContent.setMovementMethod(ScrollingMovementMethod.getInstance());


//        if(title != null && !title.equals("")){
//            textTitle.setText(title);
//        }




//        ImageLoaderFactory.getLoader().loadAvator(news_img_avatar, author.getAvatar(), R.mipmap.head2);
        initBottomNavBar();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    private void initBottomNavBar(){

        main_bottom_navigation.setAutoHideEnabled(true);//自动隐藏

//        BottomNavigationBar.MODE_SHIFTING;
        //BottomNavigationBar.MODE_FIXED;
        //BottomNavigationBar.MODE_DEFAULT;
        main_bottom_navigation.setMode(BottomNavigationBar.MODE_SHIFTING);

        // BottomNavigationBar.BACKGROUND_STYLE_DEFAULT;
        // BottomNavigationBar.BACKGROUND_STYLE_RIPPLE
        // BottomNavigationBar.BACKGROUND_STYLE_STATIC
        main_bottom_navigation.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        main_bottom_navigation.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        main_bottom_navigation.setBarBackgroundColor(R.color.base_color_text_white);//背景颜色
        main_bottom_navigation.setInActiveColor(R.color.color_navi_origin);//未选中时的颜色
        main_bottom_navigation.setActiveColor(R.color.colorPrimaryDark);//选中时的颜色

        main_bottom_navigation.addItem(new BottomNavigationItem(
                R.mipmap.like_n,R.string.like)
                .setInactiveIconResource(R.mipmap.like_n)
                .setActiveColorResource(R.color.grey))
                .addItem(new BottomNavigationItem(
                        R.mipmap.collect_n,R.string.collect)
                        .setInactiveIconResource(R.mipmap.collect_n)
                        .setActiveColorResource(R.color.grey))
                .addItem(new BottomNavigationItem(
                        R.mipmap.comment,R.string.comment)
                        .setInactiveIconResource(R.mipmap.comment)
                        .setActiveColorResource(R.color.grey))
                .setFirstSelectedPosition(0)//默认显示面板
                .initialise();//初始化

//        main_bottom_navigation.addItem(likeItem).addItem(collectItem).addItem(commentItem).initialise();
//        main_bottom_navigation.initialise();
        main_bottom_navigation.setTabSelectedListener(this);

    }


    /*底部NaV监听*/
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:   //like this news
                if(isLiked == false){
                    newslike = newslike + 1;
                    isLiked = true;
                    article = new Article();
                    article.setNews_like(newslike);
                    article.update(newsid, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("DetailActivity","点赞成功：" + e.getMessage());
                            }else{
                                Log.i("DetailActivity","点赞失败：" + e.getMessage());
                            }
                        }

                    });
                }else{
                    newslike = newslike - 1;
                    isLiked = false;
                    article = new Article();
                    article.setNews_like(newslike);
                    article.update(newsid, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("DetailActivity","取消点赞成功：" + e.getMessage());
                            }else{
                                Log.i("DetailActivity","取消点赞失败：" + e.getMessage());
                            }
                        }

                    });
                }




                break;
            case 1: //collect this piece of news
                user =  BmobUser.getCurrentUser( User.class);
                Article article1 = new Article();
                article1.setObjectId(newsid);
                BmobRelation relation = new BmobRelation();

                relation.add(article1);
                user.setCollect(relation);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","收藏成功！");
                            System.out.println("收藏成功！");

                        }else{
                            Log.i("bmob","失败："+e.getMessage());
                        }
                    }

                });

                break;
            case 2:  //comment
                //暂时没有comment页
//                Intent intent = new Intent(DetailsForLatestActivity.this, CommentActivity.class);
//                intent.putExtra("news_id",newsid);
//                startActivity(intent);
                break;
        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

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
