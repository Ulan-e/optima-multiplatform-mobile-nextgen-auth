package kz.optimabank.optima24.fragment.authorization;

import static kz.optimabank.optima24.utility.Utilities.getPreferences;
import static kz.optimabank.optima24.utility.Utilities.isDeviceScreenLocked;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;

public class ContactlessLoginFragment extends MLoginFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bt_register.setVisibility(View.GONE);
        if (!isDeviceScreenLocked(getActivity())) {
            showScreenLockedSettingAlert();
        }

        if (checkVersion()) {
            cbUseImprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbCode.setChecked(false);
                    } else {
                        cbCode.setChecked(true);
                    }
                }
            });
            cbCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbUseImprint.setChecked(false);
                    } else {
                        cbUseImprint.setChecked(true);
                        }
                }
            });
        } else {
            cbCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cbCode.setChecked(true);
                }
            });
        }
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!cbCode.isChecked()) {
            cbCode.setChecked(true);
        }
        cbSaveLogin.setClickable(false);
        cbSaveLogin.setChecked(true);
    }

    @Override
    public void navigationActivity() {
        Intent intent = new Intent(parentActivity, NavigationActivity.class);
        intent.putExtra("is_contactless_login", true);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void deleteLogin() {
        editor = getPreferences(getActivity()).edit();
        editor.remove(SAVED_LOGIN).apply();
    }

    private void showScreenLockedSettingAlert() {
        android.app.AlertDialog.Builder alertBox = new android.app.AlertDialog.Builder(getActivity());
        alertBox.setTitle(getActivity().getResources().getString(R.string.alert_info));
        alertBox.setCancelable(false);
        alertBox.setMessage(getActivity().getResources().getString(R.string.screen_locked_setting_info));
        alertBox.setPositiveButton(getActivity().getResources().getString(R.string.status_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        alertBox.show();
    }
}
