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
import info.lusito.jamendo.api.results.artist.ArtistLocation;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.LocationCountry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "artists/locations")
public class ArtistLocationQuery extends AbstractQueryList<ArtistLocation, ArtistLocationQuery> {

    protected List<ArtistOrder> order;
    protected List<Integer> id;
    protected String name;
    protected String namesearch;
    protected Boolean hasimage;
    protected String datebetween;
    protected Boolean haslocation;
    protected List<LocationCountry> location_country;
    protected String location_city;
    protected String location_coords;
    protected Integer location_radius;

    /**
     * @param values Does not support TRACK_* or ALBUM_* values
     */
    public ArtistLocationQuery order(ArtistOrder... values) {
        if (order == null) {
            order = new ArrayList<ArtistOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistLocationQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistLocationQuery name(String value) {
        name = value;
        return this;
    }

    public ArtistLocationQuery namesearch(String value) {
        namesearch = value;
        return this;
    }

    public ArtistLocationQuery hasimage(boolean value) {
        hasimage = Boolean.valueOf(value);
        return this;
    }

    public ArtistLocationQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ArtistLocationQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ArtistLocationQuery haslocation(boolean value) {
        haslocation = Boolean.valueOf(value);
        return this;
    }

    public ArtistLocationQuery location_country(LocationCountry... values) {
        if (location_country == null) {
            location_country = new ArrayList<LocationCountry>();
        }
        location_country.addAll(Arrays.asList(values));
        return this;
    }

    public ArtistLocationQuery location_city(String value) {
        location_city = value;
        return this;
    }

    public ArtistLocationQuery location_coords(String value) {
        location_coords = value;
        return this;
    }

    public ArtistLocationQuery location_radius(int value) {
        location_radius = Integer.valueOf(value);
        return this;
    }
}
