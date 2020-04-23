package com.example.bigtalk.bigtalk.latest;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.CollectFragment;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.ContestAttendedFragment;
import com.example.bigtalk.bigtalk.frontpage.fourFragments.NewsReleasedFragment;
import com.example.bigtalk.bigtalk.latest.fourFragments.ContestReportFragment;
import com.example.bigtalk.bigtalk.latest.fourFragments.CrazyFragment;
import com.example.bigtalk.bigtalk.latest.fourFragments.CurrentEventFragment;
import com.example.bigtalk.bigtalk.latest.fourFragments.DebateTechniqueFragment;
import com.example.bigtalk.bigtalk.latest.fourFragments.NewTopicFragment;
import com.example.bigtalk.bigtalk.myspace.release.ReleaseNewsActivity;
import com.example.bigtalk.bigtalk.myspace.release.ReleaseSelectActivity;
import com.example.bigtalk.bigtalk.myspace.release.ReleaseTopicActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LatestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LatestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LatestFragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    SearchView latest_search;

    FloatingActionButton latest_floating;
    private OnFragmentInteractionListener mListener;

    private FrameLayout mHomeContent;
//    private Fragment mFragments[];
//    private RadioGroup radioGroup;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private RadioButton rbtContestReport,rbtDebateTechnique;
    View view;
    private ContestReportFragment contestReportFragment;
    //private CrazyFragment crazyFragment;
    private DebateTechniqueFragment debateTechniqueFragment;
    private Fragment mContent;
    private Button[] mTabs;
    private int currentTabIndex;
    private int index;

    private boolean isAdd = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.latest_ll01,R.id.latest_ll02};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.latest_miniFab01,R.id.latest_miniFab02};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];
    private AnimatorSet addBillTranslate1;
    private AnimatorSet addBillTranslate2;

    public LatestFragment() {
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
    public static LatestFragment newInstance(String param1, String param2) {
        LatestFragment fragment = new LatestFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bmob.initialize(getContext(), "4ab85f97c2bc54d88133be3de0df4131");
        view = inflater.inflate(R.layout.fragment_latest, container, false);
        ButterKnife.bind(getActivity(), view);
        initView();


//        latest_search = (SearchView)view.findViewById(R.id.latest_search);

        latest_floating = (FloatingActionButton)view.findViewById(R.id.latest_floating);
//        latest_floating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), ReleaseSelectActivity.class);
//                startActivity(intent);
//            }
//        });


        //set searchview
//        latest_search.onActionViewExpanded();
//        //设置最大宽度
//        latest_search.setMaxWidth(500);
//        //设置是否显示搜索框展开时的提交按钮
//        latest_search.setSubmitButtonEnabled(true);
//        //设置输入框提示语
//        latest_search.setQueryHint("输入想要查找的内容");
//        //搜索框展开时后面叉叉按钮的点击事件
//        latest_search.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Toast.makeText(getActivity().getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//        //搜索图标按钮(打开搜索框的按钮)的点击事件
//        latest_search.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //搜索框文字变化监听
//        latest_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                .findViewById(R.id.latest_btn_contest_report);
        mTabs[1] = (Button) view
                .findViewById(R.id.latest_btn_debate_technique);

        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);

        mHomeContent = (FrameLayout) view.findViewById(R.id.latest_fragment_container); //tab上方的区域
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
        rlAddBill = (RelativeLayout)view.findViewById(R.id.latest_rlAddBill);
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
        latest_floating.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }
    private void initFragment(){
        contestReportFragment = new ContestReportFragment();
        debateTechniqueFragment = new DebateTechniqueFragment();

        FragmentTransaction beginTransaction = getFragmentManager()
                .beginTransaction();
        beginTransaction
                .add(R.id.latest_fragment_container, contestReportFragment);
        beginTransaction.commitAllowingStateLoss();
        mContent = contestReportFragment;
        currentTabIndex = 0;
        mTabs[currentTabIndex].setSelected(true);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.latest_btn_contest_report:
                index = 0;
                if (null == contestReportFragment) {
                    contestReportFragment = new ContestReportFragment();
                }
                switchContent(contestReportFragment);
                break;
            case R.id.latest_btn_debate_technique:
                index = 1;
                if (null == debateTechniqueFragment) {
                    debateTechniqueFragment = new DebateTechniqueFragment();
                }
                switchContent(debateTechniqueFragment);
                break;
            case R.id.latest_floating:
                latest_floating.setImageResource(isAdd ? R.drawable.plus:R.mipmap.close);
                isAdd = !isAdd;
                rlAddBill.setVisibility(isAdd ? View.VISIBLE : View.GONE);
                if (isAdd) {
                    addBillTranslate1.setTarget(ll[0]);
                    addBillTranslate1.start();
                    addBillTranslate2.setTarget(ll[1]);
                    addBillTranslate2.start();

                }
                break;
            case R.id.latest_miniFab01:
                hideFABMenu();
                Intent intent1 = new Intent(getActivity(), ReleaseNewsActivity.class);
                startActivity(intent1);
                break;
            case R.id.latest_miniFab02:

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
                            .add(R.id.latest_fragment_container, to)
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
        latest_floating.setImageResource(R.drawable.plus);
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
