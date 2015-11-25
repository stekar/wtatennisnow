package com.stekar.apps.sports.wtatennisnow.views.adapters;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.content.ClipboardManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.*;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsUpdateDeleteFlag;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsUpdateHiddenFlag;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsUpdateShareFlag;
import com.stekar.apps.sports.wtatennisnow.asynctasks.AsyncATPNewsUpdateStarFlag;
import com.stekar.apps.sports.wtatennisnow.cursors.ATPNewsCursor;
import com.stekar.apps.sports.wtatennisnow.models.ATPNews;


public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    public ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

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

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardsListAdapter(Cursor cursor, Context ctx ) {
        mContext = ctx;
        mCursor = cursor;
    }

    private void updateNewItemShare(ATPNews newsItem) {
        new AsyncATPNewsUpdateShareFlag().execute(newsItem);
    }

    private void showShareMenu(String shareData, ATPNews newsItem) {
        updateNewItemShare(newsItem);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent,
                mContext.getResources().getString(R.string.news_share_heading)));
    }

    private void copyToClipboard(String copyData) {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager)
                mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        // Creates a clip object with the Intent in it. Its label is "Intent" and its data is
        // the Intent object created previously
        ClipData clip = ClipData.newPlainText("ATPNewsFeed", copyData);
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);
    }

    private void updateNewsItemStar(ATPNews newsItem) {
        new AsyncATPNewsUpdateStarFlag().execute(newsItem);
    }

    private void updateNewsItemHidden(ATPNews newsItem) {
        new AsyncATPNewsUpdateHiddenFlag().execute(newsItem);
    }

    private void updateNewsItemDeleted(ATPNews newsItem) {
        new AsyncATPNewsUpdateDeleteFlag().execute(newsItem);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View cardsView = LayoutInflater.from(mContext)
                .inflate(R.layout.cards_main, parent, false);


        return new ViewHolder(cardsView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        mCursor.moveToPosition(position);
        Resources res = AppController.getInstance().getAppContext().getResources();

        final ATPNews atpNews = ((ATPNewsCursor)mCursor).getNewsItem();

        viewHolder.textTitle.setText(atpNews.getTitle());
        viewHolder.textDesc.setText(atpNews.getDescription());

        /// if today, show "Today", otherwise we show the day (i.e. SUN)
        if (atpNews.isPubDateToday()) {
            viewHolder.textDateDay.setText(R.string.news_day_today);
            viewHolder.textDateDay.setTextColor(Color.WHITE);
            LinearLayout ll = (LinearLayout)viewHolder.cardView.findViewById(R.id.newsPubDate);
            ll.setBackgroundColor(res.getColor(R.color.news_today_background));
        } else if (atpNews.isPubDateYesterday()) {
            String day = atpNews.getPubDateDay();
            viewHolder.textDateDay.setText(day);
            viewHolder.textDateDay.setTextColor(Color.WHITE);
            LinearLayout ll = (LinearLayout)viewHolder.cardView.findViewById(R.id.newsPubDate);
            ll.setBackgroundColor(res.getColor(R.color.news_yesterday_background));
        }else if (atpNews.isPubDateTwoDay()) {
            String day = atpNews.getPubDateDay();
            viewHolder.textDateDay.setText(day);
            viewHolder.textDateDay.setTextColor(Color.WHITE);
            LinearLayout ll = (LinearLayout) viewHolder.cardView.findViewById(R.id.newsPubDate);
            ll.setBackgroundColor(res.getColor(R.color.news_two_day_background));
        } else if (atpNews.isPubDateThreeDay()) {
            String day = atpNews.getPubDateDay();
            viewHolder.textDateDay.setText(day);
            viewHolder.textDateDay.setTextColor(Color.WHITE);
            LinearLayout ll = (LinearLayout) viewHolder.cardView.findViewById(R.id.newsPubDate);
            ll.setBackgroundColor(res.getColor(R.color.news_three_day_background));
        } else {
            String day = atpNews.getPubDateDay();
            viewHolder.textDateDay.setText(day);
            viewHolder.textDateDay.setTextColor(Color.WHITE);
            LinearLayout ll = (LinearLayout)viewHolder.cardView.findViewById(R.id.newsPubDate);
            ll.setBackgroundColor(res.getColor(R.color.news_rest_of_week_background));
        }

        viewHolder.textDateNumber.setText(atpNews.getPubDateNumber());
        viewHolder.textDateMonth.setText(atpNews.getPubDateMonth());

        final String linkText = atpNews.getLink();
        viewHolder.textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkText));
                v.getContext().startActivity(i);
            }
        });

        viewHolder.textDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkText));
                v.getContext().startActivity(i);
            }
        });

        final String shareText = atpNews.getNewsShare();
        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareMenu(shareText, atpNews);
            }
        });

        viewHolder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(shareText);
            }
        });

        viewHolder.imgTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewsItemDeleted(atpNews);
            }
        });

        final boolean isStarred = atpNews.getIsStarred();
        if(isStarred == true) {
            viewHolder.imgStar.setImageDrawable(res.getDrawable(R.drawable.selector_bar_action_star_selected));
        } else {
            viewHolder.imgStar.setImageDrawable(res.getDrawable(R.drawable.selector_bar_action_star));
        }

        viewHolder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewsItemStar(atpNews);
            }
        });

        final boolean isHidden = atpNews.getIsIsHidden();
        if(isHidden == true) {
            viewHolder.imgHide.setImageDrawable(res.getDrawable(R.drawable.selector_bar_action_view_selected));
        } else {
            viewHolder.imgHide.setImageDrawable(res.getDrawable(R.drawable.selector_bar_action_view));
        }

        viewHolder.imgHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewsItemHidden(atpNews);
            }
        });

        if (mImageLoader == null)
            mImageLoader = AppController.getInstance().getImageLoader();

        String newsCover = atpNews.getCover();
        viewHolder.imgCover.setImageUrl(newsCover, mImageLoader);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textTitle;
        public TextView textDesc;
        public TextView textDateDay;
        public TextView textDateMonth;
        public TextView textDateNumber;
        public CardView cardView;
        public ImageView imgShare;
        public ImageView imgCopy;
        public ImageView imgStar;
        public ImageView imgHide;
        public ImageView imgTrash;
        public NetworkImageView imgCover;

        public ViewHolder(View cardsView) {
            super(cardsView);
            cardView = (CardView)cardsView.findViewById(R.id.card_view);
            textTitle = (TextView)cardView.findViewById(R.id.newsTitle);
            textDesc = (TextView)cardView.findViewById(R.id.newsDesc);
            textDateDay = (TextView)cardView.findViewById(R.id.newsPubDateDay);
            textDateNumber = (TextView)cardView.findViewById(R.id.newsPubDateNumber);
            textDateMonth = (TextView)cardView.findViewById(R.id.newsPubDateMonth);
            imgShare = (ImageView)cardView.findViewById(R.id.newsButtonShare);
            imgCopy = (ImageView)cardView.findViewById(R.id.newsButtonCopy);
            imgStar = (ImageView)cardView.findViewById(R.id.newsButtonStar);
            imgHide = (ImageView)cardView.findViewById(R.id.newsButtonHide);
            imgTrash = (ImageView)cardView.findViewById(R.id.newsButtonTrash);
            imgCover = (NetworkImageView) cardView.findViewById(R.id.newsCover);
        }
    }
}