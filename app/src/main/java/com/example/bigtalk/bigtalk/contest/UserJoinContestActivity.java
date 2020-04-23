package com.example.bigtalk.bigtalk.contest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.bean.ContestNegaUser;
import com.example.bigtalk.bigtalk.bean.ContestPosiUser;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.bean.User;

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

public class UserJoinContestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


//    @Bind(R.id.tv_con_title)
//    TextView tvConTitle;
    @Bind(R.id.tv_con_time)
    TextView tvConTime;
//    @Bind(R.id.btn_intro)
//    Button btnIntro;
    @Bind(R.id.tv_posi)
    TextView tvPosi;
    @Bind(R.id.tv_nega)
    TextView tvNega;
    @Bind(R.id.tv_posiseatLeft)
    TextView tvPosiseatLeft;
    @Bind(R.id.tv_negaseatLeft)
    TextView tvNegaseatLeft;
    //    @Bind(R.id.lv_posidebater)
//    ListView lvPosiDebater;
//    @Bind(R.id.lv_negadebater)
//    ListView lvNegaDebater;
    @Bind(R.id.iv_user1)
    ImageView ivUser1;
    @Bind(R.id.tv_user1)
    TextView tvUser1;
    @Bind(R.id.iv_user2)
    ImageView ivUser2;
    @Bind(R.id.tv_user2)
    TextView tvUser2;
    @Bind(R.id.iv_user3)
    ImageView ivUser3;
    @Bind(R.id.tv_user3)
    TextView tvUser3;
    @Bind(R.id.iv_user4)
    ImageView ivUser4;
    @Bind(R.id.tv_user4)
    TextView tvUser4;
    @Bind(R.id.iv_user5)
    ImageView ivUser5;
    @Bind(R.id.tv_user5)
    TextView tvUser5;
    @Bind(R.id.iv_user6)
    ImageView ivUser6;
    @Bind(R.id.tv_user6)
    TextView tvUser6;
    @Bind(R.id.iv_user7)
    ImageView ivUser7;
    @Bind(R.id.tv_user7)
    TextView tvUser7;
    @Bind(R.id.iv_user8)
    ImageView ivUser8;
    @Bind(R.id.tv_user8)
    TextView tvUser8;
    @Bind(R.id.tv_intro)
    TextView tvIntro;
//    @Bind(R.id.horizontallistview1)
//    HorizontalListView hlv;


    private String contestID;
    private ArrayAdapter arr_posi_aAdapter, arr_nega_aAdapter;
    private ArrayList<String> posidebaterlist;
    private ArrayList<String> negadebaterlist;
    List<Contest> contestInfo;
    List<ContestPosiUser> posiDebaterInfo;
    List<ContestNegaUser> negaDebaterInfo;
    Topics currentTopic;
    String title;
    Toolbar toolbar;

//    private HorizontalListViewAdapter hlva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_join_contest);
        ButterKnife.bind(this);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131", "bmob");

        Intent i = getIntent();
        contestID = i.getStringExtra("contestIdSent");
        //test
        contestID = "03";
//
//        hlva=new HorizontalListViewAdapter(this);
//        hlva.notifyDataSetChanged();
//        hlv.setAdapter(hlva);


//        BmobQuery<Topics> queryTest = new BmobQuery<>();
//        queryTest.addWhereEqualTo("","");



        contestInfo = new ArrayList<>();
        posiDebaterInfo = new ArrayList<>();
        negaDebaterInfo = new ArrayList<>();

        BmobQuery<Contest> query = new BmobQuery<Contest>();
        query.addWhereEqualTo("contest_id", contestID);
        query.include("topic");
        query.setLimit(50);
        query.findObjects(new FindListener<Contest>() {
            @Override
            public void done(List<Contest> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            contestInfo.add(list.get(i));
                            Log.i("bmob", "房间id" + contestInfo.get(i).getContest_id());

                        }
                        updateQueryUI(contestInfo);
                    } else {
                        Toast.makeText(UserJoinContestActivity.this, "没有查询到相关比赛记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserJoinContestActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });

        //查正方辩手
        BmobQuery<ContestPosiUser> queryPosiDebater = new BmobQuery<ContestPosiUser>();
        queryPosiDebater.addWhereEqualTo("contest_id", contestID);
        queryPosiDebater.include("user1,user2,user3,user4");
        queryPosiDebater.setLimit(50);
        queryPosiDebater.findObjects(new FindListener<ContestPosiUser>() {
            @Override
            public void done(List<ContestPosiUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            posiDebaterInfo.add(list.get(i));
                            Log.i("bmob", "UserJoinContest辩手1 " + posiDebaterInfo.get(i).getUser1().getUsername());

                        }
                    } else {
                        Log.i("bmob", "UserJoinContest辩手1   应该没有东西");
//                        Toast.makeText(UserJoinContestActivity.this, "没有查询到相关辩手记录", Toast.LENGTH_SHORT).show();
                    }
                    updatePosiDebaterUI(posiDebaterInfo);
                } else {
                    Toast.makeText(UserJoinContestActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });
        //查反方辩手
        BmobQuery<ContestNegaUser> queryNegaDebater = new BmobQuery<ContestNegaUser>();
        queryNegaDebater.addWhereEqualTo("contest_id", contestID);
        queryNegaDebater.include("user5,user6,user7,user8");
        queryNegaDebater.setLimit(50);
        queryNegaDebater.findObjects(new FindListener<ContestNegaUser>() {
            @Override
            public void done(List<ContestNegaUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            negaDebaterInfo.add(list.get(i));
                            Log.i("bmob", "UserJoinContest辩手5" + negaDebaterInfo.get(i).getUser5().getUsername());

                        }
                    } else {
                        Log.i("bmob", "UserJoinContest辩手5   应该没有东西");

//                        Toast.makeText(UserJoinContestActivity.this, "没有查询到相关辩手记录", Toast.LENGTH_SHORT).show();
                    }
                    updateNegaDebaterUI(negaDebaterInfo);
                } else {
                    Toast.makeText(UserJoinContestActivity.this, "查询失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查询失败:" + e.getMessage());
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.join_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

    }



    public void updateQueryUI(List<Contest> contestInfo) {

        //数据的显示
        currentTopic = contestInfo.get(0).getTopic();
        title = currentTopic.getTopic_name();
        tvPosi.setText(currentTopic.getPosi_opi());
        tvNega.setText(currentTopic.getNega_opi());


        Log.i("bmob", "!!!!DATE: " + contestInfo.get(0).getContest_date());
        Log.i("bmob", "!!!!TIME: " + contestInfo.get(0).getContest_time());

        tvConTime.setText(contestInfo.get(0).getContest_date() + "  " + contestInfo.get(0).getContest_time());
        tvIntro.setText(currentTopic.getTopic_intro());

//        btnIntro.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserJoinContestActivity.this, TopicsIntroActivity.class);
//                intent.putExtra("topicID", currentTopic.getTopic_id());
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void updatePosiDebaterUI(List<ContestPosiUser> posiDebaterInfo) {

        posidebaterlist = new ArrayList<>();
        int posiseatleft = 4;
        if (posiDebaterInfo.get(0).getUser1() != null) {
            posidebaterlist.add(posiDebaterInfo.get(0).getUser1().getUsername());
            tvUser1.setText(posiDebaterInfo.get(0).getUser1().getUsername());
            ivUser1.setImageResource(R.drawable.cir8);
            posiseatleft = posiseatleft - 1;
        } else {
            posidebaterlist.add("待报名");

            ivUser1.setImageResource(R.drawable.baoming);
        }

        if (posiDebaterInfo.get(0).getUser2() != null) {
            posidebaterlist.add(posiDebaterInfo.get(0).getUser2().getUsername());
            tvUser2.setText(posiDebaterInfo.get(0).getUser2().getUsername());
            ivUser2.setImageResource(R.drawable.cir2);
            posiseatleft = posiseatleft - 1;
        } else {
            posidebaterlist.add("待报名");
            ivUser2.setImageResource(R.drawable.baoming);
        }

        if (posiDebaterInfo.get(0).getUser3() != null) {
            posidebaterlist.add(posiDebaterInfo.get(0).getUser3().getUsername());
            tvUser3.setText(posiDebaterInfo.get(0).getUser3().getUsername());
            ivUser3.setImageResource(R.drawable.cir4);
            posiseatleft = posiseatleft - 1;
        } else {
            posidebaterlist.add("待报名");
            ivUser3.setImageResource(R.drawable.baoming);
        }
        if (posiDebaterInfo.get(0).getUser4() != null) {
            posidebaterlist.add(posiDebaterInfo.get(0).getUser4().getUsername());
            tvUser4.setText(posiDebaterInfo.get(0).getUser4().getUsername());
            ivUser4.setImageResource(R.drawable.cir1);
            posiseatleft = posiseatleft - 1;
        } else {
            posidebaterlist.add("待报名");
            ivUser4.setImageResource(R.drawable.baoming);
        }

        ivUser1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser1.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updatePosiDebater(1);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        ivUser2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser2.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updatePosiDebater(2);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        ivUser3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser3.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updatePosiDebater(3);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        ivUser4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser4.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updatePosiDebater(4);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        //绑定正方辩手名单
        arr_posi_aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, posidebaterlist);
//        lvPosiDebater.setAdapter(arr_posi_aAdapter);
//
        //计算剩余多少个位置
        Log.i("bmob", "正方剩余" + posiseatleft);
        tvPosiseatLeft.setText("正方还剩" + posiseatleft + "个席位");

//        //选择正反方选手名单进行报名：如果该项不为空，则提示“该位置已被锁定，请选择其他位置”；如果为空，则执行update操作：一个popupwindow
//        lvPosiDebater.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                if (posidebaterlist.get(position).equals("待报名")) {
//                    // show dialog to choose role
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
//                    builder.setMessage(R.string.confirm_register);
//                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            updatePosiDebater(position + 1);
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                } else {
//                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });


    }

    public void updateNegaDebaterUI(List<ContestNegaUser> negaDebaterInfo) {

        negadebaterlist = new ArrayList<>();
        int negaseatleft = 4;
        if (negaDebaterInfo.get(0).getUser5() != null) {
            negadebaterlist.add(negaDebaterInfo.get(0).getUser5().getUsername());
            tvUser5.setText(negaDebaterInfo.get(0).getUser5().getUsername());
            ivUser5.setImageResource(R.drawable.cir5);
            negaseatleft = negaseatleft - 1;
        } else {
            negadebaterlist.add("待报名");
            ivUser5.setImageResource(R.drawable.baoming);
        }

        if (negaDebaterInfo.get(0).getUser6() != null) {
            negadebaterlist.add(negaDebaterInfo.get(0).getUser6().getUsername());
            tvUser6.setText(negaDebaterInfo.get(0).getUser6().getUsername());
            ivUser6.setImageResource(R.drawable.cir7);
            negaseatleft = negaseatleft - 1;
        } else {
            negadebaterlist.add("待报名");
            ivUser6.setImageResource(R.drawable.baoming);
        }

        if (negaDebaterInfo.get(0).getUser7() != null) {
            negadebaterlist.add(negaDebaterInfo.get(0).getUser7().getUsername());
            tvUser7.setText(negaDebaterInfo.get(0).getUser7().getUsername());
            ivUser7.setImageResource(R.drawable.cir6);
            negaseatleft = negaseatleft - 1;
        } else {
            negadebaterlist.add("待报名");
            ivUser7.setImageResource(R.drawable.baoming);
        }
        if (negaDebaterInfo.get(0).getUser8() != null) {
            negadebaterlist.add(negaDebaterInfo.get(0).getUser8().getUsername());
            tvUser8.setText(negaDebaterInfo.get(0).getUser8().getUsername());
            ivUser8.setImageResource(R.drawable.cir3);
            negaseatleft = negaseatleft - 1;
        } else {
            negadebaterlist.add("待报名");
            ivUser8.setImageResource(R.drawable.baoming);
        }


        ivUser5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser5.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateNegaDebater(5);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        ivUser6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser6.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateNegaDebater(6);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        ivUser7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser7.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateNegaDebater(7);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        ivUser8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ivUser8.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.baoming).getConstantState()) {
                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
                    builder.setMessage(R.string.confirm_register);
                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateNegaDebater(8);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        Log.i("bmob", "反方名单" + negadebaterlist.get(3));


        //绑定反方辩手名单
        arr_nega_aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, negadebaterlist);
//        lvNegaDebater.setAdapter(arr_nega_aAdapter);
//

        //计算剩余多少个位置
        Log.i("bmob", "反方剩余" + negaseatleft);
        tvNegaseatLeft.setText("反方还剩" + negaseatleft + "个席位");
//
//        //选择正反方选手名单进行报名：如果该项不为空，则提示“该位置已被锁定，请选择其他位置”；如果为空，则执行update操作：一个popupwindow
//        lvNegaDebater.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                if (negadebaterlist.get(position).equals("待报名")) {
//                    // show dialog to choose role
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserJoinContestActivity.this);
//                    builder.setMessage(R.string.confirm_register);
//                    builder.setNegativeButton(R.string.label_cancelRegister, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setPositiveButton(R.string.label_register, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            updateNegaDebater(position + 5);
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                } else {
//                    Toast.makeText(UserJoinContestActivity.this, "该位置已被锁定，请选择其他位置", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

    }

    public void updatePosiDebater(int position) {
        User cUser = BmobUser.getCurrentUser(User.class);
        final ContestPosiUser posiDebaterUp = new ContestPosiUser();
        String keyValue = String.format("user%1$s", position);
        posiDebaterUp.setValue(keyValue, cUser);
        posiDebaterUp.update(posiDebaterInfo.get(0).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserJoinContestActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserJoinContestActivity.this, UserJoinContestSuccessActivity.class);
                    intent.putExtra("contestIdSent", contestID);
                    startActivity(intent);

                } else {
                    Toast.makeText(UserJoinContestActivity.this, "报名失败", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "添加失败:" + e.getMessage());
                }

            }
        });
    }

    public void updateNegaDebater(int position) {
        User cUser = BmobUser.getCurrentUser(User.class);

        final ContestNegaUser negaDebaterUp = new ContestNegaUser();

        String keyValue = String.format("user%1$s", position);

        negaDebaterUp.setValue(keyValue, cUser);
        negaDebaterUp.update(negaDebaterInfo.get(0).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserJoinContestActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserJoinContestActivity.this, UserJoinContestSuccessActivity.class);
                    intent.putExtra("contestIdSent", contestID);
                    startActivity(intent);

                } else {
                    Toast.makeText(UserJoinContestActivity.this, "报名失败", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "添加失败:" + e.getMessage());
                }

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub

        //android.R.id.home对应应用程序图标的id
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
