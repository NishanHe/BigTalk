package com.example.bigtalk.bigtalk.frontpage.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.latest.bean.Collect;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class CollectAdapter extends BaseRecyclerAdapter<Collect> {
    public CollectAdapter(Context context, IMutlipleItem<Collect> items, Collection<Collect> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(final BaseRecyclerHolder holder, final Collect add, int position) {
        holder.setText(R.id.card_text_title, add == null ? "未知" : add.getNews().getTitle());
        holder.setText(R.id.card_text_content, add == null ? "未知" : add.getNews().getContent());
    }
}
