package com.ghteam.eventgo.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 06.12.2017.
 */

public class SelectedCategoriesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Category> mItems;

    private OnItemClickListener itemClickListener;
    private OnAddItemClickListener addItemClickListener;

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_ADD_ITEM = 1;

    public SelectedCategoriesRecyclerAdapter(List<Category> items) {
        this.mItems = items;
    }

    public SelectedCategoriesRecyclerAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            View v = inflater.inflate(R.layout.layout_selected_category_item, parent, false);
            return new CategoryViewHolder(v);
        } else {
            return new AddItemViewHolder(inflater.inflate(R.layout.layout_add_category_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            ((CategoryViewHolder) holder).getDataBinding()
                    .setVariable(BR.categories, mItems.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mItems.size()) {
            return VIEW_TYPE_ADD_ITEM;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size() + 1;
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setAddItemClickListener(OnAddItemClickListener addItemClickListener) {
        this.addItemClickListener = addItemClickListener;
    }

    public void setItems(List<Category> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(Category item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeItem(Category item) {
        mItems.remove(item);
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding dataBinding;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);

            if (itemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(mItems.get(getAdapterPosition()));
                    }
                });
            }
        }

        public ViewDataBinding getDataBinding() {
            return dataBinding;
        }
    }

    class AddItemViewHolder extends RecyclerView.ViewHolder {

        public AddItemViewHolder(View itemView) {
            super(itemView);
            if (addItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItemClickListener.onAddItemClick();
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }

    public interface OnAddItemClickListener {
        void onAddItemClick();
    }


}
