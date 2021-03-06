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
import info.lusito.jamendo.api.results.user.UserAlbum;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.AlbumRelation;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.UserAlbumOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JamQuery(value = "users/albums")
public class UserAlbumQuery extends AbstractQueryList<UserAlbum, UserAlbumQuery> {

    protected List<UserAlbumOrder> order;
    protected List<Integer> id;
    protected String access_token;
    protected String name;
    protected ImageSize imagesize;
    protected List<Integer> album_id;
    protected ImageSize album_imagesize;
    protected List<AlbumRelation> relation;

    public UserAlbumQuery order(UserAlbumOrder... values) {
        if (order == null) {
            order = new ArrayList<UserAlbumOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public UserAlbumQuery id(Integer... values) {
        if (id == null) {
            id = new ArrayList<Integer>();
        }
        id.addAll(Arrays.asList(values));
        return this;
    }

    public UserAlbumQuery access_token(String value) {
        access_token = value;
        return this;
    }

    public UserAlbumQuery name(String value) {
        name = value;
        return this;
    }

    /**
     * @param value Currently only values _30, _50 and _100 are supported
     */
    public UserAlbumQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }

    public UserAlbumQuery album_id(Integer... values) {
        if (album_id == null) {
            album_id = new ArrayList<Integer>();
        }
        album_id.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * @param value _30 is not supported
     */
    public UserAlbumQuery album_imagesize(ImageSize value) {
        album_imagesize = value;
        return this;
    }

    public UserAlbumQuery relation(AlbumRelation... values) {
        if (relation == null) {
            relation = new ArrayList<AlbumRelation>();
        }
        relation.addAll(Arrays.asList(values));
        return this;
    }
}
