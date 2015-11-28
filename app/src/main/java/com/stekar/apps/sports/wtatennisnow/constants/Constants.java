package com.stekar.apps.sports.wtatennisnow.constants;

/**
 * Created by stekar on 12/6/14.
 */

/**
 *
 * Constants used by multiple classes in this package
 */
public final class Constants {
    // Set to true to turn on verbose logging
    public static final boolean LOGV = false;

    // Set to true to turn on debug logging
    public static final boolean LOGD = true;

    public static final String ACTION_NOTIFICATION = "ACTION_NOTIFICATION";

    // Defines a custom Intent action
    public static final String BROADCAST_RANKING_PARSED = "com.stekar.apps.sports.wtatennisnow.broadcast.ranking.parsed";
    public static final String BROADCAST_RANKING_CURSOR_LOADER_READY = "com.stekar.apps.sports.wtatennisnow.broadcast.ranking.cursor.loader.ready";
    public static final String BROADCAST_RANKING_READY = "com.stekar.apps.sports.wtatennisnow.broadcast.ranking.ready";
    public static final String BROADCAST_NEWS_PARSED = "com.stekar.apps.sports.wtatennisnow.broadcast.news.parsed";
    public static final String BROADCAST_NEWS_READY = "com.stekar.apps.sports.wtatennisnow.broadcast.news.ready";
    public static final String BROADCAST_NEWS_CURSOR_LOADER_READY = "com.stekar.apps.sports.wtatennisnow.broadcast.news.cursor.loader.ready";
    public static final String BROADCAST_SCHEDULE_PARSED = "com.stekar.apps.sports.wtatennisnow.broadcast.schedule.parsed";
    public static final String BROADCAST_SCHEDULE_READY = "com.stekar.apps.sports.wtatennisnow.broadcast.schedule.ready";
    public static final String BROADCAST_SCHEDULE_CURSOR_LOADER_READY = "com.stekar.apps.sports.wtatennisnow.broadcast.schedule.cursor.loader.ready";
    public static final String RANKING_ALL_PLAYERS_SITE_URL = "http://www.wtatennis.com/singles-rankings";

    // External Web Sites
    public static final String ICONS4ANDROID_SITE_URL = "http://www.icons4android.com";

    // Fragment types
    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final int FRAGMENT_NEWS_TYPE = 0;
    public static final int FRAGMENT_RANKING_TYPE = 1;
    public static final int FRAGMENT_SCHEDULE_TYPE = 2;
    public static final int FRAGMENT_REACHABILITY_TYPE = 3;

    // Status values to broadcast to the Activity

    // The download is starting
    public static final int STATE_ACTION_STARTED = 0;

    // The background thread is connecting to the RSS feed
    public static final int STATE_ACTION_CONNECTING = 1;

    // The background thread is parsing the RSS feed
    public static final int STATE_ACTION_PARSING = 2;

    // The background thread is writing data to the content provider
    public static final int STATE_ACTION_WRITING = 3;

    // The background thread is done
    public static final int STATE_ACTION_COMPLETE = 4;

    // The background thread is doing logging
    public static final int STATE_LOG = -1;
}
