package com.example.bigtalk.bigtalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.bigtalk.bigtalk.R;
import com.example.bigtalk.bigtalk.contest.UserJoinContestActivity;
import com.example.bigtalk.bigtalk.contest.WatchContestActivity;
import com.example.bigtalk.bigtalk.bean.Contest;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    List<Contest> items;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size() == 0 ? 0 : items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public MyRecyclerViewAdapter(Context context,List<Contest> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.contest_item, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).join_text_conStatus.setText((CharSequence) items.get(position).getTopic().getTopic_status());
            ((ItemViewHolder) holder).join_text_topics.setText((CharSequence) items.get(position).getTopic().getTopic_name());
            ((ItemViewHolder) holder).join_text_time.setText((CharSequence) items.get(position).getContest_date()+"  "+(CharSequence) items.get(position).getContest_time());
            ((ItemViewHolder) holder).btn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context, UserJoinContestActivity.class);
                    intent1.putExtra("contestIdSent",items.get(position).getContest_id());
                    context.startActivity(intent1);
                }
            });
            ((ItemViewHolder) holder).btn_watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(context, WatchContestActivity.class);
                    intent2.putExtra("contestIdSent",items.get(position).getContest_id());
                    context.startActivity(intent2);
                }
            });
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
    }


    public class ItemViewHolder extends  RecyclerView.ViewHolder {
        TextView join_text_conStatus,join_text_topics,join_text_time;
        Button btn_join,btn_watch;
        public ItemViewHolder(View itemView) {
            super(itemView);
            join_text_conStatus = (TextView) itemView.findViewById(R.id.join_text_conStatus);
            join_text_topics = (TextView) itemView.findViewById(R.id.join_text_topics);
            join_text_time = (TextView) itemView.findViewById(R.id.join_text_time);
            btn_join = (Button) itemView.findViewById(R.id.btn_join);
            btn_watch = (Button) itemView.findViewById(R.id.btn_watch);
        }
    }

    public class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

}
