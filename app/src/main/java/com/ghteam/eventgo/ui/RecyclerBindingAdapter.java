package com.ghteam.eventgo.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class RecyclerBindingAdapter<T>
        extends RecyclerView.Adapter<RecyclerBindingAdapter.BindingHolder> {

    private int holderLayout, variableId;

    private List<T> mItems = new ArrayList<>();

    private OnItemClickListener<T> onItemClickListener;

    private static final String TAG = RecyclerBindingAdapter.class.getSimpleName();

    public RecyclerBindingAdapter(int holderLayout, int variableId, List<T> items) {
        this.holderLayout = holderLayout;
        this.variableId = variableId;
        this.mItems = items;
    }

    @Override
    public RecyclerBindingAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(holderLayout, parent, false);
        return new BindingHolder(v);
    }

    public void setItems(List<T> items) {
        if (items != null) {
            this.mItems.clear();
            this.mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> items) {
        int notifyFrom = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(notifyFrom, items.size());
    }

    public void addItem(T item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeItem(T item) {
        mItems.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerBindingAdapter.BindingHolder holder, int position) {
        T item = mItems.get(position);
        holder.getBinding().setVariable(variableId, item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);

            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}