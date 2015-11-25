package com.stekar.apps.sports.wtatennisnow.cursors;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.stekar.apps.sports.wtatennisnow.database.AppDatabase;
import com.stekar.apps.sports.wtatennisnow.models.ATPNews;
import com.stekar.apps.sports.wtatennisnow.utils.BooleanUtils;

/**
 * Created by stekar on 1/11/15.
 */
public class ATPNewsCursor extends CursorWrapper {
    public ATPNewsCursor(Cursor cursor) {
        super(cursor);
    }

    public ATPNews getNewsItem() {
        if(isBeforeFirst() || isAfterLast()) {
            return null;
        }

        ATPNews newsItem = new ATPNews();

        long millis = getLong(getColumnIndex(AppDatabase.NewsDatabase.PUB_DATE_MILLIS));
        newsItem.setPubDate(millis);

        String title = getString(getColumnIndex(AppDatabase.NewsDatabase.TITLE));
        newsItem.setTitle(title);

        String cover = getString(getColumnIndex(AppDatabase.NewsDatabase.COVER));
        newsItem.setCover(cover);

        String desc = getString(getColumnIndex(AppDatabase.NewsDatabase.DESCRIPTION));
        newsItem.setDescription(desc);

        String shareText = getString(getColumnIndex(AppDatabase.NewsDatabase.SHARE_TEXT));
        newsItem.setNewsShare(shareText);

        String isStarred = getString(getColumnIndex(AppDatabase.NewsDatabase.IS_STARRED));
        newsItem.setIsStarred(BooleanUtils.ordinalToBoolean(isStarred));

        String isShared = getString(getColumnIndex(AppDatabase.NewsDatabase.IS_SHARED));
        newsItem.setIsShared(BooleanUtils.ordinalToBoolean(isShared));

        String isHidden = getString(getColumnIndex(AppDatabase.NewsDatabase.IS_HIDDEN));
        newsItem.setIsHidden(BooleanUtils.ordinalToBoolean(isHidden));

        String isDeleted = getString(getColumnIndex(AppDatabase.NewsDatabase.IS_DELETED));
        newsItem.setIsDeleted(BooleanUtils.ordinalToBoolean(isDeleted));

        String link = getString(getColumnIndex(AppDatabase.NewsDatabase.LINK));
        newsItem.setLink(link);

        int id = getInt(getColumnIndex(AppDatabase.NewsDatabase._ID));
        newsItem.setId(id);

        return newsItem;
    }
}
