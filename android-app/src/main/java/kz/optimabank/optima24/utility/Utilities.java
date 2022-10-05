package kz.optimabank.optima24.utility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import kg.optima.mobile.R;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.utility.crypt.CryptoUtils;

import static kz.optimabank.optima24.app.OptimaBank.getContext;
import static kz.optimabank.optima24.utility.Constants.DATE_FORMAT_FOR_REQEST;
import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX;
import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX_PLUS;
import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX_ZERO;
import static kz.optimabank.optima24.utility.Constants.VIEW_DATE_FORMAT;

/**
 * Class for holding different utility classes and methods
 *
 */
public final class Utilities {
    private static final DateFormat target = new SimpleDateFormat("dd MMMM, E", new Locale("ru", "RU"));
    private static int kik = 0;

    private Utilities() {
    }

    public static String getPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return "";
        String result = "";
        if (phoneNumber.startsWith(PHONE_NUMBER_PREFIX)) {
            result = phoneNumber.substring(3);
        } else if (phoneNumber.startsWith(PHONE_NUMBER_PREFIX_PLUS)) {
            result = phoneNumber.substring(4);
        } else if (phoneNumber.startsWith(PHONE_NUMBER_PREFIX_ZERO)) {
            result = phoneNumber.substring(1);
        }
        return result;
    }

    /**
     * Returns true if Internet connection is available
     */
    public static boolean hasInternetConnection(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else
            return false;
    }

    public static String convertDate(String d) {
        try {
            return target.format(getFormatForDate("yyyy-MM-dd'T'HH:mm:ss").parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences("my_pref",Context.MODE_PRIVATE);
    }

    public static void getToast(Context context, String message){
        Log.d("TAG", "getToast message = " + message);
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean isInternetConnectionError() {
        if (!Utilities.hasInternetConnection(getContext())) {
            // может вызываться не в главном потоке
            new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getContext(), getContext().getString(R.string.internet_switched_off), Toast.LENGTH_SHORT).show());
            return true;
        }
        return false;
    }

    public static ProgressDialog progressDialog(Context context, String text){
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progressDialog.setMessage(text);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return progressDialog;
    }

    public static AlertDialog showAlertDialog(Context context, String text, Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(intent);
            }
        });
        return builder.create();
    }

    public static String getCurrentDate(String format) {
        Calendar c = Calendar.getInstance();
        return getFormatForDate(format).format(c.getTime());
    }

    public static String getDateThirtyDayAgo(String format) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -30);
        return getFormatForDate(format).format(c.getTime());
    }

    public static String getDateMonthAgo(String format) {
        Calendar c = Calendar.getInstance();
        String day = getFormatForDate("dd").format(c.getTime());
        Log.i("DAYmotherfacker","day = "+day);
        String itog = getDate(format, 6);//Integer.valueOf(day)-1
        Log.i("DAYmotherfacker","itog = "+itog);
        return itog;
    }

    public static boolean getDateSevenDaysAgo(String dateIN) {
        String date = dateIN.replaceAll("\\.","-");
        Log.i("getDateSevenDaysAgo","date = "+date);
        boolean isSevenDateAgo;
        //Calendar c = Calendar.getInstance();
        String itog = getDate("dd-MM-yyyy", 7);
        Log.i("getDateSevenDaysAgo","itog = "+itog);

        Date dateN = new Date();
        Date dateC = new Date();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            dateN = format.parse(date);
            dateC = format.parse(itog);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("getDateSevenDaysAgo","dateN.getTime() = "+dateN.getTime());
        Log.i("getDateSevenDaysAgo","dateC.getTime() = "+dateC.getTime());
        Log.i("getDateSevenDaysAgo","dateN = "+dateN);
        Log.i("getDateSevenDaysAgo","dateC. = "+dateC);

        if (dateN.getTime()<dateC.getTime())
            isSevenDateAgo=false;
        else
            isSevenDateAgo = true;
        Log.i("getDateSevenDaysAgo","isSevenDateAgo = "+isSevenDateAgo);
        return isSevenDateAgo;
    }

    public static String getDate(String format, int daysAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -daysAgo);
        return getFormatForDate(format).format(c.getTime());
    }

    public static String getCurrencyBadge(Context context, String currency) {
        String badge = "";
        if(currency!=null) {
            switch (currency) {
                case "KZT":
                    badge = context.getResources().getString(R.string.tenge_icon);
                    break;
                case "KGS":
                    badge = context.getResources().getString(R.string.KGS);
                    break;
                case "USD":
                    badge = context.getResources().getString(R.string.USD);
                    break;
                case "CAD":
                    badge = context.getResources().getString(R.string.USD);
                    break;
                case "EUR":
                    badge = context.getResources().getString(R.string.EUR);
                    break;
                case "RUB":
                    badge = context.getResources().getString(R.string.RUB);
                    break;
                case "RUR":
                    badge = context.getResources().getString(R.string.RUB);
                    break;
                case "GBP":
                    badge = context.getResources().getString(R.string.GBP);
                    break;
                case "CNY":
                    badge = context.getResources().getString(R.string.CNY);
                    break;
            }
        }
        return badge;
    }

    public static String getFormattedInteger(double amount) {
        BigDecimal bd = new BigDecimal(amount);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(bd.doubleValue());
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getFormatForDate(String format) {
        return new SimpleDateFormat(format);
    }

    public static void clickAnimation(View view,ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(view)
                .setDuration(150)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterpolator())
                .setListener(listener).start();
    }

    public static String AmountFormatter(String amount){
        boolean isReverce = false;
        StringBuffer stringBuffer = new StringBuffer();
        String string = amount;
            if (amount.length()>3) {
                int pointPosition = string.indexOf(".");
                if (pointPosition==0 || pointPosition==-1){
                    pointPosition = string.indexOf(",");
                }
                if (pointPosition == -1) {
                    pointPosition = string.length();
                    if (amount.length()==4) {
                        isReverce = true;
                        stringBuffer.delete(0,stringBuffer.length());
                        stringBuffer.append(string);
                        stringBuffer.reverse();
                        string =String.valueOf(stringBuffer);
                        string = string.substring(0, pointPosition - 1) + " " + string.substring(pointPosition - 1);
                    }else{
                        isReverce = false;
                        string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                    }
                } else {
                    if (pointPosition>3)
                        string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                }
                for (int i = 0; i < pointPosition / 3; i++) {
                    int lastSpacePosition = string.indexOf(" ");
                    if ((lastSpacePosition - 3) > 0) {
                        string = string.substring(0, lastSpacePosition - 3) + " " + string.substring(lastSpacePosition - 3);
                    } else {
                        break;
                    }
                }
                Log.i("string","string = "+string);
            }

        if (isReverce){
            stringBuffer.delete(0,stringBuffer.length());
            stringBuffer.append(string);
            stringBuffer.reverse();
            string =String.valueOf(stringBuffer);
        }

        return string;
    }

    public static String NumberFormatter(String amount) {
        try {
            amount = amount.replaceAll("\\s", "");
            amount = amount.replaceAll(",", ".");
            BigDecimal bd = new BigDecimal(amount);
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
            return formatter.format(bd.doubleValue());
        } catch (Exception e) {
            return "0.00";
        }
    }


    public static String getFormattedBalance(double balance, String currency) {
        BigDecimal bd = new BigDecimal(balance);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
        return formatter.format(bd.doubleValue()) + " " + currency;
    }

    static public JSONObject getFieldNamesAndValues(final Object valueObj) {
        try {
            Class c1 = valueObj.getClass();
            JSONObject fieldMap = new JSONObject();
            Field[] valueObjFields = c1.getFields();
            // compare values now
            for (Field valueObjField : valueObjFields) {
                String fieldName = valueObjField.getName();
                Object newObj = valueObjField.get(valueObj);
                if (newObj instanceof Integer) {
                    if ((int) newObj != -1000) {
                        fieldMap.put(fieldName, newObj);
                    }
                } else {
                    fieldMap.put(fieldName, newObj);
                }
            }
            return fieldMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static String doubleFormatter(String string,Context context) {
        BigDecimal bd = new BigDecimal(getDoubleType(string));
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
        String str = formatter.format(bd.doubleValue()) + "";
        str = str.replaceAll(",", ".");
        str = str.replaceAll("\\s", "");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String loc = sharedPreferences.getString(Constants.APP_LANGUAGE,"null");
        if (loc.toLowerCase().contains("en")){
            str = str.replaceAll(",", ".");
        } else {
            str = str.replaceAll("\\.", ",");
        }
        return str;
    }

    public static String doubleFormatter(Double d) {
        BigDecimal bd = new BigDecimal(d);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
        String str = formatter.format(bd.doubleValue()) + "";
        str = str.replaceAll(",", ".");
        return str;
    }

    public static Double getDoubleType(String number) {
        double d = 0;
        if(number!=null) {
            d = Double.parseDouble(number.replaceAll("\\s", "").replaceAll(" ", "").replaceAll(",", "."));
        }
        return d;
    }

    public static void setPaymentImage(Context context ,ImageView image, String alias) {
        Log.i("setPaymentImage","kik = "+ ++kik);
        switch (alias) {
            case "20":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_webpay));
                break;
            case "12":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_taxi));
                break;
            case "14":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_socialnetworks));
                break;
            case "1":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_public));
                break;
            case "9":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_other));
                break;
            case "15":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_money));
                break;
            case "2":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_mobile));
                break;
            case "10":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_mobile));
                break;
            case "21":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_internetstore));
                break;
            case "5":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_internet));
                break;
            case "13":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_games));
                break;
            case "16":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_gameplatforms));
                break;
            case "3":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_fixed));
                break;
            case "11":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_education));
                break;
            case "17":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_dealer));
                break;
            case "6":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_cable));
                break;
            case "7":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_budget));
                break;
            case "4":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_budget));
                break;
            case "8":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_beauty));
                break;
            /*case "mobile":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile_new));
                break;
            case "utility":
            case "Комуслуги":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.komunal));
                break;
            case "telecom":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.phone_orange));//  ic_2
                break;
            case "tv":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.tv));
                break;
            case "Детский":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.kindergarten));
                break;
            case "internet":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.internet_new));
                break;
            case "Билеты":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ticket));
                break;
            case "InetShopping":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.shop));
                break;
            case "Букмекерские конторы":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.casino_new));
                break;
            case "Хостинг":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.server));
                break;
            case "Купоны":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.discount));
                break;
            case "Образование":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.educ));
                break;
            case "Онлайн издания":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.mag));
                break;
            case "Охрана":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.shield));
                break;
            case "Страхование":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.insurance));
                break;
            case "eCash":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.purse));
                break;
            case "Ипотека и кредитование":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.home));
                break;
            case "Интернет-ресурсы":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.internet_new_pay));
                break;
            case "сервис":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.gamepad));
                break;
            case "Налоги и штрафы":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.fine));
                break;
            case "Косметика":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.cosmet));
                break;
            case "Транспорт":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.transport));//transport  ic_13
                break;
            case "Штрафы ПДД":
            case "penalties":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.taxi));
                break;
            case "Благотворительность":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.charity_new));
                break;
            case "Виртуальные деньги":
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.money_new));
                break;*/
            default:
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_red_payments_other));//star
                break;
        }
            /*case "22":
                    image.setImageDrawable(context.getResources().getDrawable(R.drawable.bus));
                    break;*/
    }

    public static LinearLayout.LayoutParams getLayoutParamsForImageSize(Context context, int width, int height){
        int imageWidthForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,
                context.getResources().getDisplayMetrics());
        int imageHeightForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,
                context.getResources().getDisplayMetrics());
       return  new LinearLayout.LayoutParams(imageWidthForCard, imageHeightForCard);
    }

    public static ConstraintLayout.LayoutParams getConstraintLayoutParamsForImageSize(Context context, int width, int height){
        int imageWidthForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,
                context.getResources().getDisplayMetrics());
        int imageHeightForCard = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,
                context.getResources().getDisplayMetrics());
        return  new ConstraintLayout.LayoutParams(imageWidthForCard, imageHeightForCard);
    }

    private static class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }

    public static String transliterate(String srcstring) {
        ArrayList<String> copyTo = new ArrayList<>();

        String cyrcodes = "";
        for (int i = 1040; i <= 1071; i++) {
            cyrcodes = cyrcodes + (char) i;
        }
        for (int j = 1072; j <= 1103; j++) {
            cyrcodes = cyrcodes + (char) j;
        }
        // Uppercase
        copyTo.add("A");
        copyTo.add("B");
        copyTo.add("V");
        copyTo.add("G");
        copyTo.add("D");
        copyTo.add("E");
        copyTo.add("Zh");
        copyTo.add("Z");
        copyTo.add("I");
        copyTo.add("I");
        copyTo.add("K");
        copyTo.add("L");
        copyTo.add("M");
        copyTo.add("N");
        copyTo.add("O");
        copyTo.add("P");
        copyTo.add("R");
        copyTo.add("S");
        copyTo.add("T");
        copyTo.add("U");
        copyTo.add("F");
        copyTo.add("Kh");
        copyTo.add("TS");
        copyTo.add("Ch");
        copyTo.add("Sh");
        copyTo.add("Shch");
        copyTo.add("");
        copyTo.add("Y");
        copyTo.add("");
        copyTo.add("E");
        copyTo.add("Yu");
        copyTo.add("Ya");

        // lowercase
        copyTo.add("a");
        copyTo.add("b");
        copyTo.add("v");
        copyTo.add("g");
        copyTo.add("d");
        copyTo.add("e");
        copyTo.add("zh");
        copyTo.add("z");
        copyTo.add("i");
        copyTo.add("i");
        copyTo.add("k");
        copyTo.add("l");
        copyTo.add("m");
        copyTo.add("n");
        copyTo.add("o");
        copyTo.add("p");
        copyTo.add("r");
        copyTo.add("s");
        copyTo.add("t");
        copyTo.add("u");
        copyTo.add("f");
        copyTo.add("kh");
        copyTo.add("ts");
        copyTo.add("ch");
        copyTo.add("sh");
        copyTo.add("shch");
        copyTo.add("");
        copyTo.add("y");
        copyTo.add("");
        copyTo.add("e");
        copyTo.add("yu");
        copyTo.add("ya");

        String newstring = "";
        char onechar;
        int replacewith;
        for (int j = 0; j < srcstring.length(); j++) {
            onechar = srcstring.charAt(j);
            replacewith = cyrcodes.indexOf((int) onechar);
            if (replacewith > -1) {
                newstring = newstring + copyTo.get(replacewith);
            } else {
                // keep the original character, not in replace list
                newstring = newstring + String.valueOf(onechar);
            }
        }

        return newstring;
    }

    public static String filterPhoneText(String text) {
        return text.replaceAll("\\D","").substring(1);
    }

    public static String dotAndComma(String string) {
        String firstCD = "";
        String lastCD = "";
        int first = -1;
        int firstD = -1;
        int firstC = -1;
        firstD = string.indexOf(".");
        firstC = string.indexOf(",");
        if (firstC!=-1 || firstD!=-1){
            if (firstC<firstD){
                first=firstC;
            }else{
                first=firstD;
            }
            if (firstC==-1){
                first=firstD;
            }
            if (firstD==-1){
                first=firstC;
            }
        }
        Log.i("INDEX","firstC = "+firstC);
        Log.i("INDEX","firstD = "+firstD);
        Log.i("INDEX","first = "+first);
        if (first!=-1) {
            try {
                firstCD = string.substring(0, first + 1);
                lastCD = string.substring(first + 1, string.length());

                Log.i("INDEX", "firstCD = " + firstCD);
                Log.i("INDEX", "lastCD = " + lastCD);

                lastCD = lastCD.replaceAll(",", "");
                lastCD = lastCD.replaceAll("\\.", "");

                Log.i("INDEX", "stringB = " + string);
                string = new StringBuffer(firstCD).append(lastCD).toString();
                //string = firstCD.concat(lastCD);

                Log.i("INDEX", "stringA = " + string);
                Log.i("INDEX", "new StringBuffer(firstCD).append(lastCD).toString() = " + new StringBuffer(firstCD).append(lastCD).toString());
            }catch (Exception e){}
        }
        return string;
    }

    public static String amountFormat(String text) {
        boolean isReverce = false;
        StringBuffer stringBuffer = new StringBuffer();
        String string = text.replaceAll(" ","");
        if (text.length()>3) {
            int pointPosition = string.indexOf(".");
            if (pointPosition==0 || pointPosition==-1){
                pointPosition = string.indexOf(",");
            }
            Log.i("pointPosition","pointPosition = "+pointPosition);
            if (pointPosition == -1) {
                pointPosition = string.length();
                if (text.length()==4) {
                    isReverce = true;
                    stringBuffer.delete(0,stringBuffer.length());
                    stringBuffer.append(string);
                    stringBuffer.reverse();
                    string =String.valueOf(stringBuffer);
                    string = string.substring(0, pointPosition - 1) + " " + string.substring(pointPosition - 1);
                }else{
                    isReverce = false;
                    string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                }
            } else {
                if (pointPosition>3)
                    string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
            }
            for (int i = 0; i < pointPosition / 3; i++) {
                int lastSpacePosition = string.indexOf(" ");
                if ((lastSpacePosition - 3) > 0) {
                    string = string.substring(0, lastSpacePosition - 3) + " " + string.substring(lastSpacePosition - 3);
                } else {
                    break;
                }
            }
            Log.i("string","string = "+string);
        }

        if (string.length()!=0) {
            if (isReverce){
                stringBuffer.delete(0,stringBuffer.length());
                stringBuffer.append(string);
                stringBuffer.reverse();
                string =String.valueOf(stringBuffer);
            }
        }

        return string;
    }

    public static String amountFormat(String text, EditText edAmount) {
        boolean isReverce = false;
        StringBuffer stringBuffer = new StringBuffer();
        String string = text.replaceAll(" ","");
        if (text.length()>3) {
            int pointPosition = string.indexOf(".");
            if (pointPosition==0 || pointPosition==-1){
                pointPosition = string.indexOf(",");
            }
            Log.i("pointPosition","pointPosition = "+pointPosition);
            if (pointPosition == -1) {
                pointPosition = string.length();
                if (text.length()==4) {
                    isReverce = true;
                    stringBuffer.delete(0,stringBuffer.length());
                    stringBuffer.append(string);
                    stringBuffer.reverse();
                    string =String.valueOf(stringBuffer);
                    string = string.substring(0, pointPosition - 1) + " " + string.substring(pointPosition - 1);
                }else{
                    isReverce = false;
                    string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                }
            } else {
                if (pointPosition>3)
                    string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
            }
            for (int i = 0; i < pointPosition / 3; i++) {
                int lastSpacePosition = string.indexOf(" ");
                if ((lastSpacePosition - 3) > 0) {
                    string = string.substring(0, lastSpacePosition - 3) + " " + string.substring(lastSpacePosition - 3);
                } else {
                    break;
                }
            }
            Log.i("string","string = "+string);
        }

        if (string.length()!=0) {
            if (isReverce){
                stringBuffer.delete(0,stringBuffer.length());
                stringBuffer.append(string);
                stringBuffer.reverse();
                string =String.valueOf(stringBuffer);
            }
        }
        int selection = edAmount.getSelectionStart();
        if((edAmount.getText().toString().length() + 2) == string.length()) {
            selection = selection + 2;
        } else if((edAmount.getText().toString().length() + 1) == string.length()) {
            selection = selection + 1;
        } else if((edAmount.getText().toString().length() - 1) == string.length()) {
            selection = selection - 1;
        } else if((edAmount.getText().toString().length() - 2) == string.length()) {
            selection = selection - 2;
        } else {
            selection = edAmount.getSelectionStart();
        }

        edAmount.setText(string);

        try {
            edAmount.setSelection(selection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }

    public static void setCardToImageView(UserAccounts.Cards card, ImageView imageView, TextView tvMulti, Bitmap smallIm) {
        Log.i("setCardToImageView", "card.productName = " + card.productName);
        Log.i("setCustomCard","smallIm = "+smallIm);
        if (smallIm == null) {
            switch (card.productName.replace(" ", "").replace("-", "").toLowerCase()) {
            }
        } else {
            imageView.setImageBitmap(smallIm);
        }
        if (tvMulti != null) {
            if (card.isMultiBalance) {
                tvMulti.setVisibility(View.VISIBLE);
            } else {
                tvMulti.setVisibility(View.GONE);
            }
        }
    }

    public static void setCardToImageView(DigitizedCard card, ImageView imageView, Bitmap smallIm) {
        if (smallIm == null) {
            switch (card.getProductName().replace(" ", "").replace("-", "").toLowerCase()) {
            }
        } else {
            imageView.setImageBitmap(smallIm);
        }
    }

    public static void setColorsForChart(int ID, ArrayList<Integer> colors) {
        switch (ID) {
            case 1:
                colors.add(Color.rgb(240, 209, 168));
                break;
            case 2:
                colors.add(Color.rgb(117, 215, 161));
                break;
            case 3:
                colors.add(Color.rgb(224, 199, 184));
                break;
            case 4:
            case 26:
                colors.add(Color.rgb(168, 240, 194));
                break;
            case 5:
                colors.add(Color.rgb(244, 134, 165));
                break;
            case 6:
                colors.add(Color.rgb(244, 148, 228));
                break;
            case 7:
                colors.add(Color.rgb(240, 168, 222));
                break;
            case 8:
                colors.add(Color.rgb(168, 192, 240));
                break;
            case 9:
                colors.add(Color.rgb(136, 130, 252));
                break;
            case 10:
                colors.add(Color.rgb(168, 240, 239));
                break;
            case 11:
                colors.add(Color.rgb(184, 245, 143));
                break;
            case 12:
                colors.add(Color.rgb(217, 155, 233));
                break;
            case 14:
                colors.add(Color.rgb(119, 123, 197));
                break;
            case 15:
                colors.add(Color.rgb(184, 224, 191));
                break;
            case 16:
                colors.add(Color.rgb(243, 168, 124));
                break;
            case 17:
                colors.add(Color.rgb(201, 206, 197));
                break;
            case 22:
                colors.add(Color.rgb(253, 175, 159));
                break;
            default:
                colors.add(Color.rgb(204, 168, 240));
                break;
        }
    }

    public static String formatDateString(Context context, String dateString, boolean isTemplate) {//12,02,2018
        String[] dateParts;
        if (dateString.contains(",")) {
            dateParts = dateString.split(",");
        } else if (dateString.contains(":")) {
            dateParts = dateString.split(":");
        } else if (dateString.contains(".")) {
            dateParts = dateString.split("\\.");
        } else {
            dateParts = dateString.split("-");
        }
        String[] date = context.getResources().getStringArray(R.array.month_for_date);
        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];
        if (isTemplate) {
            switch (Integer.valueOf(month)) {
                case 0:
                    month = date[0];
                    break;
                case 1:
                    month = date[1];
                    break;
                case 2:
                    month = date[2];
                    break;
                case 3:
                    month = date[3];
                    break;
                case 4:
                    month = date[4];
                    break;
                case 5:
                    month = date[5];
                    break;
                case 6:
                    month = date[6];
                    break;
                case 7:
                    month = date[7];
                    break;
                case 8:
                    month = date[8];
                    break;
                case 9:
                    month = date[9];
                    break;
                case 10:
                    month = date[10];
                    break;
                case 11:
                    month = date[11];
                    break;
            }
        } else {
            switch (Integer.valueOf(month)) {
                case 1:
                    month = date[0];
                    break;
                case 2:
                    month = date[1];
                    break;
                case 3:
                    month = date[2];
                    break;
                case 4:
                    month = date[3];
                    break;
                case 5:
                    month = date[4];
                    break;
                case 6:
                    month = date[5];
                    break;
                case 7:
                    month = date[6];
                    break;
                case 8:
                    month = date[7];
                    break;
                case 9:
                    month = date[8];
                    break;
                case 10:
                    month = date[9];
                    break;
                case 11:
                    month = date[10];
                    break;
                case 12:
                    month = date[11];
                    break;
            }
        }
        return day + " " + month + " " + year;
    }

    /**
     * Метод по проверки карты методом луны
     * @param ccNumber номер карты
     * @return boolean
     */
    public static boolean cardLuna(String ccNumber)
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // принудительно сетим шрифт так как знак рубля не отображается на экране
    public static void setRobotoTypeFaceToTextView(Context context, TextView textView) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        textView.setTypeface(typeFace);
    }


    /**
     * Метод для форматирования даты
     * @param isForRequest флаг для определения в как   ой формат преобразовать, если true то форматируем для запроса, иначе наоборот
     *                     {@code
     *                     if (isForRequest) {
     *                          dd.MM.yyyy ---> yyyy-MM-dd'T'HH:mm:ss
     *                     } else {
     *                         yyyy-MM-dd'T'HH:mm:ss --- > dd.MM.yyyy
     *                     }
     *                     }
     * @param dateString дата которую надо преобразовать
     * @return отформатированная дата
     */
    public static String formatDate(boolean isForRequest, String dateString) {
        String formattedDate = null;
        try {
            if (isForRequest) {
                formattedDate = DATE_FORMAT_FOR_REQEST.format(VIEW_DATE_FORMAT.parse(dateString));
            } else {
                formattedDate = VIEW_DATE_FORMAT.format(DATE_FORMAT_FOR_REQEST.parse(dateString));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static int pxInDp(Context context, int px) {
        return (int) (context.getResources().getDisplayMetrics().density * px + 0.5f);
    }

    public static boolean isDeviceScreenLocked(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return isDeviceLocked(context);
        } else {
            return isPatternSet(context) || isPassOrPinSet(context);
        }
    }

    /**
     * @return true if pattern set, false if not (or if an issue when checking)
     */
    private static boolean isPatternSet(Context context) {
        ContentResolver cr = context.getContentResolver();
        try {
            int lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED);
            return lockPatternEnable == 1;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    /**
     * @return true if pass or pin set
     */
    private static boolean isPassOrPinSet(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardSecure();
    }

    /**
     * @return true if pass or pin or pattern locks screen
     */
    @TargetApi(23)
    private static boolean isDeviceLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isDeviceSecure();
    }

    public static byte[] main ( String text )
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {

        Cipher cipher = Cipher.getInstance( "RSA" );
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
        keyGen.initialize( 2048 );
        KeyPair kp = keyGen.genKeyPair();

        PublicKey publicKey = kp.getPublic();
        //PrivateKey privateKey = kp.getPrivate();


        cipher.init( Cipher.ENCRYPT_MODE, publicKey );
        byte[] encryptMass = cipher.doFinal( text.getBytes() );

        /*cipher.init( Cipher.DECRYPT_MODE, privateKey );
        byte[] y = cipher.doFinal( x );

        System.out.println("\nText is:\n" + text);
        System.out.println("\nEncrypt text:\n" + new String (x));
        System.out.println("\nDecrypt text:\n" + new String (y));*/

        return encryptMass;
    }

    public static PrivateKey getPemPrivateKey(Context context, String filename, String algorithm) throws Exception {
        InputStream is = context.getAssets().open(filename);
        byte[] keyBytes = new byte[is.available()];
        is.read(keyBytes);
        is.close();
        String temp = new String(keyBytes);
        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----", "");
        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
        Log.d(CryptoUtils.class.getSimpleName(), "Private key\n" + privKeyPEM);
        byte[] decoded = Base64.decode(privKeyPEM, Base64.DEFAULT);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePrivate(spec);
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(data);
        return privateSignature.sign();
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        private final int decimalDigits;

        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {


            int dotPos = -1;
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i;
                    break;
                }
            }
            if (dotPos >= 0) {

                // protects against many dots
                if (source.equals(".") || source.equals(","))
                {
                    return "";
                }
                // if the text is entered before the dot
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > decimalDigits) {
                    return "";
                }
            }

            return null;
        }

    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
