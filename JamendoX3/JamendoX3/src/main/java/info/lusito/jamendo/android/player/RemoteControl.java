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
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;

/**
 * Controls the RemoteControlClient for the media server (lock screen controls).
 *
 * @author crazywater
 *
 */
public class RemoteControl {
    private RemoteControlClient remoteControlClient;

    /**
     * Register the remote control at the audio manager.
     */
    public void register(Context context, AudioManager audioManager) {
        if (remoteControlClient == null) {
            ComponentName myEventReceiver = new ComponentName(
                    context.getPackageName(), "RemoteControl");
            audioManager.registerMediaButtonEventReceiver(myEventReceiver);
// build the PendingIntent for the remote control client
            Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            mediaButtonIntent.setComponent(myEventReceiver);
// create and register the remote control client
            PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(context, 0,
                    mediaButtonIntent, 0);
            remoteControlClient = new RemoteControlClient(mediaPendingIntent);
            remoteControlClient
                    .setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                            | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
                            | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS);
            audioManager.registerRemoteControlClient(remoteControlClient);
        }
    }

    /**
     * Update the state of the remote control.
     */
    public void updateState(boolean isPlaying) {
        if (remoteControlClient != null) {
            if (isPlaying) {
                remoteControlClient
                        .setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            } else {
                remoteControlClient
                        .setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
            }
        }
    }

    /**
     * Updates the state of the remote control to "stopped".
     */
    public void stop() {
        if (remoteControlClient != null) {
            remoteControlClient
                    .setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
        }
    }

    /**
     * Set the metadata of this episode according to the episode.
     *//*
    public void updateMetadata(DBEpisode episode, long duration) {
        if (remoteControlClient != null) {
            MetadataEditor editor = remoteControlClient.editMetadata(true);
            DBFeed feed = episode.getFeed();

            ImageCache imageCache = App.get().getImageCache();
            String imgUrl = episode.getImgUrl();
            BitmapDrawable episodeIcon = imageCache.getResource(imgUrl);
            if (episodeIcon.equals(imageCache.getDefaultIcon())) {
                episodeIcon = imageCache.getResource(feed.getImgUrl());
            }
            editor.putBitmap(MetadataEditor.BITMAP_KEY_ARTWORK,
                    episodeIcon.getBitmap());

            editor.putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, duration);

            editor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST,
                    feed.getTitle());
            editor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE,
                    episode.getTitle());
            editor.apply();
        }
    }*/

    /**
     * Release the remote control.
     */
    public void release() {
        remoteControlClient = null;
    }
}