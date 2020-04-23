package com.example.bigtalk.bigtalk.myspace;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ImageLoaderFactory;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
import com.example.bigtalk.bigtalk.bean.User;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditMyFileActivity extends ParentWithNaviActivity {

    @Bind(R.id.myfile_img_avatar)
    CircleImageView myfile_img_avator;
    @Bind(R.id.myfile_edit_username)
    EditText myfile_edit_username;
    @Bind(R.id.myfile_edit_field)
    EditText myfile_edit_field;
    @Bind(R.id.myfile_edit_signature)
    EditText myfile_edit_signature;
    @Bind(R.id.myfile_text_id)
    TextView myfile_text_id;




    String userid;   //当前用户或其他用户id
    User user;
    String path;

    @Override
    protected String title() {
        return "编辑个人资料";
    }
    @Override
    public Object left() {
        return R.drawable.base_action_bar_back_bg_selector;
    }
    @Override
    public Object right() {
        return "提交";
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_file);
        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");
        initNaviView();

        user = BmobUser.getCurrentUser( User.class);
        ImageLoaderFactory.getLoader().loadAvator(myfile_img_avator, user.getAvatar(), R.mipmap.head2);

        //从本地图库获得图片url
        myfile_img_avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //得到图片url
            Uri uri = data.getData();
            path = getImagePath(uri, null);
            ContentResolver cr = this.getContentResolver();
            try {
                Log.e("qwe", path.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                /* 将Bitmap设定到ImageView */
                myfile_img_avator.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("qwe", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }
    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {
                final User newUser = new User();
                final BmobFile file = new BmobFile(new File(path));
                newUser.setAvatar(file);
                newUser.setUsername(myfile_edit_username.getText().toString());
                newUser.setField(myfile_edit_field.getText().toString());
                newUser.setSignature(myfile_edit_signature.getText().toString());
                newUser.update(userid, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            toast("更新成功:"+ newUser.getUpdatedAt());
                        }else{
                            toast("更新失败：" + e.getMessage());
                        }
                    }

                });

            }

            @Override
            public void clickRight2() {

            }
        };
    }


}
