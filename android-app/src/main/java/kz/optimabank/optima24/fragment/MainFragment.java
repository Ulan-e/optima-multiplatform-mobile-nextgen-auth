package kz.optimabank.optima24.fragment;

import static android.app.Activity.RESULT_OK;
import static kz.optimabank.optima24.fragment.authorization.MLoginFragment.REQUEST_CODE_ENABLE;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;
import static kz.optimabank.optima24.utility.Utilities.hasInternetConnection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tmall.ultraviewpager.UltraViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.BuildConfig;
import kg.optima.mobile.R;
import kg.optima.mobile.android.ui.SingleActivity;
import kz.optimabank.optima24.activity.ChangeAppLangActivity;
import kz.optimabank.optima24.activity.Communications;
import kz.optimabank.optima24.activity.LoginActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.UltraPagerAdapter;
import kz.optimabank.optima24.db.controllers.BannerController;
import kz.optimabank.optima24.imprint.FingerprintAuthentication;
import kz.optimabank.optima24.imprint.FingerprintUiHelper;
import kz.optimabank.optima24.model.base.Banner;
import kz.optimabank.optima24.model.interfaces.BannerInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.BannerImpl;
import kz.optimabank.optima24.utility.Root;
import kz.optimabank.optima24.utility.Utilities;

public class MainFragment extends ATFFragment implements View.OnClickListener, FingerprintUiHelper.Callback,
        BannerImpl.Callback, BannerImpl.CheckServerAvailabilityCallback {
    @BindView(R.id.button_login)
    Button btnLogin;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;
    @BindView(R.id.relContact)
    RelativeLayout relContact;
    @BindView(R.id.viewPager)
    UltraViewPager viewPager;
    @BindView(R.id.relLang)
    RelativeLayout relLang;
    @BindView(R.id.content_button_login)
    ConstraintLayout contentButtonLogin;
    @BindView(R.id.btn_remote_registration)
    Button btn_registration;

    BannerInterface bannerInterface;

    private FingerprintUiHelper mFingerprintUiHelper;
    AlertDialog alert;
    private int secretInt = 0;
    final public static String SAVED_LOGIN = "saved_login", SAVED_PASSWORD = "saved_password", LOGIN = "login",
            CODE = "code", CANCEL = "cancel", ATTEMPTS = "attempts";
    boolean isPayPhone;
    private final int SYSTEM_EXIT_STATUS = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralManager.isEnteredToSession = false;
        checkIfDeviceRooted();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isAttached()) {
            BannerController.getController().setContext(requireActivity());
            bannerInterface = new BannerImpl();
            bannerInterface.registerCallBack(this);
            bannerInterface.registerCheckServerAvailabilityCallback(this);
            bannerInterface.getBanners(requireActivity());

            btnLogin.setOnClickListener(this);
            relContact.setOnClickListener(this);
            relLang.setOnClickListener(this);
        }

        btn_registration.setOnClickListener(buttonView -> {
            try {
                startActivity(new Intent(requireActivity(), Class.forName("kz.optimabank.optima24.feature.registration.confirmation.ConfirmationActivity")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        isPayPhone = false;
        try {
            if (mFingerprintUiHelper != null)
                mFingerprintUiHelper.stopListening();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                startActivity(new Intent(requireActivity(), SingleActivity.class));
//                openLoginScreen();
                break;
            case R.id.relContact:
                startActivity(new Intent(requireActivity(), Communications.class));
                break;
            case R.id.relLang:
                startActivity(new Intent(requireActivity(), ChangeAppLangActivity.class));
                break;
        }
    }

    // проверка на интернет, если нет интернета показываем диалог
    private void openLoginScreen() {
        if (hasInternetConnection(requireActivity())) {
            btnLogin.setVisibility(View.GONE);
            loadingProgress.setVisibility(View.VISIBLE);
            bannerInterface.checkServerAvailability(requireActivity());
        } else {
            showMessageDialog(getString(R.string.internet_switched_off));
        }
    }

    private void checkIfDeviceRooted() {
        Root root = new Root();
        if (root.isDeviceRooted() && !BuildConfig.DEBUG) {
            new AlertDialog.Builder(requireActivity())
                    .setMessage(requireActivity().getString(R.string.your_device_is_rooted))
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_dialog_info)
                    .setPositiveButton(android.R.string.ok,
                            (dialog, whichButton) -> {
                                requireActivity().finish();
                                System.exit(SYSTEM_EXIT_STATUS);
                            }).create().show();
        }
    }

    private void clearDBase() {
        SharedPreferences mSharedPreferences = Utilities.getPreferences(parentActivity);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(requireActivity().getString(R.string.login_key));
        editor.remove(requireActivity().getString(R.string.password_key));
        editor.remove(SAVED_LOGIN);
        editor.remove(SAVED_PASSWORD);
        editor.remove(LOGIN);
        editor.remove(CODE);
        editor.remove(CANCEL);
        editor.remove(ATTEMPTS);
        editor.putBoolean(requireActivity().getString(R.string.use_fingerprint_to_authenticate_key), false);
        editor.apply();
    }

    @Override
    public void jsonBannersResponse(final int statusCode, String errorMessage, final ArrayList<Banner> banners) {
        if (isAttached()) {
            if (statusCode == 0) {
                if (!banners.isEmpty()) {
                    BannerController.getController().validateBanners(banners);
                    viewPager.setAdapter(new UltraPagerAdapter(BannerController.getController().getAllBanners()));
                    viewPager.initIndicator();
                    viewPager.getIndicator()
                            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                            .setFocusColor(ContextCompat.getColor(requireActivity(), R.color.blue_atf))
                            .setNormalColor(ContextCompat.getColor(requireActivity(), R.color.gray_181_181_181))
                            .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
                    viewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    viewPager.getIndicator().setMargin(0, 0, 0, 20);
                    viewPager.getIndicator().setIndicatorPadding(Utilities.pxInDp(requireActivity(), 8));
                    viewPager.getIndicator().build();
                    viewPager.setInfiniteLoop(true);
                    viewPager.setAutoScroll(5000);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ENABLE) {
            isPayPhone = true;
            openDigitizedCardFragment();
        }
    }

    public boolean checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) requireActivity().getSystemService(Context.FINGERPRINT_SERVICE);
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                if (fingerprintManager.isHardwareDetected()) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingProgress.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
    }

    @SuppressLint("InflateParams")
    @TargetApi(Build.VERSION_CODES.M)
    private void imprintAuthentication(FingerprintAuthentication fingerprintAuthentication) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View imprint = inflater.inflate(R.layout.imprint, null);
        if (fingerprintAuthentication.imprintAuthentication(null)) {
            mFingerprintUiHelper = fingerprintAuthentication.getFingerprintUiHelperBuilder()
                    .build((ImageView) imprint.findViewById(R.id.fingerprint_icon),
                            (TextView) imprint.findViewById(R.id.fingerprint_status), this);

            if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
                return;
            }
            final AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
            TextView title = new TextView(requireActivity());
            title.setTextColor(Color.BLACK);
            title.setTextSize(19);
            title.setGravity(Gravity.CENTER);
            title.setPadding(0, 30, 0, 0);
            title.setText(getString(R.string.title_dialog));
            adb.setCustomTitle(title);
            adb.setCancelable(false);
            adb.setView(imprint);
            adb.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFingerprintUiHelper.stopListening();
                    dialog.dismiss();
                }
            });
            if (alert != null) {
                alert.dismiss();
            }
            alert = adb.create();
            alert.show();

            Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (b != null) {
                b.setTextColor(getResources().getColor(R.color.gray_atf_));
            }
            mFingerprintUiHelper.startListening(null);
        }
    }

    @Override
    public void onAuthenticated() {
        if (alert != null) {
            alert.dismiss();
        }
        mFingerprintUiHelper.stopListening();
        openDigitizedCardFragment();
    }

    @Override
    public void onError() {
        if (alert != null) {
            alert.dismiss();
        }
        mFingerprintUiHelper.stopListening();
        new AlertDialog.Builder(getActivity())
                .setMessage(requireActivity().getString(R.string.limit_touch_id))
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        }).create().show();
        mFingerprintUiHelper.stopListening();
        SharedPreferences.Editor editor = getPreferences(requireActivity()).edit();
        editor.remove(requireActivity().getString(R.string.login_key));
        editor.remove(requireActivity().getString(R.string.password_key));
        editor.putBoolean(requireActivity().getString(R.string.use_fingerprint_to_authenticate_key), false);
        editor.apply();
    }

    private void openDigitizedCardFragment() {
        Intent intent = new Intent(parentActivity, NavigationActivity.class);
        intent.putExtra("is_contactless_login", true);
        startActivity(intent);
    }

    @Override
    public void jsonCheckServerResponse(Boolean isSuccess) {
        if (isAttached()) {
            if (isSuccess) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                showMessageDialog(getActivity().getResources().getString(R.string.RESOURCE_NOT_FOUND));
            }
            loadingProgress.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    private void showMessageDialog(String message) {
        if (alert != null) alert.dismiss();
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok,
                        (dialog, whichButton) -> dialog.dismiss()).create()
                .show();
    }
}