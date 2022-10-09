package kz.optimabank.optima24.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.SelectAccountAdapter;
import kz.optimabank.optima24.controller.adapter.SelectCurrencyAdapter;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.LocaleUtils;

import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.IS_TRANSFER_ELCARD_TO_ELCARD;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;

/**
  Created by Timur on 02.05.2017.
 */

public class SelectAccountActivity extends ListActivity implements TextWatcher {
    @BindView(R.id.imgWindowsClose) ImageView imgWindowsClose;
    @BindView(R.id.lin_search) LinearLayout lin_search;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.no_data_view) TextView no_data_view;

    private static final String TRANSFER_VISA_TO_VISA = "transferVisaToVisa";

    boolean isCards, isAccountsForDebitWithoutCardAcc, debitAccounts, transferAccFrom,isChecking_Card_Deposit_Accounts, isCurrency, isCheckOnlyKZT,isCheckOnlyNotKZT,
            isCitizenship, refillDeposit,onlyDeposit,onlyWish,isLimit,customList,isCheckOnly,isCheckChOnlyNotKZT,searchBool, isCardsAndChekingAndDepositAccounts,
    transferType, chooseLimit, isChekingAndDepositAccounts, isElcart, exceptElcart,depVos;
    String Curr;
    UserAccounts accountFrom;
    ArrayList<UserAccounts> accounts;
    ArrayList<String> stringList;
    ArrayList<String> temp = new ArrayList<>();
    SelectCurrencyAdapter adapter;

    private boolean transferVisaToVisa;
    private boolean isTransferElcardToElcard;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleUtils.updateResources(newBase, LocaleUtils.getLanguage(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_account_activity);
        ButterKnife.bind(this);
        accountFrom = (UserAccounts) getIntent().getSerializableExtra("accountFrom");
        depVos = getIntent().getBooleanExtra("depVos", false);
        isCards = getIntent().getBooleanExtra("isCards", false);
        isAccountsForDebitWithoutCardAcc = getIntent().getBooleanExtra("isAccountsForDebitWithoutCardAcc", false);
        isCardsAndChekingAndDepositAccounts = getIntent().getBooleanExtra("isCardsAndChekingAndDepositAccounts", false);
        debitAccounts = getIntent().getBooleanExtra("debitAccounts", false);
        transferAccFrom = getIntent().getBooleanExtra("transferAccFrom", false);
        transferVisaToVisa = getIntent().getBooleanExtra(TRANSFER_VISA_TO_VISA, false);
        isTransferElcardToElcard = getIntent().getBooleanExtra(IS_TRANSFER_ELCARD_TO_ELCARD, false);
        customList = getIntent().getBooleanExtra("customListBool",false);
        isCurrency = getIntent().getBooleanExtra("Currency",false);
        searchBool = getIntent().getBooleanExtra("searchBool",false);
        isLimit = getIntent().getBooleanExtra("isLimit",false);
        chooseLimit = getIntent().getBooleanExtra("chooseLimit",false);
        isCheckOnlyKZT = getIntent().getBooleanExtra("checkOnlyKZT",false);
        isCheckOnly = getIntent().getBooleanExtra("checkOnly",false);
        isCheckOnlyNotKZT = getIntent().getBooleanExtra("checkOnlyNotKZT",false);
        isCheckChOnlyNotKZT = getIntent().getBooleanExtra("checkChOnlyNotKZT",false);
        isChecking_Card_Deposit_Accounts = getIntent().getBooleanExtra("checking_Card_Deposit_Accounts",false);
        isCitizenship = getIntent().getBooleanExtra("citizenship",false);
        refillDeposit = getIntent().getBooleanExtra("refillDeposit",false);
        Curr = getIntent().getStringExtra("tvCurrency");
        Log.i("tvCurrency","tvCurrency = "+Curr);
        onlyDeposit = getIntent().getBooleanExtra("onlyDeposit",false);
        onlyWish = getIntent().getBooleanExtra("refillWish",false);
        transferType = getIntent().getBooleanExtra("isTransferType",false);
        isChekingAndDepositAccounts = getIntent().getBooleanExtra("isChekingAndDepositAccounts",false);
        isElcart = getIntent().getBooleanExtra("isElcart",false);
        exceptElcart = getIntent().getBooleanExtra("exceptElcart",false);
        accounts = new ArrayList<>();
        if (searchBool){
            lin_search.setVisibility(View.VISIBLE);
            edSearch.addTextChangedListener(this);
        }

        if(accountFrom!=null) {
            accounts.add(new UserAccounts(getResources().getString(R.string.introduce_new), Constants.NEW_CARD_ID));
        }

        if(isCards) {
            accounts.addAll(GeneralManager.getInstance().getCardsForPayment(this));
        } else if(debitAccounts) {
            accounts.addAll(GeneralManager.getInstance().getAccountsForDebit(this));
        } else if(transferAccFrom) {
            if(exceptElcart){
                accounts.addAll(GeneralManager.getInstance().getAccountsForTransfer(this, true, true));
            } else
                accounts.addAll(GeneralManager.getInstance().getAccountsForTransfer(this, true, false));
        } else if(isChecking_Card_Deposit_Accounts) {
            accounts.addAll(GeneralManager.getInstance().getChecking_Card_Deposits_Accounts(this));
        } else if (isCardsAndChekingAndDepositAccounts){
            if(exceptElcart)
                accounts.addAll(GeneralManager.getInstance().getCardsAndChekingAndDepositAccounts(this, true));
            else
                accounts.addAll(GeneralManager.getInstance().getCardsAndChekingAndDepositAccounts(this, false));
        } else if(isAccountsForDebitWithoutCardAcc) {
            if(exceptElcart)
                accounts.addAll(GeneralManager.getInstance().getAccountsForDebitWithoutCardAcc(this, true));
            else
                accounts.addAll(GeneralManager.getInstance().getAccountsForDebitWithoutCardAcc(this, false));
        } else if(isChekingAndDepositAccounts) {
            accounts.addAll(GeneralManager.getInstance().getCheckingAndDepositAccount(this));
        } else if(isElcart) {
            accounts.addAll(GeneralManager.getInstance().getCardsForCredit(this));
        } else if(isCurrency) {
            stringList = getIntent().getStringArrayListExtra("listCurrency");
            addHeader(getString(R.string.rate));
        } else if (customList){
            stringList = getIntent().getStringArrayListExtra("customList");
            Log.i("customList","stringList = "+ stringList.size());
            addHeader(getIntent().getStringExtra("header"));
        } else if(isCitizenship) {
            if (isLimit){
                addHeader(getString(R.string.region_or_type_operation));
            }
            else {
                addHeader(getString(R.string.citizenship));
            }
            stringList = getIntent().getStringArrayListExtra("citizenshipList");
        } else if(isCheckOnlyKZT) {
            accounts.addAll(GeneralManager.getInstance().getCheckingAndCardAccountKZT(this));
        } else if(isCheckChOnlyNotKZT) {
            accounts.addAll(GeneralManager.getInstance().getVisibleCheckingAccountsNotKZT(this));
        } else if(isCheckOnly) {
            accounts.addAll(GeneralManager.getInstance().getVisibleCheckingAccounts());
        } else if(isCheckOnlyNotKZT) {
            accounts.addAll(GeneralManager.getInstance().getCheckingAndCardAccountNotKZT(this));
        } else if(refillDeposit) {
            if(exceptElcart){
                accounts.addAll(GeneralManager.getInstance().getAccountsForTransfer(this, false, true));
            } else
                accounts.addAll(GeneralManager.getInstance().getAccountsForTransfer(this, false, false));
        } else if(onlyDeposit) {
            accounts.addAll(GeneralManager.getInstance().getWorkingDeposits());
        } else if(onlyWish) {
            accounts.addAll(GeneralManager.getInstance().getWorkingWishDeposits());
        } else if (transferType){
            addHeader(getString(R.string.text_transfer_type));
            stringList = getIntent().getStringArrayListExtra("transferTypeList");
        } else if (chooseLimit){
            addHeader(getString(R.string.select_limit_type));
            stringList = getIntent().getStringArrayListExtra("limitTypeList");
        }else if(transferVisaToVisa){
            accounts.addAll(GeneralManager.getInstance().getVisaCards(this));
        }else if(isTransferElcardToElcard){
            accounts.addAll(GeneralManager.getInstance().getElcardCards(this));
        }

        if(!isCurrency&&!isCitizenship&&!customList&&!transferType&&!chooseLimit) {
            SelectAccountAdapter adapter = new SelectAccountAdapter(this, accounts);
            setListAdapter(adapter);

                adapter.setDepVos(depVos);
            if (accountFrom != null) {
                try {
                    adapter.setAcc(((UserAccounts.Cards) accountFrom).brand);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
                removeSelectedAccount();
            }
        } else {
            SelectCurrencyAdapter adapter = new SelectCurrencyAdapter(this, stringList);
            setListAdapter(adapter);
        }


        imgWindowsClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent();

        if(!isCurrency&&!isCitizenship&&!customList&&!transferType&&!chooseLimit) {
            UserAccounts account = (UserAccounts) l.getItemAtPosition(position);
            intent.putExtra(ACCOUNT_KEY, account);
            intent.putExtra("tvCurrency", Curr);
            Log.i("tvCurrency","tvCurrencyonListItemClick = "+Curr);
        } else {
            String string = (String) l.getItemAtPosition(position);
            intent.putExtra(STRING_KEY, string);
            intent.putExtra("tvCurrency", Curr);
            Log.i("tvCurrency","tvCurrencyonListItemClick = "+Curr);
        }

        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }

    private void removeSelectedAccount() {
        Iterator<UserAccounts> it = accounts.iterator();
        while (it.hasNext()) {
            UserAccounts account = it.next();
            if (account.code == accountFrom.code) {
                it.remove();
            }
        }
    }

    private void addHeader(String headerText) {
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.account_header, lv, false);
        TextView textView = (TextView) header.findViewById(R.id.tv_header);
        textView.setText(headerText);
        lv.addHeaderView(header, null, false);
    }

    @OnClick(R.id.clearSearch)
    public void onClearSeaarchClick() {
        edSearch.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        filter(s.toString());
    }

    void filter(String text) {
        Log.i("text","text = "+text);
        temp = new ArrayList<>();
        for(String d : stringList) {
            if(d.toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        if (text.isEmpty()||text.equals(" ")){
            temp.clear();
            temp = stringList;
        }
        Log.i("temp","temp.size.after = "+temp.size());
        if (temp!=null && temp.isEmpty()){
            adapter = new SelectCurrencyAdapter(this, temp);
        }
        if (temp!=null && temp.size() > 0) {
            adapter = new SelectCurrencyAdapter(this, temp);
        }
        setListAdapter(adapter);
    }


}
