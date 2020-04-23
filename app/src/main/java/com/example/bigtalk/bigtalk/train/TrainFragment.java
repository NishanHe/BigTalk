package com.example.bigtalk.bigtalk.train;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.RelativeLayout;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.adapter.TopicsAdapter;
import com.example.bigtalk.bigtalk.bean.Article;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.frontpage.adapter.ContestAdapter;
import com.example.bigtalk.bigtalk.latest.ArticleAdapter;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private TopicsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Topics> list;
    private User user;
    private IMutlipleItem<Topics> mutlipleItem;

    private FrameLayout train_ll;
    private RecyclerView train_rcview;
    private SwipeRefreshLayout train_swrefresh;

    public TrainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainFragment newInstance(String param1, String param2) {
        TrainFragment fragment = new TrainFragment();
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

        view = inflater.inflate(R.layout.fragment_train, container, false);
        Bmob.initialize(getActivity(), "4ab85f97c2bc54d88133be3de0df4131");

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

        train_ll = (FrameLayout) view.findViewById(R.id.train_ll);

        adapter = new TopicsAdapter(getActivity(),mutlipleItem,null);
        train_swrefresh = (SwipeRefreshLayout) view.findViewById(R.id.train_swrefresh);
        train_rcview = (RecyclerView) view.findViewById(R.id.train_rcview);
        train_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        train_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        train_swrefresh.setRefreshing(true);
        query();

    }

    @Override
    public void onPause() {
        super.onPause();
    }



    private List<Topics> initNews(){
        user = BmobUser.getCurrentUser( User.class);

        String loginId = user.getObjectId();
        if(TextUtils.isEmpty(loginId)){
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
                        Log.i("bmob","TrainFragment查到了！！！");
                        System.out.println("查到所有比赛的内容咯咯");
                        System.out.println(product.getTopic_name());

                        list.add(product);
                    }

                }else{
                    Log.i("bmob","TrainFragment查询比赛失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        train_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        train_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                train_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                train_swrefresh.setRefreshing(true);
                query();
            }
        });
        train_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), TrainChooseSideActivity.class);

                intent.putExtra("topic_id",list.get(position).getObjectId());
                intent.putExtra("topic_name",list.get(position).getTopic_name());
                intent.putExtra("posi_opi",list.get(position).getPosi_opi());
                intent.putExtra("nega_opi",list.get(position).getNega_opi());
                intent.putExtra("topic_intro",list.get(position).getTopic_intro());
                System.out.print("TrainFragment: 传过去了topic_id是"+ list.get(position).getObjectId());

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
