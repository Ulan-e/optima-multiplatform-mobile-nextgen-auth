package kz.optimabank.optima24.notifications.ui.notificationsList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotificationsListViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public NotificationsListViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NotificationsListViewModel.class) {
            return (T) new NotificationsListViewModel(application);
        }
        return null;
    }
}