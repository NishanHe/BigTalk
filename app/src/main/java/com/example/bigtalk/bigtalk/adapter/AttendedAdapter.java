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
import com.example.bigtalk.bigtalk.bean.Contest;
import com.example.bigtalk.bigtalk.contest.ContestINGActivity;
import com.example.bigtalk.bigtalk.contest.PlayContestActivity;
import com.example.bigtalk.bigtalk.contest.vote.PositionActivity;

import java.util.List;


public class AttendedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    List<Contest> items;
    private OnItemClickListener onItemClickListener;

    public AttendedAdapter(Context context,List<Contest> items) {
        this.items = items;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.contest_attended, parent,
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
        if (holder instanceof AttendedAdapter.ItemViewHolder) {
            ((AttendedAdapter.ItemViewHolder) holder).join_text_conStatus.setText((CharSequence) items.get(position).getTopic().getTopic_status());
            ((AttendedAdapter.ItemViewHolder) holder).join_text_conTime.setText((CharSequence) items.get(position).getContest_date()+" "+ (CharSequence) items.get(position).getContest_time());
            ((AttendedAdapter.ItemViewHolder) holder).join_text_topics.setText((CharSequence) items.get(position).getTopic().getTopic_name());
            ((AttendedAdapter.ItemViewHolder) holder).join_text_posi.setText((CharSequence) items.get(position).getTopic().getPosi_opi());
            ((AttendedAdapter.ItemViewHolder) holder).join_text_nega.setText((CharSequence) items.get(position).getTopic().getNega_opi());

            ((ItemViewHolder) holder).btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context, PositionActivity.class);
                    context.startActivity(intent1);
                }
            });
            ((ItemViewHolder) holder).btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context, PlayContestActivity.class);
                    String id = items.get(position).getObjectId();
                    intent1.putExtra("contest_id",id);
                    context.startActivity(intent1);
                }
            });
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                        String contestIDsent = items.get(position).getContest_id();
                        Intent intent1 = new Intent(context, ContestINGActivity.class);
                        intent1.putExtra("contestIdSent",contestIDsent);
                        context.startActivity(intent1);
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
        TextView join_text_conStatus,join_text_topics,join_text_posi,join_text_nega,join_text_conTime;
        Button btn_choose,btn_play;
        public ItemViewHolder(View itemView) {
            super(itemView);
            join_text_conStatus = (TextView) itemView.findViewById(R.id.join_text_conStatus);
            join_text_conTime = (TextView) itemView.findViewById(R.id.join_text_conTime);
            join_text_topics = (TextView) itemView.findViewById(R.id.join_text_topics);
            join_text_posi = (TextView) itemView.findViewById(R.id.join_text_posi);
            join_text_nega = (TextView) itemView.findViewById(R.id.join_text_nega);
            btn_choose = (Button) itemView.findViewById(R.id.btn_choose);
            btn_play = (Button) itemView.findViewById(R.id.btn_play);
        }
    }

    public class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() == 0 ? 0 : items.size() + 1;
    }
}
