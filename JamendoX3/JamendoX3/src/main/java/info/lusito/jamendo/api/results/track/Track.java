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

package info.lusito.jamendo.api.results.track;

import info.lusito.jamendo.api.enums.TrackVocalInstrumental;
import info.lusito.jamendo.api.enums.TrackSpeed;
import info.lusito.jamendo.api.enums.TrackGender;
import info.lusito.jamendo.api.enums.TrackAcousticElectric;

import info.lusito.jamendo.api.annotations.JamResultList;

import java.util.Date;
import java.util.LinkedList;

public class Track {

    public Integer id;
    public String name;
    public Integer duration;
    public Integer artist_id;
    public String artist_name;
    public String artist_idstr;
    public String album_name;
    public Integer album_id;
    public String license_ccurl;
    public Integer position;
    public Date releasedate;
    public String album_image;
    public String audio;
    public String audiodownload;
    public String prourl;
    public String shorturl;
    public String shareurl;
    public String lyrics;
    public MusicInfo musicinfo;
    public Licenses licenses;
    public Stats stats;
    public Float score; // used on similar track

    public static class MusicInfo {

        public TrackVocalInstrumental vocalinstrumental;
        public String lang;
        public TrackGender gender;
        public TrackAcousticElectric acousticelectric;
        public TrackSpeed speed;
        public Tag tags;

        public static class Tag {

            @JamResultList(String.class)
            public LinkedList<String> genres;
            @JamResultList(String.class)
            public LinkedList<String> instruments;
            @JamResultList(String.class)
            public LinkedList<String> vartags;
        }
    }

    public static class Licenses {

        public Boolean ccnc;
        public Boolean ccnd;
        public Boolean ccsa;
        public Boolean prolicensing;
        public Boolean probackground;
    }

    public static class Stats {

        public Integer rate_downloads_total;
        public Integer rate_listened_total;
        public Integer playlisted;
        public Integer favorited;
        public Integer likes;
        public Integer dislikes;
        public Float avgnote;
        public Integer notes;
    }
}
