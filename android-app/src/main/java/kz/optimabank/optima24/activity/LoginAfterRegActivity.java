package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.utility.Constants.REG_EX_FOR_PHONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.db.controllers.DHKeyController;
import kz.optimabank.optima24.db.controllers.PushTokenKeyController;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.authorization.MLoginFragment;
import kz.optimabank.optima24.fragment.registration.ChangePasswordForRegFragment;
import kz.optimabank.optima24.model.interfaces.AuthorizationUser;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AuthorizationUserImpl;
import kz.optimabank.optima24.utility.Utilities;

public class LoginAfterRegActivity extends OptimaActivity implements TextWatcher, AuthorizationUserImpl.Callback {
    public static final String EXTRA_PHONE = "phone";

    private static final String TAG = LoginAfterRegActivity.class.getSimpleName();

    @BindView(R.id.edittext_phone) EditText etLogin;
    @BindView(R.id.edittext_password) EditText etPassword;
    @BindView(R.id.cbSaveLogin) CheckBox cbSaveLogin;
    @BindView(R.id.ALLcontentLOGIN) ScrollView ALLcontentLOGIN;
    @BindView(R.id.ATFlogo) RelativeLayout ATFlogo;
    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.clearPass) ImageView clearPass;
    @BindView(R.id.clearLogin) ImageView clearLogin;
    @BindView(R.id.view) View blockView;

    private AuthorizationUser authorizationUser;
    private DHKeyController dhKeyController = DHKeyController.getController();
    private PushTokenKeyController pushTokenKeyController = PushTokenKeyController.getController();

    private boolean isMaskFilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        authorizationUser = new AuthorizationUserImpl();
        authorizationUser.registerCallBack(this);

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "([000]) [000]-[00]-[00]",
                true,
                etLogin,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        isMaskFilled = maskFilled;
                        if (extractedValue.isEmpty()) {
                            clearLogin.setVisibility(View.GONE);
                        } else {
                            clearLogin.setVisibility(View.VISIBLE);
                        }
                        Log.d(TAG, extractedValue);
                        Log.d(TAG, String.valueOf(maskFilled));
                    }
                }
        );
        etLogin.addTextChangedListener(listener);
        etLogin.setText(getIntent().getStringExtra(EXTRA_PHONE));
        clearPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.getText().clear();
            }
        });
        clearLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLogin.getText().clear();
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginAfterRegActivity.this, UnauthorizedTabActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(LoginAfterRegActivity.this, UnauthorizedTabActivity.class));
//        finish();
    }

    @OnClick(R.id.button_login)
    public void onLoginClick() {
        if (validate()) {
            authorizationUser.loginRequest(this, etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""), getSHA256(etPassword.getText().toString().toUpperCase()), null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dhKeyController.close();
        pushTokenKeyController.close();
    }

    private boolean validate() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etLogin.getText())) {
            etLogin.setError(getString(R.string.error_empty));
            isValid = false;
        } else if (!isMaskFilled) {
            etLogin.setError(getString(R.string.error_wrong_format));
            isValid = false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError(getString(R.string.error_empty));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            clearPass.setVisibility(View.GONE);
        } else {
            clearPass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void jsonAuthorizationResponse(int statusCode, String errorMessage, int errorCode) {
        if (statusCode == 203 && errorCode == -10010) {
    //        GeneralManager.getInstance().setAlarmManager(this);                                   Заранее добавил строчку чтобы не забыть поставить таймер сессии когда добавится регистрация
            userAuthSuccess();
        } else {
            ATFFragment.ErrorDialogFragment fragment = ATFFragment.ErrorDialogFragment.newInstance(errorMessage);
            fragment.show(getSupportFragmentManager(), "error_dialog");
        }
    }

    private void userAuthSuccess() {
        if (cbSaveLogin.isChecked()) {
            SharedPreferences.Editor editor = Utilities.getPreferences(this).edit();
            editor.putString(MLoginFragment.SAVED_LOGIN, etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
            editor.putBoolean(MLoginFragment.LOGIN, true);
            editor.apply();
        }

        String token = Utilities.getPreferences(this).getString("pushTokenKey", null);
        int user = isOtherUser();
        byte[] key = dhKeyController.getKey();

        if(key==null || token==null || user==2) {
            Log.d(TAG,"deviceRegisterPush");
            pushTokenKeyController.setPushTokenKey(etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
        } else if(user==1) {
            Log.d(TAG,"deviceUnregisterPush");
            GeneralManager.getInstance().setIsInfomeReg(false);
        }

        ChangePasswordForRegFragment fragment = ChangePasswordForRegFragment.newInstance(etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
        replaceFragment(fragment, R.id.container, true);
    }

    private int isOtherUser() {
        String key = pushTokenKeyController.getPushTokenKey();
        Log.i("PTKC","getPushTokenKey isOtherUser = "+pushTokenKeyController.getPushTokenKey());

        Log.d("isOtherUser", "key = " + key);
        if(key==null){
            Log.d("isOtherUser", "2");
            return 2;
        }else if(!key.equals(etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""))) {
            Log.d("isOtherUser", "1");
            pushTokenKeyController.setPushTokenKey(etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
            Log.i("PTKC","edittextLogin.getText().toString().replaceAll(\"-\",\"\") isOtherUser = " + etLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
            return 1;
        } else {
            Log.d("isOtherUser", "0");
            return 0;
        }
    }

    private String getSHA256(String passw) {
        String def;
        String newPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(passw.getBytes("UTF-8"));
            def = Base64.encodeToString(hash, Base64.DEFAULT);
            passw = def.replaceAll("\n", "");
            newPassword = passw;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return newPassword;
    }
}
