package kz.optimabank.optima24.fragment.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.controller.adapter.AccountVisibilityAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.interfaces.AccountsVisibility;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountVisibilityImpl;
import kz.optimabank.optima24.model.service.AccountsImpl;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 05.07.2017.
 */

public class AccountVisibilityFragment extends ATFFragment implements AccountVisibilityImpl.Callback, AccountsImpl.UpdateCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    ArrayList<UserAccounts> accountsList;
    ArrayList<UserAccounts.Cards> cardList;
    ArrayList<UserAccounts> accountsListItog = new ArrayList<>();
    AccountsVisibility accountsVisibility;
    AccountVisibilityAdapter adapter;
    private ArrayList<UserAccounts> dataChange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_visibility, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();
        accountsVisibility = new AccountVisibilityImpl();
        accountsVisibility.registerCallBack(this);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataChange = adapter.getDataChange();
                for (UserAccounts changedAccounts : dataChange) {
                    for (UserAccounts allAccounts : accountsListItog) {
                        if (changedAccounts instanceof UserAccounts.Cards
                                && allAccounts instanceof UserAccounts.CardAccounts
                                && ((UserAccounts.Cards) changedAccounts).cardAccountCode == allAccounts.code
                                && changedAccounts.isVisibility()
                                && !allAccounts.isVisibility()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setCancelable(true);
                            builder.setMessage(R.string.make_card_account_visible_first);
                            AlertDialog alert = builder.create();
                            alert.show();
                            return;
                        }
                    }
                }
                Log.i("adapter.getDataChange()",""+dataChange);
                if(!dataChange.isEmpty()) {
                    accountsVisibility.setVisibilityAccounts(getActivity(), getInvoiceBodyParameters(adapter.getDataChange()));
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(R.string.youNothingChoose);
                    builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        return view;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.done_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }*/



    public JSONArray getInvoiceBodyParameters(ArrayList<UserAccounts> accounts) {
        if (accounts==null){
            return null;
        }
        JSONArray bodyList = new JSONArray();
        for(UserAccounts item : accounts) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Code", item.getCode());
                jsonObject.put("Name", item.getName());
                jsonObject.put("Visible", item.isVisibility());
                bodyList.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bodyList;
    }

    private void setAdapter() {
        accountsList =  GeneralManager.getInstance().getAllAccounts(getActivity());
        cardList = GeneralManager.getInstance().getAllCards();

        for(UserAccounts account : accountsList) {
            account.setVisibility(account.isVisible);
        }

        for (UserAccounts acc:accountsList){
            if (acc != null) {
                try{
                    accountsListItog.add(acc);
                }catch (Exception ignored){}
                if (acc instanceof UserAccounts.CardAccounts) {
                    int code = acc.code;
                    for (UserAccounts.Cards accCard : cardList) {
                        if (accCard != null) {
                            if (accCard.cardAccountCode == code) {
                                accountsListItog.add(accCard);
                                accCard.setVisibility(accCard.isVisible);
                            }
                        }
                    }
                }
            }
        }

        adapter = new AccountVisibilityAdapter(getActivity(), accountsListItog);
        recyclerView.setAdapter(adapter);
        for (UserAccounts item : accountsList){
            Log.i("accountsList","accounts_befor_query= " + item.isVisibility());
        }
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

    @Override
    public void jsonAccountsVisibilityResponse(int statusCode, String errorMessage) {
        Log.i("AccountsVisibility","statusCode= " + statusCode);
        if(statusCode==0){
            Accounts accounts = new AccountsImpl();
            accounts.registerUpdateCallBack(this);
            Log.i("QUERY","UPDATE ACC");
            accounts.getAccounts(getActivity(), true, false);
        }else if(statusCode!=CONNECTION_ERROR_STATUS){
            onError(errorMessage);
        }
    }

    @Override
    public void jsonAccountsResponse(int statusCode, String errorMessage) {
        Log.i("AccountsVisibility","statusCode= " + statusCode);
        if(statusCode==0) {
            if (isAttached()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setMessage(R.string.operation_success);
                builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFragmentManager().popBackStack();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                GeneralManager.getInstance().setNeedToUpdateAccounts(true);
            } else if (statusCode != CONNECTION_ERROR_STATUS) {
                onError(errorMessage);
            }
        }
    }
}
