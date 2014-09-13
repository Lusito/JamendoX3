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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.adapter.PlaylistAdapter;
import info.lusito.jamendo.api.results.radio.RadioStream;
import info.lusito.jamendo.api.results.track.Track;

public class MusicPlayerService extends Service {
    private final MusicPlayer musicPlayer = new MusicPlayer();
    private final IBinder mBinder = new LocalBinder();

    private NotificationManager nm;
    private static final int NOTIFY_ID = 1234;//fixme: ;R.layout.songlist;
    private PowerManager.WakeLock wakeLock;

    public final PlaylistAdapter playlist = new PlaylistAdapter();

    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PowerManager mgr = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "JamWakeLock");
        playlist.setService(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        musicPlayer.onDestroy();
        stopForeground(true);
//        nm.cancel(NOTIFY_ID);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void showNotification(String title, String text, int icon) {
        Notification notification = new Notification.Builder(this.getApplicationContext())
                .setContentTitle(title)
//                .setContentText(text)
                .setSmallIcon(icon)
//                .setTicker(title)
                .getNotification();
        startForeground(NOTIFY_ID, notification);
    }

    private void stayAwake() {
        wakeLock.acquire();
    }

    private void allowSleep() {
        wakeLock.release();
    }

    public void setListener(MusicPlayerListener listener) {
        musicPlayer.setListener(listener);
    }

    public void setVolume(float volume) {
        musicPlayer.setVolume(volume);
    }

    public void playTrack(Track track, Track nextTrack) {
        showNotification(track.name, "", R.drawable.playbackstart);
        musicPlayer.playTrack(track, nextTrack);
    }

    public void playRadio(RadioStream stream) {
        showNotification(stream.playingnow.track_name, "", R.drawable.playbackstart); // fixme: icon for radio
        musicPlayer.playRadio(stream);
    }

    public void togglePause() {
        showNotification("", "", R.drawable.playbackpause);
        musicPlayer.togglePause();
    }

    public void stop() {
        stopForeground(true);
//            nm.cancel(NOTIFY_ID);
        musicPlayer.stop();
    }
}
