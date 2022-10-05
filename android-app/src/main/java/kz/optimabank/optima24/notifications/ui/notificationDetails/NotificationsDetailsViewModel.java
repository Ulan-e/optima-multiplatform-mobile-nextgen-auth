package kz.optimabank.optima24.notifications.ui.notificationDetails;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kz.optimabank.optima24.notifications.data.local.NotificationsLocalRepository;
import kz.optimabank.optima24.notifications.data.local.NotificationsLocalRepositoryImpl;
import kz.optimabank.optima24.notifications.data.remote.NotificationsRemoteRepository;
import kz.optimabank.optima24.notifications.data.remote.NotificationsRemoteRepositoryImpl;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.NotificationIdBody;

import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_ID;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_SEND_TIME;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_TEXT;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_DATA_TITLE;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

public class NotificationsDetailsViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> loadingState;

    private CompositeDisposable compositeDisposable;
    private NotificationsRemoteRepository remoteRepository;
    private NotificationsLocalRepository localRepository;

    private MutableLiveData<Notification> notificationValue;

    public NotificationsDetailsViewModel(@NonNull Application application) {
        super(application);
        init(application);
    }

    private void init(Application application) {
        compositeDisposable = new CompositeDisposable();
        remoteRepository = new NotificationsRemoteRepositoryImpl(application);
        localRepository = new NotificationsLocalRepositoryImpl(application);
        loadingState = new MutableLiveData<>();
    }

    public LiveData<Notification> getReadNotification(String clientId, NotificationIdBody requestBody) {
        if (notificationValue == null) {
            notificationValue = new MutableLiveData<>();
            setReadNotificationById(clientId, requestBody);
        }
        return notificationValue;
    }

    // получаем уведомление по айди далее если успешно запрашиваем читание уведомления
    private void setReadNotificationById(String bankId, NotificationIdBody requestBody) {
        compositeDisposable.add(localRepository.findNotificationById(requestBody.getNotificationId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        notification -> {
                            if (!notification.isRead()) {
                                upsertLocalNotification(notification, bankId, requestBody);
                            } else {
                                notificationValue.setValue(notification);
                            }
                            Log.d(PUSH_TAG, "setReadNotificationById success " + notification);
                        },
                        error -> {
                            Notification notification = getNotificationFromPreferences();
                            upsertLocalNotification(notification, bankId, requestBody);
                            Log.w(PUSH_TAG, "setReadNotificationById error " + error.getLocalizedMessage());
                        }));
    }

    // записываем в базу уведомлени с пометкой что она прочитано
    private void upsertLocalNotification(Notification notification, String bankId, NotificationIdBody requestBody) {
        compositeDisposable.add(remoteRepository.setReadNotification(requestBody)
                .flatMap(insertedId -> localRepository.insert(Notification.makeRead(notification, bankId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            notificationValue.setValue(notification);
                            loadingState.setValue(false);
                            Log.d(PUSH_TAG, "insert notifications success ");
                        },
                        error -> {
                            notificationValue.setValue(notification);
                            loadingState.setValue(false);
                            Log.e(PUSH_TAG, "insert notifications error, but notification showed " + error);
                        }
                )
        );
    }

    // костыль, иногда не получаю объект пуша, так как сервер последний пуш поздно возвращает
    private Notification getNotificationFromPreferences() {
        SharedPreferences preferences = getPreferences(getApplication());
        String id = preferences.getString(NOTIFICATION_DATA_ID, "");
        String title = preferences.getString(NOTIFICATION_DATA_TITLE, "");
        String text = preferences.getString(NOTIFICATION_DATA_TEXT, "");
        String sentDate = preferences.getString(NOTIFICATION_DATA_SEND_TIME, "");

        Notification notification = new Notification();
        notification.setId(id);
        notification.setTitle(title);
        notification.setText(text);
        notification.setSentDate(sentDate);
        return notification;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}