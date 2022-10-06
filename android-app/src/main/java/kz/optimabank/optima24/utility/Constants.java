package kz.optimabank.optima24.utility;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.namespace.QName;

import kg.optima.mobile.BuildConfig;

public final class Constants {
    // TEST
    // public static String API_BASE_URL = "http:/10.185.233.208:3080/";  //main test3
    // public static String API_BASE_URL = "http://10.185.233.237:5555/";  //main test
     public static String API_BASE_URL = "https://test-cl-mapi.optima24.kg/";  //main test

    // БОЙ
    // public static String API_BASE_URL = "https://telebank3.optima24.kg:3080";

    // public static String API_BASE_URL = "http://192.168.10.157:8083/"; //optima sasha
    // public static String API_BASE_URL = "http://192.168.11.200:61/"; //optima
    // public static String API_BASE_URL = "http://192.168.11.200:9030/"; //optima 9030
    // public static String API_BASE_URL = "https://test.optima24.kg:440/"; //200 сервер . работает через интернет

    // public static String API_BASE_URL = "http://10.185.233.208:3080/";     //тестовый банкиров
    // public static String API_BASE_URL = "http://217.76.72.58:9030/";      //копия боя  -  testoptima440

    // PUSH
    public static final String API_PUSH_URL = "https://api.optimabank.kg/"; // бой
    // public static final String API_PUSH_URL = "http://10.185.233.241:8013/"; // тест

    public static final String APP_VERSION = "Optima24/" + BuildConfig.VERSION_NAME;
    public static final int SUCCESS = 0;
    public static final int DEFAULT_VALUE_1000 = -1000;
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final int CONNECTION_ERROR_STATUS = 1000;
    public static final int SELECT_ACCOUNT_FROM_REQUEST_CODE = 0;
    public static final int SELECT_ACCOUNT_TO_REQUEST_CODE = 1;
    public static final int SELECT_CURRENCY_REQUEST_CODE = 2;
    public static final int SELECT_CITIZENSHIP_REQUEST_CODE = 3;
    public static final int SELECT_SECO_REQUEST_CODE = 4;
    public static final int SELECT_KNP_REQUEST_CODE = 5;
    public static final int SELECT_BIC_REQUEST_CODE = 6;
    public static final int SELECT_DATE = 7;
    public static final int SETTINGS_POINT = 8;
    public static final int SELECT_LIMIT_REQUEST_CODE = 11;
    public static final int ACTION_LOCATION_SOURCE_SETTINGS = 9;
    public static final int SELECT_ACCOUNT_FROM_TO_REQUEST_CODE = 10;
    public static final String TAG = "TAG";
    public static final int CLICK_ITEM_TAG = 0;
    public static final int TAG_CHANGE = 1;
    public static final int TAG_PAY = 1;
    public static final String ACCOUNT_KEY = "account";
    public static final String STRING_KEY = "string";
    public static final String DICTIONARY_KEY = "dictionary";
    public static final String TEMPLATE_KEY = "template";
    public static final String HISTORY_KEY = "history";
    public static final String CARD_KEY = "card";
    public static final String CARDS_LIST_KEY = "cards_list";
    public static final String DATE_TAG = "history";
    public static final int ITEM_ID = 0;
    public static final int HEADER_ID = -1;
    public static final int FOOTER_ID = -2;
    public static final int CREDIT_HEADER = -3;
    public static final int CARD_HEADER = -4;
    public static final int NEW_CARD_ID = -2;
    public static final int CARD_SMSINFO = 11;
    public static final int PUSH_NOTIFICATION = 12;
    public static final Integer HEADER_SPACING = -5;
    //апи версия 3(изменены переводы и платежи)
    static final int API_VERSION = 3;
    public static final int DEFAULT_TAG = 0;
    public static final int CHECK_TAG = 1;
    public static final int CONFIRM_TAG = 2;
    public static final int DELETE_TAG = 3;
    public static final int UPDATE_TAG = 4;
    public static final int DICTIONARIES_TAG = 5;
    public static final int TYPE_ATM = 100;
    public static final int TYPE_BRANCH = 101;
    public static final int PENALTY_TEMPLATE = 12345;
    public static final Calendar CALENDAR = Calendar.getInstance();
    public static final String DATE_PICKER_TAG = "datePicker";
    public static SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("dd MMMM", new Locale("ru", "RU"));
    public static SimpleDateFormat VIEW_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    public static String COMMON_DATE_FORMAT = "dd.MM.yyyy";
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static final SimpleDateFormat API_DAY_MONTH_FORMAT = new SimpleDateFormat("dd.MM");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat CARD_DATE_FORMAT = new SimpleDateFormat("MM/yy");
    public static final SimpleDateFormat API_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_FOR_REQEST = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static SimpleDateFormat DAY_MONTH_YEAR_FORMAT = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru", "RU"));
    public static final int NO_ACCOUNT = -1;
    public static final int ACCOUNTS_AND_DEPOSITS = 998;
    public static final int ALL_ACCOUNTS = 999;
    public static final int CARD_ACCOUNT = 2;
    public static final int CARD = 5;
    public static final int DEPOSIT_ACCOUNT = 3;
    public static final int CURRENT_ACCOUNT = 1;
    public static final int CREDIT_ACCOUNT = 4;
    public static final int LOAN_ACCOUNT = 1003;
    public static final int CARDS_AND_CURRENT_ACCOUNT = 1004;
    public static final int VISA_CARDS = 1005;
    public static final int SWIFT_ACCOUNTS = 1006;
    public static final int TransferMoneyToDeposit = 10;
    public static final int TransferMoneyInsideClientCards = 20;
    public static final int TransferMoneyInsideClientAccounts = 30;
    public static final int TransferMoneyInsideClientCardToAccount = 40;
    public static final int TransferMoneyInsideClientAccountToCard = 50;
    public static final int TransferMoneyInsideBankFromAccountToCard = 60;
    public static final int TransferMoneyInsideBankFromCardAccountToCard = 70;
    public static final int TransferMoneyInsideBankFromAccountToAccount = 80;
    public static final int TransferMoneyInsideBankFromCardAccountToAccount = 90;
    public static final int TransferMoneyToAnotherBank = 100;
    public static final int TransferMoneyVisaToVisa = 102;
    public static final int TransferMoneyMasterToMaster = 103;
    public static final int TransferMoneyToOutsideCard = 110;
    public static final int TransferForeignCurrencyToAnotherBank = 120;
    public static final int TransferMoneyInsideClientDepositToAccount = 130;
    public static final int TransferMoneyInsideClientDepositToCard = 140;
    public static final int TransferConversionInsideMulticurrencyCard = 150;// TODO: 03.02.2021 Поменять на 130
    public static final int TRANSFER_INTERBANK_TYPE_CODE = 1;
    public static final String CHOSEN_REGION = "chosen_region";
    public static final String APP_LANGUAGE = "app_language";
    public static final String APP_DEVICE_ID = "app_device_id";
    public static final String SHOW_DANGER_ALERT = "danger_alert";
    public static final String EXTRA_DATA = "data";
    public static final String CURRENCY_KZT = "KZT";
    public static final String CURRENCY_KGS = "KGS";
    public static final String CURRENCY_USD = "USD";
    public static final String CURRENCY_EUR = "EUR";
    public static final String message = "message";
    public static final String userPhone = "user_phone";
    public static final String NOTIF_COUNT = "notif_count";

    // Для удаления специальных символов в телефоне
    public static final String REG_EX_FOR_PHONE = "[() -]";
    public static String BASE_URL = "https://apay.atfbank.kz"; // bank
    public static String KEY_TYPE = "f3e39e080cf4fe155e14f32e3979d32d"; // bank
    public static String PUBLIC_KEY = "D3BBB53AF064EDBB730A72D56933241FC445CB327421DD8277199C73DFEB7492EB6A3AFC957B77194455E494F92641DBF6684DE5CA4600E6706EB188B474277A06C99E6B11C26C5A85784FF9BA0349F043ED3AF621AA62D84ADF29D936B27DD14A725BB71DF8DE957E7CEC2BE27B449D6680E5037AEAB96ADFA3B252FA4E1331B052528C523BE1503B1BEFCCD67E12B2E486EFDC205D19049FCA6E98DE77F41A7347F4FDBCAA0E5545E671D161B2AF69A18936495AC9E33012F9A2383F84456EFABCD4D58B497A8E3A4BD6B1935FE990FB803F37C857C5B3416635C0E840AE76A182DBBB86DCB0CF025C3E1F6B182D3B028EC409AF0126BE7CCAAA8BB41D99B9";
    public static final String COMMON_DATE_FORMAT_WITH_SS = "yyyy-MM-dd'T'HH:mm:ss";


    public static final String allowHostName = "62.80.161.102";

    public static final String DELETE_TOKEN_ACTION = "delete_token_action";
    public static final String RESUME_TOKEN_ACTION = "resume_token_action";
    public static final String SUSPEND_TOKEN_ACTION = "suspend_token_action";
    public static final String SELECT_DEFAULT_CARD_ACTION = "select_default_card_action";
    public static final String BLOCK_CARD_ACTION = "block_card_action";
    public static final String URGENT_MESSAGE_TAG = "urgent_message_tag";
    public static final String URGENT_MESSAGE_PREFERENCES = "urgent_message_preferences";
    public static final String URGENT_MESSAGE_TIME = "urgent_message_time";

    public static final int TRANSFER_OTP_KEY = 7;
    public static final int PAYMENT_OTP_KEY = 6;
    public static final int TEMPLATE_OTP_KEY = 10;
    public static final int CREATE_TEMPLATE_OTP_KEY = 10;
    public static final String PUSH_TAG = "push_tag";
    public static final String DATE_CONVERSION_TAG = "date_conversion tag";
    public final static String BROADCAST_ACTION = "kz.atfbank.atf24.fragment.message";
    public final static String ADAPTER_UPDATE = "adapter_update";
    public final static String IS_CONTACTLESS = "is_contactless";
    public final static String NOTIFICATION_ARG_ID = "notification_id";
    public final static String IS_NOTIFICATION = "notification";
    public final static String PUSH_PREFERENCES = "push_preferences";
    public final static String PUSH_TOKEN = "push_token";
    public final static String DEVICE_OS = "android";
    public final static String APP_NAME = "o24";
    public final static String NOTIFICATION_ID = "id";
    public final static String BANK_ID = "bank_id";

    public static final String REGISTRATION_LOGIN = "registration_login";
    public static final String REGISTRATION_CODE = "registration_code";
    public static final String REGISTRATION_TAG = "registration_tag";

    public final static String BACKGROUND_NOTIFICATION_ID = "background_notification";
    public final static String IS_BACKGROUND_NOTIFICATION = "background_notification_state";

    public final static String NOTIFICATION_CHANNEL_ID = "channel_id_1";
    public final static String NOTIFICATION_DATA_ID = "notification_data_id";
    public final static String NOTIFICATION_DATA_TITLE = "notification_data_title";
    public final static String NOTIFICATION_DATA_TEXT = "notification_data_text";
    public final static String NOTIFICATION_DATA_SEND_TIME = "notification_data_sent_time";
    public static final String RECEIPT_MODEL = "receipt_model";
    public static final String RECEIPT_TYPE = "receipt_type";

    public static final String RECEIPT_NUMBER = "receipt_number";
    public static final String RECEIPT_FEE= "receipt_fee";
    public static final String SUM_WITH_AMOUNT= "sumWithAmount";
    public static final String RECEIPT_ACCOUNT_FROM = "receipt_account_from";
    public static final String RECEIPT_ACCOUNT_TO = "receipt_account_to";
    public static final String RECEIPT_OWN_ACCOUNT_TO = "receipt_own_account_to";
    public static final String RECEIPT_ACCOUNT_TO_FULL_NAME = "receipt_account_to_full_name";
    public static final String RECEIPT_AMOUNT= "amount";
    public static final String RECEIPT_AMOUNT_CURRENCY = "operationCurrency";
    public static final String RECEIPT_ACCOUNT_FEE = "receipt_currency";
    public static final String PAYMENT_TITLE = "paymentTitle";
    public static final String RECEIPT_ACCOUNT_NUMBER = "account_number";
    public static final String IS_RECEIPT_TRANSFER = "is_receipt_transfer";
    public static final String IS_TRANSFER_ELCARD_TO_ELCARD = "IS_TRANSFER_ELCARD_TO_ELCARD";

    public static final String CONTACT = "contact";
    public static final String PHONE_NUMBER_PREFIX = "996";
    public static final String PHONE_NUMBER_PREFIX_PLUS = "+996";
    public static final String PHONE_NUMBER_PREFIX_ZERO = "0";
    public static final String IS_SHOW_TIP_DEF_CARD = "IS_TIP_SHOW";
    public static final String IS_HIDE_CREATE_TEMPLATE = "IS_HIDE_CREATE_TEMPLATE";
    public static final String RECEIPT_PHONE_NUMBER = "RECEIPT_PHONE_NUMBER";

    public static final String COUNT_OF_ATTEMPTS = "COUNT_OF_ATTEMPTS";

    public static final String RX_JAVA_ERRORS = "rx_java_errors";

    public static void resetVIEW_DATE_FORMAT() {
        VIEW_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        DAY_DATE_FORMAT = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        DAY_MONTH_YEAR_FORMAT = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    }
}