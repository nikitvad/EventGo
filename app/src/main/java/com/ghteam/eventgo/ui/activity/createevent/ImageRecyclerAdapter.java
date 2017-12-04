package com.ghteam.eventgo.ui.activity.createevent;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ghteam.eventgo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 02.12.2017.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mImagesSources;

    private ArrayMap<String, Boolean> mLoadingItems;

    private static final String TAG = ImageRecyclerAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_IMAGE_ITEM = 1;
    private static final int VIEW_TYPE_ADD_NEW_ITEM = 2;

    private OnAddItemClickListener addItemClickListener;
    private OnItemClickListener mItemClickListener;

    public ImageRecyclerAdapter() {
        mImagesSources = new ArrayList<>();
        mLoadingItems = new ArrayMap<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        if (viewType == VIEW_TYPE_IMAGE_ITEM) {

            v = inflater.inflate(R.layout.layout_image_item, parent, false);
            return new ImageViewHolder(v);
        } else {
            v = inflater.inflate(R.layout.layout_add_list_item, parent, false);
            return new AddItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_IMAGE_ITEM) {
            ((ImageViewHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        //for one more view
        return mImagesSources.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mImagesSources.size()) {
            return VIEW_TYPE_IMAGE_ITEM;
        } else {
            return VIEW_TYPE_ADD_NEW_ITEM;
        }
    }

    public void setAddItemClickListener(OnAddItemClickListener addItemClickListener) {
        this.addItemClickListener = addItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void addItem(String imageSource) {
        mImagesSources.add(imageSource);
//        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }


    public void setIsLoadingForItem(boolean isLoading, String item) {

        mLoadingItems.put(item, isLoading);
        int pos = mImagesSources.indexOf(item);
        if (pos >= 0) {
            notifyItemChanged(pos);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ProgressBar progressBar;

        public ImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_image);
            progressBar = itemView.findViewById(R.id.progress_bar);

        }

        public void bindView(final int pos) {

            if (mLoadingItems.get(mImagesSources.get(pos)) != null
                    && mLoadingItems.get(mImagesSources.get(pos))) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            Picasso.with(itemView.getContext()).load(mImagesSources.get(pos))
                    .resize(300, 300).centerCrop().into(image);

            if (mItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onClick(mImagesSources.get(pos));
                    }
                });
            }
        }
    }

    class AddItemViewHolder extends RecyclerView.ViewHolder {

        public AddItemViewHolder(View itemView) {
            super(itemView);
            if (addItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItemClickListener.onClick();
                    }
                });
            }
        }
    }

    public interface OnAddItemClickListener {
        void onClick();
    }

    public interface OnItemClickListener {
        void onClick(String imageUri);
    }
}
