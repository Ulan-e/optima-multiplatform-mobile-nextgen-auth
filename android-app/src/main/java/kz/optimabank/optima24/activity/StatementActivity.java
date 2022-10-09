package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.statements.StatementContainer;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class StatementActivity extends OptimaActivity {
    ATFFragment fragment;
    UserAccounts userAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_detail_activity);

        Intent intent = getIntent();
        userAccounts = (UserAccounts) intent.getSerializableExtra(ACCOUNT);
        fragment = new StatementContainer();
        fragment.setArguments(setBundle());
        navigateToPage(fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GeneralManager.getInstance().getSessionId() == null) {
            finish();
        }
    }

    public void navigateToPage(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        ft.commit();
    }

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACCOUNT,userAccounts);
        return bundle;
    }
}
