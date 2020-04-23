package com.example.bigtalk.bigtalk.myspace.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.frontpage.bean.Contest;
import com.example.bigtalk.bigtalk.frontpage.bean.Topics;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class TopicAdapter extends BaseRecyclerAdapter<Topics> {
    public TopicAdapter(Context context, IMutlipleItem<Topics> items, Collection<Topics> datas) {
        super(context, items, datas);
    }
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Topics add, int position) {
        holder.setText(R.id.card_text_title, add == null ? "未知" : add.getTopic_name());
        holder.setText(R.id.card_text_content, add == null ? "未知" : "辩题");

    }
}
