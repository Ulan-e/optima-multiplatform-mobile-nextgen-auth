package kz.optimabank.optima24.fragment.registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.interfaces.AuthorizationUser;
import kz.optimabank.optima24.model.interfaces.RegistrationClient;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AuthorizationUserImpl;
import kz.optimabank.optima24.model.service.RegistrationClientImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class ChangePasswordForRegFragment extends ATFFragment implements RegistrationClientImpl.ChangeTempPasswordCallback, TextWatcher, AuthorizationUserImpl.Callback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.scroll_view) View passwordView;
    @BindView(R.id.et_temp_password) EditText etTempPassword;
    @BindView(R.id.et_new_password) EditText etNewPassword;
    @BindView(R.id.et_re_new_password) EditText etReNewPassword;
    @BindView(R.id.success_change_password_layout) View successLayout;

    private RegistrationClient mRegistrationClient;
    private AuthorizationUser authorizationUser;
    private String phoneNumber;

    public static ChangePasswordForRegFragment newInstance(String phoneNumber) {
        ChangePasswordForRegFragment fragment = new ChangePasswordForRegFragment();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationClient = new RegistrationClientImpl();
        mRegistrationClient.setChangeTempPasswordCallback(this);
        authorizationUser = new AuthorizationUserImpl();
        authorizationUser.registerCallBack(this);
        phoneNumber = getArguments().getString("phone");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password_for_reg, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        etTempPassword.addTextChangedListener(this);
        etNewPassword.addTextChangedListener(this);
        etReNewPassword.addTextChangedListener(this);

        return view;
    }

    private void initToolbar() {
        ((OptimaActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((OptimaActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @OnClick(R.id.btn_further)
    public void onFurtherClick() {
        if (validate()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("OldPassword", getSHA256(etTempPassword.getText().toString().toUpperCase()));
                jsonObject.put("Password", getSHA256(etNewPassword.getText().toString()));
                jsonObject.put("PhoneNumber", convertToHex(phoneNumber));
                mRegistrationClient.changeTempPassword(parentActivity, jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "Error when put fields to json", e);
            }
        }
    }

    @Override
    public void changeTempPasswordCallback(int statusCode, String errorMessage, BaseRegistrationResponse responseBody) {
        if (statusCode == 200) {
            switch (responseBody.code) {
                case SUCCESS:
                    parentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    passwordView.setVisibility(View.GONE);
                    successLayout.setVisibility(View.VISIBLE);
                    break;
                case NO_CLIENT_WITH_HASH:
                    onError(getString(R.string.client_not_found));
                    break;
                case INCORRECT_OLD_PASSWORD:
                    etTempPassword.setError(getString(R.string.incorrect_temp_password));
                    break;
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @OnClick(R.id.btn_authorize)
    public void onAuthorizeClick() {
        authorizationUser.loginRequest(parentActivity, phoneNumber, getSHA256(etNewPassword.getText().toString()), null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            if (s == etTempPassword.getText()) {
                etTempPassword.setError(null);
            } else if (s == etNewPassword.getText()) {
                etNewPassword.setError(null);
            } else if (s == etReNewPassword.getText()) {
                etReNewPassword.setError(null);
            }
        }
    }

    private boolean isOkPassword(String pass) {
        boolean isLetter = false;
        boolean isDigit = false;
        for (int i = 0; i < pass.length(); i++) {
            if (Character.isLetter(pass.charAt(i))) {
                isLetter = true;
            } else if (Character.isDigit(pass.charAt(i))) {
                isDigit = true;
            }
        }
        return isDigit && isLetter && pass.length() > 7 && pass.length() < 16;
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

    private boolean validate() {
        boolean isValid = true;
        if (TextUtils.isEmpty(etTempPassword.getText())) {
            isValid = false;
            etTempPassword.setError(getString(R.string.error_empty));
        }
        if (TextUtils.isEmpty(etNewPassword.getText())) {
            isValid = false;
            etNewPassword.setError(getString(R.string.error_empty));
        }
        if (TextUtils.isEmpty(etReNewPassword.getText())) {
            isValid = false;
            etReNewPassword.setError(getString(R.string.error_empty));
        }

        if (!etNewPassword.getText().toString().equals(etReNewPassword.getText().toString())) {
            isValid = false;
            etReNewPassword.setError(getString(R.string.not_equals_password));
        }

        if (!isOkPassword(etNewPassword.getText().toString())) {
            isValid = false;
            etNewPassword.setError(getString(R.string.error_wrong_format));
        }
        if (!isOkPassword(etReNewPassword.getText().toString())) {
            isValid = false;
            etReNewPassword.setError(getString(R.string.error_wrong_format));
        }

        return isValid;
    }

    @Override
    public void jsonAuthorizationResponse(int statusCode, String errorMessage, int errorCode) {
        if (statusCode == Constants.SUCCESS) {
            String profImage = GeneralManager.getInstance().getProfImage();
            if (profImage == null) {
                GeneralManager.setNeedUpdateProfImage(false);
            } else {
                if (Utilities.getPreferences(requireContext()).getString("profImage", "").equals(profImage)) {
                    GeneralManager.setNeedUpdateProfImage(false);
                } else {
                    SharedPreferences.Editor editorProf = Utilities.getPreferences(requireContext()).edit();
                    editorProf.putString("profImage", profImage);
                    editorProf.apply();
                    GeneralManager.setNeedUpdateProfImage(true);
                }
            }

           GeneralManager.getInstance().setAlarmManagerOn(getActivity());
            Intent intent = new Intent(parentActivity, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (errorCode == -10006) {
            smsEntry();
        } else {
            onError(errorMessage);
        }
    }

    private void smsEntry() {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.text_sms_code))
                .setView(input)
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_info)
                .setTitle(R.string.alert_info)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                authorizationUser.loginRequest(getActivity(), phoneNumber,
                                        getSHA256(etNewPassword.getText().toString()),
                                        input.getText().toString());
                            }
                        })
                .setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private String convertToHex(String login) {
        char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(login.getBytes("UTF-8"));

            char[] chars = new char[hash.length * 2];
            for (int i = 0; i < hash.length; i++) {
                chars[i * 2] = HEX_DIGITS[(hash[i] >> 4) & 0xf];
                chars[i * 2 + 1] = HEX_DIGITS[hash[i] & 0xf];
            }
            return new String(chars);
        } catch (Exception ex) {
            Log.e(TAG, "Error when convert login to hash string", ex);
        }
        return null;
    }
}
