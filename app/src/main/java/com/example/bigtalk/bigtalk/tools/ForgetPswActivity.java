package com.example.bigtalk.bigtalk.tools;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPswActivity extends AppCompatActivity {


    EditText userPassword,userRePassword, userPhone, userCode;
    String password, repassword ,phone, code;
    Button verify, sure;
    User user;
    String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");
        BmobSMS.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        verify = (Button)findViewById(R.id.forget_psw_btn_verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = (EditText) findViewById(R.id.forget_psw_edit_phone);
                if(userPhone != null && userPhone.getText() != null){
                    phone = userPhone.getText().toString();

                }

                if(phone.isEmpty()) {
                    Toast.makeText(ForgetPswActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if(phone.length()!=11) {
                    Toast.makeText(ForgetPswActivity.this, "请输入11位有效号码", Toast.LENGTH_LONG).show();
                    return;
                }
                //进行获取验证码操作和倒计时1分钟操作
                BmobSMS.requestSMSCode(ForgetPswActivity.this, phone, "login", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer integer, BmobException e) {
                        System.out.println("进来了！！！！！");
                        if (e == null) {
                            //发送成功时，让获取验证码按钮不可点击，且为灰色
                            System.out.println("发送成功！！！！！");
                            verify.setClickable(false);
                            verify.setBackgroundColor(Color.GRAY);
                            Toast.makeText(ForgetPswActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                            /**
                             * 倒计时1分钟操作
                             * 说明：
                             * new CountDownTimer(60000, 1000),第一个参数为倒计时总时间，第二个参数为倒计时的间隔时间
                             * 单位都为ms，其中必须要实现onTick()和onFinish()两个方法，onTick()方法为当倒计时在进行中时，
                             * 所做的操作，它的参数millisUntilFinished为距离倒计时结束时的时间，以此题为例，总倒计时长
                             * 为60000ms,倒计时的间隔时间为1000ms，然后59000ms、58000ms、57000ms...该方法的参数
                             * millisUntilFinished就等于这些每秒变化的数，然后除以1000，把单位变成秒，显示在textView
                             * 或Button上，则实现倒计时的效果，onFinish()方法为倒计时结束时要做的操作，此题可以很好的
                             * 说明该方法的用法，最后要注意的是当new CountDownTimer(60000, 1000)之后，一定要调用start()
                             * 方法把该倒计时操作启动起来，不调用start()方法的话，是不会进行倒计时操作的
                             */
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    verify.setBackgroundResource(R.drawable.button_style_full);
                                    verify.setTextColor(getResources().getColor(R.color.base_color_text_white));
                                    verify.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    verify.setClickable(true);
                                    verify.setBackgroundResource(R.drawable.button_style_empty);
                                    verify.setText("重新发送");
                                }
                            }.start();
                            Log.e("MESSAGE:", "4");
                        } else {
                            Toast.makeText(ForgetPswActivity.this, e.getErrorCode() + "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        sure = (Button)findViewById(R.id.forget_psw_btn_confirm);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = (EditText)findViewById(R.id.forget_psw_edit_psw);
                userRePassword = (EditText)findViewById(R.id.forget_psw_edit_psw_again);
                userCode = (EditText)findViewById(R.id.forget_psw_edit_code);

                if(userPassword != null && userPassword.getText() != null){
                    password = userPassword.getText().toString();
                }
                if(userRePassword != null && userRePassword.getText() != null){
                    repassword = userRePassword.getText().toString();
                }
                if(userCode != null && userCode.getText() != null){
                    code = userCode.getText().toString();
                }



                if(password.isEmpty()){
                    Toast.makeText(ForgetPswActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                }
                if(repassword.isEmpty()){
                    Toast.makeText(ForgetPswActivity.this, "请确认密码！", Toast.LENGTH_SHORT).show();
                }
                if(!repassword.equals(password)){
                    Toast.makeText(ForgetPswActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                }

                if(code.isEmpty()){
                    Toast.makeText(ForgetPswActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                }

                BmobSMS.verifySmsCode(ForgetPswActivity.this, phone, code, new VerifySMSCodeListener() {

                    @Override
                    public void done(BmobException ex) {
                        // TODO Auto-generated method stub
                        if (ex == null) {//短信验证码已验证成功
                            Log.i("bmob", "验证通过");

                            user = new User();
                            BmobQuery<User> query = new BmobQuery<User>();
                            query.addWhereEqualTo("mobilePhoneNumber", phone);
                            query.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> object, cn.bmob.v3.exception.BmobException e) {
                                    if(e==null){
                                        for (User u : object) {
                                            //获得playerName的信息
                                            // ;
                                            user = u;
                                            objectId = user.getObjectId();
                                        }
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });

                            user.setValue("password", password);
                            user.update(objectId, new UpdateListener() {
                                @Override
                                public void done(cn.bmob.v3.exception.BmobException e) {
                                    if(e==null){
                                        Log.i("bmob","更新成功");
                                    }else{
                                        Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }

                            });


                        } else {
                            Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                        }

                    }
                });

            }



        });
    }
}
