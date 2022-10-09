package kz.optimabank.optima24.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConverterUtils {

    private static final String TAG = "DateConverterUtils";
    private static final String DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String TIMEZONE_GMT = "GMT";

    private static DateFormat dateFormatFullTime = new SimpleDateFormat("d MMMM HH:mm", Locale.getDefault());
    private static DateFormat dateFormatHourMinute = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static DateFormat dateFormatDayMonth = new SimpleDateFormat("d MMMM", Locale.getDefault());

    private static DateFormat getDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat(DATE, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_GMT));
        return dateFormat;
    }

    // конвертируем в часы и минуты
    public static Date dateFromString(@NonNull String value) {
        try {
            return getDateFormat().parse(value);
        } catch (ParseException e) {
            Log.e(TAG, "stringHourMinuteFromDate error while parsing " + e.getLocalizedMessage());
        }
        return null;
    }

    // конвертируем в часы и минуты
    public static String stringHourMinuteFromDate(@NonNull String value) {
        try {
            Date date = getDateFormat().parse(value);
            return dateFormatHourMinute.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "stringHourMinuteFromDate error while parsing " + e.getLocalizedMessage());
            return dateFormatHourMinute.format(new Date());
        }
    }

    // конвертируем дату в день и месяц и часы и минуты
    public static String stringDateTimeFromDate(@NonNull String value) {
        try {
            Date date = getDateFormat().parse(value);
            return dateFormatFullTime.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "stringDateTimeFromDate error while parsing " + e.getLocalizedMessage());
            return dateFormatFullTime.format(new Date());
        }
    }

    // конвертируем дату в день и месяц
    public static String stringDayMonthFromDate(@NonNull String value) {
        try {
            Date date = getDateFormat().parse(value);
            return dateFormatDayMonth.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "stringDayMonthFromDate error while parsing" + e.getLocalizedMessage());
            return dateFormatDayMonth.format(new Date());
        }
    }

    // конвертируем дату в день и месяц
    public static String stringDayMonthFromDate(long timeInMillis) {
        Date date = new Date(timeInMillis);
        return getDateFormat().format(date);
    }

    // конвертируем дату в день и месяц
    public static String stringFromDate(Date date) {
        return dateFormatDayMonth.format(date);
    }

    // проверяем не одинаковые ли дни
    public static boolean isTheSameDay(Date currentDate, Date otherDate) {
        if (currentDate == null || otherDate == null)
            return false;
        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.setTime(currentDate);
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTime(otherDate);
        return thisCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR)
                && thisCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)
                && thisCalendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH);
    }
}