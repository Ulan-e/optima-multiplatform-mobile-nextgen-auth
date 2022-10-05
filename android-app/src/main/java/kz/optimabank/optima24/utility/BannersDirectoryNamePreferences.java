package kz.optimabank.optima24.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class BannersDirectoryNamePreferences {

    public static volatile BannersDirectoryNamePreferences instance;
    private SharedPreferences preferences;

    public BannersDirectoryNamePreferences(Context context) {
        instance = this;
        preferences = context.getSharedPreferences("bannerDirectory", Context.MODE_PRIVATE);
    }

    public static BannersDirectoryNamePreferences getInstance(Context context) {
        if (instance == null) {
            instance = new BannersDirectoryNamePreferences(context);
        }
        return instance;
    }

    public String returnDirectory() {
        return preferences.getString("dir", "");
    }


    public void saveDirectory(String directory) {
        preferences.edit().putString("dir", directory).apply();
    }

    public void clearDirectory() {
        preferences.edit().clear().apply();
    }
}