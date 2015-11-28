package com.stekar.apps.sports.wtatennisnow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stekar.apps.sports.wtatennisnow.cursors.ATPNewsCursor;
import com.stekar.apps.sports.wtatennisnow.cursors.ATPPlayerRankingCursor;
import com.stekar.apps.sports.wtatennisnow.cursors.ATPTournasCursor;
import com.stekar.apps.sports.wtatennisnow.models.ATPNews;
import com.stekar.apps.sports.wtatennisnow.models.ATPRanking;
import com.stekar.apps.sports.wtatennisnow.models.ATPSchedule;

import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by stekar_work on 1/9/15.
 */
public class AppSqlHelper extends SQLiteOpenHelper {
    private static AppSqlHelper sInstance = null;
    private Context mContext;
    private ATPNewsSqlHelper mATPNewsSqlHelper;
    private ATPTournasSqlHelper mATPTournasSqlHelper;
    private ATPPlayerRankingSqlHelper mATPPlayerRankingSqlHelper;

    public static synchronized AppSqlHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppSqlHelper(context);
        }

        return sInstance;
    }

    private AppSqlHelper(Context ctx) {
        super(ctx, AppDatabase.DATABASE_FILENAME, null, AppDatabase.DATABASE_VERSION);
        mContext = ctx;
        mATPNewsSqlHelper = new ATPNewsSqlHelper();
        mATPTournasSqlHelper = new ATPTournasSqlHelper();
        mATPPlayerRankingSqlHelper = new ATPPlayerRankingSqlHelper();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            AppDatabase.NewsDatabase.createTable(mContext, sqLiteDatabase);
            AppDatabase.TournasDatabase.createTable(mContext, sqLiteDatabase);
            AppDatabase.PlayerRankingDatabase.createTable(mContext, sqLiteDatabase);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public ATPNewsSqlHelper getATPNewsHelper() {
        return mATPNewsSqlHelper;
    }

    public final class ATPNewsSqlHelper {
        public long insertNewsItem(ATPNews newsItem) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.PUB_DATE, newsItem.getPubDate());
            cv.put(AppDatabase.NewsDatabase.PUB_DATE_MILLIS, newsItem.getPubDateMillis());
            cv.put(AppDatabase.NewsDatabase.PUB_DATE_DAYS_OF_YEAR, newsItem.getPubDateDaysOfYear());

            /*String title = newsItem.getTitle();
            int hashedTitle = title.toLowerCase().hashCode();*/
            cv.put(AppDatabase.NewsDatabase.TITLE, newsItem.getTitle());
            cv.put(AppDatabase.NewsDatabase.COVER, newsItem.getCover());
            cv.put(AppDatabase.NewsDatabase.PLAYER_PHOTO_URL, newsItem.getPlayerPhotoUrl());
            cv.put(AppDatabase.NewsDatabase.HASHED_TITLE, newsItem.getHashedTitle());
            cv.put(AppDatabase.NewsDatabase.DESCRIPTION, newsItem.getDescription());
            cv.put(AppDatabase.NewsDatabase.LINK, newsItem.getLink());
            cv.put(AppDatabase.NewsDatabase.SHARE_TEXT, newsItem.getNewsShare());
            cv.put(AppDatabase.NewsDatabase.IS_STARRED, newsItem.getIsStarred());
            cv.put(AppDatabase.NewsDatabase.IS_SHARED, newsItem.getIsShared());
            cv.put(AppDatabase.NewsDatabase.IS_HIDDEN, newsItem.getIsIsHidden());
            cv.put(AppDatabase.NewsDatabase.IS_DELETED, newsItem.getIsDeleted());

            return getWritableDatabase().insertWithOnConflict(AppDatabase.NewsDatabase.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }

        public boolean updateNewsItemCover(ATPNews newsItem) {
            String cover = newsItem.getCover();
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.COVER, cover);

            // If 'cover' not present in the players.json file, NULL is returned
            // Different that 'winner' where and empty string is returned given entries are
            // like "winner:""
            String WHERE = AppDatabase.NewsDatabase.HASHED_TITLE + "=" + "'" + newsItem.getHashedTitle() + "'" + " AND " +
                    "( " + AppDatabase.NewsDatabase.COVER + "<>" + "'" + cover + "'" + " OR "
                    + AppDatabase.NewsDatabase.COVER + " ISNULL" + " )";
            int rowsUpdated = getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    WHERE,
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateNewsItemStarFlag(ATPNews newsItem, boolean isStarred) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_STARRED, isStarred);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase._ID + "=" + newsItem.getId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateNewsItemShareFlag(ATPNews newsItem, boolean isShared) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_SHARED, isShared);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase._ID + "=" + newsItem.getId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateNewsItemHiddenFlag(ATPNews newsItem, boolean isHidden) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_HIDDEN, isHidden);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase._ID + "=" + newsItem.getId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateNewsItemDeleteFlag(ATPNews newsItem, boolean isDeleted) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_DELETED, isDeleted);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase._ID + "=" + newsItem.getId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public ATPNewsCursor queryAtpNewsStarred() {
            final String WHERE = AppDatabase.NewsDatabase.IS_STARRED + "=" + "1" + " AND " +
                    AppDatabase.NewsDatabase.IS_DELETED + "<>" + "1";
            final String SORTING = AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc";

            return executeQuery(WHERE, SORTING);
        }

        public ATPNewsCursor queryAtpNewsShared() {
            Cursor wrapped = getReadableDatabase().query(AppDatabase.NewsDatabase.TABLE_NAME,
                    null,
                    AppDatabase.NewsDatabase.IS_SHARED + "=?",
                    new String[] {"1"},
                    null,
                    null,
                    AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc");

            return new ATPNewsCursor(wrapped);
        }

        public ATPNewsCursor queryAtpNewsHidden() {
            final String WHERE = AppDatabase.NewsDatabase.IS_HIDDEN + "=" + "1" + " AND " +
                    AppDatabase.NewsDatabase.IS_DELETED + "<>" + "1";
            final String SORTING = AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc";

            return executeQuery(WHERE, SORTING);
        }

        public int updateClearAtpNewsShared() {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_SHARED, false);
            return getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase.IS_SHARED + "=?",
                    new String[] {"1"}
            );
        }

        public int updateClearAtpNewsHidden() {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_HIDDEN, false);
            return getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase.IS_HIDDEN + "=?",
                    new String[] {"1"}
            );
        }

        public int updateClearAtpNewsDeleted() {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.NewsDatabase.IS_DELETED, false);
            return getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.NewsDatabase.IS_DELETED + "=?",
                    new String[] {"1"}
            );
        }

        public ATPNewsCursor queryAtpNewsRecent() {
            Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
            int daysOfYear = cal.get(Calendar.DAY_OF_YEAR);
            final String WHERE = AppDatabase.NewsDatabase.PUB_DATE_DAYS_OF_YEAR + ">=" + String.valueOf(daysOfYear - 1) + " AND "
                    + AppDatabase.NewsDatabase.IS_HIDDEN + "<>" + "1" +
                    " AND " + AppDatabase.NewsDatabase.IS_DELETED + "<>" + "1";

            final String SORTING = AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc";

            return executeQuery(WHERE, SORTING);
        }

        public ATPNewsCursor queryAtpNews() {
            final String WHERE = AppDatabase.NewsDatabase.IS_HIDDEN + "<>" + "1" + " AND " +
                    AppDatabase.NewsDatabase.IS_DELETED + "<>" + "1";
            final String SORTING = AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc";

            return executeQuery(WHERE, SORTING);
        }

        public ATPNewsCursor queryAtpNewsFromRowIds(List<Long> rowIds) {
            StringBuilder WHERE = new StringBuilder();

            for(int i = 0; i < rowIds.size(); i++) {
                WHERE.append(AppDatabase.NewsDatabase._ID);
                WHERE.append(" = ");
                WHERE.append(rowIds.get(i));

                if(i < rowIds.size() - 1) {
                    WHERE.append(" AND ");
                }
            }

            final String SORTING = AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc";

            return executeQuery(WHERE.toString(), SORTING);
        }

        public long queryAtpNewsDeletedCount() {
            return DatabaseUtils.queryNumEntries(getReadableDatabase(),
                    AppDatabase.NewsDatabase.TABLE_NAME,
                    AppDatabase.NewsDatabase.IS_DELETED + "=?",
                    new String[] {"1"});
        }

        public long queryAtpNewsHiddenCount() {
            return DatabaseUtils.queryNumEntries(getReadableDatabase(),
                    AppDatabase.NewsDatabase.TABLE_NAME,
                    AppDatabase.NewsDatabase.IS_HIDDEN + "=?",
                    new String[] {"1"});
        }

        private ATPNewsCursor executeQuery(String WHERE, String SORTNG) {
            Cursor wrapped = getReadableDatabase().query(AppDatabase.NewsDatabase.TABLE_NAME,
                    null,
                    WHERE,
                    null,
                    null,
                    null,
                    SORTNG);

            return new ATPNewsCursor(wrapped);
        }
    }

    public ATPTournasSqlHelper getATPTournasHelper() {
        return mATPTournasSqlHelper;
    }

    public final class ATPTournasSqlHelper {
        private final int CURRENT_WEEK = 0;
        private final int COMPLETED = 1;
        private final int ALL_MONTHS = 2;
        private final int MONTH_DELTA = 3;

        public long insertTournaItem(ATPSchedule tournaItem) {
            ContentValues cv = new ContentValues();

            cv.put(AppDatabase.TournasDatabase.TOURNA_ID, tournaItem.getTournaId());
            cv.put(AppDatabase.TournasDatabase.NAME, tournaItem.getTournaName());
            cv.put(AppDatabase.TournasDatabase.DATE_NUMBER, tournaItem.getTournaDay());
            cv.put(AppDatabase.TournasDatabase.DATE_MONTH, tournaItem.getTournaMonth());
            cv.put(AppDatabase.TournasDatabase.DATE_WEEK_START, tournaItem.getTournaWeekStart());
            cv.put(AppDatabase.TournasDatabase.DATE_WEEK_END, tournaItem.getTournaWeekEnd());
            cv.put(AppDatabase.TournasDatabase.CITY, tournaItem.getTournaCity());
            cv.put(AppDatabase.TournasDatabase.COUNTRY, tournaItem.getTournaCountry());
            cv.put(AppDatabase.TournasDatabase.SURFACE, tournaItem.getTournaSurface());
            cv.put(AppDatabase.TournasDatabase.IS_SLAM, tournaItem.getTournaSlam());
            cv.put(AppDatabase.TournasDatabase.IS_MASTERS1000, tournaItem.getTournaMaster());
            cv.put(AppDatabase.TournasDatabase.LINK, tournaItem.getTournaWebSite());
            cv.put(AppDatabase.TournasDatabase.MAP_TILE_NAME, tournaItem.getTournaMapTile());
            cv.put(AppDatabase.TournasDatabase.MAP_URL, tournaItem.getTournaMapUrl());
            cv.put(AppDatabase.TournasDatabase.IS_STARRED, tournaItem.getIsStarred());
            cv.put(AppDatabase.TournasDatabase.IS_SHARED, tournaItem.getIsShared());
            cv.put(AppDatabase.TournasDatabase.WINNER, tournaItem.getTournaWinner());
            cv.put(AppDatabase.TournasDatabase.PRIZE, tournaItem.getTournaPrizeMoney());
            cv.put(AppDatabase.TournasDatabase.POINTS, tournaItem.getTournaPoints());
            cv.put(AppDatabase.TournasDatabase.SHARE_TEXT, tournaItem.getTournaShareText());

            return getWritableDatabase().insertWithOnConflict(AppDatabase.TournasDatabase.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }

        public boolean updateTournaItemWinner(ATPSchedule scheduleItem) {
            String winner = scheduleItem.getTournaWinner();
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.TournasDatabase.WINNER, winner);

            String WHERE = AppDatabase.TournasDatabase.TOURNA_ID + "=" + scheduleItem.getTournaId() + " AND " +
                    AppDatabase.TournasDatabase.WINNER + "<>" + "'" + winner + "'";
            int rowsUpdated = getWritableDatabase().update(AppDatabase.TournasDatabase.TABLE_NAME,
                    cv,
                    WHERE,
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateTournaItemEventFlag(ATPSchedule scheduleItem, long eventId) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.TournasDatabase.EVENT_ID, eventId);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.TournasDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.TournasDatabase.TOURNA_ID + "=" + scheduleItem.getTournaId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateTournaItemShareFlag(ATPSchedule scheduleItem, boolean isShared) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.TournasDatabase.IS_SHARED, isShared);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.NewsDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.TournasDatabase.TOURNA_ID + "=" + scheduleItem.getTournaId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updateTournaItemStarFlag(ATPSchedule scheduleItem, boolean isStarred) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.TournasDatabase.IS_STARRED, isStarred);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.TournasDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.TournasDatabase.TOURNA_ID + "=" + scheduleItem.getTournaId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public ATPTournasCursor queryAtpTournaStarred() {
            final String WHERE = AppDatabase.TournasDatabase.IS_STARRED + "=1";
            return this.executeQuery(WHERE);
        }

        public ATPTournasCursor queryAtpTournasShared() {
            final String WHERE = AppDatabase.TournasDatabase.IS_SHARED + "=1";
            return this.executeQuery(WHERE);
        }

        public ATPTournasCursor queryAtpTournasSlam(int month) {
            if(month == ALL_MONTHS) {
                final String WHERE = AppDatabase.TournasDatabase.IS_SLAM + "=1";
                return this.executeQuery(WHERE);
            } else if(month == CURRENT_WEEK) {
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                final String WHERE = "(" + AppDatabase.TournasDatabase.DATE_WEEK_START + "=" + week + " OR " +
                        week + "=" +AppDatabase.TournasDatabase.DATE_WEEK_END + ") AND " + AppDatabase.TournasDatabase.IS_SLAM + "=1";
                return this.executeQuery(WHERE);
            } else if(month == COMPLETED) {
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                final String WHERE = AppDatabase.TournasDatabase.DATE_WEEK_END + " < " + week + " AND " + AppDatabase.TournasDatabase.IS_SLAM + "=1";
                return this.executeQuery(WHERE);
            } else {
                final String WHERE = AppDatabase.TournasDatabase.DATE_MONTH + "=" + String.valueOf(month-MONTH_DELTA) + " AND " + AppDatabase.TournasDatabase.IS_SLAM + "=1";
                return this.executeQuery(WHERE);
            }
        }

        public ATPTournasCursor queryAtpTournasMasters1000(int month) {
            if(month == ALL_MONTHS) {
                final String WHERE = AppDatabase.TournasDatabase.IS_MASTERS1000 + "=1";
                return this.executeQuery(WHERE);
            } else if(month == CURRENT_WEEK) {
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                final String WHERE = "(" + AppDatabase.TournasDatabase.DATE_WEEK_START + "=" + week + " OR " +
                        week + "=" +AppDatabase.TournasDatabase.DATE_WEEK_END + ") AND " + AppDatabase.TournasDatabase.IS_MASTERS1000 + "=1";
                return this.executeQuery(WHERE);
            }  else if(month == COMPLETED) {
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                final String WHERE = AppDatabase.TournasDatabase.DATE_WEEK_END + " < " + week + " AND " + AppDatabase.TournasDatabase.IS_MASTERS1000 + "= 1";
                return this.executeQuery(WHERE);
            } else {
                final String WHERE = AppDatabase.TournasDatabase.DATE_MONTH + "=" + String.valueOf(month-MONTH_DELTA) + " AND "  + AppDatabase.TournasDatabase.IS_MASTERS1000 + "=1";
                return this.executeQuery(WHERE);
            }
        }

        public ATPTournasCursor queryAtpTournas(int month) {
            if(month == ALL_MONTHS) {
                return this.executeQuery(null);
            } else if(month == CURRENT_WEEK) {
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                final String WHERE = AppDatabase.TournasDatabase.DATE_WEEK_START + "=" + week + " OR " +
                        week + "=" +AppDatabase.TournasDatabase.DATE_WEEK_END;
                return this.executeQuery(WHERE);
            }  else if(month == COMPLETED) {
                Calendar cal = Calendar.getInstance();
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                final String WHERE = AppDatabase.TournasDatabase.DATE_WEEK_END + " < " + week;
                return this.executeQuery(WHERE);
            } else {
                final String WHERE = AppDatabase.TournasDatabase.DATE_MONTH + "=" + String.valueOf(month-MONTH_DELTA);
                return this.executeQuery(WHERE);
            }
        }

        public ATPTournasCursor queryAtpTournasWithEvents() {
            final String WHERE = AppDatabase.TournasDatabase.EVENT_ID + ">0";
            return this.executeQuery(WHERE);
        }

        public long queryAtpTournasEventsCount() {
            return DatabaseUtils.queryNumEntries(getReadableDatabase(),
                    AppDatabase.TournasDatabase.TABLE_NAME,
                    AppDatabase.TournasDatabase.EVENT_ID + ">?",
                    new String[] {"0"});
        }

        private ATPTournasCursor executeQuery(String WHERE) {
            Cursor wrapped = getReadableDatabase().query(AppDatabase.TournasDatabase.TABLE_NAME,
                    null,
                    WHERE,
                    null,
                    null,
                    null,
                    AppDatabase.TournasDatabase.DATE_WEEK_START + " asc");

            return new ATPTournasCursor(wrapped);
        }
    }

    public ATPPlayerRankingSqlHelper getATPPlayerRankingHelper() {
        return mATPPlayerRankingSqlHelper;
    }

    public final class ATPPlayerRankingSqlHelper {
        public long insertPlayerRankingItem(ATPRanking rankingItem) {
            ContentValues cv = new ContentValues();

            cv.put(AppDatabase.PlayerRankingDatabase.PLAYER_ID, rankingItem.getPlayerId());
            cv.put(AppDatabase.PlayerRankingDatabase.FIRST_NAME, rankingItem.getPlayerFirstName());
            cv.put(AppDatabase.PlayerRankingDatabase.LAST_NAME, rankingItem.getPlayerLastName());
            cv.put(AppDatabase.PlayerRankingDatabase.SITE_URL, rankingItem.getPlayerWebSite());
            cv.put(AppDatabase.PlayerRankingDatabase.PROFILE_URL, rankingItem.getPlayerProfileUrl());
            cv.put(AppDatabase.PlayerRankingDatabase.PHOTO_URL, rankingItem.getPlayerPhotoUrl());
            cv.put(AppDatabase.PlayerRankingDatabase.POINTS, rankingItem.getPlayerTotalPoints());
            cv.put(AppDatabase.PlayerRankingDatabase.TOURNAMENTS_YTD, rankingItem.getPlayerTournas());
            cv.put(AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_YTD, rankingItem.getPlayerPrizeMoneyCurrent());
            cv.put(AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_TOTAL, rankingItem.getPlayerPrizeMoneyTotal());
            cv.put(AppDatabase.PlayerRankingDatabase.AGE, rankingItem.getPlayerAge());
            cv.put(AppDatabase.PlayerRankingDatabase.BIRTHPLACE, rankingItem.getPlayerBirthplace());
            cv.put(AppDatabase.PlayerRankingDatabase.RANK_YTD, rankingItem.getPlayerRank());
            cv.put(AppDatabase.PlayerRankingDatabase.RANK_HIGHEST, rankingItem.getPlayerRankHigh());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_YTD, rankingItem.getPlayerTitlesYTD());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_TOTAL, rankingItem.getPlayerTitlesCareer());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_YTD, rankingItem.getPlayerSlamTitlesYTD());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_TOTAL, rankingItem.getPlayerSlamTitlesCareer());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_YTD, rankingItem.getPlayerMasters1000TitlesYTD());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_TOTAL, rankingItem.getPlayerMasters1000TitlesCareer());
            cv.put(AppDatabase.PlayerRankingDatabase.SHARE_TEXT, rankingItem.getShareText());
            cv.put(AppDatabase.PlayerRankingDatabase.IS_STARRED, rankingItem.getIsStarred());
            cv.put(AppDatabase.PlayerRankingDatabase.IS_SHARED, rankingItem.getIsShared());

            return getWritableDatabase().insertWithOnConflict(AppDatabase.PlayerRankingDatabase.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }

        public ATPPlayerRankingCursor queryAtpPlayerRankings() {
            Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
            int daysOfYear = cal.get(Calendar.DAY_OF_YEAR);
            final String AND = " AND " + AppDatabase.NewsDatabase.PUB_DATE_DAYS_OF_YEAR + ">=" + String.valueOf(daysOfYear - 3);
            Cursor wrapped = getReadableDatabase().rawQuery("SELECT " +
                            AppDatabase.PlayerRankingDatabase.PLAYER_ID + "," +
                            AppDatabase.PlayerRankingDatabase.FIRST_NAME + "," +
                            AppDatabase.PlayerRankingDatabase.LAST_NAME + "," +
                            AppDatabase.PlayerRankingDatabase.SITE_URL + "," +
                            AppDatabase.PlayerRankingDatabase.PROFILE_URL + "," +
                            AppDatabase.PlayerRankingDatabase.PHOTO_URL + "," +
                            AppDatabase.PlayerRankingDatabase.POINTS + "," +
                            AppDatabase.PlayerRankingDatabase.TOURNAMENTS_YTD + "," +
                            AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_YTD + "," +
                            AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_TOTAL + "," +
                            AppDatabase.PlayerRankingDatabase.AGE + "," +
                            AppDatabase.PlayerRankingDatabase.BIRTHPLACE + "," +
                            AppDatabase.PlayerRankingDatabase.RANK_YTD + "," +
                            AppDatabase.PlayerRankingDatabase.RANK_HIGHEST + "," +
                            AppDatabase.PlayerRankingDatabase.TITLES_YTD + "," +
                            AppDatabase.PlayerRankingDatabase.TITLES_TOTAL + "," +
                            AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_YTD + "," +
                            AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_TOTAL + "," +
                            AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_YTD + "," +
                            AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_TOTAL + "," +
                            AppDatabase.PlayerRankingDatabase.SHARE_TEXT + "," +
                            AppDatabase.PlayerRankingDatabase.IS_STARRED + "," +
                            AppDatabase.PlayerRankingDatabase.IS_SHARED + "," +
                            " GROUP_CONCAT (" + AppDatabase.NewsDatabase.TITLE + ",'@') AS title" +
                            ", GROUP_CONCAT (" + AppDatabase.NewsDatabase.LINK + ",'@') AS link" +
                            ", GROUP_CONCAT (" + AppDatabase.NewsDatabase.PUB_DATE_MILLIS + ",'@') AS pubdatemillis" +
                            " FROM " +
                            AppDatabase.PlayerRankingDatabase.TABLE_NAME +
                            " LEFT JOIN " +
                            AppDatabase.NewsDatabase.TABLE_NAME +
                            " ON " + AppDatabase.PlayerRankingDatabase.PHOTO_URL + " = " +
                            AppDatabase.NewsDatabase.PLAYER_PHOTO_URL +
                            AND +
                            " GROUP BY " +
                            AppDatabase.PlayerRankingDatabase.PLAYER_ID +
                            " ORDER BY " + AppDatabase.PlayerRankingDatabase.POINTS + " desc",
                            //AppDatabase.NewsDatabase.PUB_DATE_MILLIS + " desc",
                    null);
            return new ATPPlayerRankingCursor(wrapped);

            /*final String WHERE = null;
            final String SORTING = AppDatabase.PlayerRankingDatabase.POINTS + " desc";

            return this.executeQuery(WHERE, SORTING);*/
        }

        public ATPPlayerRankingCursor queryAtpPlayerRankingsTitlesYtd() {
            final String WHERE = AppDatabase.PlayerRankingDatabase.TITLES_YTD + ">0";
            final String SORTING = AppDatabase.PlayerRankingDatabase.TITLES_YTD + " desc";

            return this.executeQuery(WHERE, SORTING);
        }

        public boolean updatePlayerRankingItemStarFlag(ATPRanking rankingItem, boolean isStarred) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.PlayerRankingDatabase.IS_STARRED, isStarred);

            int rowsUpdated = getWritableDatabase().update(AppDatabase.PlayerRankingDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.PlayerRankingDatabase.PLAYER_ID + "=" + rankingItem.getPlayerId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public ATPPlayerRankingCursor queryAtpPlayerRankingStarred() {
            final String WHERE = AppDatabase.PlayerRankingDatabase.IS_STARRED + "=1";
            final String SORTING = AppDatabase.PlayerRankingDatabase.POINTS + " desc";
            return this.executeQuery(WHERE, SORTING);
        }

        public boolean updatePlayerRankingItemPoints(ATPRanking rankingItem) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.PlayerRankingDatabase.POINTS, rankingItem.getPlayerTotalPoints());

            int rowsUpdated = getWritableDatabase().update(AppDatabase.PlayerRankingDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.PlayerRankingDatabase.PLAYER_ID + "=" + rankingItem.getPlayerId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        public boolean updatePlayerRankingItem(ATPRanking rankingItem) {
            ContentValues cv = new ContentValues();
            cv.put(AppDatabase.PlayerRankingDatabase.FIRST_NAME, rankingItem.getPlayerFirstName());
            cv.put(AppDatabase.PlayerRankingDatabase.LAST_NAME, rankingItem.getPlayerLastName());
            cv.put(AppDatabase.PlayerRankingDatabase.AGE, rankingItem.getPlayerAge());
            cv.put(AppDatabase.PlayerRankingDatabase.RANK_YTD, rankingItem.getPlayerRank());
            cv.put(AppDatabase.PlayerRankingDatabase.POINTS, rankingItem.getPlayerTotalPoints());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_YTD, rankingItem.getPlayerTitlesYTD());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_TOTAL, rankingItem.getPlayerTitlesCareer());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_YTD, rankingItem.getPlayerSlamTitlesYTD());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_TOTAL, rankingItem.getPlayerSlamTitlesCareer());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_YTD, rankingItem.getPlayerMasters1000TitlesYTD());
            cv.put(AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_TOTAL, rankingItem.getPlayerMasters1000TitlesCareer());
            cv.put(AppDatabase.PlayerRankingDatabase.TOURNAMENTS_YTD, rankingItem.getPlayerTournas());
            cv.put(AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_YTD, rankingItem.getPlayerPrizeMoneyCurrent());
            cv.put(AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_TOTAL, rankingItem.getPlayerPrizeMoneyTotal());
            cv.put(AppDatabase.PlayerRankingDatabase.PROFILE_URL, rankingItem.getPlayerProfileUrl());
            cv.put(AppDatabase.PlayerRankingDatabase.PHOTO_URL, rankingItem.getPlayerPhotoUrl());

            int rowsUpdated = getWritableDatabase().update(AppDatabase.PlayerRankingDatabase.TABLE_NAME,
                    cv,
                    AppDatabase.PlayerRankingDatabase.PLAYER_ID + "=" + rankingItem.getPlayerId(),
                    null);

            return rowsUpdated > 0 ? true : false;
        }

        private ATPPlayerRankingCursor executeQuery(String WHERE, String SORTNG) {
            Cursor wrapped = getReadableDatabase().query(AppDatabase.PlayerRankingDatabase.TABLE_NAME,
                    null,
                    WHERE,
                    null,
                    null,
                    null,
                    SORTNG);

            return new ATPPlayerRankingCursor(wrapped);
        }
    }

}
