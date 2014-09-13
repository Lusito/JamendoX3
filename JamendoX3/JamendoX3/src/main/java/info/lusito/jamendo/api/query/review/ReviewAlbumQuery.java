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

import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.review.ReviewAlbum;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ReviewOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "reviews/albums")
public class ReviewAlbumQuery extends AbstractQueryList<ReviewAlbum, ReviewAlbumQuery> {

    protected List<ReviewOrder> order;
    protected List<Integer> id;
    protected String lang;
    protected String datebetween;
    protected Integer user_id;
    protected String access_token;
    protected Boolean hasscore;
    protected List<Integer> album_id;
    protected Integer artist_id;

    public ReviewAlbumQuery order(ReviewOrder... values) {
        if (order == null) {
            order = new ArrayList<ReviewOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ReviewAlbumQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ReviewAlbumQuery lang(String value) {
        lang = value;
        return this;
    }

    public ReviewAlbumQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ReviewAlbumQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ReviewAlbumQuery user_id(Integer value) {
        user_id = value;
        return this;
    }

    public ReviewAlbumQuery access_token(String value) {
        access_token = value;
        return this;
    }

    public ReviewAlbumQuery hasscore(boolean value) {
        hasscore = Boolean.valueOf(value);
        return this;
    }

    public ReviewAlbumQuery album_id(Integer... values) {
        if (album_id == null) {
            album_id = new ArrayList<Integer>();
        }
        album_id.addAll(Arrays.asList(values));
        return this;
    }

    public ReviewAlbumQuery artist_id(Integer value) {
        artist_id = value;
        return this;
    }
}
