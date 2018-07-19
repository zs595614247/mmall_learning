package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     *  String -> Date
     */
    public static String dateToStr(Date date,String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(formatStr));
    }

    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(STANDARD_FORMAT));
    }

    /**
     * Date -> String
     */
    public static Date strToDate(String dateTimeStr,String formatStr) {
        return Date.from(LocalDateTime.parse(dateTimeStr,  DateTimeFormatter.ofPattern(formatStr)).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date strToDate(String dateTimeStr) {
        return Date.from(LocalDateTime.parse(dateTimeStr,  DateTimeFormatter.ofPattern(STANDARD_FORMAT)).atZone(ZoneId.systemDefault()).toInstant());
    }

}
