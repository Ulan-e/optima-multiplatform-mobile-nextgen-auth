package kz.optimabank.optima24.activity;

import static org.koin.java.KoinJavaComponent.inject;
import static kg.optima.mobile.android.utils.NavigationUtilsKt.toBankContactsScreen;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.activity.PaymentActivity.EXTERNAL_ID;
import static kz.optimabank.optima24.utility.Constants.BACKGROUND_NOTIFICATION_ID;
import static kz.optimabank.optima24.utility.Constants.BANK_ID;
import static kz.optimabank.optima24.utility.Constants.IS_BACKGROUND_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.IS_NOTIFICATION;
import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import kg.optima.mobile.R;
import kg.optima.mobile.feature.auth.component.AuthPreferences;
import kotlin.Lazy;
import kz.optimabank.optima24.app.NonSwipeableViewPager;
import kz.optimabank.optima24.controller.adapter.TabAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.JustFragment;
import kz.optimabank.optima24.fragment.account.AccountListFragment;
import kz.optimabank.optima24.fragment.history.HistoryContainer;
import kz.optimabank.optima24.fragment.payment.PaymentAdapFragment;
import kz.optimabank.optima24.fragment.payment.PaymentFragment;
import kz.optimabank.optima24.fragment.payment.invoice.FindInvoiceAccountFragment;
import kz.optimabank.optima24.fragment.transfer.TransfersFragment;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.interfaces.News;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.NewsImpl;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.notifications.ui.NotificationsActivity;
import kz.optimabank.optima24.notifications.ui.notificationsList.NotificationsListViewModel;
import kz.optimabank.optima24.notifications.ui.notificationsList.NotificationsListViewModelFactory;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;


// navigation drawer
public class MenuActivity extends OptimaActivity implements TransferAndPaymentImpl.Callback, View.OnClickListener,
        NewsImpl.Callback {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static boolean isMenu = false;

    NavigationTabBar navigationTabBar;
    public NonSwipeableViewPager viewPager;
    public TabAdapter tabAdapter;
    UnderlinePageIndicator indicator;
    PaymentContextController paymentController;
    private ArrayList<NewsItem> newsList;
    public static double screenInches;
    public String sessionID;

    private GeneralManager generalManager;

    News news;
    TransferAndPayment transferAndPayment;
    AccountListFragment accListFragment;

    private NotificationsListViewModel model;

    private Lazy<AuthPreferences> authPreferences = inject(AuthPreferences.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generalManager = GeneralManager.getInstance();
        Log.i("TAG", "checkPlayServices = " + checkPlayServices());

        setContentView(R.layout.fragment_container);

        GeneralManager.dispose();
        GeneralManager.getInstance().setSessionId(authPreferences.getValue().getSessionData().getAccessToken());

        initViewModel();
        saveUserData();

        final RelativeLayout mainRelMenu = (RelativeLayout) findViewById(R.id.mainRelMenu);

        RelativeLayout relUser = (RelativeLayout) findViewById(R.id.relUser);
        RelativeLayout relNotice = (RelativeLayout) findViewById(R.id.relNotice);
        RelativeLayout relNews = (RelativeLayout) findViewById(R.id.relNews);
        RelativeLayout relMap = (RelativeLayout) findViewById(R.id.relMap);
        RelativeLayout relExit = (RelativeLayout) findViewById(R.id.relExit);
        RelativeLayout relExchange = (RelativeLayout) findViewById(R.id.relExchange);
        RelativeLayout relMess = (RelativeLayout) findViewById(R.id.relMess);
        RelativeLayout relCommunicate = (RelativeLayout) findViewById(R.id.relCommunicate);
//        RelativeLayout relWebView = (RelativeLayout) findViewById(R.id.relWebview);

        final Animation sunRiseAnimation = AnimationUtils.loadAnimation(this, R.anim.menu_anim);
        final Animation sunRiseAnimationBack = AnimationUtils.loadAnimation(this, R.anim.menu_anim_back);

        relUser.setOnClickListener(this);
        relMess.setOnClickListener(this);
        relNotice.setOnClickListener(this);
        relNews.setOnClickListener(this);
        relMap.setOnClickListener(this);
        relExit.setOnClickListener(this);
        relExchange.setOnClickListener(this);
        relCommunicate.setOnClickListener(this);
        mainRelMenu.setOnClickListener(this);

        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.vp_horizontal_ntb);
        indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), getFragments());
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(2);
        navigationTabBar.setModels(getModels());
        navigationTabBar.setViewPager(null, 0);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                Log.i("navigationTabBar", "StartPos = " + index);
                switch (index) {
                    case 0:
                        if (generalManager.isNeedToUpdateAccounts())
                            accListFragment.showProgressIfUpdating();
                        if (isMenu) {
                            isMenu = false;
                            mainRelMenu.setClickable(false);
                            mainRelMenu.startAnimation(sunRiseAnimationBack);
                            mainRelMenu.setVisibility(View.GONE);
                        }
                        viewPager.setCurrentItem(index);
                        break;
                    case 1:
                    case 2:
                    case 3:
                        if (isMenu) {
                            isMenu = false;
                            mainRelMenu.setClickable(false);
                            mainRelMenu.startAnimation(sunRiseAnimationBack);
                            mainRelMenu.setVisibility(View.GONE);
                        }
                        viewPager.setCurrentItem(index);
                        break;
                    case 4:
                        if (!isMenu) {
                            mainRelMenu.setClickable(true);
                            isMenu = true;
                            mainRelMenu.setVisibility(View.VISIBLE);
                            mainRelMenu.startAnimation(sunRiseAnimation);
                            fetchUnreadNotificationsCount();
                        } else {
                            navigationTabBar.setModelIndex(viewPager.getCurrentItem());
                            indicator.setCurrentItem(viewPager.getCurrentItem());
                            isMenu = false;
                            mainRelMenu.setClickable(false);
                            mainRelMenu.startAnimation(sunRiseAnimationBack);
                            mainRelMenu.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
            }
        });
        indicator.setViewPager(viewPager);
        indicator.setFades(false);


        getForegroundNotification();
        getBackgroundNotification();

        transferAndPayment = new TransferAndPaymentImpl();
        transferAndPayment.registerCallBack(this);
        transferAndPayment.getPaymentContext(this);

        SharedPreferences isNeedRegistredAgainSP = getSharedPreferences("isNeedRegistredAgain", MODE_PRIVATE);
        boolean isNeedRegistredAgain = isNeedRegistredAgainSP.getBoolean("isNeedRegistredAgain", true);
        Log.i("infome", "isNeedRegistredAgain = " + isNeedRegistredAgain);
        if (isNeedRegistredAgain) {
            SharedPreferences.Editor isNeedRegistredAgainSPEditor = isNeedRegistredAgainSP.edit();
            isNeedRegistredAgainSPEditor.putBoolean("isNeedRegistredAgain", false);
            isNeedRegistredAgainSPEditor.apply();

        }
    }
    private void saveUserData() {
        GeneralManager.getInstance().setUser(mapToUser());
        GeneralManager.getInstance().setAppOpen(true);
        GeneralManager.getInstance().setAutoEncrypt(authPreferences.getValue().getUserInfo().getAutoEncrypt());
        GeneralManager.setPhone(authPreferences.getValue().getUserInfo().getPhoneNumber());
        GeneralManager.setSessionDuration(authPreferences.getValue().getSessionData().getSessionDuration());
        GeneralManager.getInstance().setProfImage(authPreferences.getValue().getUserInfo().getImageHash());
        saveBankId(authPreferences.getValue().getUserInfo().getBankId());
    }

    private void saveBankId(String bankId) {
        SharedPreferences.Editor editor
                = this.getSharedPreferences("my_pref", Context.MODE_PRIVATE).edit();
        editor.putString(BANK_ID, bankId);
        editor.apply();
    }

    private AuthorizationResponse.User mapToUser() {
        AuthorizationResponse.User user = new AuthorizationResponse.User();
        user.setAddress(authPreferences.getValue().getUserInfo().getAddress());
        user.setBankId(authPreferences.getValue().getUserInfo().getBankId());
        user.setFirstName(authPreferences.getValue().getUserInfo().getFirstName());
        user.setIdn(authPreferences.getValue().getUserInfo().getIdn());
        user.setFullName(authPreferences.getValue().getUserInfo().getFullName());
        user.setImageHash(authPreferences.getValue().getUserInfo().getImageHash());
        user.setLogin(authPreferences.getValue().getUserInfo().getLogin());
        user.setLastName(authPreferences.getValue().getUserInfo().getLastName());
        user.setMiddleName(authPreferences.getValue().getUserInfo().getMiddleName());
        user.setSex(authPreferences.getValue().getUserInfo().getSex());
        user.setMobilePhoneNumber(authPreferences.getValue().getUserInfo().getPhoneNumber());
        user.setAutoEncrypt(authPreferences.getValue().getUserInfo().getAutoEncrypt());
        return user;
    }

    private void initViewModel() {
        model = new ViewModelProvider(
                this,
                new NotificationsListViewModelFactory(this.getApplication())
        ).get(NotificationsListViewModel.class);
    }

    // ???????????????? ?????????????????????? ?????????? ???????????????????? ??????????????
    private void getBackgroundNotification() {
        SharedPreferences preferences = Utilities.getPreferences(this);
        boolean isNotification = preferences.getBoolean(IS_BACKGROUND_NOTIFICATION, false);

        // ?????????? ?????????????????? ?????????????????????? ???????????? ???????????? ???? false
        resetNotification();

        if (isNotification) {
            String notificationId = preferences.getString(BACKGROUND_NOTIFICATION_ID, null);
            Intent notificationIntent = new Intent(this, NotificationsActivity.class);
            notificationIntent.putExtra(NOTIFICATION_ARG_ID, notificationId);
            Log.d(PUSH_TAG, "getBackgroundNotification background, notificationId = " + notificationId);
            startActivity(notificationIntent);
        }
    }

    // ???????????? ???????????????? is_notification ???? false
    private void resetNotification() {
        SharedPreferences.Editor editor = Utilities.getPreferences(this).edit();
        editor.putBoolean(IS_BACKGROUND_NOTIFICATION, false);
        editor.apply();
    }

    // ???????????????? ?????????????????????? ?????????? ???????????????????? ??????????????
    private void getForegroundNotification() {
        Intent intent = getIntent();
        boolean isNotification = intent.getBooleanExtra(IS_NOTIFICATION, false);

        if (isNotification) {
            Intent notificationsIntent = new Intent(this, NotificationsActivity.class);
            String notificationId = intent.getStringExtra(NOTIFICATION_ARG_ID);
            notificationsIntent.putExtra(NOTIFICATION_ARG_ID, notificationId);
            Log.d(PUSH_TAG, "getForegroundNotification, notificationId = " + notificationId);
            startActivity(notificationsIntent);
        }
    }

    private ArrayList<NavigationTabBar.Model> getModels() {
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#00000000");
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_home),//R.drawable.ic_index
                        bgColor).title(getString(R.string.main))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_transfers),//R.drawable.transfer_copy
                        bgColor).title(getString(R.string.transfers_title))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_payments),//R.drawable.pay
                        bgColor).title(getString(R.string.payments))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_history),//R.drawable.history_ico
                        bgColor).title(getString(R.string.payments_history))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_menu),
                        bgColor).title(getString(R.string.menu))
                        .build()
        );
        return models;
    }

    private ArrayList<ATFFragment> getFragments() {
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        accListFragment = new AccountListFragment();
        fragments.add(accListFragment);
        fragments.add(new TransfersFragment());
        fragments.add(new PaymentAdapFragment());
        fragments.add(new HistoryContainer());
        fragments.add(new JustFragment());
        return fragments;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        if (generalManager.getSessionId() == null) {
//            finish();
//        }
    }



    @Override
    protected void onStart() {
        news = new NewsImpl();
        news.registerCallBack(this);
        news.getNews(this);
        super.onStart();
    }


    // ???????????????? ?????? ?????????????????????????? ??????????????????
    private void fetchUnreadNotificationsCount() {
        String bankId = getBankId();

        if (bankId != null) {
            model.getUnreadNotificationsCount(bankId).observe(this, count -> {
                if (count != null) {
                    showUnreadNotificationsCount(count);
                }
            });
        }
    }

    // ???????????????????? ???????????????????? ?????????????????????????? ?????????????????? ?? ?????????????? ????????????????
    private void showUnreadNotificationsCount(int count) {
        RelativeLayout layoutRedMessages = findViewById(R.id.relRedMess);
        TextView textViewUnreadMessagesCount = findViewById(R.id.tvRedMess);

        if (count > 0) {
            layoutRedMessages.setVisibility(View.VISIBLE);
            textViewUnreadMessagesCount.setText(String.valueOf(count));
        } else {
            layoutRedMessages.setVisibility(View.GONE);
        }
        Log.d(PUSH_TAG, "showUnreadNotificationsCount() " + count);
    }

    // ?????????????????? ???????????? ???????????????? ????????????????????????
    private String getBankId() {
        SharedPreferences preferences = Utilities.getPreferences(this);
        return preferences.getString(BANK_ID, "");
    }

    private void setRedNews() {
        RelativeLayout relRedNews = (RelativeLayout) findViewById(R.id.relRedNews);
        TextView tvRedNews = (TextView) findViewById(R.id.tvRedNews);
        int newCount = 0;
        newsList = generalManager.getNews();
        for (NewsItem newsItemForIm : newsList) {
            Log.i("NEWSPUBDATE", "date = " + newsItemForIm.getPublishDate());
            String str = String.valueOf(newsItemForIm.getId());
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int isViewed = prefs.getInt(str, -1);
            if (Utilities.getDateSevenDaysAgo(newsItemForIm.getPublishDate()) && isViewed == -1) {
                newCount++;
            }
        }
        if (newCount > 0) {
            relRedNews.setVisibility(View.VISIBLE);
            tvRedNews.setText(String.valueOf(newCount));
        } else {
            relRedNews.setVisibility(View.INVISIBLE);
        }
    }

    public int checkInvoice(Templates template, int tag) {
        Log.i("tag ====", "tag = " + tag);
        if (template instanceof TemplatesPayment) {
            TemplatesPayment templatesPayment = (TemplatesPayment) template;
            Log.i("template ====", "template = " + templatesPayment.serviceId);
            paymentController = PaymentContextController.getController();
            PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
            Log.d("MenuActivity", "paymentService = " + paymentService);
            if (paymentService.IsInvoiceable) {
                if (tag == Constants.TAG_CHANGE) {
                    return 1;
                }
                for (Invoices invoice : generalManager.getInvoices()) {
                    if (invoice.getSubscriptionId() == templatesPayment.id) {
                        Intent intent = new Intent(MenuActivity.this, InvoiceAblePaymentActivity.class);
                        intent.putExtra("InvoiceId", invoice.getInvoiceId());
                        intent.putExtra("paymentServiceId", paymentService.id);
                        Log.e("MenuAtivity", paymentService.ExternalId + "");
                        intent.putExtra(EXTERNAL_ID, paymentService.ExternalId);
                        intent.putExtra(CATEGORY_NAME, paymentService.name);
                        startActivity(intent);
                        //this invoice, there is a receipt
                        return 0;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.not_invoices));
                builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                //this invoice, There is no receipt
                return 1;
            } else {
                //This is not an invoice
                return 2;
            }
        }
        //error
        return -1000;
    }

    @Override
    public void onBackPressed() {
        exitAppDialog();
    }

    public void navigateToPage(Fragment fragment, String name, int ID) {
        //putString(CATEGORY_NAME, paymentService.name);
        //putInt("paymentServiceId", paymentService.id);
        Intent intent;
        intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(CATEGORY_NAME, name);
        intent.putExtra("paymentServiceId", ID);
        if (fragment instanceof PaymentFragment) {
            intent.putExtra("isToPayFrag", true);
        } else if (fragment instanceof FindInvoiceAccountFragment) {
            intent.putExtra("isToFindInvoiceAccountFragment", true);
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (generalManager.getSessionId() == null) {
//            startActivity(new Intent(MenuActivity.this, UnauthorizedTabActivity.class));
//            finish();
//        } else {
//            OptimaBank.getContext().setIsBackground(false);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MenuActivity", "onDestroy");
        generalManager.cancelAlarmManager();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            }
            return false;
        }
        return true;
    }

    private void closeMenu() {
        final RelativeLayout mainRelMenu = (RelativeLayout) findViewById(R.id.mainRelMenu);
        final Animation sunRiseAnimationBack = AnimationUtils.loadAnimation(this, R.anim.menu_anim_back);
        isMenu = false;
        navigationTabBar.setModelIndex(viewPager.getCurrentItem());
        indicator.setCurrentItem(viewPager.getCurrentItem());
        mainRelMenu.setClickable(false);
        mainRelMenu.startAnimation(sunRiseAnimationBack);
        mainRelMenu.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        final RelativeLayout mainRelMenu = (RelativeLayout) findViewById(R.id.mainRelMenu);
        final Animation sunRiseAnimationBack = AnimationUtils.loadAnimation(this, R.anim.menu_anim_back);

        switch (v.getId()) {
            case R.id.mainRelMenu:
                closeMenu();
                break;
            case R.id.relUser:
                intent = new Intent(this, ProfileActivity.class);
                closeMenu();
                break;
            case R.id.relNotice:
                intent = new Intent(this, NotificationsActivity.class);
                intent.putExtra("isNotice", true);
                closeMenu();
                break;
            case R.id.relMess:
                getNotificationList();
                closeMenu();
                break;
            case R.id.relNews:
                intent = new Intent(this, NavigationActivity.class);
                intent.putExtra("isNewsList", true);
                closeMenu();
                break;
            case R.id.relMap:
                intent = new Intent(this, NavigationActivity.class);
                intent.putExtra("isMap", true);
                closeMenu();
                break;
            case R.id.relExchange:
                intent = new Intent(this, NavigationActivity.class);
                intent.putExtra("isRate", true);
                closeMenu();
                break;
            case R.id.relCommunicate:
                intent = new Intent(this, NavigationActivity.class);
                intent.putExtra("isContact", true);
                Log.d("asdsadasdas", "onClick: dsadas");
                toBankContactsScreen(this);
                closeMenu();
                break;
            case R.id.relExit:
                exitAppDialog();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void exitAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MainAlertDialogStyle);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.q_logout));
        builder.setPositiveButton(getString(R.string._yes), (dialog, which) -> {
            // ?????????????? ????????????
            GeneralManager.dispose();
            GeneralManager.getInstance().setSessionId(null);
            finishAffinity();
            System.exit(0);
        });
        builder.setNegativeButton(getString(R.string._no), (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert.show();
    }

    private void getNotificationList() {
        Intent intent = new Intent(this, NotificationsActivity.class);
        //intent.putExtra("isNotice",true);
        startActivity(intent);
    }

    @Override
    public void jsonNewsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            setRedNews();
        }
    }

    @Override
    public void jsonPaymentContextResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {

    }
}
