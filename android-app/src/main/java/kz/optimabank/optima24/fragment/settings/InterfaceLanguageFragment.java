package kz.optimabank.optima24.fragment.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.db.controllers.BannerController;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.LocaleUtils;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.fragment.ATFFragment;

/**
 * Created by Максим on 04.08.2017.
 */

public class InterfaceLanguageFragment extends ATFFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rg_lang)
    RadioGroup rgLang;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language_list, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        prepareLangGroup();
        return view;
    }

    private void prepareLangGroup() {
        switch (LocaleUtils.getLanguage(getActivity()).toUpperCase()) {
            case "KY":
                rgLang.check(R.id.rb_kz);
                break;
            case "RU":
                rgLang.check(R.id.rb_ru);
                break;
            case "EN":
                rgLang.check(R.id.rb_en);
                break;
        }
        rgLang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_kz:
                        langChangeAlert("KY");
                        break;
                    case R.id.rb_ru:
                        langChangeAlert("RU");
                        break;
                    case R.id.rb_en:
                        langChangeAlert("EN");
                        break;
                }

                BannerController.getController().setContext(requireContext());
                BannerController.getController().clearCache();
            }
        });
    }

    private void langChangeAlert(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.t_app_restart));
        builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        /*preferences = PreferenceManager.getDefaultSharedPreferences(OptimaBank.getContext());
                        editor = preferences.edit();
                        editor.putString(Constants.APP_LANGUAGE, str);
                        editor.apply();
                        dialog.cancel();
                        startActivity(new Intent(getActivity(), UnauthorizedTabActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();*/
                setLocale(str);
            }
        });
        builder.setNegativeButton(getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        tvTitle.setText(getResources().getString(R.string.interfaceLang));
        ((SettingsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((SettingsActivity) getActivity()).getSupportActionBar();
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

    public void setLocale(String lang) {
        if (LocaleUtils.setLocale(getActivity(), lang)) {
            String loc = lang;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (lang.toLowerCase()) {
                case "ky":
                    loc = "ky-KG";
                    break;
                case "ru":
                    loc = "ru-RU";
                    break;
                case "en":
                    loc = "en-US";
                    break;
            }
            editor.putString(Constants.APP_LANGUAGE, loc);
            editor.apply();
            GeneralManager.setLocaleChanged(true);
            Intent i = parentActivity.getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(parentActivity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            Constants.resetVIEW_DATE_FORMAT();
//            System.exit(0);
        }
    }
}
