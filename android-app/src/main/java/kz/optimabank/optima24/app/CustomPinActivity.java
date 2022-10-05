//package kz.optimabank.optima24.app;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import kg.optima.mobile.R;
//import kz.optimabank.optima24.utility.LocaleUtils;
//import rps.pincode.managers.AppLock;
//import rps.pincode.managers.AppLockActivity;
//
//
///**
// * Created by Timur on 29.09.2016.
// */
//public class CustomPinActivity extends AppLockActivity {
//    SharedPreferences sPref;
//    SharedPreferences.Editor editor;
//    final String CODE = "code";
//    TextView tvCancel;
//    final String SAVED_PASSWORD = "saved_password";
//    final String CANCEL = "cancel";
//    final String ATTEMPTS = "attempts";
//    int i = 2;
//    private boolean isContactless;
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        newBase = LocaleUtils.updateResources(newBase, LocaleUtils.getLanguage(newBase));
//        super.attachBaseContext(newBase);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sPref = this.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
//
//        if (!sPref.getBoolean(CODE, false)) {
//            mLockManager.getAppLock().setPasscode(null);
//        }
//        editor = sPref.edit();
//        editor.putBoolean(ATTEMPTS, false);
//        editor.apply();
//
//        mForgotTextView = (TextView) this.findViewById(R.id.pin_code_forgot_textview);
//        tvCancel = (TextView) findViewById(R.id.pin_code_button_10);
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isContactless) {
//                    mLockManager.getAppLock().setPasscode(null);
//                    editor.remove(SAVED_PASSWORD);
//                    editor.putBoolean(CANCEL, true);
//                    editor.apply();
//                }
//                finish();
//            }
//        });
//        tvCancel.setText(R.string.dialog_cancel);
//        isContactless = getIntent().getBooleanExtra("is_contactless", false);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        sPref = this.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
//        editor = sPref.edit();
//        editor.putBoolean(CODE, mLockManager.getAppLock().isPasscodeSet());
//        editor.apply();
//    }
//
//    @Override
//    public void showForgotDialog() {
//    }
//
//    @Override
//    public void onPinFailure(int attempts) {
//        if (mLockManager.getAppLock().isPasscodeSet()) {
//            mForgotTextView.setVisibility(View.VISIBLE);
//            mForgotTextView.setText(getResources().getString(R.string.attempt_atf) + " " + i--);
//        } else {
//            mForgotTextView.setVisibility(View.GONE);
//        }
//
//        if (attempts == 3) {
//            sPref = this.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
//            editor = sPref.edit();
//            mLockManager.getAppLock().setPasscode(null);
//            editor.remove(SAVED_PASSWORD);
//            editor.putBoolean(CANCEL, true);
//            editor.putBoolean(ATTEMPTS, true);
//            editor.apply();
//            finish();
//        }
//    }
//
//    @Override
//    public void onPinSuccess(int attempts) {
//        finish();
//        sPref = this.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
//        editor = sPref.edit();
//        editor.putBoolean(CANCEL, false);
//        editor.apply();
//    }
//
//    @Override
//    public int getPinLength() {
//        return 4;
//    }
//
//    @Override
//    public void setPinCode(String pinCode) {
//        super.setPinCode(pinCode);
//    }
//
//
////    @Override
////    public String getForgotText() {
////        return getString(R.string.entry_using_login_code);
////    }
//
//    @Override
//    protected void onPinCodeInputed() {
////        sPref = this.getSharedPreferences("my_pref",Context.MODE_PRIVATE);
////        if(!sPref.getBoolean(CODE,false)){
////            mType = AppLock.ENABLE_PINLOCK;
////        }
//        switch (mType) {
//            case AppLock.DISABLE_PINLOCK:
//                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
//                    setResult(RESULT_OK);
//                    mLockManager.getAppLock().setPasscode(null);
//                    onPinCodeSuccess();
//                    finish();
//                } else {
//                    onPinCodeError();
//                }
//                break;
//            case AppLock.ENABLE_PINLOCK:
//                mOldPinCode = mPinCode;
//                setPinCode("");
//                mType = AppLock.CONFIRM_PIN;
//                setStepText();
//                break;
//            case AppLock.CONFIRM_PIN:
//                if (mPinCode.equals(mOldPinCode)) {
//                    setResult(RESULT_OK);
//                    mLockManager.getAppLock().setPasscode(mPinCode);
//                    onPinCodeSuccess();
//                    finish();
//                } else {
//                    mOldPinCode = "";
//                    setPinCode("");
//                    mType = AppLock.ENABLE_PINLOCK;
//                    setStepText();
//                    onPinCodeError();
//                }
//                break;
//            case AppLock.CHANGE_PIN:
//                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
//                    mType = AppLock.ENABLE_PINLOCK;
//                    setStepText();
//                    setPinCode("");
//                    onPinCodeSuccess();
//                } else {
//                    onPinCodeError();
//                }
//                break;
//            //����
//            case AppLock.UNLOCK_PIN:
//                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
//                    setResult(RESULT_OK);
//                    onPinCodeSuccess();
//                    finish();
//                } else {
//                    onPinCodeError();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void setStepText() {
//        mStepTextView.setText(getStepText(mType));
//    }
//
//    @Override
//    public String getStepText(int reason) {
//        String msg = null;
//        switch (reason) {
//            case AppLock.ENABLE_PINLOCK:
//                msg = getString(R.string.pin_code_step_unlock);
//                break;
//            case AppLock.UNLOCK_PIN:
//                msg = getString(R.string.pin_code_step_unlock);
//                break;
//            case AppLock.CONFIRM_PIN:
//                msg = getString(R.string.pin_code_step_enable_confirm);
//                break;
//        }
//        return msg;
//    }
//}
//
