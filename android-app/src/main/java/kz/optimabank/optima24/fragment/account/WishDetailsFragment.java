package kz.optimabank.optima24.fragment.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

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
import kz.optimabank.optima24.app.ServiceGenerator;
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
import okhttp3.OkHttpClient;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.formatDate;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

/**
  Created by Timur on 11.06.2017.
 */

public class WishDetailsFragment extends ATFFragment implements AccountOperationImpl.Callback,SwipeRefreshLayout.OnRefreshListener,
        AccountVisibilityImpl.Callback{
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.pieChart) PieChart mChart;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.ntb_horizontal) NavigationTabBar navigationTabBar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.tvEmptyList) TextView tvEmptyList;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.wish_img) ImageView wishImageView;

    AccountOperation accountOperation;
    UserAccounts.WishAccounts wishAccounts;
    ArrayList<ATFStatement> data = new ArrayList<>();
    String dateFrom, dateTo;
    double remainPay;
    AccountsVisibility accountsVisibility;
    private Picasso picasso;
    String newName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (picasso == null) {
            OkHttpClient client = ServiceGenerator.okHttpClient(getActivity(),false);
            picasso = new Picasso.Builder(getActivity())
                    .downloader(new OkHttp3Downloader(client))
                    .build();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wish_details_fragment, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();
        if (wishAccounts.pictureName != null) {
            String URL = wishAccounts.pictureName.contains(Constants.API_BASE_URL) ? wishAccounts.pictureName : Constants.API_BASE_URL + wishAccounts.pictureName;
            picasso.load(URL).fit().centerCrop().into(wishImageView);
        }
        setTollBarScrollFlags(true);
        if(wishAccounts!=null) {
            initChart();
        }
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
                    intent = new Intent(getActivity(), NavigationActivity.class); //���������
                } else if(index == 1) {
                    intent = new Intent(getActivity(), TransfersActivity.class);  //���������
                    intent.putExtra("isReferences", true);
                    intent.putExtra("acTo", true);
                } else if(index == 2) {
                    //if(!userCard.isClosed) {
                        intent = new Intent(getActivity(), TransfersActivity.class);//�������
                        intent.putExtra("isReferences", true);
                    //} else {
                    //    onError(getString(R.string.closed_card));
                    //}
                }else if(index == 3) {
                    intent = new Intent(getActivity(), StatementActivity.class);  //�������
                }/*else if(index == 4) {
                    Toast.makeText(getActivity(),R.string.not_available,Toast.LENGTH_LONG).show();  //��������
                }*/
                if(intent!=null) {
                    intent.putExtra(ACCOUNT, wishAccounts);
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
        tvTitle.setText(wishAccounts.wishName);
        dateFrom = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 6);
        dateTo = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss");
        accountOperation = new AccountOperationImpl();
        accountOperation.registerCallBack(this);
        accountOperation.getAccountOperationsAndStats(getActivity(), wishAccounts.code, dateFrom, dateTo,true, false);
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
            title.setText(R.string.change_number_wish);
            name.setText(wishAccounts.wishName);
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
                    if (!wishAccounts.getName().equals(name.getText().toString()) && !name.getText().toString().isEmpty()) {
                        accountsVisibility.setVisibilityAccounts(getActivity(), getBody(wishAccounts, name.getText()));
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
    public void onRefresh() {
        if(wishAccounts!=null && accountOperation!=null) {
            accountOperation.getAccountOperationsAndStats(getActivity(), wishAccounts.code, dateFrom, dateTo, true, false);
        }
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

    private void setAdapter() {
        if(isAdded()) {
            setTollBarScrollFlags(false);
            ArrayList<ATFStatement> statements = getSortStatement();
            if (!statements.isEmpty()) {
                StatementAdapter adapter = new StatementAdapter(getActivity(), getSortStatement(), wishAccounts, true
                        , false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            } else {
                //tvEmptyList.setVisibility(View.VISIBLE);
            }
        }
    }

    private ArrayList<ATFStatement> getSortStatement() {
        if(!data.isEmpty()) {
            data.clear();
        }
        Log.d(TAG,"GeneralManager.getInstance().getStatementsTwoWeeksAccount() = " + GeneralManager.getInstance().getStatementsTwoWeeksAccount());
        ArrayList<ATFStatement> statements = GeneralManager.getInstance().getStatementsTwoWeeksAccount();
        Collections.sort(statements, new Comparator<ATFStatement>() {
            @Override
            public int compare(ATFStatement o1, ATFStatement o2) {
                return o2.operationDate.compareTo(o1.operationDate);
            }
        });
        String header = getString(R.string.history_title) + " " + getString(R.string.with) + " " +
                ((AccountDetailsActivity)getActivity()).dateFormat(dateFrom) + " " + getString(R.string.by) + " " +
                ((AccountDetailsActivity)getActivity()).dateFormat(dateTo);

        data.add(new ATFStatement(Constants.CREDIT_HEADER));
        data.add(new ATFStatement(Constants.HEADER_ID, header));

        String operationDate = "operation date";
        if (statements.size()>0) {
            for (ATFStatement statement : statements) {
                if (operationDate != null && !operationDate.equals(statement.getOperationDate())) {
                    data.add(statement);
                } else {
                    data.add(statement);
                }
            }
        }else{
            data.clear();
            data.add(new ATFStatement(Constants.CREDIT_HEADER));
            data.add(new ATFStatement(-1,getString(R.string.not_data_fmt, formatDate(false, dateFrom), formatDate(false, dateTo))));
        }

        //data.addAll(statements);
        return data;
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

    private void getBundle() {
        if(getArguments()!=null) {
            UserAccounts userAccounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if(userAccounts!=null && userAccounts instanceof UserAccounts.WishAccounts) {
                wishAccounts = (UserAccounts.WishAccounts) userAccounts;
                newName = wishAccounts.wishName;
                Log.i("pictureName","pictureName = "+wishAccounts.pictureName);
            }
        }
    }

    private ArrayList<NavigationTabBar.Model> getModels() {
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#00000000");
        models.add (
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_attributes),
                        bgColor)
                        .title(getString(R.string.requisites))
                        .build()
        );
        if (wishAccounts.isCredit) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_button_grey_topbar_pay),
                            bgColor)
                            .title(getString(R.string.replenish))
                            .build()
            );
        }
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_transfers),
                        bgColor)
                        .title(getString(R.string.transfer_title))
                        .build()
        );

        models.add (
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_statement),
                        bgColor)
                        .title(getString(R.string.account_statement))
                        .build()
        );
        /*models.add (
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.transfer_ico_fill),
                        bgColor)
                        .title(getString(R.string.change))
                        .build()
        );*/
        return models;
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);
        remainPay = wishAccounts.totalAmount - wishAccounts.balance;
        mChart.setCenterText(TextUtils.concat(generateCenterSpannableTextInfo(), "\n", "\n",
                generateCenterSpannableTextInfo2(), "\n", "\n" , generateCenterSpannableTextInfo3()));
        mChart.setHoleColor(getResources().getColor(R.color.transparent));
        mChart.setHoleRadius(85f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(-90);
        mChart.setDescription(null);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setTouchEnabled(false);
        setData();
        mChart.highlightValues(null);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float f1 = (float) wishAccounts.totalAmount;
        float f2 = (float) wishAccounts.balance;
        entries.add(new PieEntry(f2,""));
        entries.add(new PieEntry(f1,""));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.orange_246_121_37));
        colors.add(getResources().getColor(R.color.blue_178_198_216));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        mChart.setData(data);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableTextInfo() {
        SpannableString s = new SpannableString(getString(R.string.accumulated));
        s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_178_198_216)), 0, s.length(), 0);
        return s;
    }

    private SpannableString generateCenterSpannableTextInfo2() {
        SpannableString s = new SpannableString(getFormattedBalance(wishAccounts.balance, wishAccounts.currency));
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, s.length(), 0);
        return s;
    }

    private SpannableString generateCenterSpannableTextInfo3() {
        SpannableString s = new SpannableString(getString(R.string.of_fmt, getFormattedBalance(wishAccounts.totalAmount, wishAccounts.currency)));
        s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_178_198_216)), 0, s.length(), 0);
        return s;
    }

    @Override
    public void jsonAccountsVisibilityResponse(int statusCode, String errorMessage) {
        if (statusCode==0){
            GeneralManager.setUpdateAccList(true);
            tvTitle.setText(newName);
        }
    }
}
