package kz.optimabank.optima24.app;

import static kz.optimabank.optima24.utility.Constants.APP_VERSION;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import kg.optima.mobile.R;
import kz.optimabank.optima24.room_db.AppDatabase;
import kz.optimabank.optima24.utility.Constants;

public class OptimaBank extends Application implements LifecycleObserver {
    private static OptimaBank instance;
    private boolean isBackground;
    private boolean urgentMessageState;
    public static String NFC_TAG = "NFC_TAG";
    private int updateStatusCount = 0;
    private boolean isPayWithPin = false;
    public AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        // Realm.init(this);
        //для сохранении последнего статуса закрыт или открыт.
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        // для поддержки обратной совместимости векторной графики
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //registerActivityLifecycleCallbacks(new AppLifeCycle());

        // для перехвата ошибок в RxJava, когда реактивные процессы не закончены
        RxJavaPlugins.setErrorHandler(error -> Log.e(PUSH_TAG, "error in rxJava " + error.getLocalizedMessage()));

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        appDatabase = AppDatabase.getInstance(this);

//        if(BuildConfig.DEBUG){
//            Timber.plant(new Timber.DebugTree());
//        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("appIsRunning", false).apply();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("appIsRunning", true).apply();
    }

    public OptimaBank() {
        OptimaBank.instance = this;
    }

    public static OptimaBank getContext() {
        return instance;
    }

    public static String getDeviceId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String device_id;
        device_id = preferences.getString(Constants.APP_DEVICE_ID, null);
        if (TextUtils.isEmpty(device_id)) {
            char[] chars = "qwertyuiopasdfghjklzxcvbnm0123456789".toCharArray();
            Random rnd = new Random();
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                id.append(chars[rnd.nextInt(chars.length)]);
            }
            Log.d("TAG", "random = " + id.toString());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.APP_DEVICE_ID, id.toString());
            editor.apply();
            device_id = id.toString();
        }

//        String device_id;
//        device_id = preferences.getString(Constants.APP_DEVICE_ID, null);
//        if (TextUtils.isEmpty(device_id)) {
//            if (ContextCompat.checkSelfPermission(getContext(),
//                    Manifest.permission.READ_PHONE_STATE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                device_id = ((TelephonyManager) instance.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
//            } else {
//                device_id = String.valueOf(System.currentTimeMillis());
//            }
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(Constants.APP_DEVICE_ID, device_id);
//            editor.apply();
//        }
        return device_id;
    }

    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String deviceModel;
        if (model.startsWith(manufacturer)) {
            deviceModel = capitalize(model);
        } else {
            deviceModel = capitalize(manufacturer) + " " + model;
        }
        return deviceModel;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getOSVersion() {
        return "Android/" + android.os.Build.VERSION.RELEASE;
    }


    public static OptimaBank getInstance() {
        if (instance == null) {
            instance = new OptimaBank();
        }
        return instance;
    }

    public boolean isGPSEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return !(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public static int pxInDp(int px) {
        return (int) (instance.getResources().getDisplayMetrics().density * px + 0.5f);
    }

    public boolean isBackground() {
        return this.isBackground;
    }

    public void setIsBackground(boolean bool) {
        this.isBackground = bool;
    }

    public void toastBackgroundInfo() {
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (OptimaBank.getContext().isBackground()) {
                    Toast.makeText(getContext(), getString(R.string.background_mode_atf24), Toast.LENGTH_LONG).show();
                }
            }
        }, 4000);*/

    }

    public String getLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString(Constants.APP_LANGUAGE, "ru-RU");
    }

    public Map<String, String> getOpenSessionHeader(String sessionId) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept-Language", getLanguage());
        header.put("User-Agent", String.format("%s (%s; %s/%s)", APP_VERSION, getOSVersion(), getDeviceModel(), getDeviceId()));
        if (sessionId != null) {
            header.put("XX-TB-AuthToken", sessionId);
        }
        Log.d("TAG", "header = " + header);
        return header;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openConfirmCredentials() {
        KeyguardManager mKeyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (mKeyguardManager != null) {
            Intent intent = mKeyguardManager.createConfirmDeviceCredentialIntent(
                    getApplicationContext().getResources().getString(R.string.unlock_device),
                    getApplicationContext().getResources().getString(R.string.unlock_phone_for_payment));
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //need to add device verification http://www.androhub.com/android-confirm-credential-security-lock/
    }

    public boolean getUrgentMessageState() {
        return urgentMessageState;
    }

    public void changeUrgentMessageState(boolean enableUrgentMessage) {
        urgentMessageState = enableUrgentMessage;
    }
}