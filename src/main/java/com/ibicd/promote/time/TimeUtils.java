package com.ibicd.promote.time;


import java.sql.Timestamp;
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

/**
 * 时间工具类
 */
public class TimeUtils {

    public static void main(String[] args) {

        Long time2 = 1604988316L;
        Long time1 = 1605004200L;
        System.out.println(isSameDay(time1, time2));
    }

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
            formatterBuilder = new DateTimeFormatterBuilder()
                    .parseLenient()
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

            }
        }
        throw new RuntimeException("无法转换时间");
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


    /**
     * 将指定时间戳的对应的hour时minutes 分转换为时间戳
     *
     * @param time
     * @param hour
     * @param minutes
     * @return
     */
    public static Long convertTimestamp(Long time, int hour, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);

        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        return timestamp.getTime() / 1000;
    }


    /**
     * 判断两个时间是否为同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(Long time1, Long time2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time1 * 1000);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(time2 * 1000);

        int days1 = cal1.get(Calendar.DAY_OF_YEAR);
        int days2 = cal2.get(Calendar.DAY_OF_YEAR);

        return days1 == days2;

    }


    /**
     * 获取当前时间对应的这个月的第一天
     *
     * @param time
     * @return
     */
    public static Long getFirstDayOfMonth(Long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);

        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int minimumHour = calendar.getActualMinimum(Calendar.HOUR_OF_DAY);
        int minimum = calendar.getActualMinimum(Calendar.MINUTE);
        int minSec = calendar.getActualMinimum(Calendar.SECOND);

        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        calendar.set(Calendar.HOUR_OF_DAY, minimumHour);
        calendar.set(Calendar.MINUTE, minimum);
        calendar.set(Calendar.SECOND, minSec);

        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取两个时间的相差天数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getDayDiff(long time1, long time2) {

        long diff = time1 - time2;
        return (int) Math.ceil((Math.abs(diff) / (60 * 60 * 24)));
    }

}
