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

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.utils.ExecuteQueryTask;
import info.lusito.jamendo.android.utils.QueryTaskListener;
import info.lusito.jamendo.android.utils.ToastUtil;
import info.lusito.jamendo.android.utils.UiRunner;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.query.track.TrackQuery;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.radio.RadioStream;
import info.lusito.jamendo.api.results.track.Track;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RadioInfo extends LinearLayout implements QueryTaskListener<Track> {
    private TextView radioName;
    private TextView radioTime;

    private TextView radioArtistAlbum;

    private TextView trackName;
    private RemoteImageView radioImage;
    private long callmeback;
    private UiRunner radioTimeRefreshRunner;
    private ActionButtonList actionButtons;
    private Track track;

    public Track getTrack() {
        return track;
    }

    private class RefreshRadioTimeRequest implements Runnable {
        @Override
        public void run() {
            refreshRadioTime();
        }
    }

    public RadioInfo(Context context) {
        super(context);
        setup();
    }

    public RadioInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public RadioInfo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.widget_radio_info, this);

        radioName = (TextView) findViewById(R.id.radioName);
        radioTime = (TextView) findViewById(R.id.radioTime);
        radioArtistAlbum = (TextView) findViewById(R.id.radioArtistAlbum);
        trackName = (TextView) findViewById(R.id.trackName);
        radioImage = (RemoteImageView) findViewById(R.id.radioImage);
        actionButtons = (ActionButtonList) findViewById(R.id.actionButtons);

        if (!isInEditMode()) {
            setLoading("");
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        radioTimeRefreshRunner = new UiRunner(radioTime, executorService);
        radioTimeRefreshRunner.scheduleAtFixedRate(new RefreshRadioTimeRequest(), 500, 500, TimeUnit.MILLISECONDS);
    }

    public void setLoading(String radioText) {
        radioName.setText(radioText);
        radioTime.setText("--:--");
        radioArtistAlbum.setText("Loading ..");
        trackName.setText("Loading ..");
        radioImage.setLoading();
        actionButtons.data.clear();
        actionButtons.dataChanged();
    }

    public void refreshRadioTime() {
        int s = (int) Math.max(0, (callmeback - System.currentTimeMillis()) / 1000);
        int m = s / 60;
        int h = m / 60;
        s -= m * 60;

        if (h > 0) {
            m -= h * 60;
            radioTime.setText(String.format("%d:%02d:%02d", h, m, s));
        } else {
            radioTime.setText(String.format("%02d:%02d", m, s));
        }
    }

    public void updateStreamInfo(final RadioStream radioStream) {
        track = null;
        callmeback = System.currentTimeMillis() + radioStream.callmeback;
        refreshRadioTime();
        radioName.setText(radioStream.dispname);
        String artistAlbum = String.format("%s (%s)", radioStream.playingnow.artist_name, radioStream.playingnow.album_name);
        radioArtistAlbum.setText(artistAlbum);
        trackName.setText(radioStream.playingnow.track_name);

        actionButtons.data.clear();
        actionButtons.data.artist_id = radioStream.playingnow.artist_id;
        actionButtons.data.album_id = radioStream.playingnow.album_id;
        actionButtons.data.track_id = radioStream.playingnow.track_id;
        actionButtons.dataChanged();

        TrackQuery query = new TrackQuery().id(radioStream.playingnow.track_id).imagesize(ImageSize._600);
        ExecuteQueryTask.execute(this, query, Track.class);
    }

    @Override
    public void onResult(LinkedList<Track> result, Header header) {
        if (result == null || result.isEmpty()) {
            ToastUtil.showLong("Error getting track info for album image");
        } else {
            track = result.get(0);
            radioImage.setUrl(track.album_image, R.drawable.unknown_album);
            actionButtons.data.prourl = track.prourl;
            actionButtons.data.download = track.audio;
            actionButtons.dataChanged();
        }
    }
}
