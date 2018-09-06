package com.example.numbergame;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OtherAdapter extends RecyclerView.Adapter<ViewHolder> {

    // 展示数据
    private List<Integer> mData;
    // 事件回调监听
    private OnItemClickListener onItemClickListener;

    private RecyclerView mRecyclerView;
    private SparseBooleanArray mSparseArray = new SparseBooleanArray(10);

    public OtherAdapter(RecyclerView recyclerView, List<Integer> data) {
        this.mRecyclerView = recyclerView;
        this.mData = data;
    }
    public void updateData(List<Integer> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    // 删除Item
    public void deleteItem() {
        if(mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(0);
        notifyItemRemoved(0);
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v;
        if (mRecyclerView.getLayoutManager().getLayoutDirection() == LinearLayoutManager.HORIZONTAL) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hor_item_rv, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        }
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setSelected(mSparseArray.get(position));
        holder.mTv.setText("-" + mData.get(position));
        //③ 对RecyclerView的每一个itemView设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mSparseArray.put(holder.getAdapterPosition(), true);
                holder.itemView.setSelected(mSparseArray.get(holder.getAdapterPosition()));
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
        return mData == null ? 0 : mData.size();
    }

    public void clear() {
        mSparseArray.clear();
    }
}
