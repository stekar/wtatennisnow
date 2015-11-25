package com.stekar.apps.sports.wtatennisnow.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by stekar on 12/24/14.
 */
public final class DateUtils {
    private static final HashMap<String, Integer> mDayMap = new HashMap<String, Integer>(7);
    private static final HashMap<String, Integer> mMonthMap = new HashMap<String, Integer>(12);

    // static initializer
    static {
        mDayMap.put("Sun", new Integer(1));
        mDayMap.put("Mon", new Integer(2));
        mDayMap.put("Tue", new Integer(3));
        mDayMap.put("Wed", new Integer(4));
        mDayMap.put("Thu", new Integer(5));
        mDayMap.put("Fri", new Integer(6));
        mDayMap.put("Sat", new Integer(7));

        mMonthMap.put("Jan", 0);
        mMonthMap.put("Feb", 1);
        mMonthMap.put("Mar", 2);
        mMonthMap.put("Apr", 3);
        mMonthMap.put("May", 4);
        mMonthMap.put("Jun", 5);
        mMonthMap.put("Jul", 6);
        mMonthMap.put("Aug", 7);
        mMonthMap.put("Sep", 8);
        mMonthMap.put("Oct", 9);
        mMonthMap.put("Nov", 10);
        mMonthMap.put("Dec", 11);
    }

    public static int dayOfWeek(String dayOfWeek) {
        return mDayMap.get(dayOfWeek);
    }

    public static int monthOfYear(String monthOfYear) {
        return mMonthMap.get(monthOfYear);
    }

    public static boolean isDayOfWeek(String srcDayOfWeek) {
        //Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 4 for WED

        return DateUtils.dayOfWeek(srcDayOfWeek) == dayOfWeek;
    }

    public static boolean isDayOfWeekYesterday(String srcDayOfWeek, String srcDayOfMonth) {
        Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));

        // create YESTERDAY date
        // extract YESTERDAY components, from that newly-created YESTERDAY date
        calendar.add(Calendar.DATE, -1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 4 for WED
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // 24 for the 24th

        return (DateUtils.dayOfWeek(srcDayOfWeek) == dayOfWeek &&
                Integer.parseInt(srcDayOfMonth) == dayOfMonth);
    }

    public static boolean isDayOfWeekThreeDaysPast(String srcDayOfWeek, String srcDayOfMonth) {
        Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));

        // create THREE_DAY date
        // extract THREE_DAY components, from that newly-created THREE_DAY date
        calendar.add(Calendar.DATE, -3);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 4 for WED
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // 24 for the 24th

        return (DateUtils.dayOfWeek(srcDayOfWeek) == dayOfWeek &&
                Integer.parseInt(srcDayOfMonth) == dayOfMonth);
    }

    public static boolean isDayOfWeekTwoDaysPast(String srcDayOfWeek, String srcDayOfMonth) {
        Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));

        // create TWO_DAY date
        // extract TWO_DAY components, from that newly-created TWO_DAY date
        calendar.add(Calendar.DATE, -2);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 4 for WED
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // 24 for the 24th

        return (DateUtils.dayOfWeek(srcDayOfWeek) == dayOfWeek &&
                Integer.parseInt(srcDayOfMonth) == dayOfMonth);
    }

    public static boolean isMonthOfYear(String srcMonthOfYear) {
        //Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int month = calendar.get(Calendar.MONTH); // 11 for DEC

        return DateUtils.monthOfYear(srcMonthOfYear) == month;
    }

    public static boolean isDayOfMonth(String srcDayOfMonth) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        //Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // 24 for the 24th

        return Integer.parseInt(srcDayOfMonth) == dayOfMonth;
    }
}
