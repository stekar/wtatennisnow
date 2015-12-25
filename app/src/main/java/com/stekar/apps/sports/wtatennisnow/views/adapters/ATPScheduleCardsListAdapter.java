package com.stekar.apps.sports.wtatennisnow.views.adapters;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.*;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPScheduleUpdateEventFlag;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPScheduleUpdateStarFlag;
import com.stekar.apps.sports.wtatennisnow.constants.PrefsConstants;
import com.stekar.apps.sports.wtatennisnow.cursors.ATPTournasCursor;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.fragments.SettingsFragment;
import com.stekar.apps.sports.wtatennisnow.models.ATPSchedule;
import java.util.Calendar;
import java.util.TimeZone;


public class ATPScheduleCardsListAdapter extends RecyclerView.Adapter<ATPScheduleCardsListAdapter.ViewHolder> {
    private static final String RES_DRAWABLE = "drawable";
    private Cursor mCursor;
    private Context mContext;
    private Resources mRes;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ATPScheduleCardsListAdapter(Cursor cursor, Context ctx ) {
        mContext = ctx;
        mCursor = cursor;
        mRes = mContext.getResources();
    }

    private void showShareMenu(String shareData, ATPSchedule scheduleItem) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent,
                mContext.getResources().getString(R.string.schedule_share_heading)));
    }

    private void copyToClipboard(String copyData) {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager)
                mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        // Creates a clip object with the Intent in it. Its label is "Intent" and its data is
        // the Intent object created previously
        ClipData clip = ClipData.newPlainText(mContext.getResources().getString(R.string.schedule_copy_heading), copyData);
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);
    }

    private void updateNewItemStar(ATPSchedule scheduleItem) {
        new AsyncATPScheduleUpdateStarFlag().execute(scheduleItem);
    }
    private void updateScheduleItemEvent(ATPSchedule scheduleItem) {
        new AsyncATPScheduleUpdateEventFlag().execute(scheduleItem);
    }

    private void addTournaToCalendar(ATPSchedule scheduleItem) {
        // Adding EVENT
        long calId = AppController.getInstance().getPrefValue(PrefsConstants.PREFS_CALENDAR_DEFAULT_ID, 1L);
        long startMillis = 0;
        long endMillis = 0;

        Calendar beginTime = Calendar.getInstance();
        int year = beginTime.get(Calendar.YEAR);
        beginTime.set(year, scheduleItem.getTournaMonth(), Integer.parseInt(scheduleItem.getTournaDay()), 8, 0);
        int duration = scheduleItem.getTournaWeekEnd() - scheduleItem.getTournaWeekStart();
        int durationDays = duration == 0 ? 7 : 14;
        startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(year, scheduleItem.getTournaMonth(), Integer.parseInt(scheduleItem.getTournaDay()) + durationDays, 23, 0);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        //values.put(CalendarContract.Events.ALL_DAY, true);
        values.put(CalendarContract.Events.TITLE, scheduleItem.getTournaName());
        values.put(CalendarContract.Events.DESCRIPTION, mRes.getString(R.string.schedule_tourna_atp_tournament));
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.EVENT_LOCATION, scheduleItem.getTournaCity() + ", " + scheduleItem.getTournaCountry());

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

        // Adding REMINDER (set 1-hr)
        values = new ContentValues();

        // Set a reminder if the user chose one
        String reminderPrefValue = AppController.getInstance().getPrefValue(PrefsConstants.PREFS_SCHEDULE_EVENTS_REMINDER, SettingsFragment.DEFAULT_REMINDER);
        int reminderCalValue = Integer.parseInt(reminderPrefValue);
        if(reminderCalValue > 0) {
            values.put(CalendarContract.Reminders.MINUTES, reminderCalValue);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
        }

        // Update our database
        scheduleItem.setTournaEventId(eventID);
        updateScheduleItemEvent(scheduleItem);
    }

    private boolean removeTournaFromCalendar(ATPSchedule scheduleItem) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, scheduleItem.getTournaEventId());
        int rows = contentResolver.delete(deleteUri, null, null);

        // This is to account for the user removing the event from within the calendar App
        // instead of within the App.
        // Int hat case, we (blindly) assume we are in the 'add' mode
        if(rows > 0) {
            scheduleItem.setTournaEventId(0);
            updateScheduleItemEvent(scheduleItem);

            return true;
        } else {
            scheduleItem.setTournaEventId(0);
            updateScheduleItemEvent(scheduleItem);

            return false;
            //addTournaToCalendar(scheduleItem);
        }
    }

    public void swapCursor(Cursor cursor) {
        // No need for...
        // CursorLoader::deliverResult does it for us, yea!
        /*if(mCursor != null) {
            mCursor.close();
        }*/

        mCursor = cursor;
        if(mCursor != null) {
            notifyDataSetChanged();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ATPScheduleCardsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View cardsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_schedule, parent, false);

        return new ViewHolder(cardsView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        mCursor.moveToPosition(position);
        Resources res =AppController.getInstance().getAppContext().getResources();

        final ATPSchedule atpSchedule = ((ATPTournasCursor)mCursor).getScheduleItem();
        viewHolder.textTournaName.setText(atpSchedule.getTournaName());

        // Treat grand slam and masters 1000 tournaments with more respect
        boolean isSlam = Boolean.valueOf(atpSchedule.getTournaSlam());
        if(isSlam == true) {
            viewHolder.textTournaPoints.setText(mRes.getString(R.string.schedule_tourna_grand_slam));
        } else {
            boolean isMasters1000 = Boolean.valueOf(atpSchedule.getTournaPremier());
            if(isMasters1000 == true) {
                viewHolder.textTournaPoints.setText(R.string.schedule_tourna_premier);
            } else {
                String tournaPoints = String.format(mRes.getString(R.string.schedule_tourna_points), atpSchedule.getTournaPoints());
                CharSequence styledTournaPoints = Html.fromHtml(tournaPoints);
                viewHolder.textTournaPoints.setText(styledTournaPoints);
            }
        }

        String tournaWinner = atpSchedule.getTournaWinner();
        if(tournaWinner != null && tournaWinner.length() > 0) {
            String winner = String.format(mRes.getString(R.string.schedule_tourna_winner), tournaWinner);
            CharSequence styledTournaWinner = Html.fromHtml(winner);
            viewHolder.textTournaWinner.setText(styledTournaWinner);
        } else {
            String winner = String.format(mRes.getString(R.string.schedule_tourna_winner_tbd), tournaWinner);
            CharSequence styledTournaWinner = Html.fromHtml(winner);
            viewHolder.textTournaWinner.setText(styledTournaWinner);
        }

        String tournaDate = String.format(mRes.getString(R.string.schedule_tourna_date),
                atpSchedule.getTournaMonthDisplayName(), atpSchedule.getTournaDay());
        CharSequence styledTournaDate = Html.fromHtml(tournaDate);
        viewHolder.textTournaDay.setText(styledTournaDate);

        int weekStart = atpSchedule.getTournaWeekStart();
        int weekEnd = atpSchedule.getTournaWeekEnd();
        if((weekEnd - weekStart) + 1 == 1) {
            String tournaWeekText = String.format(mRes.getString(R.string.schedule_tourna_one_week));
            CharSequence styledTournaWeekText = Html.fromHtml(tournaWeekText);
            viewHolder.textTournaDayEnd.setText(styledTournaWeekText);
        } else {
            String tournaWeekText = String.format(mRes.getString(R.string.schedule_tourna_two_week));
            CharSequence styledTournaWeekText = Html.fromHtml(tournaWeekText);
            viewHolder.textTournaDayEnd.setText(styledTournaWeekText);
        }

        String tournaPlace = String.format(mRes.getString(R.string.schedule_tourna_place),
                atpSchedule.getTournaCity(), atpSchedule.getTournaCountry());
        CharSequence styledTournaPlace = Html.fromHtml(tournaPlace);
        viewHolder.textTournaPlace.setText(styledTournaPlace);

        String tournaSurface = String.format(mRes.getString(R.string.schedule_tourna_surface), atpSchedule.getTournaSurface());
        CharSequence styledTournaSurface = Html.fromHtml(tournaSurface);
        viewHolder.textTournaSurface.setText(styledTournaSurface);

        viewHolder.btnTournaOfficialSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(atpSchedule.getTournaWebSite()));
                v.getContext().startActivity(i);
            }
        });

        final long curEventId = atpSchedule.getTournaEventId();
        if(curEventId == 0) {
            viewHolder.btnTournaEventAction.setText(mRes.getString(R.string.schedule_tourna_event_add));
        } else {
            viewHolder.btnTournaEventAction.setText(mRes.getString(R.string.schedule_tourna_event_remove));
        }

        viewHolder.btnTournaEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curEventId == 0) {
                    addTournaToCalendar(atpSchedule);
                } else {
                    boolean isRemoved = removeTournaFromCalendar(atpSchedule);
                    if (isRemoved == false) {
                        //viewHolder.btnTournaEventAction.setText(mRes.getString(R.string.schedule_tourna_event_add));
                    }
                }
            }
        });

        viewHolder.imgTournaMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(atpSchedule.getTournaMapUrl()));
                v.getContext().startActivity(i);
            }
        });

        int imgId = mRes.getIdentifier(atpSchedule.getTournaMapTile(), RES_DRAWABLE,
                AppController.getInstance().getAppPackageName());
        viewHolder.imgTournaMap.setImageDrawable(mRes.getDrawable(imgId));

        final String shareText = atpSchedule.getTournaShareText();
        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareMenu(shareText, atpSchedule);
            }
        });

        viewHolder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(shareText);
            }
        });

        if(atpSchedule.getIsStarred() == true) {
            viewHolder.imgStar.setImageDrawable(res.getDrawable(R.drawable.selector_bar_action_star_selected_tourna));
        } else {
            viewHolder.imgStar.setImageDrawable(res.getDrawable(R.drawable.selector_bar_action_star_tourna));
        }
        viewHolder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewItemStar(atpSchedule);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textTournaName;
        public TextView textTournaPoints;
        public TextView textTournaWinner;
        public TextView textTournaPlace;
        public TextView textTournaDay;
        public TextView textTournaDayEnd;
        public TextView textTournaSurface;
        public Button btnTournaOfficialSite;
        public Button btnTournaEventAction;
        public ImageView imgTournaMap;
        public CardView cardView;
        public ImageView imgShare;
        public ImageView imgCopy;
        public ImageView imgStar;


        public ViewHolder(View cardsView) {
            super(cardsView);
            cardView = (CardView)cardsView.findViewById(R.id.card_view);
            textTournaName = (TextView)cardView.findViewById(R.id.tournaName);
            textTournaPoints = (TextView)cardView.findViewById(R.id.tournaPoints);
            textTournaWinner = (TextView)cardView.findViewById(R.id.tournaWinner);
            textTournaPlace = (TextView)cardView.findViewById(R.id.tournaPlace);
            textTournaDay = (TextView)cardView.findViewById(R.id.tournaDay);
            textTournaDayEnd = (TextView)cardView.findViewById(R.id.tournaDayEnd);
            textTournaSurface = (TextView)cardView.findViewById(R.id.tournaSurface);
            btnTournaOfficialSite = (Button)cardView.findViewById(R.id.tournaWebSite);
            btnTournaEventAction = (Button)cardView.findViewById(R.id.tournaEventAction);
            imgTournaMap = (ImageView) cardView.findViewById(R.id.tournaFlag);
            imgCopy = (ImageView)cardView.findViewById(R.id.tournasButtonCopy);
            imgShare = (ImageView)cardView.findViewById(R.id.tournasButtonShare);
            imgStar = (ImageView)cardView.findViewById(R.id.tournasButtonStar);
        }
    }
}