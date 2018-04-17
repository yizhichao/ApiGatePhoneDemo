package com.hik.apigatephonedemo.utils;


import android.content.Context;
import android.text.TextUtils;

import com.hik.apigatephonedemo.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @ Class DateUtil
 * @ Description 时间格式转换工具
 * @ author ZJ
 * @ date 2016/3/25
 * version 5.0.0
 */
public class DateUtil {

    /**
     * @ Description:转换时间格式为nnnnyydd
     * @ param cale 时间
     * @ author ZJ
     * @ return String
     * @ date 2016/3/15 14:08
     */
    public static String getTime_nnnnyydd(Calendar cale) {
        if (cale == null) {
            return "";
        }

        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DAY_OF_MONTH);
        int hour = cale.get(Calendar.HOUR_OF_DAY);
        int minute = cale.get(Calendar.MINUTE);
        int second = cale.get(Calendar.SECOND);
        String time = String.format(Locale.ENGLISH, "%d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute,
                second);
        return time;
    }

    /**
     * @ Description:时间转换
     * @ param time 待转换的时间20160407T000000Z
     * @ return Calendar格式的时间
     * @ author ZJ
     * @ date 2016/3/25 10:25
     */
    public static Calendar getCalendar(String time) {
        if (time.length() == 16) {
            int year = Integer.parseInt(time.substring(0, 4));
            int month = Integer.parseInt(time.substring(4, 6));
            int day = Integer.parseInt(time.substring(6, 8));
            int hour = Integer.parseInt(time.substring(9, 11));
            int minute = Integer.parseInt(time.substring(11, 13));
            int second = Integer.parseInt(time.substring(13, 15));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            return calendar;
        } else {
            return null;
        }

    }

    public static Calendar getCalendar(String year, String month, String day) {
        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
            return null;
        }
        int dwYear = Integer.parseInt(year);
        int dwMonth = Integer.parseInt(month);
        int dwDay = Integer.parseInt(day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, dwYear);
        calendar.set(Calendar.MONTH, dwMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dwDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    /**
     * @ Description:日期格式转换 yyyy-mm-dd
     * @ param
     * @ author ZJ
     * @ date 2016/3/31 13:59
     */
    public static String changeDateFormat(String year, String month, String day) {

        String tempMonth;
        String tempDay;
        if (Integer.parseInt(month) < 10) {
            tempMonth = "0" + month;
        } else {
            tempMonth = month;
        }

        if (Integer.parseInt(day) < 10) {
            tempDay = "0" + day;
        } else {
            tempDay = day;
        }
        return year + "-" + tempMonth + "-" + tempDay;
    }

    /**
     * @ Description:日期格式转换 yyyy-mm-dd
     * @ param
     * @ author ZJ
     * @ date 2016/3/31 13:59
     */
    public static String changeDateFormat(Calendar calendar) {
        String year = calendar.get(Calendar.YEAR) + "";
        String month = calendar.get(Calendar.MONTH) + 1 + "";
        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        return changeDateFormat(year, month, day);
    }

    /**
     * @param date 日期，格式为20160420
     * @Description: 日期格式由20160420转换为2016年4月20日，非中文下转换为Apr,20,2016
     * @author xiadaidai
     * @date 2016/4/20 17:30
     */
    public static String changeDateFormat(Context context, String date) {
        if (TextUtils.isEmpty(date) || date.length() != 8) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String year = date.substring(0, 4);

        String tempMonth = date.substring(4, 6);
        String month = "";
        try {
            month = String.valueOf(Integer.parseInt(tempMonth));//移除前面的0
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }

        String tempDay = date.substring(6, 8);
        String day = "";
        try {
            day = String.valueOf(Integer.parseInt(tempDay));//移除前面的0
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }

        if ("zh".equalsIgnoreCase(Locale.getDefault().getLanguage())) {//中文环境
            sb.append(year + context.getResources().getString(R.string.file_date_year));
            sb.append(month + context.getResources().getString(R.string.file_date_month));
            sb.append(day + context.getResources().getString(R.string.file_date_day));
        } else {//非中文环境
            sb.append(changeMonthToEnglish(tempMonth) + "," + day + "," + year);
        }

        return sb.toString();
    }

    /**
     * 将月份转换为英文，如四月为Apr
     *
     * @param month 月份，数字1~12
     * @return 英文月份，如Apr
     * @ author xiadaidai
     * @ date 2016/6/16 22:17
     */
    private static String changeMonthToEnglish(String month) {
        try {
            int mon = Integer.parseInt(month);
            switch (mon) {
                case 1:
                    return "Jan";
                case 2:
                    return "Feb";
                case 3:
                    return "Mar";
                case 4:
                    return "Apr";
                case 5:
                    return "May";
                case 6:
                    return "Jun";
                case 7:
                    return "Jul";
                case 8:
                    return "Aug";
                case 9:
                    return "Sep";
                case 10:
                    return "Oct";
                case 11:
                    return "Nov";
                case 12:
                    return "Dec";
                default:
                    return "Jan";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "Jan";
    }

    /**
     * @param dateStr 日期，格式为20160420
     * @Description: 根据日期获取需要显示的日期为今天，昨天和XXXX年XX月XX日
     * @author xiadaidai
     * @date 2016/4/22 16:04
     */
    public static String getDate(Context context, String dateStr) {
        if (TextUtils.isEmpty(dateStr) || dateStr.length() != 8) {
            return null;
        }

        String todayStr = getToday();

        long today = Long.parseLong(todayStr);
        long date = Long.parseLong(dateStr);

        String result = "";

        if (today - date == 0) {//今天
            result = context.getResources().getString(R.string.file_date_today);
        } else if (today - date == 1) {//昨天
            result = context.getResources().getString(R.string.file_date_yesterday);
        } else {
            result = changeDateFormat(context, dateStr);
        }
        return result;
    }

    /**
     * @param
     * @Description: 获取当天的日期，格式为XXXX年XX月XX日
     * @author xiadaidai
     * @date 2016/4/22 16:16
     */
    public static String getToday() {
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        sb.append(year);

        int tempMonth = calendar.get(Calendar.MONTH) + 1;
        String month = String.format("%02d", tempMonth);
        sb.append(month);

        int tempDay = calendar.get(Calendar.DAY_OF_MONTH);
        String day = String.format("%02d", tempDay);
        sb.append(day);

        return sb.toString();

    }

    /**
     * @param dateStr 日期，格式为20160420
     * @Description: 根据日期获取星期几
     * @author xiadaidai
     * @date 2016/4/22 16:26
     */
    public static String getWeek(Context context, String dateStr) {
        if (TextUtils.isEmpty(dateStr) || dateStr.length() != 8) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 6));
        int day = Integer.parseInt(dateStr.substring(6, 8));
        calendar.set(year, month - 1, day);//设置当前时间,月份是从0月开始计算
        int week = calendar.get(Calendar.DAY_OF_WEEK);//星期表示1-7，是从星期日开始
        String[] weekStr = {context.getResources().getString(R.string.file_week_sunday)
                , context.getResources().getString(R.string.file_week_monday)
                , context.getResources().getString(R.string.file_week_tuesday)
                , context.getResources().getString(R.string.file_week_wednesday)
                , context.getResources().getString(R.string.file_week_thursday)
                , context.getResources().getString(R.string.file_week_friday)
                , context.getResources().getString(R.string.file_week_saturday)};
        return weekStr[week - 1];
    }

    /**
     * @ Description:获取默认录像查询的开始日期
     * @ author ZJ
     * @ date 2016/3/21 17:03
     */
    public static Calendar getDefaultQueryRecordStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    /**
     * @ Description:Calendar格式转换成TimeTamp格式
     * @ param calendar 可为空，为空时，转换日期为当前时间
     * @ author ZJ
     * @ date 2016/4/8 10:00
     */
    public static String calendarToTimestamp(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp.toString();
    }

    /**
     * @ Description:Calendar格式转换成TimeTamp格式
     * @ param calendar 可为空，为空时，转换日期为当前时间
     * @ author ZJ
     * @ date 2016/4/8 10:00
     */
    public static Calendar timestampToCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        if (TextUtils.isEmpty(time)) {
            return calendar;
        }
        Timestamp timestamp = Timestamp.valueOf(time);
        calendar.setTime(timestamp);
        return calendar;
    }

    public static String formatDate(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
