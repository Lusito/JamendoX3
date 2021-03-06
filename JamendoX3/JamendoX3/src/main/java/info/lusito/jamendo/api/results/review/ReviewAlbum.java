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

package info.lusito.jamendo.api.results.review;

import info.lusito.jamendo.api.annotations.JamResultList;

import java.util.Date;
import java.util.LinkedList;

public class ReviewAlbum {

    public Integer id;
    public String title;
    public String text;
    public Date dateadded;
    public Integer agreecnt;
    public String lang;
    public Integer user_id;
    public String user_name;
    public String user_dispname;
    public Integer score;
    public Integer album_id;
    public String album_name;
    public Integer artist_id;
}
