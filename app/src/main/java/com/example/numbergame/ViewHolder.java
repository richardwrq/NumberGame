package com.example.numbergame;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mTv;
    public ViewHolder(View itemView) {
        super(itemView);
        mTv = itemView.findViewById(R.id.tv);
    }
}
