package com.example.bigtalk.bigtalk.latest.fourFragments;

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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DebateTechniqueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DebateTechniqueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebateTechniqueFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DebateTechniqueFragment.OnFragmentInteractionListener mListener;

    FrameLayout latest_debate_technique_ll;

    RecyclerView latest_debate_technique_rcview;

    SwipeRefreshLayout latest_debate_technique_swrefresh;


    ArticleAdapter adapter;
    LinearLayoutManager layoutManager;
    private String searchStr = "";
    List<Article> list;
//    List<Comment> commentList;
    User user;
    View view;

    public DebateTechniqueFragment() {
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
    public static DebateTechniqueFragment newInstance(String param1, String param2) {
        DebateTechniqueFragment fragment = new DebateTechniqueFragment();
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
        view = inflater.inflate(R.layout.fragment_debate_technique, container, false);
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

        latest_debate_technique_ll = (FrameLayout)view.findViewById(R.id.latest_debate_technique_ll);
        latest_debate_technique_rcview = (RecyclerView)view.findViewById(R.id.latest_debate_technique_rcview);
        latest_debate_technique_swrefresh = (SwipeRefreshLayout)view.findViewById(R.id.latest_debate_technique_swrefresh);

        adapter = new ArticleAdapter(getActivity(),mutlipleItem,null);
        latest_debate_technique_rcview.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        latest_debate_technique_rcview.setLayoutManager(layoutManager);

//        FanManager.getInstance(this).updateBatchStatus();
        setListener();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        latest_debate_technique_swrefresh.setRefreshing(true);
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


        //通过当前用户id查询Fan表中 fan_id,再通过fan_id在User表中查询user_name,avatar
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("type", "辩论技巧");
        query.setLimit(50);
        query.order("-updatedAt");
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(final List<Article> object, BmobException e) {
                if(e==null){
                    for(final Article product : object) {
                        Log.i("bmob","DebateTechniqueFragment查到了！！！");
                        System.out.println("查到所有辩论技巧的内容嘻嘻嘻嘻嘻");
                        product.getTitle();
                        product.getContent();
                        list.add(product);
                    }
//                    MyAdapter adapter = new MyAdapter(list);
//                    mRecyclerView.setAdapter(adapter);
                }else{
                    Log.i("bmob","DebateTechniqueFragment查询辩论技巧失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return list;
    }
    public void query(){
        initNews();

        adapter.bindDatas(list);   //adapter绑定
        adapter.notifyDataSetChanged();
        latest_debate_technique_swrefresh.setRefreshing(false);
    }

    private void setListener(){
        latest_debate_technique_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                latest_debate_technique_ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                latest_debate_technique_swrefresh.setRefreshing(true);
                query();
            }
        });
        latest_debate_technique_swrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("news_author_id",list.get(position).getNews_author().getObjectId());
                intent.putExtra("news_author",list.get(position).getNews_author().getUsername());
                intent.putExtra("news_time",list.get(position).getCreatedAt());
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
