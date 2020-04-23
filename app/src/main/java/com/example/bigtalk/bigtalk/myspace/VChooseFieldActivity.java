package com.example.bigtalk.bigtalk.myspace;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.myspace.bean.Master;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class VChooseFieldActivity extends ParentWithNaviActivity {

    @Bind(R.id.v_field_btn_society)
    Button v_field_btn_society;
    @Bind(R.id.v_field_btn_entertain)
    Button v_field_btn_entertain;
    @Bind(R.id.v_field_btn_science)
    Button v_field_btn_science;
    @Bind(R.id.v_field_btn_computer)
    Button v_field_btn_computer;
    @Bind(R.id.v_field_btn_humanity)
    Button v_field_btn_humanity;
    @Bind(R.id.v_field_btn_debater)
    Button v_field_btn_debater;
    @Bind(R.id.v_field_btn_critic)
    Button v_field_btn_critic;
    @Bind(R.id.v_field_btn_summit)
    Button v_field_btn_summit;


    int clickTimeField = 0;    //领域和身份都只能选一个，用于计数，点击一个领域/身份按钮，则加一，大于一则弹出提示框，并不执行点击后的行为
    int clickTimeType = 0;
    String field;
    String type;
    User user;
    Master master;
    private Context mContext;
    AlertDialog dialog1;


    //button使用selector---按下变为实心，获取button的text，在点击下一步的时候提交，只能选择一个领域
    @Override
    protected String title() {
        return "达人认证";
    }

    @Override
    public Object left() {
        return R.drawable.base_action_bar_back_bg_selector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vchoose_field);

        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

    }

    @OnClick({R.id.v_field_btn_society, R.id.v_field_btn_entertain,R.id.v_field_btn_science,R.id.v_field_btn_computer,
            R.id.v_field_btn_humanity,R.id.v_field_btn_debater,R.id.v_field_btn_critic,R.id.v_field_btn_summit})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.v_field_btn_society:
                field = v_field_btn_society.getText().toString();
                clickTimeField = clickTimeField + 1;
                break;
            case R.id.v_field_btn_entertain:
                field = v_field_btn_entertain.getText().toString();
                clickTimeField = clickTimeField + 1;
                break;
            case R.id.v_field_btn_science:
                field = v_field_btn_science.getText().toString();
                clickTimeField = clickTimeField + 1;
                break;
            case R.id.v_field_btn_computer:
                field = v_field_btn_computer.getText().toString();
                clickTimeField = clickTimeField + 1;
                break;
            case R.id.v_field_btn_humanity:
                field = v_field_btn_humanity.getText().toString();
                clickTimeField = clickTimeField + 1;
                break;
            case R.id.v_field_btn_debater:
                type = v_field_btn_debater.getText().toString();
                clickTimeType = clickTimeType + 1;
                break;
            case R.id.v_field_btn_critic:
                type = v_field_btn_critic.getText().toString();
                clickTimeType = clickTimeType + 1;
                break;
            case R.id.v_field_btn_summit:
                if(clickTimeField == 0 || clickTimeType == 0){
                    toast("请选择达人领域和达人类型");
                }else if(clickTimeField > 1 || clickTimeType > 1){
                    toast("只限选择一个达人领域和一种达人类型!");
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(this)
//                            .setIcon(R.mipmap.icon)//设置标题的图片
                            .setTitle("确定提交达人申请吗？")//设置对话框的标题
                            .setMessage("提交申请后不可取消，结果将在1-2日内通知用户，届时用户可以开始享受达人认证。")//设置对话框的内容
                            //设置对话框的按钮
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(VChooseFieldActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(VChooseFieldActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                                    user = BmobUser.getCurrentUser( User.class);
                                    master = new Master();
                                    master.setUser_id(user.getUsername());
                                    master.setMaster_field(field);
                                    master.setMaster_type(type);
                                    master.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String objectId,BmobException e) {
                                            if(e==null){
                                                toast("添加数据成功，返回objectId为："+objectId);
                                                dialog1 = new AlertDialog.Builder(mContext)
//                            .setIcon(R.mipmap.icon)//设置标题的图片
                                                        .setTitle("确定提交达人申请吗？")//设置对话框的标题
                                                        .setMessage("提交申请后不可取消，结果将在1-2日内通知用户，届时用户可以开始享受达人认证。")//设置对话框的内容
                                                        //设置对话框的按钮
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Toast.makeText(VChooseFieldActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                                                                dialog1.dismiss();
                                                                finish();

                                                            }
                                                        }).create();
                                                dialog1.show();

                                            }else{
                                                toast("创建数据失败：" + e.getMessage());
                                            }
                                        }
                                    });
                                    dialog.dismiss();

                                }
                            }).create();
                    dialog.show();

                }

                break;
        }
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                startActivity(VRegisterActivity.class,null);
            }

            @Override
            public void clickRight() {}

            @Override
            public void clickRight2() {}

        };

    }
}
