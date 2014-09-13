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

package info.lusito.jamendo.api.results.artist;

import info.lusito.jamendo.api.annotations.JamResultMap;
import info.lusito.jamendo.api.annotations.JamResultList;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class ArtistMusicInfo {

    public Integer id;
    public String name;
    public String website;
    public Date joindate;
    public String image;
    public String shorturl;
    public String shareurl;
    public MusicInfo musicinfo;

    public static class MusicInfo {

        @JamResultList(String.class)
        public LinkedList<String> tags;
        @JamResultMap(type=String.class, defaultKey = "en")
        public Map<String, String> description;
    }
}
