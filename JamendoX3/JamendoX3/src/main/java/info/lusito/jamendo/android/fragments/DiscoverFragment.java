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

package info.lusito.jamendo.android.fragments;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.activities.JamendoServiceConnectionListener;
import info.lusito.jamendo.android.adapter.AlbumQueryAdapter;
import info.lusito.jamendo.android.adapter.ArtistQueryAdapter;
import info.lusito.jamendo.android.adapter.TrackQueryAdapter;
import info.lusito.jamendo.android.player.MusicPlayerService;
import info.lusito.jamendo.android.utils.SimpleHandler;
import info.lusito.jamendo.android.widgets.QueryListView;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropListView;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropListener;
import info.lusito.jamendo.android.widgets.search.SearchRow;
import info.lusito.jamendo.android.widgets.search.SearchSortRow;
import info.lusito.jamendo.api.enums.AlbumOrder;
import info.lusito.jamendo.api.enums.ArtistOrder;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.TrackOrder;
import info.lusito.jamendo.api.query.album.AlbumQuery;
import info.lusito.jamendo.api.query.artist.ArtistMusicInfoQuery;
import info.lusito.jamendo.api.query.track.TrackQuery;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

public class DiscoverFragment extends AbstractTabFragment implements DragDropListener, TextView.OnEditorActionListener, JamendoServiceConnectionListener {

    private static final int RESULT_LIMIT = 50;
    private static final ImageSize IMAGE_SIZE = ImageSize._35;
    private SearchRow searchRow;
    private SearchSortRow searchSortRow;
    private DragDropListView playlistView;
    private ImageButton startSearch;
    private TrackQueryAdapter trackQueryAdapter;
    private ArtistQueryAdapter artistQueryAdapter;
    private AlbumQueryAdapter albumQueryAdapter;
    private QueryListView searchResults;
    private SimpleHandler runSearchDelayed = new SimpleHandler(()->runSearch());

    @Override
    public String getTitle() {
        return "Discover";
    }

    @Override
    public int getImageResource() {
        return R.drawable.start_discover;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_discover, container, false);
        searchRow = (SearchRow) layout.findViewById(R.id.searchRow);
        searchSortRow = (SearchSortRow) layout.findViewById(R.id.searchSortRow);

        searchRow.initAdapter();
        searchRow.setSortRow(searchSortRow);
        searchRow.getEdit().setOnEditorActionListener(this);
        startSearch = (ImageButton) layout.findViewById(R.id.startSearch);
        startSearch.setOnClickListener((View v)-> runSearchDelayed.restart(0));

        // result list
        searchResults = (QueryListView) layout.findViewById(R.id.searchResults);
        trackQueryAdapter = new TrackQueryAdapter();
        artistQueryAdapter = new ArtistQueryAdapter();
        albumQueryAdapter = new AlbumQueryAdapter();

        playlistView = (DragDropListView) layout.findViewById(R.id.playlist);

        MainActivity.getInstance().addJamendoServiceConnectionListener(this);
        return layout;
    }

    @Override
    public void onMusicPlayerServiceConnected(MusicPlayerService service) {
        service.playlist.setSecondListView(playlistView);
    }

    @Override
    public void onStart() {
        super.onStart();
        searchSortRow.setChangeListener(()->runSearchDelayed.restart(200));
        runSearchDelayed.restart(200);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            runSearchDelayed.restart(0);
            return true;
        }
        return false;
    }

    public void runSearch() {
        if(MainActivity.getInstance().getCurrentFragment() != this)
            return;

        switch(searchRow.getSearchType()) {
            case TRACKS: {
                trackQueryAdapter.setListView(searchResults);
                TrackQuery query = new TrackQuery().namesearch(searchRow.getText()).limit(RESULT_LIMIT).imagesize(IMAGE_SIZE);
                TrackOrder order = searchSortRow.getTrackOrder();
                if(order != null)
                    query.order(order);
                trackQueryAdapter.query(query);
                break;
            }
            case ARTISTS: {
                artistQueryAdapter.setListView(searchResults);
                ArtistMusicInfoQuery query = new ArtistMusicInfoQuery().namesearch(searchRow.getText()).limit(RESULT_LIMIT);
                ArtistOrder order = searchSortRow.getArtistOrder();
                if(order != null)
                    query.order(order);
                artistQueryAdapter.query(query);
                break;
            }
            case ALBUMS: {
                albumQueryAdapter.setListView(searchResults);
                AlbumQuery query = new AlbumQuery().namesearch(searchRow.getText()).limit(RESULT_LIMIT).imagesize(IMAGE_SIZE);
                AlbumOrder order = searchSortRow.getAlbumOrder();
                if(order != null)
                    query.order(order);
                albumQueryAdapter.query(query);
                break;
            }
        }
        searchRow.getEdit().clearFocus();
        MainActivity.hideSoftInputFromWindow(searchRow.getEdit().getWindowToken(), 0);
    }

    public View getEdit() {
        return searchRow.getEdit();
    }

    @Override
    public void onDragStart() {
        MainActivity.getInstance().lockDrawerClosed();
    }

    @Override
    public void onDragEnd() {
        MainActivity.getInstance().unlockDrawer();
    }

    public DragDropListView getPlaylistView() {
        return playlistView;
    }
}