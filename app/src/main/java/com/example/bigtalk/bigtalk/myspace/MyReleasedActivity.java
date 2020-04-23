package com.example.bigtalk.bigtalk.myspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.CollectFragment;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.ContestAttendedFragment;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.NewsReleasedFragment;
import com.example.bigtalk.bigtalk.myspace.myReleasedFragments.MyReleasedNewsFragment;
import com.example.bigtalk.bigtalk.myspace.myReleasedFragments.MyReleasedTopicsFragment;

import cn.bmob.v3.Bmob;

public class MyReleasedActivity extends AppCompatActivity {

    String userid;
    User user;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    private LinearLayout mHomeContent;
    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbtMyNews,rbtMyTopic;
    private MyReleasedNewsFragment newsFragment;
    private MyReleasedTopicsFragment topicsFragment;
    private Fragment mContent;
    private Button[] mTabs;
    private int currentTabIndex;
    private int index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_released);

        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //通过intent获得当前传入的当前用户，或者是其他用户（fan，follow）
        userid = bundle.getString("user_id");
        mSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        editor = mSharedPreferences.edit();
        editor.putString("user_id", userid);
        editor.apply();

        initView();

    }
    protected void initView() {
        mHomeContent = (LinearLayout) findViewById(R.id.my_released_fragment_container); //tab上方的区域
        radioGroup = (RadioGroup)findViewById(R.id.my_released_rd_group);
        rbtMyNews = (RadioButton) findViewById(R.id.my_released_rd_news);
        rbtMyTopic = (RadioButton) findViewById(R.id.my_released_rd_topics);


        //监听事件：为底部的RadioGroup绑定状态改变的监听事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.my_released_rd_news:
                        index = 0;
                        break;
                    case R.id.my_released_rd_topics:
                        index = 1;
                        break;

                }
                //通过fragments这个adapter还有index来替换帧布局中的内容
                Fragment fragment = (Fragment) fragments.instantiateItem(mHomeContent, index);
                //一开始将帧布局中 的内容设置为第一个
                fragments.setPrimaryItem(mHomeContent, 0, fragment);
                fragments.finishUpdate(mHomeContent);
            }
        });


    }
//    private void initFragment(){
//        newsFragment = new MyReleasedNewsFragment();
//        topicsFragment = new MyReleasedTopicsFragment();
//
//        FragmentTransaction beginTransaction = getFragmentManager()
//                .beginTransaction();
//        beginTransaction
//                .add(R.id.my_released_fragment_container, newsFragment);
//        beginTransaction.commitAllowingStateLoss();
//        mContent = newsFragment;
//        currentTabIndex = 0;
//        mTabs[currentTabIndex].setSelected(true);
//    }
//
//    @Override
//    public void onClick(View v){
//        switch (v.getId()) {
//            case R.id.my_released_btn_news:
//                index = 0;
//                if (null == newsFragment) {
//                    newsFragment = new MyReleasedNewsFragment();
//                }
//                switchContent(newsFragment);
//                break;
//
//            case R.id.my_released_btn_topics:
//                index = 1;
//                if (null == topicsFragment) {
//                    topicsFragment = new MyReleasedTopicsFragment();
//                }
//                switchContent(topicsFragment);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//
//    /** 修改显示的内容 不会重新加载 **/
//    public void switchContent(Fragment to) {
//        try {
//            if (mContent != to) {
//                FragmentTransaction transaction = getFragmentManager()
//                        .beginTransaction();
//                if (!to.isAdded()) { // 先判断是否被add过
//                    transaction.hide(mContent)
//                            .add(R.id.my_released_fragment_container, to)
//                            .commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
//                } else {
//                    transaction.hide(mContent).show(to)
//                            .commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
//                }
//                mContent = to;
//            }
//            mTabs[currentTabIndex].setSelected(false);
//            // 把当前tab设为选中状态
//            mTabs[index].setSelected(true);
//            currentTabIndex = index;
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
    protected void onStart() {
        super.onStart();
        radioGroup.check(R.id.my_released_rd_group);
    }

    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 2;
        }

        //进行Fragment的初始化
        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new MyReleasedNewsFragment();
                    break;
                case 1://最新
                    fragment = new MyReleasedTopicsFragment();
                    break;

                default:
                    new MyReleasedNewsFragment();
                    break;
            }
            return fragment;
        }
    };
}
