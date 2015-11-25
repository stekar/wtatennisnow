package com.stekar.apps.sports.wtatennisnow.cursors;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.stekar.apps.sports.wtatennisnow.database.AppDatabase;
import com.stekar.apps.sports.wtatennisnow.models.ATPSchedule;
import com.stekar.apps.sports.wtatennisnow.utils.BooleanUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by stekar_work on 1/14/15.
 */
public class ATPTournasCursor extends CursorWrapper {
    public ATPTournasCursor(Cursor cursor) {
        super(cursor);
    }

    public ATPSchedule getScheduleItem() {
        if (isBeforeFirst() || isAfterLast()) {
            return null;
        }

        ATPSchedule tournaItem = new ATPSchedule();

        int id = getInt(getColumnIndex(AppDatabase.TournasDatabase.TOURNA_ID));
        tournaItem.setTournaId(id);

        String name = getString(getColumnIndex(AppDatabase.TournasDatabase.NAME));
        tournaItem.setTournaName(name);

        String day = getString(getColumnIndex(AppDatabase.TournasDatabase.DATE_NUMBER));
        tournaItem.setTournaDay(day);

        int month = getInt(getColumnIndex(AppDatabase.TournasDatabase.DATE_MONTH));
        tournaItem.setTournaMonth(month);

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.MONTH, month);
        String monthDisplayName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        tournaItem.setTournaMonthDisplayName(monthDisplayName);

        int weekStart = getInt(getColumnIndex(AppDatabase.TournasDatabase.DATE_WEEK_START));
        tournaItem.setTournaWeekStart(weekStart);

        int weekEnd = getInt(getColumnIndex(AppDatabase.TournasDatabase.DATE_WEEK_END));
        tournaItem.setTournaWeekEnd(weekEnd);

        String city = getString(getColumnIndex(AppDatabase.TournasDatabase.CITY));
        tournaItem.setTournaCity(city);

        String country = getString(getColumnIndex(AppDatabase.TournasDatabase.COUNTRY));
        tournaItem.setTournaCountry(country);

        String surface = getString(getColumnIndex(AppDatabase.TournasDatabase.SURFACE));
        tournaItem.setTournaSurface(surface);

        String mapTile = getString(getColumnIndex(AppDatabase.TournasDatabase.MAP_TILE_NAME));
        tournaItem.setTournaMapTile(mapTile);

        String mapUrl = getString(getColumnIndex(AppDatabase.TournasDatabase.MAP_URL));
        tournaItem.setTournaMapUrl(mapUrl);

        String isSlam = getString(getColumnIndex(AppDatabase.TournasDatabase.IS_SLAM));
        tournaItem.setTournaSlam(BooleanUtils.ordinalToBoolean(isSlam));

        String isMasters = getString(getColumnIndex(AppDatabase.TournasDatabase.IS_MASTERS1000));
        tournaItem.setTournaMaster(BooleanUtils.ordinalToBoolean(isMasters));

        String isShared = getString(getColumnIndex(AppDatabase.TournasDatabase.IS_SHARED));
        tournaItem.setIsShared(BooleanUtils.ordinalToBoolean(isShared));

        String isStarred = getString(getColumnIndex(AppDatabase.TournasDatabase.IS_STARRED));
        tournaItem.setIsStarred(BooleanUtils.ordinalToBoolean(isStarred));

        String link = getString(getColumnIndex(AppDatabase.TournasDatabase.LINK));
        tournaItem.setTournaWebSite(link);

        String winner = getString(getColumnIndex(AppDatabase.TournasDatabase.WINNER));
        tournaItem.setTournaWinner(winner);

        String prize = getString(getColumnIndex(AppDatabase.TournasDatabase.PRIZE));
        tournaItem.setTournaPrizeMoney(prize);

        String points = getString(getColumnIndex(AppDatabase.TournasDatabase.POINTS));
        tournaItem.setTournaPoints(points);

        String shareText = getString(getColumnIndex(AppDatabase.TournasDatabase.SHARE_TEXT));
        tournaItem.setTournaShareText(shareText);

        long eventId = getLong(getColumnIndex(AppDatabase.TournasDatabase.EVENT_ID));
        tournaItem.setTournaEventId(eventId);

        return tournaItem;
    }
}