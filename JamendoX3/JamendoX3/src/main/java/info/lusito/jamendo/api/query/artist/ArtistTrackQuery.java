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

package info.lusito.jamendo.api.query.artist;

import info.lusito.jamendo.api.enums.AudioFormat;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.artist.ArtistTrack;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ArtistOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "artists/tracks")
public class ArtistTrackQuery extends AbstractQueryList<ArtistTrack, ArtistTrackQuery> {

    protected List<ArtistOrder> order;
    protected List<Integer> id;
    protected String name;
    protected String namesearch;
    protected Boolean hasimage;
    protected String datebetween;
    protected List<Integer> track_id;
    protected String track_name;
    protected String album_datebetween;
    protected List<Integer> album_id;
    protected String album_name;
    protected AudioFormat audioformat;
    protected AudioFormat audiodlformat;

    /**
     * @param values Does not support ALBUM_* values
     */
    public ArtistTrackQuery order(ArtistOrder... values) {
        if (order == null) {
            order = new ArrayList<ArtistOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistTrackQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistTrackQuery name(String value) {
        name = value;
        return this;
    }

    public ArtistTrackQuery namesearch(String value) {
        namesearch = value;
        return this;
    }

    public ArtistTrackQuery hasimage(boolean value) {
        hasimage = Boolean.valueOf(value);
        return this;
    }

    public ArtistTrackQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ArtistTrackQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ArtistTrackQuery track_id(Integer... values) {
        if (track_id == null) {
            track_id = new ArrayList<Integer>();
        }
        track_id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistTrackQuery track_name(String value) {
        track_name = value;
        return this;
    }

    public ArtistTrackQuery album_datebetween(String value) {
        album_datebetween = value;
        return this;
    }

    public ArtistTrackQuery album_datebetween(Date from, Date to) {
        album_datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ArtistTrackQuery album_id(Integer... values) {
        if (album_id == null) {
            album_id = new ArrayList<Integer>();
        }
        album_id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistTrackQuery album_name(String value) {
        album_name = value;
        return this;
    }

    public ArtistTrackQuery audioformat(AudioFormat value) {
        audioformat = value;
        return this;
    }

    public ArtistTrackQuery audiodlformat(AudioFormat value) {
        audiodlformat = value;
        return this;
    }
}
