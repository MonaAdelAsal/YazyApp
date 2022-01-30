package com.asc.yazy.api.pagination.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.api.model.ModelNotification;
import com.asc.yazy.databinding.RvNotificationBinding;
import com.asc.yazy.interfaces.OnNotificationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationPagedAdapter extends PagedListAdapter<ModelNotification, NotificationPagedAdapter.ItemViewHolder> {

    //private static final String TAG = "NotificationPagedAdapte";

    private static final DiffUtil.ItemCallback<ModelNotification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ModelNotification>() {
                @Override
                public boolean areItemsTheSame(ModelNotification oldItem, ModelNotification newItem) {
                    return oldItem.getId() == (newItem.getId());
                }


                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(ModelNotification oldItem, @NonNull ModelNotification newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private final Context context;
    private final OnNotificationListener listener;
    private List<ModelNotification> list;
    private List<ModelNotification> realTimeNotifications;


    public NotificationPagedAdapter(Context context, OnNotificationListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;

        this.list = new ArrayList<>();
        this.realTimeNotifications = new ArrayList<>();

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {

                list.clear();
                list.addAll(realTimeNotifications);
                list.addAll(Objects.requireNonNull(getCurrentList()));
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });



    }

    @Override
    public void submitList(PagedList<ModelNotification> pagedList) {
        this.list = new ArrayList<>();
        this.realTimeNotifications = new ArrayList<>();

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {

                list.clear();
                list.addAll(realTimeNotifications);
                list.addAll(Objects.requireNonNull(getCurrentList()));
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });


        super.submitList(pagedList);
    }

    @NonNull
    @Override
    public NotificationPagedAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvNotificationBinding itemView = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_notification, parent, false);
        itemView.setListener(listener);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationPagedAdapter.ItemViewHolder holder, int position) {


        if (position == RecyclerView.NO_POSITION)
            return;

        try {
            getItem(position);
        } catch (Exception ignored) {
        }

        ModelNotification item = getItem(position);


        if (item != null) {
            holder.setViewModel(item);
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull NotificationPagedAdapter.ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NotificationPagedAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void insetNotification(ModelNotification notification) {
        realTimeNotifications.add(notification);
        list.clear();
        list.addAll(realTimeNotifications);
        list.addAll(Objects.requireNonNull(getCurrentList()));
        ((MainActivity) context).runOnUiThread(this::notifyDataSetChanged);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private RvNotificationBinding binding;

        ItemViewHolder(RvNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind();
            }
        }

        void setViewModel(ModelNotification item) {
            if (binding != null) {
                binding.setNotificationData(item);
            }
        }
    }


}
