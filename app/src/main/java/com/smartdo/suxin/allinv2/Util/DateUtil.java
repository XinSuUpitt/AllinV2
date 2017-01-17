package com.smartdo.suxin.allinv2.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by suxin on 9/9/16.
 */
public class DateUtil {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault());

    private static DateFormat dateFormatDate = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());

    private static DateFormat dateFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static Date stringToDate(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static String dateToStringDate(Date date) {
        return dateFormatDate.format(date);
    }

    public static String dateToStringTime(Date date) {
        return dateFormatTime.format(date);
    }
}
