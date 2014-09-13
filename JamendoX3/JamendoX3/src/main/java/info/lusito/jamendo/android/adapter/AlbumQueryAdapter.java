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

package info.lusito.jamendo.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.fragments.AlbumInfoFragment;
import info.lusito.jamendo.android.widgets.RemoteImageView;
import info.lusito.jamendo.api.query.album.AlbumQuery;
import info.lusito.jamendo.api.query.track.TrackQuery;
import info.lusito.jamendo.api.results.album.Album;

public class AlbumQueryAdapter extends AbstractQueryAdapter<AlbumQuery, Album> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public boolean hideArtist;
    public boolean hideReleaseDate;

    private class ViewHolder extends AbstractViewHolder<Album> implements View.OnClickListener {
        private final TextView title;
        private final TextView artistReleaseDate;
        private final RemoteImageView remoteImage;

        ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            artistReleaseDate = (TextView) convertView.findViewById(R.id.artistReleaseDate);
            remoteImage = (RemoteImageView) convertView.findViewById(R.id.remoteImage);
            Button addButton = (Button)convertView.findViewById(R.id.addButton);
            addButton.setOnClickListener(this);
        }

        @Override
        public void onCreate(Album entry) {
            title.setText(entry.name);
            artistReleaseDate.setText(getArtistReleaseDate(entry));
            remoteImage.setUrl(entry.image, R.drawable.unknown_album);
        }

        private String getArtistReleaseDate(Album entry) {
            String value = "";
            if (!hideArtist) {
                value = entry.artist_name;
                if (!hideReleaseDate)
                    value += " / ";
            }
            if (!hideReleaseDate)
                value += DATE_FORMAT.format(entry.releasedate);
            return value;
        }

        @Override
        public void onClick(View v) {
            MainActivity.addToPlaylist((TrackQuery q)->q.album_id(entry.id));
        }
    }

    public AlbumQueryAdapter() {
        super(Album.class);
    }

    /**
     * Call right after construction
     */
    public void hideArtist() {
        hideArtist = true;
    }

    /**
     * Call right after construction
     */
    public void hideReleaseDate() {
        hideReleaseDate = true;
    }

    @Override
    protected AbstractViewHolder createViewHolder(LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.list_row_album, null));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder != null)
            MainActivity.getInstance().showFragment(AlbumInfoFragment.create(holder.entry.id), true);
    }
}