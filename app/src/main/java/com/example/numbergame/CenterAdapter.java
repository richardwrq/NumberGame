package com.example.numbergame;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class CenterAdapter extends RecyclerView.Adapter<ViewHolder> {
    // 展示数据
    private ArrayList<ArrayList<GroupNumber>> mData;
    // 事件回调监听
    private OnItemClickListener onItemClickListener;

    public CenterAdapter(ArrayList<ArrayList<GroupNumber>> data) {
        this.mData = data;
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        GroupNumber number = mData.get(position / MainActivity.getROW_COUNT()).get(position % MainActivity.getROW_COUNT());
        if (number != null) {
            holder.itemView.setBackgroundColor(Color.parseColor(number.getBackgroundColor()));
            if (!number.isValid()) {
                holder.mTv.setVisibility(View.GONE);
            } else {
                holder.mTv.setVisibility(View.VISIBLE);
                holder.mTv.setText(number.toString());
            }
        } else {
            holder.mTv.setVisibility(View.GONE);
        }
        //③ 对RecyclerView的每一个itemView设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        int total = 0;
        for (ArrayList<GroupNumber> mDatum : mData) {
            total += mDatum.size();
        }
        return total;
    }
}
