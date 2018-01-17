package com.ghteam.eventgo.ui.activity.userslist;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.databinding.LayoutUserListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 08.12.2017.
 */

public class PeopleRecyclerAdapter extends RecyclerView.Adapter<PeopleRecyclerAdapter.UserViewHolder> {

    private List<User> mItems;

    private int categoryIconSize;

    private ViewGroup.LayoutParams categoryIconLayoutParams;

    public PeopleRecyclerAdapter(Context context) {
        mItems = new ArrayList<>();
        Resources r = context.getResources();
        categoryIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
        categoryIconLayoutParams = new ViewGroup.LayoutParams(categoryIconSize, categoryIconSize);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user_list_item, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.bindView(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<User> newItems) {
        mItems.clear();
        mItems.addAll(newItems);
        notifyDataSetChanged();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        private LayoutUserListItemBinding viewBinding;

        public UserViewHolder(View itemView) {
            super(itemView);
            viewBinding = DataBindingUtil.bind(itemView);
        }

        public void bindView(User user) {
            viewBinding.setVariable(BR.user, user);

            if (user.getInterests() != null && user.getInterests().size() > 0) {
                viewBinding.llCategoriesContainer.removeAllViews();
                viewBinding.llCategoriesContainer.setVisibility(View.VISIBLE);
                for (int i = 0; i < 3 && i < user.getInterests().size(); i++) {
                    ImageView categoryIcon = new ImageView(viewBinding.llCategoriesContainer.getContext());

                    categoryIcon.setLayoutParams(categoryIconLayoutParams);

                    Picasso.with(categoryIcon.getContext())
                            .load(user.getInterests().get(i).getIcon()).into(categoryIcon);

                    viewBinding.llCategoriesContainer.addView(categoryIcon);
                }
            } else {
                viewBinding.llCategoriesContainer.setVisibility(View.GONE);
            }

        }
    }
}
