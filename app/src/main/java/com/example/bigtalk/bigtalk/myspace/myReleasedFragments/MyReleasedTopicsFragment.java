package com.example.bigtalk.bigtalk.myspace.myReleasedFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.frontpage.bean.Topics;
import com.example.bigtalk.bigtalk.myspace.adapter.TopicAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyReleasedTopicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyReleasedTopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyReleasedTopicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.my_released_topics_ll)
    FrameLayout my_released_topics_ll;

    @Bind(R.id.my_released_topics_rcview)
    RecyclerView my_released_topics_rcview;
    @Bind(R.id.my_released_topics_swrefresh)
    SwipeRefreshLayout my_released_topics_swrefresh;


    TopicAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Topics> list;
    User user;
    SharedPreferences mSharedPreferences;
    String userid;
    Context context;
    public MyReleasedTopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyReleasedTopicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyReleasedTopicsFragment newInstance(String param1, String param2) {
        MyReleasedTopicsFragment fragment = new MyReleasedTopicsFragment();
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
        list = new ArrayList<>();
        //单一布局
        IMutlipleItem<Topics> mutlipleItem = new IMutlipleItem<Topics>() {

            @Override
            public int getItemViewType(int position, Topics c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_card;
            }

            @Override
            public int getItemCount(List<Topics> list) {
                return list.size();
            }
        };
        adapter = new TopicAdapter(getContext(),mutlipleItem,null);
        my_released_topics_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        my_released_topics_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();
        return inflater.inflate(R.layout.fragment_my_released_topics, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        my_released_topics_swrefresh.setRefreshing(true);
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

        mSharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("user_id","");

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

        BmobQuery<Topics> query = new BmobQuery<Topics>();
        query.addWhereEqualTo("topic_author", user);  // 查询当前用户的所有帖子
        query.order("-updatedAt");
//                        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Topics>() {

            @Override
            public void done(List<Topics> object,BmobException e) {
                if(e==null){
                    Log.i("bmob","MyReleasedTopicsFragment查询用户关注的人发布的辩题成功");
                    for(final Topics t: object){
                        list.add(t);
                    }
                }else{
                    Log.i("bmob","MyReleasedTopicsFragment查询用户关注的人发布的辩题失败："+e.getMessage());
                }
            }

        });

        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        my_released_topics_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        my_released_topics_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                my_released_topics_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                my_released_topics_swrefresh.setRefreshing(true);
                query();
            }
        });
        my_released_topics_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
//                Intent intent = new Intent(getActivity()
//                        .getApplicationContext(), DetailsForTopicActivity.class);

//                intent.putExtra("topic_id",list.get(position).getObjectId());
//                intent.putExtra("topic_name",list.get(position).getTopic_name());
//                intent.putExtra("topic_type",list.get(position).getTopic_type());
//                intent.putExtra("topic_author_id",list.get(position).getTopic_author().getUser_id());
//                intent.putExtra("topic_time",list.get(position).getTopic_time());
//
//                startActivity(intent);
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
