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

package info.lusito.jamendo.api.query.concert;

import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.concert.Concert;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ConcertOrder;
import info.lusito.jamendo.api.enums.LocationCountry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JamQuery(value = "concerts")
public class ConcertQuery extends AbstractQueryList<Concert, ConcertQuery> {

    protected List<ConcertOrder> order;
    protected List<Integer> id;
    protected List<Integer> artist_id;
    protected String artist_name;
    protected String datebetween;
    protected String location_name;
    protected LocationCountry location_country;
    protected String location_city;

    public ConcertQuery order(ConcertOrder... values) {
        if (order == null) {
            order = new ArrayList<ConcertOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public ConcertQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ConcertQuery artist_id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public ConcertQuery artist_name(String value) {
        artist_name = value;
        return this;
    }

    public ConcertQuery datebetween(String value) {
        datebetween = value;
        return this;
    }

    public ConcertQuery datebetween(Date from, Date to) {
        datebetween = DATE_FORMAT.format(from) + "_" + DATE_FORMAT.format(to);
        return this;
    }

    public ConcertQuery location_name(String value) {
        location_name = value;
        return this;
    }

    public ConcertQuery location_country(LocationCountry value) {
        location_country = value;
        return this;
    }

    public ConcertQuery location_city(String value) {
        location_city = value;
        return this;
    }
}
