package com.example.bigtalk.bigtalk.contest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.adapter.MyRecyclerViewAdapter;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.Follow;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.tools.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContestFragment extends Fragment implements View.OnClickListener  {

    @Bind(R.id.spi_field)
    Spinner spiField;
    @Bind(R.id.if_me_debaters)
    CheckBox ifMeDebaters;
    @Bind(R.id.filter)
    Button filter;
    @Bind(R.id.recyclerView_contest)
    RecyclerView recyclerViewContest;
    @Bind(R.id.contest_swrefresh)
    SwipeRefreshLayout contestSwrefresh;
    @Bind(R.id.pickDate)
    EditText pickDate;
    private DebateFragment.OnFragmentInteractionListener mListener;
    private int year;
    private int month;
    private int day;
    private Context mContext;
    private String ptntype, userDate;
    boolean isChecked;
    boolean isLoading;
    private List<Map<String, Object>> data = new ArrayList<>();
    List<Follow> followList;
    List<Contest> contestList = new ArrayList<>();
    private MyRecyclerViewAdapter myadapter;
    private Handler handler = new Handler();
    private View view;
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 10; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始
    String lastTime = null;
    final List<BmobQuery<Contest>> andQuery = new ArrayList<>();
    final List<BmobQuery<Topics>> andTopicQuery = new ArrayList<>();

    public ContestFragment() {

    }


    public static ContestFragment newInstance() {
        ContestFragment fragment = new ContestFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contest, container, false);
        ButterKnife.bind(this, view);
        spiField.setPrompt("请选择辩题类型");
        pickDate.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘
        Log.i("onRunning", "   :正在执行createView方法");
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bmob.initialize(getActivity(), "4ab85f97c2bc54d88133be3de0df4131", "bmob");
        this.mContext = getActivity();

        initData();
        initView();
    }

    public void initView() {
        Log.i("onRunning", "   :正在执行initView方法");
        //选择领域下拉框

        ptntype = "all";
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.ptnType,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiField.setAdapter(adapter);
        //获取选择的辩题类型
        spiField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //拿到被选择项的值  
                ptntype = (String) spiField.getSelectedItem();
                Log.i("userChoice", ptntype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ptntype = "all";
            }
        });


//        pickDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if(hasFocus){
//                    showDatePickerDialog();
//                }
//            }
//        });
        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });
//        pickDate.setFilters(new InputFilter[]{new InputFilter(){
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end,
//                                       Spanned dest, int dstart, int dend) {
//                showDatePickerDialog();//不管按什么键都让DatePicker出现
//                return source.length() == 1 ? "" : source;   //DatePicker的设置还是要让他显示滴
//            }
//
//        }});

        //获取选择的日期
//        Calendar c = Calendar.getInstance();
//        userDate = "";
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker arg0, int year
//                    , int month, int day) {
//                DebateFragment.this.year = year;
//                DebateFragment.this.month = month;
//                DebateFragment.this.day = day;
//                userDate = year + "-" + month + "-" + day + "-";
//                Toast.makeText(mContext, "您选择的日期：" + userDate, Toast.LENGTH_SHORT).show();
//            }
//        });


        //获取是否选择用户关注的辩手
        isChecked = false;
        ifMeDebaters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    User userInfo = BmobUser.getCurrentUser(User.class);

                    //测试用
                    userInfo.setObjectId("LrZn0001");

                    if (userInfo != null) {
                        // 允许用户使用应用
//                        BmobQuery<Follow> bmobQuery = new BmobQuery<>();
//                        bmobQuery.getObject(userInfo.getObjectId(), new QueryListener<Follow>() {
//                            @Override
//                            public void done(Follow object,BmobException e) {
//                                if(e==null){
//                                    showToast("查询用户成功:");
//                                    //要加的
//                                }else{
//                                    showToast("更新用户信息失败:");
//                                }
//                            }
//                        });
                        BmobQuery<Follow> queryFollow = new BmobQuery<>();
                        queryFollow.addWhereEqualTo("user", userInfo);
                        queryFollow.include("follow");
                        queryFollow.setLimit(500);
                        queryFollow.findObjects(new FindListener<Follow>() {
                            @Override
                            public void done(List<Follow> object, BmobException e) {
                                if (e == null) {
                                    if (object.size() > 0) {
                                        for (int i = 0; i < object.size(); i++) {
                                            followList.add(object.get(i));
                                        }
                                    }
                                    showToast("查询用户成功:" + object.size());
                                } else {
                                    showToast("更新用户信息失败:" + e.getMessage());
                                }
                            }
                        });

                    } else {
                        //缓存用户对象为空时， 可打开用户注册界面…
                        Intent intent1 = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(intent1);
                    }
                } else {
                    followList = null;
                }
            }
        });

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
        myadapter = new MyRecyclerViewAdapter(getContext(), contestList);
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
        myadapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("test", "item position = " + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        Log.i("checkpoint","年份："+c.get(Calendar.YEAR));
        new DatePickerDialog(getActivity(),R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                userDate = "";
                ContestFragment.this.year = year;
                ContestFragment.this.month = month;
                ContestFragment.this.day = day;
                userDate = year + "-" + month + "-" + day + "-";
                pickDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

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
        Log.i("onRunnig", "   :正在执行getData方法");
        int count = 0;

        BmobQuery<Contest> contestBmobQuery = new BmobQuery<>();
        BmobQuery<Topics> innerquery = new BmobQuery<>();
        //选择比赛状态合适的topics
        String[] status = {"waitForU", "onFire"};
        innerquery.addWhereContainedIn("topic_status", Arrays.asList(status));
        innerquery.setLimit(20000);
        contestBmobQuery.addWhereMatchesQuery("topic", "Topics", innerquery);
        contestBmobQuery.setLimit(limit);
        contestBmobQuery.include("topic");
        contestBmobQuery.order("-updatedAt");
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
//                Log.i("0621", date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            contestBmobQuery.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            contestBmobQuery.setSkip(page * count + 1);
        } else {
            // 下拉刷新
            page = 0;
            contestBmobQuery.setSkip(page);
        }
        contestBmobQuery.findObjects(new FindListener<Contest>() {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @OnClick(R.id.filter)
    public void onClicked(View view) {
        Log.i("bmob", "用户条件筛选中...");

        //判断是否需要筛选辩题类型
        if (!ptntype.equals("all")) {
            final BmobQuery<Topics> queryT = new BmobQuery<>();
            queryT.addWhereEqualTo("topic_type", ptntype);
            andTopicQuery.add(queryT);
        } else {
            Log.i("bmob", "没有特定选择的辩题类型");
        }

        //判断是否需要筛选时间
        if (!userDate.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(userDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BmobQuery<Contest> andTimeQuery = new BmobQuery<>();
            List<BmobQuery<Contest>> andTime = new ArrayList<>();
//大于00：00：00
            BmobQuery<Contest> q1 = new BmobQuery<>();
            q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date));
            andTime.add(q1);
//小于23：59：59
            BmobQuery<Contest> q2 = new BmobQuery<>();
            String end = userDate + " 23:59:59";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                date1 = sdf1.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date1));
            andTime.add(q2);
//添加复合与查询
            andTimeQuery.and(andTime);
            andQuery.add(andTimeQuery);
        } else {
            Log.i("bmob", "没有特定选择的日期");
        }

        //判断是否需要根据用户关注的辩手进行筛选
        if (followList != null) {

            BmobQuery<Contest> queryDebater = new BmobQuery<>();
            List<BmobQuery<Contest>> and = new ArrayList<>();

            BmobQuery<Contest> q1 = new BmobQuery<>();
            q1.addWhereContainedIn("nega_1", followList);
            and.add(q1);
            BmobQuery<Contest> q2 = new BmobQuery<>();
            q2.addWhereContainedIn("nega_2", followList);
            and.add(q2);
            BmobQuery<Contest> q3 = new BmobQuery<>();
            q3.addWhereContainedIn("nega_3", followList);
            and.add(q3);
            BmobQuery<Contest> q4 = new BmobQuery<>();
            q4.addWhereContainedIn("nega_4", followList);
            and.add(q4);
            BmobQuery<Contest> q5 = new BmobQuery<>();
            q5.addWhereContainedIn("posi_1", followList);
            and.add(q5);
            BmobQuery<Contest> q6 = new BmobQuery<>();
            q6.addWhereContainedIn("posi_2", followList);
            and.add(q6);
            BmobQuery<Contest> q7 = new BmobQuery<>();
            q7.addWhereContainedIn("posi_3", followList);
            and.add(q7);
            BmobQuery<Contest> q8 = new BmobQuery<>();
            q8.addWhereContainedIn("posi_4", followList);
            and.add(q8);
            queryDebater.or(and);
            andQuery.add(queryDebater);
        }
        filterData(0, STATE_REFRESH);


    }

    private List<Contest> filterData(int page, final int actionType) {
        Log.i("onRunnig", "   :正在执行filterData方法");
        BmobQuery<Contest> contestBmobQuery = new BmobQuery<>();
        BmobQuery<Topics> innerquery = new BmobQuery<>();

        BmobQuery<Topics> q1 = new BmobQuery<>();
        String[] status = {"waitForU", "onFire"};
        q1.addWhereContainedIn("topic_status", Arrays.asList(status));
        andTopicQuery.add(q1);
        innerquery.and(andTopicQuery);

        contestBmobQuery.addWhereMatchesQuery("topic", "Topics", innerquery);
        contestBmobQuery.and(andQuery);
        contestBmobQuery.include("topic");
        contestBmobQuery.setLimit(limit);
        contestBmobQuery.order("-updatedAt");
        contestBmobQuery.findObjects(new FindListener<Contest>() {
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
        return contestList;
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}
