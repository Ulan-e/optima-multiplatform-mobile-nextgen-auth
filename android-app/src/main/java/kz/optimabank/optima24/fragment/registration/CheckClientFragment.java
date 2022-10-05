package kz.optimabank.optima24.fragment.registration;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.interfaces.RegistrationClient;
import kz.optimabank.optima24.model.service.RegistrationClientImpl;
import kz.optimabank.optima24.secondary_registration.StepChangeListener;
import kz.optimabank.optima24.secondary_registration.ui.OldRegistrationActivity;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class CheckClientFragment extends ATFFragment implements RegistrationClientImpl.CheckClientCallback {

    private static final String TAG = CheckClientFragment.class.getSimpleName();

    @BindView(R.id.et_phone_hint) TextInputLayout etPhoneHint;
    @BindView(R.id.et_idn_hint) TextInputLayout etIdnHint;
    @BindView(R.id.et_phone) EditText etPhoneNumber;
    @BindView(R.id.et_idn) EditText etIdn;
    @BindView(R.id.tv_info) TextView tvInfo;
    @BindView(R.id.clearLogin) View clearLogin;
    @BindView(R.id.clearIdn) View clearIdn;

    private RegistrationClient mRegistrationClient;

    private String phoneNumber;
    private boolean isPhoneMaskFilled;
    private boolean isValidPhone = true;
    private boolean allowRequest = true;
    private StepChangeListener mStepChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepChangeListener = (StepChangeListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationClient = new RegistrationClientImpl();
        mRegistrationClient.setCheckClientCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_client, container, false);
        ButterKnife.bind(this, view);
        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000]) [000]-[00]-[00]",
                true,
                etPhoneNumber,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        if (extractedValue.isEmpty()) {
                            clearLogin.setVisibility(View.GONE);
                        } else {
                            phoneNumber = extractedValue;
                            clearLogin.setVisibility(View.VISIBLE);
                        }
                        isPhoneMaskFilled = maskFilled;
                        if (maskFilled && allowRequest) {
                            allowRequest = false;
                            mRegistrationClient.checkPhoneNumber(parentActivity, extractedValue);
                        }
                        if (etPhoneHint.isErrorEnabled()) {
                            etPhoneHint.setErrorEnabled(false);
                        }
                        Log.i(TAG, extractedValue);
                        Log.i(TAG, String.valueOf(maskFilled));
                    }
                }
        );
        etPhoneNumber.addTextChangedListener(listener);
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && etPhoneNumber.getText().length() <= 1) {
                    etPhoneNumber.setText("7");
                }
            }
        });
        etIdn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    clearIdn.setVisibility(View.VISIBLE);
                    if (etIdnHint.isErrorEnabled()) {
                        etIdnHint.setErrorEnabled(false);
                    }
                } else {
                    clearIdn.setVisibility(View.GONE);
                }
            }
        });

        clearLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhoneNumber.getText().clear();
            }
        });

        clearIdn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIdn.getText().clear();
            }
        });

        return view;
    }

    @OnClick(R.id.btn_check_client)
    public void onCheckClientButtonClick() {
        if (validate()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("PhoneNumber", phoneNumber);
                jsonObject.put("Idn", etIdn.getText().toString());
                mRegistrationClient.checkClient(parentActivity, jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "Error when put fields to json", e);
            }
        }
    }

    private boolean validate() {
        boolean isValid = true;
        if (TextUtils.isEmpty(etIdn.getText())) {
            etIdnHint.setError(getString(R.string.error_empty));
            isValid = false;
        } else if (etIdn.getText().toString().length() != 12) {
            etIdnHint.setError(getString(R.string.error_wrong_format));
            isValid = false;
        }
        if (TextUtils.isEmpty(etPhoneNumber.getText())) {
            etPhoneHint.setError(getString(R.string.error_empty));
            isValid = false;
        } else if (!isPhoneMaskFilled) {
            etPhoneHint.setError(getString(R.string.error_wrong_format));
            isValid = false;
        } else if (!isValidPhone) {
            etPhoneHint.setError(getString(R.string.incorrect_phone_number));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void checkPhoneNumberResponse(int statusCode, String errorMessage, String resultCode) {
        allowRequest = true;
        if (statusCode == 200) {
            if (Integer.valueOf(resultCode) != 0) {
                isValidPhone = false;
                etPhoneHint.setError(getString(R.string.incorrect_phone_number));
            } else {
                isValidPhone = true;
                etPhoneHint.setErrorEnabled(false);
            }
        }
    }

    @Override
    public void clientCheckResponse(int statusCode, String errorMessage, BaseRegistrationResponse response) {
        if (statusCode == 200) {
            etPhoneHint.setErrorEnabled(false);
            etIdnHint.setErrorEnabled(false);
            if (response.code != null) {
                switch (response.code) {
                    case ACTIVE_USER:
                        tvInfo.setVisibility(View.VISIBLE);
                        tvInfo.setText(getString(R.string.client_already_registered));
                        break;
                    case BLOCKED_USER:
                        tvInfo.setVisibility(View.VISIBLE);
                        tvInfo.setText(getString(R.string.client_has_blocked));
                        break;
                    case REG_FOR_CLIENT:
                    case SUCCESS:
                        tvInfo.setVisibility(View.GONE);
                        ((OldRegistrationActivity) parentActivity).isClientOfBank = true;
                        ((OldRegistrationActivity) parentActivity).idn = etIdn.getText().toString();
                        ((OldRegistrationActivity) parentActivity).phoneNumber = phoneNumber;
                        mStepChangeListener.onStepChange();
                        break;
                    case REG_FOR_NO_CLIENT:
                        tvInfo.setVisibility(View.GONE);
                        ((OldRegistrationActivity) parentActivity).isClientOfBank = false;
                        ((OldRegistrationActivity) parentActivity).idn = etIdn.getText().toString();
                        ((OldRegistrationActivity) parentActivity).phoneNumber = phoneNumber;
                        mStepChangeListener.onStepChange();
                        break;
                    case SAME_IDN:
                        tvInfo.setVisibility(View.GONE);
                        etIdnHint.setError(getString(R.string.client_with_idn_has_already_exist));
                        break;
                    case SAME_PHONE:
                        tvInfo.setVisibility(View.GONE);
                        etPhoneHint.setError(getString(R.string.client_with_number_has_already_exist));
                        break;
                    case UNEXPECTED_STATUS_USER:
                        tvInfo.setVisibility(View.VISIBLE);
                        tvInfo.setText(getString(R.string.client_already_registered_generic_error));
                        break;
                    default:
                        if (response.message != null) {
                            onError(response.message);
                        }
                        break;
                }
            } else {
                if (response.message != null) {
                    onError(response.message);
                }
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }
}
