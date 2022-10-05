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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;
import kz.optimabank.optima24.R;
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
import static kz.optimabank.optima24.utility.Utilities.formatDate;

public class AccountDetailsFragment extends ATFFragment implements AccountOperationImpl.Callback, SwipeRefreshLayout.OnRefreshListener
        ,AccountVisibilityImpl.Callback{
    //account info
    @BindView(R.id.tvAmount) TextView tvAmount;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvAccountNumber) TextView tvAccountNumber;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ntb_horizontal) NavigationTabBar navigationTabBar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;

    AccountOperation accountOperation;
    UserAccounts userAccounts;
    ArrayList<ATFStatement> data = new ArrayList<>();
    String dateFrom, dateTo;
    String newName;
    AccountsVisibility accountsVisibility;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
        setTollBarScrollFlags(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));
        navigationTabBar.setModels(getModels());
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {}

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                Intent intent = null;
                if(index == 0) {
                    intent = new Intent(getActivity(), NavigationActivity.class);   //���������
                    intent.putExtra("numberFromAccountDetail", userAccounts.number);
                    intent.putExtra("isFrom", "cheking");
                } else if(index == 1) {
                    if(!userAccounts.isClosed) {
                        intent = new Intent(getActivity(), TransfersActivity.class);   //�������
                        intent.putExtra("isReferences", true);
                        intent.putExtra("Replenish", true);
                    } else {
                        onError(getString(R.string.closed_account));
                    }
                } else if(index == 2) {
                    if(!userAccounts.isClosed) {
                        intent = new Intent(getActivity(), TransfersActivity.class);   //�������
                        intent.putExtra("isReferences", true);
                    } else {
                        onError(getString(R.string.closed_account));
                    }
                } else if(index == 3) {
                    intent = new Intent(getActivity(), StatementActivity.class);  //�������
                }
                if(intent!=null) {
                    intent.putExtra(ACCOUNT,userAccounts);
                    startActivity(intent);
                }
            }
        });
        setHasOptionsMenu(true);
        accountsVisibility =new AccountVisibilityImpl();
        accountsVisibility.registerCallBack(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setRefreshing(true);
        if(userAccounts!=null) {
            dateFrom = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 6);
            dateTo = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss");
            accountOperation = new AccountOperationImpl();
            accountOperation.registerCallBack(this);
            accountOperation.getAccountOperationsAndStats(getActivity(), userAccounts.code, dateFrom, dateTo,true,false);
        }
        Log.d("AccountDetailsFragment", "created");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.setting) {
            View v = View.inflate(getActivity(),R.layout.message_rename_acc,null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //final Button cancel = (Button) v.findViewById(R.id.btnCancel);
            //final Button ok = (Button) v.findViewById(R.id.btnOk);
            final EditText name = (EditText) v.findViewById(R.id.name);
            final TextView title = (TextView) v.findViewById(R.id.title);
            title.setText(R.string.change_number_acc);
            name.setText(userAccounts.getName());
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
                    if (!userAccounts.getName().equals(name.getText().toString()) && !name.getText().toString().isEmpty()) {
                        accountsVisibility.setVisibilityAccounts(getActivity(), getBody(userAccounts, name.getText()));
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
        if (account==null){
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
        if(isAdded()) {
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
        if(userAccounts!=null && accountOperation!=null) {
            accountOperation.getAccountOperationsAndStats(getActivity(), userAccounts.code, dateFrom, dateTo, true, false);
        }
    }

    private void initToolbar() {
        ((AccountDetailsActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AccountDetailsActivity)getActivity()).getSupportActionBar();
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

    private void setTollBarScrollFlags(boolean isEnterAlways) {
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        if(isEnterAlways) {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        collapsingToolbarLayout.setLayoutParams(p);
    }

    private ArrayList<NavigationTabBar.Model> getModels() {
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#00000000");
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(parentActivity, R.drawable.ic_button_grey_topbar_attributes),
                        bgColor)
                        .title(getString(R.string.requisites))
                        .build()
        );
        if (userAccounts.isDebit) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(parentActivity, R.drawable.ic_button_grey_topbar_pay),
                            bgColor)
                            .title(getString(R.string.replenish))
                            .build()
            );
        }
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(parentActivity, R.drawable.ic_button_grey_topbar_transfers),
                        bgColor)
                        .title(getString(R.string.transfer_title))
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(parentActivity, R.drawable.ic_button_grey_topbar_statement),
                        bgColor)
                        .title(getString(R.string.account_statement))
                        .build()
        );

        return models;
    }

    private void getBundle() {
        if(getArguments()!=null) {
            userAccounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            tvTitle.setText(userAccounts.name);
            if(userAccounts!=null) {
                tvAmount.setText(userAccounts.getFormattedBalance(getActivity()));
                tvStatus.setText(getAccountStatus());
                tvAccountNumber.setText(userAccounts.number);
                Log.d("AccountDetailsFragment", userAccounts.number);
            }
        }
    }

    private String getAccountStatus() {
        if(userAccounts.isBlocked) {
            return getString(R.string.account_blocked);
        } else if(userAccounts.isClosed) {
            return getString(R.string.loan_is_closed);
        } else {
            return getString(R.string.account_open);
        }
    }

    private void setAdapter() {
        if(isAdded()) {
            setTollBarScrollFlags(false);
            StatementAdapter adapter = new StatementAdapter(getActivity(), getSortStatement(), true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

    private String dateFormat(String date) {
        String dateFormat = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            dateFormat = Constants.VIEW_DATE_FORMAT.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    private ArrayList<ATFStatement> getSortStatement() {
        if(isAdded()) {
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
                    dateFormat(dateFrom) + " " + getString(R.string.by) + " " + dateFormat(dateTo);
            data.add(new ATFStatement(Constants.HEADER_ID, header));
            if (statements.size()>0) {
                data.addAll(statements);
            }else{
                data.clear();
                data.add(new ATFStatement(-1,getString(R.string.not_data_fmt, formatDate(false, dateFrom), formatDate(false, dateTo))));
            }
        }
        return data;
    }

    @Override
    public void jsonAccountsVisibilityResponse(int statusCode, String errorMessage) {
        if (statusCode==0){
            GeneralManager.setUpdateAccList(true);
            tvTitle.setText(newName);
        }
    }
}
