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
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;

import info.lusito.jamendo.android.utils.JLog;
import info.lusito.jamendo.api.results.radio.RadioStream;
import info.lusito.jamendo.api.results.track.Track;

public class MusicPlayer implements OnSeekCompleteListener, OnCompletionListener, OnBufferingUpdateListener, OnErrorListener {
    private static final long FAIL_TIME_FRAME = 1000;
    private static final int ACCEPTABLE_FAIL_NUMBER = 2;
    private long lastErrorTime = 0;
    private long errorCount = 0;
    private MusicType musicType;

    private final TrackMediaPlayer []mediaPlayers = {TrackMediaPlayer.create(), TrackMediaPlayer.create()};
    private TrackMediaPlayer currentMediaPlayer;

    private Object listenerSync = new Object();
    private MusicPlayerListener listener;

    public MusicPlayer() {
        for(TrackMediaPlayer player: mediaPlayers) {
            player.setOnCompletionListener(this);
            player.setOnBufferingUpdateListener(this);
            player.setOnErrorListener(this);
        }
    }

    public void setListener(MusicPlayerListener newListener) {
        synchronized (listenerSync) {
            this.listener = newListener;
        }
    }

    private interface ListenerRunnable {
        void run(MusicPlayerListener listener);
    }

    private void runOnListener(ListenerRunnable runnable) {
        if(musicType == MusicType.TRACK) {
            synchronized (listenerSync) {
                if (listener != null)
                    runnable.run(listener);
            }
        }
    }

    public void setVolume(float volume) {
        for(TrackMediaPlayer player: mediaPlayers)
            player.setVolume(volume, volume);
    }

    public int getCurrentPosition() {
        synchronized (mediaPlayers) {
            if (currentMediaPlayer != null && currentMediaPlayer.isPlaying())
                return currentMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public boolean isPlaying() {
        synchronized (mediaPlayers) {
            return currentMediaPlayer != null && !currentMediaPlayer.isPreparing() && currentMediaPlayer.isPlaying();
        }
    }

    public void togglePause() {
        synchronized (mediaPlayers) {
            if (currentMediaPlayer != null) {
                if(currentMediaPlayer.isPaused())
                    currentMediaPlayer.start();
                else
                    currentMediaPlayer.pause();

                runOnListener((MusicPlayerListener l)->l.onTrackPauseToggle(currentMediaPlayer.getTrack(), currentMediaPlayer.isPaused()));
            }
        }
    }

    public void stop() {
        currentMediaPlayer = null;
        synchronized (mediaPlayers) {
            for(TrackMediaPlayer player: mediaPlayers)
                player.stop();
        }

        runOnListener((MusicPlayerListener l)->l.onPlaybackStop());
        musicType = null;
    }

    private void setMusicType(MusicType newType) {
        if(musicType != newType) {
            stop();
            musicType = newType;
        }
    }

    public void playTrack(Track track, Track nextTrack) {
        setMusicType(MusicType.TRACK);

        synchronized (mediaPlayers) {
            if (currentMediaPlayer == null) {
                currentMediaPlayer = setupPlayer(track);
            } else if(currentMediaPlayer.getTrack() != track) {
                for(TrackMediaPlayer player: mediaPlayers)
                    player.stop();
                currentMediaPlayer = setupPlayer(track);
            }

            if(currentMediaPlayer != null) {
                currentMediaPlayer.start();
                runOnListener((MusicPlayerListener l)->l.onTrackStart(track));
                if(nextTrack != null) {
                    TrackMediaPlayer nextPlayer = currentMediaPlayer.getNext();
                    if (nextPlayer == null || nextPlayer.getTrack() != nextTrack)
                        currentMediaPlayer.setNext(setupPlayer(nextTrack));
                } else {
                    currentMediaPlayer.setNext(null);
                }
            }
        }
    }

    public void playRadio(RadioStream stream) {
        setMusicType(MusicType.RADIO);

        Track track = new Track();
        track.audio = stream.stream;
        track.name = stream.playingnow.track_name;
        track.album_id = stream.playingnow.album_id;
        track.artist_id = stream.playingnow.artist_id;
        track.id = stream.playingnow.track_id;
        track.album_image = stream.playingnow.track_image;

        synchronized (mediaPlayers) {
            if (currentMediaPlayer == null) {
                currentMediaPlayer = setupPlayer(track);
            } else if(currentMediaPlayer.getTrack().audio.equals(stream.stream)) {
                currentMediaPlayer.setTrack(track);
            } else {
                for(TrackMediaPlayer player: mediaPlayers)
                    player.stop();
                currentMediaPlayer = setupPlayer(track);
            }

            if(currentMediaPlayer != null) {
                currentMediaPlayer.setNext(null);
                currentMediaPlayer.start();
            }
        }
    }

    private TrackMediaPlayer setupPlayer(Track track) {
        String path = track.audio;
        //fixme: check if the file is already downloaded

        try {
            synchronized (mediaPlayers) {
                TrackMediaPlayer player = currentMediaPlayer == mediaPlayers[0] ? mediaPlayers[1] : mediaPlayers[0];
                player.setNext(null);
                player.playTrack(track);
                return player;
            }
        } catch (Exception e) {
            JLog.e("playTrack: " + path, e);
            streamError(track, e.toString());
        }
        return null;
    }

    private void streamError(Track track, String message) {
        runOnListener((MusicPlayerListener l)->l.onStreamError(track, message));
    }

    public void onDestroy() {
        synchronized (mediaPlayers) {
            for(TrackMediaPlayer player: mediaPlayers) {
                player.stop();
                player.release();
            }
        }
    }
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        runOnListener((MusicPlayerListener l)->l.onTrackSeekComplete(((TrackMediaPlayer)mp).getTrack()));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        runOnListener((MusicPlayerListener l)->l.onTrackBuffering(((TrackMediaPlayer)mp).getTrack(), percent));
    }

    @Override
    public void onCompletion(MediaPlayer _mp) {
        TrackMediaPlayer mp = (TrackMediaPlayer)_mp;
        currentMediaPlayer = mp.getNext();
        runOnListener((MusicPlayerListener l)->l.onTrackComplete(mp.getTrack()));
    }

    @Override
    public boolean onError(MediaPlayer _mp, int what, int extra) {
        TrackMediaPlayer mp = (TrackMediaPlayer)_mp;

        if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            // we probably lack network
            streamError(mp.getTrack(), "Unknown error, possibly bad network connection");
            stop();
            return true;
        }

        // not sure what error code -1 exactly stands for but it causes player to start to jump songs
        // if there are more than 5 jumps without playback during 1 second then we abort
        // further playback
        if (what == -1) {
            long failTime = System.currentTimeMillis();
            if (failTime - lastErrorTime > FAIL_TIME_FRAME) {
                // outside time frame
                errorCount = 1;
                lastErrorTime = failTime;
            } else {
                // inside time frame
                errorCount++;
                if (errorCount > ACCEPTABLE_FAIL_NUMBER) {
                    streamError(mp.getTrack(), "Too many errors");
                    stop();
                    errorCount = 0;
                    return true;
                }
            }
        }
        return false;
    }
}
