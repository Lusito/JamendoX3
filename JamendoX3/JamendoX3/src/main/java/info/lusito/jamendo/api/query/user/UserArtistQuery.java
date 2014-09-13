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

import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.user.UserArtist;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ArtistRelation;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.UserArtistOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JamQuery(value = "users/artists")
public class UserArtistQuery extends AbstractQueryList<UserArtist, UserArtistQuery> {

    protected List<UserArtistOrder> order;
    protected List<Integer> id;
    protected String access_token;
    protected String name;
    protected ImageSize imagesize;
    protected List<Integer> artist_id;
    protected List<ArtistRelation> relation;

    public UserArtistQuery order(UserArtistOrder... values) {
        if (order == null) {
            order = new ArrayList<UserArtistOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public UserArtistQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public UserArtistQuery access_token(String value) {
        access_token = value;
        return this;
    }

    public UserArtistQuery name(String value) {
        name = value;
        return this;
    }

    /**
     * @param value Currently only values _30, _50 and _100 are supported
     */
    public UserArtistQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }

    public UserArtistQuery artist_id(Integer... values) {
        if (artist_id == null) {
            artist_id = new ArrayList<Integer>();
        }
        artist_id.addAll(Arrays.asList(values));
        return this;
    }

    public UserArtistQuery relation(ArtistRelation... values) {
        if (relation == null) {
            relation = new ArrayList<ArtistRelation>();
        }
        relation.addAll(Arrays.asList(values));
        return this;
    }
}
