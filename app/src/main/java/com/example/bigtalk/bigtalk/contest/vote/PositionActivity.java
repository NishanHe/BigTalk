package com.example.bigtalk.bigtalk.contest.vote;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.ContestNegaUser;
import com.example.bigtalk.bigtalk.bean.ContestPosiUser;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.contest.UserJoinContestActivity;
import com.example.bigtalk.bigtalk.contest.UserJoinContestSuccessActivity;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class PositionActivity extends AppCompatActivity {

    @Bind(R.id.rbt_1)
    RadioButton rbt1;
    @Bind(R.id.rbt_2)
    RadioButton rbt2;
    @Bind(R.id.rbt_3)
    RadioButton rbt3;
    @Bind(R.id.rbt_4)
    RadioButton rbt4;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.radg)
    RadioGroup radg;

    private String contestID, currName;
    private int hold = 0;
    private String posiChoice = "";
    private String position_user = "";
    private ArrayList<String> posidebaterlist = new ArrayList<>();
    private ArrayList<String> negadebaterlist = new ArrayList<>();
    List<ContestPosiUser> posiDebaterInfo;
    List<ContestNegaUser> negaDebaterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        Intent i = getIntent();
        contestID = i.getStringExtra("contestIdSent");

        User cUser = BmobUser.getCurrentUser(User.class);
        currName = cUser.getObjectId();


        radg.setOnCheckedChangeListener(rdgcc);

        contestID = "01";


        rbt1.setText("正方一辩");
        rbt2.setText("正方二辩");
        rbt3.setText("正方三辩");
        rbt4.setText("正方四辩");



//        //查正方辩手
//        BmobQuery<ContestPosiUser> queryPosiDebater = new BmobQuery<ContestPosiUser>();
//        queryPosiDebater.addWhereEqualTo("contest_id", contestID);
//        queryPosiDebater.include("user1,user2,user3,user4");
//        queryPosiDebater.setLimit(50);
//        queryPosiDebater.findObjects(new FindListener<ContestPosiUser>() {
//            @Override
//            public void done(List<ContestPosiUser> list, BmobException e) {
//                if (e == null) {
//                    if (list.size() > 0) {
//                        for (int i = 0; i < list.size(); i++) {
//                            posiDebaterInfo.add(list.get(i));
//                            Log.i("bmob", "UserJoinContest辩手1 " + posiDebaterInfo.get(i).getUser1().getUsername());
//
//                        }
//                    } else {
//                        Log.i("bmob", "UserJoinContest辩手1   应该没有东西");
////                        Toast.makeText(UserJoinContestActivity.this, "没有查询到相关辩手记录", Toast.LENGTH_SHORT).show();
//                    }
//                    if (currName.equals(posiDebaterInfo.get(0).getUser1().getObjectId()) ||
//                            currName.equals(posiDebaterInfo.get(0).getUser2().getObjectId()) ||
//                            currName.equals(posiDebaterInfo.get(0).getUser3().getObjectId() )||
//                            currName.equals(posiDebaterInfo.get(0).getUser4().getObjectId())) {
//                        hold = 1;
//
//                        rbt1.setText("正方一辩");
//                        rbt2.setText("正方二辩");
//                        rbt3.setText("正方三辩");
//                        rbt4.setText("正方四辩");
//
//                    }
//                } else {
//                    Toast.makeText(PositionActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.i("bmob", "查询失败:" + e.getMessage());
//                }
//            }
//        });
//
//        if (hold != 1) {
//            //查反方辩手
//            BmobQuery<ContestNegaUser> queryNegaDebater = new BmobQuery<ContestNegaUser>();
//            queryNegaDebater.addWhereEqualTo("contest_id", contestID);
//            queryNegaDebater.include("user5,user6,user7,user8");
//            queryNegaDebater.setLimit(50);
//            queryNegaDebater.findObjects(new FindListener<ContestNegaUser>() {
//                @Override
//                public void done(List<ContestNegaUser> list, BmobException e) {
//                    if (e == null) {
//                        if (list.size() > 0) {
//                            for (int i = 0; i < list.size(); i++) {
//                                negaDebaterInfo.add(list.get(i));
//                                Log.i("bmob", "UserJoinContest辩手5" + negaDebaterInfo.get(i).getUser5().getUsername());
//
//                            }
//                        } else {
//                            Log.i("bmob", "UserJoinContest辩手5   应该没有东西");
//
////                        Toast.makeText(UserJoinContestActivity.this, "没有查询到相关辩手记录", Toast.LENGTH_SHORT).show();
//                        }
//                        if (currName.equals(negaDebaterInfo.get(0).getUser5().getObjectId()) ||
//                                currName.equals(negaDebaterInfo.get(0).getUser6().getObjectId()) ||
//                                currName.equals(negaDebaterInfo.get(0).getUser7().getObjectId()) ||
//                                currName.equals(negaDebaterInfo.get(0).getUser8().getObjectId())) {
//                            hold = -1;
//
//                            rbt1.setText("反方一辩");
//                            rbt2.setText("反方二辩");
//                            rbt3.setText("反方三辩");
//                            rbt4.setText("反方四辩");
//
//                        }
//                    } else {
//                        Toast.makeText(PositionActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.i("bmob", "查询失败:" + e.getMessage());
//                    }
//                }
//            });
//        }
//        if (hold == 1){
//            if (currName.equals(posiDebaterInfo.get(0).getUser1().getObjectId())  ){
//                position_user = "position_user1";
//            }else if(currName.equals(posiDebaterInfo.get(0).getUser2().getObjectId()) ){
//                position_user = "position_user2";
//            }else if(currName.equals(posiDebaterInfo.get(0).getUser3().getObjectId()) ){
//                position_user = "position_user3";
//            }else if(currName.equals(posiDebaterInfo.get(0).getUser4().getObjectId()) ){
//                position_user = "position_user4";
//            }
//
//        }else if(hold == -1){
//            if (currName.equals(negaDebaterInfo.get(0).getUser5().getObjectId()) ){
//                position_user = "position_user5";
//            }else if(currName.equals(negaDebaterInfo.get(0).getUser6().getObjectId()) ){
//                position_user = "position_user6";
//            }else if(currName.equals(negaDebaterInfo.get(0).getUser7().getObjectId())){
//                position_user = "position_user7";
//            }else if(currName.equals(negaDebaterInfo.get(0).getUser8().getObjectId())){
//                position_user = "position_user8";
//            }
//        }else{
//            Toast.makeText(PositionActivity.this,"无权选择座位",Toast.LENGTH_SHORT).show();
//
//        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PositionActivity.this,"选座成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PositionActivity.this, FrameActivity.class);
                startActivity(intent);
//                if(hold == 1){
//                    final ContestPosiUser posiDebaterUp = new ContestPosiUser();
//                    posiDebaterUp.setValue(position_user,"posi_"+posiChoice);
//                    posiDebaterUp.update(posiDebaterInfo.get(0).getObjectId(),new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null) {
//                                Toast.makeText(PositionActivity.this,"选座成功",Toast.LENGTH_SHORT).show();
//                                finish();
//
//                            }else{
//                                Toast.makeText(PositionActivity.this,"选座失败",Toast.LENGTH_SHORT).show();
//                                Log.i("bmob", "添加失败:" + e.getMessage());
//                            }
//
//                        }
//                    });
//
//                }else if (hold == -1){
//                    final ContestNegaUser negaDebaterUp = new ContestNegaUser();
//
//                    negaDebaterUp.setValue(position_user,"nega_"+posiChoice);
//                    negaDebaterUp.update(negaDebaterInfo.get(0).getObjectId(),new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null) {
//                                Toast.makeText(PositionActivity.this,"选座成功",Toast.LENGTH_SHORT).show();
//                                finish();
//
//                            }else{
//                                Toast.makeText(PositionActivity.this,"选座失败",Toast.LENGTH_SHORT).show();
//                                Log.i("bmob", "添加失败:" + e.getMessage());
//                            }
//
//                        }
//                    });
//                }


            }
        });

    }

    private RadioGroup.OnCheckedChangeListener rdgcc = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == rbt1.getId()) {
                posiChoice = "1";
            } else if (checkedId == rbt2.getId()) {
                posiChoice = "2";
            } else if (checkedId == rbt3.getId()) {
                posiChoice = "3";
            } else if (checkedId == rbt4.getId()) {
                posiChoice = "4";

            }
        }
    };


}
