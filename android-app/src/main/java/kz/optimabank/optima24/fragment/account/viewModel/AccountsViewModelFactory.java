package kz.optimabank.optima24.fragment.account.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AccountsViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public AccountsViewModelFactory(Application application) {
        super();
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AccountsListViewModel.class) {
            return (T) new AccountsListViewModel(application);
        }
        return null;
    }
}