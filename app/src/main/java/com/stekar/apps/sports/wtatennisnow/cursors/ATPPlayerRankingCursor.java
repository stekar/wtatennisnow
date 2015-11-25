package com.stekar.apps.sports.wtatennisnow.cursors;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.stekar.apps.sports.wtatennisnow.database.AppDatabase;
import com.stekar.apps.sports.wtatennisnow.models.ATPRanking;
import com.stekar.apps.sports.wtatennisnow.utils.BooleanUtils;

/**
 * Created by stekar on 1/22/15.
 */
public class ATPPlayerRankingCursor extends CursorWrapper {
    private boolean mHasNewsColumns;
    final static int COLUMN_NOT_FOUND = -1;

    public ATPPlayerRankingCursor(Cursor cursor) {
        super(cursor);
        if(cursor.getColumnIndex(AppDatabase.NewsDatabase.TITLE) == COLUMN_NOT_FOUND) {
            mHasNewsColumns = false;
        } else {
            mHasNewsColumns = true;
        }
    }

    public ATPRanking getRankingItem() {
        if(isBeforeFirst() || isAfterLast()) {
            return null;
        }

        ATPRanking rankingItem = new ATPRanking();

        int id = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.PLAYER_ID));
        rankingItem.setPlayerId(id);

        String firstName = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.FIRST_NAME));
        rankingItem.setPlayerFirstName(firstName);

        String lastName = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.LAST_NAME));
        rankingItem.setPlayerLastName(lastName);

        int points = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.POINTS));
        rankingItem.setPlayerTotalPoints(points);

        String webSite = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.SITE_URL));
        rankingItem.setPlayerWebSite(webSite);

        String photoUrl = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.PHOTO_URL));
        rankingItem.setPlayerPhotoUrl(photoUrl);

        String profileUrl = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.PROFILE_URL));
        rankingItem.setPlayerProfileUrl(profileUrl);

        int tournas = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TOURNAMENTS_YTD));
        rankingItem.setPlayerTournas(tournas);

        String moneyYTD = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_YTD));
        rankingItem.setPlayerPrizeMoneyCurrent(moneyYTD);

        String moneyTotal = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.PRIZE_MONEY_TOTAL));
        rankingItem.setPlayerPrizeMoneyTotal(moneyTotal);

        int age = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.AGE));
        rankingItem.setPlayerAge(age);

        String birthplace = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.BIRTHPLACE));
        rankingItem.setPlayerBirthplace(birthplace);

        int rankYTD = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.RANK_YTD));
        rankingItem.setPlayerRank(rankYTD);

        int rankHighest = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.RANK_HIGHEST));
        rankingItem.setPlayerRankHigh(rankHighest);

        int titlesYTD = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TITLES_YTD));
        rankingItem.setPlayerTitlesYTD(titlesYTD);

        int titlesTotal = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TITLES_TOTAL));
        rankingItem.setPlayerTitlesCareer(titlesTotal);

        int slamsYTD = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_YTD));
        rankingItem.setPlayerSlamTitlesYTD(slamsYTD);

        int slamsTotal = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TITLES_SLAMS_TOTAL));
        rankingItem.setPlayerSlamTitlesCareer(slamsTotal);

        int masters1000YTD = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_YTD));
        rankingItem.setPlayerMasters1000Titles(masters1000YTD);

        int masters1000Total = getInt(getColumnIndex(AppDatabase.PlayerRankingDatabase.TITLES_MASTERS1000_TOTAL));
        rankingItem.setPlayerMasters1000TitlesCareer(masters1000Total);

        String shareText = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.SHARE_TEXT));
        rankingItem.setShareText(shareText);

        String isStarred = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.IS_STARRED));
        rankingItem.setIsStarred(BooleanUtils.ordinalToBoolean(isStarred));

        String isShared = getString(getColumnIndex(AppDatabase.PlayerRankingDatabase.IS_SHARED));
        rankingItem.setIsShared(BooleanUtils.ordinalToBoolean(isShared));

        return rankingItem;
    }

    public boolean hasNewsColumns() {
        return mHasNewsColumns;
    }
}