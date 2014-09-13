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

package info.lusito.jamendo.api.query.user;

import info.lusito.jamendo.api.enums.AudioFormat;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.user.UserTrack;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.TrackRelation;
import info.lusito.jamendo.api.enums.UserTrackOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JamQuery(value = "users/tracks")
public class UserTrackQuery extends AbstractQueryList<UserTrack, UserTrackQuery> {

    protected List<UserTrackOrder> order;
    protected List<Integer> id;
    protected String access_token;
    protected ImageSize imagesize;
    protected List<Integer> track_id;
    protected List<Integer> artist_id;
    protected List<Integer> album_id;
    protected AudioFormat audioformat;
    protected AudioFormat audiodlformat;
    protected ImageSize album_imagesize;
    protected List<TrackRelation> relation;

    public UserTrackQuery order(UserTrackOrder... values) {
        if (order == null) {
            order = new ArrayList<UserTrackOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public UserTrackQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }
    public UserTrackQuery access_token(String value) {
        access_token = value;
        return this;
    }

    /**
     * @param value Currently only values _30, _50 and _100 are supported
     */
    public UserTrackQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }

    public UserTrackQuery track_id(Integer... values) {
        if (track_id == null) {
            track_id = new ArrayList<Integer>();
        }
        track_id.addAll(Arrays.asList(values));
        return this;
    }

    public UserTrackQuery artist_id(Integer... values) {
        if (artist_id == null) {
            artist_id = new ArrayList<Integer>();
        }
        artist_id.addAll(Arrays.asList(values));
        return this;
    }

    public UserTrackQuery album_id(Integer... values) {
        if (album_id == null) {
            album_id = new ArrayList<Integer>();
        }
        album_id.addAll(Arrays.asList(values));
        return this;
    }

    public UserTrackQuery audioformat(AudioFormat value) {
        audioformat = value;
        return this;
    }

    public UserTrackQuery audiodlformat(AudioFormat value) {
        audiodlformat = value;
        return this;
    }

    /**
     * @param value _30 is not supported
     */
    public UserTrackQuery album_imagesize(ImageSize value) {
        album_imagesize = value;
        return this;
    }

    public UserTrackQuery relation(TrackRelation... values) {
        if (relation == null) {
            relation = new ArrayList<TrackRelation>();
        }
        relation.addAll(Arrays.asList(values));
        return this;
    }
}
