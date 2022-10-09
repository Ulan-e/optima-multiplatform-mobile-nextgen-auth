package kz.optimabank.optima24.notifications.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.notifications.ui.notificationDetails.NotificationDetailsFragment;
import kz.optimabank.optima24.notifications.ui.notificationsList.NotificationListFragment;

import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;

public class NotificationsActivity extends OptimaActivity {

    private static final String IS_FROM_SINGLE_NOTIFICATION = "isFromSingleNotification";
    private static final String NOTIFICATION_ID = "notificationId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        openScreen(savedInstanceState);
    }

    // если находим айди уведомления, то показываем экран детали уведомления если нет список уведомлений
    private void openScreen(Bundle savedInstanceState) {
        String notificationId = getNotificationId();
        if (notificationId != null) {
            openNotificationsList(savedInstanceState, true, notificationId);
            Log.d(PUSH_TAG, "NotificationsActivity received notificationId " + notificationId);
        } else {
            openNotificationsList(savedInstanceState, false, notificationId);
        }
    }

    // получаем отправленное айди уведомления
    private String getNotificationId() {
        Intent intent = getIntent();
        return intent.getStringExtra(NOTIFICATION_ARG_ID);
    }

    // экран список уведомлений
    private void openNotificationsList(Bundle savedInstanceState, boolean isFromNotification, String notification) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, newInstance(isFromNotification, notification), null)
                    .commit();
        }
    }

    public NotificationListFragment newInstance(boolean isFromNotification, String notification) {
        NotificationListFragment fragment = new NotificationListFragment();
        fragment.setArguments(getBundle(isFromNotification, notification));
        return fragment;
    }

    private Bundle getBundle(boolean isFromNotification, String notification){
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FROM_SINGLE_NOTIFICATION, isFromNotification);
        bundle.putString(NOTIFICATION_ID, notification);
        return bundle;
    }
}