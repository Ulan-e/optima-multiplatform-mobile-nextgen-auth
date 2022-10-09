package kz.optimabank.optima24.secondary_registration.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import kg.optima.mobile.android.ui.SingleActivity;
import kg.optima.mobile.databinding.FragmentSuccessRegistrationBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.authorization.MLoginFragment;

import static kz.optimabank.optima24.utility.Constants.REGISTRATION_LOGIN;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

public class SuccessRegistrationFragment extends ATFFragment {

    private static final long ANIMATION_DURATION = 900;

    private FragmentSuccessRegistrationBinding binding;
    private String login;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuccessRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArgs();

        binding.btnAuthorize.setOnClickListener(buttonView -> doOnSuccessRegistration());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((OldRegistrationActivity) requireActivity()).setToolbarVisibility(false);
        setDoneImageAnimation();
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) login = args.getString(REGISTRATION_LOGIN);
    }

    // вставим анимацию на ImageView после успешной регистрации
    private void setDoneImageAnimation() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(ANIMATION_DURATION);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        binding.imageViewSuccess.startAnimation(animation);
    }

    private void doOnSuccessRegistration() {
        saveUserData();
        openLoginScreen();
    }

    // переходим на экран логина
    private void openLoginScreen() {
        Intent intent = new Intent(parentActivity, SingleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // сохраняем пользовательские данные пользователя
    private void saveUserData() {
        SharedPreferences.Editor editor = getPreferences(parentActivity).edit();
        editor.putString(MLoginFragment.SAVED_LOGIN, login);
        editor.putBoolean(MLoginFragment.LOGIN, true);
        editor.remove(MLoginFragment.SAVED_PASSWORD);
        editor.apply();
    }

    public static SuccessRegistrationFragment newInstance(String login) {
        SuccessRegistrationFragment fragment = new SuccessRegistrationFragment();
        Bundle args = new Bundle();
        args.putString(REGISTRATION_LOGIN, login);
        fragment.setArguments(args);
        return fragment;
    }
}
