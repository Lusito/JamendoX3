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

import info.lusito.jamendo.api.enums.ArtistOrder;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.artist.ArtistMusicInfo;
import info.lusito.jamendo.api.annotations.JamQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "artists/musicinfo")
public class ArtistMusicInfoQuery extends AbstractQueryList<ArtistMusicInfo, ArtistMusicInfoQuery> {

    protected List<ArtistOrder> order;
    protected List<Integer> id;
    protected String name;
    protected String namesearch;
    protected Boolean hasimage;
    protected String datebetween;
    protected String tag;

    /**
     * @param values Does not support TRACK_* or ALBUM_* values
     */
    public ArtistMusicInfoQuery order(ArtistOrder... values) {
        if (order == null) {
            order = new ArrayList<ArtistOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistMusicInfoQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistMusicInfoQuery name(String value) {
        name = value;
        return this;
    }

    public ArtistMusicInfoQuery namesearch(String value) {
        namesearch = value;
        return this;
    }

    public ArtistMusicInfoQuery hasimage(boolean value) {
        hasimage = Boolean.valueOf(value);
        return this;
    }

    public ArtistMusicInfoQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ArtistMusicInfoQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ArtistMusicInfoQuery tag(String value) {
        tag = value;
        return this;
    }
}
