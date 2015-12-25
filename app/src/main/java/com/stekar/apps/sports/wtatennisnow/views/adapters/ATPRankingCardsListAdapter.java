package com.stekar.apps.sports.wtatennisnow.views.adapters;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.*;
import com.stekar.apps.sports.wtatennisnow.R;
import com.stekar.apps.sports.wtatennisnow.cursors.ATPPlayerRankingCursor;
import com.stekar.apps.sports.wtatennisnow.database.AppDatabase;
import com.stekar.apps.sports.wtatennisnow.models.ATPNews;
import com.stekar.apps.sports.wtatennisnow.models.ATPRanking;
import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;


public class ATPRankingCardsListAdapter extends RecyclerView.Adapter<ATPRankingCardsListAdapter.ViewHolder> {
    private Activity mActivity;
    private Cursor mCursor;
    ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    // Provide a suitable constructor (depends on the kind of dataset)
    public ATPRankingCardsListAdapter(Cursor cursor, Activity activity) {
        mCursor = cursor;
        mActivity = activity;
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
    public ATPRankingCardsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View cardsView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_ranking, parent, false);

        return new ViewHolder(cardsView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        mCursor.moveToPosition(position);

        final Resources res = mActivity.getResources();
        ArrayList<ATPNews> atpNews = new ArrayList();
        final ATPRanking atpRanking = ((ATPPlayerRankingCursor)mCursor).getRankingItem();
        final LinearLayout llNewstitlesLayout = viewHolder.llNewstitlesLayout;

        if(((ATPPlayerRankingCursor) mCursor).hasNewsColumns() == true) {
            final String newsTitlesDelimited = mCursor.getString(mCursor.getColumnIndex(AppDatabase.NewsDatabase.TITLE));
            final String newsLinksDelimited = mCursor.getString(mCursor.getColumnIndex(AppDatabase.NewsDatabase.LINK));
            final String newsPubDateMillisDelimited = mCursor.getString(mCursor.getColumnIndex(AppDatabase.NewsDatabase.PUB_DATE_MILLIS));

            if (newsTitlesDelimited != null && newsTitlesDelimited.length() > 0) {
                llNewstitlesLayout.removeAllViews();
                llNewstitlesLayout.setVisibility(View.VISIBLE);
                viewHolder.textPlayerNewsShowHide.setVisibility(View.VISIBLE);
                viewHolder.imgShowHideNews.setVisibility(View.VISIBLE);
                viewHolder.imgShowHideNews.setImageDrawable(res.getDrawable(R.drawable.ic_action_arrow_up));
                viewHolder.textPlayerNewsShowHide.setText(R.string.ranking_news_hide);

                if (newsLinksDelimited != null && newsLinksDelimited.length() > 0) {
                    final String[] newsLinks = newsLinksDelimited.split("@");
                    if (newsLinks.length > 0) {
                        for (String newsLink : newsLinks) {
                            ATPNews atpNewsItem = new ATPNews();
                            atpNewsItem.setLink(newsLink);
                            atpNews.add(atpNewsItem);
                        }
                    }
                }

                if (newsTitlesDelimited != null && newsTitlesDelimited.length() > 0) {
                    final String[] newsTitles = newsTitlesDelimited.split("@");
                    int i = 0;
                    if (newsTitles.length > 0) {
                        for (String newsTitle : newsTitles) {
                            ATPNews atpNewsItem = atpNews.get(i);
                            atpNewsItem.setTitle(newsTitle);
                            i++;
                        }
                    }
                }

                if (newsPubDateMillisDelimited != null && newsPubDateMillisDelimited.length() > 0) {
                    final String[] newsPubDateMillis = newsPubDateMillisDelimited.split("@");
                    int i = 0;
                    if (newsPubDateMillis.length > 0) {
                        for (String newsPubDateMillisItem : newsPubDateMillis) {
                            ATPNews atpNewsItem = atpNews.get(i);
                            atpNewsItem.setPubDate(Long.parseLong(newsPubDateMillisItem));
                            i++;
                        }
                    }
                }

                if (newsTitlesDelimited != null && newsTitlesDelimited.length() > 0) {
                    final String[] newsTitles = newsTitlesDelimited.split("@");
                    if (newsTitles.length > 0) {
                        int i = 0;
                        for (String newsTitle : newsTitles) {
                            final ATPNews newsItem = atpNews.get(i);
                            TextView tvPubDate = new TextView(mActivity);
                            tvPubDate.setTypeface(null, Typeface.BOLD);
                            tvPubDate.setText(newsItem.getPubDateMonth() + " " + newsItem.getPubDateNumber());
                            tvPubDate.setBackgroundColor(res.getColor(R.color.ranking_tab_background));
                            tvPubDate.setGravity(Gravity.CENTER_VERTICAL);
                            LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lpDate.setMargins(5, 5, 5, 0);
                            llNewstitlesLayout.addView(tvPubDate);

                            TextView tvTitle = new TextView(mActivity);
                            LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lpTitle.setMargins(0, 0, 5, 25);
                            tvTitle.setLayoutParams(lpTitle);
                            tvTitle.setText(newsTitle);
                            tvTitle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(newsItem.getLink()));
                                    v.getContext().startActivity(i);
                                }
                            });
                            llNewstitlesLayout.addView(tvTitle);
                            i++;
                        }
                    }
                }
                viewHolder.imgShowHideNews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isShown = llNewstitlesLayout.isShown();
                        if(isShown) {
                            llNewstitlesLayout.setVisibility(View.GONE);
                            viewHolder.imgShowHideNews.setImageDrawable(res.getDrawable(R.drawable.ic_action_arrow_down));
                            viewHolder.textPlayerNewsShowHide.setText(R.string.ranking_news_show);
                        } else {
                            llNewstitlesLayout.setVisibility(View.VISIBLE);
                            viewHolder.imgShowHideNews.setImageDrawable(res.getDrawable(R.drawable.ic_action_arrow_up));
                            viewHolder.textPlayerNewsShowHide.setText(R.string.ranking_news_hide);
                        }
                    }
                });
            } else {
                viewHolder.textPlayerNewsShowHide.setVisibility(View.GONE);
                viewHolder.imgShowHideNews.setVisibility(View.GONE);
                llNewstitlesLayout.setVisibility(View.GONE);
            }
        } else {
            viewHolder.textPlayerNewsShowHide.setVisibility(View.GONE);
            viewHolder.imgShowHideNews.setVisibility(View.GONE);
            llNewstitlesLayout.setVisibility(View.GONE);
        }

        viewHolder.textPlayerLastName.setText(atpRanking.getPlayerLastName());
        viewHolder.textPlayerFirstName.setText(atpRanking.getPlayerFirstName());

        String playerAgeAndBirthplace = String.format(res.getString(R.string.ranking_player_age_birthplace),
                atpRanking.getPlayerAge(), atpRanking.getPlayerBirthplace());
        CharSequence styledPlayerAgeAndBirthplace = Html.fromHtml(playerAgeAndBirthplace);
        viewHolder.textPlayerAgeAndBirthplace.setText(styledPlayerAgeAndBirthplace);

        String playerTotalPoints = String.format(res.getString(R.string.ranking_total_points), atpRanking.getPlayerTotalPoints());
        CharSequence styledPlayerTotalPoints = Html.fromHtml(playerTotalPoints);
        viewHolder.textPlayerTotalPoints.setText(styledPlayerTotalPoints);

        String playerRank = String.format(res.getString(R.string.ranking_player_rank), atpRanking.getPlayerRank());
        CharSequence styledPlayerRank = Html.fromHtml(playerRank);
        viewHolder.textPlayerRank.setText(styledPlayerRank);

        String playerRankHigh = String.format(res.getString(R.string.ranking_player_rank_high), atpRanking.getPlayerRankHigh());
        CharSequence styledPlayerRankHigh = Html.fromHtml(playerRankHigh);
        viewHolder.textPlayerRankHigh.setText(styledPlayerRankHigh);

        // TITLES - BEGIN
        String playerTitlesYTD = String.format(res.getString(R.string.ranking_player_titles_ytd), atpRanking.getPlayerTitlesYTD());
        CharSequence styledPlayerTitlesYTD = Html.fromHtml(playerTitlesYTD);
        viewHolder.textPlayerTitlesYTD.setText(styledPlayerTitlesYTD);

        String playerTitlesCareer = String.format(res.getString(R.string.ranking_player_titles_career), atpRanking.getPlayerTitlesCareer());
        CharSequence styledPlayerTitlesCareer = Html.fromHtml(playerTitlesCareer);
        viewHolder.textPlayerTitlesCareer.setText(styledPlayerTitlesCareer);

        String playerSlamTitlesYTD = String.format(res.getString(R.string.ranking_player_titles_slam_ytd), atpRanking.getPlayerSlamTitlesYTD());
        CharSequence styledPlayerSlamTitlesYTD = Html.fromHtml(playerSlamTitlesYTD);
        viewHolder.textPlayerSlamTitlesYTD.setText(styledPlayerSlamTitlesYTD);

        String playerSlamTitlesCareer = String.format(res.getString(R.string.ranking_player_titles_slam_career),atpRanking.getPlayerSlamTitlesCareer());
        CharSequence styledPlayerSlamTitlesCareer = Html.fromHtml(playerSlamTitlesCareer);
        viewHolder.textPlayerSlamTitlesCareer.setText(styledPlayerSlamTitlesCareer);

        String playerMasters1000TitlesYTD = String.format(res.getString(R.string.ranking_player_titles_premier_ytd), atpRanking.getPlayerMasters1000TitlesYTD());
        CharSequence styledPlayerMasters1000TitlesYTD = Html.fromHtml(playerMasters1000TitlesYTD);
        viewHolder.textPlayerMasters1000TitlesYTD.setText(styledPlayerMasters1000TitlesYTD);

        String playerMasters1000TitlesCareer = String.format(res.getString(R.string.ranking_player_titles_premier_career), atpRanking.getPlayerMasters1000TitlesCareer());
        CharSequence styledPlayerMasters1000TitlesCareer = Html.fromHtml(playerMasters1000TitlesCareer);
        viewHolder.textPlayerMasters1000TitlesCareer.setText(styledPlayerMasters1000TitlesCareer);
        // TITLES - END

        /*String playerTournas = String.format(res.getString(R.string.ranking_tournas_played), atpRanking.getPlayerTournas());
        CharSequence styledPlayerTournas = Html.fromHtml(playerTournas);
        viewHolder.textPlayerTournas.setText(styledPlayerTournas);*/

        String playerPrizeMoneyCurrent = String.format(res.getString(R.string.ranking_prize_money_current), atpRanking.getPlayerPrizeMoneyCurrent());
        CharSequence styledPlayerPrizeMoneyCurrent = Html.fromHtml(playerPrizeMoneyCurrent);
        viewHolder.textPlayerPrizeMoneyCurrent.setText(styledPlayerPrizeMoneyCurrent);

        String playerPrizeMoneyTotal = String.format(res.getString(R.string.ranking_prize_money_total), atpRanking.getPlayerPrizeMoneyTotal());
        CharSequence styledPlayerPrizeMoneyTotal = Html.fromHtml(playerPrizeMoneyTotal);
        viewHolder.textPlayerPrizeMoneyTotal.setText(styledPlayerPrizeMoneyTotal);

        viewHolder.btnPlayerProfileSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(atpRanking.getPlayerProfileUrl()));
                v.getContext().startActivity(i);
            }
        });

        viewHolder.playerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(atpRanking.getPlayerProfileUrl()));
                v.getContext().startActivity(i);
            }
        });

        viewHolder.btnPlayerOfficialSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(atpRanking.getPlayerWebSite()));
                v.getContext().startActivity(i);
            }
        });

        // are we in landscape mode?
        // If no, then we enable switching between YTD and Career tabular data
        if(viewHolder.llplayerDataLayoutHeadingLandscape == null) {
            viewHolder.textPlayerYTDHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.llCareerLayout.setVisibility(View.GONE);
                    viewHolder.textPlayerCareerHeading.setTextColor(res.getColor(R.color.ranking_today_card_background));
                    viewHolder.textPlayerCareerHeading.setBackgroundColor(Color.WHITE);

                    viewHolder.llYTDLayout.setVisibility(View.VISIBLE);
                    viewHolder.textPlayerYTDHeading.setTextColor(res.getColor(R.color.ranking_tab_foreground));
                    viewHolder.textPlayerYTDHeading.setBackgroundColor(res.getColor(R.color.ranking_player_ytd_career_background));

                }
            });

            viewHolder.textPlayerCareerHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.llYTDLayout.setVisibility(View.GONE);
                    viewHolder.textPlayerYTDHeading.setTextColor(res.getColor(R.color.ranking_today_card_background));
                    viewHolder.textPlayerYTDHeading.setBackgroundColor(Color.WHITE);

                    viewHolder.llCareerLayout.setVisibility(View.VISIBLE);
                    viewHolder.textPlayerCareerHeading.setTextColor(res.getColor(R.color.ranking_tab_foreground));
                    viewHolder.textPlayerCareerHeading.setBackgroundColor(res.getColor(R.color.ranking_player_ytd_career_background));
                }
            });
        }

        if (mImageLoader == null)
            mImageLoader = AppController.getInstance().getImageLoader();

        viewHolder.playerPhoto.setImageUrl(atpRanking.getPlayerPhotoUrl(), mImageLoader);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textPlayerLastName;
        public TextView textPlayerFirstName;
        public TextView textPlayerTotalPoints;
        //public TextView textPlayerTournas;
        public TextView textPlayerPrizeMoneyCurrent;
        public TextView textPlayerPrizeMoneyTotal;
        public TextView textPlayerAgeAndBirthplace;
        public TextView textPlayerRank;
        public TextView textPlayerRankHigh;
        public TextView textPlayerTitlesYTD;
        public TextView textPlayerTitlesCareer;
        public TextView textPlayerSlamTitlesYTD;
        public TextView textPlayerSlamTitlesCareer;
        public TextView textPlayerMasters1000TitlesYTD;
        public TextView textPlayerMasters1000TitlesCareer;
        public LinearLayout llYTDLayout;
        public LinearLayout llCareerLayout;
        public TextView textPlayerYTDHeading;
        public TextView textPlayerCareerHeading;
        public TextView textPlayerNewsShowHide;
        public LinearLayout llplayerDataLayoutHeadingLandscape;
        public LinearLayout llNewstitlesLayout;

        public ScrollView svNewsTitles;

        public Button btnPlayerProfileSite;
        public Button btnPlayerOfficialSite;

        public ImageView imgShowHideNews;

        public NetworkImageView playerPhoto;

        public CardView cardView;

        public ViewHolder(View cardsView) {
            super(cardsView);
            cardView = (CardView)cardsView.findViewById(R.id.card_view);
            textPlayerLastName = (TextView)cardView.findViewById(R.id.playerLastName);
            textPlayerFirstName = (TextView)cardView.findViewById(R.id.playerFirstName);
            textPlayerTotalPoints = (TextView)cardView.findViewById(R.id.playerTotalPoints);
            //textPlayerTournas = (TextView)cardView.findViewById(R.id.playerTournas);
            textPlayerPrizeMoneyCurrent = (TextView)cardView.findViewById(R.id.playerPrizeMoneyCurrent);
            textPlayerPrizeMoneyTotal = (TextView)cardView.findViewById(R.id.playerPrizeMoneyTotal);
            textPlayerAgeAndBirthplace = (TextView)cardView.findViewById(R.id.playerAgeAndBirthplace);
            textPlayerRank = (TextView)cardView.findViewById(R.id.playerRank);
            textPlayerRankHigh = (TextView)cardView.findViewById(R.id.playerRankHigh);
            textPlayerTitlesYTD = (TextView)cardView.findViewById(R.id.playerTitlesYTD);
            textPlayerTitlesCareer = (TextView)cardView.findViewById(R.id.playerTitlesCareer);
            textPlayerSlamTitlesYTD = (TextView)cardView.findViewById(R.id.playerSlamTitlesYTD);
            textPlayerSlamTitlesCareer = (TextView)cardView.findViewById(R.id.playerSlamTitlesCareer);
            textPlayerMasters1000TitlesYTD = (TextView)cardView.findViewById(R.id.playerMasters1000TitlesYTD);
            textPlayerMasters1000TitlesCareer = (TextView)cardView.findViewById(R.id.playerMasters1000TitlesCareer);
            textPlayerYTDHeading = (TextView)cardView.findViewById(R.id.playerYTDHeading);
            textPlayerCareerHeading = (TextView)cardView.findViewById(R.id.playerCareerHeading);
            textPlayerNewsShowHide = (TextView)cardView.findViewById(R.id.playerNewsShowHide);

            llYTDLayout = (LinearLayout)cardsView.findViewById(R.id.playerYTDLayout);
            llCareerLayout = (LinearLayout)cardsView.findViewById(R.id.playerCareerLayout);
            llplayerDataLayoutHeadingLandscape = (LinearLayout)cardsView.findViewById(R.id.playerDataLayoutHeadingLandscape);
            //svNewsTitles = (ScrollView)cardsView.findViewById(R.id.scrollViewNewsTitle);
            llNewstitlesLayout = (LinearLayout)cardsView.findViewById(R.id.llNewstitlesLayout);

            btnPlayerProfileSite = (Button)cardView.findViewById(R.id.playerFullProfileSite);
            btnPlayerOfficialSite = (Button)cardView.findViewById(R.id.playerOfficialSite);
            imgShowHideNews = (ImageView)cardView.findViewById(R.id.imgNewsShowHide);

            playerPhoto = (NetworkImageView) cardView.findViewById(R.id.playerPhoto);
        }
    }
}