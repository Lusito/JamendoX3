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

package info.lusito.jamendo.android.utils;

import info.lusito.jamendo.api.results.album.AlbumMusicInfo;
import info.lusito.jamendo.api.results.artist.ArtistMusicInfo;
import info.lusito.jamendo.api.results.radio.Radio;
import info.lusito.jamendo.api.results.radio.RadioStream;
import info.lusito.jamendo.api.results.track.Track;

public class ShareInfo {
    private String shortName;
    private String subject;
    private String text;
    private String url;

    public ShareInfo() {
        subject = "Jamendo X³ for Android";
        shortName = "Jamendo X³";
        url = "http://jamendo.lusito.info/";
        text = "Check out Jamendo X³ for Android: " + url;
    }

    public ShareInfo(Radio radio) {
        subject = shortName = radio.dispname;
        url = "http://www.jamendo.com/radios";
        updateText();
    }

    public ShareInfo(RadioStream radioStream, Track track) {
        subject = radioStream.playingnow.artist_name + ": " + radioStream.playingnow.track_name;
        shortName = radioStream.playingnow.track_name;
        if(track != null)
            url = track.shareurl;
        else
            url = "http://www.jamendo.com/tracks/" + radioStream.playingnow.track_id;
        updateText();
    }

    public ShareInfo(AlbumMusicInfo albumInfo) {
        subject = albumInfo.artist_name + ": " + albumInfo.name;
        shortName = albumInfo.name;
        url = albumInfo.shareurl;
        updateText();
    }

    public ShareInfo(ArtistMusicInfo artistInfo) {
        subject = shortName = artistInfo.name;
        url = artistInfo.shareurl;
        updateText();
    }

    public ShareInfo(Track track) {
        subject = track.artist_name + ": " + track.name;
        shortName = track.name;
        url = track.shareurl;
        updateText();
    }

    private void updateText() {
        text = String.format("I'm currently listening to \"%s\".\nCheck it out: %s", shortName, url);
    }

    public String getSubject() {
        return subject;
    }

    public String getShortName() {
        return shortName;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }
}
