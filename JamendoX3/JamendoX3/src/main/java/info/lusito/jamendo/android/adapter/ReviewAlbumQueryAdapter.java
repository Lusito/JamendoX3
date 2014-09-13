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

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.api.query.review.ReviewAlbumQuery;
import info.lusito.jamendo.api.results.review.ReviewAlbum;

public class ReviewAlbumQueryAdapter extends AbstractQueryAdapter<ReviewAlbumQuery, ReviewAlbum> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private class ViewHolder extends AbstractViewHolder<ReviewAlbum> {
        private final TextView title;
        private final TextView userName;
        private final TextView date;
        private final TextView text;
        private final TextView score;
        private final TextView agreecnt;

        ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            userName = (TextView) convertView.findViewById(R.id.userName);
            date = (TextView) convertView.findViewById(R.id.date);
            text = (TextView) convertView.findViewById(R.id.text);
            score = (TextView) convertView.findViewById(R.id.score);
            agreecnt = (TextView) convertView.findViewById(R.id.agreecnt);
            userName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        @Override
        public void onCreate(ReviewAlbum entry) {
            title.setText(entry.title);
            userName.setText(entry.user_name);
            date.setText(DATE_FORMAT.format(entry.dateadded));
            text.setText(entry.text);
            score.setText(entry.score != null ? entry.score.toString() : "?");
            agreecnt.setText(entry.agreecnt != null ? entry.agreecnt.toString() : "?");
        }

    }

    public ReviewAlbumQueryAdapter() {
        super(ReviewAlbum.class);
    }

    @Override
    protected AbstractViewHolder createViewHolder(LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.list_row_review, null));
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
}