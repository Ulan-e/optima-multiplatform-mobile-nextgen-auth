package kz.optimabank.optima24.secondary_registration.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RegistrationViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public RegistrationViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == RegistrationViewModel.class) {
            return (T) new RegistrationViewModel(application);
        }
        return null;
    }
}