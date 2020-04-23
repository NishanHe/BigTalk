package com.example.bigtalk.bigtalk.myspace;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Article;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.frontpage.adapter.CollectAdapter;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.CollectFragment;
import com.example.bigtalk.bigtalk.latest.ArticleAdapter;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;
import com.example.bigtalk.bigtalk.latest.bean.Collect;
import com.example.bigtalk.bigtalk.bean.Follow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class MyCollectActivity extends AppCompatActivity {

    private CollectFragment.OnFragmentInteractionListener mListener;

    LinearLayout my_collect_ll;
    RecyclerView my_collect_rcview;

    SwipeRefreshLayout my_collect_swrefresh;
    ArticleAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Article> list;

    String userid,objectid;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);


        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        userid = bundle.getString("user_id");
        BmobQuery<User> query1 = new BmobQuery<User>();

        query1.getObject(userid, new QueryListener<User>(){
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    user = object;
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        list = new ArrayList<>();
        //单一布局
        IMutlipleItem<Article> mutlipleItem = new IMutlipleItem<Article>() {

            @Override
            public int getItemViewType(int position, Article c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_card;
            }

            @Override
            public int getItemCount(List<Article> list) {
                return list.size();
            }
        };

        my_collect_ll = (LinearLayout)findViewById(R.id.my_collect_ll);
        my_collect_rcview = (RecyclerView)findViewById(R.id.my_collect_rcview);
        my_collect_swrefresh = (SwipeRefreshLayout)findViewById(R.id.my_collect_swrefresh);

        adapter = new ArticleAdapter(this,mutlipleItem,null);
        my_collect_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        my_collect_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        my_collect_swrefresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    private List<Article> initNews(){

        BmobQuery<Article> query = new BmobQuery<Article>();


        query.addWhereRelatedTo("collect", new BmobPointer(user));
        query.findObjects(new FindListener<Article>() {

            @Override
            public void done(List<Article> object,BmobException e) {
                if(e==null){
                    Log.i("bmob","收藏查询个数!!!!!!!!!："+object.size());
                    for(Article product : object){
                        list.add(product);
                    }

                }else{
                    Log.i("bmob：MyCollectActivity","查询收藏失败："+e.getMessage());
                }
            }

        });


        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        my_collect_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        my_collect_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                my_collect_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                my_collect_swrefresh.setRefreshing(true);
                query();
            }
        });
        my_collect_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MyCollectActivity.this, DetailsForLatestActivity.class);
                intent.putExtra("news_id",list.get(position).getObjectId());
                intent.putExtra("news_content",list.get(position).getContent());
                intent.putExtra("news_author_id",list.get(position).getNews_author().getObjectId());
                intent.putExtra("type",list.get(position).getType());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("news_time",list.get(position).getNews_time());
                intent.putExtra("news_like",list.get(position).getNews_like());
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(int position) {
// click long to delete new friend
//                FanManager.getInstance(MyFanActivity.this).deleteNewFriend(adapter.getItem(position));
//                adapter.remove(position);

                return true;
            }
        });
    }
}
