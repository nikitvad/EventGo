package com.ghteam.eventgo.ui.activity.eventdetails;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ghteam.eventgo.R;

import java.util.List;
import com.ghteam.eventgo.BR;

/**
 * Created by nikit on 12.12.2017.
 */

public class ImagePreviewsRecyclerAdapter extends RecyclerView.Adapter<ImagePreviewsRecyclerAdapter.ImagePreviewViewHolder> {

    private List<String> mImages;

    private int mSelectedImagePosition = 0;

    private OnItemClickListener mItemClickListener;

    public ImagePreviewsRecyclerAdapter(List<String> images) {
        mImages = images;
    }

    @Override
    public ImagePreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_image_preview_item, parent, false);

        return new ImagePreviewViewHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(ImagePreviewViewHolder holder, int position) {

        holder.getDataBinding().setVariable(BR.imageUrl, mImages.get(position));
        holder.getDataBinding().setVariable(BR.isSelected, position==mSelectedImagePosition);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public void selectItem(int pos) {
        if (pos < mImages.size()) {
            mSelectedImagePosition = pos;
            notifyDataSetChanged();
        }
    }

    class ImagePreviewViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding dataBinding;

        public ImagePreviewViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getDataBinding() {
            if (mItemClickListener != null) {
                dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(getAdapterPosition());
                    }
                });
            }
            return dataBinding;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}
