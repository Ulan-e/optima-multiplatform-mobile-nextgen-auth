package kz.optimabank.optima24.notifications.ui.adapter;

import static kz.optimabank.optima24.notifications.models.items.BaseNotificationItem.HEADER_ITEM;
import static kz.optimabank.optima24.notifications.models.items.BaseNotificationItem.NOTIFICATION_ITEM;
import static kz.optimabank.optima24.utility.DateConverterUtils.stringFromDate;
import static kz.optimabank.optima24.utility.DateConverterUtils.stringHourMinuteFromDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import kg.optima.mobile.R;
import kg.optima.mobile.databinding.ItemNotificationBinding;
import kg.optima.mobile.databinding.ItemNotificationHeaderBinding;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.items.BaseNotificationItem;
import kz.optimabank.optima24.notifications.models.items.HeaderItem;
import kz.optimabank.optima24.notifications.models.items.NotificationItem;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private NotificationClickListener notificationClickListener;
    private Context context;
    private List<BaseNotificationItem> data = Collections.emptyList();

    public NotificationsAdapter(NotificationClickListener listener) {
        this.notificationClickListener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case HEADER_ITEM: {
                ItemNotificationHeaderBinding binding = ItemNotificationHeaderBinding.inflate(inflater, parent, false);
                return new HeaderViewHolder(binding);
            }
            case NOTIFICATION_ITEM: {
                ItemNotificationBinding binding = ItemNotificationBinding.inflate(inflater, parent, false);
                return new NotificationViewHolder(binding);
            }
            default:
                throw new IllegalStateException("unsupported view type");
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case HEADER_ITEM: {
                HeaderItem headerItem = (HeaderItem) data.get(position);
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.bind(headerItem);
                break;
            }
            case NOTIFICATION_ITEM: {
                NotificationItem notificationItem = (NotificationItem) data.get(position);
                NotificationViewHolder notificationHolder = (NotificationViewHolder) holder;
                notificationHolder.bind(notificationItem);
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    public void setData(List<BaseNotificationItem> data) {
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        private ItemNotificationBinding binding;

        public NotificationViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NotificationItem notificationItem) {
            Notification notification = notificationItem.getNotification();
            if (notification.isRead()) {
                binding.getRoot()
                        .setBackground(ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_rounded_shadowed_item_active));
            } else {
                binding.getRoot()
                        .setBackground(ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_rounded_shadowed_item_disactive));
            }
            binding.getRoot().setActivated(notification.isRead());
            binding.notificationTitle.setText(notification.getTitle());
            binding.notificationDescription.setText(notification.getText());
            String textDate = stringHourMinuteFromDate(notification.getSentDate());
            binding.notificationDate.setText(textDate);

            binding.getRoot().setOnClickListener(view -> {
                notificationClickListener.onItemClick(
                        binding.getRoot(),
                        notification.getId()
                );
            });
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ItemNotificationHeaderBinding binding;

        public HeaderViewHolder(ItemNotificationHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(HeaderItem item) {
            String textDate = stringFromDate(item.getDate());
            binding.headerTitle.setText(textDate);
        }
    }

    public interface NotificationClickListener {
        void onItemClick(View view, String notificationId);
    }
}