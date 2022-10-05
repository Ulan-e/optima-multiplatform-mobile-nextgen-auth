package kz.optimabank.optima24.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Timer;
import java.util.TimerTask;

import kg.optima.mobile.R;
import kz.optimabank.optima24.model.interfaces.KeepAliveSession;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.KeepAliveSessionImpl;
import kz.optimabank.optima24.utility.Constants;

public class SessionDialogActivity extends OptimaActivity implements KeepAliveSessionImpl.Callback {
    public int seconds = 30;
    private Timer timer = new Timer();
    KeepAliveSession keepAliveSession = new KeepAliveSessionImpl();
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    boolean isNeedToFinishSession, screenIsLocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!GeneralManager.isEnteredToSession) {
            finish();
        }

        // в старых версиях
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Log.d("jsonKeepAliveResponse", " onCreate");
        setContentView(R.layout.login_activity);
        keepAliveSession.registerCallBack(this);
        builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.session_dialog, null);
        final TextView tvTimer = (TextView) v.findViewById(R.id.tvTimer);
        final TextView tvForKGSLocal = (TextView) v.findViewById(R.id.tvForKGSLocal);
        final TextView tvContinue = (TextView) v.findViewById(R.id.tvContinue);
        final TextView tvEnd = (TextView) v.findViewById(R.id.tvEnd);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getString(Constants.APP_LANGUAGE, "ky").equals("ky-KG")){
            tvForKGSLocal.setVisibility(View.VISIBLE);
        }

        alertDialog = builder.setCancelable(false).setView(v).create();
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepAliveSession.keepAliveRequest(SessionDialogActivity.this, true);
                alertDialog.dismiss();
            }
        });
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNeedToFinishSession = true;
                cancelDialogAndTimer();
                finish();
            }
        });
        this.setFinishOnTouchOutside(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTimer.setText(String.valueOf(seconds) + " ");
                        Log.d("timer"," Runnable = " + seconds);
                        if (seconds <= 0) {
                            isNeedToFinishSession = true;
                            cancelDialogAndTimer();
                            if(screenIsLocked)
                                finishSession();
                            else
                                finish();
                        }
                        seconds--;
                    }
                });
            }

        }, 0, 1000);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(seconds <= 1 ){
            cancelDialogAndTimer();
            isNeedToFinishSession = true;
            finish();
        }else
            alertDialog.show();
        Log.d("SessionDialogActivity"," onResume - seconds" + seconds);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("SessionDialogActivity","onStop isNeedToFinishSession" + isNeedToFinishSession);
        screenIsLocked = true;
        if(isNeedToFinishSession){
            finishSession();
            cancelDialogAndTimer();
        }
    }

    @Override
    public void jsonKeepAliveResponse(int statusCode, String errorMessage) {
        cancelDialogAndTimer();
        if(statusCode != 0)
            isNeedToFinishSession = true;
        finish();
    }

    private void cancelDialogAndTimer() {
        if(timer != null)
            timer.cancel();
        if(alertDialog.isShowing())
            alertDialog.cancel();
    }

    private void finishSession() {
//        Intent intent = new Intent(this, UnauthorizedTabActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        cancelDialogAndTimer();
        finishSession();
        isNeedToFinishSession = true;
        Log.d("SessionDialogActivity"," onUserLeaveHint");
    }
}