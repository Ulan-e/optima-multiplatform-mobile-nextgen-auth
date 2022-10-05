package kz.optimabank.optima24.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;

import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.utility.LocaleUtils;

public class OptimaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (!BuildConfig.DEBUG) {
            disableScreens();
        }
        super.onCreate(savedInstanceState);
    }

    private void disableScreens() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleUtils.updateResources(newBase, LocaleUtils.getLanguage(newBase));
        super.attachBaseContext(newBase);
    }

    public void replaceFragment(ATFFragment fragment, int containerId, boolean backStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerId, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (backStack) {
            ft.addToBackStack(fragment.getClass().getSimpleName());
        }
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
