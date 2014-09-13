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

package info.lusito.jamendo.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.fragments.TrackInfoFragment;
import info.lusito.jamendo.android.player.MusicPlayerListener;
import info.lusito.jamendo.android.player.MusicPlayerService;
import info.lusito.jamendo.android.player.PlaylistRemote;
import info.lusito.jamendo.android.utils.ExecuteQueryTask;
import info.lusito.jamendo.android.utils.TextUtils;
import info.lusito.jamendo.android.utils.ToastUtil;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropListView;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropSwapper;
import info.lusito.jamendo.android.widgets.RemoteImageView;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.query.track.TrackQuery;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.track.Track;

public class PlaylistAdapter extends AbstractAdapter<PlaylistAdapter.Entry> implements DragDropSwapper, PlaylistRemote, MusicPlayerListener {
    private boolean shuffle;
    private boolean repeating = true;
    private Entry selected;
    private final LinkedList<Entry> unordered = new LinkedList<Entry>();
    private long nextId;
    private static final ImageSize IMAGE_SIZE = ImageSize._35;
    private MusicPlayerService service;

    private ListView firstListView;
    private ListView secondListView;

    public static class Entry {
        public final Track track;
        public final long id;
        public Entry(Track track, long id) {
            this.track = track;
            this.id = id;
        }
    }

    private class ViewHolder extends AbstractViewHolder<Entry> {
        private final TextView title;
        private final TextView artistAlbum;
        private final TextView duration;
        private final RemoteImageView remoteImage;
        private boolean removed;

        ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            artistAlbum = (TextView) convertView.findViewById(R.id.artistAlbum);
            duration = (TextView) convertView.findViewById(R.id.duration);
            remoteImage = (RemoteImageView) convertView.findViewById(R.id.remoteImage);
            convertView.findViewById(R.id.addButton).setVisibility(View.GONE);
            Button playButton = (Button)convertView.findViewById(R.id.playButton);
            playButton.setVisibility(View.VISIBLE);
            playButton.setOnClickListener((View v)->selectPlayEntry(entry));
        }

        @Override
        public void onCreate(Entry entry) {
            removed = false;
            title.setText(entry.track.name);
            artistAlbum.setText(getArtistAlbum(entry.track));
            duration.setText(TextUtils.formatTime(entry.track.duration));
            remoteImage.setUrl(entry.track.album_image, R.drawable.unknown_album);
        }

        private String getArtistAlbum(Track entry) {
            String value = entry.artist_name;
            value += " / ";
            value += entry.album_name;
            return value;
        }

        @Override
        public void updateBackground(int position) {
            if(entry == selected)
                convertView.setBackgroundColor(0xFFCBE8FE);
            else
                super.updateBackground(position);
        }
    }

    public PlaylistAdapter() {
        setList(new LinkedList<PlaylistAdapter.Entry>());
    }

    public void setService(MusicPlayerService service) {
        this.service = service;
        service.setListener(this);
    }

    public long getNextId() {
        return nextId++;
    }

    public void setFirstListView(DragDropListView listView) {
        firstListView = listView;
        connectDragDropListView(listView);
    }

    public void setSecondListView(DragDropListView listView) {
        secondListView = listView;
        connectDragDropListView(listView);
    }

    private void connectDragDropListView(DragDropListView listView) {
        connectView(listView);
        listView.setSwapper(this);
        if(listView != null)
            updateSelection(listView);
    }

    @Override
    public void setList(LinkedList<Entry> entries) {
        unordered.clear();
        unordered.addAll(entries);
        super.setList(entries);
    }

    @Override
    protected AbstractViewHolder createViewHolder(LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.list_row_track, null));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder != null) {
            Track track = holder.entry.track;
            MainActivity.getInstance().showFragment(TrackInfoFragment.create(track.id, track.album_id, track.artist_id), true);
        }
    }

    private void selectPlayEntry(Entry entry) {
        selected = entry;
        if(firstListView != null)
            updateSelection(firstListView);
        if(secondListView != null)
            updateSelection(secondListView);

        play();
    }

    @Override
    public long getItemId(int position) {
        if(position < 0 || position >= entries.size())
            return -1;
        return entries.get(position).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void swapEntries(View a, View b) {
        ViewHolder holderA = (ViewHolder)a.getTag();
        ViewHolder holderB = (ViewHolder)b.getTag();
        Collections.swap(entries, entries.indexOf(holderA.entry), entries.indexOf(holderB.entry));
        notifyDataSetChanged();
        if(holderA.entry == selected || holderB.entry == selected )
            play(); // to update the next media player
        // fixme: store changes in db
    }

    @Override
    public void removeEntry(View view) {
        ViewHolder holder = (ViewHolder)view.getTag();
        Entry track = holder.entry;
        int selectedIndex = entries.indexOf(selected);

        entries.remove(track);
        if(track == selected) {
            // Is last ?
            if(selectedIndex == entries.size()) {
                if(isRepeating()) {
                    selectPlayEntry(entries.getFirst());
                } else {
                    selected = entries.getLast();
                    stop();
                }
            } else {
                selectPlayEntry(entries.get(selectedIndex));
            }
        } else {
            // selected is the next to be played, update next media player
            int index = entries.indexOf(track);
            if(selectedIndex+1 == index) {
                play(); // to update next media player
            }
        }

        unordered.remove(track);
        notifyDataSetChanged();
        // fixme: store changes in db
    }

    private void updateSelection(ListView listView) {
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        for (int i = first; i <= last; i++) {
            View convertView = listView.getChildAt(i - first);
            if (convertView != null) {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.updateBackground(i);
            }
        }
    }

    public void add(Track track) {
        Entry entry = new Entry(track, getNextId());
        entries.add(entry);
        unordered.add(entry);
        notifyDataSetChanged();
    }

    public void add(List<Track> tracks) {
        for(Track track: tracks) {
            Entry entry = new Entry(track, getNextId());
            entries.add(entry);
            unordered.add(entry);
        }
        notifyDataSetChanged();
    }

    public interface TrackQueryModifier {
        void modify(TrackQuery q);
    }

    public void add(TrackQueryModifier modifier) {
        TrackQuery query = new TrackQuery().limit(200).imagesize(IMAGE_SIZE);
        modifier.modify(query);
        ExecuteQueryTask.execute(
                (LinkedList<Track> result, Header header) -> {
                    if (result != null && !result.isEmpty()) {
                        ToastUtil.showShort(result.size() + " tracks have been added to the playlist");
                        add(result);
                    }
                },
                query, Track.class
        );
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int size() {
        return entries.size();
    }

    public Track getSelectedTrack() {
        return selected != null ? selected.track : null;
    }

    public boolean isLastTrackOnList() {
        return selected == entries.getLast();
    }

    private Entry getNextEntry() {
        if(selected == null)
            return null;
        int selectedIndex = entries.indexOf(selected) + 1;
        if(selectedIndex >= entries.size())
            return entries.getFirst();
        else
            return entries.get(selectedIndex);
    }

    @Override
    public void next() {
        if (!isEmpty())
            selectPlayEntry(getNextEntry());
    }

    @Override
    public void prev() {
        if (!isEmpty() && selected != null) {
            int selectedIndex = entries.indexOf(selected) - 1;
            if(selectedIndex < 0)
                selectPlayEntry(entries.getLast());
            else
                selectPlayEntry(entries.get(selectedIndex));
        }
    }

    @Override
    public void togglePause() {
        service.togglePause();
    }

    @Override
    public void play() {
        //fixme: no selection.., highlight
        if(selected != null)
            service.playTrack(selected.track, getNextEntry().track);
    }

    @Override
    public void stop() {
        service.stop();
    }

    @Override
    public void setShuffle(boolean value) {
        shuffle = value;
        if (!shuffle) {
            Collections.copy(entries, unordered);
        } else {
            if (entries.isEmpty()) {
                selected = null;
            } else {
                Collections.shuffle(entries);
            }
        }
    }

    @Override
    public void setRepeating(boolean value) {
        repeating = value;
    }

    public boolean isRepeating() {
        return repeating;
    }



    @Override
    public void onTrackStart(Track track) {
    }

    @Override
    public void onTrackComplete(Track track) {
        MainActivity.getInstance().runOnUiThread(()->next());
    }

    @Override
    public void onTrackSeekComplete(Track track) {
    }

    @Override
    public void onTrackBuffering(Track track, int percent) {
    }

    @Override
    public void onTrackPauseToggle(Track track, boolean paused) {
    }

    @Override
    public void onPlaybackStop() {
    }

    @Override
    public void onStreamError(Track track, String message) {
        ToastUtil.showLong("Error \"" + message + "\" on track: " + (track!=null ? track.name : "<unknown>"));
    }
}