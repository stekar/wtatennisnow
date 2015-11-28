package com.stekar.apps.sports.wtatennisnow.parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;

import com.stekar.apps.sports.wtatennisnow.app.AppController;
import com.stekar.apps.sports.wtatennisnow.database.AppSqlHelper;
import com.stekar.apps.sports.wtatennisnow.models.*;

public class ATPNewsParser {
    private static final String ns = null;

    public void parse(StringReader in, final Map<String,String> mapPlayers, List<ATPNews> insertedRows) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in);
            parser.nextTag();
            readFeed(parser, mapPlayers, insertedRows);
        } finally {
            in.close();
        }
    }

    private void readFeed(XmlPullParser parser, final Map<String,String> mapPlayers, List<ATPNews> insertedRows) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            // Starts by looking for the entry tag
            if (tagName.equals("item")) {
                readNewsItem(parser, mapPlayers, insertedRows);
            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private void readNewsItem(XmlPullParser parser, final Map<String,String> mapPlayers, List<ATPNews> newNews) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String pubDate = null;
        String link = null;
        String desc = null;
        String cover = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("title")) {
                title = readTitle(parser);
            } else if (tagName.equals("publishedDate")) {
                pubDate = readPubDate(parser);
            } else if (tagName.equals("link")) {
                link = readLink(parser);
            } else if(tagName.equals("description")) {
                desc = readDescription(parser);
            } else if(tagName.equals("teaserImage")) {
                cover = readCover(parser);
        }   else {
                skip(parser);
            }
        }

        ATPNews newsItem = new ATPNews();
        newsItem.setTitle(title);
        newsItem.setPubDate(pubDate);
        newsItem.setLink(link);
        newsItem.setDescription(desc);
        newsItem.setCover(cover);
        // set-up the share send intent data
        StringBuilder sb = new StringBuilder();
        sb.append(newsItem.getPubDateMonth());
        sb.append(" ");
        sb.append(newsItem.getPubDateNumber());
        sb.append(". ");
        sb.append("WTA News: ");
        sb.append(newsItem.getTitle());
        sb.append("\n");
        sb.append(newsItem.getLink());
        newsItem.setNewsShare(sb.toString());

        String playerPhotoUrl = this.playerPhotoForNewsTitle(title, mapPlayers);
        newsItem.setPlayerPhotoUrl(playerPhotoUrl);

        boolean isRowUpdated = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().updateNewsItemCover(newsItem);
        if(isRowUpdated == false) {
            long rowId = AppSqlHelper.getInstance(AppController.getInstance().getAppContext()).getATPNewsHelper().insertNewsItem(newsItem);
            if (rowId != -1) {
                newNews.add(newsItem);
            }
        }
    }

    private String getPlayerPhotoUrl(final String newsTitleComponent, final Map<String,String> mapPlayers) {
        String newsTitlePhoto = null;

        if(newsTitleComponent != null && newsTitleComponent.length() > 0) {
            String playerPhotoUrl = mapPlayers.get(newsTitleComponent);
            if (playerPhotoUrl != null && playerPhotoUrl.length() > 0) {
                newsTitlePhoto = playerPhotoUrl;
            }
        }
        return newsTitlePhoto;
    }

    private String getPlayerPhotoUrl(final String newsTitleComponent, final String delimiter, final Map<String,String> mapPlayers) {
        String newsTitlePhoto = null;

        if(newsTitleComponent != null && newsTitleComponent.length() > 0) {
            String[] newsTitleSubComponents = newsTitleComponent.split(delimiter);
            int len = newsTitleSubComponents.length;
            if (newsTitleSubComponents != null && len > 0) {
                newsTitlePhoto = this.getPlayerPhotoUrl(newsTitleSubComponents[0], mapPlayers);

                // Try for the right side of the delimiter (i.e. player_1/player_2
                if(newsTitlePhoto == null && len > 1) {
                    newsTitlePhoto = this.getPlayerPhotoUrl(newsTitleSubComponents[1], mapPlayers);
                }
            }
        }
        return newsTitlePhoto;
    }

    private String playerPhotoForNewsTitle(final String newsTitle, final Map<String,String> mapPlayers) {
        String newsTitlePhoto = null;
        String[] newsTitleComponents = newsTitle.split(" ");
        if(newsTitleComponents != null && newsTitleComponents.length > 0) {
            for(String newsTitleComponent : newsTitleComponents)
            {
                String playerPhotoUrl = this.getPlayerPhotoUrl(newsTitleComponent, mapPlayers);
                if(playerPhotoUrl != null && playerPhotoUrl.length() > 0) {
                    newsTitlePhoto = playerPhotoUrl;
                    break;
                }
            }
        }

        // Let's give it one more shot
        // See if the very first title component contains a ':'
        if(newsTitlePhoto == null) {
            for(String newsTitleComponent : newsTitleComponents) {
                newsTitlePhoto = this.getPlayerPhotoUrl(newsTitleComponent, ":", mapPlayers);
                if(newsTitlePhoto != null) {
                    break;
                }
            }
        }

        // See if the very first title component contains a '/'
        if(newsTitlePhoto == null) {
            for(String newsTitleComponent : newsTitleComponents) {
                newsTitlePhoto = this.getPlayerPhotoUrl(newsTitleComponent, "/", mapPlayers);
                if(newsTitlePhoto != null) {
                    break;
                }
            }
        }

        // See if the very first title component contains a ','
        if(newsTitlePhoto == null) {
            for(String newsTitleComponent : newsTitleComponents) {
                newsTitlePhoto = this.getPlayerPhotoUrl(newsTitleComponent, ",", mapPlayers);
                if(newsTitlePhoto != null) {
                    break;
                }
            }
        }

        // See if the very first title component contains a ''s'
        if(newsTitlePhoto == null) {
            for(String newsTitleComponent : newsTitleComponents) {
                newsTitlePhoto = this.getPlayerPhotoUrl(newsTitleComponent, "'s", mapPlayers);
                if(newsTitlePhoto != null) {
                    break;
                }
            }
        }

        return newsTitlePhoto;
    }

    // Processes name tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes date tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "publishedDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "publishedDate");
        return pubDate;
    }

    // Processes city tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    // For the tags description, extracts their text values.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            int eofDescription = result.indexOf("<br/>");
            if(eofDescription != -1) {
                result = result.substring(0, eofDescription);
            }
            parser.nextTag();
        }
        return result;
    }

    // For the tags description, extracts their text values.
    private String readCover(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "teaserImage");
        String cover = "http://www.wtatennis.com" + readText(parser).trim();
        parser.require(XmlPullParser.END_TAG, ns, "teaserImage");
        return cover.trim();
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
