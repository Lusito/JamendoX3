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
import info.lusito.jamendo.api.results.artist.ArtistAlbum;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ImageSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "artists/albums")
public class ArtistAlbumQuery extends AbstractQueryList<ArtistAlbum, ArtistAlbumQuery> {

    protected List<ArtistOrder> order;
    protected List<Integer> id;
    protected String name;
    protected String namesearch;
    protected Boolean hasimage;
    protected String datebetween;
    protected List<Integer> album_id;
    protected String album_name;
    protected String album_datebetween;
    protected ImageSize imagesize;

    /**
     * @param values Does not support TRACK_* values
     */
    public ArtistAlbumQuery order(ArtistOrder... values) {
        if (order == null) {
            order = new ArrayList<ArtistOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistAlbumQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistAlbumQuery name(String value) {
        name = value;
        return this;
    }

    public ArtistAlbumQuery namesearch(String value) {
        namesearch = value;
        return this;
    }

    public ArtistAlbumQuery hasimage(boolean value) {
        hasimage = Boolean.valueOf(value);
        return this;
    }

    public ArtistAlbumQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ArtistAlbumQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ArtistAlbumQuery album_id(Integer... values) {
        if (album_id == null) {
            album_id = new ArrayList<Integer>();
        }
        album_id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistAlbumQuery album_name(String value) {
        album_name = value;
        return this;
    }

    public ArtistAlbumQuery album_datebetween(String value) {
        album_datebetween = value;
        return this;
    }

    public ArtistAlbumQuery album_datebetween(Date from, Date to) {
        album_datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ArtistAlbumQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }
}
