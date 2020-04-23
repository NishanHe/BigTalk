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
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.frontpage.adapter.ContestAdapter;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;
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

public class MyAttenedDebateActivity extends AppCompatActivity {


    LinearLayout my_contest_attended_ll;

    RecyclerView my_contest_attended_rcview;
    SwipeRefreshLayout my_contest_attended_swrefresh;


    ContestAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Contest> list;
    String userid;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attened_debate);

        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //通过intent获得当前传入的当前用户，或者是其他用户（fan，follow）
        userid = bundle.getString("user_id");
        BmobQuery<User> query1 = new BmobQuery<User>();
        //再通过fan_id在User表中查询user_name,avatar
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
        IMutlipleItem<Contest> mutlipleItem = new IMutlipleItem<Contest>() {

            @Override
            public int getItemViewType(int position, Contest c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_card;
            }

            @Override
            public int getItemCount(List<Contest> list) {
                return list.size();
            }
        };

        my_contest_attended_ll = (LinearLayout)findViewById(R.id.my_contest_attended_ll);
        my_contest_attended_rcview = (RecyclerView)findViewById(R.id.my_contest_attended_rcview);
        my_contest_attended_swrefresh = (SwipeRefreshLayout)findViewById(R.id.my_contest_attended_swrefresh);

        adapter = new ContestAdapter(this,mutlipleItem,null);
        my_contest_attended_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        my_contest_attended_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();

    }
    @Override
    public void onResume() {
        super.onResume();
        my_contest_attended_swrefresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    private List<Contest> initNews(){
        BmobQuery<Contest> queryDebater = new BmobQuery<>();
        List<BmobQuery<Contest>> and = new ArrayList<BmobQuery<Contest>>();

        BmobQuery<Contest> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("nega_1", user);
        and.add(q1);
        BmobQuery<Contest> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("nega_2", user);
        and.add(q2);
        BmobQuery<Contest> q3 = new BmobQuery<>();
        q3.addWhereEqualTo("nega_3", user);
        and.add(q3);
        BmobQuery<Contest> q4 = new BmobQuery<>();
        q4.addWhereEqualTo("nega_4", user);
        and.add(q4);
        BmobQuery<Contest> q5 = new BmobQuery<>();
        q5.addWhereEqualTo("posi_1", user);
        and.add(q5);
        BmobQuery<Contest> q6 = new BmobQuery<>();
        q6.addWhereEqualTo("posi_2", user);
        and.add(q6);
        BmobQuery<Contest> q7 = new BmobQuery<>();
        q7.addWhereEqualTo("posi_3", user);
        and.add(q7);
        BmobQuery<Contest> q8 = new BmobQuery<>();
        q8.addWhereEqualTo("posi_4", user);
        and.add(q8);
        BmobQuery<Contest> orQuery = queryDebater.or(and);
        orQuery.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> object, BmobException e) {
                if(e==null){
                    Log.i("bmob","MyAttendedDebateActivity查询比赛成功："+e.getMessage()+","+e.getErrorCode());
                    System.out.println("查到用户参与的所有比赛啦！！！！");
                    for(final Contest product:object){
                        list.add(product);
                    }


                }else{
                    Log.i("bmob","MyAttendedDebateActivity查询比赛失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        my_contest_attended_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        my_contest_attended_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                my_contest_attended_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                my_contest_attended_swrefresh.setRefreshing(true);
                query();
            }
        });
        my_contest_attended_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if(list.get(position).getTopic().getTopic_status() == "waitForU"){
                    Toast.makeText(MyAttenedDebateActivity.this, "比赛尚未开始，，请耐心等待", Toast.LENGTH_SHORT).show();
                }
//                else if(list.get(position).getTopic().getTopic_status() == "onFire"){
//                    Intent intent = new Intent(MyAttenedDebateActivity.this, ContestINGActivity.class);
//
//                    intent.putExtra("contest_id",list.get(position).getObjectId());
//                    intent.putExtra("userRole","broadcaster");
//                }else if(list.get(position).getTopic().getTopic_status() == "end"){
//                    Intent intent = new Intent(MyAttenedDebateActivity.this, ContestPlayActivity.class);
//
//                    intent.putExtra("contest_id",list.get(position).getObjectId());
//                }
//
//



            }

            @Override
            public boolean onItemLongClick(int position) {

                return true;
            }
        });
    }
}
