package kz.optimabank.optima24.app;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;


import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.db.controllers.DigitizedCardController;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.app.OptimaBank.NFC_TAG;


public class UnlockScreenActivity extends OptimaActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);

        try {
            SharedPreferences mSharedPreferences = getApplicationContext().getSharedPreferences("my_pref", Context.MODE_PRIVATE);
            DigitizedCard digitizedCard;
            DigitizedCardController digitizedCardController = DigitizedCardController.getController();
            Log.i("NFC_TAG", "digitizedCardController = " + digitizedCardController);
            Log.i("NFC_TAG", "mSharedPreferences.getString(Constants.userPhone,\"\") = " + mSharedPreferences.getString(Constants.userPhone, ""));
            digitizedCard = digitizedCardController.getDefaultDigitizedCard(mSharedPreferences.getString(Constants.userPhone, ""));
            Log.i("NFC_TAG", "digitizedCard = " + digitizedCard);

        } catch (Exception e) {
            Log.i("NFC_TAG", "selectCard error = " + e);
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(NFC_TAG,"Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ==== TRUE");

            try{
                Log.i(NFC_TAG,"try");
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }catch (Exception e){
                Log.i(NFC_TAG,"catch error = "+e);
                KeyguardManager mKeyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                if (mKeyguardManager != null) {
                    mKeyguardManager.requestDismissKeyguard(this, new KeyguardManager.KeyguardDismissCallback() {
                        @Override
                        public void onDismissError() {
                            Log.i(NFC_TAG,"onDismissError");
                            super.onDismissError();
                            Log.i(NFC_TAG,"onDismissError");
                        }
                    });
                }
            }


        } else {
            Log.i(NFC_TAG,"Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ==== FALSE");
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
        setContentView(R.layout.unlock_screen);

        Log.d(NFC_TAG,"onCreate UnlockScreenActivity");
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                try {
                    finish();
//                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(r, 1000);*/
    }

    @Override
    public void onAttachedToWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(NFC_TAG,"Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ==== TRUE");

                KeyguardManager mKeyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                if (mKeyguardManager != null) {
                    mKeyguardManager.requestDismissKeyguard(this, new KeyguardManager.KeyguardDismissCallback() {
                        @Override
                        public void onDismissError() {
                            Log.i(NFC_TAG,"onDismissError");
                            super.onDismissError();
                            Log.i(NFC_TAG,"onDismissError");
                        }



                        @Override
                        public void onDismissSucceeded() {
                            Log.i(NFC_TAG,"onDismissSucceeded");
                            super.onDismissSucceeded();
                            Log.i(NFC_TAG,"onDismissSucceeded");

                        }


                    });
                }


        } else {
            Log.i(NFC_TAG,"Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ==== FALSE");
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
        setContentView(R.layout.unlock_screen);

        Log.d(NFC_TAG,"onCreate UnlockScreenActivity");
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                try {
                    finish();
//                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(r, 1000);
    }
}
