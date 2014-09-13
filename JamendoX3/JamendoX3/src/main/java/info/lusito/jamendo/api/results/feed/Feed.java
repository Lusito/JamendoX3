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

package info.lusito.jamendo.api.results.feed;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import info.lusito.jamendo.api.annotations.JamResultList;
import info.lusito.jamendo.api.annotations.JamResultMap;
//fixme: query class still missing
public class Feed {
    public Integer id;
    @JamResultMap(type=String.class, defaultKey = "en")
    public Map<String, String> title;
    public String link;
    public Integer position;
    @JamResultList(String.class)
    public LinkedList<String> lang;
    public Date date_start;
    public Date date_end;
    @JamResultMap(type=String.class, defaultKey = "en")
    public Map<String, String> text;
    public String type; // fixme: probably an enum
    public Integer joinid;
    @JamResultMap(type=String.class, defaultKey = "en")
    public Map<String, String> subtitle;
    public String target; // fixme: enum ?
    @JamResultMap(type=String.class, defaultKey = "size_unknown")
    public Map<String, String> images;

}
