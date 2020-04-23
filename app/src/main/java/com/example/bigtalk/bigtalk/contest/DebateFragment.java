package com.example.bigtalk.bigtalk.contest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bigtalk.bigtalk.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DebateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebateFragment extends Fragment implements View.OnClickListener {


    @Bind(R.id.radioContest)
    RadioButton radioContest;
    @Bind(R.id.radioChat)
    RadioButton radioChat;
    @Bind(R.id.radioAttended)
    RadioButton radioAttended;
    @Bind(R.id.framelayout)
    FrameLayout framelayout;
    @Bind(R.id.topGroup)
    RadioGroup topGroup;
    private LayoutInflater inflater;
    private Context mActivity;
    private ContestFragment contestFragment;
    private ChatFragment chatFragment;
    private AttendedFragment attendedFragment;
    public View view;
    private OnFragmentInteractionListener mListener;


    public DebateFragment() {

    }

    public static DebateFragment newInstance() {
        DebateFragment fragment = new DebateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_debate, container, false);
        ButterKnife.bind(this, view);

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bmob.initialize(getActivity(), "4ab85f97c2bc54d88133be3de0df4131", "bmob");
        mActivity = getActivity();
        inflater = LayoutInflater.from(getActivity());


        radioContest.setOnClickListener(this);
        radioChat.setOnClickListener(this);
        radioAttended.setOnClickListener(this);

        setDefaultFragment();
    }

    private void setDefaultFragment()
    {
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        contestFragment = new ContestFragment();
        transaction.add(R.id.framelayout, contestFragment).commit();
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
    public void onClick(View v) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = childFragmentManager.beginTransaction();

        switch (v.getId())
        {
            case R.id.radioContest:
                if (contestFragment == null)
                {
                    contestFragment = new ContestFragment();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.framelayout, contestFragment);
                break;
            case R.id.radioChat:
                if (chatFragment == null)
                {
                    chatFragment = new ChatFragment();
                }
                transaction.replace(R.id.framelayout, chatFragment);
                break;
            case R.id.radioAttended:
                if (attendedFragment == null)
                {
                    attendedFragment = new AttendedFragment();
                }
                transaction.replace(R.id.framelayout, attendedFragment);

                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();

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
