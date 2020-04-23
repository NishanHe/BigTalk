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

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Article;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.frontpage.adapter.CollectAdapter;
import com.example.bigtalk.bigtalk.frontpage.adapter.CollectArticleAdapter;
import com.example.bigtalk.bigtalk.frontpage.adapter.ContestAdapter;
import com.example.bigtalk.bigtalk.frontpage.bean.Contest;
import com.example.bigtalk.bigtalk.latest.ArticleAdapter;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;
import com.example.bigtalk.bigtalk.latest.bean.Collect;
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
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CollectFragment.OnFragmentInteractionListener mListener;

    FrameLayout front_collect_ll;

    RecyclerView front_collect_rcview;

    SwipeRefreshLayout front_collect_swrefresh;


    ArticleAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Article> list;
    View view;

    User user;

    public CollectFragment() {
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
    public static CollectFragment newInstance(String param1, String param2) {
        CollectFragment fragment = new CollectFragment();
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
        view = inflater.inflate(R.layout.fragment_collect, container, false);
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
        adapter = new ArticleAdapter(getActivity(),mutlipleItem,null);

        front_collect_ll = (FrameLayout)view.findViewById(R.id.front_collect_ll);
        front_collect_rcview = (RecyclerView)view.findViewById(R.id.front_collect_rcview);
        front_collect_swrefresh = (SwipeRefreshLayout)view.findViewById(R.id.front_collect_swrefresh);


        front_collect_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        front_collect_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();



        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        front_collect_swrefresh.setRefreshing(true);
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

        user = BmobUser.getCurrentUser( User.class);
        String loginId = user.getObjectId();
        if(TextUtils.isEmpty(loginId)){
            throw new RuntimeException("you must login.");
        }


        BmobQuery<User> followQuery = new BmobQuery<User>();
        User host = new User();
        host.setObjectId(user.getObjectId());
        followQuery.addWhereRelatedTo("follow", new BmobPointer(host));
        followQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object,BmobException e) {
                if(e==null){
                    Log.i("bmob","CollectFragment查询关注的人个数："+object.size());
                    for(final User product : object) {


                        BmobQuery<Article> query = new BmobQuery<Article>();
                        query.addWhereEqualTo("news_author", product);  // 查询当前用户的所有帖子
                        query.order("-updatedAt");
//                        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
                        query.findObjects(new FindListener<Article>() {

                            @Override
                            public void done(List<Article> object,BmobException e) {
                                if(e==null){
                                    Log.i("bmob","CollectFragment查询用户关注的人发布的内容成功");
                                    for(final Article a: object){
                                        list.add(a);
                                    }
                                }else{
                                    Log.i("bmob","CollectFragment查询用户关注的人发布的内容失败："+e.getMessage());
                                }
                            }

                        });

                    }
                }else{
                    Log.i("bmob","CollectFragment查询用户关注的人!失败："+e.getMessage());
                }
            }

        });
        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        front_collect_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        front_collect_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                front_collect_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                front_collect_swrefresh.setRefreshing(true);
                query();
            }
        });
        front_collect_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                intent.putExtra("news_author",list.get(position).getNews_author().getUsername());
                intent.putExtra("type",list.get(position).getType());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("news_time",list.get(position).getCreatedAt());
                intent.putExtra("news_like",list.get(position).getNews_like());
                startActivity(intent);

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
