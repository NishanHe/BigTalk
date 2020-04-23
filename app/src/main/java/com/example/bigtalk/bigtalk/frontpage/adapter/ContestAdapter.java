package com.example.bigtalk.bigtalk.frontpage.adapter;

import android.content.Context;

import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerAdapter;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.BaseRecyclerHolder;
import com.example.bigtalk.bigtalk.chat.adapter.base_adapter.IMutlipleItem;
import com.example.bigtalk.bigtalk.bean.Contest;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/3/3.
 */

public class ContestAdapter extends BaseRecyclerAdapter<Contest> {

        public ContestAdapter(Context context, IMutlipleItem<Contest> items, Collection<Contest> datas) {
                super(context, items, datas);
        }
        @Override
        public void bindView(final BaseRecyclerHolder holder, final Contest add, int position) {
                holder.setText(R.id.train_title, add == null ? "未知" : add.getTopic().getTopic_name());
                holder.setText(R.id.train_posi, add == null ? "未知" : add.getTopic().getPosi_opi());
                holder.setText(R.id.train_nega, add == null ? "未知" : add.getTopic().getNega_opi());

//                holder.setText(R.id.card_text_content, add == null ? "未知" : add.getTopic().getTopic_name()));
        }


}
