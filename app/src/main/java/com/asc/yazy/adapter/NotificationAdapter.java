package com.asc.yazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelNotification;
import com.asc.yazy.databinding.NotificationCellBinding;
import com.asc.yazy.interfaces.OnNotificationListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<ModelNotification> list;
    OnNotificationListener listener;

    public NotificationAdapter(Context context, List<ModelNotification> list, OnNotificationListener listener) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_cell, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull NotificationAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NotificationAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private NotificationCellBinding binding;

        ViewHolder(NotificationCellBinding binding) {
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

        void setViewModel(ModelNotification notification) {
            if (binding != null) {
                binding.setNotificationData(notification);
            
                binding.layoutBackGround.setOnClickListener(v -> {
                    if (notification.getRead().equals("0")) {
                        binding.layoutBackGround.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                    listener.OnNotificationClicked(notification);
                });

                if (notification.getRead().equals("1"))
                    binding.layoutBackGround.setBackgroundColor(context.getResources().getColor(R.color.white));
                else
                    binding.layoutBackGround.setBackgroundColor(context.getResources().getColor(R.color.orange_notification));
            }
        }
    }
}