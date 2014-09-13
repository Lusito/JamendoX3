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

package info.lusito.jamendo.api.query.playlist;

import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.playlist.PlaylistTrack;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.AudioFormat;
import info.lusito.jamendo.api.enums.PlaylistOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "playlists/tracks")
public class PlaylistTrackQuery extends AbstractQueryList<PlaylistTrack, PlaylistTrackQuery> {

    protected List<PlaylistOrder> order;
    protected List<Integer> id;
    protected String name;
    protected String namesearch;
    protected List<Integer> user_id;
    protected String access_token;
    protected String user_name;
    protected String datebetween;
    protected AudioFormat audioformat;
    protected AudioFormat audiodlformat;
    protected ImageSize imagesize;
    protected String positionbetween;

    public PlaylistTrackQuery order(PlaylistOrder... values) {
        if (order == null) {
            order = new ArrayList<PlaylistOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public PlaylistTrackQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public PlaylistTrackQuery name(String value) {
        name = value;
        return this;
    }

    public PlaylistTrackQuery namesearch(String value) {
        namesearch = value;
        return this;
    }

    public PlaylistTrackQuery user_id(Integer... values) {
        if (user_id == null) {
            user_id = new ArrayList<Integer>();
        }
        user_id.addAll(Arrays.asList(values));
        return this;
    }

    public PlaylistTrackQuery access_token(String value) {
        access_token = value;
        return this;
    }

    public PlaylistTrackQuery user_name(String value) {
        user_name = value;
        return this;
    }

    public PlaylistTrackQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public PlaylistTrackQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public PlaylistTrackQuery audioformat(AudioFormat value) {
        audioformat = value;
        return this;
    }

    public PlaylistTrackQuery audiodlformat(AudioFormat value) {
        audiodlformat = value;
        return this;
    }

    public PlaylistTrackQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }

    public PlaylistTrackQuery positionbetween(String value) {
        positionbetween = value;
        return this;
    }

    public PlaylistTrackQuery positionbetween(int from, int to) {
        positionbetween = from + "_" + to;
        return this;
    }
}
