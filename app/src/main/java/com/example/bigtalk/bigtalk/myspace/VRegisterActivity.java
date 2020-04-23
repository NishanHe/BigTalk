package com.example.bigtalk.bigtalk.myspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.base.ParentWithNaviActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.AboutUsActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.AccountSafetyActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.CacheClearActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.CheckLatestActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.FeedbackActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.LanguageSetActivity;
//import com.example.bigtalk.bigtalk.myspace.settings.MessageNoteActivity;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class VRegisterActivity extends ParentWithNaviActivity {

    @Bind(R.id.v_btn_next)
    Button v_btn_next;

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
        setContentView(R.layout.activity_vregister);

        Bmob.initialize(this, "4ab85f97c2bc54d88133be3de0df4131");

        initNaviView();

    }

    @OnClick({R.id.v_btn_next})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.v_btn_next:
                startActivity(VChooseFieldActivity.class,null);
                break;

        }
    }


    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {}

            @Override
            public void clickRight2() {}

        };

    }

}
