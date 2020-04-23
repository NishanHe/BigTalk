package com.example.bigtalk.bigtalk.latest;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.Article;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/3/1.
 */

public class ArticleAdapter extends BaseRecyclerAdapter<Article> {
    public ArticleAdapter(Context context, IMutlipleItem<Article> items, Collection<Article> datas) {
        super(context, items, datas);
    }
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Article add, int position) {
        holder.setText(R.id.card_text_title, add == null ? "未知" : add.getTitle());
        holder.setText(R.id.card_text_content, add == null ? "未知" : add.getIntro());
    }
}
