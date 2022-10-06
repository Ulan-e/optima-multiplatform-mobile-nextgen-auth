package kz.optimabank.optima24.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.controllers.BannerController;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.LocaleUtils;

/**
 * Created by Максим on 04.08.2017.
 */

public class ChangeAppLangActivity extends OptimaActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rg_lang)
    RadioGroup rgLang;
    BannerController bannerController = BannerController.getController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.language_list, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        initToolbar();
        prepareLangGroup();
        super.onCreate(savedInstanceState);
    }

    private void prepareLangGroup() {
        String language = LocaleUtils.getLanguage(this).toUpperCase();
        Log.e("Lang", language);
        switch (language) {
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

                BannerController.getController().setContext(OptimaApp.Companion.getInstance());
                BannerController.getController().clearCache();
            }
        });
    }

    private void langChangeAlert(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.t_app_restart));
        builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setLocale(String lang) {
        if (LocaleUtils.setLocale(this, lang)) {
            String loc = lang;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            Constants.resetVIEW_DATE_FORMAT();
        }
    }
}
