package com.example.bigtalk.bigtalk.frontpage.fourFragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.frontpage.bean.NewsReleased;
import com.example.bigtalk.bigtalk.myspace.release.ReleaseNewsActivity;
import com.example.bigtalk.bigtalk.myspace.release.ReleaseSelectActivity;
import com.example.bigtalk.bigtalk.myspace.release.ReleaseTopicActivity;
import com.example.bigtalk.bigtalk.train.TrainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrontFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrontFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    SearchView front_search;

    FloatingActionButton front_floating;
    private OnFragmentInteractionListener mListener;

    private FrameLayout mHomeContent;
    //    private Fragment mFragments[];
//    private RadioGroup radioGroup;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private RadioButton rbtContestReport,rbtDebateTechnique;
    View view;
    private ContestAttendedFragment contestAttendedFragment;
    private NewsReleasedFragment newsReleasedFragment;
    private CollectFragment collectFragment;

    private Fragment mContent;
    private Button[] mTabs;
    private int currentTabIndex;
    private int index;
    private TrainFragment trainFragment;
    private FragmentTransaction transaction;
    private boolean isAdd = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01,R.id.miniFab02};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];
    private AnimatorSet addBillTranslate1;
    private AnimatorSet addBillTranslate2;

    public FrontFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LatestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontFragment newInstance(String param1, String param2) {
        FrontFragment fragment = new FrontFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Intent intert = getActivity().getIntent();
        int id = intert.getIntExtra("grxx",-1);
        if(id > 0){
            System.out.println("aaa"+id);
            if(id==1){
                transaction.replace(R.id.mHomeContent, trainFragment); //这里是指定跳转到指定的fragment
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bmob.initialize(getContext(), "4ab85f97c2bc54d88133be3de0df4131");
        view = inflater.inflate(R.layout.fragment_front, container, false);
        ButterKnife.bind(getActivity(), view);

        initView();

//        front_search = (SearchView)view.findViewById(R.id.front_search);

        front_floating = (FloatingActionButton)view.findViewById(R.id.front_floating);
//        latest_floating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), ReleaseSelectActivity.class);
//                startActivity(intent);
//            }
//        });


        //set searchview
//        front_search.onActionViewExpanded();
//        //设置最大宽度
//        front_search.setMaxWidth(500);
//        //设置是否显示搜索框展开时的提交按钮
//        front_search.setSubmitButtonEnabled(true);
//        //设置输入框提示语
//        front_search.setQueryHint("输入想要查找的内容");
//        //搜索框展开时后面叉叉按钮的点击事件
//        front_search.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Toast.makeText(getActivity().getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//        //搜索图标按钮(打开搜索框的按钮)的点击事件
//        front_search.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //搜索框文字变化监听
//        front_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Log.e("CSDN_LQR", "TextSubmit : " + s);
////                Intent intent = new Intent(getActivity()
////                        .getApplicationContext(), SearchActivity.class);
////
////                intent.putExtra("search",s);
////                startActivity(intent);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Log.e("CSDN_LQR", "TextChange --> " + s);
//                return false;
//            }
//        });

        initFragment();
        setDefaultValues();
        bindEvents();

        return view;

    }

    protected void initView() {

        mTabs = new Button[2];
        mTabs[0] = (Button) view
                .findViewById(R.id.front_btn_news_released);
//        mTabs[1] = (Button) view
//                .findViewById(R.id.front_btn_contest_attended);
        mTabs[1] = (Button) view
                .findViewById(R.id.front_btn_collect);

        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
//        mTabs[2].setOnClickListener(this);

        mHomeContent = (FrameLayout) view.findViewById(R.id.front_fragment_container); //tab上方的区域
//        radioGroup = (RadioGroup)view.findViewById(R.id.latest_rd_group);
//        rbtContestReport = (RadioButton) view.findViewById(R.id.latest_rd_contest_report);
//        rbtDebateTechnique = (RadioButton) view.findViewById(R.id.latest_rd_debate_technique);
//
//
//        //监听事件：为底部的RadioGroup绑定状态改变的监听事件
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int index = 0;
//                switch (checkedId) {
//                    case R.id.latest_rd_contest_report:
//                        index = 0;
//                        break;
//                    case R.id.latest_rd_debate_technique:
//                        index = 1;
//                        break;
//
//                }
//                //通过fragments这个adapter还有index来替换帧布局中的内容
//                Fragment fragment = (Fragment) fragments.instantiateItem(mHomeContent, index);
//                //一开始将帧布局中 的内容设置为第一个
//                fragments.setPrimaryItem(mHomeContent, 0, fragment);
//                fragments.finishUpdate(mHomeContent);
//            }
//        });
        rlAddBill = (RelativeLayout)view.findViewById(R.id.rlAddBill);
        for (int i = 0; i < llId.length;i++){
            ll[i] = (LinearLayout)view.findViewById(llId[i]);
        }
        for (int i = 0;i < fabId.length; i++){
            fab[i] = (FloatingActionButton)view.findViewById(fabId[i]);
        }

    }
    private void setDefaultValues(){
        addBillTranslate1 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),R.animator.add_bill_anim);
        addBillTranslate2 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),R.animator.add_bill_anim);

    }
    private void bindEvents(){
        front_floating.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }
    private void initFragment(){
//        contestAttendedFragment = new ContestAttendedFragment();
        newsReleasedFragment = new NewsReleasedFragment();
        collectFragment = new CollectFragment();

        FragmentTransaction beginTransaction = getFragmentManager()
                .beginTransaction();
        beginTransaction
                .add(R.id.front_fragment_container, newsReleasedFragment);
        beginTransaction.commitAllowingStateLoss();
        mContent = newsReleasedFragment;
        currentTabIndex = 0;
        mTabs[currentTabIndex].setSelected(true);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.front_btn_news_released:
                index = 0;
                if (null == newsReleasedFragment) {
                    newsReleasedFragment = new NewsReleasedFragment();
                }
                switchContent(newsReleasedFragment);
                break;
//            case R.id.front_btn_contest_attended:
//                index = 1;
//                if (null == contestAttendedFragment) {
//                    contestAttendedFragment = new ContestAttendedFragment();
//                }
//                switchContent(contestAttendedFragment);
//                break;

            case R.id.front_btn_collect:
                index = 1;
                if (null == collectFragment) {
                    collectFragment = new CollectFragment();
                }
                switchContent(collectFragment);
                break;

            case R.id.front_floating:
                front_floating.setImageResource(isAdd ? R.drawable.plus:R.mipmap.close);
                isAdd = !isAdd;
                rlAddBill.setVisibility(isAdd ? View.VISIBLE : View.GONE);
                if (isAdd) {
                    addBillTranslate1.setTarget(ll[0]);
                    addBillTranslate1.start();
                    addBillTranslate2.setTarget(ll[1]);
                    addBillTranslate2.start();

                }
                break;
            case R.id.miniFab01:
                hideFABMenu();
                Intent intent1 = new Intent(getActivity(), ReleaseNewsActivity.class);
                startActivity(intent1);
                break;
            case R.id.miniFab02:

                hideFABMenu();
                Intent intent2 = new Intent(getActivity(), ReleaseTopicActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }

    /** 修改显示的内容 不会重新加载 **/
    public void switchContent(Fragment to) {
        try {
            if (mContent != to) {
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(mContent)
                            .add(R.id.front_fragment_container, to)
                            .commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(mContent).show(to)
                            .commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
                }
                mContent = to;
            }
            mTabs[currentTabIndex].setSelected(false);
            // 把当前tab设为选中状态
            mTabs[index].setSelected(true);
            currentTabIndex = index;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void hideFABMenu(){
        rlAddBill.setVisibility(View.GONE);
        front_floating.setImageResource(R.drawable.plus);
        isAdd = false;
    }

    public void onStart() {
        super.onStart();
//        radioGroup.check(R.id.latest_rd_contest_report);

    }

//    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(fragmentManager) {
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        //进行Fragment的初始化
//        @Override
//        public Fragment getItem(int i) {
//            Fragment fragment = null;
//            switch (i) {
//                case 0: //比赛整理
//                    fragment = new ContestReportFragment();
//                    break;
//                case 1://辩论技巧
//                    fragment = new DebateTechniqueFragment();
//                    break;
//                default:
//                    new ContestReportFragment();
//                    break;
//            }
//            return fragment;
//        }
//    };



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    /*
* onAttach(Context) is not called on pre API 23 versions of Android and onAttach(Activity) is deprecated
* Use onAttachToContext instead
*/
//    @TargetApi(23)
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        onAttachToContext(context);
//    }
//
//    /*
//     * Deprecated on API 23
//     * Use onAttachToContext instead
//     */
//    @SuppressWarnings("deprecation")
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            onAttachToContext(activity);
//        }
//    }
//
//    /*
//     * Called when the fragment attaches to the context
//     */
//    protected void onAttachToContext(Context context) {
//        //do something
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
