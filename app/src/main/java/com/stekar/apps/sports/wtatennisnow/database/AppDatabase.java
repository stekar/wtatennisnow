package com.stekar.apps.sports.wtatennisnow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by stekar_work on 1/9/15.
 */
public final class AppDatabase {
    public static final String DATABASE_FILENAME = "tennisnow.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static final class NewsDatabase implements BaseColumns {
        private static final String TAG = "TENNISNOW_NewsDb";
        private NewsDatabase() {
        }

        public static final String TABLE_NAME = "news";
        public static final String PUB_DATE = "pubdate";
        public static final String PUB_DATE_MILLIS = "pubdatemillis";
        public static final String PUB_DATE_DAYS_OF_YEAR = "pubdatedaysofyear";
        public static final String TITLE = "title";
        public static final String COVER = "titlecover";
        public static final String PLAYER_PHOTO_URL = "playerphotourl";
        public static final String HASHED_TITLE = "hashedtitle";
        public static final String DESCRIPTION = "description";
        public static final String LINK = "link";
        public static final String SHARE_TEXT = "sharetext";
        public static final String IS_STARRED = "isstarred";
        public static final String IS_SHARED = "isshared";
        public static final String IS_HIDDEN = "ishidden";
        public static final String IS_DELETED = "isdeleted";

        public static void createTable(Context ctx, SQLiteDatabase db) {
            if(db == null) {
                throw new IllegalArgumentException("Pass a valid database handle");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(NewsDatabase.TABLE_NAME).append(" (");
            sb.append(NewsDatabase._ID).append(" INTEGER").append(" PRIMARY KEY AUTOINCREMENT,");
            sb.append(NewsDatabase.HASHED_TITLE).append(" INTEGER").append(" UNIQUE,");
            sb.append(NewsDatabase.PUB_DATE).append(" TEXT,");
            sb.append(NewsDatabase.PUB_DATE_MILLIS).append(" INTEGER,");
            sb.append(NewsDatabase.PUB_DATE_DAYS_OF_YEAR).append(" INTEGER,");
            sb.append(NewsDatabase.TITLE).append(" TEXT,");
            sb.append(NewsDatabase.COVER).append(" TEXT,");
            sb.append(NewsDatabase.PLAYER_PHOTO_URL).append(" TEXT,");
            sb.append(NewsDatabase.DESCRIPTION).append(" TEXT,");
            sb.append(NewsDatabase.LINK).append(" TEXT,");
            sb.append(NewsDatabase.SHARE_TEXT).append(" TEXT,");
            sb.append(NewsDatabase.IS_STARRED).append(" TEXT,");
            sb.append(NewsDatabase.IS_SHARED).append(" TEXT, ");
            sb.append(NewsDatabase.IS_HIDDEN).append(" TEXT, ");
            sb.append(NewsDatabase.IS_DELETED).append(" TEXT");

            sb.append(");");
            db.execSQL(sb.toString());

            Log.d(TAG, "News Table created successfully");
        }
    }

    public static final class TournasDatabase implements BaseColumns {
        private static final String TAG = "TENNISNOW_TournasDb";
        private TournasDatabase() {
        }

        public static final String TABLE_NAME = "tournas_2016";
        public static final String TOURNA_ID = "tournaid";
        public static final String NAME = "name";
        public static final String LINK = "link";
        public static final String MAP_TILE_NAME = "maptilename";
        public static final String MAP_URL = "mapurl";
        public static final String POINTS = "points";
        public static final String DATE_NUMBER = "datenumber";
        public static final String DATE_MONTH = "datemonth";
        public static final String DATE_YEAR = "dateyear";
        public static final String DATE_WEEK_START = "dateweekstart";
        public static final String DATE_WEEK_END = "dateweekend";
        public static final String CITY = "city";
        public static final String COUNTRY = "country";
        public static final String SURFACE = "surface";
        public static final String INDOOR = "indoor";
        public static final String PRIZE = "prize";
        public static final String WINNER = "winner";
        public static final String IS_STARRED = "isstarred";
        public static final String IS_SHARED = "isshared";
        public static final String IS_SLAM = "isslam";
        public static final String IS_PREMIER = "ispremier";
        public static final String SHARE_TEXT = "sharetext";
        public static final String EVENT_ID = "eventid";


        public static void createTable(Context ctx, SQLiteDatabase db) {
            if(db == null) {
                throw new IllegalArgumentException("Pass a valid database handle");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(TournasDatabase.TABLE_NAME).append(" (");
            sb.append(TournasDatabase._ID).append(" INTEGER").append(" PRIMARY KEY AUTOINCREMENT,");
            sb.append(TournasDatabase.TOURNA_ID).append(" INTEGER").append(" UNIQUE,");
            sb.append(TournasDatabase.NAME).append(" TEXT,");
            sb.append(TournasDatabase.IS_SLAM).append(" TEXT,");
            sb.append(TournasDatabase.IS_PREMIER).append(" TEXT,");
            sb.append(TournasDatabase.LINK).append(" TEXT,");
            sb.append(TournasDatabase.MAP_TILE_NAME).append(" TEXT,");
            sb.append(TournasDatabase.MAP_URL).append(" TEXT,");
            sb.append(TournasDatabase.DATE_NUMBER).append(" TEXT,");
            sb.append(TournasDatabase.DATE_MONTH).append(" INTEGER,");
            sb.append(TournasDatabase.DATE_YEAR).append(" INTEGER,");
            sb.append(TournasDatabase.DATE_WEEK_START).append(" INTEGER,");
            sb.append(TournasDatabase.DATE_WEEK_END).append(" INTEGER,");
            sb.append(TournasDatabase.CITY).append(" TEXT,");
            sb.append(TournasDatabase.COUNTRY).append(" TEXT,");
            sb.append(TournasDatabase.SURFACE).append(" TEXT,");
            sb.append(TournasDatabase.INDOOR).append(" TEXT,");
            sb.append(TournasDatabase.PRIZE).append(" TEXT,");
            sb.append(TournasDatabase.POINTS).append(" TEXT,");
            sb.append(TournasDatabase.WINNER).append(" TEXT,");
            sb.append(TournasDatabase.IS_STARRED).append(" TEXT,");
            sb.append(TournasDatabase.IS_SHARED).append(" TEXT,");
            sb.append(TournasDatabase.SHARE_TEXT).append(" TEXT,");
            sb.append(TournasDatabase.EVENT_ID).append(" INTEGER");

            sb.append(");");
            db.execSQL(sb.toString());

            Log.d(TAG, "Tournas Table created successfully");
        }
    }


    public static final class PlayerRankingDatabase implements BaseColumns {
        private static final String TAG = "TENNISNOW_RankingDb";
        private PlayerRankingDatabase() {
        }

        public static final String TABLE_NAME = "ranking";
        public static final String PLAYER_ID = "playerid";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String POINTS = "points";
        public static final String SITE_URL = "siteurl";
        public static final String PROFILE_URL = "profileurl";
        public static final String PHOTO_URL = "photourl";
        public static final String TOURNAMENTS_YTD = "tournasytd";
        public static final String PRIZE_MONEY_YTD = "prizemoneyytd";
        public static final String PRIZE_MONEY_TOTAL = "prizemoneytotal";
        public static final String AGE = "age";
        public static final String BIRTHPLACE = "birthplace";
        public static final String RANK_YTD = "rankytd";
        public static final String RANK_HIGHEST = "rankhighest";
        public static final String TITLES_YTD = "titlesytd";
        public static final String TITLES_TOTAL = "titlestotal";
        public static final String TITLES_SLAMS_YTD = "titlesslamsytd";
        public static final String TITLES_SLAMS_TOTAL = "titlesslamstotal";
        public static final String TITLES_MASTERS1000_YTD = "titlesmasters1000ytd";
        public static final String TITLES_MASTERS1000_TOTAL = "titlesmasters1000total";
        public static final String SHARE_TEXT = "rankingsharetext";
        public static final String IS_STARRED = "rankingisstarred";
        public static final String IS_SHARED = "rankingisshared";

        public static void createTable(Context ctx, SQLiteDatabase db) {
            if(db == null) {
                throw new IllegalArgumentException("Pass a valid database handle");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(PlayerRankingDatabase.TABLE_NAME).append(" (");
            sb.append(PlayerRankingDatabase._ID).append(" INTEGER").append(" PRIMARY KEY AUTOINCREMENT,");
            sb.append(PlayerRankingDatabase.PLAYER_ID).append(" INTEGER").append(" UNIQUE,");
            sb.append(PlayerRankingDatabase.FIRST_NAME).append(" TEXT,");
            sb.append(PlayerRankingDatabase.LAST_NAME).append(" TEXT,");
            sb.append(PlayerRankingDatabase.SITE_URL).append(" TEXT,");
            sb.append(PlayerRankingDatabase.PROFILE_URL).append(" TEXT,");
            sb.append(PlayerRankingDatabase.PHOTO_URL).append(" TEXT,");
            sb.append(PlayerRankingDatabase.POINTS).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TOURNAMENTS_YTD).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.PRIZE_MONEY_YTD).append(" TEXT,");
            sb.append(PlayerRankingDatabase.PRIZE_MONEY_TOTAL).append(" TEXT,");
            sb.append(PlayerRankingDatabase.AGE).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.BIRTHPLACE).append(" TEXT,");
            sb.append(PlayerRankingDatabase.RANK_YTD).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.RANK_HIGHEST).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TITLES_YTD).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TITLES_TOTAL).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TITLES_SLAMS_YTD).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TITLES_SLAMS_TOTAL).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TITLES_MASTERS1000_YTD).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.TITLES_MASTERS1000_TOTAL).append(" INTEGER,");
            sb.append(PlayerRankingDatabase.SHARE_TEXT).append(" TEXT,");
            sb.append(PlayerRankingDatabase.IS_STARRED).append(" TEXT,");
            sb.append(PlayerRankingDatabase.IS_SHARED).append(" TEXT");

            sb.append(");");
            db.execSQL(sb.toString());

            Log.d(TAG, "Ranking Table created successfully");
        }
    }
}
