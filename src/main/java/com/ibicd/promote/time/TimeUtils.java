package com.ibicd.promote.time;


import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.util.StringUtils;

public class TimeUtils {

    private static String DEFAULT_ZONEID = "GMT+8";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_FORMAT = "yyyy-MM-dd";


    private static final String[] parsePatterns = new String[]{"yyyy年MM月dd日 HH点mm分ss秒", "yyyy年MM月dd日 HH点mm分", "yyyy年MM月dd日",
            "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm", "MM/dd/yyyy", "MM/dd/yyyy HH:mm",
            "yyyy-MM-dd", "yyyy/MM/dd"};

    /**
     * 获取当天00:00:00的时间戳
     *
     * @param time
     * @return
     */
    public static long getDayStart(long time, String zoneId) {

        Calendar cal = Calendar.getInstance(getZone(zoneId));
        cal.setTimeInMillis(time * 1000);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获取当天23:59:59的时间戳
     *
     * @param time
     * @return
     */
    public static long getDayEnd(long time, String zoneId) {

        Calendar cal = Calendar.getInstance(getZone(zoneId));
        cal.setTimeInMillis(time * 1000);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 将time格式化为pattern格式
     *
     * @param pattern
     * @param time
     * @param zoneId
     * @return
     */
    public static String format(String pattern, long time, String zoneId) {

        time = time * 1000;
        pattern = StringUtils.isEmpty(pattern) ? DATE_TIME_FORMAT : pattern;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        ZoneId zone = getZone(zoneId).toZoneId();
        return dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), zone));
    }


    /**
     * 字符串格式时间转为时间戳
     *
     * @param strDate  字符串的时间
     * @param timeZone 时区：GMT+8
     * @return
     */
    public static Long parseStringToUnixTime(String strDate, String timeZone) {
        DateTimeFormatterBuilder formatterBuilder;
        final ParsePosition pos = new ParsePosition(0);
        for (String pattern : parsePatterns) {
            //appendPattern不可放在parseDefaulting后，会导致parseDefaulting失效
            formatterBuilder = new DateTimeFormatterBuilder().parseLenient()
                    .appendPattern(pattern)
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0);
            if (pattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            pos.setIndex(0);

            String str2 = strDate;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (pattern.endsWith("ZZ")) {
                str2 = strDate.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
            }
            try {
                final LocalDateTime localDateTime = LocalDateTime.parse(str2, formatterBuilder.toFormatter());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
                int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
                return localDateTime.toInstant(ZoneOffset.ofTotalSeconds(zoneOffset / 1000)).getEpochSecond();
            } catch (DateTimeParseException ex) {
                throw new RuntimeException("字符串格式时间转为时间戳失败！");
            }
        }
        throw new RuntimeException("字符串格式时间转为时间戳失败！");
    }

    /**
     * 获取时区
     *
     * @param zoneId
     * @return
     */
    public static TimeZone getZone(String zoneId) {

        return TimeZone.getTimeZone(zoneId);
    }
}
