package kz.optimabank.optima24.notifications.ui.notificationDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotificationsDetailsViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public NotificationsDetailsViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NotificationsDetailsViewModel.class) {
            return (T) new NotificationsDetailsViewModel(application);
        }
        return null;
    }
}