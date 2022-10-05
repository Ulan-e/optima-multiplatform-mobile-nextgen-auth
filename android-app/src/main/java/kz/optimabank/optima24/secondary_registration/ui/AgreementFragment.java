package kz.optimabank.optima24.secondary_registration.ui;

import static kz.optimabank.optima24.utility.Constants.BACKGROUND_NOTIFICATION_ID;
import static kz.optimabank.optima24.utility.Constants.IS_BACKGROUND_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_CODE;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_LOGIN;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_TAG;
import static kz.optimabank.optima24.utility.Constants.SUCCESS;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import kz.optimabank.optima24.R;
import kz.optimabank.optima24.databinding.FragmentAgreementBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.secondary_registration.viewModels.AgreementViewModel;
import kz.optimabank.optima24.secondary_registration.viewModels.AgreementViewModelFactory;
import kz.optimabank.optima24.utility.LocaleUtils;
import kz.optimabank.optima24.utility.Utilities;

public class AgreementFragment extends ATFFragment {

    private final String agreementRu = "registration_agreement_ru.pdf";
    private final String agreementKg = "registration_agreement_kg.pdf";
    private final String agreementEn = "registration_agreement_en.pdf";

    private static final int SMS_CODE_LENGTH = 4;
    private static final int HANDLER_DELAY = 2000;
    private static final int INCORRECT_SMS_CODE = -10001;
    private static final int REGISTRATION_PAGE = 1;

    private FragmentAgreementBinding binding;
    private AgreementViewModel model;

    private AlertDialog alertDialog;
    private View viewAlertDialog;
    private String login;
    private String registrationCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity(), new AgreementViewModelFactory(requireActivity()
                .getApplication()))
                .get(AgreementViewModel.class
                );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArgs();
        checkUser();
        showErrorMessage();

        changeContinueButtonState();
        setContinueClickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getAgreementShowState()){
            showDocument();
        }
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            login = args.getString(REGISTRATION_LOGIN);
            registrationCode = args.getString(REGISTRATION_CODE);
        }
    }

    // не делаем запрос для получения файла так как с WebView у нас были проблемы с сертификатом
    private void getDocument() {
        binding.progressBarLoading.setVisibility(View.VISIBLE);

        model.getAgreementDocument().observe(getViewLifecycleOwner(), response -> {
            binding.progressBarLoading.setVisibility(View.GONE);
            if (response.code == SUCCESS) {
                binding.progressBarLoading.setVisibility(View.GONE);
                showDocument();
                Log.d(REGISTRATION_TAG, "agreement document url   " + response.data);
            } else {
                showMessage(response.message);
                Log.e(REGISTRATION_TAG, "agreement document url error " + response.message);
            }
        });
    }

    private void showDocument() {
        String languageApp = LocaleUtils.getLanguage(requireContext());
        if (languageApp != null) {
            switch (languageApp.toUpperCase()) {
                case "RU":
                    showPdfFile(agreementRu);
                    break;
                case "EN":
                    showPdfFile(agreementEn);
                    break;
                default:
                    showPdfFile(agreementKg);
                    break;
            }
        } else {
            showPdfFile(agreementRu);
        }
    }

    private void showPdfFile(String fileName) {
        binding.pdfViewDocument.fromAsset(fileName)
                .enableDoubletap(true)
                .load();
    }

    // проверка пользователя
    private void checkUser() {
        binding.progressBarLoading.setVisibility(View.VISIBLE);

        model.checkUser(login, registrationCode).observe(getViewLifecycleOwner(), response -> {
            binding.progressBarLoading.setVisibility(View.VISIBLE);
            if (response.code == SUCCESS) {
                showInputSmsCodeDialog();
                Log.d(REGISTRATION_TAG, "checkUser success " + response.data);
            } else {
                showMessage(response.message);
                backToMainPage();
                Log.e(REGISTRATION_TAG, "checkUser error " + response.data);
            }
        });
    }

    private void showErrorMessage() {
        model.errorMessage.observe(getViewLifecycleOwner(), errorMessage -> {
            showMessage(errorMessage);
            binding.progressBarLoading.setVisibility(View.GONE);
            Log.d(REGISTRATION_TAG, "getErrorMessage() checkUser Success " + errorMessage);
            backToMainPage();
        });
    }

    private void setContinueClickListener() {
        binding.btnContinue.setOnClickListener(v ->
                ((OldRegistrationActivity) requireActivity()).setCurrentItem(
                        REGISTRATION_PAGE,
                        false
                )
        );
    }

    // меняем состояние кнопки если пользователь ознакомился с офертой
    private void changeContinueButtonState() {
        binding.btnContinue.setEnabled(false);
        binding.checkboxAgreement.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.btnContinue.setEnabled(isChecked)
        );
    }

    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showInputSmsCodeDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        viewAlertDialog = inflater.inflate(R.layout.dialog_input_sms_code, null);
        alertDialog = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setView(viewAlertDialog).create();
        EditText inputCode = viewAlertDialog.findViewById(R.id.input_sms_code);
        Button btnSend = viewAlertDialog.findViewById(R.id.btn_send_sms_code);

        inputCode.requestFocus();
        btnSend.setOnClickListener(button -> {
            String smsCode = inputCode.getText().toString();
            if (!smsCode.isEmpty() && smsCode.length() == SMS_CODE_LENGTH) {
                confirmSmsCode(login, registrationCode, smsCode);
            } else {
                Snackbar.make(viewAlertDialog, requireContext().getString(R.string.text_sms), Snackbar.LENGTH_SHORT).show();
            }
        });

        Window window = alertDialog.getWindow();
        if (window != null) window.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    // потверждение проверки пользователя с смс кодом
    private void confirmSmsCode(String login, String registrationCode, String smsCode) {
        model.confirmCheckUser(login, registrationCode, smsCode).observe(getViewLifecycleOwner(), response -> {
            switch (response.code) {
                case SUCCESS: {
                    alertDialog.dismiss();
                    binding.progressBarLoading.setVisibility(View.GONE);
                    showDocument();
                    putAgreementShowState(true);
                    Log.d(REGISTRATION_TAG, "checkUser confirmation + " + response.data);
                    break;
                }
                case INCORRECT_SMS_CODE: {
                    if (viewAlertDialog != null) {
                        EditText inputSmsCode = viewAlertDialog.findViewById(R.id.input_sms_code);
                        inputSmsCode.setText("");
                        Snackbar.make(viewAlertDialog, response.message, Snackbar.LENGTH_SHORT).show();
                    }
                    Log.d(REGISTRATION_TAG, "checkUser error " + response.message);
                    putAgreementShowState(false);
                    break;
                }
                default: {
                    showMessage(response.message);
                    alertDialog.dismiss();
                    backToMainPage();
                    Log.e(REGISTRATION_TAG, "checkUser error " + response.data);
                    break;
                }
            }
        });
    }

    // возвращаемся в главный экран
    private void backToMainPage() {
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                requireActivity().finish(), HANDLER_DELAY
        );
    }

    private void putAgreementShowState(Boolean isShowed){
        SharedPreferences.Editor editor = Utilities.getPreferences(requireContext()).edit();
        editor.putBoolean("is_agreement_show_state", isShowed);
        editor.apply();
    }

    private Boolean getAgreementShowState() {
        SharedPreferences preferences = Utilities.getPreferences(requireContext());
        return preferences.getBoolean("is_agreement_show_state", false);
    }

    public static AgreementFragment newInstance(String login, String registrationCode) {
        AgreementFragment fragment = new AgreementFragment();
        Bundle args = new Bundle();
        args.putString(REGISTRATION_LOGIN, login);
        args.putString(REGISTRATION_CODE, registrationCode);
        fragment.setArguments(args);
        return fragment;
    }
}