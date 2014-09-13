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

package info.lusito.jamendo.api.results.user;

import info.lusito.jamendo.api.annotations.JamResultList;

import java.util.Date;
import java.util.LinkedList;

public class UserAlbum {
    public String name;
    public String dispname;
    public Integer id;
    public String lang;
    public Date creationdate;
    public String image;
    @JamResultList(Album.class)
    public LinkedList<Album> albums;

    public static class Album {

        public static class Relation {

            public Integer myalbums;
        }

        public Integer id;
        public String name;
        public Date releasedate;
        public Integer artist_id;
        public String artist_name;
        public Date updatedate;
        public String image;
        @JamResultList(Relation.class)
        public LinkedList<Relation> relations;
    }
}
