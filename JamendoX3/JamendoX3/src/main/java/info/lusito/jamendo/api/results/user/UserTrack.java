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

public class UserTrack {
    public String name;
    public String dispname;
    public Integer id;
    public String lang;
    public Date creationdate;
    public String avatar_type; // fixme: probably an enum?
    public String avatar;
    @JamResultList(Track.class)
    public LinkedList<Track> tracks;

    public static class Track {

        public static class Relation {

            public Integer review;
            public Integer favorite;
            public Integer like;
        }

        public Integer id;
        public String name;
        public Date releasedate;
        public Integer album_id;
        public Integer artist_id;
        public Integer duration;
        public String artist_name;
        public String license_ccurl;
        public Date updatedate;
        public String album_image;
        public String audio;
        public String audiodownload;
        @JamResultList(Relation.class)
        public LinkedList<Relation> relations;
    }
}
