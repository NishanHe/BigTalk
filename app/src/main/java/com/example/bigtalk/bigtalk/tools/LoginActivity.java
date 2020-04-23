package com.example.bigtalk.bigtalk.tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.contest.ContestINGActivity;
import com.example.bigtalk.bigtalk.FrameActivity;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.contest.PlayContestActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {

    EditText userPhone,userPassword;
    TextView logo,forgetPass,register;
    TextView line1,line2;
    Button sure;
    String phone = "";
    String code = "";
//    User user;
//    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
        //给logo设置自定义字体
//        logo = (TextView)findViewById(R.id.logo_text);
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "logo_font.otf");
//        if(typeface != null){
//            logo.setTypeface(typeface);
//        }


        line1 = (TextView)findViewById(R.id.login_line1);
        line2 = (TextView)findViewById(R.id.login_line2);

        userPhone  = (EditText)findViewById(R.id.login_edit_phone);
        userPassword = (EditText)findViewById(R.id.login_edit_psw);
        if(userPhone != null){
            userPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    line1.setBackgroundResource(R.color.colorAccent);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    line1.setBackgroundResource(R.color.color_line);
                }
            });
        }

        if(userPhone != null){
            userPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    line2.setBackgroundResource(R.color.colorAccent);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    line2.setBackgroundResource(R.color.color_line);
                }
            });
        }

        setContentView(R.layout.activity_login);

        sure = (Button)findViewById(R.id.login_btn_confirm);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone  = (EditText)findViewById(R.id.login_edit_phone);
                userPassword = (EditText)findViewById(R.id.login_edit_psw);

                if(userPhone != null && userPhone.getText() != null){
                    phone = userPhone.getText().toString();
                }

                if(userPassword != null && userPassword.getText() != null){
                    code = userPassword.getText().toString();
                }

                if(phone.isEmpty() || code.isEmpty()){
                    Toast.makeText(LoginActivity.this, "手机号或密码不能为空", Toast.LENGTH_LONG).show();
                }else{

//                    BmobUser newuser = new BmobUser();
//                    newuser.setUsername(phone);
////                    newuser.setMobilePhoneNumber(phone);
//                    newuser.setPassword(code);
//                    newuser.login(new SaveListener<BmobUser>() {
//                        @Override
//                        public void done(BmobUser bmobUser, BmobException e) {
//                            if(e==null){
//                                Intent intent = new Intent(LoginActivity.this, FrameActivity.class);
//                                startActivity(intent);
//                            }else{
//                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                            }
//                        }
//                    });

                    System.out.println("快开始了");
                    System.out.println("收到用户名" + phone);
                    System.out.println("收到密码" + code);
                    BmobUser.loginByAccount(phone,code, new LogInListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            System.out.println("进入查询");
                            if(user!=null){
                                Log.i("smile","用户登录成功");
                                System.out.println("登录成功");
                                Intent intent = new Intent(LoginActivity.this, FrameActivity.class);
                                startActivity(intent);

                            }
                        }
                    });

                }

            }
        });

        forgetPass = (TextView)findViewById(R.id.login_text_forget_psw);
        register = (TextView)findViewById(R.id.login_text_register);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPswActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }


//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
