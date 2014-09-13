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

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.fragments.TrackInfoFragment;
import info.lusito.jamendo.android.utils.TextUtils;
import info.lusito.jamendo.android.widgets.RemoteImageView;
import info.lusito.jamendo.api.query.track.TrackQuery;
import info.lusito.jamendo.api.results.track.Track;

public class TrackQueryAdapter extends AbstractQueryAdapter<TrackQuery, Track> {
    private boolean hideImage;
    private boolean hideArtist;
    private boolean hideAlbum;

    private class ViewHolder extends AbstractViewHolder<Track> implements View.OnClickListener {
        private final TextView title;
        private final TextView artistAlbum;
        private final TextView duration;
        private final RemoteImageView remoteImage;

        ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            artistAlbum = (TextView) convertView.findViewById(R.id.artistAlbum);
            duration = (TextView) convertView.findViewById(R.id.duration);
            remoteImage = (RemoteImageView) convertView.findViewById(R.id.remoteImage);
            if (hideImage)
                remoteImage.setVisibility(View.GONE);
            Button addButton = (Button)convertView.findViewById(R.id.addButton);
            addButton.setOnClickListener(this);
        }

        @Override
        public void onCreate(Track entry) {
            title.setText(entry.name);
            artistAlbum.setText(getArtistAlbum(entry));
            duration.setText(TextUtils.formatTime(entry.duration));
            if (!hideImage)
                remoteImage.setUrl(entry.album_image, R.drawable.unknown_album);
        }

        private String getArtistAlbum(Track entry) {
            String value = "";
            if (!hideArtist) {
                value = entry.artist_name;
                if (!hideAlbum)
                    value += " / ";
            }
            if (!hideAlbum)
                value += entry.album_name;
            return value;
        }

        @Override
        public void onClick(View v) {
            // fixme: play animation
            MainActivity.addToPlaylist(entry);
        }
    }

    public TrackQueryAdapter() {
        super(Track.class);
    }

    /**
     * Call right after construction
     */
    public TrackQueryAdapter hideImage() {
        hideImage = true;
        return this;
    }

    /**
     * Call right after construction
     */
    public TrackQueryAdapter hideArtist() {
        hideArtist = true;
        return this;
    }

    /**
     * Call right after construction
     */
    public TrackQueryAdapter hideAlbum() {
        hideAlbum = true;
        return this;
    }

    @Override
    protected AbstractViewHolder createViewHolder(LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.list_row_track, null));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder != null)
            MainActivity.getInstance().showFragment(TrackInfoFragment.create(holder.entry.id, holder.entry.album_id, holder.entry.artist_id), true);
    }
}