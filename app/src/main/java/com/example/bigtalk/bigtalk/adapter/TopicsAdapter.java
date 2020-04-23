package com.example.bigtalk.bigtalk.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Topics;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;

import java.util.Collection;

public class TopicsAdapter extends BaseRecyclerAdapter<Topics> {

    public TopicsAdapter(Context context, IMutlipleItem<Topics> items, Collection<Topics> datas) {
        super(context, items, datas);
    }
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Topics add, int position) {
        holder.setText(R.id.train_title, add == null ? "未知" : add.getTopic_name());
        holder.setText(R.id.train_posi, add == null ? "未知" : add.getPosi_opi());
        holder.setText(R.id.train_nega, add == null ? "未知" : add.getNega_opi());

    }


}
