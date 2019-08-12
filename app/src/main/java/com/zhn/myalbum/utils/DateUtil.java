package com.zhn.myalbum.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//yyyy-MM-dd HH:mm:ss
public class DateUtil {

    public static String formatFromDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(date);

    }

    public static String formatFromLong(String format, long date) {
        return new SimpleDateFormat(format)
                .format(new Date(date));
    }

    public static String getDay(long date) {
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(date));
        return str.substring(0, 10);

    }

    public static String getDay(String date) {
        String str = date.replace(":","-");

        return str.substring(0, 10);

    }
}
