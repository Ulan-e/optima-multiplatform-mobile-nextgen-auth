package kz.optimabank.optima24.activity;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;
import static kz.optimabank.optima24.utility.Constants.BACKGROUND_NOTIFICATION_ID;
import static kz.optimabank.optima24.utility.Constants.IS_BACKGROUND_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_CHANNEL_ID;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

import kg.optima.mobile.BuildConfig;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.service.CheckVersionImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Root;
import kz.optimabank.optima24.utility.Utilities;
import timber.log.Timber;

public class SplashActivity extends OptimaActivity implements CheckVersionImpl.Callback {

    private static final int RECOMMEND_UPDATE = 1;
    private static final int FORCE_UPDATE = 2;
    private static final long SPLASH_DELAY = 400;
    private static final long MAX_ATTEMPTS_PIN_CODE = 3;

    private Handler navigateHandler;
//    private UserPreferences userPreferences;
//    private BiometricPreferences biometricPreferences;
//    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initPreferences();

        getBundles();
        createNotificationChannel();
        navigateToMainScreen();

        Root root = new Root();
        if (root.isDeviceRooted() && !BuildConfig.DEBUG) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.your_device_is_rooted))
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_dialog_info)
                    .setPositiveButton(android.R.string.ok,
                            (dialog, whichButton) -> {
                                finish();
                                System.exit(0);
                            }).create().show();
        }

        CheckVersionImpl checkVersion = new CheckVersionImpl(this);
        checkVersion.registerCallBack(this);
        checkVersion.checkVersion();


        SharedPreferences prefs = getSharedPreferences("isAppFirstStart", MODE_PRIVATE);
        String local = Locale.getDefault().toString();
        if (prefs.getBoolean("isAppFirstStart", true)) {
            SharedPreferences.Editor editorIsFirst = prefs.edit();
            editorIsFirst.putBoolean("isAppFirstStart", false);
            editorIsFirst.apply();
            switch (local.toLowerCase()) {
                case "ky":
                    local = "ky-KG";
                    break;
                case "ru":
                    local = "ru-RU";
                    break;
                case "en":
                    local = "en-US";
                    break;
            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.APP_LANGUAGE, local);
            editor.apply();
        }
    }

    private void initPreferences() {
//        userPreferences = new UserPreferencesImpl(this);
//        biometricPreferences = new BiometricPreferencesImpl(this);
//        sessionPreferences = new SessionPreferencesImpl(this);
    }

    private void navigateToMainScreen() {
        navigateHandler = new Handler(Looper.getMainLooper());
        navigateHandler.postDelayed(
                () -> {
//                    if (userPreferences.isPinSet()
//                            && sessionPreferences.getAttemptsPinCode() == MAX_ATTEMPTS_PIN_CODE) {
//                        navigateToPinEnterActivity();
//                    } else {
//                        if (userPreferences.getUserLogin() != null && userPreferences.getUserPassword() != null) {
//                            navigateToPinEnterActivity();
//                        } else {
//                            navigateToEnterActivity();
//                        }
//                    }
                    finish();
                },
                SPLASH_DELAY
        );
    }

    // создание канала уведомления для android api >= 26
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void jsonCheckVersionResponse(int resultCode, String message, boolean isSuccess) {
        switch (resultCode) {
            case RECOMMEND_UPDATE:
            case -10013:
                showUpdateDialog(false, message);
                break;
            case FORCE_UPDATE:
            case -10014:
                showUpdateDialog(true, message);
                break;
            default:
                // navigateToMainScreen();
                break;
        }
    }

    private void showUpdateDialog(boolean forceUpdate, String message) {
        String positiveButtonName = forceUpdate ? getString(R.string.update) : getString(R.string.yes);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(R.string.attention);
        builder.setCancelable(false);
        if (!forceUpdate) {
            builder.setNegativeButton(getString(R.string._no), (dialog, which) -> {
                dialog.cancel();
//                startActivity(new Intent(this, UnauthorizedTabActivity.class));
//                finish();
            });
        }
        builder.setPositiveButton(positiveButtonName, (dialog, which) -> {
            try {
                if (!forceUpdate) {
//                    startActivity(new Intent(this, UnauthorizedTabActivity.class));
                }
                finish();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=kz.optimabank.optima24")));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=kz.optimabank.optima24")));
            }
        });
        builder.create().show();
    }

    // получаем айди уведомления когда приложение на заднем фоне
    private void getBundles() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String notificationId = extras.getString(NOTIFICATION_ID);
            if (notificationId != null) {
                saveNotificationsData(notificationId);
            }
            Timber.d("get notificationId from background %s", notificationId);
        }
    }

    // сохраняем айди уведомленияв преференсах
    private void saveNotificationsData(String notificationId) {
        SharedPreferences preferences = Utilities.getPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BACKGROUND_NOTIFICATION_ID, notificationId);
        editor.putBoolean(IS_BACKGROUND_NOTIFICATION, true);
        editor.apply();
    }

    private void navigateToPinEnterActivity() {
        //startActivity(new Intent(this, PinEnterActivity.class));
    }

    private void navigateToEnterActivity() {
        //startActivity(new Intent(this, EnterActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigateHandler != null) {
            navigateHandler.removeCallbacksAndMessages(null);
        }
    }
}