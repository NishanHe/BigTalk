package com.example.bigtalk.bigtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bigtalk.bigtalk.contest.DebateFragment;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.FrontFragment;
import com.example.bigtalk.bigtalk.latest.LatestFragment;
import com.example.bigtalk.bigtalk.myspace.MyFragment;
import com.example.bigtalk.bigtalk.train.TrainFragment;

import java.util.ArrayList;
import java.util.List;

public class FrameActivity extends FragmentActivity implements View.OnClickListener{
    private FrameLayout mHomeContent;
    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private RadioButton rbtFront,rbtLatest,rbtDebate,rbtMy;

    private View view_fragment;
    private FrontFragment frontFragment;
    private LatestFragment latestFragment;
    private DebateFragment debateFragment;
    private MyFragment myFragment;
    private Fragment mContent;
    private Button[] mTabs;
    private int currentTabIndex;
    private int index;

    private Fragment mFragmentOne,mFragmentTwo,mFragmentThree,mFragmentFour,mFragmentFive;
    private Fragment  fragmentNow;

    //    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    //用于记录当前显示的Fragment
    private int currentIndex = 0;
    ImageButton front,latest,debate,train,my;
    int shouldGoTrain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);

        Intent intent = getIntent();
        shouldGoTrain = intent.getIntExtra("go_train",0);



        initView();
//        initData();

        initDefaultFragment();
        if(shouldGoTrain > 0){
            android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
            if (mFragmentFour.isAdded()) {
                fragmentTransaction1.hide(fragmentNow).show(mFragmentFour);
            } else {
                fragmentTransaction1.hide(fragmentNow).add(R.id.mHomeContent, mFragmentFour);
                fragmentTransaction1.addToBackStack(null);
            }
            fragmentNow = mFragmentFour;
            fragmentTransaction1.commit();
        }
    }



//    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//        //进行Fragment的初始化k
//        @Override
//        public Fragment getItem(int i) {
//            Fragment fragment = null;
//            switch (i) {
//                case 1://front
//                    fragment = new FrontFragment();
//                    break;
//                case 2://latest
//                    fragment = new LatestFragment();
//                    break;
//                case 3://debate
//                    fragment = new DebateFragment();
//                    break;
//                case 4://my
//                    fragment = new MyFragment();
//                    break;
//                default:
//                    new LatestFragment();
//                    break;
//            }
//            return fragment;
//        }
//    };

    //初始化默认fragment的加载
    private void initDefaultFragment() {

        //开启一个事务
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //add：往碎片集合中添加一个碎片；
        //replace：移除之前所有的碎片，替换新的碎片（remove和add的集合体）(很少用，不推荐，因为是重新加载，所以消耗流量)
        //参数：1.公共父容器的的id  2.fragment的碎片
        fragmentTransaction.add(R.id.mHomeContent, mFragmentOne);
        fragmentTransaction.addToBackStack(null);

        //提交事务
        fragmentTransaction.commit();
        fragmentNow = mFragmentOne;

    }

    protected void initView() {

        mHomeContent = (FrameLayout) findViewById(R.id.mHomeContent); //tab上方的区域

        front = (ImageButton) findViewById(R.id.btn_front);
        latest = (ImageButton) findViewById(R.id.btn_latest);
        debate = (ImageButton) findViewById(R.id.btn_debate);
        train  = (ImageButton) findViewById(R.id.btn_train);
        my = (ImageButton) findViewById(R.id.btn_my);

        front.setOnClickListener(this);
        latest.setOnClickListener(this);
        debate.setOnClickListener(this);
        train.setOnClickListener(this);
        my.setOnClickListener(this);

        mHomeContent = (FrameLayout) findViewById(R.id.mHomeContent);
        //实例化FragmentOne
        mFragmentOne = new FrontFragment();
        mFragmentTwo = new LatestFragment();
        mFragmentThree = new DebateFragment();
        mFragmentFour = new TrainFragment();
        mFragmentFive = new MyFragment();


        //获取碎片管理者
        fragmentManager = getSupportFragmentManager();

        mHomeContent.setOnClickListener(this);

//        front = (Button)findViewById(R.id.btn_front);
//        latest = (Button)findViewById(R.id.btn_latest);
//        debate = (Button)findViewById(R.id.btn_debate);
//        my = (Button)findViewById(R.id.btn_my);
//
//        front.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                index = 0;
//            }
//        });
//
//        latest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                index = 1;
//            }
//        });
//
//        debate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                index = 2;
//            }
//        });
//
//        my.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                index = 3;
//            }
//        });
//
//        if (index == currentIndex) {
//            return;
//        }
//        Fragment baseFragment = fragmentList.get(index);
//        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        //先判断该Fragment是否已经添加到Activity了，如果没有则添加，如果有，则显示
//        if (baseFragment.isAdded()) {
//            //则显示
//            fragmentTransaction.show(baseFragment);
//        } else {
//            //添加
//            fragmentTransaction.add(R.id.mHomeContent, baseFragment, index + "");
//        }
//        //隐藏之前显示的Fragment
//        fragmentTransaction.hide(fragmentList.get(currentIndex));
//        //提交事务
//        fragmentTransaction.commit();
//        currentIndex = index;

        //通过fragments这个adapter还有index来替换帧布局中的内容

//        radioGroup = (RadioGroup)findViewById(R.id.bottomGroup);
//        rbtFront = (RadioButton) findViewById(R.id.radioFront);
//        rbtLatest = (RadioButton) findViewById(R.id.radioLatest);
//        rbtDebate = (RadioButton) findViewById(R.id.radioDebate);
//        rbtMy = (RadioButton) findViewById(R.id.radioMy);
//
//
//        //监听事件：为底部的RadioGroup绑定状态改变的监听事件
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int index = 1;
//                switch (checkedId) {
//                    case R.id.radioFront:
//                        index = 1;
//                        break;
//                    case R.id.radioLatest:
//                        index = 2;
//                        break;
//                    case R.id.radioDebate:
//                        index = 3;
//                        break;
//                    case R.id.radioMy:
//                        index = 4;
//                        break;
//                }
//
//
//                Fragment fragment = (Fragment) fragments.instantiateItem(mHomeContent, index);
//                //一开始将帧布局中 的内容设置为第一个
//                fragments.setPrimaryItem(mHomeContent, 1, fragment);
//                fragments.finishUpdate(mHomeContent);
//            }
//        });
    }
    //通过点击事件跳转到对应的fragment上
    @Override
    public void onClick(View v) {
        android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_front:
                //判断fragmentOne是否已经存在
                if (mFragmentOne.isAdded()) {
                    //如果fragmentOne已经存在，则隐藏当前的fragment，
                    //然后显示fragmentOne（不会重新初始化，只是加载之前隐藏的fragment）
                    fragmentTransaction1.hide(fragmentNow).show(mFragmentOne);
                } else {
                    //如果fragmentOne不存在，则隐藏当前的fragment，
                    //然后添加fragmentOne（此时是初始化）
                    fragmentTransaction1.hide(fragmentNow).add(R.id.mHomeContent, mFragmentOne);
                    fragmentTransaction1.addToBackStack(null);

                }
                fragmentNow = mFragmentOne;
                fragmentTransaction1.commit();
                break;
            case R.id.btn_latest:
                if (mFragmentTwo.isAdded()) {
                    fragmentTransaction1.hide(fragmentNow).show(mFragmentTwo);
                } else {
                    fragmentTransaction1.hide(fragmentNow).add(R.id.mHomeContent, mFragmentTwo);
                    fragmentTransaction1.addToBackStack(null);
                }
                fragmentNow = mFragmentTwo;
                fragmentTransaction1.commit();
                break;
            case R.id.btn_debate:
                if (mFragmentThree.isAdded()) {
                    fragmentTransaction1.hide(fragmentNow).show(mFragmentThree);
                } else {
                    fragmentTransaction1.hide(fragmentNow).add(R.id.mHomeContent, mFragmentThree);
                    fragmentTransaction1.addToBackStack(null);
                }
                fragmentNow = mFragmentThree;
                fragmentTransaction1.commit();
                break;


            case R.id.btn_train:
                if (mFragmentFour.isAdded()) {
                    fragmentTransaction1.hide(fragmentNow).show(mFragmentFour);
                } else {
                    fragmentTransaction1.hide(fragmentNow).add(R.id.mHomeContent, mFragmentFour);
                    fragmentTransaction1.addToBackStack(null);
                }
                fragmentNow = mFragmentFour;
                fragmentTransaction1.commit();
                break;

            case R.id.btn_my:
                if (mFragmentFive.isAdded()) {
                    fragmentTransaction1.hide(fragmentNow).show(mFragmentFive);
                } else {
                    fragmentTransaction1.hide(fragmentNow).add(R.id.mHomeContent, mFragmentFive);
                    fragmentTransaction1.addToBackStack(null);
                }
                fragmentNow = mFragmentFive;
                fragmentTransaction1.commit();
                break;
        }
    }
//    protected void initData() {
//        fragmentManager = getSupportFragmentManager();
//        //初始化三个Fragment
//        //不走任何生命周期方法
//        frontFragment = new FrontFragment();
//        latestFragment = new LatestFragment();
//        debateFragment = new DebateFragment();
//        myFragment = new MyFragment();
//
//        fragmentList.add(frontFragment);
//        fragmentList.add(latestFragment);
//        fragmentList.add(debateFragment);
//        fragmentList.add(myFragment);
//
//        Fragment fragment0 = fragmentManager.findFragmentByTag("0");
//        if (fragment0 != null) {
//            fragmentManager.beginTransaction().remove(fragment0).commit();
//        }
//        Fragment fragment1 = fragmentManager.findFragmentByTag("1");
//        if (fragment1 != null) {
//            fragmentManager.beginTransaction().remove(fragment1).commit();
//        }
//        Fragment fragment2 = fragmentManager.findFragmentByTag("2");
//        if (fragment2 != null) {
//            fragmentManager.beginTransaction().remove(fragment2).commit();
//        }
//
//        //默认让消息Fragment选中
//        fragmentManager.beginTransaction().add(R.id.mHomeContent, frontFragment, "0").commit();
//        currentIndex = 0;
//    }

//    @Override
//    public void onClick(View v) {
//        int index = 0;
//        switch (v.getId()) {
//            case R.id.btn_front:
//                index = 0;
//                break;
//            case R.id.btn_latest:
//                index = 1;
//                break;
//            case R.id.btn_debate:
//                index = 2;
//                break;
//            case R.id.btn_my:
//                index = 3;
//                break;
//        }
//        if (index == currentIndex) {
//            return;
//        }
//        Fragment baseFragment = fragmentList.get(index);
//        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        //先判断该Fragment是否已经添加到Activity了，如果没有则添加，如果有，则显示
//        if (baseFragment.isAdded()) {
//            //则显示
//            fragmentTransaction.show(baseFragment);
//        } else {
//            //添加
//            fragmentTransaction.add(R.id.mHomeContent, baseFragment, index + "");
//        }
//        //隐藏之前显示的Fragment
//        fragmentTransaction.hide(fragmentList.get(currentIndex));
//        //提交事务
//        fragmentTransaction.commit();
//        currentIndex = index;
//    }

    protected void onStart() {
        super.onStart();
//        radioGroup.check(R.id.radioFront);
    }





}
