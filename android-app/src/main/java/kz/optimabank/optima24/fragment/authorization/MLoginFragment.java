package kz.optimabank.optima24.fragment.authorization;

import static kz.optimabank.optima24.utility.Constants.BANK_ID;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.IS_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_CODE;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_LOGIN;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_TAG;
import static kz.optimabank.optima24.utility.Constants.REG_EX_FOR_PHONE;
import static kz.optimabank.optima24.utility.Constants.SUCCESS;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.PollsActivity;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.LoginActivity;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.controllers.DHKeyController;
import kz.optimabank.optima24.db.controllers.PushTokenKeyController;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.authorization.viewModel.RegistrationPushViewModel;
import kz.optimabank.optima24.fragment.authorization.viewModel.RegistrationPushViewModelFactory;
import kz.optimabank.optima24.fragment.registration.ChangePasswordForRegFragment;
import kz.optimabank.optima24.imprint.FingerprintAuthentication;
import kz.optimabank.optima24.imprint.FingerprintUiHelper;
import kz.optimabank.optima24.model.interfaces.AuthorizationUser;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AuthorizationUserImpl;
import kz.optimabank.optima24.secondary_registration.ui.OldRegistrationActivity;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;
import rps.pincode.managers.AppLock;
import rps.pincode.managers.LockManager;

public class MLoginFragment extends ATFFragment implements FingerprintUiHelper.Callback,
        AuthorizationUserImpl.Callback, AuthorizationUserImpl.GetAuthorizationTypeCallback, TextWatcher {

    @BindView(R.id.button_login)
    Button button_login;
    @BindView(R.id.view)
    View blockView;
    @BindView(R.id.cbUseImprint)
    CheckBox cbUseImprint;
    @BindView(R.id.cbSaveLogin)
    CheckBox cbSaveLogin;
    @BindView(R.id.cbCode)
    CheckBox cbCode;
    @BindView(R.id.edittext_phone)
    EditText edittextLogin;
    @BindView(R.id.edittext_password)
    EditText edittextPassword;
    @BindView(R.id.ALLcontentLOGIN)
    ScrollView ALLcontentLOGIN;
    @BindView(R.id.ATFlogo)
    RelativeLayout ATFlogo;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.clearPass)
    ImageView clearPass;
    @BindView(R.id.clearLogin)
    ImageView clearLogin;
    @BindView(R.id.bt_register)
    Button bt_register;

    private static final String AUTHORIZATION = "1";
    private static final String REGISTRATION = "2";

    FingerprintAuthentication fingerprintAuthentication;
    boolean isNeedSms = false, isAdd = true;
    AlertDialog alert;
    AuthorizationUser authorizationUser;
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    SharedPreferences sPref, mSharedPreferences;
    SharedPreferences.Editor editor;
    String login, password, token = null;
    final public static String SAVED_LOGIN = "saved_login", SAVED_PASSWORD = "saved_password", LOGIN = "login",
            CODE = "code", CANCEL = "cancel", ATTEMPTS = "attempts";
    DHKeyController dhKeyController = DHKeyController.getController();
    public static final int REQUEST_CODE_ENABLE = 11;
    PushTokenKeyController pushTokenKeyController = PushTokenKeyController.getController();

    private static final int SMS_CODE_LENGTH = 4;
    private boolean finishReg;

    private RegistrationPushViewModel model;
    private String notificationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity(), new RegistrationPushViewModelFactory(requireActivity()
                .getApplication()))
                .get(RegistrationPushViewModel.class);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mSharedPreferences = getPreferences(requireContext());
        ButterKnife.bind(this, view);

        clearPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edittextPassword.getText().clear();
            }
        });
        clearLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edittextLogin.getText().clear();
            }
        });
        edittextPassword.addTextChangedListener(this);

        logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requireActivity() != null && isAdded()) {
                    requireActivity().finish();
                }
            }
        });

        if (checkVersion()) {
            fingerprintAuthentication = new FingerprintAuthentication(requireContext());
        }
        button_login.setOnClickListener(
                new OnClickListener() {
                    @SuppressLint("SyntheticAccessor")
                    @Override
                    public void onClick(View arg0) {
                        if (TextUtils.isEmpty(edittextLogin.getText())) {
                            edittextLogin.setError(getString(R.string.error_empty));
                            return;
                        }
                        String login = edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, "");
                        if (TextUtils.isEmpty(edittextPassword.getText())) {
                            edittextPassword.setError(getString(R.string.error_empty));
                            return;
                        }
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(Constants.userPhone, edittextLogin.getText().toString());
                        editor.apply();
                        if (password != null) {
                            setCode();
                        } else {
                            hideKeyboard();
                            checkLogin(login);
                        }
                    }
                });
        if (checkVersion()) {
            cbUseImprint.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!cbUseImprint.isClickable()) {
                        final AlertDialog.Builder builderInner = new AlertDialog.Builder(requireContext(), R.style.Theme);
                        builderInner.setMessage(getString(R.string.to_use_imprint));
                        builderInner.setIcon(R.mipmap.ic_optima24_launch);
                        builderInner.setTitle(R.string.app_name);
                        builderInner.setCancelable(false);
                        builderInner.setPositiveButton(
                                android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                                        startActivityForResult(intent, 99);

                                    }
                                });
                        builderInner.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builderInner.show();
                    }
                    return false;
                }
            });
        }
        if (ALLcontentLOGIN.getVisibility() == View.INVISIBLE) {
            ALLcontentLOGIN.setVisibility(View.VISIBLE);
            ATFlogo.setVisibility(View.INVISIBLE);
        }

        if (mSharedPreferences.getBoolean(getString(R.string.use_fingerprint_to_authenticate_key), false)) {
            cbUseImprint.setChecked(true);
        }

        return view;
    }

    // метод для проверки логина к какому типу авторизации оно относится
    private void checkLogin(String login) {
        if (login != null) {
            authorizationUser = new AuthorizationUserImpl();
            authorizationUser.registerGetAuthorizationTypeCallback(this);
            authorizationUser.getAuthorizationType(requireContext(), login);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ALLcontentLOGIN.getVisibility() == View.INVISIBLE) {
            ALLcontentLOGIN.setVisibility(View.VISIBLE);
            ATFlogo.setVisibility(View.INVISIBLE);
        }
        if (checkVersion()) {

            cbUseImprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbCode.setChecked(false);
                        cbSaveLogin.setChecked(true);
                        cbSaveLogin.setClickable(false);
                    } else {
                        cbSaveLogin.setClickable(true);
                    }
                }
            });
            cbCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbUseImprint.setChecked(false);
                        cbSaveLogin.setChecked(true);
                        cbSaveLogin.setClickable(false);
                    } else {
                        cbSaveLogin.setClickable(true);
                    }
                }
            });
        } else {
            cbCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbSaveLogin.setChecked(true);
                        cbSaveLogin.setClickable(false);
                    } else {
                        cbSaveLogin.setClickable(true);
                    }
                }
            });
        }
        isNeedSms = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isNeedSms) {
            edittextPassword.setText("");
            edittextLogin.getText().clear();
            edittextPassword.setCursorVisible(true);
            edittextLogin.setCursorVisible(true);
            button_login.setText(getString(R.string.enter));
            blockView.setVisibility(View.GONE);
            loadLogin();
            loadPassword();
            if (checkVersion()) {
                imprintAuthentication();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (mFingerprintUiHelper != null)
                mFingerprintUiHelper.stopListening();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cbSaveLogin.isChecked()) {
            saveLogin();
        } else if (!cbSaveLogin.isChecked() && !cbCode.isChecked()) {
            deleteLogin();
        }

        if (cbCode.isChecked() && password == null && edittextPassword.getText().length() != 0) {
            savePassword();
            saveLogin();
        } else if (!cbCode.isChecked()) {
            deletePassword();
        }
        if (alert != null) {
            alert.dismiss();
        }

        if (dhKeyController != null) {
            dhKeyController.close();
        }

        if (pushTokenKeyController != null) {
            pushTokenKeyController.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 99) {
            if (checkVersion())
                imprintAuthentication();
        } else if (requestCode == REQUEST_CODE_ENABLE) {
            sPref = getPreferences(requireContext());
            if (!sPref.getBoolean(CANCEL, true)) {
                entry();
            } else {
                edittextPassword.setText("");
            }
            if (sPref.getBoolean(ATTEMPTS, false)) {
                new AlertDialog.Builder(requireContext())
                        .setMessage(R.string.number_attempt)
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
            }
        }
    }

    @Override
    public void onAuthenticated() {
        loginUser(mSharedPreferences.getString(getString(R.string.login_key), ""), mSharedPreferences.getString(getString(R.string.password_key), ""));
    }

    @Override
    public void onError() {
        if (alert != null) {
            alert.dismiss();
        }
        new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.limit_touch_id))
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        }).create().show();
        mFingerprintUiHelper.stopListening();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(getString(R.string.login_key));
        editor.remove(getString(R.string.password_key));
        editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key), false);
        editor.apply();
        cbSaveLogin.setVisibility(View.VISIBLE);
        cbCode.setVisibility(View.VISIBLE);
        cbUseImprint.setVisibility(View.VISIBLE);
    }

    @Override
    public void jsonAuthorizationResponse(int statusCode, String errorMessage, int errorCode) {
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

        if (isAttached()) {
            blockView.setVisibility(View.GONE);
            if (statusCode == CONNECTION_ERROR_STATUS) {
                Log.d("TAG", "connection error");
            } else if (errorCode == -10006) {
                showInputSmsCodeDialog();
            } else if (errorCode == -10010) {
                finishRegistration();
                finishReg = true;
            } else if (statusCode == Constants.SUCCESS) {
                // после логина меняем состояние срочного сообщения
                OptimaBank.getContext().changeUrgentMessageState(false);
                sendPushToken();
                entryToApp();

                // после логина меняем состояние срочного сообщения
                OptimaBank.getContext().changeUrgentMessageState(false);

                GeneralManager.getInstance().setAlarmManagerOn(requireContext());
            } else if (statusCode == -100 || statusCode == -105) {
                userAuthorizationError(errorMessage);
            } else if (statusCode == -999) {
                showToast(getString(R.string.RESOURCE_NOT_FOUND));
            } else if (statusCode == -10001) {
                edittextPassword.setText("");
                showToast(getString(R.string.incorrect_sms_code_confirmation));
            }
            if (statusCode != 0 && statusCode != -10006) {
                button_login.setText(getString(R.string.enter));
            }
        }
    }

    @SuppressLint("InflateParams")
    @TargetApi(Build.VERSION_CODES.M)
    private void imprintAuthentication() {
        cbUseImprint.setVisibility(View.VISIBLE);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View imprint = inflater.inflate(R.layout.imprint, null);

        if (fingerprintAuthentication != null && fingerprintAuthentication.imprintAuthentication(cbUseImprint)) {
            mFingerprintUiHelper = fingerprintAuthentication.getFingerprintUiHelperBuilder()
                    .build(imprint.findViewById(R.id.fingerprint_icon),
                            imprint.findViewById(R.id.fingerprint_status), this);

            if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
                cbUseImprint.setClickable(false);
                return;
            }

            final AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
            TextView title = new TextView(requireContext());
            title.setTextColor(Color.BLACK);
            title.setTextSize(19);
            title.setGravity(Gravity.CENTER);
            title.setPadding(0, 30, 0, 0);
            title.setText(getString(R.string.title_dialog));
            adb.setCustomTitle(title);
            adb.setCancelable(false);
            adb.setView(imprint);
            adb.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFingerprintUiHelper.stopListening();
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.remove(getString(R.string.login_key));
                    editor.remove(getString(R.string.password_key));
                    editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key), false);
                    editor.apply();
                    cbSaveLogin.setVisibility(View.VISIBLE);
                    cbCode.setVisibility(View.VISIBLE);
                    cbUseImprint.setVisibility(View.VISIBLE);
                    cbUseImprint.setChecked(false);
                    dialog.dismiss();
                }
            });

            if (alert != null) {
                alert.dismiss();
            }
            alert = adb.create();
            alert.show();

            Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (b != null) {
                b.setTextColor(getResources().getColor(R.color.gray_atf_));
            }
            mFingerprintUiHelper.startListening(mCryptoObject);
        }
    }

    String getHash(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes("UTF-8"));
            String def = Base64.encodeToString(hash, Base64.DEFAULT);
            return def.replaceAll("\n", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    void loginUser(String login, String password) {
        button_login.setText(getString(R.string.loading));
        blockView.setVisibility(View.VISIBLE);
        authorizationUser = new AuthorizationUserImpl();
        authorizationUser.registerCallBack(this);
        authorizationUser.registerGetAuthorizationTypeCallback(this);
        authorizationUser.loginRequest(requireContext(), login.replaceAll(Constants.REG_EX_FOR_PHONE, ""), password, null);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void saveLogin() {
        SharedPreferences.Editor editor = getPreferences(requireContext()).edit();
        editor.putString(SAVED_LOGIN, edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, ""));
        editor.apply();
    }

    private void loadLogin() {
        sPref = getPreferences(requireContext());
        login = sPref.getString(SAVED_LOGIN, null);
        if (login != null && sPref.getBoolean(LOGIN, false)) {
            cbSaveLogin.setChecked(true);
            edittextLogin.setText(login);
        } else {
            cbSaveLogin.setChecked(false);
        }
    }

    public void deleteLogin() {
        editor = getPreferences(requireContext()).edit();
        editor.remove(SAVED_LOGIN).apply();
        cbSaveLogin.setChecked(false);
    }

    @SuppressLint("CommitPrefEdits")
    private void savePassword() {
        editor = getPreferences(requireContext()).edit();
        if (!getHash(edittextPassword.getText().toString()).equals(mSharedPreferences.getString(getString(R.string.password_key), "")) && !edittextPassword.getText().toString().equals("123456789")
                && !edittextPassword.getText().toString().isEmpty())
            editor.putString(SAVED_PASSWORD, getHash(String.valueOf(edittextPassword.getText())));
        editor.apply();
    }

    private void loadPassword() {
        password = getPreferences(requireContext()).getString(SAVED_PASSWORD, null);
        Log.i(TAG, "loadPassword  = " + password);
        if (password != null) {
            cbCode.setChecked(true);
            edittextPassword.setText("123456789");
            loginUser(edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, ""), password);
        } else {
            cbCode.setChecked(false);
            if (!cbCode.isChecked() && !cbSaveLogin.isChecked()) {
                edittextLogin.getText().clear();
            }
        }
    }

    private void deletePassword() {
        editor = getPreferences(requireContext()).edit();
        editor.remove(SAVED_PASSWORD);
        editor.apply();
        cbCode.setChecked(false);
        password = null;
    }

    void setCode() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        if (getPreferences(requireContext()).getBoolean(CODE, false)) {
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        } else {
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);

        }
        startActivityForResult(intent, REQUEST_CODE_ENABLE);
    }

    public boolean checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) requireContext().getSystemService(Context.FINGERPRINT_SERVICE);
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                if (fingerprintManager.isHardwareDetected()) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public void entry() {
        if (alert != null) {
            alert.dismiss();
        }
        if (cbUseImprint.getVisibility() == View.VISIBLE && cbUseImprint.isChecked()) {
            Log.i("if", "(cbUseImprint.isShown()&&cbUseImprint.isChecked())");
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key), true);
            editor.putString(getString(R.string.login_key), edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, ""));
            Log.i(TAG, "HASHPAASSWORD = " + edittextPassword.getText().toString());
            if (!getHash(edittextPassword.getText().toString()).equals(mSharedPreferences.getString(getString(R.string.password_key), "")) && !edittextPassword.getText().toString().equals("123456789")
                    && !edittextPassword.getText().toString().isEmpty())
                editor.putString(getString(R.string.password_key), getHash(edittextPassword.getText().toString()));
            editor.apply();
        }

        editor = getPreferences(requireContext()).edit();
        if (cbSaveLogin.isChecked()) {
            editor.putBoolean(LOGIN, true);
        } else {
            editor.putBoolean(LOGIN, false);
        }
        editor.apply();
        edittextPassword.clearFocus();
        edittextLogin.clearFocus();
        hideKeyboard();
        ALLcontentLOGIN.setVisibility(View.INVISIBLE);
        ATFlogo.setVisibility(View.VISIBLE);
        navigationActivity();
    }

    public void navigationActivity() {
        Intent intent = new Intent(parentActivity, MenuActivity.class);
        if (getReceivedNotificationData()) {
            intent.putExtra(IS_NOTIFICATION, true);
            intent.putExtra(NOTIFICATION_ARG_ID, notificationId);
        }
        startActivity(intent);
        requireActivity().finish();
    }

    public boolean getReceivedNotificationData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            notificationId = bundle.getString(NOTIFICATION_ARG_ID);
            return bundle.getBoolean(IS_NOTIFICATION, false);
        } else {
            return false;
        }
    }

    // запрос на Firebase получение токена пользователя, если успешно тот же токен отправляем на push сервис
    private void sendPushToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(PUSH_TAG, "Fetching FCM registration token failed ", task.getException());
                        return;
                    }
                    Log.d(PUSH_TAG, "firebase token = " + task.getResult());

                    String firebaseToken = task.getResult();

                    // при каждом входе в приложении отправляем токен, так как токен может был изменен
                    registerToken(firebaseToken);
                });
    }

    // отправляем токен на сервере далее сохраняем его в преференсах
    private void registerToken(String pushToken) {
        String bankId = getBankId();
        if (bankId != null) {
            model.getRegisterToken(bankId, pushToken)
                    .observe(getViewLifecycleOwner(), response ->
                            Log.d(PUSH_TAG, "success register token " + response.toString())
                    );
        } else {
            Log.d(PUSH_TAG, "Client bankId is null");
        }
    }

    // получение логина текущего пользователя
    private String getBankId() {
        SharedPreferences preferences = Utilities.getPreferences(requireContext());
        return preferences.getString(BANK_ID, "");
    }

    // заходим в приложение
    private void entryToApp() {
        if (!cbCode.isChecked()) {
            entry();
        } else {
            cbCode.isChecked();
            setCode();
        }

    }

    private void finishRegistration() {
        if (cbSaveLogin.isChecked()) {
            SharedPreferences.Editor editor = Utilities.getPreferences(parentActivity).edit();
            editor.putString(MLoginFragment.SAVED_LOGIN, edittextLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
            editor.putBoolean(MLoginFragment.LOGIN, true);
            editor.apply();
        }
        ChangePasswordForRegFragment fragment = ChangePasswordForRegFragment.newInstance(edittextLogin.getText().toString().replaceAll(REG_EX_FOR_PHONE, ""));
        parentActivity.replaceFragment(fragment, R.id.fragment_content, true);
    }

    private void userAuthorizationError(String errorMessage) {
        edittextPassword.setText("");
        edittextLogin.getText().clear();
        onError(errorMessage);
        //for code
        deleteLogin();
        deletePassword();
        LockManager mLockManager = new LockManager();
        mLockManager.disableAppLock();
        SharedPreferences.Editor editor1 = getPreferences(requireContext()).edit();
        editor1.putBoolean(CODE, false);
        editor1.apply();

        //for imprint
        if (mFingerprintUiHelper != null) {
            mFingerprintUiHelper.stopListening();
        }
        if (cbUseImprint.isShown()) {
            cbUseImprint.setChecked(false);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.remove(getString(R.string.login_key));
            editor.remove(getString(R.string.password_key));
            editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key), false);
            editor.apply();
            if (alert != null) {
                alert.dismiss();
            }
        }
    }

    //status code equals 403
    private void smsEntry() {
        button_login.setText(getString(R.string.enter));
        isNeedSms = true;
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(SMS_CODE_LENGTH)});
        final AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.text_sms_code))
                .setView(input)
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_info)
                .setTitle(R.string.alert_info)
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().length() != SMS_CODE_LENGTH) {
                    input.setError(getString(R.string.text_sms));
                } else {

                }
            }
        });
    }

    private void showInputSmsCodeDialog() {
        button_login.setText(getString(R.string.enter));
        isNeedSms = true;
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewAlertDialog = inflater.inflate(R.layout.dialog_input_sms_code, null);
        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setView(viewAlertDialog).create();
        EditText inputCode = viewAlertDialog.findViewById(R.id.input_sms_code);
        Button btnSend = viewAlertDialog.findViewById(R.id.btn_send_sms_code);
        Button btnCancel = viewAlertDialog.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.VISIBLE);

        inputCode.requestFocus();


        btnCancel.setOnClickListener(view -> {
            if (alertDialog != null) {
                alertDialog.dismiss();
                edittextPassword.setText("");
            }
        });

        btnSend.setText(getActivity().getResources().getString(R.string.status_ok));
        btnSend.setOnClickListener(button -> {
            String smsCode = inputCode.getText().toString();
            if (!smsCode.isEmpty() && smsCode.length() == SMS_CODE_LENGTH) {
                hideKeyboard();
                if (authorizationUser == null) {
                    authorizationUser = new AuthorizationUserImpl();
                }
                authorizationUser.loginRequest(requireContext(), edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, ""),
                        getHash(edittextPassword.getText().toString()),
                        inputCode.getText().toString());
                alertDialog.dismiss();
            } else {
                Snackbar.make(viewAlertDialog, requireContext().getString(R.string.text_sms), Snackbar.LENGTH_SHORT).show();
            }
        });

        Window window = alertDialog.getWindow();
        if (window != null) window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (edittextPassword.getText().toString().equals(s.toString())) {
            if (s.toString().isEmpty()) {
                clearPass.setVisibility(View.GONE);
            } else {
                clearPass.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void jsonCheckPasswordResponse(int statusCode, String errorMessage, String responseType) {
        if (statusCode == SUCCESS) {
            if (responseType.equals(AUTHORIZATION)) {
                // переход на авторизацию
                String login = edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, "");
                loginUser(login, getHash(String.valueOf(edittextPassword.getText())));
            } else if (responseType.equals(REGISTRATION)) {
                // переход на регистрацию
                String login = edittextLogin.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, "");
                Intent intent = new Intent(requireActivity(), OldRegistrationActivity.class);
                intent.putExtra(REGISTRATION_LOGIN, login);
                intent.putExtra(REGISTRATION_CODE, edittextPassword.getText().toString());
                startActivity(intent);
            }
        } else {
            showToast(getString(R.string.internet_connection_error));
            Log.d(REGISTRATION_TAG, "Error = " + errorMessage);
        }
    }
}