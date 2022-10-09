package kz.optimabank.optima24.fragment.authorization.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RegistrationPushViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public RegistrationPushViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == RegistrationPushViewModel.class) {
            return (T) new RegistrationPushViewModel(application);
        }
        return null;
    }
}