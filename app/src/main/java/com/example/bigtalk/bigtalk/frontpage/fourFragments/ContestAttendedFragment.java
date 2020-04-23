package com.example.bigtalk.bigtalk.frontpage.fourFragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.contest.ContestINGActivity;
import com.example.bigtalk.bigtalk.contest.PlayContestActivity;
import com.example.bigtalk.bigtalk.frontpage.adapter.ContestAdapter;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;
import com.example.bigtalk.bigtalk.bean.Follow;
import com.example.bigtalk.bigtalk.myspace.MyAttenedDebateActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContestAttendedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContestAttendedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContestAttendedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ContestAttendedFragment.OnFragmentInteractionListener mListener;

    FrameLayout front_contest_attended_ll;
    RecyclerView front_contest_attended_rcview;

    SwipeRefreshLayout front_contest_attended_swrefresh;


    View view;
    ContestAdapter adapter;
    LinearLayoutManager layoutManager;

    List<Contest> list;
    User user;



    public ContestAttendedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContestReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContestAttendedFragment newInstance(String param1, String param2) {
        ContestAttendedFragment fragment = new ContestAttendedFragment();
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

        view = inflater.inflate(R.layout.fragment_contest_attended, container, false);
        ButterKnife.bind(getActivity(),view);


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

        adapter = new ContestAdapter(getActivity(),mutlipleItem,null);
        front_contest_attended_rcview = (RecyclerView)view.findViewById(R.id.front_contest_attended_rcview);
        front_contest_attended_ll = (FrameLayout)view.findViewById(R.id.front_contest_attended_ll);
        front_contest_attended_swrefresh = (SwipeRefreshLayout)view.findViewById(R.id.front_contest_attended_swrefresh);


        front_contest_attended_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        front_contest_attended_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();



        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        front_contest_attended_swrefresh.setRefreshing(true);
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

        user = BmobUser.getCurrentUser( User.class);
        String loginId = user.getObjectId();
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }



        BmobQuery<User> followQuery = new BmobQuery<User>();

        followQuery.addWhereRelatedTo("follow", new BmobPointer(user));
        followQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object,BmobException e) {
                if(e==null){
                    Log.i("bmob","ContestAttendedFragment查询关注的人个数："+object.size());


                        BmobQuery<Contest> queryDebater = new BmobQuery<>();
                        List<BmobQuery<Contest>> and = new ArrayList<BmobQuery<Contest>>();

                        BmobQuery<Contest> q1 = new BmobQuery<>();
                        q1.addWhereContainedIn("nega_1", object);
                        and.add(q1);
                        BmobQuery<Contest> q2 = new BmobQuery<>();
                        q2.addWhereContainedIn("nega_2", object);
                        and.add(q2);
                        BmobQuery<Contest> q3 = new BmobQuery<>();
                        q3.addWhereContainedIn("nega_3", object);
                        and.add(q3);
                        BmobQuery<Contest> q4 = new BmobQuery<>();
                        q4.addWhereContainedIn("nega_4", object);
                        and.add(q4);
                        BmobQuery<Contest> q5 = new BmobQuery<>();
                        q5.addWhereContainedIn("posi_1", object);
                        and.add(q5);
                        BmobQuery<Contest> q6 = new BmobQuery<>();
                        q6.addWhereContainedIn("posi_2", object);
                        and.add(q6);
                        BmobQuery<Contest> q7 = new BmobQuery<>();
                        q7.addWhereContainedIn("posi_3", object);
                        and.add(q7);
                        BmobQuery<Contest> q8 = new BmobQuery<>();
                        q8.addWhereContainedIn("posi_4", object);
                        and.add(q8);
                        queryDebater.or(and);
                        queryDebater.order("-updatedAt");
                        queryDebater.findObjects(new FindListener<Contest>() {
                            @Override
                            public void done(List<Contest> o, BmobException e) {
                                if(e==null){
                                    Log.i("bmob","ContestAttendedFragment查询比赛成功！！！");
                                    System.out.println("查到关注用户的所有比赛啦啦啦啦");
                                    for(final Contest contest:o){
                                        list.add(contest);
                                        System.out.println("查询到的比赛辩题名字："+contest.getTopic().getTopic_name());
                                    }


                                }else{
                                    Log.i("bmob","ContestAttendedFragment查询比赛失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                }else{
                    Log.i("bmob","ContestAttendedFragment查询关注失败："+e.getMessage());
                }
            }

        });
        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        front_contest_attended_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        front_contest_attended_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                front_contest_attended_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                front_contest_attended_swrefresh.setRefreshing(true);
                query();
            }
        });
        front_contest_attended_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if(list.get(position).getTopic().getTopic_status() == "waitForU"){
                    Toast.makeText(getActivity(), "比赛尚未开始，，请耐心等待", Toast.LENGTH_SHORT).show();
                }
                else if(list.get(position).getTopic().getTopic_status() == "onFire"){
                    Intent intent = new Intent(getActivity(), ContestINGActivity.class);

                    intent.putExtra("contest_id",list.get(position).getObjectId());
                    intent.putExtra("userRole","broadcaster");

                }else if(list.get(position).getTopic().getTopic_status() == "end"){
                    Intent intent = new Intent(getActivity(), PlayContestActivity.class);

                    intent.putExtra("contestIdSent",list.get(position).getObjectId());
                }





//                log("点击："+position);

            }

            @Override
            public boolean onItemLongClick(int position) {
// click long to delete new friend
//                FanManager.getInstance(MyFanActivity.this).deleteNewFriend(adapter.getItem(position));
//                adapter.remove(position);
//                Intent intent = new Intent(getActivity().getApplicationContext(), DetailsForLatestActivity.class);
//                intent.putExtra("news_id",list.get(position).getObjectId());

                return true;
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
