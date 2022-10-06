package kz.optimabank.optima24.notifications.service;

import static kz.optimabank.optima24.utility.Constants.IS_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_CHANNEL_ID;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_ID;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_SEND_TIME;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_TEXT;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_TITLE;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;
import static kz.optimabank.optima24.utility.DateConverterUtils.stringDayMonthFromDate;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kg.optima.mobile.R;
import kg.optima.mobile.android.ui.SingleActivity;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.notifications.data.remote.NotificationsRemoteRepository;
import kz.optimabank.optima24.notifications.data.remote.NotificationsRemoteRepositoryImpl;
import kz.optimabank.optima24.notifications.models.DeliveredNotificationResponse;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.NotificationIdBody;
import kz.optimabank.optima24.notifications.ui.NotificationsActivity;
import retrofit2.Response;

public class Optima24MessagingService extends FirebaseMessagingService {

    private static final String DATA_ID = "id";
    private static final String DATA_TITLE = "title";
    private static final String DATA_BODY = "body";

    private NotificationsRemoteRepository remoteRepository;
    /*private UserPreferences userPreferences;
    private BiometricPreferences biometricPreferences;
    private SessionPreferences sessionPreferences;*/

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        remoteRepository = new NotificationsRemoteRepositoryImpl(this);

        initPreferences();

        Notification notification = getPayloadData(remoteMessage);

        saveNotificationInPreferences(notification);
        prepareNotification(notification);
        makeDeliveredNotification(notification.getId());
    }

    private void initPreferences() {
        //TODO Optima24MessagingService
       /* userPreferences = new UserPreferencesImpl(this);
        biometricPreferences = new BiometricPreferencesImpl(this);
        sessionPreferences = new SessionPreferencesImpl(this);*/
    }

    // получаем айди из ключа значение который отправляет сервис пушей
    private Notification getPayloadData(RemoteMessage remoteMessage) {
        Map<String, String> payloadData = remoteMessage.getData();
        String notificationId = payloadData.get(DATA_ID);
        String notificationTitle = payloadData.get(DATA_TITLE);
        String notificationBody = payloadData.get(DATA_BODY);
        return createNotificationDto(remoteMessage, notificationId, notificationTitle, notificationBody);
    }

    // конвертируем полученное сообщение из Firebase в объект Notification
    private Notification createNotificationDto(RemoteMessage message, String notificationId, String title, String text) {
        Notification notification = new Notification();
        if (notificationId != null) notification.setId(notificationId);
        notification.setTitle(title);
        notification.setText(text);
        notification.setSentDate(stringDayMonthFromDate(message.getSentTime()));
        return notification;
    }

    // охранение данных об уведомлении в преференсах
    private void saveNotificationInPreferences(Notification notification) {
        SharedPreferences.Editor editor = getPreferences(this).edit();
        editor.putString(NOTIFICATION_DATA_ID, notification.getId());
        editor.putString(NOTIFICATION_DATA_TEXT, notification.getText());
        editor.putString(NOTIFICATION_DATA_TITLE, notification.getTitle());
        editor.putString(NOTIFICATION_DATA_SEND_TIME, notification.getSentDate());
        editor.apply();
    }

    // делаем уведомление полученным
    private void makeDeliveredNotification(String notificationId) {
        NotificationIdBody requestBody = getRequestBody(notificationId);
        remoteRepository.requestNotificationDelivered(requestBody)
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(new SingleObserver<Response<DeliveredNotificationResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(PUSH_TAG, "subscription disposed ");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<DeliveredNotificationResponse> response) {
                        Log.d(PUSH_TAG + "delivered", "notification success " + response.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(PUSH_TAG + "delivered", "notification error " + e.getLocalizedMessage());
                    }
                });
    }

    // подготовка пуша и открытие activity
    private void prepareNotification(Notification notification) {
        PendingIntent pendingIntent = createNotificationPendingIntent(notification);

        // создаем уведомление в зависимости от версиии Android
        createNotification(
                notification.getTitle(),
                notification.getText(),
                pendingIntent
        );
    }

    // создаем intent для сообщения
    private Intent createNotificationIntent(Notification notification) {
       // SessionPreferences sessionPreferences = new SessionPreferencesImpl(this);
        Intent intent;

        if (true) {
            if (GeneralManager.getInstance().getSessionId() == null) {
                intent = new Intent(this, SingleActivity.class);
            } else {
                intent = new Intent(this, NotificationsActivity.class);
            }
        } else {
            intent = new Intent(this, SingleActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(IS_NOTIFICATION, true);
        intent.putExtra(NOTIFICATION_ARG_ID, notification.getId());
        return intent;
    }

    // создаем pendingIntent для сообщения
    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent createNotificationPendingIntent(Notification notification) {
        Intent intent = createNotificationIntent(notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getActivity(
                    getApplicationContext(),
                    new Random().nextInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            return PendingIntent.getActivity(
                    getApplicationContext(),
                    new Random().nextInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        }
    }

    private void createNotification(String title, String body, PendingIntent pendingIntent) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_optima_small_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification.build());
    }

    private NotificationIdBody getRequestBody(String notificationId) {
        NotificationIdBody body = new NotificationIdBody();
        body.setNotificationId(notificationId);
        return body;
    }
}