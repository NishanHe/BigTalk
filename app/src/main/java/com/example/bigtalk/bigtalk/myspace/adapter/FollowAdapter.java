package com.example.bigtalk.bigtalk.myspace.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.Follow;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/2/19.
 */

public class FollowAdapter extends BaseRecyclerAdapter<User> {

    public FollowAdapter(Context context, IMutlipleItem<User> items, Collection<User> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, User add, int position) {
//        holder.setImageView(String.valueOf(add == null ? null : add.getFollow().getAvatar()), R.mipmap.head2, R.id.follow_avatar);
        holder.setText(R.id.follow_name, add == null ? "未知" : add.getUsername());
    }
}
