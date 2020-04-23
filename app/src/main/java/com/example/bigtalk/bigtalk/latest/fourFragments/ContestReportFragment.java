package com.example.bigtalk.bigtalk.latest.fourFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.adapter.AttendedAdapter;
import com.example.bigtalk.bigtalk.adapter.TopicsAdapter;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.ContestReport;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.contest.PlayContestActivity;
import com.example.bigtalk.bigtalk.frontpage.adapter.ContestAdapter;
import com.example.bigtalk.bigtalk.latest.ArticleAdapter;
import com.example.bigtalk.bigtalk.latest.ContestReportActivity;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;
import com.example.bigtalk.bigtalk.bean.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContestReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContestReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContestReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FrameLayout latest_contest_report_ll;
    RecyclerView latest_contest_report_rcview;
    SwipeRefreshLayout latest_contest_report_swrefresh;


    TopicsAdapter adapter;
    LinearLayoutManager layoutManager;
    List<Topics> list;
    User user;
    View view;
    IMutlipleItem<Topics> mutlipleItem;

    public ContestReportFragment() {
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
    public static ContestReportFragment newInstance(String param1, String param2) {
        ContestReportFragment fragment = new ContestReportFragment();
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
        view = inflater.inflate(R.layout.fragment_contest_report, container, false);
//        ButterKnife.bind(getActivity(), view);
        Bmob.initialize(getActivity(), "4ab85f97c2bc54d88133be3de0df4131bigt");

        list = new ArrayList<>();
        //单一布局
        mutlipleItem = new IMutlipleItem<Topics>() {

            @Override
            public int getItemViewType(int position, Topics c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.train_item;
            }

            @Override
            public int getItemCount(List<Topics> list) {
                return list.size();
            }
        };

        latest_contest_report_ll = (FrameLayout) view.findViewById(R.id.latest_contest_report_ll);

        adapter = new TopicsAdapter(getActivity(),mutlipleItem,null);
        latest_contest_report_swrefresh = (SwipeRefreshLayout) view.findViewById(R.id.latest_contest_report_swrefresh);
        latest_contest_report_rcview = (RecyclerView) view.findViewById(R.id.latest_contest_report_rcview);
        latest_contest_report_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        latest_contest_report_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();

        return view;
    }
    @Override
    public void onResume() {

        super.onResume();
        latest_contest_report_swrefresh.setRefreshing(true);
        query();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    private List<Topics> initNews(){
        user = BmobUser.getCurrentUser( User.class);

        String loginId = user.getObjectId();
        if(TextUtils.isEmpty(loginId)) {
            throw new RuntimeException("you must login.");
        }


        BmobQuery<Topics> query = new BmobQuery<Topics>();
        query.setLimit(50);
        query.order("-updatedAt");
        query.findObjects(new FindListener<Topics>() {
            @Override
            public void done(final List<Topics> object, BmobException e) {
                if(e==null){
                    for(final Topics product : object) {
                        //Topics topics = product.getTopic();
                        Log.i("bmob","ContestTopicFragment查到了！！！");
                        System.out.print("ContestReportFragment查到的内容"+product.getObjectId());
                        //System.out.print("ContestReportFragment查到的内容"+topics);
                        //System.out.print("ContestReportFragment查到的内容"+topics.getTopic_name());
                        list.add(product);
                    }
                }else{
                    Log.i("bmob","ContestReportFragment查询比赛整理失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        return list;


    }

    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        latest_contest_report_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        latest_contest_report_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                latest_contest_report_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                latest_contest_report_swrefresh.setRefreshing(true);
                query();
            }
        });
        latest_contest_report_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), PlayContestActivity.class);

                //intent.putExtra("contestIdSent",list.get(position).getContest_id());
                //intent.putExtra("contest_id",list.get(position).getObjectId());
                intent.putExtra("contestIdSent","05");
                intent.putExtra("contest_id","HYtzCCC9");
                startActivity(intent);

//                log("点击："+position);

            }

            @Override
            public boolean onItemLongClick(int position) {

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
