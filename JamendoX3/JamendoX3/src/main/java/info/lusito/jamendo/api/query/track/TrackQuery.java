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

package info.lusito.jamendo.api.query.track;

import info.lusito.jamendo.api.enums.TrackBoost;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.track.Track;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.AudioFormat;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.TrackAcousticElectric;
import info.lusito.jamendo.api.enums.TrackGender;
import info.lusito.jamendo.api.enums.TrackGroupBy;
import info.lusito.jamendo.api.enums.TrackInclude;
import info.lusito.jamendo.api.enums.TrackOrder;
import info.lusito.jamendo.api.enums.TrackSpeed;
import info.lusito.jamendo.api.enums.TrackVocalInstrumental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "tracks")
public class TrackQuery extends AbstractQueryList<Track, TrackQuery> {

    protected List<TrackOrder> order;
    protected List<Integer> id;
    protected String name;
    protected String namesearch;
    protected List<Integer> album_id;
    protected String album_name;
    protected List<Integer> artist_id;
    protected String artist_name;
    protected String datebetween;
    protected Boolean featured;
    protected ImageSize imagesize;
    protected AudioFormat audioformat;
    protected AudioFormat audiodlformat;
    protected List<String> tags;
    protected List<String> fuzzytags;
    protected TrackAcousticElectric acousticelectric;
    protected TrackVocalInstrumental vocalinstrumental;
    protected TrackGender gender;
    protected TrackSpeed speed;
    protected List<String> lang;
    protected String durationbetween;
    protected String xartist;
    protected String search;
    protected Boolean prolicensing;
    protected Boolean probackground;
    protected Boolean ccsa;
    protected Boolean ccnd;
    protected Boolean ccnc;
    protected List<TrackInclude> include;
    protected TrackGroupBy groupby;
    protected TrackBoost boost;

    public TrackQuery order(TrackOrder... values) {
        if (order == null) {
            order = new ArrayList<TrackOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery name(String value) {
        name = value;
        return this;
    }

    public TrackQuery namesearch(String value) {
        namesearch = value;
        return this;
    }

    public TrackQuery album_id(Integer... values) {
        if (album_id == null) {
            album_id = new ArrayList<Integer>();
        }
        album_id.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery album_name(String value) {
        album_name = value;
        return this;
    }

    public TrackQuery artist_id(Integer... values) {
        if (artist_id == null) {
            artist_id = new ArrayList<Integer>();
        }
        artist_id.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery artist_name(String value) {
        artist_name = value;
        return this;
    }

    public TrackQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public TrackQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public TrackQuery featured(boolean value) {
        featured = Boolean.valueOf(value);
        return this;
    }

    public TrackQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }

    public TrackQuery audioformat(AudioFormat value) {
        audioformat = value;
        return this;
    }

    public TrackQuery audiodlformat(AudioFormat value) {
        audiodlformat = value;
        return this;
    }

    public TrackQuery tags(String... values) {
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        tags.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery fuzzytags(String... values) {
        if (fuzzytags == null) {
            fuzzytags = new ArrayList<String>();
        }
        fuzzytags.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery acousticelectric(TrackAcousticElectric value) {
        acousticelectric = value;
        return this;
    }

    public TrackQuery vocalinstrumental(TrackVocalInstrumental value) {
        vocalinstrumental = value;
        return this;
    }

    public TrackQuery gender(TrackGender value) {
        gender = value;
        return this;
    }

    public TrackQuery speed(TrackSpeed value) {
        speed = value;
        return this;
    }

    public TrackQuery lang(String... values) {
        if (lang == null) {
            lang = new ArrayList<String>();
        }
        lang.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery durationbetween(String value) {
        durationbetween = value;
        return this;
    }

    public TrackQuery durationbetween(int from, int to) {
        durationbetween = from + "_" + to;
        return this;
    }

    public TrackQuery xartist(String value) {
        xartist = value;
        return this;
    }

    public TrackQuery search(String value) {
        namesearch = value;
        return this;
    }

    public TrackQuery prolicensing(boolean value) {
        prolicensing = Boolean.valueOf(value);
        return this;
    }

    public TrackQuery probackground(boolean value) {
        probackground = Boolean.valueOf(value);
        return this;
    }

    public TrackQuery ccsa(boolean value) {
        ccsa = Boolean.valueOf(value);
        return this;
    }

    public TrackQuery ccnd(boolean value) {
        ccnd = Boolean.valueOf(value);
        return this;
    }

    public TrackQuery ccnc(boolean value) {
        ccnc = Boolean.valueOf(value);
        return this;
    }

    public TrackQuery include(TrackInclude... values) {
        if (include == null) {
            include = new ArrayList<TrackInclude>();
        }
        include.addAll(Arrays.asList(values));
        return this;
    }

    public TrackQuery groupby(TrackGroupBy value) {
        groupby = value;
        return this;
    }

    public TrackQuery boost(TrackBoost value) {
        boost = value;
        return this;
    }
}
