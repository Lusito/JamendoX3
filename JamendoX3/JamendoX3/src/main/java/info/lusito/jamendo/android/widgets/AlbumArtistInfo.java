/*
 * Copyright (c) 2014 Santo Pfingsten
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose, including commercial
 * applications, and to alter it and redistribute it freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not claim that you wrote the
 *    original software. If you use this software in a product, an acknowledgment in the product
 *    documentation would be appreciated but is not required.
 *
 * 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being
 *    the original software.
 *
 * 3. This notice may not be removed or altered from any source distribution.
 */

package info.lusito.jamendo.android.widgets;

import java.util.ArrayList;
import java.util.LinkedList;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.utils.ExecuteQueryTask;
import info.lusito.jamendo.android.utils.QueryTaskListener;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.utils.TextUtils;
import info.lusito.jamendo.android.utils.ToastUtil;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.TrackInclude;
import info.lusito.jamendo.api.query.album.AlbumMusicInfoQuery;
import info.lusito.jamendo.api.query.artist.ArtistMusicInfoQuery;
import info.lusito.jamendo.api.query.track.TrackQuery;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.album.AlbumMusicInfo;
import info.lusito.jamendo.api.results.artist.ArtistMusicInfo;
import info.lusito.jamendo.api.results.track.Track;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlbumArtistInfo extends LinearLayout {

    private static final ImageSize IMAGE_SIZE = ImageSize._300;
    private RemoteImageView imageView;
    private TextView title;
    private TextView tagList;
    private WebView description;
    private ActionButtonList actionButtons;
    private AlbumMusicInfo albumInfo;
    private ArtistMusicInfo artistInfo;
    private Track track;

    public AlbumArtistInfo(Context context) {
        super(context);
        setup();
    }

    public AlbumArtistInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public AlbumArtistInfo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.widget_album_artist_info, this);

        imageView = (RemoteImageView) findViewById(R.id.remoteImage);
        title = (TextView) findViewById(R.id.title);
        tagList = (TextView) findViewById(R.id.tagList);
        description = (WebView) findViewById(R.id.description);
        actionButtons = (ActionButtonList) findViewById(R.id.actionButtons);

        description.setBackgroundColor(Color.argb(1, 0, 0, 0));

        if (!isInEditMode()) {
            WebSettings webSettings = description.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            setLoading();
        }
    }

    public void setLoading() {
        title.setText("Loading...");
        tagList.setText("");
        setHtmlDescription("");
        imageView.setLoading();
        actionButtons.data.clear();
        actionButtons.dataChanged();
    }

    private void setHtmlDescription(String html) {
        description.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        description.setBackgroundColor(Color.argb(1, 0, 0, 0));
    }

    public void setArtist(int artist_id) {
        actionButtons.setupButtons("favorite,website");
        QueryTaskListener<ArtistMusicInfo> listener = new QueryTaskListener<ArtistMusicInfo>() {

            @Override
            public void onResult(LinkedList<ArtistMusicInfo> result, Header header) {
                if (result == null || result.isEmpty()) {
                    ToastUtil.showLong("Error getting artist info");
                } else {
                    artistInfo = result.get(0);
                    imageView.setUrl(artistInfo.image, R.drawable.unknown_artist);

                    title.setText(artistInfo.name);
                    tagList.setText("[" + TextUtils.join(artistInfo.musicinfo.tags, ",") + "]");

                    if (artistInfo.musicinfo.description == null) {
                        setHtmlDescription("No description available");
                    } else {
                        // Try to get english
                        String desc = artistInfo.musicinfo.description.get("en");
                        // Fall back to the first available language
                        if (desc == null)
                            desc = artistInfo.musicinfo.description.values().iterator().next();
                        setHtmlDescription(desc);
                    }

                    actionButtons.data.website = artistInfo.website;
                    actionButtons.data.artist_id = artistInfo.id;
                    actionButtons.dataChanged();
                }
            }
        };

        ArtistMusicInfoQuery query = new ArtistMusicInfoQuery().id(artist_id);
        ExecuteQueryTask.execute(listener, query, ArtistMusicInfo.class);
    }

    public void setAlbum(int album_id) {
        actionButtons.setupButtons("favorite,download,artist");
        QueryTaskListener<AlbumMusicInfo> listener = new QueryTaskListener<AlbumMusicInfo>() {

            @Override
            public void onResult(LinkedList<AlbumMusicInfo> result, Header header) {
                if (result == null|| result.isEmpty()) {
                    ToastUtil.showLong("Error getting album info");
                } else {
                     albumInfo = result.get(0);
                    imageView.setUrl(albumInfo.image, R.drawable.unknown_album);

                    title.setText(albumInfo.name);
                    tagList.setText("[" + TextUtils.join(albumInfo.musicinfo.tags, ",") + "]");

                    if (albumInfo.musicinfo.description == null) {
                        setHtmlDescription("No description available");
                    } else {
                        // Try to get english
                        String desc = albumInfo.musicinfo.description.get("en");
                        // Fall back to the first available language
                        if (desc == null)
                            desc = albumInfo.musicinfo.description.values().iterator().next();
                        setHtmlDescription(desc);
                    }

                    actionButtons.data.download = albumInfo.zip;
                    actionButtons.data.album_id = albumInfo.id;
                    actionButtons.data.artist_id = albumInfo.artist_id;
                    actionButtons.dataChanged();
                }
            }
        };

        AlbumMusicInfoQuery query = new AlbumMusicInfoQuery().id(album_id).imagesize(IMAGE_SIZE);
        ExecuteQueryTask.execute(listener, query, AlbumMusicInfo.class);
    }

    private void addLineIfNotNull(StringBuilder sb, String text, Object value) {
        if(value != null && !value.toString().trim().isEmpty())
            sb.append("<b>").append(text).append(": </b>").append(value.toString()).append("<br/>");
    }

    public boolean has(Object value) {
        return value != null && !value.toString().trim().isEmpty();
    }
    public boolean is(Boolean value) {
        return value != null && value.booleanValue();
    }

    private void addLineIfNotNull(StringBuilder sb, String text, Object value1, Object value2) {
        if(has(value1)) {
            sb.append("<b>").append(text).append(": </b>").append(value1.toString());
            if(has(value2))
                sb.append(" [").append(value2).append("]");
            sb.append("<br/>");
        }
    }

    public void setTrack(int track_id) {
        actionButtons.setupButtons("favorite,download,artist,album,buy");
        QueryTaskListener<Track> listener = new QueryTaskListener<Track>() {

            @Override
            public void onResult(LinkedList<Track> result, Header header) {
                if (result == null|| result.isEmpty()) {
                    ToastUtil.showLong("Error getting track info");
                } else {
                    track = result.get(0);
                    imageView.setUrl(track.album_image, R.drawable.unknown_album);

                    title.setText(track.name);
                    ArrayList<String> tags = new ArrayList();
                    if(track.musicinfo.tags.genres != null)
                        tags.add(TextUtils.join(track.musicinfo.tags.genres, ","));
                    if(track.musicinfo.tags.instruments != null)
                        tags.add(TextUtils.join(track.musicinfo.tags.instruments, ","));
                    if(track.musicinfo.tags.vartags != null)
                        tags.add(TextUtils.join(track.musicinfo.tags.vartags, ","));
                    tagList.setText("[" + TextUtils.join(tags, ",") + "]");

                    StringBuilder sb = new StringBuilder();
                    sb.append("<h3>Track Info:</h3>");
                    addLineIfNotNull(sb, "Artist", track.artist_name);
                    addLineIfNotNull(sb, "Album", track.album_name, track.position);
                    addLineIfNotNull(sb, "Duration", track.duration == null ? null : TextUtils.formatTime(track.duration));
                    addLineIfNotNull(sb, "Released", track.releasedate);

                    if (track.licenses != null) {
                        boolean ccnd = is(track.licenses.ccnd);
                        boolean ccnc = is(track.licenses.ccnc);
                        boolean ccsa = is(track.licenses.ccsa);
                        if(ccnd || ccnc || ccsa) {
                            String license = "by";
                            if(!ccnd && ccnc)
                                license = ccsa ? "by-nc-sa" : "by-nc";
                            else if(!ccsa && ccnd)
                                license = ccnc ? "by-nc-nd" : "by-nd";
                            else if(ccsa && !ccnc && !ccnd)
                                license = "by-sa";
                            String imgUrl = "http://i.creativecommons.org/l/" + license + "/3.0/88x31.png";

                            if(track.license_ccurl != null)
                                sb.append("<a href=\"" + track.license_ccurl + "\">");
                            sb.append("<img src=\"").append(imgUrl).append("\"/>");
                            if(track.license_ccurl != null)
                                sb.append("</a>");
                            sb.append("<br/>");
                        }
                    }
                    if(track.musicinfo != null && (
                            has(track.musicinfo.lang) ||
                                    has(track.musicinfo.gender) ||
                                    has(track.musicinfo.vocalinstrumental) ||
                                    has(track.musicinfo.acousticelectric) ||
                                    has(track.musicinfo.speed)
                    )) {
                        sb.append("<h3>Music Info:</h3>");
                        addLineIfNotNull(sb, "Language", track.musicinfo.lang);
                        addLineIfNotNull(sb, "Gender", track.musicinfo.gender);
                        addLineIfNotNull(sb, "Vocal/Intrumental", track.musicinfo.vocalinstrumental);
                        addLineIfNotNull(sb, "Acoustic/Electric", track.musicinfo.acousticelectric);
                        addLineIfNotNull(sb, "Speed", track.musicinfo.speed);
                    }
                    if(track.stats != null && (
                            has(track.stats.rate_downloads_total) ||
                                    has(track.stats.rate_listened_total) ||
                                    has(track.stats.playlisted) ||
                                    has(track.stats.favorited) ||
                                    has(track.stats.likes) ||
                                    has(track.stats.dislikes) ||
                                    has(track.stats.avgnote)
                    )) {
                        sb.append("<h3>Statistics:</h3>");
                        addLineIfNotNull(sb, "Downloads", track.stats.rate_downloads_total);
                        addLineIfNotNull(sb, "Listens:", track.stats.rate_listened_total);
                        addLineIfNotNull(sb, "Playlisted", track.stats.playlisted);
                        addLineIfNotNull(sb, "Favorited", track.stats.favorited);
                        addLineIfNotNull(sb, "Likes", track.stats.likes);
                        addLineIfNotNull(sb, "Dislikes", track.stats.dislikes);
                        addLineIfNotNull(sb, "Avg. Note", track.stats.avgnote, track.stats.notes);
                    }
                    String value = sb.toString();
                    if (value.isEmpty()) {
                        setHtmlDescription("No data available");
                    } else {
                        setHtmlDescription(value);
                    }

                    actionButtons.data.download = track.audiodownload;
                    actionButtons.data.track_id = track.id;
                    actionButtons.data.album_id = track.album_id;
                    actionButtons.data.artist_id = track.artist_id;
                    actionButtons.data.prourl = track.prourl;
                    actionButtons.dataChanged();
                }
            }
        };

        TrackQuery query = new TrackQuery().id(track_id).imagesize(IMAGE_SIZE).include(TrackInclude.values());
        ExecuteQueryTask.execute(listener, query, Track.class);
    }

    public ShareInfo getShareInfo() {
        if(albumInfo != null)
            return new ShareInfo(albumInfo);
        if(artistInfo != null)
            return new ShareInfo(artistInfo);
        if(track != null)
            return new ShareInfo(track);
        return null;
    }
}
