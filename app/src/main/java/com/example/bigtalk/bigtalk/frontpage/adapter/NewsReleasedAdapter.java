package com.example.bigtalk.bigtalk.frontpage.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.frontpage.bean.NewsReleased;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class NewsReleasedAdapter extends BaseRecyclerAdapter<NewsReleased> {

    public NewsReleasedAdapter(Context context, IMutlipleItem<NewsReleased> items, Collection<NewsReleased> datas) {
        super(context, items, datas);
    }
    @Override
    public void bindView(final BaseRecyclerHolder holder, final NewsReleased add, int position) {
        holder.setText(R.id.card_text_title, add == null ? "未知" : add.getNews().getTitle());
        holder.setText(R.id.card_text_content, add == null ? "未知" : add.getNews().getContent());
    }


}
