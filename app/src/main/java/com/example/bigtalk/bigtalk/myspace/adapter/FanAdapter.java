package com.example.bigtalk.bigtalk.myspace.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.User;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.myspace.bean.Fan;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/2/18.
 */

public class FanAdapter extends BaseRecyclerAdapter<User> {

    public FanAdapter(Context context, IMutlipleItem<User> items, Collection<User> datas) {
        super(context, items, datas);
    }
    @Override
    public void bindView(final BaseRecyclerHolder holder, final User add, int position) {
//        holder.setImageView(String.valueOf(add == null ? null : add.getFan().getAvatar()), R.mipmap.head2, R.id.fan_avatar);
        holder.setText(R.id.fan_name, add == null ? "未知" : add.getUsername());

    }

}
