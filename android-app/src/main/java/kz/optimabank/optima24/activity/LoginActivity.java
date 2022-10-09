package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.utility.Constants.IS_CONTACTLESS;
import static kz.optimabank.optima24.utility.Constants.IS_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.authorization.ContactlessLoginFragment;
import kz.optimabank.optima24.fragment.authorization.MLoginFragment;

/**
 * Created by Timur on 12.01.2017.
 */

public class LoginActivity extends OptimaActivity {

    private static final String TAG = "LoginActivity";

    private boolean isContactless;
    private boolean isNotification;
    private String notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        getIntentData();
        putDataToBundle();
    }

    // получаем данные отправленный из активити
    private void getIntentData() {
        Intent intent = getIntent();
        isNotification = intent.getBooleanExtra(IS_NOTIFICATION, false);
        isContactless = intent.getBooleanExtra(IS_CONTACTLESS, false);
        notificationId = intent.getStringExtra(NOTIFICATION_ARG_ID);
    }

    // кладем данные которые открывается вместе с фрагментом
    private void putDataToBundle() {
        Bundle bundle = new Bundle();
        if (!isContactless) {
            bundle.putBoolean(IS_CONTACTLESS, true);
            bundle.putBoolean(IS_NOTIFICATION, isNotification);
            if (notificationId != null) bundle.putString(NOTIFICATION_ARG_ID, notificationId);
            Log.d(TAG, "not contact less login, is notification " + isNotification);
            navigateToPage(new MLoginFragment(), bundle, false);
        } else {
            bundle.putBoolean(IS_NOTIFICATION, isNotification);
            Log.d(TAG, "contact login, is notification " + isNotification);
            navigateToPage(new ContactlessLoginFragment(), bundle, false);
        }
    }

    public void navigateToPage(ATFFragment fragment, Bundle bundle, boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        ft.replace(R.id.fragment_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "BACK");
        if (isNotification) {
            super.onBackPressed();
//            Intent intent = new Intent(this, UnauthorizedTabActivity.class);
//            startActivity(intent);
            Log.i(TAG, "startActivity");
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
            Log.i(TAG, "popBack");
        } else {
            Log.i(TAG, "super");
            super.onBackPressed();
        }
    }
}