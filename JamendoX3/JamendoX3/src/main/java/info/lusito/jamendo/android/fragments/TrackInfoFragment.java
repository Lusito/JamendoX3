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

package info.lusito.jamendo.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.adapter.ReviewTrackQueryAdapter;
import info.lusito.jamendo.android.adapter.TrackSimilarQueryAdapter;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.widgets.AlbumArtistInfo;
import info.lusito.jamendo.android.widgets.QueryListView;
import info.lusito.jamendo.api.enums.ReviewOrder;
import info.lusito.jamendo.api.query.review.ReviewTrackQuery;
import info.lusito.jamendo.api.query.track.TrackSimilarQuery;

public class TrackInfoFragment extends JamFragment {
    private static final int TRACK_LIMIT = 50;
    private static final int REVIEW_LIMIT = 20;
    private MainActivity activity;
    private AlbumArtistInfo trackInfo;

    public static TrackInfoFragment create(int track_id, int album_id, int artist_id) {
        TrackInfoFragment fragment = new TrackInfoFragment();
        Bundle args = new Bundle();
        args.putInt("track_id", track_id);
        args.putInt("album_id", album_id);
        args.putInt("artist_id", artist_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int track_id = getArguments().getInt("track_id");
        int album_id = getArguments().getInt("album_id");
        int artist_id = getArguments().getInt("artist_id");
        activity = MainActivity.getInstance();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_album_artist_info, container, false);
        trackInfo = (AlbumArtistInfo) layout.findViewById(R.id.infoPanel);
        trackInfo.setTrack(track_id);

        setListTitle(layout, R.id.firstListTitle, "Similar Tracks");
        TrackSimilarQueryAdapter tracks = new TrackSimilarQueryAdapter().hideAlbum().hideImage();
        QueryListView firstList = (QueryListView) layout.findViewById(R.id.firstList);
        tracks.setListView(firstList);
        tracks.query(new TrackSimilarQuery().id(track_id).no_artist(artist_id).no_album(artist_id).limit(TRACK_LIMIT));
        addStoredList(firstList);

        setListTitle(layout, R.id.secondListTitle, "Reviews");
        ReviewTrackQueryAdapter reviews = new ReviewTrackQueryAdapter();
        QueryListView secondList = (QueryListView) layout.findViewById(R.id.secondList);
        reviews.setListView(secondList);
        reviews.query(new ReviewTrackQuery().track_id(track_id).limit(REVIEW_LIMIT).order(ReviewOrder.ADDEDDATE_DESC));
        addStoredList(secondList);

        return layout;
    }

    private void setListTitle(View layout, int resourceId, String text) {
        TextView tv = (TextView) layout.findViewById(resourceId);
        tv.setText(text);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public ShareInfo getShareInfo() {
        return trackInfo.getShareInfo();
    }

}