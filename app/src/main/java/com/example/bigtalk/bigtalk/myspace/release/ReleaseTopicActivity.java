package com.example.bigtalk.bigtalk.myspace.release;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.frontpage.bean.Topics;
import com.example.bigtalk.bigtalk.tools.ForgetPswActivity;
import com.example.bigtalk.bigtalk.tools.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ReleaseTopicActivity extends AppCompatActivity {


    EditText release_topic_edit_name;
    EditText release_topic_edit_posi;
    EditText release_topic_edit_nega;

    EditText release_topic_edit_intro;

    Spinner spinner;

    CheckBox checkbox;

    Button release_topic_btn_submit;

    private ArrayAdapter adapter;
    String topicName,posi,nega,topicType,topicIntro;
    User user;
    String censorStatus = "fail";
    Topics topic;
    Context context;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_topic);


        //将可选内容与ArrayAdapter连接起来
        adapter = ArrayAdapter.createFromResource(this, R.array.ptnType, android.R.layout.simple_spinner_item);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter2 添加到spinner中


        release_topic_edit_name = (EditText)findViewById(R.id.release_topic_edit_name);
        release_topic_edit_posi = (EditText)findViewById(R.id.release_topic_edit_posi);
        release_topic_edit_nega = (EditText)findViewById(R.id.release_topic_edit_nega);

        release_topic_edit_intro = (EditText)findViewById(R.id.release_topic_edit_intro);
        spinner = (Spinner)findViewById(R.id.release_topic_spinner);
        checkbox = (CheckBox)findViewById(R.id.release_topic_ckb);

        checkbox.setOnCheckedChangeListener(myListener);
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

        toolbar = (Toolbar) findViewById(R.id.release_topic_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        //使能app bar的导航功能
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        release_topic_btn_submit = (Button)findViewById(R.id.release_topic_btn_submit);
        release_topic_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicName = release_topic_edit_name.getText().toString();
                posi = release_topic_edit_posi.getText().toString();
                nega = release_topic_edit_nega.getText().toString();
                topicIntro = release_topic_edit_intro.getText().toString();

                user = BmobUser.getCurrentUser( User.class);

                topic = new Topics();
                topic.setTopic_name(topicName);
                topic.setPosi_opi(posi);
                topic.setNega_opi(nega);
                topic.setTopic_intro(topicIntro);
                topic.setTopic_type(topicType);
                topic.setTopic_status("waitForU");
                topic.setCensor_status(censorStatus);
                topic.setTopic_author(user);
                topic.setNews_like(1);
                topic.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
//                            toast("添加数据成功，返回objectId为："+objectId);
//                            AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
////                            .setIcon(R.mipmap.icon)//设置标题的图片
//                                    .setTitle("提交成功！")//设置对话框的标题
//                                    .setMessage("")//设置对话框的内容
//                                    //设置对话框的按钮
//                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(ReleaseTopicActivity.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
//
//                                            dialog.dismiss();
//                                            finish();
//                                        }
//                                    }).create();
//                            dialog.show();

                            Toast.makeText(ReleaseTopicActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReleaseTopicActivity.this, FrameActivity.class);
                            startActivity(intent);

                        }else{
//                            toast("创建数据失败：" + e.getMessage());
                        }
                    }
                });
            }
        });

    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle("发布辩题");
        }
    }
    private CompoundButton.OnCheckedChangeListener myListener  =  new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                //获取复选框对应的值
                censorStatus = "fail";

            }
        }

    };

    //使用XML形式操作
    class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
            if((String)adapter.getItem(position) == "事实辩题"){
                topicType = "事实辩题";

            }else if((String)adapter.getItem(position) == "价值辩题"){
                topicType = "价值辩题";
            }else if((String)adapter.getItem(position) == "政策辩题"){
                topicType = "政策辩题";
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            topicType = "事实辩论";
        }

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
