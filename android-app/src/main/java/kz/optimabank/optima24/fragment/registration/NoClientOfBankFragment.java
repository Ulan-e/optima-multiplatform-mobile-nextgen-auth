package kz.optimabank.optima24.fragment.registration;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.textfield.TextInputLayout;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.base.SecretQuestionResponse;
import kz.optimabank.optima24.model.interfaces.RegistrationClient;
import kz.optimabank.optima24.model.service.RegistrationClientImpl;
import kz.optimabank.optima24.secondary_registration.StepChangeListener;
import kz.optimabank.optima24.secondary_registration.ui.OldRegistrationActivity;

import static kz.optimabank.optima24.fragment.CustomListFragment.CUSTOM_LIST_EXTRA;
import static kz.optimabank.optima24.fragment.CustomListFragment.SELECTED_ITEM_EXTRA;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class NoClientOfBankFragment extends ATFFragment implements TextWatcher, RegistrationClientImpl.SecretQuestionsCallback, RegistrationClientImpl.RequestSmsNoClientBankCallback, View.OnClickListener {
    private static final String TAG = NoClientOfBankFragment.class.getSimpleName();

    @BindView(R.id.et_phone_hint) TextInputLayout etPhoneHint;
    @BindView(R.id.et_idn_hint) TextInputLayout etIdnHint;
    @BindView(R.id.et_first_name_hint) TextInputLayout etFirstNameHint;
    @BindView(R.id.et_middle_name_hint) TextInputLayout etMiddleNameHint;
    @BindView(R.id.et_last_name_hint) TextInputLayout etLastNameHint;
    @BindView(R.id.et_password_hint) TextInputLayout etPasswordHint;
    @BindView(R.id.et_repeat_password_hint) TextInputLayout etRepeatPasswordHint;
    @BindView(R.id.et_secret_question_hint) TextInputLayout etSecretQuestionHint;
    @BindView(R.id.et_answer_hint) TextInputLayout etAnswerHint;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_idn) EditText etIdn;
    @BindView(R.id.et_first_name) EditText etFirstName;
    @BindView(R.id.et_middle_name) EditText etMiddleName;
    @BindView(R.id.et_last_name) EditText etLastName;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_repeat_password) EditText etRepeatPassword;
    @BindView(R.id.et_secret_question) EditText etSecretQuestion;
    @BindView(R.id.et_answer) EditText etAnswer;
    @BindView(R.id.cb_agreement) CheckBox cbAgreement;
    @BindView(R.id.tv_agreement) TextView tvAgreement;
    @BindView(R.id.tv_confirm_error) TextView tvConfirmError;
    @BindView(R.id.clearLastName) View clearLastName;
    @BindView(R.id.clearFirstName) View clearFirstName;
    @BindView(R.id.clearMiddleName) View clearMiddleName;
    @BindView(R.id.clearPassword) View clearPassword;
    @BindView(R.id.clearRePassword) View clearRePassword;
    @BindView(R.id.clearAnswer) View clearAnswer;

    private StepChangeListener mStepChangeListener;
    private final List<TextInputLayout> validateFields = new ArrayList<>();
    private RegistrationClient mRegistrationClient;
    private List<SecretQuestionResponse> mSecretQuestionResponses = new ArrayList<>();
    private SecretQuestionResponse mSelectedQuestion;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepChangeListener = (StepChangeListener) context;
        mRegistrationClient = new RegistrationClientImpl();
        mRegistrationClient.setSecretQuestionsCallback(this);
        mRegistrationClient.setRequestSmsNoClientBankCallback(this);
        mRegistrationClient.getSecretQuestions(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_client_of_bank, container, false);
        ButterKnife.bind(this, view);
        setSpannableLinks();
        prepareFields();
        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvConfirmError.setVisibility(View.GONE);
                }
            }
        });

        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000]) [000]-[00]-[00]",
                true,
                etPhone,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        if (etPhoneHint.isErrorEnabled()) {
                            etPhoneHint.setErrorEnabled(false);
                        }
                        Log.i(TAG, extractedValue);
                        Log.i(TAG, String.valueOf(maskFilled));
                    }
                }
        );
        etPhone.addTextChangedListener(listener);
        etPhone.setEnabled(false);
        etIdn.setEnabled(false);
        etPhone.setText("7" + ((OldRegistrationActivity) parentActivity).phoneNumber);
        etIdn.setText(((OldRegistrationActivity) getActivity()).idn);

        InputFilter[] filters = {
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches(getString(R.string.reg_filter_regex3))) {
                            return src;
                        }
                        return "";
                    }
                }
        };
        etFirstName.setFilters(filters);
        etMiddleName.setFilters(filters);
        etLastName.setFilters(filters);

        clearLastName.setOnClickListener(this);
        clearFirstName.setOnClickListener(this);
        clearMiddleName.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        clearRePassword.setOnClickListener(this);
        clearAnswer.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStepChangeListener = null;
    }

    @OnClick(R.id.btn_further)
    public void onFurtherClick() {
        if (validate()) {
            try {
                JSONObject noClientObject = new JSONObject();
                noClientObject.put("FirstName", etFirstName.getText().toString());
                noClientObject.put("MiddleName", etMiddleName.getText().toString());
                noClientObject.put("LastName", etLastName.getText().toString());
                noClientObject.put("Password", getSHA256(etPassword.getText().toString()));
                noClientObject.put("QuestionId", mSelectedQuestion.questionId);
                noClientObject.put("Answer", etAnswer.getText().toString());
                noClientObject.put("Idn", ((OldRegistrationActivity) parentActivity).idn);
                noClientObject.put("PhoneNumber", ((OldRegistrationActivity) parentActivity).phoneNumber);
                mRegistrationClient.requestSmsNoClientBank(parentActivity, noClientObject);
            } catch (JSONException e) {
                Log.e(TAG, "Exception when put filed to jsonObject", e);
            }
        }
    }

    @OnClick(R.id.view_secret_questions)
    public void onSecretQuestionClick() {
        Intent intent = new Intent(parentActivity, NavigationActivity.class);
        intent.putExtra("isCustomList", true);
        intent.putExtra(CUSTOM_LIST_EXTRA, (ArrayList) mSecretQuestionResponses);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS && requestCode == 1) {
            if (data != null) {
                mSelectedQuestion = (SecretQuestionResponse) data.getSerializableExtra(SELECTED_ITEM_EXTRA);
                etSecretQuestion.setText(mSelectedQuestion.question);
            }
        }
    }

    private void setSpannableLinks() {
        SpannableString contractAndPolicySpan = new SpannableString(getString(R.string.i_agree_with_contract_and_policy));
        String spannableString;
        int startIndex;
        int endIndex;
        if (contractAndPolicySpan.toString().contains(getString(R.string.to_policy))) {
            spannableString = getString(R.string.to_policy);
            startIndex = contractAndPolicySpan.toString().indexOf(spannableString);
            endIndex = startIndex + spannableString.length();
            contractAndPolicySpan.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Log.i(TAG, "policy");
                    widget.invalidate();
                    Intent intent = new Intent(parentActivity, NavigationActivity.class);
                    intent.putExtra("isPdfView", true);
                    startActivity(intent);
                }
            }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (contractAndPolicySpan.toString().contains(getString(R.string.to_contract))) {
            spannableString = getString(R.string.to_contract);
            startIndex = contractAndPolicySpan.toString().indexOf(spannableString);
            endIndex = startIndex + spannableString.length();
            contractAndPolicySpan.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Log.i(TAG, "contract");
                    widget.invalidate();
                    Intent intent = new Intent(parentActivity, NavigationActivity.class);
                    intent.putExtra("isPdfView", true);
                    startActivity(intent);
                }
            }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvAgreement.setText(contractAndPolicySpan);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void prepareFields() {
        validateFields.add(etFirstNameHint);
        validateFields.add(etMiddleNameHint);
        validateFields.add(etLastNameHint);
        validateFields.add(etPasswordHint);
        validateFields.add(etRepeatPasswordHint);
        validateFields.add(etSecretQuestionHint);
        validateFields.add(etAnswerHint);

        for (TextInputLayout textInputLayout : validateFields) {
            textInputLayout.getEditText().addTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        for (TextInputLayout textInputLayout : validateFields) {
            if (s == etFirstName.getEditableText()) {
                if (etFirstName.getText().toString().replaceAll(" ","").length() == 0) {
                    etFirstNameHint.setError((getString(R.string.need_fill_field_fmt, etFirstNameHint.getHint())));
                    clearFirstName.setVisibility(View.GONE);
                } else {
                    if (etFirstName.getText().length() < 2) {
                        etFirstNameHint.setError(getString(R.string.error_number_of_characters));
                    } else {
                        etFirstNameHint.setErrorEnabled(false);
                    }
                    clearFirstName.setVisibility(View.VISIBLE);
                }
                break;
            } else if (s == etLastName.getEditableText()) {
                if (etLastName.getText().toString().replaceAll(" ","").length() == 0) {
                    etLastNameHint.setError((getString(R.string.need_fill_field_fmt, etLastNameHint.getHint())));
                    clearLastName.setVisibility(View.GONE);
                } else {
                    if (etLastName.getText().length() < 2) {
                        etLastNameHint.setError(getString(R.string.error_number_of_characters));
                    } else {
                        etLastNameHint.setErrorEnabled(false);
                    }
                    clearLastName.setVisibility(View.VISIBLE);
                }
                break;
            } else if (s == etMiddleName.getEditableText()) {
                if (etMiddleName.getText().toString().replaceAll(" ","").length() == 0) {
                    etMiddleNameHint.setError((getString(R.string.need_fill_field_fmt, etMiddleNameHint.getHint())));
                    clearMiddleName.setVisibility(View.GONE);
                } else {
                    if (etMiddleName.getText().length() < 2) {
                        etMiddleNameHint.setError(getString(R.string.error_number_of_characters));
                    } else {
                        etMiddleNameHint.setErrorEnabled(false);
                    }
                    clearMiddleName.setVisibility(View.VISIBLE);
                }
                break;
            } else if (s == etPassword.getEditableText()) {
                if (etPassword.getText().length() == 0) {
                    etPasswordHint.setError((getString(R.string.need_fill_field_fmt, etPasswordHint.getHint())));
                    clearPassword.setVisibility(View.GONE);
                } else {
                    if (!isOkPassword(etPassword.getText().toString())) {
                        etPasswordHint.setError(getString(R.string.error_number_of_characters));
                    } else {
                        etPasswordHint.setErrorEnabled(false);
                    }
                    clearPassword.setVisibility(View.VISIBLE);
                }
                break;
            } else if (s == etRepeatPassword.getEditableText()) {
                if (etRepeatPassword.getText().length() == 0) {
                    etRepeatPasswordHint.setError((getString(R.string.need_fill_field_fmt, etRepeatPasswordHint.getHint())));
                    clearRePassword.setVisibility(View.GONE);
                } else {
                    if (!isOkPassword(etRepeatPassword.getText().toString())) {
                        etRepeatPasswordHint.setError(getString(R.string.error_number_of_characters));
                    } else if (!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())) {
                        etRepeatPasswordHint.setError(getString(R.string.passwords_doesnt_match));
                    } else {
                        etRepeatPasswordHint.setErrorEnabled(false);
                    }
                    clearRePassword.setVisibility(View.VISIBLE);
                }
                break;
            } else if (s == etAnswer.getEditableText()) {
                if (etAnswer.getText().length() == 0) {
                    etAnswerHint.setError((getString(R.string.need_fill_field_fmt, etAnswerHint.getHint())));
                    clearAnswer.setVisibility(View.GONE);
                } else {
                    etAnswerHint.setErrorEnabled(false);
                    clearAnswer.setVisibility(View.VISIBLE);
                }
                break;
            }
//            switch (textInputLayout.getEditText().getId()) {
//                case R.id.et_first_name:
//                    if (etFirstName.getText().length() == 0) {
//                        etFirstNameHint.setError((getString(R.string.need_fill_field_fmt, textInputLayout.getHint())));
//                        clearFirstName.setVisibility(View.GONE);
//                    } else {
//                        if (etFirstName.getText().length() < 2) {
//                            etFirstNameHint.setError(getString(R.string.error_wrong_format));
//                        } else {
//                            etFirstNameHint.setErrorEnabled(false);
//                        }
//                        clearFirstName.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.et_last_name:
//                    if (etLastName.getText().length() == 0) {
//                        etLastNameHint.setError((getString(R.string.need_fill_field_fmt, textInputLayout.getHint())));
//                        clearLastName.setVisibility(View.GONE);
//                    } else {
//                        if (etLastName.getText().length() < 2) {
//                            etFirstNameHint.setError(getString(R.string.error_wrong_format));
//                        } else {
//                            etFirstNameHint.setErrorEnabled(false);
//                        }
//                        clearLastName.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.et_middle_name:
//                    if (etMiddleName.getText().length() == 0) {
//                        etMiddleNameHint.setError((getString(R.string.need_fill_field_fmt, textInputLayout.getHint())));
//                        clearMiddleName.setVisibility(View.GONE);
//                    } else {
//                        if (etMiddleName.getText().length() < 2) {
//                            etMiddleNameHint.setError(getString(R.string.error_wrong_format));
//                        } else {
//                            etMiddleNameHint.setErrorEnabled(false);
//                        }
//                        clearMiddleName.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.et_password:
//                    if (etPassword.getText().length() == 0) {
//                        etPasswordHint.setError((getString(R.string.need_fill_field_fmt, textInputLayout.getHint())));
//                        clearPassword.setVisibility(View.GONE);
//                    } else {
//                        if (!isOkPassword(etPassword.getText().toString())) {
//                            etPasswordHint.setError(getString(R.string.error_wrong_format));
//                        } else {
//                            etPasswordHint.setErrorEnabled(false);
//                        }
//                        clearPassword.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.et_repeat_password:
//                    if (etRepeatPassword.getText().length() == 0) {
//                        etRepeatPasswordHint.setError((getString(R.string.need_fill_field_fmt, textInputLayout.getHint())));
//                        clearRePassword.setVisibility(View.GONE);
//                    } else {
//                        if (!isOkPassword(etRepeatPassword.getText().toString())) {
//                            etRepeatPasswordHint.setError(getString(R.string.error_wrong_format));
//                        } else if (!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())) {
//                            etRepeatPasswordHint.setError(getString(R.string.passwords_doesnt_match));
//                        } else {
//                            etRepeatPasswordHint.setErrorEnabled(false);
//                        }
//                        clearRePassword.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.et_answer:
//                    if (etAnswer.getText().length() == 0) {
//                        etAnswerHint.setError((getString(R.string.need_fill_field_fmt, textInputLayout.getHint())));
//                        clearAnswer.setVisibility(View.GONE);
//                    } else {
//                        etAnswerHint.setErrorEnabled(false);
//                        clearAnswer.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
        }
    }

    private boolean validate() {
        boolean isValid = true;

        if (!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())) {
            etRepeatPasswordHint.setError(getString(R.string.passwords_doesnt_match));
            isValid = false;
        }
        if (!isOkPassword(etPassword.getText().toString())) {
            isValid = false;
            etPasswordHint.setError(getString(R.string.error_number_of_characters));
        }
        if (!isOkPassword(etRepeatPassword.getText().toString())) {
            isValid = false;
            etRepeatPasswordHint.setError(getString(R.string.error_number_of_characters));
        }

        if (etFirstName.getText().length() < 2) {
            isValid = false;
            etFirstNameHint.setError(getString(R.string.error_number_of_characters));
        }
        if (etMiddleName.getText().length() < 2) {
            isValid = false;
            etMiddleNameHint.setError(getString(R.string.error_number_of_characters));
        }
        if (etLastName.getText().length() < 2) {
            isValid = false;
            etLastNameHint.setError(getString(R.string.error_number_of_characters));
        }

        for (TextInputLayout textInputLayout : validateFields) {
            if (TextUtils.isEmpty(textInputLayout.getEditText().getText())) {
                textInputLayout.setError(getString(R.string.need_fill_field_fmt, textInputLayout.getHint()));
                isValid = false;
            }
        }

        if (!cbAgreement.isChecked()) {
            tvConfirmError.setVisibility(View.VISIBLE);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void secretQuestionsResponse(int statusCode, String errorMessage, List<SecretQuestionResponse> response) {
        if (statusCode == 200) {
            mSecretQuestionResponses = response;
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private String getSHA256(String passw) {
        String def;
        String newPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(passw.getBytes(StandardCharsets.UTF_8));
            def = Base64.encodeToString(hash, Base64.DEFAULT);
            passw = def.replaceAll("\n", "");
            newPassword = passw;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return newPassword;
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
        return isDigit && isLetter && pass.length() > 7;
    }

    @Override
    public void requestSmsNoClientBankResponse(int statusCode, String errorMessage, BaseRegistrationResponse response) {
        if (statusCode == 200) {
            switch (response.code) {
                case SUCCESS:
                    mStepChangeListener.onStepChange();
                    break;
                default:
                    onError(response.message);
                    break;
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearLastName:
                etLastName.getText().clear();
                break;
            case R.id.clearFirstName:
                etFirstName.getText().clear();
                break;
            case R.id.clearMiddleName:
                etMiddleName.getText().clear();
                break;
            case R.id.clearPassword:
                etPassword.getText().clear();
                break;
            case R.id.clearRePassword:
                etRepeatPassword.getText().clear();
                break;
            case R.id.clearAnswer:
                etAnswer.getText().clear();
                break;
        }
    }
}
