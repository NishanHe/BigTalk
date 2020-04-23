package com.example.bigtalk.bigtalk.frontpage.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.bean.Article;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;

import java.util.Collection;

/**
 * Created by carbon on 2018/5/3.
 */

public class CollectArticleAdapter extends BaseRecyclerAdapter<Article> {
    public CollectArticleAdapter(Context context, IMutlipleItem<Article> items, Collection<Article> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(final BaseRecyclerHolder holder, final Article add, int position) {
        holder.setText(R.id.card_text_title, add == null ? "未知" : add.getTitle());
        holder.setText(R.id.card_text_content, add == null ? "未知" : add.getIntro());
    }
}
