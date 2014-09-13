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
import info.lusito.jamendo.android.adapter.AlbumQueryAdapter;
import info.lusito.jamendo.android.adapter.TrackQueryAdapter;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.widgets.AlbumArtistInfo;
import info.lusito.jamendo.android.widgets.QueryListView;
import info.lusito.jamendo.api.enums.AlbumOrder;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.TrackOrder;
import info.lusito.jamendo.api.query.album.AlbumQuery;
import info.lusito.jamendo.api.query.track.TrackQuery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArtistInfoFragment extends JamFragment {
    private static final int TRACK_LIMIT = 50;
    private static final int ALBUM_LIMIT = 20;
    private static final ImageSize IMAGE_SIZE = ImageSize._35;
    private AlbumArtistInfo artistInfo;

    public static ArtistInfoFragment create(int artist_id) {
        ArtistInfoFragment fragment = new ArtistInfoFragment();
        Bundle args = new Bundle();
        args.putInt("artist_id", artist_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int artist_id = getArguments().getInt("artist_id");
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_album_artist_info, container, false);
        artistInfo = (AlbumArtistInfo) layout.findViewById(R.id.infoPanel);
        artistInfo.setArtist(artist_id);

        setListTitle(layout, R.id.firstListTitle, "Top Tracks");
        TrackQueryAdapter tracks = new TrackQueryAdapter().hideImage().hideArtist();
        tracks.setListView((QueryListView) layout.findViewById(R.id.firstList));
        tracks.query(new TrackQuery().artist_id(artist_id).order(TrackOrder.BUZZRATE).imagesize(IMAGE_SIZE).limit(TRACK_LIMIT));

        setListTitle(layout, R.id.secondListTitle, "Albums");
        AlbumQueryAdapter albums = new AlbumQueryAdapter();
        albums.setListView((QueryListView) layout.findViewById(R.id.secondList));
        albums.query(new AlbumQuery().artist_id(artist_id).order(AlbumOrder.RELEASEDATE_DESC).imagesize(IMAGE_SIZE).limit(ALBUM_LIMIT));
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
        return artistInfo.getShareInfo();
    }
}