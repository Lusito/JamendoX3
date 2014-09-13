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

package info.lusito.jamendo.api.query.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.lusito.jamendo.api.enums.TrackInclude;
import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.track.TrackSimilar;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.AudioFormat;
import info.lusito.jamendo.api.enums.ImageSize;

@JamQuery(value = "tracks/similar")
public class TrackSimilarQuery extends AbstractQueryList<TrackSimilar, TrackSimilarQuery> {

    protected ImageSize imagesize;
    protected AudioFormat audioformat;
    protected AudioFormat audiodlformat;
    protected List<TrackInclude> include;
    protected Integer id;
    protected Integer no_artist;
    protected Integer no_album;

    public TrackSimilarQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }

    public TrackSimilarQuery audioformat(AudioFormat value) {
        audioformat = value;
        return this;
    }

    public TrackSimilarQuery audiodlformat(AudioFormat value) {
        audiodlformat = value;
        return this;
    }

    public TrackSimilarQuery include(TrackInclude... values) {
        if (include == null) {
            include = new ArrayList<TrackInclude>();
        }
        include.addAll(Arrays.asList(values));
        return this;
    }

    public TrackSimilarQuery id(int value) {
        id = Integer.valueOf(value);
        return this;
    }

    public TrackSimilarQuery no_artist(int value) {
        no_artist = Integer.valueOf(value);
        return this;
    }

    public TrackSimilarQuery no_album(int value) {
        no_album = Integer.valueOf(value);
        return this;
    }
}
