package com.example.bigtalk.bigtalk.frontpage.fourFragments;

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
import com.example.bigtalk.bigtalk.bean.Article;
import com.example.bigtalk.bigtalk.chat.adapter.OnRecyclerViewListener;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.latest.ArticleAdapter;
import com.example.bigtalk.bigtalk.latest.DetailsForLatestActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsReleasedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsReleasedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsReleasedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NewsReleasedFragment.OnFragmentInteractionListener mListener;

    FrameLayout front_news_released_ll;

//    @Bind(R.id.front_news_released_rcview)
    RecyclerView front_news_released_rcview;
//    @Bind(R.id.front_news_released_swrefresh)
    SwipeRefreshLayout front_news_released_swrefresh;


    ArticleAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Article> list;
    User user;
    View view;

    public NewsReleasedFragment() {
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
    public static NewsReleasedFragment newInstance(String param1, String param2) {
        NewsReleasedFragment fragment = new NewsReleasedFragment();
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
        Bmob.initialize(getActivity(), "4ab85f97c2bc54d88133be3de0df4131");
        view = inflater.inflate(R.layout.fragment_news_released, container, false);
        ButterKnife.bind(getActivity(), view);
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

        front_news_released_swrefresh = (SwipeRefreshLayout)view.findViewById(R.id.front_news_released_swrefresh) ;
        front_news_released_rcview = (RecyclerView)view.findViewById(R.id.front_news_released_rcview);
        front_news_released_ll = (FrameLayout)view.findViewById(R.id.front_news_released_ll);
        front_news_released_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        front_news_released_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        front_news_released_swrefresh.setRefreshing(true);
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

        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("type", "用户发布");
        query.setLimit(50);
        query.order("-updatedAt");
        System.out.println("开始查询所有用户发布的内容啦啦啦啦啦啦啦啦");
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> object, BmobException e) {
                if(e==null){
                    for (Article product : object) {
                        Log.i("bmob","NewsReleasedFragment查到了！！！");
                        System.out.println("查到所有用户发布的内容咯咯");
                        list.add(product);
                    }
                }else{
                    Log.i("bmob","NewsReleasedFragment失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        front_news_released_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        front_news_released_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                front_news_released_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                front_news_released_swrefresh.setRefreshing(true);
                query();
            }
        });
        front_news_released_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof NewsReleasedFragment.OnFragmentInteractionListener) {
//            mListener = (NewsReleasedFragment.OnFragmentInteractionListener) context;
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
