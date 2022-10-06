package kz.optimabank.optima24.app;

import static kz.optimabank.optima24.utility.Constants.APP_VERSION;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import kz.optimabank.optima24.utility.Constants;

public class HeaderHelper {

    public static String getLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.APP_LANGUAGE, "ru-RU");
    }

    public static Map<String, String> getOpenSessionHeader(Context context, String sessionId) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept-Language", getLanguage(context));
        header.put("User-Agent", String.format("%s (%s; %s/%s)", APP_VERSION,
                getOSVersion(),
                getDeviceModel(),
                getDeviceId(context)));
        if (sessionId != null) {
            header.put("XX-TB-AuthToken", sessionId);
        }
        Log.d("TAG", "header = " + header);
        return header;
    }

    public static String getOSVersion() {
        return "Android/" + android.os.Build.VERSION.RELEASE;
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

    public static String getDeviceId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        return device_id;
    }
}
