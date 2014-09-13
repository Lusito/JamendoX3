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

package info.lusito.jamendo.android.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.adapter.PlaylistAdapter;
import info.lusito.jamendo.android.fragments.DiscoverFragment;
import info.lusito.jamendo.android.fragments.JamFragment;
import info.lusito.jamendo.android.fragments.RadioFragment;
import info.lusito.jamendo.android.fragments.StartFragment;
import info.lusito.jamendo.android.player.MusicPlayerService;
import info.lusito.jamendo.android.utils.JLog;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.utils.ToastUtil;
import info.lusito.jamendo.android.fragments.AbstractTabFragment;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropListView;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropListener;
import info.lusito.jamendo.android.widgets.JamDrawerLayout;
import info.lusito.jamendo.android.widgets.PlaylistEditor;
import info.lusito.jamendo.api.results.radio.RadioStream;
import info.lusito.jamendo.api.results.track.Track;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, DragDropListener, ServiceConnection {

    private MenuItem share;
    private MenuItem mute;
    private MenuItem stop;
    private MenuItem playlist;

    private boolean muted;
    private List<AbstractTabFragment> tabFragments = new ArrayList<AbstractTabFragment>();
    private boolean disableTabs;
    private SharedPreferences sharedPrefs;
    private static MainActivity instance;
    private JamFragment currentFragment;
    private JamDrawerLayout drawerLayout;
    private PlaylistEditor playlistEditor;

    MusicPlayerService musicPlayerService;
    private HashSet<JamendoServiceConnectionListener> jamendoServiceConnectionListeners = new HashSet<JamendoServiceConnectionListener>();

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        disableTabs = true;
        sharedPrefs = this.getSharedPreferences("info.lusito.jamendo.android", MODE_PRIVATE);

        ToastUtil.init(this);

        setContentView(R.layout.activity_main);
        drawerLayout = (JamDrawerLayout) findViewById(R.id.drawer_layout);

        playlistEditor = (PlaylistEditor)findViewById(R.id.playlist_drawer);

        setupActionBar(savedInstanceState);

        bindService(new Intent(this, MusicPlayerService.class), this, BIND_AUTO_CREATE);
    }

    private void setupActionBar(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        addTab(actionBar, DiscoverFragment.class);
//        addTab(actionBar, PlayFragment.class);
        addTab(actionBar, RadioFragment.class);

        setHasEmbeddedTabs(actionBar);

        if(savedInstanceState == null) {
            int tab = sharedPrefs.getInt("tab", -1);
            if(tab == -1) {
                deselectTab();
                showFragment(new StartFragment(), false);
            } else {
                disableTabs = false;
                getActionBar().setSelectedNavigationItem(tab);
            }
        } else {
            int tab = savedInstanceState.getInt("tab");
            if(tab == -1)
                deselectTab();
            else
                getActionBar().setSelectedNavigationItem(tab);
        }
        disableTabs = false;
    }

    public void showFragment(JamFragment fragment, boolean addToHistory) {
        drawerLayout.closeDrawers();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(!addToHistory) {
            DiscoverFragment search = (DiscoverFragment)getFragmentManager().findFragmentByTag(DiscoverFragment.class.getName());
            if(search != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getEdit().getWindowToken(), 0);
            }
            transaction.replace(R.id.content_frame, fragment, fragment.getClass().getName());
        }
        else {
            transaction.replace(R.id.content_frame, fragment);
            deselectTab();
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed () {
        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void deselectTab() {
        try {
            getActionBar().selectTab(null);
        } catch (Exception e) {}
    }

    private void addTab(ActionBar actionBar, Class<? extends AbstractTabFragment> clazz) {
        try {
            AbstractTabFragment fragment = (AbstractTabFragment)getFragmentManager().findFragmentByTag(clazz.getName());
            if(fragment == null) {
                fragment = clazz.newInstance();
                Bundle arguments = new Bundle();
                arguments.putInt("tab", actionBar.getNavigationItemCount());
                fragment.setArguments(arguments);
            }

            ActionBar.Tab tab = actionBar.newTab()
                    .setText(fragment.getTitle())
                    .setTabListener(this).setTag(fragment);
            actionBar.addTab(tab);
            tabFragments.add(fragment);
        } catch (InstantiationException e) {
            JLog.wtf("addTab", e);
        } catch (IllegalAccessException e) {
            JLog.wtf("addTab", e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    private void setHasEmbeddedTabs(Object actionBar) {
        try {
            Method setHasEmbeddedTabsMethod = actionBar.getClass().getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            setHasEmbeddedTabsMethod.setAccessible(true);
            setHasEmbeddedTabsMethod.invoke(actionBar, true);
        }
        catch (Exception e) {
            JLog.wtf("setHasEmbeddedTabs", e);
        }
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
        musicPlayerService = binder.getService();

        DragDropListView listView = (DragDropListView) playlistEditor.findViewById(R.id.playlist);
        musicPlayerService.playlist.setFirstListView(listView);
        listView.setDragDropListener(this);

        for(JamendoServiceConnectionListener listener: jamendoServiceConnectionListeners)
            listener.onMusicPlayerServiceConnected(musicPlayerService);
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        musicPlayerService = null;
    }

    public void setVolume(float volume) {
        if (musicPlayerService != null)
            musicPlayerService.setVolume(volume);
    }

    public void playRadio(RadioStream stream) {
        if (musicPlayerService != null)
            musicPlayerService.playRadio(stream);
    }

    private MenuItem addMenuItem(Menu menu, String text, int icon) {
        MenuItem item = menu.add(text).setIcon(icon);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        share = addMenuItem(menu, "Share", android.R.drawable.ic_menu_share);
        mute = addMenuItem(menu, "Mute", R.drawable.ic_audio_vol);
        stop = addMenuItem(menu, "Stop", R.drawable.ic_media_stop);
        playlist = addMenuItem(menu, "Playlist", R.drawable.ic_drawer);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == mute) {
            muted = !muted;
            mute.setIcon( muted ? R.drawable.ic_audio_vol_mute : R.drawable.ic_audio_vol);
            if(muted)
                setVolume(0);
            else
                setVolume(1);
        }
        else if(item == stop) {
            musicPlayerService.playlist.stop();
        }
        else if(item == playlist) {
            if(drawerLayout.isDrawerOpen(playlistEditor))
                drawerLayout.closeDrawer(playlistEditor);
            else
                drawerLayout.openDrawer(playlistEditor);
        }
        else if(item == share) {
            ShareInfo info = currentFragment == null ? null : currentFragment.getShareInfo();
            if(info == null) {
                ToastUtil.showShort("Nothing to share in this area. Maybe wait for data to load ?");
            } else {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, info.getSubject());
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, info.getText());

                startActivity(Intent.createChooser(shareIntent, "Share \"" + info.getShortName() + "\" via.."));
            }
        }
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(!disableTabs) {
            FragmentManager fm = getFragmentManager();
            int size = fm.getBackStackEntryCount();
            for(int i=0; i<size; i++) {
                fm.popBackStackImmediate();
            }
            showFragment((AbstractTabFragment) tab.getTag(), false);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        onTabSelected(tab, fragmentTransaction);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public void quietlySelectTab(int position) {
        boolean last = disableTabs;
        disableTabs = true;
        getActionBar().setSelectedNavigationItem(position);
        sharedPrefs.edit().putInt("tab", position).commit();
        disableTabs = last;
    }

    public List<AbstractTabFragment> getTabFragments() {
        return tabFragments;
    }

    public SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }

    public void setCurrentFragment(JamFragment jamFragment) {
        currentFragment = jamFragment;
    }

    public JamFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void onDragStart() {
        lockDrawerOpen();
    }

    @Override
    public void onDragEnd() {
        unlockDrawer();
    }

    public void lockDrawerOpen() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, Gravity.END);
    }
    public void lockDrawerClosed() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
    }
    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.END);
    }

    public void addJamendoServiceConnectionListener(JamendoServiceConnectionListener listener) {
        jamendoServiceConnectionListeners.add(listener);
        if(musicPlayerService != null)
            listener.onMusicPlayerServiceConnected(musicPlayerService);
    }

    public static void addToPlaylist(Track track) {
        MusicPlayerService mps = getInstance().musicPlayerService;
        if(mps != null)
            mps.playlist.add(track);
    }

    public static void addToPlaylist(List<Track> tracks) {
        MusicPlayerService mps = getInstance().musicPlayerService;
        if(mps != null)
            mps.playlist.add(tracks);
    }

    public static void addToPlaylist(PlaylistAdapter.TrackQueryModifier modifier) {
        MusicPlayerService mps = getInstance().musicPlayerService;
        if(mps != null)
            mps.playlist.add(modifier);
    }

    public static void hideSoftInputFromWindow(IBinder windowToken, int flags) {
        InputMethodManager in = (InputMethodManager)getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(windowToken, flags);
    }
}