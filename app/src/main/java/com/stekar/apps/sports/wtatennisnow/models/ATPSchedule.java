package com.stekar.apps.sports.wtatennisnow.models;

/**
 * Created by stekar on 12/25/14.
 */
public class ATPSchedule {
    public ATPSchedule() {
        mTournaEventId = 0;
    }

    private int tournaId;
    private String tournaName;
    private String tournaCountry;
    private String tournaCity;
    private int tournaMonth;
    private int mTournaWeekStart;
    private int mTournaWeekEnd;
    private long mTournaEventId;
    private String mTournaMonthDisplayName;
    private String tournaDay;
    private String tournaPoints;
    private String tournaSurface;
    private boolean tournaSlam;
    private boolean tournaPremier;
    private String tournaPrizeMoney;
    private String tournaWebSite;
    private String tournaMapUrl;
    private String tournaMapTile;
    private String tournaWinner;
    private boolean mIsStarred;
    private boolean mIsShared;
    private String mTournaShareText;

    public int getTournaId() { return tournaId; }
    public void setTournaId(int id) { tournaId = id; }

    public String getTournaWinner() { return tournaWinner; }
    public void setTournaWinner(String winner) { tournaWinner = winner; }

    public String getTournaName() {
        return tournaName;
    }
    public void setTournaName(String tournaName) {
        this.tournaName = tournaName;
    }

    public String getTournaCountry() {
        return tournaCountry;
    }
    public void setTournaCountry(String tournaCountry) {
        this.tournaCountry = tournaCountry;
    }

    public String getTournaCity() {
        return tournaCity;
    }
    public void setTournaCity(String tournaCity) {
        this.tournaCity = tournaCity;
    }

    public int getTournaMonth() {
        return tournaMonth;
    }
    public void setTournaMonth(int tournaMonth) {
        this.tournaMonth = tournaMonth;
    }

    public String getTournaDay() {
        return tournaDay;
    }
    public void setTournaDay(String tournaDay) {
        this.tournaDay = tournaDay;
    }

    public String getTournaPoints() {
        return tournaPoints;
    }
    public void setTournaPoints(String tournaPoints) {
        this.tournaPoints = tournaPoints;
    }

    public String getTournaSurface() {
        return tournaSurface;
    }
    public void setTournaSurface(String tournaSurface) {
        this.tournaSurface = tournaSurface;
    }

    public boolean getTournaSlam() {
        return tournaSlam;
    }
    public void setTournaSlam(boolean tournaSlam) {
        this.tournaSlam = tournaSlam;
    }

    public boolean getTournaPremier() {
        return tournaPremier;
    }
    public void setTournaPremier(boolean tournaPremier) {
        this.tournaPremier = tournaPremier;
    }

    public String getTournaPrizeMoney() {
        return tournaPrizeMoney;
    }
    public void setTournaPrizeMoney(String tournaPrizeMoney) {
        this.tournaPrizeMoney = tournaPrizeMoney;
    }

    public String getTournaWebSite() {
        return tournaWebSite;
    }
    public void setTournaWebSite(String tournaWebSite) {
        this.tournaWebSite = tournaWebSite;
    }

    public String getTournaMapUrl() {
        return tournaMapUrl;
    }
    public void setTournaMapUrl(String tournaMapUrl) {
        this.tournaMapUrl = tournaMapUrl;
    }

    public String getTournaMapTile() {
        return tournaMapTile;
    }
    public void setTournaMapTile(String tournaMapTile) {
        this.tournaMapTile = tournaMapTile;
    }

    public boolean getIsStarred() { return mIsStarred; }
    public void setIsStarred(boolean flag) { mIsStarred = flag; }

    public boolean getIsShared() { return mIsShared; }
    public void setIsShared(boolean flag) { mIsShared = flag; }

    public String getTournaMonthDisplayName() {
        return mTournaMonthDisplayName;
    }

    public void setTournaMonthDisplayName(String tournaMonthDisplayName) {
        mTournaMonthDisplayName = tournaMonthDisplayName;
    }

    public int getTournaWeekStart() {
        return mTournaWeekStart;
    }
    public void setTournaWeekStart(int tournaWeek) {
        mTournaWeekStart = tournaWeek;
    }

    public int getTournaWeekEnd() {
        return mTournaWeekEnd;
    }
    public void setTournaWeekEnd(int tournaWeek) {
        mTournaWeekEnd = tournaWeek;
    }

    public String getTournaShareText() {
        return mTournaShareText;
    }

    public void setTournaShareText(String tournaShareText) {
        mTournaShareText = tournaShareText;
    }

    public long getTournaEventId() {
        return mTournaEventId;
    }

    public void setTournaEventId(long tournaEventId) {
        mTournaEventId = tournaEventId;
    }
}
