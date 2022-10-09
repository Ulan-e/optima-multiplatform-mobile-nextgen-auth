package kz.optimabank.optima24.secondary_registration.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import kz.optimabank.optima24.secondary_registration.ui.AgreementFragment;
import kz.optimabank.optima24.secondary_registration.ui.RegistrationFragment;
import kz.optimabank.optima24.secondary_registration.ui.SuccessRegistrationFragment;

public class RegistrationScreensPagerAdapter extends FragmentStateAdapter {

    private static final int SCREENS_COUNT = 3;
    private String login;
    private String registrationCode;

    public RegistrationScreensPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setData(String login, String registrationCode) {
        this.login = login;
        this.registrationCode = registrationCode;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AgreementFragment.newInstance(login, registrationCode);
            case 1:
                return RegistrationFragment.newInstance(login);
            case 2:
                return SuccessRegistrationFragment.newInstance(login);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return SCREENS_COUNT;
    }
}