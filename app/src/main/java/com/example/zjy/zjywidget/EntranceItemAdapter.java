package com.example.zjy.zjywidget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 74215 on 2019/2/12.
 */

public class EntranceItemAdapter extends RecyclerView.Adapter<EntranceItemAdapter.EntranceViewHolder>{

    private Context mContext;
    private List<ViewItemBean> mViewItemList;

    public EntranceItemAdapter(Context mContext, List<ViewItemBean> mViewItemList) {
        this.mContext = mContext;
        this.mViewItemList = mViewItemList;
    }

    @Override
    public void onBindViewHolder(EntranceViewHolder holder, int position) {
        String name = mViewItemList.get(position).getName();
        String describe = mViewItemList.get(position).getDescirbe();
        final Class testClass = mViewItemList.get(position).getTestClass();
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, testClass);
                mContext.startActivity(intent);
            }
        });
        holder.mNameTv.setText(TextUtils.isEmpty(name) ? "" : name);
        holder.mDescribeTv.setText(TextUtils.isEmpty(describe) ? "" : describe);
    }

    @Override
    public EntranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_entrance_item, null);
        return new EntranceViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mViewItemList == null ? 0 : mViewItemList.size();
    }



    class EntranceViewHolder extends RecyclerView.ViewHolder{

        private CardView mRootView;
        private TextView mNameTv;
        private TextView mDescribeTv;

        public EntranceViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.item_root_view);
            mNameTv = itemView.findViewById(R.id.view_name_tv);
            mDescribeTv = itemView.findViewById(R.id.view_describe_tv);
        }
    }
}
