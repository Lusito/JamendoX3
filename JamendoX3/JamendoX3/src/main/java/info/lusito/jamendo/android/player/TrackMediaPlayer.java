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

package info.lusito.jamendo.android.player;

import android.media.MediaPlayer;

import java.io.IOException;

import info.lusito.jamendo.api.results.track.Track;

public class TrackMediaPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {
    private Track track;
    private boolean preparing = false;
    private boolean startAfterPrepare = false;
    protected TrackMediaPlayer next;
    private boolean paused;

    public static TrackMediaPlayer create() {
        // fixme: setNextMediaPlayer(null) seems to crash even tho the version is 16
//        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN ? new TrackMediaPlayerCompat() : new TrackMediaPlayer();
        return new TrackMediaPlayerCompat();
    }

    private TrackMediaPlayer() {
        setOnPreparedListener(this);
    }

    public void setNext(TrackMediaPlayer next) {
        this.next = next;
        super.setNextMediaPlayer(next);
    }

    public TrackMediaPlayer getNext() {
        return next;
    }

    public boolean isPreparing() {
        return preparing;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        preparing = false;

        if (startAfterPrepare) {
            startAfterPrepare = false;
            start();
        }
    }

    public void playTrack(Track track) throws IOException {
        reset();
        this.track = track;
        setDataSource(track.audio);
        preparing = true;
        prepareAsync();
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
    @Override
    public void reset() {
        preparing = false;
        paused = false;
        next = null;
        track = null;
        startAfterPrepare = false;
        super.reset();
    }

    @Override
    public void start() {
        if (isPreparing())
            startAfterPrepare = true;
        else if (!isPlaying())
            super.start();
        paused = false;
    }

    @Override
    public void pause() {
        if (isPreparing())
            startAfterPrepare = false;
        else if (isPlaying())
            super.pause();
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void stop() {
        try {
            if(isPlaying())
                super.stop();
        } catch (Exception e) {
        }
        reset();
    }


    private static class TrackMediaPlayerCompat extends TrackMediaPlayer implements MediaPlayer.OnCompletionListener {
        private OnCompletionListener completionListener;

        TrackMediaPlayerCompat() {
            super.setOnCompletionListener(this);
        }

        @Override
        public void setNext(TrackMediaPlayer next) {
            this.next = next;
        }

        @Override
        public void setOnCompletionListener(OnCompletionListener listener) {
            completionListener = listener;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (next != null)
                next.start();
            if(completionListener != null)
                completionListener.onCompletion(this);
        }
    }

}
