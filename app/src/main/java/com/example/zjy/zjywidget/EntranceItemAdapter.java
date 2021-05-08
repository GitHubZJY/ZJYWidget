package com.example.zjy.zjywidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zjy.zjywidget.utils.GlideCircleBorderTransform;

import java.util.List;

/**
 * Created by Yang on 2019/2/12.
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
        String describe = mViewItemList.get(position).getDescribe();
        int previewGifId = mViewItemList.get(position).getPreviewGif();
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
        RequestOptions options = RequestOptions.bitmapTransform(new GlideCircleBorderTransform(6, Color.LTGRAY));
        Glide.with(mContext).asGif().load(previewGifId).apply(options).into(holder.mPreviewIv);
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



    static class EntranceViewHolder extends RecyclerView.ViewHolder{

        private CardView mRootView;
        private TextView mNameTv;
        private TextView mDescribeTv;
        private ImageView mPreviewIv;

        public EntranceViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.item_root_view);
            mNameTv = itemView.findViewById(R.id.view_name_tv);
            mDescribeTv = itemView.findViewById(R.id.view_describe_tv);
            mPreviewIv = itemView.findViewById(R.id.preview_iv);
        }
    }
}
