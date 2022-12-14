package kz.optimabank.optima24.secondary_registration.ui;

import static kz.optimabank.optima24.utility.Constants.REGISTRATION_CODE;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import kg.optima.mobile.android.ui.SingleActivity;
import kg.optima.mobile.databinding.ActivityOldRegistrationBinding;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.secondary_registration.adapter.RegistrationScreensPagerAdapter;

public class OldRegistrationActivity extends OptimaActivity {

    private final static long HANDLER_DELAY = 100;
    private final static int START_PAGE = -1;
    private final static int END_PAGE = 1;

    public String phoneNumber;
    public boolean isClientOfBank;
    public String idn;

    private ActivityOldRegistrationBinding binding;
    private int currentStep;
    private String login;
    private String registrationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getArgs();
        setToolbarNavClick();
        initViewPagerAdapter();
        setViewPagerPageChangeCallback();
    }

    @Override
    public void onBackPressed() {
        if (currentStep == END_PAGE) {
            binding.viewPager.setCurrentItem(--currentStep, true);
        } else {
            backToMainPage();
        }
    }

    private void setToolbarNavClick() {
        binding.toolbar.setNavigationOnClickListener(view ->
                setCurrentItem(--currentStep, true)
        );
    }

    public void setToolbarVisibility(boolean isVisible) {
        if (isVisible) binding.toolbar.setVisibility(View.VISIBLE);
        else binding.toolbar.setVisibility(View.GONE);
    }

    private void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            login = intent.getStringExtra(REGISTRATION_LOGIN);
            registrationCode = intent.getStringExtra(REGISTRATION_CODE);
        }
    }

    private void initViewPagerAdapter() {
        RegistrationScreensPagerAdapter pagerAdapter = new RegistrationScreensPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle()
        );
        pagerAdapter.setData(login, registrationCode);
        binding.viewPager.setAdapter(pagerAdapter);
    }

    // ?????????????? ?????????????????? ?????????????? ???? ViewPager'??
    private void setViewPagerPageChangeCallback() {
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentStep = position;
                binding.stepView.go(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    // ?????????????? ?????????? ???????????????? ???? ViewPager'??
    public void setCurrentItem(int item, boolean isBack) {
        if (isBack) {
            if (item == START_PAGE || item == END_PAGE) backToMainPage();
            else binding.viewPager.setCurrentItem(item, true);
        } else {
            binding.viewPager.setCurrentItem(item, true);
        }
    }

    // ???????????????????????? ?? ?????????????? ??????????
    private void backToMainPage() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, SingleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }, HANDLER_DELAY);
    }
}