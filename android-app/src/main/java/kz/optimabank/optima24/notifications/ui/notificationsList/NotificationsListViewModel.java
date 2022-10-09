package kz.optimabank.optima24.notifications.ui.notificationsList;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kz.optimabank.optima24.notifications.data.local.NotificationsLocalRepository;
import kz.optimabank.optima24.notifications.data.local.NotificationsLocalRepositoryImpl;
import kz.optimabank.optima24.notifications.data.remote.NotificationsRemoteRepository;
import kz.optimabank.optima24.notifications.data.remote.NotificationsRemoteRepositoryImpl;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.items.BaseNotificationItem;
import kz.optimabank.optima24.notifications.utils.NotificationListUtils;

import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;

public class NotificationsListViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> loadingState;

    private CompositeDisposable compositeDisposable;
    private NotificationsRemoteRepository remoteRepository;
    private NotificationsLocalRepository localRepository;

    private MutableLiveData<List<BaseNotificationItem>> allNotificationsItem;
    private MutableLiveData<Integer> unreadNotificationsCount;

    public NotificationsListViewModel(@NonNull Application application) {
        super(application);
        init(application);
    }

    private void init(Application application) {
        compositeDisposable = new CompositeDisposable();
        remoteRepository = new NotificationsRemoteRepositoryImpl(application);
        localRepository = new NotificationsLocalRepositoryImpl(application);
        unreadNotificationsCount = new MutableLiveData<>();
        loadingState = new MutableLiveData<>();
    }

    public LiveData<List<BaseNotificationItem>> getAllNotifications(String bankId) {
        allNotificationsItem = new MutableLiveData<>();
        fetchAllNotifications(bankId);
        return allNotificationsItem;
    }

    // добавляем не прочитанные сообщения в базу данных
    public Single<List<Long>> putRemoteNotification(String bankId) {
        return remoteRepository.getUnreadNotifications(bankId)
                .flatMap(items -> localRepository.putNotifications(items, bankId))
                .subscribeOn(Schedulers.io());
    }

    // сливаем увдеомления из сервера с уведомлениями из базы данных
    private void fetchAllNotifications(String bankId) {
        loadingState.setValue(true);
        compositeDisposable.add(putRemoteNotification(bankId)
                .flatMap(insertedItems -> localRepository.getNotifications(bankId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Notification>>() {
                    @Override
                    public void onSuccess(@NonNull List<Notification> notifications) {
                        if(notifications != null) {
                            NotificationListUtils.sortByDateDesc(notifications);
                            List<BaseNotificationItem> items = NotificationListUtils.splitByDate(notifications);
                            loadingState.setValue(false);
                            allNotificationsItem.postValue(items);
                            Log.d(PUSH_TAG, "all notifications size " + notifications.size());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingState.setValue(false);
                        Log.e(PUSH_TAG, "fetchAllNotifications error " + e.getLocalizedMessage());
                    }
                }));
    }

    public LiveData<Integer> getUnreadNotificationsCount(String clientId) {
        unreadNotificationsCount = new MutableLiveData<>();
        requestUnreadNotificationsCount(clientId);
        return unreadNotificationsCount;
    }

    // сливаем увдеомления из сервера с уведомлениями из базы данных
    private void requestUnreadNotificationsCount(String bankId) {
        compositeDisposable.add(remoteRepository.getUnreadNotifications(bankId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Notification>>() {
                    @Override
                    public void onSuccess(@NonNull List<Notification> notifications) {
                        if (notifications != null) {
                            int count = notifications.size();
                            Log.d(PUSH_TAG, "requestUnreadNotifications count  " + notifications.size());
                            unreadNotificationsCount.setValue(count);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(PUSH_TAG, "requestUnreadNotifications count error " + e.getLocalizedMessage());
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}