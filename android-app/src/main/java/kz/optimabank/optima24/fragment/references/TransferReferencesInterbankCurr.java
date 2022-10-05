package kz.optimabank.optima24.fragment.references;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kz.optimabank.optima24.fragment.transfer.TransferInterbankCurrency;
import kz.optimabank.optima24.model.gson.response.UserAccounts;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;

/**
  Created by Timur on 17.06.2017.
 */

public class TransferReferencesInterbankCurr extends TransferInterbankCurrency {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getBundle();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAccountSpinnerFrom(accountFrom);
        currency = accountFrom.currency;
//        tvCurrency.setText(Utilities.getCurrencyBadge(getContext(), accountFrom.currency));
        tvCurrency.setText(accountFrom.currency);
    }

    private void getBundle() {
        if(getArguments()!=null) {
            accountFrom = (UserAccounts) getArguments().getSerializable(ACCOUNT);
        }
    }
}
