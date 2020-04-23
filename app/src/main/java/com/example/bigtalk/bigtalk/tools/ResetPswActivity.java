package com.example.bigtalk.bigtalk.tools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class ResetPswActivity extends AppCompatActivity {

    EditText psw_old, psw_new, psw_new_again;
    String oldPass, newPass, confirmNewPass;
    Button confirm;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_psw);

        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        confirm = (Button)findViewById(R.id.reset_btn_sonfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psw_old = (EditText)findViewById(R.id.reset_edit_psw_origin);
                psw_new = (EditText)findViewById(R.id.reset_edit_psw_new);
                psw_new_again = (EditText)findViewById(R.id.reset_edit_psw_new_again);

                oldPass = psw_old.getText().toString();
                newPass = psw_new.getText().toString();
                confirmNewPass = psw_new_again.getText().toString();

                user = BmobUser.getCurrentUser(User.class);

//                if(oldPass.equals("")){
//                    Toast.makeText(ResetPswActivity.this, "请输入用户名！", Toast.LENGTH_LONG).show();
//                }else if( user!=null && oldPass.equals(user.get)){
//                    user.setValue("password", newPass);
//                    user.update(user.getObjectId(), new UpdateListener() {
//                        @Override
//                        public void done(cn.bmob.v3.exception.BmobException e) {
//                            if(e==null){
//                                Log.i("bmob","更新成功");
//                                Toast.makeText(ResetPswActivity.this, "更新成功！", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(ResetPswActivity.this, AccountSafetyActivity.class);
//                                startActivity(intent);
//                            }else{
//                                Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
//                            }
//                        }
//
//                    });
//                }
            }
        });
    }
}
