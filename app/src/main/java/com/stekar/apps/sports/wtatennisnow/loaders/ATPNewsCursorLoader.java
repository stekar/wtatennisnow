package com.stekar.apps.sports.wtatennisnow.loaders;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import com.stekar.apps.sports.wtatennisnow.broadcastreceivers.ATPNewsCursorBroadcastReceiver;
import com.stekar.apps.sports.wtatennisnow.constants.Constants;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.fragments.PlaceholderFragmentNews;

/**
 * Created by stekar on 1/11/15.
 */
public class ATPNewsCursorLoader extends SQLiteCursorLoader {
    ATPNewsCursorBroadcastReceiver mATPNewsCursorBroadcastReceiver;
    Context mContext;
    private int mQueryNewsType;

    public ATPNewsCursorLoader(Context context, int queryNewsType) {
        super(context);
        mContext = context;
        mATPNewsCursorBroadcastReceiver = new ATPNewsCursorBroadcastReceiver(this);
        mQueryNewsType = queryNewsType;

        this.registerReceiver();
    }

    private void unregisterReceiver() {
        if (mATPNewsCursorBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mATPNewsCursorBroadcastReceiver);
            mATPNewsCursorBroadcastReceiver = null;
        }
    }

    private void registerReceiver() {
        IntentFilter statusIntentFilter = new IntentFilter();
        statusIntentFilter.addAction(Constants.BROADCAST_NEWS_CURSOR_LOADER_READY);
        statusIntentFilter.addAction(Constants.BROADCAST_NEWS_READY);
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mATPNewsCursorBroadcastReceiver,
                statusIntentFilter);
    }

    @Override
    protected Cursor loadCursor() {
        if(mQueryNewsType == PlaceholderFragmentNews.HEADER_SELECTION_RECENT) {
            return AppSqlHelper.getInstance(mContext).getATPNewsHelper().queryAtpNewsRecent();
        } else if(mQueryNewsType == PlaceholderFragmentNews.HEADER_SELECTION_ALL) {
            return AppSqlHelper.getInstance(mContext).getATPNewsHelper().queryAtpNews();
        } else if(mQueryNewsType == PlaceholderFragmentNews.HEADER_SELECTION_STARRED) {
            return AppSqlHelper.getInstance(mContext).getATPNewsHelper().queryAtpNewsStarred();
        } else if(mQueryNewsType == PlaceholderFragmentNews.HEADER_SELECTION_SHARED) {
            return AppSqlHelper.getInstance(mContext).getATPNewsHelper().queryAtpNewsShared();
        } else {
            return AppSqlHelper.getInstance(mContext).getATPNewsHelper().queryAtpNewsHidden();
        }
    }
}
