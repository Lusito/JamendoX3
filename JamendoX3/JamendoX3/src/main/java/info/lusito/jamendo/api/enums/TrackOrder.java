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

package info.lusito.jamendo.api.enums;

public enum TrackOrder {

    BUZZRATE,
    DOWNLOADS_WEEK,
    DOWNLOADS_WEEK_DESC,
    DOWNLOADS_MONTH,
    DOWNLOADS_MONTH_DESC,
    DOWNLOADS_TOTAL,
    DOWNLOADS_TOTAL_DESC,
    LISTENS_WEEK,
    LISTENS_WEEK_DESC,
    LISTENS_MONTH,
    LISTENS_MONTH_DESC,
    LISTENS_TOTAL,
    LISTENS_TOTAL_DESC,
    POPULARITY_WEEK,
    POPULARITY_MONTH,
    POPULARITY_TOTAL,
    NAME,
    NAME_DESC,
    ALBUM_NAME,
    ALBUM_NAME_DESC,
    ARTIST_NAME,
    ARTIST_NAME_DESC,
    RELEASEDATE,
    RELEASEDATE_DESC,
    DURATION,
    DURATION_DESC,
    ID,
    ID_DESC;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
