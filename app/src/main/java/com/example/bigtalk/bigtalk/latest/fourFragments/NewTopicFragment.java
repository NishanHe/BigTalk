package com.example.bigtalk.bigtalk.latest.fourFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.latest.ArticleAdapter;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;
import com.example.bigtalk.bigtalk.bean.Article;
//import com.example.bigtalk.bigtalk.latest.bean.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTopicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTopicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTopicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NewTopicFragment.OnFragmentInteractionListener mListener;
    @Bind(R.id.latest_new_topic_ll)
    FrameLayout latest_new_topic_ll;

    @Bind(R.id.latest_new_topic_rcview)
    RecyclerView latest_new_topic_rcview;
    @Bind(R.id.latest_new_topic_swrefresh)
    SwipeRefreshLayout latest_new_topic_swrefresh;


    ArticleAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Article> list;
//    List<Comment> commentList;
    User user;

    public NewTopicFragment() {
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
    public static NewTopicFragment newInstance(String param1, String param2) {
        NewTopicFragment fragment = new NewTopicFragment();
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
        IMutlipleItem<Article> mutlipleItem = new IMutlipleItem<Article>() {

            @Override
            public int getItemViewType(int position, Article c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_card;
            }

            @Override
            public int getItemCount(List<Article> list) {
                return list.size();
            }
        };
        adapter = new ArticleAdapter(getContext(),mutlipleItem,null);
        latest_new_topic_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        latest_new_topic_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();


        return inflater.inflate(R.layout.fragment_new_topic, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        latest_new_topic_swrefresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */
    private List<Article> initNews(){

        String loginId = user.getObjectId();
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }
        searchStr = loginId;

        //通过当前用户id查询Fan表中 fan_id,再通过fan_id在User表中查询user_name,avatar
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("type", "newTopic");
        query.setLimit(50);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(final List<Article> object, BmobException e) {
                if(e==null){
                    for(final Article product : object) {
                        String fanid = product.getObjectId();
                        product.getTitle();
                        product.getContent();
                        list.add(product);
                    }
//                    MyAdapter adapter = new MyAdapter(list);
//                    mRecyclerView.setAdapter(adapter);
                }else{
                    Log.i("bmob","失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        latest_new_topic_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        latest_new_topic_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                latest_new_topic_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                latest_new_topic_swrefresh.setRefreshing(true);
                query();
            }
        });
        latest_new_topic_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity()
                        .getApplicationContext(), DetailsForLatestActivity.class);

                intent.putExtra("news_id",list.get(position).getObjectId());
                intent.putExtra("news_content",list.get(position).getContent());
                intent.putExtra("news_author_id",list.get(position).getNews_author().getObjectId());
                intent.putExtra("news_time",list.get(position).getNews_time());
                intent.putExtra("news_like",list.get(position).getNews_like());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewTopicFragment.OnFragmentInteractionListener) {
            mListener = (NewTopicFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
