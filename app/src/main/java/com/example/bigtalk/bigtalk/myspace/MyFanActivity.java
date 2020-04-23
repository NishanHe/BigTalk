package com.example.bigtalk.bigtalk.myspace;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.myspace.adapter.FanAdapter;
import com.example.bigtalk.bigtalk.myspace.bean.Fan;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class MyFanActivity extends ParentWithNaviActivity {

    LinearLayout my_fan_ll_root;
    RecyclerView my_fan_rcview;
    SwipeRefreshLayout my_fan_swrefresh;

    FanAdapter adapter;
    LinearLayoutManager layoutManager;
    List<User> list;
    User user;
    String userid;

    @Override
    protected String title() {
        return "我的粉丝";
    }

    @Override
    public Object left() {
        return R.drawable.base_action_bar_back_bg_selector;
    }



    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {

            }

            @Override
            public void clickRight2() {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fan);
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
        initNaviView();
        //单一布局
        IMutlipleItem<User> mutlipleItem = new IMutlipleItem<User>() {

            @Override
            public int getItemViewType(int position, User c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_fan;
            }

            @Override
            public int getItemCount(List<User> list) {
                return list.size();
            }
        };
        adapter = new FanAdapter(this,mutlipleItem,null);


        my_fan_ll_root = (LinearLayout)findViewById(R.id.my_fan_ll_root);
        my_fan_rcview = (RecyclerView)findViewById(R.id.my_fan_rcview);
        my_fan_swrefresh = (SwipeRefreshLayout)findViewById(R.id.my_fan_swrefresh);

        my_fan_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        my_fan_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();

    }


    @Override
    public void onResume() {
        super.onResume();
        my_fan_swrefresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    private List<User> initFan(){

        String loginId = userid;
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }


        return list;
    }
    public void query(){
        initFan();

        adapter.bindDatas(list);   //adapter绑定  当前用户粉丝<Fan>
        adapter.notifyDataSetChanged();
        my_fan_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        my_fan_ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                my_fan_ll_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                my_fan_swrefresh.setRefreshing(true);
                query();
            }
        });
        my_fan_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(UserActivity.class, null);
//                log("点击："+position);

            }

            @Override
            public boolean onItemLongClick(int position) {
// click long to delete new friend
//                FanManager.getInstance(MyFanActivity.this).deleteNewFriend(adapter.getItem(position));
//                adapter.remove(position);
                Intent intent = new Intent(MyFanActivity.this, UserActivity.class);
                intent.putExtra("user_id",list.get(position).getObjectId());
                intent.putExtra("user_name",list.get(position).getUsername());
                startActivity(intent);
                return true;
            }
        });
    }
}
