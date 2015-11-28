package com.stekar.apps.sports.wtatennisnow.models;

import android.os.Parcel;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.utils.DateUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class ATPNews {
    private final String TAG = "TENNISNOW_ATPNEWS";
    private String mPubDate;
    private long mPubDateMillis;
    private int mPubDateDaysOfYear;
    private String mTitle;
    private int mHashedTitle;
    private String mLink;
    private String mPubDateDay;
    private String mPubDateNumber;
    private String mPubDateMonth;
    private String mPubDateYear;
    private String mPubDateHr;
    private String mPubDateMin;
    private String mPubDateSec;
    private String mNewsShare;
    private String mDescription;
    private String mCover;
    private String mPlayerPhotoUrl;
    private int mId;
    private boolean mIsPubDateToday;
    private boolean mIsPubDateYesterday;
    private boolean mIsPubDateThreeDay;
    private boolean mIsPubDateTwoDay;
    private boolean mIsStarred;
    private boolean mIsShared;
    private boolean mIsHidden;
    private boolean mIsDeleted;

    private final static String DELIMETER = " ";

    public ATPNews() {
    }

    @Override
    public String toString() {
        return mTitle + DELIMETER + mPubDate + DELIMETER + mLink;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public String getPubDateDay() { return mPubDateDay; }

    public String getPubDateMonth() { return mPubDateMonth; }

    public String getPubDateNumber() { return mPubDateNumber; }

    /**
     * @return the mPubDate
     */
    public String getPubDate() {
        return mPubDate;
    }

    // hydrate Calendar from the millis coming form the Database
    public void setPubDate(long pubDateMillis) {
        TimeZone timeZone = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(pubDateMillis);

        int month = cal.get(Calendar.MONTH);
        mPubDateMonth = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        mPubDateNumber = String.valueOf(day);
        mPubDateDay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US).toUpperCase(Locale.US);

        int year = cal.get(Calendar.YEAR);
        mPubDateYear = String.valueOf(year);

        int hr = cal.get(Calendar.HOUR_OF_DAY);
        mPubDateHr = String.valueOf(hr);

        int min = cal.get(Calendar.MINUTE);
        mPubDateMin = String.valueOf(min);

        int sec = cal.get(Calendar.SECOND);
        mPubDateSec = String.valueOf(sec);

        this.setPubDateFlags(day, month, timeZone);
    }

    private void setPubDateFlags(int day, int month, TimeZone timeZone) {
        this.setTodayFlag(day, month, timeZone);
        if(mIsPubDateToday == false) {
            this.setYesterdayFlag(day, month, timeZone);
            if(mIsPubDateYesterday == false) {
                this.setTwoDaysFlag(day, month, timeZone);
                if(mIsPubDateTwoDay == false) {
                    this.setThreeDaysFlag(day, month, timeZone);
                }
            }
        }
    }

    private boolean setDayFlag(int day, int month, TimeZone timeZone, int dayDelta) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.add(Calendar.DATE, dayDelta);

        int pastDay = cal.get(Calendar.DAY_OF_MONTH);
        int pastMonth = cal.get(Calendar.MONTH);
        return (day == pastDay && month == pastMonth);
    }

    private void setTodayFlag(int day, int month, TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);
        int todayMonth = cal.get(Calendar.MONTH);
        if(day == todayDay && month == todayMonth) {
            mIsPubDateToday = true;
        } else {
            mIsPubDateToday = false;
        }
    }

    private void setYesterdayFlag(int day, int month, TimeZone timeZone) {
        mIsPubDateYesterday = this.setDayFlag(day, month, timeZone, -1);
    }

    private void setTwoDaysFlag(int day, int month, TimeZone timeZone) {
        mIsPubDateTwoDay = this.setDayFlag(day, month, timeZone, -2);
    }

    private void setThreeDaysFlag(int day, int month, TimeZone timeZone) {
        mIsPubDateThreeDay = this.setDayFlag(day, month, timeZone, -3);
    }
    /**
     * @param pubDate the mPubDate to set
     */
    public void setPubDate(String pubDate) {
        this.mPubDate = pubDate;

        // only the ATPNewsParser is setting that flag to TRUE
        try {
            if (pubDate != null) {
                int commaPos = pubDate.indexOf(",");
                if (commaPos != -1) {
                    String[] pubDateSplit = pubDate.split(",");
                    if (pubDateSplit != null && pubDateSplit.length == 2) {
                        mPubDateDay = pubDateSplit[0];
                        String[] pubDateRightSplit = pubDateSplit[0].split(" ");
                        if (pubDateRightSplit != null && pubDateRightSplit.length > 2) {
                            mPubDateNumber = pubDateRightSplit[1];
                            mPubDateMonth = pubDateRightSplit[0];
                            mPubDateYear = pubDateRightSplit[2];

                            String[] timeSplit = pubDateSplit[1].split(":");
                            if(timeSplit != null && timeSplit.length > 1) {
                                mPubDateHr = timeSplit[0].trim();
                                mPubDateMin = timeSplit[1];
                                mPubDateSec = "0";
                            }
                        }
                    }
                }
            }

            //Set Calendar Millis
            Calendar cal = new GregorianCalendar(new SimpleTimeZone(0, "GMT"));
            cal.clear();

            int year = Integer.parseInt(mPubDateYear);
            int month = DateUtils.monthOfYear(mPubDateMonth);
            int day = Integer.parseInt(mPubDateNumber);
            int hr = Integer.parseInt(mPubDateHr);
            int min = Integer.parseInt(mPubDateMin);
            int sec = Integer.parseInt(mPubDateSec);

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, hr);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, sec);

            mPubDateDaysOfYear = cal.get(Calendar.DAY_OF_YEAR);
            mPubDateMillis = cal.getTimeInMillis();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isPubDateToday() { return mIsPubDateToday; }
    public boolean isPubDateYesterday() { return mIsPubDateYesterday; }
    public boolean isPubDateThreeDay() { return mIsPubDateThreeDay; }
    public boolean isPubDateTwoDay() { return mIsPubDateTwoDay; }

    /**
     * @return the mTitle
     */
    public String getTitle() {
        return mTitle;
    }
    /**
     * @param title the mTitle to set
     */
    public void setTitle(String title) {
        this.mTitle = title;
        mHashedTitle = mTitle.toLowerCase().hashCode();
    }
    /**
     * @return the mLink
     */
    public String getLink() {
        return mLink;
    }
    /**
     * @param link the mLink to set
     */
    public void setLink(String link) {
        this.mLink = link;
    }

    private ATPNews(Parcel in) {
        mPubDate = in.readString();
        mTitle = in.readString();
        mLink = in.readString();

    }

    public String getNewsShare() {
        return mNewsShare;
    }

    public void setNewsShare(String mNewsShare) {
        this.mNewsShare = mNewsShare;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean getIsStarred() { return mIsStarred; }
    public void setIsStarred(boolean flag) { mIsStarred = flag; }

    public boolean getIsShared() { return mIsShared; }
    public void setIsShared(boolean flag) { mIsShared = flag; }

    public long getPubDateMillis() { return mPubDateMillis; }
    public void setPubDateMillis(long m) { mPubDateMillis = m; }

    public String getPubDateHr() { return mPubDateHr; }
    public void setPubDateHr(String hr) { mPubDateHr = hr; }

    public String getPubDateMin() { return mPubDateMin; }
    public void setPubDateMin(String min) { mPubDateMin = min; }

    public String getPubDateSec() { return mPubDateSec; }
    public void setPubDateSec(String sec) { mPubDateSec = sec; }

    public String getPubDateYear() { return mPubDateYear; }
    public void setPubDateYear(String year) { mPubDateYear = year; }

    public int getPubDateDaysOfYear() {
        return mPubDateDaysOfYear;
    }

    public void setPubDateDaysOfYear(int pubDateDaysOfYear) {
        mPubDateDaysOfYear = pubDateDaysOfYear;
    }

    public String getCover() {
        return mCover;
    }

    public void setCover(String cover) {
        mCover = cover;
    }

    public int getHashedTitle() {
        return mHashedTitle;
    }

    public boolean getIsIsHidden() {
        return mIsHidden;
    }

    public void setIsHidden(boolean isHidden) {
        mIsHidden = isHidden;
    }

    public boolean getIsDeleted() {
        return mIsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        mIsDeleted = isDeleted;
    }

    public String getPlayerPhotoUrl() {
        return mPlayerPhotoUrl;
    }

    public void setPlayerPhotoUrl(String playerPhotoUrl) {
        this.mPlayerPhotoUrl = playerPhotoUrl;
    }
}
