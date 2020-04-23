package com.example.bigtalk.bigtalk.myspace;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

//import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    TextView myfrag_text_debate_num;
    TextView myfrag_text_fo_num;
    TextView myfrag_text_fan_num;

    ImageView myfrag_img_contest_attended;
    TextView myfrag_text_contest_attended;
    ImageView myfrag_img_contest_attended_getin;

    ImageView myfrag_img_news_released;
    TextView myfrag_text_news_released;
    ImageView myfrag_img_news_released_getin;

    ImageView myfrag_img_collect;
    TextView myfrag_text_collect;
    ImageView myfrag_img_collect_getin;

    ImageView myfrag_img_v;
    TextView myfrag_text_v;
    ImageView myfrag_img_v_getin;

    //用户

    //用户信息
//    BmobIMUserInfo info;

    User user;
    String userid;
    String title;
    private View view;

    private OnFragmentInteractionListener mListener;
    private Context mContext;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment .
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my, container, false);

        Bmob.initialize(getContext(), "4ab85f97c2bc54d88133be3de0df4131");

//        initView();
//        ButterKnife.bind(getActivity(), view);
        user = BmobUser.getCurrentUser(User.class);
        //        ImageLoaderFactory.getLoader().loadAvator(myfrag_img_avator, user.getAvatar(), R.mipmap.head2);
        //显示名称
        if(user == null){
            Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
        } else if(user.getObjectId() != null && !user.getObjectId().isEmpty()){
            userid = user.getObjectId();
//
        }

        if(user.getUsername() != null && !user.getUsername().isEmpty()){
            title = user.getUsername();
        }

        myfrag_text_debate_num = (TextView)view.findViewById(R.id.myfrag_text_debate_num);
        myfrag_text_fo_num = (TextView)view.findViewById(R.id.myfrag_text_fo_num);
        myfrag_text_fan_num = (TextView)view.findViewById(R.id.myfrag_text_fan_num);

        myfrag_img_contest_attended = (ImageView)view.findViewById(R.id.myfrag_img_contest_attended);

        myfrag_text_contest_attended = (TextView)view.findViewById(R.id.myfrag_text_contest_attended);
        myfrag_img_contest_attended_getin = (ImageView)view.findViewById(R.id.myfrag_img_contest_attended_getin);

        myfrag_img_news_released = (ImageView)view.findViewById(R.id.myfrag_img_news_released);
        myfrag_text_news_released = (TextView)view.findViewById(R.id.myfrag_text_news_released);
        myfrag_img_news_released_getin = (ImageView)view.findViewById(R.id.myfrag_img_news_released_getin);

        myfrag_img_collect = (ImageView)view.findViewById(R.id.myfrag_img_collect);
        myfrag_text_collect = (TextView)view.findViewById(R.id.myfrag_text_collect);
        myfrag_img_collect_getin = (ImageView)view.findViewById(R.id.myfrag_img_collect_getin);

        myfrag_img_v = (ImageView)view.findViewById(R.id.myfrag_img_v);
        myfrag_text_v = (TextView)view.findViewById(R.id.myfrag_text_v);
        myfrag_img_v_getin = (ImageView)view.findViewById(R.id.myfrag_img_v_getin);

        myfrag_text_debate_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), MyAttenedDebateActivity.class);
                intent1.putExtra("user_id",userid);
                startActivity(intent1);
            }
        });

        myfrag_text_fo_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), MyFollowActivity.class);
                intent2.putExtra("user_id",userid);
                startActivity(intent2);
            }
        });
        myfrag_text_fan_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getContext(), MyFanActivity.class);
                intent3.putExtra("user_id",userid);
                startActivity(intent3);
            }
        });


        myfrag_img_contest_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getContext(), MyAttenedDebateActivity.class);
                intent4.putExtra("user_id",userid);
                startActivity(intent4);
            }
        });
        myfrag_text_contest_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(getActivity(), MyAttenedDebateActivity.class);
                intent5.putExtra("user_id",userid);
                startActivity(intent5);
            }
        });
        myfrag_img_contest_attended_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 = new Intent(getActivity(), MyAttenedDebateActivity.class);
                intent6.putExtra("user_id",userid);
                startActivity(intent6);
            }
        });

        myfrag_img_news_released.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = new Intent(getActivity(), MyReleasedActivity.class);
                intent7.putExtra("user_id",userid);
                startActivity(intent7);
            }
        });
        myfrag_text_news_released.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent8 = new Intent(getActivity(), MyReleasedActivity.class);
                intent8.putExtra("user_id",userid);
                startActivity(intent8);
            }
        });
        myfrag_img_news_released_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent9 = new Intent(getActivity(), MyReleasedActivity.class);
                intent9.putExtra("user_id",userid);
                startActivity(intent9);
            }
        });

        myfrag_img_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent10 = new Intent(getActivity(), MyCollectActivity.class);
                intent10.putExtra("user_id",userid);
                startActivity(intent10);
            }
        });
        myfrag_text_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent11 = new Intent(getActivity(), MyCollectActivity.class);
                intent11.putExtra("user_id",userid);
                startActivity(intent11);
            }
        });
        myfrag_img_collect_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent12 = new Intent(getActivity(), MyCollectActivity.class);
                intent12.putExtra("user_id",userid);
                startActivity(intent12);
            }
        });


        myfrag_img_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent13 = new Intent(getActivity(), VRegisterActivity.class);
                intent13.putExtra("user_id",userid);
                startActivity(intent13);
            }
        });
        myfrag_text_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent14 = new Intent(getActivity(), VRegisterActivity.class);
                intent14.putExtra("user_id",userid);
                startActivity(intent14);
            }
        });
        myfrag_img_v_getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent15 = new Intent(getActivity(), VRegisterActivity.class);
                intent15.putExtra("user_id",userid);
                startActivity(intent15);
            }
        });


            return view;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //set toolbar elements......
//    @Override
//    protected String title() {
//        User user = BmobUser.getCurrentUser( User.class);
//        return user.getUsername();
//    }
//    @Override
//    public Object right() {
//        return R.drawable.toolbar_share_selector;
//    }
//
//    @Override
//    public Object left() {
//        return R.drawable.toolbar_edit_selector;
//    }
//
//    @Override
//    public Object right2() {
//        return R.drawable.toolbar_settings_selector;
//    }
//
//
//    @Override
//    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
//        return new ParentWithNaviActivity.ToolBarListener() {
//            @Override
//            public void clickLeft() {
//                Intent intent = new Intent(getActivity(), EditMyFileActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void clickRight() {
//                // invite friend
//                //in the plan
//                //coming soon....
//            }
//
//            @Override
//            public void clickRight2() {
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(intent);
//            }
//        };
//    }


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

}
