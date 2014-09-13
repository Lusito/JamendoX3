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

package info.lusito.jamendo.api.query.review;

import info.lusito.jamendo.api.enums.AudioFormat;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.review.ReviewTrack;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ReviewOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "reviews/tracks")
public class ReviewTrackQuery extends AbstractQueryList<ReviewTrack, ReviewTrackQuery> {

    protected List<ReviewOrder> order;
    protected List<Integer> id;
    protected String lang;
    protected String datebetween;
    protected Integer user_id;
    protected String access_token;
    protected Boolean hasscore;
    protected List<Integer> track_id;
    protected Integer album_id;
    protected Integer artist_id;
    protected AudioFormat audioformat;
    protected AudioFormat audiodlformat;

    public ReviewTrackQuery order(ReviewOrder... values) {
        if (order == null) {
            order = new ArrayList<ReviewOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ReviewTrackQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ReviewTrackQuery lang(String value) {
        lang = value;
        return this;
    }

    public ReviewTrackQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ReviewTrackQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ReviewTrackQuery user_id(Integer value) {
        user_id = value;
        return this;
    }

    public ReviewTrackQuery access_token(String value) {
        access_token = value;
        return this;
    }

    public ReviewTrackQuery hasscore(boolean value) {
        hasscore = Boolean.valueOf(value);
        return this;
    }

    public ReviewTrackQuery track_id(Integer... values) {
        if (track_id == null) {
            track_id = new ArrayList<Integer>();
        }
        track_id.addAll(Arrays.asList(values));
        return this;
    }

    public ReviewTrackQuery album_id(Integer value) {
        album_id = value;
        return this;
    }

    public ReviewTrackQuery artist_id(Integer value) {
        artist_id = value;
        return this;
    }

    public ReviewTrackQuery audioformat(AudioFormat value) {
        audioformat = value;
        return this;
    }

    public ReviewTrackQuery audiodlformat(AudioFormat value) {
        audiodlformat = value;
        return this;
    }
}
