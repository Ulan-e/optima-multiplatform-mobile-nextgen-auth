package kz.optimabank.optima24.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.AccountDetailsActivity;
import kz.optimabank.optima24.controller.adapter.AccountListAdapter;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountsImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

public class ListFragment extends ATFFragment implements AccountsImpl.Callback{
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;

    ArrayList<UserAccounts.Cards> cards;
    ArrayList<UserAccounts.CheckingAccounts> checkingAccounts;
    ArrayList<UserAccounts.DepositAccounts> depositAccounts;
    ArrayList<UserAccounts.WishAccounts> wishAccounts;
    ArrayList<UserAccounts.CreditAccounts> creditAccounts;

    ArrayList<UserAccounts> accounts = new ArrayList<>();
    Accounts accountsInterface;
    String account = "";
    boolean isClickableRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getBundle();
        initToolbar();
        setAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setAdapter() {
        if (GeneralManager.getInstance().getSessionId() != null) {
            accountsInterface = new AccountsImpl();
            accountsInterface.registerCallBack(this);

            if(GeneralManager.getInstance().getAllFullAccounts().isEmpty()) {
                accountsInterface.getAccounts(getActivity(), true,false);
            } else {
                if (account.equals("Cards")){
                    accounts.clear();
                    accounts.addAll(GeneralManager.getInstance().getVisibleCards());
                } else if (account.equals("CheckingAccounts")){
                    accounts.addAll(GeneralManager.getInstance().getVisibleCheckingAccounts());
                }else if (account.equals("DepositAccounts")){
                    accounts.addAll(GeneralManager.getInstance().getVisibleDeposits());
                }else if (account.equals("WishAccounts")){
                    accounts.addAll(GeneralManager.getInstance().getVisibleWishDeposits());
                }else
                if (account.equals("CreditAccounts")){
                    accounts.addAll(GeneralManager.getInstance().getVisibleCredit());
                }else
                if (account.equals("CardAccounts")){
                    accounts.addAll(GeneralManager.getInstance().getVisibleCardAccounts());
                }
                AccountListAdapter adapter = new AccountListAdapter(getActivity(), accounts, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                if (accounts != null && !accounts.isEmpty() && isClickableRecyclerView) {
                                    isClickableRecyclerView = false;
                                    UserAccounts userAccounts = accounts.get(position);
                                    if (userAccounts.code != Constants.HEADER_ID) {
                                        startDetailsActivity(userAccounts);
                                    }
                                }
                            }

                            @Override
                            public void onAnimationCancel(View view) {
                            }
                        };
                        clickAnimation(view, animatorListener);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }

    }

    private void startDetailsActivity(UserAccounts account) {
        Intent intent = new Intent(parentActivity, AccountDetailsActivity.class);
        intent.putExtra(AccountDetailsActivity.ACCOUNT, account);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        isClickableRecyclerView = true;
        if (GeneralManager.getInstance().isNeedToUpdateAccounts()) {
            setAdapter();
            GeneralManager.getInstance().setNeedToUpdateAccounts(false);
        }

        if (account.equals("Cards")){
            if (GeneralManager.getInstance().isNeedToUpdateAfterDefaultStatusCard()) {
                accountsInterface.getAccounts(getActivity(), true, true);
                setAdapter();
            }
        }
    }

    private void initToolbar() {
        //((NavigationActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        if (account.equals("Cards")){
            tvTitle.setText(R.string.card_accounts);}
        else
        if (account.equals("CheckingAccounts")){
            tvTitle.setText(R.string.current_accounts_tek);}
        else
        if (account.equals("DepositAccounts")){
            tvTitle.setText(R.string.deposit_accounts);}
        else
        if (account.equals("WishAccounts")){
            tvTitle.setText(R.string.wish);}
        else
        if (account.equals("CreditAccounts")){
            tvTitle.setText(R.string.loan_accounts);}
        else
        if (account.equals("CardAccounts")){
            tvTitle.setText(R.string.current_cards_accounts);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle(){
        if (getArguments()!=null){
            Log.i("UserAccounts","getBundle = " + getArguments().getString("UserAccounts"));
            account = getArguments().getString("UserAccounts");
        }
    }

    @Override
    public void jsonAccountsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            setAdapter();
        } else {
            if (errorMessage != null) {
                onError(errorMessage);
            }
        }
    }

    @Override
    public void jsonAccountsOperationsResponse(int statusCode, String errorMessage) {
        /*if(statusCode==200) {
            final ArrayList<ATFStatement> statements = GeneralManager.getInstance().getStatements();
            if(!statements.isEmpty()) {
                new AsyncTask<Void, Void, ArrayList<Entry>>() {
                    @Override
                    protected ArrayList<Entry> doInBackground(Void... voids) {
                        Collections.sort(statements, new Comparator<ATFStatement>() {
                            @Override
                            public int compare(ATFStatement o1, ATFStatement o2) {
                                return o1.operationDate.compareTo(o2.operationDate);
                            }
                        });
                        values = new ArrayList<>();
                        float amount = 0;
                        for(int i = 0; i < statements.size(); i++) {
                            if(i+1!=statements.size()) {
                                if (!statements.get(i).getOperationDate().equals(statements.get(i + 1).getOperationDate())) {
                                    amount += statements.get(i).baseAmount;
//                                    Log.d(TAG, "statement.getOperationDate() = " + statements.get(i).getOperationDate() + " amount = " + amount);
                                    values.add(new Entry(i, amount));
                                    amount = 0;
                                } else {
                                    amount += statements.get(i).baseAmount;
                                }
                            } else {
//                                Log.d(TAG, "statement.getOperationDate() = " + statements.get(i).getOperationDate() + " amount = " + amount);
                                values.add(new Entry(i, amount));
                            }
                        }
                        return values;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<Entry> list) {
                        super.onPostExecute(list);
                        if (isAdded()) {
                            try {
                                initChart(list);
                                Log.i("list","list = " + list);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.execute();
            } else {
                if(isAdded()) {
                    mChart.invalidate();
                    mChart.setNoDataText(getString(R.string.not_data));
                }
            }
        } else {
            if(errorMessage!=null) {
                onError(errorMessage);
            }
        }*/
    }
}
