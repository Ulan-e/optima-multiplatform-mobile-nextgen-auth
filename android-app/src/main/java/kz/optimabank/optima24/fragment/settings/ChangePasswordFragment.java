package kz.optimabank.optima24.fragment.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.interfaces.ChangePassword;
import kz.optimabank.optima24.model.service.ChangePasswordImpl;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 11.07.2017.
 */

public class ChangePasswordFragment extends ATFFragment implements TextWatcher, View.OnClickListener ,ChangePasswordImpl.Callback {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.edCurrentPassword) EditText edCurrentPassword;
    @BindView(R.id.edNewPassword) EditText edNewPassword;
    @BindView(R.id.edReNewPassword) EditText edReNewPassword;

    @BindView(R.id.btnSaveNewPassword) Button btnSaveNewPassword;

    ChangePassword changePassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        initToolbar();

        edCurrentPassword.addTextChangedListener(this);
        edNewPassword.addTextChangedListener(this);
        edReNewPassword.addTextChangedListener(this);

        btnSaveNewPassword.setOnClickListener(this);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edCurrentPassword.getEditableText()) {
            if (edCurrentPassword.getText().toString().length() > 1) {
                edCurrentPassword.setError(null);
            }
        } else if (editable == edNewPassword.getEditableText()) {
            if (edNewPassword.getText().toString().length() > 1) {
                edNewPassword.setError(null);
            }
        } else if (editable == edReNewPassword.getEditableText()) {
            if (edReNewPassword.getText().toString().length() > 1) {
                edReNewPassword.setError(null);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSaveNewPassword) {
            String curPass = edCurrentPassword.getText().toString();
            String newPass = edNewPassword.getText().toString();
            String rePass = edReNewPassword.getText().toString();

            if(!isOkPassword(curPass)) {
                edCurrentPassword.setError(getString(R.string.error_wrong_format));
                edCurrentPassword.requestFocus();
                return;
            } else if (!isOkPassword(newPass)) {
                edNewPassword.setError(getString(R.string.error_wrong_format));
                edNewPassword.requestFocus();
                return;
            } else if(!isOkPassword(rePass)) {
                edReNewPassword.setError(getString(R.string.error_wrong_format));
                edReNewPassword.requestFocus();
                return;
            } else if(!newPass.equals(rePass)) {
                edReNewPassword.setError(getString(R.string.not_equals_password));
                return;
            }
            Log.d(TAG,"OK");
            changePassword = new ChangePasswordImpl();
            changePassword.registerCallBack(this);
            JSONObject body = new JSONObject();
            try {
                body.put("currentPassword",getSHA256(curPass));
                body.put("newPassword",getSHA256(newPass));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            changePassword.ChangePassword(getActivity(),body);
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

    public void initToolbar() {
        toolbar.setTitle("");
        ((SettingsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((SettingsActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
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
    public void jsonChangePasswordResponse(int statusCode, String errorMessage) {
        Log.i("statusCode-changePass","statusCode= "+statusCode);
        if(statusCode==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setMessage(R.string.operation_success);
            builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else if(statusCode!=CONNECTION_ERROR_STATUS){
            onError(errorMessage);
        }
    }
}
