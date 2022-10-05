package kz.optimabank.optima24.fragment.registration;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.interfaces.RegistrationClient;
import kz.optimabank.optima24.model.service.RegistrationClientImpl;
import kz.optimabank.optima24.secondary_registration.StepChangeListener;
import kz.optimabank.optima24.secondary_registration.ui.OldRegistrationActivity;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class ClientOfBankFragment extends ATFFragment implements RegistrationClientImpl.RequestSmsClientBankCallback {
    private static final String TAG = ClientOfBankFragment.class.getSimpleName();

    @BindView(R.id.et_phone_hint) TextInputLayout etPhoneHint;
    @BindView(R.id.et_idn_hint) TextInputLayout etIdnHint;
    @BindView(R.id.et_phone) EditText etPhoneNumber;
    @BindView(R.id.et_idn) EditText etIdn;
    @BindView(R.id.cb_agreement) CheckBox cbAgreement;
    @BindView(R.id.tv_agreement) TextView tvAgreement;
    @BindView(R.id.tv_confirm_error) TextView tvConfirmError;

    private RegistrationClient mRegistrationClient;
    private StepChangeListener mStepChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepChangeListener = (StepChangeListener) context;
        mRegistrationClient = new RegistrationClientImpl();
        mRegistrationClient.setRequestSmsClientBankCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_of_bank, container, false);
        ButterKnife.bind(this, view);
        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+7 ([000]) [000]-[00]-[00]",
                true,
                etPhoneNumber,
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
        etPhoneNumber.addTextChangedListener(listener);
        etPhoneNumber.setEnabled(false);
        etIdn.setEnabled(false);
        etPhoneNumber.setText("7" + ((OldRegistrationActivity) parentActivity).phoneNumber);
        etIdn.setText(((OldRegistrationActivity) getActivity()).idn);

        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvConfirmError.setVisibility(View.GONE);
                }
            }
        });

        setSpannableLinks();

        parentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
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
                    // intent.putExtra(PdfViewFragment.PDF_FILE, "PrivacyPolicy.pdf");
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
                    // intent.putExtra(PdfViewFragment.PDF_FILE, "contract.pdf");
                    startActivity(intent);
                }
            }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvAgreement.setText(contractAndPolicySpan);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStepChangeListener = null;
    }

    @OnClick(R.id.btn_further)
    public void onFurtherClick() {
        if (!cbAgreement.isChecked()) {
            tvConfirmError.setVisibility(View.VISIBLE);
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phoneNumber", ((OldRegistrationActivity) parentActivity).phoneNumber);
                jsonObject.put("idn", etIdn.getText().toString());
                mRegistrationClient.requestSmsClientBank(parentActivity, jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "Error when put fields to json", e);
            }
        }
    }

    @Override
    public void requestSmsClientBankResponse(int statusCode, String errorMessage, BaseRegistrationResponse responseBody) {
        if (statusCode == 200) {
            switch (responseBody.code) {
                case SUCCESS:
                    mStepChangeListener.onStepChange();
                    break;
                case INCORRECT_PHONE:
                    etPhoneHint.setError(responseBody.message);
                    break;
                case SAME_IDN:
                    etIdnHint.setError(responseBody.message);
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
