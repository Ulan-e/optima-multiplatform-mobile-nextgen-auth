package kz.optimabank.optima24.fragment.registration;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.interfaces.RegistrationClient;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.RegistrationClientImpl;
import kz.optimabank.optima24.secondary_registration.StepChangeListener;
import kz.optimabank.optima24.secondary_registration.ui.OldRegistrationActivity;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class SendSMSFragment extends ATFFragment implements RegistrationClientImpl.FinishRegCallback, RegistrationClientImpl.RequestSmsAgainCallback {
    private static final String TAG = SendSMSFragment.class.getSimpleName();

    private static final int SECOND = 1000;
    private static final int MINUTE = 1000 * 60;
    private static final int HOUR_IN_SECONDS = 60 * 60;
    private static final int MINUTE_IN_SECONDS = 60;

    private static final String TIME_FORMAT = "%d:%02d";

    @BindView(R.id.tv_countdown) TextView tvCountdown;
    @BindView(R.id.et_sms) EditText etSms;
    @BindView(R.id.btn_send_again) Button btnSendAgain;
    @BindView(R.id.tv_expired_error) TextView tvExpiredError;

    private StepChangeListener mStepChangeListener;
    private CountDownTimer timerForSms;
    private CountDownTimer timerForUnlockButton;
    private RegistrationClient mRegistrationClient;

    private boolean countdownFinished;

    private PowerManager.WakeLock wakeLock;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepChangeListener = (StepChangeListener) context;
        mRegistrationClient = new RegistrationClientImpl();
        mRegistrationClient.setFinishRegCallback(this);
        mRegistrationClient.setRequestSmsAgainCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_sms, container, false);
        ButterKnife.bind(this, view);
        startSmsCountdown();
        etSms.post(new Runnable() {
            @Override
            public void run() {
                etSms.requestFocus();
                InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(etSms, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        etSms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    if (countdownFinished) {
                        onError(getString(R.string.you_time_expired));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("phoneNumber", ((OldRegistrationActivity) parentActivity).phoneNumber);
                            jsonObject.put("idn", ((OldRegistrationActivity) parentActivity).idn);
                            jsonObject.put("Code", s.toString());
                        } catch (JSONException e) {
                            Log.e(TAG, "Error when put fields to json", e);
                        }
                        if (((OldRegistrationActivity) parentActivity).isClientOfBank) {
                            mRegistrationClient.finishRegForClientBank(parentActivity, jsonObject);
                        } else {
                            mRegistrationClient.finishRegForNoClientBank(parentActivity, jsonObject);
                        }
                    }
                }
            }
        });

        btnSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegistrationClient.requestSmsAgain(parentActivity, ((OldRegistrationActivity) parentActivity).phoneNumber);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        if (timerForUnlockButton != null) {
            timerForUnlockButton.cancel();
        }
        try {
            timerForSms.cancel();
            wakeLock.release();
        }catch (Exception ignored){
        }
        GeneralManager.getInstance().setErrorMessageForReg(tvExpiredError.getText().toString());
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStepChangeListener = null;
    }

    private void startSmsCountdown() {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) parentActivity.getSystemService(Context.POWER_SERVICE);
            if (pm != null)
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wake");
        }
        wakeLock.acquire(3 * MINUTE);
        btnSendAgain.setEnabled(false);
        if (timerForSms != null) {
            timerForSms.cancel();
        }
        timerForSms = new CountDownTimer(3 * MINUTE, SECOND - 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (countdownFinished) {
                    countdownFinished = false;
                }
                String formattedTime = String.format(Locale.getDefault(), TIME_FORMAT, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Log.i(TAG, "formattedTime = " + formattedTime);
                tvCountdown.setText(Html.fromHtml(getString(R.string.code_expire, formattedTime)));
            }

            @Override
            public void onFinish() {
                countdownFinished = true;
                Log.i(TAG, "onFinish");
            }
        };
        timerForSms.start();
    }

    private void startUnlockButtonCountdown(int seconds) {
        btnSendAgain.setEnabled(false);
        if (timerForUnlockButton != null) {
            timerForUnlockButton.cancel();
        }
        wakeLock.acquire(seconds * SECOND);
        timerForUnlockButton = new CountDownTimer(seconds * SECOND, SECOND - 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                GeneralManager.getInstance().setMillisecondsForUnlockButton(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                btnSendAgain.setEnabled(true);
            }
        };
        timerForUnlockButton.start();
    }

    private void incorrectCodeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder
                .setMessage(getString(R.string.incorrect_code_operation))
                .setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void finishRegResponse(int statusCode, String errorMessage, BaseRegistrationResponse responseBody) {
        if (statusCode == 200) {
            switch (responseBody.code) {
                case SUCCESS:
                    mStepChangeListener.onStepChange();
                    break;
                case INCORRECT_PHONE:
                    onError(responseBody.message);
                    break;
                case INCORRECT_SMS:
                    incorrectCodeAlert();
                    if (responseBody.data != 0) {
                        startUnlockButtonCountdown(responseBody.data);
                    }
                    switch (responseBody.data) {
                        case MINUTE_IN_SECONDS / 2:
                            tvExpiredError.setText(getString(R.string.sms_input_info_30_secs));
                            break;
                        case MINUTE_IN_SECONDS:
                            tvExpiredError.setText(getString(R.string.sms_input_info_60_secs));
                            break;
                        case 5 * MINUTE_IN_SECONDS:
                            tvExpiredError.setText(getString(R.string.sms_input_info_5_minutes));
                            break;
                        case HOUR_IN_SECONDS:
                            tvExpiredError.setText(getString(R.string.sms_input_info_one_hour));
                            break;
                        case 5 * HOUR_IN_SECONDS:
                            tvExpiredError.setText(getString(R.string.sms_input_info_five_hours));
                            break;
                    }
                    break;
                default:
                    onError(responseBody.message);
                    break;
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void requestSmsAgainResponse(int statusCode, String errorMessage, BaseRegistrationResponse responseBody) {
        if (statusCode == 200) {
            switch (responseBody.code) {
                case SUCCESS:
                    startSmsCountdown();
                    tvExpiredError.setText("");
                    break;
                case DOESNT_EXPIRED_SMS:
                    onError(responseBody.message);
                    break;
                default:
                    onError(responseBody.message);
                    break;
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }
}
