package kz.optimabank.optima24.fragment.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.AccountDetailsActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.StatementActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.controller.adapter.StatementAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.StatementsWithStats;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.AccountOperation;
import kz.optimabank.optima24.model.interfaces.AccountsVisibility;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountOperationImpl;
import kz.optimabank.optima24.model.service.AccountVisibilityImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.doubleFormatter;
import static kz.optimabank.optima24.utility.Utilities.formatDate;

/**
 * Created by Timur on 31.05.2017.
 */

public class DepositDetailsFragment extends ATFFragment implements AccountOperationImpl.Callback, SwipeRefreshLayout.OnRefreshListener
        , AccountVisibilityImpl.Callback {
    //account info
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvAccountNumber)
    TextView tvAccountNumber;
    @BindView(R.id.tvDateAccountValidity)
    TextView tvDateAccountValidity;
    @BindView(R.id.tvDateAccountRemainder)
    TextView tvDateAccountRemainder;
    @BindView(R.id.tvOpenDate)
    TextView tvOpenDate;
    @BindView(R.id.tvDateAccountCapitalization)
    TextView tvDateAccountCapitalization;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ntb_horizontal)
    NavigationTabBar navigationTabBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    AccountOperation accountOperation;
    UserAccounts.DepositAccounts depositAccounts;
    ArrayList<ATFStatement> data = new ArrayList<>();
    String dateFrom, dateTo;
    String newName;
    AccountsVisibility accountsVisibility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deposit_details_fragment, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();

        setTollBarScrollFlags(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));

        navigationTabBar.setModels(getModels());
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                Intent intent = null;
                if (model.getTitle().equals(getString(R.string.transfer_title))) {
                    if (!depositAccounts.isClosed) {                                   //if (!depositAccounts.isClosed) {
                        intent = new Intent(getActivity(), TransfersActivity.class);
                        intent.putExtra("isReferences", true);
                        intent.putExtra("acToO", true);             // Добавлен
                    } else {
                        onError(getString(R.string.closed_deposit));
                    }
                } else if (!depositAccounts.isClosed && model.getTitle().equals(getString(R.string.replenish))) { // с R.string.transfer_title на R.string.replenish
                    intent = new Intent(getActivity(), TransfersActivity.class);
                    intent.putExtra("isReferences", true);
//                    intent.putExtra("acToO", true);                     //Убран
                    intent.putExtra("Replenish", true);    // Добавлен
                    Log.e(TAG, "onEndTabSelected: isClosed");
                } else if (model.getTitle().equals(getString(R.string.account_statement))) {// index == 2
                    intent = new Intent(getActivity(), StatementActivity.class);
                    Log.e(TAG, "onEndTabSelected: statement");
                } else if (model.getTitle().equals(getString(R.string.branches))) {//index == 3
                    intent = new Intent(getActivity(), NavigationActivity.class);
                    intent.putExtra("isMap", true);
                    Log.e(TAG, "onEndTabSelected: офисе");

                } else if (model.getTitle().equals(getString(R.string.requisites))) {
                    intent = new Intent(getActivity(), NavigationActivity.class);   //���������
                    intent.putExtra("isFrom", "deposit");
                    intent.putExtra("numberFromAccountDetail", depositAccounts.number);
                    Log.e(TAG, "onEndTabSelected: Реквезиты");

                }
                if (intent != null) {
                    intent.putExtra(ACCOUNT, depositAccounts);
                    startActivity(intent);
                    Log.e(TAG, "onEndTabSelected: depositAccounts");
                }
            }
        });
        setHasOptionsMenu(true);
        accountsVisibility = new AccountVisibilityImpl();
        accountsVisibility.registerCallBack(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (depositAccounts != null) {
            swipeRefreshLayout.setRefreshing(true);
            dateFrom = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 6);
            dateTo = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss");
            accountOperation = new AccountOperationImpl();
            accountOperation.registerCallBack(this);
            accountOperation.getAccountOperationsAndStats(getActivity(), depositAccounts.code,
                    dateFrom, dateTo, true, false);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            View v = View.inflate(getActivity(), R.layout.message_rename_acc, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //final Button cancel = (Button) v.findViewById(R.id.btnCancel);
            //final Button ok = (Button) v.findViewById(R.id.btnOk);
            final EditText name = (EditText) v.findViewById(R.id.name);
            final TextView title = (TextView) v.findViewById(R.id.title);
            if (depositAccounts.interestTargetIban == 0) {
                title.setText(R.string.change_number_nakop);
            } else {
                title.setText(R.string.change_number_depoz);
            }
            name.setText(depositAccounts.getName());
            name.setSelection(name.getText().length());
            ImageView clearSearch = (ImageView) v.findViewById(R.id.clearSearch);
            clearSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name.setText("");
                }
            });

            builder.setView(v);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!depositAccounts.getName().equals(name.getText().toString()) && !name.getText().toString().isEmpty()) {
                        accountsVisibility.setVisibilityAccounts(getActivity(), getBody(depositAccounts, name.getText()));
                        newName = name.getText().toString();
                    }
                    dialog.dismiss();
                }
            });
            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private JSONArray getBody(UserAccounts account, Editable text) {
        if (account == null) {
            return null;
        }
        JSONArray bodyList = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Code", account.getCode());
            jsonObject.put("Name", text);
            jsonObject.put("Visible", account.isVisible);
            bodyList.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bodyList;
    }

    @Override
    public void jsonAccountOperationsAndStatsResponse(int statusCode, String errorMessage, StatementsWithStats response) {
        if (isAdded()) {
            swipeRefreshLayout.setRefreshing(false);
            if (statusCode == Constants.SUCCESS) {
                setAdapter();
            } else if (statusCode != CONNECTION_ERROR_STATUS) {
                onError(errorMessage);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (depositAccounts != null && accountOperation != null) {
            accountOperation.getAccountOperationsAndStats(getActivity(), depositAccounts.code, dateFrom, dateTo, true, false);
        }
    }

    private void initToolbar() {
        ((AccountDetailsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AccountDetailsActivity) getActivity()).getSupportActionBar();
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

    private void setTollBarScrollFlags(boolean isEnterAlways) {
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        if (isEnterAlways) {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        collapsingToolbarLayout.setLayoutParams(p);
    }

    private ArrayList<NavigationTabBar.Model> getModels() {
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#00000000");

        if (depositAccounts.isCredit) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(parentActivity, R.drawable.ic_button_grey_topbar_attributes),
                            bgColor)
                            .title(getString(R.string.requisites))
                            .build()
            );
            Log.e(TAG, "getModels: isCredit");
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_button_grey_topbar_pay),
                            bgColor)
                            .title(getString(R.string.replenish))
                            .build()
            );
            Log.e(TAG, "getModels: Replenish");

        }

        if (depositAccounts.isDebit) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_button_grey_topbar_transfers),
                            bgColor)
                            .title(getString(R.string.transfer_title))
                            .build()
            );
            Log.e(TAG, "getModels: Transfer");

        }
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_statement),
                        bgColor)
                        .title(getString(R.string.account_statement))
                        .build()
        );
        Log.e(TAG, "getModels: Выписка");

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_map),
                        bgColor)
                        .title(getString(R.string.branches))
                        .build()
        );
        Log.e(TAG, "getModels: Офисы");

        return models;
    }

    private void getBundle() {
        if (getArguments() != null) {
            UserAccounts userAccounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if (userAccounts != null && userAccounts instanceof UserAccounts.DepositAccounts) {
                depositAccounts = (UserAccounts.DepositAccounts) userAccounts;
                tvTitle.setText(depositAccounts.name);
                tvAmount.setText(userAccounts.getFormattedBalance(getActivity()));
                tvAccountNumber.setText(depositAccounts.number);
                tvRate.setText(doubleFormatter(depositAccounts.rate) + "" + getString(R.string.percent_symbol));
                if (depositAccounts.getDepositExpireDate().equals(depositAccounts.getDepositOpenDate())) {
                    tvDateAccountValidity.setText(getString(R.string.perpetual_deposit));
                } else {
                    tvDateAccountValidity.setText(depositAccounts.getDepositExpireDate() + " " + getString(R.string.of_year));
                }
                tvDateAccountRemainder.setText(String.valueOf(depositAccounts.minBalance));
                tvDateAccountCapitalization.setText(depositAccounts.capitalization ? R.string.yes : R.string.Cancel);
                tvOpenDate.setText(depositAccounts.getDepositOpenDate() + " " + getString(R.string.of_year));

                Log.i("agreementNumber", "agreementNumber = " + depositAccounts.agreementNumber);
                Log.i("balance", "balance = " + depositAccounts.balance);
                Log.i("productName", "productName = " + depositAccounts.productName);
                Log.i("Period", "Period = " + depositAccounts.Period);
                Log.i("rate", "rate = " + depositAccounts.rate);
                Log.i("capitalization", "capitalization = " + depositAccounts.capitalization);
                Log.i("minBalance", "minBalance = " + depositAccounts.minBalance);
                Log.i("openDate", "openDate = " + depositAccounts.openDate);
                Log.i("expireDate", "expireDate = " + depositAccounts.expireDate);
                Log.i("isWithDrawal", "isWithDrawal = " + depositAccounts.isWithDrawal);
                Log.i("isWish", "isWish = " + depositAccounts.isWish);
                Log.i("productCode", "productCode = " + depositAccounts.productCode);

            }
        }
    }

    private void setAdapter() {
        if (isAdded()) {
            setTollBarScrollFlags(false);
            StatementAdapter adapter = new StatementAdapter(getActivity(), getSortStatement(), true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

    private ArrayList<ATFStatement> getSortStatement() {
        if (!data.isEmpty()) {
            data.clear();
        }
        ArrayList<ATFStatement> statements = GeneralManager.getInstance().getStatementsTwoWeeksAccount();
        Collections.sort(statements, new Comparator<ATFStatement>() {
            @Override
            public int compare(ATFStatement o1, ATFStatement o2) {
                return o2.operationDate.compareTo(o1.operationDate);
            }
        });
        String header = getString(R.string.history_title) + " " + getString(R.string.with) + " " +
                ((AccountDetailsActivity) getActivity()).dateFormat(dateFrom) + " " + getString(R.string.by) + " " +
                ((AccountDetailsActivity) getActivity()).dateFormat(dateTo);
        data.add(new ATFStatement(Constants.HEADER_ID, header));
        //data.addAll(statements);

        String operationDate = "operation date";
        if (statements.size() > 0) {
            for (ATFStatement statement : statements) {
                if (operationDate != null && !operationDate.equals(statement.getOperationDate())) {
                    data.add(statement);
                } else {
                    data.add(statement);
                }
            }
        } else {
            data.clear();
            data.add(new ATFStatement(-1, getString(R.string.not_data_fmt, formatDate(false, dateFrom), formatDate(false, dateTo))));
        }
        return data;
    }

    @Override
    public void jsonAccountsVisibilityResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.setUpdateAccList(true);
            tvTitle.setText(newName);
        }
    }
}
