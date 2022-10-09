package kz.optimabank.optima24.secondary_registration.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AgreementViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public AgreementViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AgreementViewModel.class) {
            return (T) new AgreementViewModel(application);
        }
        return null;
    }
}