package kz.optimabank.optima24.secondary_registration.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import kg.optima.mobile.R;
import kg.optima.mobile.databinding.FragmentRegistrationBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.SecretQuestionResponse;
import kz.optimabank.optima24.secondary_registration.viewModels.RegistrationViewModel;
import kz.optimabank.optima24.secondary_registration.viewModels.RegistrationViewModelFactory;

import static kz.optimabank.optima24.utility.Constants.REGISTRATION_TAG;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_LOGIN;
import static kz.optimabank.optima24.utility.Constants.SUCCESS;

public class RegistrationFragment extends ATFFragment {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,15}$";
    private static final int REGISTRATION_PAGE = 2;

    private FragmentRegistrationBinding binding;
    private RegistrationViewModel model;
    private List<SecretQuestionResponse> secretQuestions;
    private String login;
    private SecretQuestionResponse question;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity(), new RegistrationViewModelFactory(requireActivity().getApplication())).get(RegistrationViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArgs();
        getQuestions();
        clickButtonRegistration();
        setInputDoneListeners();
        setFocusChangeListeners();
        setSpinnerItemClickListener();
        showErrorMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((OldRegistrationActivity) requireActivity()).setToolbarVisibility(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftKeyBoard(requireContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void getArgs() {
        Bundle args = getArguments();
        login = args != null ? args.getString(REGISTRATION_LOGIN) : "";
    }

    private void setSpinnerItemClickListener() {
        binding.spinnerQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question = secretQuestions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                question = secretQuestions.get(0);
            }
        });
    }

    private void setFocusChangeListeners() {
        binding.inputAnswer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) binding.textSecurityAnswer.setError(null);
            else checkInputAnswer();
        });

        binding.inputNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) binding.textNewPassword.setError(null);
            else checkPassword();
        });

        binding.inputPasswordConfirmation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) binding.textNewPasswordConfirmation.setError(null);
            else checkPasswordConfirmation();
        });
    }

    private void setInputDoneListeners() {
        binding.inputAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkInputAnswer();
                return true;
            }
            return false;
        });
        binding.inputNewPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPassword();
                return true;
            }
            return false;
        });

        binding.inputPasswordConfirmation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPasswordConfirmation();
                return true;
            }
            return false;
        });
    }

    private void getQuestions() {
        model.getQuestions().observe(getViewLifecycleOwner(), this::initSpinnerData);
    }

    private void initSpinnerData(List<SecretQuestionResponse> questions) {
        secretQuestions = questions;
        ArrayAdapter<SecretQuestionResponse> questionsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, questions);
        questionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerQuestions.setAdapter(questionsAdapter);
        binding.spinnerQuestions.setSelection(0);
    }

    private void checkInputAnswer() {
        if (binding.inputAnswer.getText().toString().isEmpty()) {
            binding.textSecurityAnswer.setError(getString(R.string.text_input_answer));
        } else {
            hideSoftKeyBoard(requireContext());
            binding.textSecurityAnswer.setError(null);
        }
    }

    private void checkPasswordForEmpty() {
        if (binding.inputNewPassword.getText().toString().isEmpty()) {
            binding.textNewPassword.setError(getString(R.string.text_input_new_password));
        } else {
            binding.textNewPassword.setError(null);
        }
    }

    private void checkPassword() {
        if (!binding.inputNewPassword.getText().toString().matches(PASSWORD_REGEX)) {
            binding.textNewPassword.setError(getString(R.string.incorrect_password));
        } else {
            hideSoftKeyBoard(requireContext());
            binding.textNewPassword.setError(null);
        }
    }

    private void checkPasswordConfirmationForEmpty() {
        if (binding.inputPasswordConfirmation.getText().toString().isEmpty()) {
            binding.textNewPasswordConfirmation.setError(getString(R.string.text_input_password_confirmation));
        } else {
            binding.textNewPasswordConfirmation.setError(null);
        }
    }

    private void checkPasswordConfirmation() {
        if (!binding.inputNewPassword.getText().toString().equals(binding.inputPasswordConfirmation.getText().toString())) {
            binding.textNewPasswordConfirmation.setError(getString(R.string.passwords_doesnt_match));
        } else {
            binding.textNewPasswordConfirmation.setError(null);
            hideSoftKeyBoard(requireContext());
        }
    }

    private void clickButtonRegistration() {
        binding.btnRegistration.setOnClickListener(v -> {
                    checkInputAnswer();
                    checkPasswordForEmpty();
                    checkPassword();
                    checkPasswordConfirmationForEmpty();
                    checkPasswordConfirmation();

                    if (binding.textSecurityAnswer.getError() == null
                            && binding.textNewPassword.getError() == null
                            && binding.textNewPasswordConfirmation.getError() == null) {
                        saveUser();
                    }
                }
        );
    }

    // отправляем запрос на сохранение данных пользователя
    private void saveUser() {
        binding.progressBarLoading.setVisibility(View.VISIBLE);
        binding.btnRegistration.setVisibility(View.GONE);

        saveUserData();
    }

    private void saveUserData() {
        String answer = binding.inputAnswer.getText().toString();
        String password = binding.inputPasswordConfirmation.getText().toString();
        model.saveUser(login, password, answer, question.questionId).observe(getViewLifecycleOwner(), response -> {
            if (response.code == SUCCESS) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ((OldRegistrationActivity) requireActivity()).setCurrentItem(REGISTRATION_PAGE, false);
                Log.d(REGISTRATION_TAG, "getSaveUserResult " + response.data);
            } else {
                binding.progressBarLoading.setVisibility(View.GONE);
                showMessage(response.message);
                Log.e(REGISTRATION_TAG, "getSaveUserResult error " + response.data);
            }
        });
    }

    private void showErrorMessage() {
        model.errorMessage.observe(getViewLifecycleOwner(), message -> {
            binding.progressBarLoading.setVisibility(View.GONE);
            binding.btnRegistration.setEnabled(false);
            showMessage(message);
        });
    }

    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    public static RegistrationFragment newInstance(String login) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(REGISTRATION_LOGIN, login);
        fragment.setArguments(args);
        return fragment;
    }
}