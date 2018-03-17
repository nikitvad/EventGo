package com.ghteam.eventgo.ui.activity.userslist;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.AppLocation;
import com.ghteam.eventgo.data.entity.UserLocationInfo;
import com.ghteam.eventgo.databinding.UsersListItemBinding;
import com.ghteam.eventgo.util.network.LocationUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nikit on 08.12.2017.
 */

public class InviteUsersRecyclerAdapter extends RecyclerView.Adapter<InviteUsersRecyclerAdapter.InviteUserViewHolder> {

    private AppLocation currentLocation;

    private OnItemClickListener onItemClickListener;
    private OnInviteClickListener onInviteClickListener;

    private List<UserLocationInfo> items;

    private Set<UserLocationInfo> listInvited;

    public InviteUsersRecyclerAdapter(List<UserLocationInfo> items) {
        listInvited = new HashSet<>();
        this.items = items;
    }

    @Override
    public InviteUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        return new InviteUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InviteUserViewHolder holder, int position) {
        holder.bindView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeAllItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public void setCurrentLocation(AppLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setOnInviteClickListener(OnInviteClickListener onInviteClickListener) {
        this.onInviteClickListener = onInviteClickListener;
    }

    public void addItem(UserLocationInfo item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addItems(List<UserLocationInfo> newItems) {
        int startPos = items.size();
        items.addAll(newItems);
        notifyItemRangeInserted(startPos, newItems.size());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class InviteUserViewHolder extends RecyclerView.ViewHolder {

        private UsersListItemBinding viewBinding;

        public InviteUserViewHolder(View itemView) {
            super(itemView);
            viewBinding = DataBindingUtil.bind(itemView);
        }

        public void bindView(UserLocationInfo userLocationInfo) {
//            UserLocationInfo userLocationInfo = items.get(getAdapterPosition());
            viewBinding.setVariable(BR.user, userLocationInfo);

            if (listInvited.contains(userLocationInfo)) {
                viewBinding.tvInvite.setBackgroundColor(itemView.getResources().getColor(R.color.orange));
            } else {
                viewBinding.tvInvite.setBackgroundColor(itemView.getResources().getColor(R.color.lightBlue));
            }

            if (currentLocation != null) {
                double distance = LocationUtil.calculateDistance(currentLocation.getLatitude(), currentLocation.getLongitude(),
                        userLocationInfo.getAppLocation().getLatitude(), userLocationInfo.getAppLocation().getLongitude());

                NumberFormat formatter = new DecimalFormat("#0.00");

                String strDistance = formatter.format(distance) + " km";

                viewBinding.setVariable(BR.distance, strDistance);
            }
            if (onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                });
            }

            if (onInviteClickListener != null) {
                viewBinding.tvInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (listInvited.contains(items.get(getAdapterPosition()))) {
                            removeFromInvited(getAdapterPosition());
                        } else {
                            addToInvited(getAdapterPosition());
                        }

                        onInviteClickListener.onInviteClickListener(v, items.get(getAdapterPosition()));
                    }
                });
            }
        }

    }

    private void addToInvited(int pos) {
        listInvited.add(items.get(pos));
        notifyItemChanged(pos);

    }

    private void removeFromInvited(int pos) {
        listInvited.remove(items.get(pos));
        notifyItemChanged(pos);
    }

    private interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public interface OnInviteClickListener {
        void onInviteClickListener(View v, UserLocationInfo userLocationInfo);
    }
}
