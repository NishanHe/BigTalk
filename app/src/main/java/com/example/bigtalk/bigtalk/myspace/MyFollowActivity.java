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
import android.widget.LinearLayout;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.myspace.adapter.FanAdapter;
import com.example.bigtalk.bigtalk.myspace.adapter.FollowAdapter;
import com.example.bigtalk.bigtalk.myspace.bean.Fan;
import com.example.bigtalk.bigtalk.bean.Follow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class MyFollowActivity extends AppCompatActivity {

    @Bind(R.id.my_follow_ll_root)
    LinearLayout my_follow_ll_root;
    @Bind(R.id.my_follow_rcview)
    RecyclerView my_follow_rcview;
    @Bind(R.id.my_follow_swrefresh)
    SwipeRefreshLayout my_follow_swrefresh;
    FollowAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<User> list;
    User user;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");
        list = new ArrayList<>();


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
        //单一布局
        IMutlipleItem<User> mutlipleItem = new IMutlipleItem<User>() {

            @Override
            public int getItemViewType(int position, User c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_follow;
            }

            @Override
            public int getItemCount(List<User> list) {
                return list.size();
            }
        };
        adapter = new FollowAdapter(this,mutlipleItem,null);
        my_follow_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        my_follow_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();

    }


    @Override
    public void onResume() {
        super.onResume();
        my_follow_swrefresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    private List<User> initFollow(){


        String loginId = userid;
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }


        BmobQuery<User> followQuery = new BmobQuery<User>();

        followQuery.addWhereRelatedTo("follow", new BmobPointer(user));
        followQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object,BmobException e) {
                if(e==null){
                    Log.i("bmob","MyFollowActivity查询关注的人个数："+object.size());
                    for(final User product : object) {

                        list.add(product);
                    }
                }else{
                    Log.i("bmob","MyFollowActivity查询关注失败："+e.getMessage());
                }
            }

        });


        return list;
    }
    public void query(){
        initFollow();
        adapter.bindDatas(list);   //adapter绑定  当前用户粉丝<Fan>
        adapter.notifyDataSetChanged();
        my_follow_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        my_follow_ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                my_follow_ll_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                my_follow_swrefresh.setRefreshing(true);
                query();
            }
        });
        my_follow_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                //设置访问用户个人资料页：怎样访问到其他用户的资料页？？？---进入到一个新的activity(UserActivity)...
                // ...显示用户资料（里面的信息获取为点击的用户的资料）
                Intent intent = new Intent(MyFollowActivity.this, UserActivity.class);
                intent.putExtra("user_id",list.get(position).getObjectId());
                intent.putExtra("user_name",list.get(position).getUsername());
                startActivity(intent);
//                log("点击："+position);

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
