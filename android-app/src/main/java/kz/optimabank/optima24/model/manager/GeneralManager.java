package kz.optimabank.optima24.model.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import androidx.collection.ArrayMap;

import org.json.JSONArray;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import kg.optima.mobile.R;
import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.activity.SessionDialogActivity;
import kz.optimabank.optima24.db.controllers.DHKeyController;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.controllers.PushTokenKeyController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.Category;
import kz.optimabank.optima24.model.base.HistoryApplications;
import kz.optimabank.optima24.model.base.HistoryItem;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.base.Rate;
import kz.optimabank.optima24.model.base.SettingsModel;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.base.TransferModel;
import kz.optimabank.optima24.model.gson.response.AccountsResponse;
import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;
import kz.optimabank.optima24.model.gson.response.PaymentTemplateResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.crypt.CryptoUtils;

/**
 * Created by Timur on 12.02.2017.
 */

public class GeneralManager {
    private static GeneralManager instance;
    public ArrayMap<Integer, byte[]> fullBimapForCards = new ArrayMap<>();
    public ArrayMap<Integer, byte[]> minBimapForCards = new ArrayMap<>();
    private String sessionId, transferDocumentId;
    private AuthorizationResponse.User user;
    private ArrayList<UserAccounts.Cards> cards;
    private ArrayList<UserAccounts.CardAccounts> cardsAccounts;
    private ArrayList<UserAccounts.CheckingAccounts> checkingAccounts;
    private ArrayList<UserAccounts.CreditAccounts> credit;
    private ArrayList<UserAccounts.DepositAccounts> deposits;
    private ArrayList<UserAccounts.WishAccounts> wishDeposits;
    private ArrayList<UserAccounts.EncryptedCard> encryptedCards;
    private ArrayList<Terminal> branches;
    private ArrayList<Terminal> atms;
    private ArrayList<Terminal> terminals;
    private ArrayList<TemplatesPayment> templatesPayment;
    private ArrayList<TemplateTransfer> templatesTransfer;
    private ArrayList<NewsItem> news;
    private ArrayList<Rate> rate;
    private ArrayList<Category> category;
    private ArrayList<ATFStatement> statements;
    private ArrayList<ATFStatement> statementsTwoWeeksAccount;
    private ArrayList<ATFStatement> statementsAccount;
    private ArrayList<HistoryItem.PaymentHistoryItem> paymentHistory;
    private ArrayList<Limit> limit;
    private ArrayList<HistoryItem.TransferHistoryItem> transferHistory;
    private ArrayList<LoanScheduleResponse> loanScheduleResponses;
    private ArrayList<Invoices> invoices;
    private JSONArray paymentParameters;
    private HashMap<String, Terminal> terminalHashMap;
    private boolean needToUpdateAccounts, needToUpdateTemplate, needToUpdateHistory, isInvoiceCheckedStatus, isAppOpen, needToUpdatePayTempl, isInitializedMobocardsSdk;
    private Location userLocation;
    private UserAccounts invoiceAccountFrom;
    private BigInteger a, p;
    private int regionID;
    private static boolean isLocaleChanged;
    private static boolean isRegionSeleсted;
    private static boolean isUpdateAccList;
    private static boolean isLocaleChangedForBanners;
    private static boolean isInfomeReg;
    private static boolean isFirstCheckSms = true;
    private static boolean isNeedUpdateProfImage;
    private static boolean terminalChecked = true, branchesChecked = true, atmChecked = true;
    private static boolean templateClicked;                                           // булева для MaskTextWatcher, что в платежах был нажат шаблон, а не ручное редактирование текста в поле number
    private static ArrayMap<Integer, Boolean> hashSmsNotif = new ArrayMap<>();
    private ArrayList<HistoryApplications> mHistoryApplicationses;
    private Bitmap bitmap;
    //  private AlarmManager alarmManager;
    //  private PendingIntent pendingIntent;
    public static boolean isEnteredToSession;
    public boolean isMobilePayment;

    private Intent sessionTimerService;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Intent sessionDialogIntent;
    private String errorMessageForReg;
    private long millisecondsForUnlockButton;
    private static int sessionDuration;
    private String rbsDefaultCard;
    private String profImage;
    private boolean autoEncrypt;
    private AlarmManager.OnAlarmListener onAlarmListener;
    public static boolean isClickOnPush;
    public static String messageId;
    private static String phone;
    private static boolean isFromTemplate;
    private boolean isUpdateDefaultStatusCard;

    private static BankRequisitesResponse bankRequisitesResponse;

    public static GeneralManager getInstance() {
        if (instance == null) {
            instance = new GeneralManager();
        }
        return instance;
    }

    private GeneralManager() {
        cards = new ArrayList<>();
        cardsAccounts = new ArrayList<>();
        checkingAccounts = new ArrayList<>();
        credit = new ArrayList<>();
        deposits = new ArrayList<>();
        category = new ArrayList<>();
        atms = new ArrayList<>();
        branches = new ArrayList<>();
        terminals = new ArrayList<>();
        news = new ArrayList<>();
        templatesPayment = new ArrayList<>();
        rate = new ArrayList<>();
        statements = new ArrayList<>();
        wishDeposits = new ArrayList<>();
        encryptedCards = new ArrayList<>();
        templatesTransfer = new ArrayList<>();
        statementsTwoWeeksAccount = new ArrayList<>();
        statementsAccount = new ArrayList<>();
        paymentHistory = new ArrayList<>();
        transferHistory = new ArrayList<>();
        invoices = new ArrayList<>();
        mHistoryApplicationses = new ArrayList<>();
    }


    public static boolean isInfomeReg() {
        return isInfomeReg;
    }

    public static void setIsInfomeReg(boolean isInfomeReg) {
        GeneralManager.isInfomeReg = isInfomeReg;
    }

    public static void dispose() {
        instance = null;
    }

    public static boolean isFirstCheckSms() {
        return isFirstCheckSms;
    }

    public static void setIsFirstCheckSms(boolean isFirstCheckSms) {
        GeneralManager.isFirstCheckSms = isFirstCheckSms;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        GeneralManager.phone = phone;
    }

    public static boolean getIsFromTemplate() {
        return isFromTemplate;
    }

    public static void setIsFromTemplate(boolean isFromTemplate) {
        GeneralManager.isFromTemplate = isFromTemplate;
    }

    public static void setSessionDuration(int duration) {
        GeneralManager.sessionDuration = duration;
    }

    public static boolean isTerminalChecked() {
        return terminalChecked;
    }

    public static BankRequisitesResponse getBankRequisitesResponse() {
        return bankRequisitesResponse;
    }

    public static void setBankRequisitesResponse(BankRequisitesResponse bankRequisitesResponse) {
        GeneralManager.bankRequisitesResponse = bankRequisitesResponse;

    }

    public static void setTerminalChecked(boolean terminalChecked) {
        GeneralManager.terminalChecked = terminalChecked;
    }

    public static boolean isBranchesChecked() {
        return branchesChecked;
    }

    public static void setBranchesChecked(boolean branchesChecked) {
        GeneralManager.branchesChecked = branchesChecked;
    }

    public static boolean isAtmChecked() {
        return atmChecked;
    }

    public static void setAtmChecked(boolean atmChecked) {
        GeneralManager.atmChecked = atmChecked;
    }

    private long getElapsedSessionTimeMillis() {
        return SystemClock.elapsedRealtime() + ((sessionDuration * 60000) - 60000);
    }

    //Таймер на сессию старая версия
    public void setAlarmManager(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SessionDialogActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            Log.d("Exception", "setAlarmManager + " + e.getMessage());
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), pendingIntent);
        }
        isEnteredToSession = true;
    }

    //Таймер на сессию новая версия. Начиная с андроид 7+ если приложение закрыт он его не откроет.
    public void setAlarmManagerOn(Context context) {
        Log.d("setAlarmManagerOn", "Alarm started");

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        sessionDialogIntent = new Intent(context, SessionDialogActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), "alarm", onAlarmListener = () -> {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean appIsRunning = prefs.getBoolean("appIsRunning", true);
                if (appIsRunning) {
                    Log.d("setAlarmManagerOn", "Приложение был открытым");
                    context.startActivity(sessionDialogIntent);
                } else {
                    Log.d("setAlarmManagerOn", "Приложение был закрытым");
                    System.exit(0);
                }
            }, null);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, sessionDialogIntent, 0);
            try {
                alarmManager.cancel(pendingIntent);
            } catch (Exception e) {
                Log.d("Exception", "setAlarmManager + " + e.getMessage());
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), pendingIntent);
                Log.d("setAlarmManagerOn", "Приложение был Киткат и выше до N");

            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), pendingIntent);
                Log.d("setAlarmManagerOn", "Приложение ниже киткат");
            }
        }
        isEnteredToSession = true;
    }

    public void refreshAlarmManager(Context context) {
        if (isEnteredToSession) {
            sessionDialogIntent = new Intent(context, SessionDialogActivity.class);
            Log.d("setAlarmManagerOn", "Alarm refreshed");
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    alarmManager.cancel(onAlarmListener);
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), "alarm", onAlarmListener = () -> {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        boolean appIsRunning = prefs.getBoolean("appIsRunning", true);
                        if (appIsRunning) {
                            Log.d("setAlarmManagerOn", "Приложение был открытым");
                            context.startActivity(sessionDialogIntent);
                        } else {
                            Log.d("setAlarmManagerOn", "Приложение был закрытым");
                            System.exit(0);
                        }
                    }, null);
                } else {
                    pendingIntent = PendingIntent.getActivity(context, 0, sessionDialogIntent, 0);
                    try {
                        alarmManager.cancel(pendingIntent);
                    } catch (Exception e) {
                        Log.d("Exception", "setAlarmManager + " + e.getMessage());
                    }
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), pendingIntent);
                        Log.d("setAlarmManagerOn", "Приложение был Киткат и выше до N");

                    } else {
                        alarmManager.set(AlarmManager.ELAPSED_REALTIME, getElapsedSessionTimeMillis(), pendingIntent);
                        Log.d("setAlarmManagerOn", "Приложение ниже киткат");
                    }
                }
            }
        }
    }

    public void cancelAlarmManager() {
        try {
            isEnteredToSession = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                alarmManager.cancel(onAlarmListener);
            } else {
                alarmManager.cancel(pendingIntent);
            }
        } catch (Exception e) {
            Log.d("refreshSessionTime", " + " + e.getMessage());
        }
    }

    public static boolean isNeedUpdateProfImage() {
        return isNeedUpdateProfImage;
    }

    public static void setNeedUpdateProfImage(boolean isNeedUpdateProfImage) {
        GeneralManager.isNeedUpdateProfImage = isNeedUpdateProfImage;
    }

    public static boolean isTemplateClicked() {
        return templateClicked;
    }

    public static void setTemplateClicked(boolean templateClicked) {
        GeneralManager.templateClicked = templateClicked;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLanguageApp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OptimaApp.Companion.getInstance());
        return preferences.getString(Constants.APP_LANGUAGE, "ru");
    }

    public boolean isNeedToUpdateAccounts() {
        return needToUpdateAccounts;
    }

    public void setNeedToUpdateAfterDefaultStatusCard(boolean isUpdate) {
        this.isUpdateDefaultStatusCard = isUpdate;
    }

    public boolean isNeedToUpdateAfterDefaultStatusCard() {
        return isUpdateDefaultStatusCard;
    }

    public boolean isNeedToUpdatePayTempl() {
        return needToUpdatePayTempl;
    }

    public void setNeedToUpdateAccounts(boolean needToUpdateAccounts) {
        this.needToUpdateAccounts = needToUpdateAccounts;
    }

    public void setNeedToUpdatePayTempl(boolean needToUpdatePayTempl) {
        this.needToUpdatePayTempl = needToUpdatePayTempl;
    }

    public boolean isNeedToUpdateTemplate() {
        return needToUpdateTemplate;
    }

    public void setNeedToUpdateTemplate(boolean needToUpdateTemplate) {
        this.needToUpdateTemplate = needToUpdateTemplate;
    }

    public boolean isNeedToUpdateHistory() {
        return needToUpdateHistory;
    }

    public void setNeedToUpdateHistory(boolean needToUpdateHistory) {
        this.needToUpdateHistory = needToUpdateHistory;
    }

    public AuthorizationResponse.User getUser() {
        return user;
    }

    public void setUser(AuthorizationResponse.User user) {
        this.user = user;
    }

    public UserAccounts getInvoiceAccountFrom() {
        return invoiceAccountFrom;
    }

    public void setInvoiceAccountFrom(UserAccounts invoiceAccountFrom) {
        this.invoiceAccountFrom = invoiceAccountFrom;
    }

    public ArrayList<UserAccounts.Cards> getAllCards() {
        return cards;
    }

    public ArrayList<UserAccounts.Cards> getVisibleCards() {
        ArrayList<UserAccounts.Cards> visibleCards = new ArrayList<>();
        for (UserAccounts.Cards card : getAllCards()) {
            if (card.isVisible) {
                visibleCards.add(card);
            }
        }
        return visibleCards;
    }

    public ArrayList<UserAccounts.CardAccounts> getVisibleCardAccounts() {
        ArrayList<UserAccounts.CardAccounts> visibleCardsAcc = new ArrayList<>();
        for (UserAccounts.CardAccounts cardAcc : getCardsAccounts()) {
            if (cardAcc.isVisible) {
                visibleCardsAcc.add(cardAcc);
            }
        }
        return visibleCardsAcc;
    }

    public static void setRegionSeleted(boolean sessionSucc) {
        isRegionSeleсted = sessionSucc;
    }

    public static void setUpdateAccList(boolean isUpdateAccL) {
        isUpdateAccList = isUpdateAccL;
    }

    public static boolean isRegionSeleted() {
        return isRegionSeleсted;
    }

    public static boolean isUpdateAccList() {
        return isUpdateAccList;
    }

    public ArrayList<UserAccounts.Cards> getWorkingCards() {
        ArrayList<UserAccounts.Cards> workingCards = new ArrayList<>();
        for (UserAccounts.Cards card : getAllCards()) {
            if (isWorking(card)) {
                workingCards.add(card);
            }
        }
        return workingCards;
    }

    public ArrayList<UserAccounts> getCardsForPayment(Context context) {
        ArrayList<UserAccounts> cards = new ArrayList<>();
        cards.add(new UserAccounts(context.getString(R.string.cards)));
        cards.addAll(getWorkingCards());
        return cards;
    }

    //CardAccounts, CheckingAccounts, DepositsAccounts, WishDeposits
    public ArrayList<UserAccounts> getChecking_Card_Deposits_Accounts(Context context) {
        ArrayList<UserAccounts> transferAccounts = new ArrayList<>();
        transferAccounts.addAll(getCardAndCheckingAccounts(context));
        transferAccounts.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
        transferAccounts.addAll(getWorkingDeposits());
        transferAccounts.add(new UserAccounts(context.getString(R.string.wish)));
        transferAccounts.addAll(getWorkingWishDeposits());
        return transferAccounts;
    }

    //cards, CardAccounts, CheckingAccounts, savingsAccount
    public ArrayList<UserAccounts> getAccountsForDebit(Context context) {
        ArrayList<UserAccounts> transferAccounts = new ArrayList<>();
        transferAccounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
        transferAccounts.addAll(getWorkingCards());
        transferAccounts.addAll(getCardAndCheckingAccounts(context));
        transferAccounts.add(new UserAccounts(context.getString(R.string.wish)));
        transferAccounts.addAll(getWorkingWishDeposits());
        return transferAccounts;
    }

    //cards, CardAccounts with blocked cards, CheckingAccounts, savingsAccount
    public ArrayList<UserAccounts> getAccountsForTransfer(Context context, boolean isNeedToAddDeposits, boolean isNeedExceptElcart) {
        ArrayList<UserAccounts> transferAccounts = new ArrayList<>();
        transferAccounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
        for (UserAccounts.Cards cards : getWorkingCards()) {
            if (cards.isDebit) {
                if (isNeedExceptElcart) {
                    if (cards.brandType != 1)
                        transferAccounts.add(cards);
                } else
                    transferAccounts.add(cards);
            }
        }
        for (UserAccounts.Cards cards : getCardsAttachedToCardAccounts()) {
            if (cards.isDebit) {
                if (isNeedExceptElcart) {
                    if (cards.brandType != 1)
                        transferAccounts.add(cards);
                } else
                    transferAccounts.add(cards);
            }
        }
        transferAccounts.add(new UserAccounts(context.getString(R.string.current_accounts)));
        for (UserAccounts.CheckingAccounts checkingAccount : getWorkingCheckingAccounts()) {
            if (checkingAccount.isDebit) {
                transferAccounts.add(checkingAccount);
            }
        }
        for (UserAccounts.CardAccounts cardAccounts : getWorkingCardAccountsWithoutWorkingCardOnly()) {
            if (cardAccounts.isDebit) {
                transferAccounts.add(cardAccounts);
            }
        }
        if (isNeedToAddDeposits) {
            transferAccounts.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
            for (UserAccounts.DepositAccounts depositAccounts : getWorkingDeposits()) {
                if (depositAccounts.isDebit) {
                    transferAccounts.add(depositAccounts);
                }
            }
        }
        return transferAccounts;
    }

    public ArrayList<UserAccounts> getVisaCards(Context context) {
        ArrayList<UserAccounts> transferAccounts = new ArrayList<>();
        transferAccounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
        for (UserAccounts.Cards cards : getWorkingCards()) {
            if (cards.isDebit) {
                if (cards.brandType != 1) // исключаем Элкарт карту
                    transferAccounts.add(cards);
            }
        }
        for (UserAccounts.Cards cards : getCardsAttachedToCardAccounts()) {
            if (cards.isDebit) {
                if (cards.brandType != 1)
                    transferAccounts.add(cards);
            }
        }
        return transferAccounts;
    }

    public ArrayList<UserAccounts> getElcardCards(Context context) {
        ArrayList<UserAccounts> transferAccounts = new ArrayList<>();
        transferAccounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
        for (UserAccounts.Cards cards : getWorkingCards()) {
            if (cards.isDebit) {
                if (cards.brandType == 1) // исключаем Элкарт карту
                    transferAccounts.add(cards);
            }
        }
        for (UserAccounts.Cards cards : getCardsAttachedToCardAccounts()) {
            if (cards.isDebit) {
                if (cards.brandType != 1)
                    transferAccounts.add(cards);
            }
        }
        return transferAccounts;
    }

    //cards, CardAccounts, savingsAccount
    public ArrayList<UserAccounts> getAccountsForDebitWithoutCardAcc(Context context, boolean isNeedExceptElcart) {
        ArrayList<UserAccounts> transferAccounts = new ArrayList<>();
        transferAccounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
        for (UserAccounts.Cards cards : getWorkingCards()) {
            if (cards.isCredit) {
                if (isNeedExceptElcart) {
                    if (cards.brandType != 1)
                        transferAccounts.add(cards);
                } else
                    transferAccounts.add(cards);
            }
        }
        for (UserAccounts.Cards cards : getCardsAttachedToCardAccounts()) {
            if (cards.isCredit) {
                if (isNeedExceptElcart) {
                    if (cards.brandType != 1)
                        transferAccounts.add(cards);
                } else
                    transferAccounts.add(cards);
            }
        }
        transferAccounts.add(new UserAccounts(context.getString(R.string.current_accounts)));
        for (UserAccounts.CheckingAccounts checkingAccount : getWorkingCheckingAccounts()) {
            if (checkingAccount.isCredit) {
                transferAccounts.add(checkingAccount);
            }
        }
        for (UserAccounts.CardAccounts cardAccounts : getWorkingCardAccountsWithoutWorkingCardOnly()) {
            if (cardAccounts.isCredit) {
                transferAccounts.add(cardAccounts);
            }
        }
        transferAccounts.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
        for (UserAccounts.DepositAccounts depositAccounts : getWorkingDeposits()) {
            if (depositAccounts.isCredit) {
                transferAccounts.add(depositAccounts);
            }
        }
        return transferAccounts;
    }

    //CardAccounts, CheckingAccounts
    private ArrayList<UserAccounts> getCardAndCheckingAccounts(Context context) {
        ArrayList<UserAccounts> cardAndCheckingAccounts = new ArrayList<>();
        cardAndCheckingAccounts.add(new UserAccounts(context.getString(R.string.current_accounts)));
        cardAndCheckingAccounts.addAll(getWorkingCardAccounts());
        cardAndCheckingAccounts.addAll(getWorkingCheckingAccounts());
        return cardAndCheckingAccounts;
    }

    //only KZT Checking, Cards Accounts
    public ArrayList<UserAccounts> getCheckingAndCardAccountKZT(Context context) {
        ArrayList<UserAccounts> checkingAccountKZT = new ArrayList<>();

//        checkingAccountKZT.add(new UserAccounts(context.getString(R.string.card_accounts)));
        //checkingAccountKZT.addAll(getWorkingCards());
//        for(UserAccounts.Cards card : getWorkingCards()) {
//            if(card.currency.equals("KGS")) {
//                checkingAccountKZT.add(card);
//            }
//        }

        checkingAccountKZT.add(new UserAccounts(context.getString(R.string.current_accounts)));
        /*for(UserAccounts.CardAccounts cardsAccount : getWorkingCardAccounts()) {
            if(cardsAccount.currency.equals("KZT")) {
                checkingAccountKZT.add(cardsAccount);
            }
        }*/

//        for(UserAccounts.CardAccounts cardsAccount : getWorkingCardAccountsWithoutWorkingCardOnly()) {
//            if(cardsAccount.currency.equals("KGS")) {
//                checkingAccountKZT.add(cardsAccount);
//            }
//        }

        for (UserAccounts.CheckingAccounts checkingAccount : getWorkingCheckingAccounts()) {
            if (checkingAccount.currency.equals("KGS") && checkingAccount.isDebit) {
                checkingAccountKZT.add(checkingAccount);
            }
        }

//        checkingAccountKZT.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
//        for(UserAccounts.DepositAccounts depositAccounts : getWorkingDeposits()) {
//            if(depositAccounts.currency.equals("KGS") && depositAccounts.isDebit) {
//                checkingAccountKZT.add(depositAccounts);
//            }
//        }
        return checkingAccountKZT;
    }

    public ArrayList<UserAccounts> getCheckingAndCardAccountNotKZT(Context context) {
        ArrayList<UserAccounts> checkingAccountKZT = new ArrayList<>();
        checkingAccountKZT.add(new UserAccounts(context.getString(R.string.current_accounts)));
        for (UserAccounts.CardAccounts cardsAccount : getWorkingCardAccounts()) {
            if (!cardsAccount.currency.equals("KGS")) {
                checkingAccountKZT.add(cardsAccount);
            }
        }
        for (UserAccounts.CheckingAccounts checkingAccount : getWorkingCheckingAccounts()) {
            if (!checkingAccount.currency.equals("KGS")) {
                checkingAccountKZT.add(checkingAccount);
            }
        }
        return checkingAccountKZT;
    }

    // Checking, Deposit Accounts and Cards
    public ArrayList<UserAccounts> getCardsAndChekingAndDepositAccounts(Context context, boolean isNeedExceptElcart) {
        ArrayList<UserAccounts> accounts = new ArrayList<>();

        accounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
        for (UserAccounts.Cards cards : getWorkingCards()) {
            if (cards.isCredit) {
                if (isNeedExceptElcart) {
                    if (cards.brandType != 1)
                        accounts.add(cards);
                } else
                    accounts.add(cards);
            }
        }
        for (UserAccounts.Cards cards : getCardsAttachedToCardAccounts()) {
            if (cards.isCredit) {
                if (isNeedExceptElcart) {
                    if (cards.brandType != 1)
                        accounts.add(cards);
                } else
                    accounts.add(cards);
            }
        }
        accounts.add(new UserAccounts(context.getString(R.string.current_accounts)));
        for (UserAccounts.CheckingAccounts checkingAccount : getWorkingCheckingAccounts()) {
            if (checkingAccount.isCredit) {
                accounts.add(checkingAccount);
            }
        }
        for (UserAccounts.CardAccounts cardAccounts : getWorkingCardAccountsWithoutWorkingCardOnly()) {
            if (cardAccounts.isCredit) {
                accounts.add(cardAccounts);
            }
        }
        accounts.add(new UserAccounts(context.getString(R.string.deposites)));
        for (UserAccounts.DepositAccounts depositAccounts : getWorkingDeposits()) {
            if (depositAccounts.isCredit) {
                accounts.add(depositAccounts);
            }
        }
        return accounts;
    }

    // Checking, Cards Accounts
    public ArrayList<UserAccounts> getAllCheckingAndCardAccount(Context context) {
        ArrayList<UserAccounts> checkingAccount = new ArrayList<>();
        checkingAccount.add(new UserAccounts(context.getString(R.string.card_accounts)));
        checkingAccount.addAll(getWorkingCards());
        checkingAccount.add(new UserAccounts(context.getString(R.string.current_accounts)));
//        checkingAccount.addAll(getWorkingCardAccounts());
        checkingAccount.addAll(getWorkingCheckingAccounts());
        return checkingAccount;
    }

    public void setCards(AccountsResponse accounts) {
        this.cards.clear();
        if (accounts != null) {
            this.cards.addAll(accounts.cards);
        }
    }

    public ArrayList<UserAccounts.CardAccounts> getCardsAccounts() {
        return cardsAccounts;
    }

    public ArrayList<UserAccounts.CardAccounts> getWorkingCardAccounts() {
        ArrayList<UserAccounts.CardAccounts> workingCardAccounts = new ArrayList<>();
        for (UserAccounts.CardAccounts cardAccount : getCardsAccounts()) {
            if (isWorking(cardAccount)) {
                workingCardAccounts.add(cardAccount);
            }
        }
        return workingCardAccounts;
    }

    public UserAccounts.CardAccounts getCardAccByCard(UserAccounts.Cards card) {
        for (UserAccounts.CardAccounts cardAccount : getCardsAccounts()) {
            if (cardAccount.code == card.cardAccountCode)
                return cardAccount;
        }
        return null;
    }

    public ArrayList<UserAccounts.CardAccounts> getWorkingCardAccountsWithoutWorkingCardOnly() {
        ArrayList<UserAccounts.CardAccounts> itogCardAccounts = new ArrayList<>();
        ArrayList<UserAccounts.CardAccounts> CardAccounts = new ArrayList<>();
        ArrayList<UserAccounts.Cards> workingCards = getWorkingCards();
        for (UserAccounts.CardAccounts cardAccount : getCardsAccounts()) {
            if (isWorking(cardAccount)) {
                for (UserAccounts.Cards card : workingCards) {
                    if (cardAccount.code == card.cardAccountCode) {
                        //continue;
                        CardAccounts.add(cardAccount);
                    }
                }
                if (!CardAccounts.contains(cardAccount)) {
                    itogCardAccounts.add(cardAccount);
                }
            }
        }
        return itogCardAccounts;
    }

    public void setCardsAccounts(AccountsResponse accounts) {
        this.cardsAccounts.clear();
        if (accounts != null) {
            for (UserAccounts.CardAccounts cardsAccount : accounts.cardsAccounts) {
                this.cardsAccounts.add(cardsAccount);
            }
        }
    }

    public ArrayList<UserAccounts.CheckingAccounts> getCheckingAccounts() {
        return checkingAccounts;
    }

    public ArrayList<UserAccounts.CheckingAccounts> getVisibleCheckingAccounts() {
        ArrayList<UserAccounts.CheckingAccounts> visibleCheckingAccounts = new ArrayList<>();
        ArrayList<UserAccounts.CheckingAccounts> checkingAccounts1 = getCheckingAccounts();
        for (UserAccounts.CheckingAccounts checkingAccounts : checkingAccounts1) {
            if (checkingAccounts.isVisible) {
                visibleCheckingAccounts.add(checkingAccounts);
            }
        }
        return visibleCheckingAccounts;
    }


    public ArrayList<UserAccounts> getVisibleCheckingAccountsNotKZT(Context context) {
        ArrayList<UserAccounts> visibleCheckingAccounts = new ArrayList<>();
        visibleCheckingAccounts.add(new UserAccounts(context.getString(R.string.current_accounts)));
        for (UserAccounts.CheckingAccounts checkingAccounts : getCheckingAccounts()) {
            if (checkingAccounts.isVisible && !checkingAccounts.currency.equals("KGS") && checkingAccounts.isDebit) {
                visibleCheckingAccounts.add(checkingAccounts);
            }
        }
        visibleCheckingAccounts.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
        for (UserAccounts.DepositAccounts depositAccounts : getWorkingDeposits()) {
            if (depositAccounts.currency.equals("KGS") && depositAccounts.isDebit) {
                visibleCheckingAccounts.add(depositAccounts);
            }
        }
        return visibleCheckingAccounts;
    }

    public ArrayList<UserAccounts.CheckingAccounts> getWorkingCheckingAccounts() {
        ArrayList<UserAccounts.CheckingAccounts> workingCheckingAccounts = new ArrayList<>();
        for (UserAccounts.CheckingAccounts cardAccount : getCheckingAccounts()) {
            if (isWorking(cardAccount)) {
                workingCheckingAccounts.add(cardAccount);
            }
        }
        return workingCheckingAccounts;
    }

    public void setCheckingAccounts(AccountsResponse accounts) {
        this.checkingAccounts.clear();
        if (accounts != null) {
            for (UserAccounts.CheckingAccounts checkingAccount : accounts.checkingAccounts) {
                this.checkingAccounts.add(checkingAccount);
            }
        }
    }

    public ArrayList<UserAccounts> getCheckingAndDepositAccount(Context context) {
        ArrayList<UserAccounts> checkingAndDepositAccount = new ArrayList<>();
        checkingAndDepositAccount.add(new UserAccounts(context.getString(R.string.current_accounts)));
        for (UserAccounts.CheckingAccounts checkingAccount : getWorkingCheckingAccounts()) {
            if (checkingAccount.isCredit) {
                checkingAndDepositAccount.add(checkingAccount);
            }
        }
        checkingAndDepositAccount.add(new UserAccounts(context.getString(R.string.deposites)));
        for (UserAccounts.DepositAccounts depositAccounts : getWorkingDeposits()) {
            if (depositAccounts.isCredit) {
                checkingAndDepositAccount.add(depositAccounts);
            }
        }
        return checkingAndDepositAccount;
    }

    public ArrayList<UserAccounts> getCardsForCredit(Context context) {
        ArrayList<UserAccounts> cardsToCredit = new ArrayList<>();
        cardsToCredit.add(new UserAccounts(context.getString(R.string.card_accounts)));
        for (UserAccounts.Cards checkingAccount : getWorkingCards()) {
            if (checkingAccount.isCredit) {
                cardsToCredit.add(checkingAccount);
            }
        }
        return cardsToCredit;
    }


    public ArrayList<UserAccounts.Cards> getCardsAttachedToCardAccounts() {
        ArrayList<UserAccounts.Cards> itogCards = new ArrayList<>();
        for (UserAccounts.CardAccounts cardAccount : getWorkingCardAccounts()) {
            for (UserAccounts.Cards attachedCard : getWorkingCards()) {
                Log.i("getWorkingCardAc", "cardAccount.code = " + cardAccount.code);
                Log.i("getWorkingCardAc", "card.cardAccountCode = " + attachedCard.cardAccountCode);
                if (cardAccount.code == attachedCard.cardAccountCode) {
                    Log.i("getWorkingCards", " ==== ");
                    if (!getWorkingCards().contains(attachedCard)) {
                        itogCards.add(attachedCard);
                    }
                }
            }
        }
        return itogCards;
    }

    public ArrayList<UserAccounts.CreditAccounts> getCredit() {
        return credit;
    }

    public ArrayList<UserAccounts.CreditAccounts> getVisibleCredit() {
        ArrayList<UserAccounts.CreditAccounts> credits = new ArrayList<>();
        for (UserAccounts.CreditAccounts creditAccounts : getCredit()) {
            if (creditAccounts.isVisible) {
                credits.add(creditAccounts);
            }
        }
        return credits;
    }

    public void setCredit(AccountsResponse accounts) {
        this.credit.clear();
        if (accounts != null) {
            this.credit.addAll(accounts.credit);
        }
    }

    public ArrayList<UserAccounts.DepositAccounts> getDeposits() {
        return deposits;
    }

    public ArrayList<UserAccounts.DepositAccounts> getVisibleDeposits() {
        ArrayList<UserAccounts.DepositAccounts> deposits = new ArrayList<>();
        for (UserAccounts.DepositAccounts depositAccounts : getDeposits()) {
            if (depositAccounts.isVisible) {
                deposits.add(depositAccounts);
            }
        }
        return deposits;
    }

    public ArrayList<UserAccounts.DepositAccounts> getWorkingDeposits() {
        ArrayList<UserAccounts.DepositAccounts> workingDeposits = new ArrayList<>();
        for (UserAccounts.DepositAccounts deposit : getDeposits()) {
            if (isWorking(deposit)) {
                workingDeposits.add(deposit);
            }
        }
        return workingDeposits;
    }

    public ArrayList<UserAccounts.WishAccounts> getWorkingWishDeposits() {
        ArrayList<UserAccounts.WishAccounts> workingWishDeposits = new ArrayList<>();
        for (UserAccounts.WishAccounts wishDeposit : getWishDeposits()) {
            if (isWorking(wishDeposit) && wishDeposit.isWithDrawal) {
                workingWishDeposits.add(wishDeposit);
            }
        }
        return workingWishDeposits;
    }

    private ArrayList<UserAccounts> getAllWorkingAccounts() {
        ArrayList<UserAccounts> allWorkingAccounts = new ArrayList<>();
        allWorkingAccounts.addAll(getWorkingCards());
        allWorkingAccounts.addAll(getWorkingCardAccounts());
        allWorkingAccounts.addAll(getWorkingCheckingAccounts());
        allWorkingAccounts.addAll(getWorkingDeposits());
        allWorkingAccounts.addAll(getWorkingWishDeposits());
        allWorkingAccounts.addAll(getEncryptedCards());
        return allWorkingAccounts;
    }

    public void setDeposits(AccountsResponse accounts) {
        this.deposits.clear();
        if (accounts != null) {
            for (UserAccounts.DepositAccounts depositAccount : accounts.deposits) {
                this.deposits.add(depositAccount);
            }
        }
    }

    public void setMobilePayment(boolean isMobilePayment){
        this.isMobilePayment = isMobilePayment;
    }

    public boolean getMobilePayment(){
        return isMobilePayment;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.category.clear();
        if (categories != null) {
            for (Category category : categories) {
                this.category.add(category);
            }
        }
    }

    public ArrayList<Category> getCategories() {
        return category;
    }

    private ArrayList<UserAccounts.WishAccounts> getWishDeposits() {
        return wishDeposits;
    }

    public ArrayList<UserAccounts.EncryptedCard> getEncryptedCards() {
        return encryptedCards;
    }

    public ArrayList<UserAccounts.WishAccounts> getVisibleWishDeposits() {
        ArrayList<UserAccounts.WishAccounts> deposits = new ArrayList<>();
        for (UserAccounts.WishAccounts wishAccounts : getWishDeposits()) {
            if (wishAccounts.isVisible) {
                deposits.add(wishAccounts);
            }
        }
        return deposits;
    }

    public void setWishDeposits(AccountsResponse accounts) {
        this.wishDeposits.clear();
        if (accounts != null) {
            wishDeposits.addAll(accounts.wishDeposits);
        }
    }

    public void setEncryptedCards(AccountsResponse accounts) {
        this.encryptedCards.clear();
        if (accounts != null) {
            encryptedCards.addAll(accounts.encryptedCard);
        }
    }

    public ArrayList<Terminal> getAtms() {
        ArrayList<Terminal> ats = new ArrayList<>();
        //if(atms!=null&&!atms.isEmpty()) {
        //    int regionId = getRegionID();
        //    for(Terminal points : atms){
        //        if(points.getBranchCode().equals(String.valueOf(regionId)) || regionId==0) {
        //            ats.add(points);
        //        }
        //    }
        //}
        //return ats;
        return atms;
    }

    public void setAtms(ArrayList<Terminal> servicePoints) {
        atms.clear();
        if (servicePoints != null)
            atms.addAll(servicePoints);
    }

    public ArrayList<Terminal> getTerminals() {
        return terminals;
    }

    public void setTerminals(ArrayList<Terminal> servicePoints) {
        terminals.clear();
        if (servicePoints != null)
            terminals.addAll(servicePoints);
    }

    public ArrayList<Terminal> getBranches() {
        ArrayList<Terminal> brans = new ArrayList<>();
        //if(branches!=null&&!branches.isEmpty()) {
        //    int regionId = getRegionID();
        //    for(Terminal points : branches){
        //        if(points.getBranchCode().equals(String.valueOf(regionId)) || regionId==0) {
        //            brans.add(points);
        //        }
        //    }
        //}
        //return brans;
        return branches;
    }

    public void setBranches(ArrayList<Terminal> servicePoints) {
        branches.clear();
        if (servicePoints != null)
            branches.addAll(servicePoints);
    }

    public ArrayList<NewsItem> getNews() {
        Collections.sort(news, new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                Log.i("LOGICOMPARE", "o2.getPublishDate()= " + o2.getPublishDate());
                return o2.getPublishDateDATE().compareTo(o1.getPublishDateDATE());
            }
        });
        return news;
    }

    public void setNews(final ArrayList<NewsItem> news) {//15.12.2017
        //Collections.sort(news, new Comparator<NewsItem>() {
        //    @Override
        //    public int compare(NewsItem o1, NewsItem o2) {
        //        Log.i("LOGICOMPARE","o2.getPublishDate()= "+o2.getPublishDate());
        //        return o1.getPublishDateDATE().compareTo(o2.getPublishDateDATE());
        //    }
        //});
        //Collections.reverse(news);
        //Log.i("LOGICOMPARE","news.get(1).getPublishDate()= "+news.get(1).getPublishDate());
        this.news = news;
    }

    public ArrayList<Rate> getRate() {
        return rate;
    }

    public ArrayList<TemplatesPayment> getTemplatesPayment() {
        return templatesPayment;
    }

    public void setTemplatesPayment(PaymentTemplateResponse templatesPayment) {
        this.templatesPayment.clear();
        if (templatesPayment != null) {
            for (TemplatesPayment template : templatesPayment.templatesPayments) {
                //if(!template.isAutoPay) {
                this.templatesPayment.add(template);
                //}
            }
        }
    }

    public ArrayList<TemplateTransfer> getTemplatesTransfer() {
        return templatesTransfer;
    }

    public void setTemplatesTransfer(ArrayList<TemplateTransfer> templatesTransfer) {
        this.templatesTransfer.clear();
        if (templatesTransfer != null) {
            for (TemplateTransfer templateTransfer : templatesTransfer) {
                //if(!templateTransfer.isStandingInstruction()) {
                this.templatesTransfer.add(templateTransfer);
                //}
            }
        }
    }

    public boolean hasVisibleAccounts() {
        ArrayList<UserAccounts> allVis = new ArrayList<>();
        boolean isAllInv = false;
        allVis.addAll(getAllCards());
        allVis.addAll(getWishDeposits());
        allVis.addAll(getCredit());
        allVis.addAll(getDeposits());
        allVis.addAll(getCardsAccounts());
        allVis.addAll(getCheckingAccounts());
        Log.i("getBoolInvisAcc", "allVis.size() = " + allVis.size());

        for (UserAccounts userAccounts : allVis) {
            if (userAccounts.isVisible) {
                isAllInv = true;
            }
            Log.i("getBoolInvisAcc", "userAccounts.name = " + userAccounts.name);
        }
        if (allVis.size() == 0)
            isAllInv = true;
        return isAllInv;
    }

    public ArrayList<UserAccounts> getThreeAll(Context context) {

        //objects.add(Constants.HEADER_SPACING);
        //objects.add(new Templates(Constants.FOOTER_ID, OptimaBank.getInstance().getString(R.string.show_all)));

        ArrayList<UserAccounts> objects = new ArrayList<>();

        ArrayList<UserAccounts.Cards> cards = new ArrayList<>();
        cards.addAll(getVisibleCards());
        ArrayList<UserAccounts.CheckingAccounts> checkingAccountses = new ArrayList<>();
        checkingAccountses.addAll(getVisibleCheckingAccounts());
        ArrayList<UserAccounts.DepositAccounts> depositAccountses = new ArrayList<>();
        depositAccountses.addAll(getVisibleDeposits());

        //ArrayList<UserAccounts.CardAccounts> cardAccountses = new ArrayList<>();
        //cardAccountses.addAll(getVisibleCardAccounts());

        ArrayList<UserAccounts.WishAccounts> wishAccountses = new ArrayList<>();
        wishAccountses.addAll(getVisibleWishDeposits());
        ArrayList<UserAccounts.CreditAccounts> creditAccountses = new ArrayList<>();
        creditAccountses.addAll(getVisibleCredit());

        //objects.add(new UserAccounts((Constants.HEADER_SPACING)));
        try {
            if (cards.size() > 0) {
                objects.add(new UserAccounts(context.getString(R.string.card_accounts)));
                for (int i = 0; i < 3; i++) {
                    objects.add(cards.get(i));
                    Log.i("BAC1", "card balance = " + cards.get(i).balance);
                    Log.i("BAC1", "card isMultiBalance = " + cards.get(i).isMultiBalance);
                    if (cards.get(i).isMultiBalance) {
                        for (UserAccounts.Cards.MultiBalanceList asd : cards.get(i).multiBalanceList)
                            Log.i("BAC1", "card isMultiBalance amount = " + asd.amount + "  " + asd.currency);
                    }
                }
                if (cards.size() > 3)
                    objects.add(new UserAccounts(context.getString(R.string.show_all), Constants.FOOTER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (checkingAccountses.size() > 0) {
                objects.add(new UserAccounts(context.getString(R.string.current_accounts_tek)));
                for (int i = 0; i < 3; i++) {
                    objects.add(checkingAccountses.get(i));
                }
                if (checkingAccountses.size() > 3)
                    objects.add(new UserAccounts(context.getString(R.string.show_all), Constants.FOOTER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //try {
        //    if (cardAccountses.size()>0) {
        //        objects.add(new UserAccounts(OptimaBank.getInstance().getString(R.string.current_cards_accounts)));
        //        for (int i = 0; i < 3; i++) {
        //            objects.add(cardAccountses.get(i));
        //        }
        //        if (cardAccountses.size() > 3)
        //            objects.add(new UserAccounts(OptimaBank.getInstance().getString(R.string.show_all), Constants.FOOTER_ID));
        //    }
        //}catch (Exception e){e.printStackTrace();}

        try {
            if (depositAccountses.size() > 0) {
                objects.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
                for (int i = 0; i < 3; i++) {
                    objects.add(depositAccountses.get(i));
                }
                if (depositAccountses.size() > 3)
                    objects.add(new UserAccounts(context.getString(R.string.show_all), Constants.FOOTER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (wishAccountses.size() > 0) {
                objects.add(new UserAccounts(context.getString(R.string.wish)));
                for (int i = 0; i < 3; i++) {
                    objects.add(wishAccountses.get(i));
                }
                if (wishAccountses.size() > 3)
                    objects.add(new UserAccounts(context.getString(R.string.show_all), Constants.FOOTER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (creditAccountses.size() > 0) {
                objects.add(new UserAccounts(context.getString(R.string.loan_accounts)));
                for (int i = 0; i < 3; i++) {
                    objects.add(creditAccountses.get(i));
                }
                if (creditAccountses.size() > 3)
                    objects.add(new UserAccounts(context.getString(R.string.show_all), Constants.FOOTER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    private ArrayList<Templates> getLastTwoTemplatesTransfer() {
        ArrayList<Templates> templates = new ArrayList<>();
        templates.addAll(getTemplatesTransfer());
        try {
            if (!templates.isEmpty()) {
                Collections.sort(templates, new Comparator<Templates>() {
                    @Override
                    public int compare(Templates o1, Templates o2) {
                        return o2.createDate.compareTo(o1.createDate);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!templates.isEmpty() && templates.size() > 2) {
            ArrayList<Templates> lastFiveTemp = new ArrayList<>();
            lastFiveTemp.addAll(templates.subList(0, 2));
            return lastFiveTemp;
        }
        return templates;
    }

    public ArrayList<Templates> getLastTwoPaymentsTemplates() {
        ArrayList<Templates> templates = new ArrayList<>();
        templates.addAll(getTemplatesPayment());
        try {
            if (!templates.isEmpty()) {
                Collections.sort(templates, new Comparator<Templates>() {
                    @Override
                    public int compare(Templates o1, Templates o2) {
                        return o2.createDate.compareTo(o1.createDate);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!templates.isEmpty() && templates.size() > 2) {
            ArrayList<Templates> lastFiveTemp = new ArrayList<>();
            lastFiveTemp.addAll(templates.subList(0, 2));
            return lastFiveTemp;
        }
        return templates;
    }

    private ArrayList<Templates> getLastFiveTemplatesPayment() {
        ArrayList<Templates> templates = new ArrayList<>();
        templates.addAll(getTemplatesTransfer());
        templates.addAll(getTemplatesPayment());
        try {
            if (!templates.isEmpty()) {
                Collections.sort(templates, new Comparator<Templates>() {
                    @Override
                    public int compare(Templates o1, Templates o2) {
                        return o2.createDate.compareTo(o1.createDate);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!templates.isEmpty() && templates.size() > 5) {
            ArrayList<Templates> lastFiveTemp = new ArrayList<>();
            lastFiveTemp.addAll(templates.subList(0, 5));
            return lastFiveTemp;
        }
        return templates;
    }

//    private ArrayList<TemplateTransfer> getLastTwoTemplatesTransfer() {
//        ArrayList<TemplateTransfer> lastTwoTemp = new ArrayList<>();
//        if(getTemplatesTransfer()!=null && !getTemplatesTransfer().isEmpty()) {
//            int size;
//            if(getTemplatesTransfer().size() < 2) {
//                size = getTemplatesTransfer().size()+1;
//            } else {
//                size = 3;
//            }
//            for (int i = 1; i < size; i++) {
//                TemplateTransfer templatesTransfer = getTemplatesTransfer().get(getTemplatesTransfer().size() - i);
//                if (templatesTransfer != null && !templatesTransfer.StandingInstruction) {
//                    lastTwoTemp.add(templatesTransfer);
//                }
//            }
//        }
//        return lastTwoTemp;
//    }

    public void setRate(ArrayList<Rate> rate) {
        this.rate = rate;
    }

    public ArrayList<ATFStatement> getStatements() {
        return statements;
    }

    public ArrayList<ATFStatement> getKZTStatements() {
        ArrayList<ATFStatement> statementList = new ArrayList<>();
        for (ATFStatement statement : statements) {
            if (statement.currency.equals("KGS")) {
                statementList.add(statement);
            } else {
                Log.i("PARA", "PARA");
            }
        }
        return statementList;
    }

    public void setStatements(ArrayList<ATFStatement> statements) {
        this.statements = statements;
    }

    public ArrayList<ATFStatement> getStatementsTwoWeeksAccount() {
        return statementsTwoWeeksAccount;
    }

    public void setStatementsTwoWeeksAccount(ArrayList<ATFStatement> statementsTwoWeeksAccount) {
        this.statementsTwoWeeksAccount = statementsTwoWeeksAccount;
    }

    public ArrayList<ATFStatement> getStatementsAccount() {
        return statementsAccount;
    }

    public void setStatementsAccount(ArrayList<ATFStatement> statementsTwoWeeksAccount) {
        this.statementsAccount = statementsTwoWeeksAccount;
    }

    public ArrayList<HistoryItem.PaymentHistoryItem> getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(ArrayList<HistoryItem.PaymentHistoryItem> paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public ArrayList<Limit> getLimit() {
        return limit;
    }

    public void setLimit(ArrayList<Limit> limit) {
        this.limit = limit;
    }

    public ArrayList<HistoryItem.TransferHistoryItem> getTransferHistory() {
        return transferHistory;
    }

    public void setTransferHistory(ArrayList<HistoryItem.TransferHistoryItem> transferHistory) {
        this.transferHistory = transferHistory;
    }

    public UserAccounts.Cards getCardByAccountCode(int code) {
        for (UserAccounts.Cards card : getWorkingCards()) {
            if (card.cardAccountCode == code) {
                return card;
            }
        }
        return null;
    }

    public UserAccounts.Cards getCardByRbs(String rbs) {
        for (UserAccounts.Cards card : getWorkingCards()) {
            if (card.rbsNumber.equals(rbs)) {
                return card;
            }
        }
        return null;
    }

    public UserAccounts.Cards getCardByCode(int code) {
        for (UserAccounts.Cards card : getWorkingCards()) {
            if (card.code == code) {
                return card;
            }
        }
        return null;
    }

    public ArrayList<UserAccounts.Cards> getEncryptPossibilityCards() {
        ArrayList<UserAccounts.Cards> cards = new ArrayList<>();
        for (UserAccounts.Cards card : getWorkingCards()) {
            if (card.encryptPossibility) {
                cards.add(card);
            }
        }
        return cards;
    }

    public UserAccounts getAccountByCode(int code) {
        for (UserAccounts acc : getAllWorkingAccounts()) {
            if (acc.code == code) {
                return acc;
            }
        }
        return null;
    }

    public UserAccounts.CheckingAccounts getCheckingAccounts(String id) {
        for (UserAccounts.CheckingAccounts cacc : getCheckingAccounts()) {
            if (cacc.id.equals(id)) {
                return cacc;
            }
        }
        return null;
    }

    public UserAccounts.CardAccounts getCardAccountByCode(int cardAccountCode) {
        for (UserAccounts.CardAccounts acc : getWorkingCardAccounts()) {
            if (acc.code == cardAccountCode) {
                return acc;
            }
        }
        return null;
    }

    private boolean isWorking(UserAccounts account) {
        return !account.isBlocked && !account.isClosed && account.isVisible;
    }


    public JSONArray getPaymentParameters() {
        return paymentParameters;
    }

    public void setPaymentParameters(JSONArray paymentParameters) {
        this.paymentParameters = paymentParameters;
    }

    public String getTransferDocumentId() {
        return transferDocumentId;
    }

    public void setTransferDocumentId(String transferDocumentId) {
        this.transferDocumentId = transferDocumentId;
    }

    public HashMap<String, Terminal> getTerminalHashMap() {
        return terminalHashMap;
    }

    public void setTerminalHashMap(HashMap<String, Terminal> terminalHashMap) {
        this.terminalHashMap = terminalHashMap;
    }

    public ArrayList<LoanScheduleResponse> getLoanScheduleResponses() {
        return loanScheduleResponses;
    }

    public void setLoanScheduleResponses(ArrayList<LoanScheduleResponse> loanScheduleResponses) {
        this.loanScheduleResponses = loanScheduleResponses;
    }

    public ArrayList<Object> getAllTemplatesAndPaymentsAndTransfers(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        ArrayList<Templates> templates = new ArrayList<>();
        templates.addAll(getTemplatesPayment());
        templates.addAll(getTemplatesTransfer());

        if (!templates.isEmpty()) {
            objects.add(new Templates(Constants.HEADER_ID, context.getString(R.string.templates)));
            objects.addAll(templates);
            objects.add(new Templates(Constants.FOOTER_ID, context.getString(R.string.show_all)));
        }

        objects.add(new TransferModel(Constants.HEADER_ID, context.getString(R.string.transfer_accounts)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift_tenge)));

        objects.add(new PaymentCategory(Constants.HEADER_ID, context.getString(R.string.payments)));
        objects.addAll(PaymentContextController.getController().getPaymentCategory(context));
        return objects;
    }

    public ArrayList<Object> getTransfersAndPayments(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(Constants.HEADER_SPACING);

        ArrayList<Templates> templates = new ArrayList<>();
        templates.addAll(getLastFiveTemplatesPayment());

        if (!templates.isEmpty()) {
            objects.add(new Templates(Constants.HEADER_ID, context.getString(R.string.templates)));
            objects.addAll(templates);
            objects.add(new Templates(Constants.FOOTER_ID, context.getString(R.string.show_all)));
        }

        objects.add(new TransferModel(Constants.HEADER_ID, context.getString(R.string.transfer_accounts)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift_tenge)));
//        templates.add(new TransferModel(Constants.ITEM_ID,OptimaBank.getInstance().getString(R.string.transfer_swift)));

        objects.add(new PaymentCategory(Constants.HEADER_ID, context.getString(R.string.payments)));
        objects.addAll(PaymentContextController.getController().getAllPaymentCategory());
//        objects.add(new PaymentCategory(Constants.ITEM_ID, OptimaBank.getInstance().getString(R.string.other)));
        return objects;
    }

    public ArrayList<Object> getTransfersAndTwoTransTemplate(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        //objects.add(Constants.HEADER_SPACING);

        ArrayList<Templates> templates = new ArrayList<>(getTemplatesTransfer());

//        if(!templates.isEmpty()) {
//            objects.add(new Templates(Constants.HEADER_ID, context.getString(R.string.templates)));
//            objects.addAll(templates);
//            objects.add(new Templates(Constants.FOOTER_ID, context.getString(R.string.show_all)));
//        }

        try {
            if (!templates.isEmpty()) {
                Collections.sort(templates, new Comparator<Templates>() {
                    @Override
                    public int compare(Templates o1, Templates o2) {
                        return o2.createDate.compareTo(o1.createDate);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!templates.isEmpty() && templates.size() >= 1) {
            objects.add(new Templates(Constants.HEADER_ID, context.getString(R.string.templates)));
            if (templates.size() == 1) {
                objects.addAll(templates.subList(0, 1));
            } else if (templates.size() == 2) {
                objects.addAll(templates.subList(0, 2));
            } else if (templates.size() > 2) {
                objects.addAll(templates.subList(0, 2));
                objects.add(new Templates(Constants.FOOTER_ID, context.getString(R.string.show_all)));
            }
        }
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card_visa_to_visa_for_item)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card_elcard_to_elcard_for_item)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_phone_number)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift_tengeN)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift)));
        return objects;
    }

    public ArrayList<TransferModel> getTransfersWithoutHead(Context context) {
        ArrayList<TransferModel> transferModels = new ArrayList<>();

        transferModels.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card)));
        transferModels.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card_visa_to_visa_for_item)));
        transferModels.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_phone_number)));
        transferModels.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift_tenge)));
        transferModels.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift)));
        return transferModels;
    }

    public ArrayList<Object> getPaymentsCategoryAndTwoPaymentTemplate(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        //objects.add(Constants.HEADER_SPACING);

        ArrayList<Templates> templates = new ArrayList<>();
        templates.addAll(getLastTwoPaymentsTemplates());

        if (!templates.isEmpty()) {
            objects.add(new Templates(Constants.HEADER_ID, context.getString(R.string.templates)));
            objects.addAll(templates);
            objects.add(new Templates(Constants.FOOTER_ID, context.getString(R.string.show_all)));
        }


        objects.addAll(PaymentContextController.getController().getAllPaymentCategory());
        return objects;
    }

    public ArrayList<Templates> getTwoPaymentsTemplateWithHeaders(Context context) {
        ArrayList<Templates> lastFiveTemp = new ArrayList<>();
        ArrayList<TemplatesPayment> templates = new ArrayList<>(getTemplatesPayment());

        try {
            if (!templates.isEmpty()) {
                Collections.sort(templates, new Comparator<Templates>() {
                    @Override
                    public int compare(Templates o1, Templates o2) {
                        return o2.createDate.compareTo(o1.createDate);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!templates.isEmpty() && templates.size() >= 1) {
            lastFiveTemp.add(new Templates(Constants.HEADER_ID, context.getString(R.string.templates)));
            if (templates.size() == 1) {
                lastFiveTemp.addAll(templates.subList(0, 1));
            } else if (templates.size() == 2) {
                lastFiveTemp.addAll(templates.subList(0, 2));
            } else if (templates.size() > 2) {
                lastFiveTemp.addAll(templates.subList(0, 2));
                lastFiveTemp.add(new Templates(Constants.FOOTER_ID, context.getString(R.string.show_all)));
            }
        }
        return lastFiveTemp;
    }

    public ArrayList<Object> getAllPayments(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(PaymentContextController.getController().getAllPaymentCategory());
        return objects;
    }

    public ArrayList<Object> getTransfers(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new TransferModel(Constants.HEADER_ID, context.getString(R.string.transfer_accounts)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift_tenge)));
        return objects;
    }

    public ArrayList<Object> getTransfersForInCurr(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new TransferModel(Constants.HEADER_ID, context.getString(R.string.transfer_accounts)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_card)));
        objects.add(new TransferModel(Constants.ITEM_ID, context.getString(R.string.transfer_swift)));
        return objects;
    }

    public ArrayList<Object> getPayments(Context context) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new PaymentCategory(Constants.HEADER_ID, context.getString(R.string.payments)));
        objects.addAll(PaymentContextController.getController().getPaymentCategory(context));
        return objects;
    }

    public ArrayList<String> getProfileList(Context context) {
        ArrayList<String> items = new ArrayList<>();
        //items.add(context.getString(R.string.personal_data));
        //items.add(context.getString(R.string.notice));
        //items.add(context.getString(R.string.applications));
        items.add(context.getString(R.string.settings_title));
//        items.add(context.getString(R.string.contact_bank));
        //items.add(context.getString(R.string.exiting_application));
        return items;
    }

    public ArrayList<SettingsModel> getSettingsList(Context context) {
        ArrayList<SettingsModel> items = new ArrayList<>();
        items.add(new SettingsModel(Constants.HEADER_ID, context.getString(R.string.general_settings)));
        items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.SMS_alert)));
        items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.visibility_account)));
        //items.add(new SettingsModel(Constants.ITEM_ID,context.getString(R.string.region)));
        items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.InterfaceLanguage)));

        items.add(new SettingsModel(Constants.HEADER_ID, context.getString(R.string.security)));
        items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.change_password)));
        return items;
    }

    /**
     *  Метод для списков отображения настроек, для отпределенной карты
     * @param context контекст приложения
     * @param isDefaultCard если true то карта Visa, если false то карта Элкарт
     * @return возвращает список настройки карты
     */
    public ArrayList<SettingsModel> getSettingsCardList(Context context, boolean isDefaultCard) {
        ArrayList<SettingsModel> items = new ArrayList<>();
        if (isDefaultCard) {
            items.add(new SettingsModel(Constants.HEADER_ID, context.getString(R.string.AboutCard)));
            items.add(new SettingsModel(Constants.CARD_SMSINFO, context.getString(R.string.SMS_alert)));
            //items.add(new SettingsModel(Constants.ITEM_ID,context.getString(R.string.alert_info)));
            //items.add(new SettingsModel(Constants.ITEM_ID,context.getString(R.string.requisites)));
            //items.add(new SettingsModel(Constants.ITEM_ID,context.getString(R.string.limits_info)));

            items.add(new SettingsModel(Constants.HEADER_ID, context.getString(R.string.Limits)));
            items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.Limits)));
//        items.add(new SettingsModel(Constants.ITEM_ID,context.getString(R.string.Cash_withdrawal)));
            items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.Internet_payments)));
//        items.add(new SettingsModel(Constants.ITEM_ID,context.getString(R.string.pos_terminal_pay)));

            items.add(new SettingsModel(Constants.HEADER_ID, context.getString(R.string.security)));
            items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.Card_lock)));
        } else {
            items.add(new SettingsModel(Constants.HEADER_ID, context.getString(R.string.security)));
            items.add(new SettingsModel(Constants.ITEM_ID, context.getString(R.string.Card_lock)));
        }
        return items;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public ArrayList<UserAccounts> getAccounts(Context context) {
        ArrayList<UserAccounts> accounts = new ArrayList<>();
        ArrayList<UserAccounts.Cards> cards = getVisibleCards();

        ArrayList<UserAccounts> cardsAndCheckingAccounts = new ArrayList<>();
//        cardsAndCheckingAccounts.addAll(getCardsAccounts());
        cardsAndCheckingAccounts.addAll(getVisibleCheckingAccounts());

        ArrayList<UserAccounts.DepositAccounts> depositAccounts = getVisibleDeposits();
        ArrayList<UserAccounts.WishAccounts> wishAccounts = getVisibleWishDeposits();
        ArrayList<UserAccounts.CreditAccounts> creditAccounts = getVisibleCredit();

        if (!cards.isEmpty() || !cardsAndCheckingAccounts.isEmpty() || !depositAccounts.isEmpty()
                || !wishAccounts.isEmpty() || !creditAccounts.isEmpty()) {
            accounts.add(new UserAccounts(Constants.HEADER_SPACING));
        }

        if (!cards.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.card_accounts)));
            accounts.addAll(cards);
        }
        if (!cardsAndCheckingAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.current_accounts)));
            accounts.addAll(cardsAndCheckingAccounts);
        }
        if (!depositAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
            accounts.addAll(depositAccounts);
        }

        if (!wishAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.wish)));
            accounts.addAll(wishAccounts);
        }

        if (!creditAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.loan_accounts)));
            accounts.addAll(creditAccounts);
        }

        return accounts;
    }

    public ArrayList<UserAccounts> getAllFullAccounts() {
        ArrayList<UserAccounts> accounts = new ArrayList<>();

        ArrayList<UserAccounts.Cards> cards = getAllCards();
        ArrayList<UserAccounts.CheckingAccounts> cardsAndCheckingAccounts = getCheckingAccounts();
        ArrayList<UserAccounts.CardAccounts> cardsAccounts = getCardsAccounts();

        ArrayList<UserAccounts.DepositAccounts> depositAccounts = getDeposits();
        ArrayList<UserAccounts.WishAccounts> wishAccounts = getWishDeposits();
        ArrayList<UserAccounts.CreditAccounts> creditAccounts = getCredit();

        accounts.addAll(cards);
        accounts.addAll(cardsAndCheckingAccounts);
        accounts.addAll(cardsAccounts);
        accounts.addAll(depositAccounts);
        accounts.addAll(wishAccounts);
        accounts.addAll(creditAccounts);

        return accounts;
    }

    public ArrayList<UserAccounts> getAllAccounts(Context context) {
        ArrayList<UserAccounts> accounts = new ArrayList<>();

        ArrayList<UserAccounts> cardsAndCheckingAccounts = new ArrayList<>();
        cardsAndCheckingAccounts.addAll(getCheckingAccounts());
        ArrayList<UserAccounts> cardsAccounts = new ArrayList<>();
        cardsAccounts.addAll(getCardsAccounts());

        ArrayList<UserAccounts.DepositAccounts> depositAccounts = getDeposits();
        ArrayList<UserAccounts.CreditAccounts> creditAccounts = getCredit();

        if (!cardsAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.current_cards_accounts_cards)));
            accounts.addAll(cardsAccounts);
        }
        if (!cardsAndCheckingAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.current_accounts_tek)));
            accounts.addAll(cardsAndCheckingAccounts);
        }
        if (!depositAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.deposit_accounts)));
            accounts.addAll(depositAccounts);
        }

        if (!creditAccounts.isEmpty()) {
            accounts.add(new UserAccounts(context.getString(R.string.loan_accounts)));
            accounts.addAll(creditAccounts);
        }

        return accounts;
    }

    public boolean isInvoiceCheckedStatus() {
        return isInvoiceCheckedStatus;
    }

    public void setInvoiceCheckedStatus(boolean invoiceCheckedStatus) {
        isInvoiceCheckedStatus = invoiceCheckedStatus;
    }

    public ArrayList<Invoices> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<Invoices> invoices) {
        this.invoices = invoices;
    }

    public String getAuthorizationHeaders(Context context) {
        String headers = null;
        DHKeyController dhKeyController = DHKeyController.getController();
        byte[] key = dhKeyController.getKey();
        Log.d("PTKC", "--------------- ================== " + key);
        Log.d("PTKC", "key = " + key);
        if (key != null && key.length > 0) {
            final String device_secret = Base64.encodeToString(key, Base64.NO_WRAP);
            final String salt = "4Gw8hoCQYPhhM5QS";
            long time = System.currentTimeMillis();
            String deviceTokenPush = getDeviceTokenPush(context);
            Log.d("PTKC", "device_secret = " + device_secret);
            Log.d("PTKC", "getDeviceTokenPush(context) = " + getDeviceTokenPush(context));
            try {
                headers = CryptoUtils.generateToken(deviceTokenPush, device_secret, time, salt);
            } catch (Exception e) {
                Log.d("PTKC", "Exception getAuthorizationHeaders = " + e);
            }
            Log.d("PTKC", "headers = " + headers);
            dhKeyController.close();
        }
        return headers;
    }

    private String getDeviceTokenPush(Context context) {
        PushTokenKeyController pushTokenKeyController = PushTokenKeyController.getController();
        SharedPreferences sPref = context.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        String deviceTokenPush = sPref.getString("pushTokenKey", null);//pushTokenKeyController.getPushTokenKey()
        Log.d("PTKC", "deviceTokenPush METHODGDTpush= " + deviceTokenPush);
        Log.d("PTKC", "getPushTokenKey METHODGDTpush= " + pushTokenKeyController.getPushTokenKey());
        pushTokenKeyController.close();
        return deviceTokenPush;
    }

    public BigInteger getA() {
        return a;
    }

    public void setA(BigInteger a) {
        this.a = a;
    }

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public boolean isAppOpen() {
        return isAppOpen;
    }

    public void setAppOpen(boolean appOpen) {
        this.isAppOpen = appOpen;
    }

    public int getRegionID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OptimaApp.Companion.getInstance());
        return preferences.getInt(Constants.CHOSEN_REGION, 1);
    }

    public static String getHashSmsNotif(int code) {
        String isSMS = null;
        if (hashSmsNotif.containsKey(code)) {
            isSMS = String.valueOf(hashSmsNotif.get(code));
        }
        return isSMS;
    }

    public static void setHashSmsNotif(int code, boolean isSms) {
        hashSmsNotif.put(code, isSms);
    }

    public static void setLocaleChanged(boolean isLocaleChanged) {
        GeneralManager.isLocaleChanged = isLocaleChanged;
    }

    public static boolean isLocaleChanged() {
        return isLocaleChanged;
    }

    public void setHistoryApplicationses(ArrayList<HistoryApplications> historyApplicationses) {
        this.mHistoryApplicationses.clear();
        this.mHistoryApplicationses.addAll(historyApplicationses);
    }

    public ArrayList<HistoryApplications> getHistoryApplicationses() {
        return mHistoryApplicationses;
    }

    public String getErrorMessageForReg() {
        return errorMessageForReg;
    }

    public void setErrorMessageForReg(String errorMessageForReg) {
        this.errorMessageForReg = errorMessageForReg;
    }

    public long getMillisecondsForUnlockButton() {
        return millisecondsForUnlockButton;
    }

    public void setMillisecondsForUnlockButton(long millisecondsForUnlockButton) {
        this.millisecondsForUnlockButton = millisecondsForUnlockButton;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isInitializedMobocardsSdk() {
        return isInitializedMobocardsSdk;
    }

    public void setInitializedMobocardsSdk(boolean initializedMobocardsSdk) {
        isInitializedMobocardsSdk = initializedMobocardsSdk;
    }

    public String getRbsDefaultCard() {
        return rbsDefaultCard;
    }

    public void setRbsDefaultCard(String rbsDefaultCard) {
        this.rbsDefaultCard = rbsDefaultCard;
    }

    public boolean isAutoEncrypt() {
        return autoEncrypt;
    }

    public void setAutoEncrypt(boolean autoEncrypt) {
        this.autoEncrypt = autoEncrypt;
    }

    public String getProfImage() {
        return profImage;
    }

    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }
}
