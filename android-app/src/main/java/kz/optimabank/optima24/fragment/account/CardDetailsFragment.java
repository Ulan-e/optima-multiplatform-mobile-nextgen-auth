package kz.optimabank.optima24.fragment.account;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.AccountDetailsActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.activity.StatementActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.controller.adapter.CardsViewPagerAdapter;
import kz.optimabank.optima24.controller.adapter.StatementAdapter;
import kz.optimabank.optima24.db.controllers.DigitizedCardController;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.StatementsWithStats;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.AccountOperation;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.interfaces.AccountsVisibility;
import kz.optimabank.optima24.model.interfaces.CardLock;
import kz.optimabank.optima24.model.interfaces.DefaultCardAction;
import kz.optimabank.optima24.model.manager.CardStatusChangeEvent;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountOperationImpl;
import kz.optimabank.optima24.model.service.AccountVisibilityImpl;
import kz.optimabank.optima24.model.service.AccountsImpl;
import kz.optimabank.optima24.model.service.CardLockImpl;
import kz.optimabank.optima24.model.service.DefaultCardActionImpl;
import kz.optimabank.optima24.utility.CircularViewPagerHandler;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.DepthPageTransformer;
import kz.optimabank.optima24.utility.Utilities;
import kz.optimabank.optima24.utility.views.CirclePageIndicatorOfCards;

import static android.app.Activity.RESULT_OK;
import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.BLOCK_CARD_ACTION;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.DELETE_TOKEN_ACTION;
import static kz.optimabank.optima24.utility.Constants.RESUME_TOKEN_ACTION;
import static kz.optimabank.optima24.utility.Constants.SUSPEND_TOKEN_ACTION;
import static kz.optimabank.optima24.utility.Utilities.formatDate;

/**
 * Created by Timur on 29.05.2017.
 */

public class CardDetailsFragment extends ATFFragment implements AccountOperationImpl.Callback, SwipeRefreshLayout.OnRefreshListener,
        AccountVisibilityImpl.Callback, AccountsImpl.Callback, CardLockImpl.Callback, DefaultCardActionImpl.DefaultStatusCardCallback {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.indicator)
    CirclePageIndicatorOfCards indicator;

    @BindView(R.id.balance_amount_tv)
    TextView balance_tv;

    @BindView(R.id.ntb_horizontal)
    NavigationTabBar navigationTabBar;
    @BindView(R.id.balance_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.service_recycler)
    RecyclerView serviceRecycler;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.button_unblock_card)
    Button button_unblock_card;

    AccountOperation accountOperation;
    Accounts accounts;
    ArrayList<ATFStatement> data = new ArrayList<>();
    String dateFrom, dateTo;
    UserAccounts.Cards userCard;
    int cardPosition = 0;
    String newName;
    AccountsVisibility accountsVisibility;
    private ProgressDialog progressDialog;
    private AlertDialog dialog;
    boolean isCardSlided = false;

    private final String CARD_UNBLOCK_REASON_KEY = "Reason";
    private final String CARD_UNBLOCK_REASON_CODE = "0";

    private DefaultCardAction defaultCardAction;
    private StatementAdapter statementAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_details_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(isAttached()) {
            initToolbar();
            getBundle();
            tvTitle.setText(userCard.name);
            setToolBarScrollFlags(true);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));
            buildNavTabBar();
            setHasOptionsMenu(true);
            accountsVisibility = new AccountVisibilityImpl();
            accountsVisibility.registerCallBack(this);
            setButtonUnblockCardListener();
            setDefaultCardOptions();
        }
    }

    private void setDefaultCardOptions(){
        defaultCardAction = new DefaultCardActionImpl();
        defaultCardAction.registerDefaultCardCallBack(this);
    }

    @SuppressLint("ResourceAsColor")
    private void buildNavTabBar() {
        navigationTabBar.setModels(getModels());

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                Intent intent = null;
                if (index == 0) {
                    if (!userCard.isClosed) {
                        intent = new Intent(requireContext(), NavigationActivity.class); //реквезиты
                    } else {
                        onError(getString(R.string.closed_card));   //Если карта закрыта Отобразить диалоговое окно
                    }
                } else if (index == 1) {
                    if (!userCard.isClosed) {
                        intent = new Intent(getActivity(), PaymentActivity.class);//оплатить
                        intent.putExtra("isReferences", true);
                        intent.putExtra("externalId", "TYPE_MOBILE");

                    } else {
                        onError(getString(R.string.closed_card));
                    }
                } else if (index == 2) {
                    if (!userCard.isClosed) {
                        intent = new Intent(getActivity(), TransfersActivity.class);//перевод
                        intent.putExtra("isReferences", true);
                    } else {
                        onError(getString(R.string.closed_card));
                    }
                } else if (index == 3) {
                    intent = new Intent(getActivity(), StatementActivity.class);//выписка
                } else if (index == 4) {
                    intent = new Intent(getActivity(), SettingsActivity.class);      //настройки
                    intent.putExtra("CardSett", true);
                    intent.putExtra("code", userCard.code);
                    intent.putExtra(ACCOUNT, userCard);
                    startActivityForResult(intent, 1);
                    return;
                }
                if (intent != null) {
                    intent.putExtra(ACCOUNT, userCard);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            View v = View.inflate(getActivity(), R.layout.message_rename_acc, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            final EditText name = (EditText) v.findViewById(R.id.name);
            final TextView title = (TextView) v.findViewById(R.id.title);
            title.setText(R.string.change_number_card);
            name.setText(userCard.getName());
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
                    if (!userCard.getName().equals(name.getText().toString()) && !name.getText().toString().isEmpty()) {
                        accountsVisibility.setVisibilityAccounts(getActivity(), getBody(userCard, name.getText()));
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isAttached()) {
            if (GeneralManager.getInstance().isNeedToUpdateAccounts()) {
                if (accounts == null) {
                    accounts = new AccountsImpl();
                }
                accounts.registerCallBack(this);
                accounts.getAccounts(getActivity(), true, true);

            }
            swipeRefreshLayout.setRefreshing(true);
            accountOperation = new AccountOperationImpl();
            accountOperation.registerCallBack(this);

            dateTo = Utilities.getCurrentDate(Constants.COMMON_DATE_FORMAT_WITH_SS);
            dateFrom = Utilities.getDate(Constants.COMMON_DATE_FORMAT_WITH_SS, 6);
            accountOperation.getAccountOperationsAndStats(requireContext(), userCard.code, dateFrom, dateTo, true, false);
            viewPager.setAdapter(new CardsViewPagerAdapter(requireContext(), getFragmentManager(), getUserCards()));
            viewPager.setOffscreenPageLimit(5);
            viewPager.setPageTransformer(true, new DepthPageTransformer());
            indicator.setViewPager(viewPager);
            final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
            circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
            viewPager.addOnPageChangeListener(circularViewPagerHandler);
            viewPager.setCurrentItem(cardPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAttached()) {
            checkCardBlockingStatus();
            if (GeneralManager.getInstance().isNeedToUpdateAccounts()) {
                if (accounts == null) {
                    accounts = new AccountsImpl();
                }
                accounts.registerCallBack(this);
                accounts.getAccounts(requireContext(), true, true);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CardStatusChangeEvent event) {
        if (isAttached()) {
            updateAllDigitizedCards();
        }
    }

    private void checkCardBlockingStatus() {
        if (userCard.isBlockedFromOptima24 && userCard.isBlocked) {
            button_unblock_card.setVisibility(View.VISIBLE);
        } else {
            button_unblock_card.setVisibility(View.GONE);
        }
    }

    private void setButtonUnblockCardListener() {
        CardLock cardLock;
        cardLock = new CardLockImpl();
        cardLock.registerCallBack(this);
        button_unblock_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put(CARD_UNBLOCK_REASON_KEY, CARD_UNBLOCK_REASON_CODE);
                    cardLock.setCardLock(requireContext(), userCard.code, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ViewPager.OnPageChangeListener createOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                int mPosition = position;
                if (GeneralManager.getInstance().getVisibleCards().size() != 1) {
                    if (mPosition == -1) {
                        mPosition = 0;
                    } else if (mPosition == GeneralManager.getInstance().getVisibleCards().size()) {
                        mPosition = GeneralManager.getInstance().getVisibleCards().size() - 1;
                    }
                } else {
                    mPosition = 0;
                }
                if (accountOperation != null) {
                    userCard = GeneralManager.getInstance().getVisibleCards().get(mPosition);
                    tvTitle.setText(userCard.name);
                    setToolBarScrollFlags(true);
                    recyclerView.setAdapter(null);
                    accountOperation.cancelAccountOperation();
                    swipeRefreshLayout.setRefreshing(true);
                    accountOperation.getAccountOperationsAndStats(getActivity(), userCard.code, dateFrom, dateTo, true, false);
                    checkCardBlockingStatus();
                }
                buildNavTabBar();
                if (isCardSlided && (mPosition == 0 || mPosition == (GeneralManager.getInstance().getVisibleCards().size() - 1))) {
                    Handler handler = new Handler();
                    final Runnable r = new Runnable() {
                        public void run() {
                            // force transform with a 1 pixel nudge
                            try {
                                if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0) {
                                    viewPager.beginFakeDrag();
                                    viewPager.fakeDragBy(1.0f);
                                    viewPager.endFakeDrag();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(r, 10);
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                isCardSlided = true;
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int position = viewPager.getCurrentItem();
                if (data != null) {
                    if (data.getBooleanExtra(BLOCK_CARD_ACTION, false)) {
                        userCard.isBlocked = true;
                        userCard.isBlockedFromOptima24 = true;
                        accountOperation.cancelAccountOperation();
                        swipeRefreshLayout.setRefreshing(true);
                        accountOperation.getAccountOperationsAndStats(getActivity(), userCard.code, dateFrom, dateTo, true, false);
                        buildNavTabBar();
                    } else if (data.getBooleanExtra(DELETE_TOKEN_ACTION, false) ||
                            data.getBooleanExtra(SUSPEND_TOKEN_ACTION, false) ||
                            data.getBooleanExtra(RESUME_TOKEN_ACTION, false)) {
                        showAlertInfo(getString(R.string.operation_success));
                    }
                }
                viewPager.getAdapter().notifyDataSetChanged();
                viewPager.setCurrentItem(position);
            }
        }
    }

    @Override
    public void jsonAccountsResponse(int statusCode, String errorMessage) {
        if (isAttached()) {
            if (statusCode == Constants.SUCCESS) {
                GeneralManager.getInstance().setNeedToUpdateAccounts(false);
                userCard = GeneralManager.getInstance().getCardByCode(userCard.code);
                setAdapter(true);
            } else {
                if (errorMessage != null) {
                    onError(errorMessage);
                }
            }
        }
    }

    @Override
    public void jsonAccountsOperationsResponse(int statusCode, String errorMessage) {
    }

    @Override
    public void jsonAccountOperationsAndStatsResponse(int statusCode, String errorMessage, StatementsWithStats response) {
        swipeRefreshLayout.setRefreshing(false);
        if (statusCode == Constants.SUCCESS) {
            setAdapter(true);
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void onRefresh() {
        dateFrom = Utilities.getDate(Constants.COMMON_DATE_FORMAT_WITH_SS, 7);
        dateTo = Utilities.getCurrentDate(Constants.COMMON_DATE_FORMAT_WITH_SS);
        accountOperation.getAccountOperationsAndStats(getActivity(), userCard.code, dateFrom, dateTo, true, false);
    }

    private void setToolBarScrollFlags(boolean isEnterAlways) {
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        if (isEnterAlways) {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        collapsingToolbarLayout.setLayoutParams(p);
    }

    private void setAdapter(boolean isDefaultCard) {
        setToolBarScrollFlags(false);
        data = getSortStatement();
        if (data.size() > 0) {
            if (data.get(0).id != -4) {
                ATFStatement atfStatement = new ATFStatement(-4);
                atfStatement.id = -4;
                data.add(0, atfStatement);
            }

            if (userCard.isBlocked || userCard.isClosed) {
                ATFStatement atfStatementStatus = new ATFStatement(-5);
                atfStatementStatus.id = -5;
                data.add(1, atfStatementStatus);
            }
        } else {
            if (isAdded() && getActivity() != null) {
                data.add(new ATFStatement(-4, ""));
                if (userCard.isBlocked || userCard.isClosed) {
                    ATFStatement atfStatementStatus = new ATFStatement(-5);
                    atfStatementStatus.id = -5;
                    data.add(1, atfStatementStatus);
                }
            }
            if (isAdded())
                data.add(new ATFStatement(-1, getString(R.string.not_data_fmt, formatDate(false, dateFrom), formatDate(false, dateTo))));
        }
        if(isAttached()) {
            statementAdapter = new StatementAdapter(requireActivity(), data, userCard, true, isDefaultCard);
            statementAdapter.setDefaultCardChangeListener(() -> defaultCardAction.setDefaultCardStatus(requireContext(), userCard.getId()));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(statementAdapter);
        }
    }

    private ArrayList<ATFStatement> getSortStatement() {
        if (!data.isEmpty()) {
            data.clear();
        }
        ArrayList<ATFStatement> statements = GeneralManager.getInstance().getStatementsTwoWeeksAccount();
        if (statements != null && !statements.isEmpty()) {
            Collections.sort(statements, new Comparator<ATFStatement>() {
                @Override
                public int compare(ATFStatement o1, ATFStatement o2) {
                    return o2.operationDate.compareTo(o1.operationDate);
                }
            });

            if (userCard.debtAmount != 0 || userCard.blockedAmount != 0 || userCard.isMultiBalance) {
                data.add(new ATFStatement(Constants.CARD_HEADER));
            }
            try {
                String header = getString(R.string.history_title) + " " + getString(R.string.with) + " " +
                        dateFormat(dateFrom) + " " + getString(R.string.by) + " " + dateFormat(dateTo);
                data.add(new ATFStatement(Constants.HEADER_ID, header));
            } catch (Exception e) {
                Log.i("exce111", "" + e);
            }

            String operationDate = "operation date";
            for (ATFStatement statement : statements) {
                if (operationDate != null && !operationDate.equals(statement.getOperationDate())) {
                    data.add(statement);
                } else {
                    data.add(statement);
                }
            }
        }
        return data;
    }

    private String dateFormat(String date) {
        String dateFormat = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(Constants.COMMON_DATE_FORMAT_WITH_SS);
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            dateFormat = Constants.VIEW_DATE_FORMAT.format(Objects.requireNonNull(sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
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
        if (userCard.isDebit) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(parentActivity, R.drawable.ic_button_grey_topbar_pay),
                            bgColor)
                            .title(getString(R.string.pay_action))
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
        Date expireDate = userCard.getCardExpireDate();
        Calendar calendar = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        if (expireDate != null)
            calendar.setTime(expireDate);

        if (!userCard.isClosed && !userCard.isBlocked && !c.getTime().after(calendar.getTime())) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_button_dark_common_settings),
                            bgColor)
                            .title(getString(R.string.account_settings))
                            .build()
            );
        }
        return models;
    }

    private ArrayList<UserAccounts.Cards> getUserCards() {
        int position = -1;
        for (UserAccounts.Cards card : GeneralManager.getInstance().getVisibleCards()) {
            position++;
            if (userCard.code == card.code) {
                cardPosition = position + 1;
            }
        }
        return GeneralManager.getInstance().getVisibleCards();
    }

    private void initToolbar() {
        ((AccountDetailsActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AccountDetailsActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        if (getArguments() != null) {
            userCard = (UserAccounts.Cards) getArguments().getSerializable(ACCOUNT);
        }
    }

    @Override
    public void jsonAccountsVisibilityResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.setUpdateAccList(true);
            tvTitle.setText(newName);
        }
    }

    private void updateAllDigitizedCards() {
        GeneralManager.getInstance().setRbsDefaultCard("");
        DigitizedCardController digitizedCardController = DigitizedCardController.getController();

        digitizedCardController.close();
        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
        viewPager.setCurrentItem(viewPager.getCurrentItem());
    }

    private void showAlertInfo(String message) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new AlertDialog.Builder(requireContext())
                .setTitle(requireContext().getString(R.string.alert_info))
                .setMessage(message)
                .setPositiveButton(requireContext().getString(R.string.status_ok), (dialog1, which) -> {
                    dialog1.dismiss();
                }).create();
        dialog.show();
    }

    private void showSuccessDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setMessage(R.string.operation_success)
                .setPositiveButton(getString(R.string.status_ok), (dialog, which) -> {
                    userCard.isBlocked = false;
                    Intent intent = new Intent();
                    intent.putExtra(Constants.BLOCK_CARD_ACTION, false);
                    requireActivity().setResult(RESULT_OK, intent);
                    requireActivity().finish();
                }).create();
        dialog.show();
    }

    @Override
    public void jsonCardLockResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            showSuccessDialog();
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonDefaultStatusCardResponse(int code, String message) {
        if (code == 0) {
            if(statementAdapter != null){
                Log.d("CardDetails", " code " + code);
                setAdapter(false);
                GeneralManager.getInstance().setNeedToUpdateAfterDefaultStatusCard(true);
            }
        } else {
            Log.d("CardDetails", "jsonDefaultStatusCardResponse error " + message);
        }
    }
}