package com.ghteam.eventgo.ui.dialog.selectcategories;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.databinding.LayoutCategoryItemBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nikit on 23.11.2017.
 */

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoryViewHolder> {

    private List<Category> mItems;
    private OnClickListener mOnClickListener;
    private boolean isEnabledToSelectItems;
    private OnSelectItemListener mSelectItemListener;

    private HashSet<Integer> mSelectedItems;
    public static final String TAG = CategoriesRecyclerAdapter.class.getSimpleName();

    public CategoriesRecyclerAdapter() {
        mItems = new ArrayList<>();
        mSelectedItems = new HashSet<>();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_category_item, parent, false);

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private LayoutCategoryItemBinding itemBinding;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);

        }

        public void bindView(final int pos) {

            itemBinding.setVariable(BR.categories, mItems.get(pos));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isEnabledToSelectItems && mSelectItemListener != null) {

                        if (mSelectedItems.contains(pos)) {
                            removeItemFromSelected(pos);
                            mSelectItemListener.onUnselected(mItems.get(pos));

                        } else {
                            addItemToSelected(pos);
                            mSelectItemListener.onSelected(mItems.get(pos));
                        }
                        Log.d(TAG, "onClick: " + mSelectedItems.toString());
                    }

                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(mItems.get(pos));
                    }
                }
            });

            if (mSelectedItems.contains(pos)) {
                itemView.setBackgroundColor(0x22000000);
            } else {
                itemView.setBackgroundColor(0x222);
            }

        }
    }


    public void addItemToSelected(Category category) {
        int pos = mItems.indexOf(category);
        if (pos >= 0) {
            addItemToSelected(pos);
        }
    }

    public void addItemToSelected(int pos) {
        mSelectedItems.add(pos);
        notifyItemChanged(pos);
    }

    public void removeItemFromSelected(int pos) {
        mSelectedItems.remove(pos);
        notifyItemChanged(pos);
    }

    public void removeItemFromSelected(Category category) {
        int pos = mItems.indexOf(category);
        if (pos >= 0) {
            removeItemFromSelected(pos);
        }
    }


    public void removeAllFromSelected() {
        mSelectedItems.clear();
    }

    public void setItems(List<Category> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
            mSelectedItems.clear();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setSelectItemListener(OnSelectItemListener selectItemListener) {
        this.mSelectItemListener = selectItemListener;
    }

    public interface OnSelectItemListener {
        void onSelected(Category category);

        void onUnselected(Category category);
    }

    public interface OnClickListener {
        void onClick(Category category);
    }

    public boolean isEnabledToSelectItems() {
        return isEnabledToSelectItems;
    }

    public void setEnabledToSelectItems(boolean enabledToSelectItems) {
        isEnabledToSelectItems = enabledToSelectItems;
    }
}
