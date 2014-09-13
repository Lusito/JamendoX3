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

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.adapter.ReviewAlbumQueryAdapter;
import info.lusito.jamendo.android.adapter.TrackQueryAdapter;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.widgets.AlbumArtistInfo;
import info.lusito.jamendo.android.widgets.QueryListView;
import info.lusito.jamendo.api.enums.ReviewOrder;
import info.lusito.jamendo.api.query.review.ReviewAlbumQuery;
import info.lusito.jamendo.api.query.track.TrackQuery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlbumInfoFragment extends JamFragment {
    private static final int TRACK_LIMIT = 50;
    private static final int REVIEW_LIMIT = 20;
    private MainActivity activity;
    private AlbumArtistInfo albumInfo;

    public static AlbumInfoFragment create(int album_id) {
        AlbumInfoFragment fragment = new AlbumInfoFragment();
        Bundle args = new Bundle();
        args.putInt("album_id", album_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int album_id = getArguments().getInt("album_id");
        activity = MainActivity.getInstance();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_album_artist_info, container, false);
        albumInfo = (AlbumArtistInfo) layout.findViewById(R.id.infoPanel);
        albumInfo.setAlbum(album_id);

        setListTitle(layout, R.id.firstListTitle, "Top Tracks");
        TrackQueryAdapter tracks = new TrackQueryAdapter().hideAlbum().hideImage();
        QueryListView firstList = (QueryListView) layout.findViewById(R.id.firstList);
        tracks.setListView(firstList);
        tracks.query(new TrackQuery().album_id(album_id).limit(TRACK_LIMIT));
        addStoredList(firstList);

        setListTitle(layout, R.id.secondListTitle, "Reviews");
        ReviewAlbumQueryAdapter reviews = new ReviewAlbumQueryAdapter();
        QueryListView secondList = (QueryListView) layout.findViewById(R.id.secondList);
        reviews.setListView(secondList);
        reviews.query(new ReviewAlbumQuery().album_id(album_id).limit(REVIEW_LIMIT).order(ReviewOrder.ADDEDDATE_DESC));
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
        return albumInfo.getShareInfo();
    }

}