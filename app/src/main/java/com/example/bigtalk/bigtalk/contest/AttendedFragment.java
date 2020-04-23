package com.example.bigtalk.bigtalk.contest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.adapter.AttendedAdapter;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendedFragment extends Fragment {

    @Bind(R.id.recyclerView_contest)
    RecyclerView recyclerViewContest;
    @Bind(R.id.contest_swrefresh)
    SwipeRefreshLayout contestSwrefresh;
    private View view;
    private Context mContext;
    private Handler handler = new Handler();
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private AttendedAdapter myadapter;
    private int limit = 10; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始
    List<Contest> contestList = new ArrayList<>();
    String lastTime = null;
    private List<Map<String, Object>> data = new ArrayList<>();
    boolean isChecked;
    boolean isLoading;


    private OnFragmentInteractionListener mListener;

    public AttendedFragment() {
        // Required empty public constructor
    }


    public static AttendedFragment newInstance(String param1, String param2) {
        AttendedFragment fragment = new AttendedFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attended, container, false);
        ButterKnife.bind(this, view);
        Log.i("onRunning", "   :正在执行createView方法");
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bmob.initialize(getContext(), "4ab85f97c2bc54d88133be3de0df4131", "bmob");
        this.mContext = getActivity();

        initData();
        initView();
    }
    public void initView() {
        Log.i("onRunning", "   :正在执行initView方法");

        contestSwrefresh.setColorSchemeResources(R.color.colorPrimary);
        contestSwrefresh.post(new Runnable() {
            @Override
            public void run() {
                contestSwrefresh.setRefreshing(true);
            }
        });

        contestSwrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        getData(0, STATE_REFRESH);
                    }
                }, 3000);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        myadapter = new AttendedAdapter(getContext(), contestList);
        recyclerViewContest.setLayoutManager(layoutManager);
        recyclerViewContest.setAdapter(myadapter);
        recyclerViewContest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == myadapter.getItemCount()) {
                    Log.d("test", "loading executed");

                    boolean isRefreshing = contestSwrefresh.isRefreshing();
                    if (isRefreshing) {
                        myadapter.notifyItemRemoved(myadapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(curPage, STATE_MORE);
                                Log.d("test", "load more completed");
                                isLoading = false;
                            }
                        }, 2000);
                    }
                }
            }
        });
        myadapter.setOnItemClickListener(new AttendedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("test", "item position = " + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    public void initData() {
        Log.i("onRunnig", "   :正在执行initData方法");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(0, STATE_REFRESH);
            }
        }, 1000);

    }

    private void getData(int page, final int actionType) {

        int count = 0;


        User userInfo = BmobUser.getCurrentUser(User.class);


        BmobQuery<Contest> queryDebater = new BmobQuery<>();
        List<BmobQuery<Contest>> or = new ArrayList<>();


        BmobQuery<Contest> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("nega_1", userInfo);
        or.add(q1);
        BmobQuery<Contest> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("nega_2", userInfo);
        or.add(q2);
        BmobQuery<Contest> q3 = new BmobQuery<>();
        q3.addWhereEqualTo("nega_3", userInfo);
        or.add(q3);
        BmobQuery<Contest> q4 = new BmobQuery<>();
        q4.addWhereEqualTo("nega_4", userInfo);
        or.add(q4);
        BmobQuery<Contest> q5 = new BmobQuery<>();
        q5.addWhereEqualTo("posi_1", userInfo);
        or.add(q5);
        BmobQuery<Contest> q6 = new BmobQuery<>();
        q6.addWhereEqualTo("posi_2", userInfo);
        or.add(q6);
        BmobQuery<Contest> q7 = new BmobQuery<>();
        q7.addWhereEqualTo("posi_3", userInfo);
        or.add(q7);
        BmobQuery<Contest> q8 = new BmobQuery<>();
        q8.addWhereEqualTo("posi_4", userInfo);
        or.add(q8);
        queryDebater.or(or);
        queryDebater.setLimit(500);
        queryDebater.include("topic");
        queryDebater.order("-updatedAt");
        queryDebater.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把contestList清空，重新添加
                            curPage = 0;
                            contestList.clear();
                            // 获取最后时间
                            lastTime = list.get(list.size() - 1).getCreatedAt();
                        }
                        for (int i = 0; i < list.size(); i++) {
                            contestList.add(list.get(i));
                            Log.i("bmob", "房间id" + contestList.get(i).getContest_id());
                            Log.i("bmob", "房间名" + contestList.get(i).getContest_room());
                        }
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
//					 showToast("第"+(page+1)+"页数据加载完成");
                    } else if (actionType == STATE_MORE) {
                        showToast("没有更多数据了");

                    } else if (actionType == STATE_REFRESH) {
                        showToast("没有数据");
                    }
                    contestSwrefresh.setRefreshing(false);
                    recyclerViewContest.setAdapter(myadapter);
                } else {
                    showToast("查询失败:" + e.getMessage());
                    contestSwrefresh.setRefreshing(false);
                }
            }
        });
        myadapter.notifyDataSetChanged();
        myadapter.notifyItemRemoved(myadapter.getItemCount());

    }
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
