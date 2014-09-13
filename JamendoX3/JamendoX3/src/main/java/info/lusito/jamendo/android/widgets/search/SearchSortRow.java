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

package info.lusito.jamendo.android.widgets.search;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.api.enums.AlbumOrder;
import info.lusito.jamendo.api.enums.ArtistOrder;
import info.lusito.jamendo.api.enums.TrackOrder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SearchSortRow extends LinearLayout implements AdapterView.OnItemSelectedListener {
    private Spinner orders;
    private Spinner timeframes;
    private Spinner direction;
    private Spinner types;
    private ChangeListener changeListener;

    public SearchSortRow(Context context) {
        super(context);
        setup();
    }

    public SearchSortRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public SearchSortRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.widget_search_sort_row, this);

        orders = (Spinner) findViewById(R.id.orderSpinner);
        timeframes = (Spinner) findViewById(R.id.timeframeSpinner);
        direction = (Spinner) findViewById(R.id.ascdescSpinner);

        if (!isInEditMode()) {
            orders.setOnItemSelectedListener(this);
            timeframes.setOnItemSelectedListener(this);
            direction.setOnItemSelectedListener(this);

            SpinnerArrayAdapter timeframeAdapter = new SpinnerArrayAdapter(getContext(), SearchTimeframe.values());
            SearchOrder.POPULARITY.setTimeframeAdapter(timeframeAdapter);
            SearchOrder.DOWNLOADS.setTimeframeAdapter(timeframeAdapter);
            SearchOrder.LISTENS.setTimeframeAdapter(timeframeAdapter);

            SearchOrder.NAME.setDirectionVisibility(VISIBLE);
            SearchOrder.DATE.setDirectionVisibility(VISIBLE);

            SpinnerArrayAdapter tracksAdapter = new SpinnerArrayAdapter(getContext(), SearchOrder.values());
            SearchType.TRACKS.setOrderAdapter(tracksAdapter);
//            SearchType.TAGS.setOrderAdapter(tracksAdapter);
//            SearchType.FUZZY_TAGS.setOrderAdapter(tracksAdapter);
            SpinnerArrayAdapter nameDatePopularityAdapter = new SpinnerArrayAdapter(getContext(), SearchOrder.POPULARITY, SearchOrder.NAME, SearchOrder.DATE);
            SearchType.ARTISTS.setOrderAdapter(nameDatePopularityAdapter);
            SearchType.ALBUMS.setOrderAdapter(nameDatePopularityAdapter);
//            SpinnerArrayAdapter nameDateAdapter = new SpinnerArrayAdapter(getContext(), SearchOrder.NAME, SearchOrder.DATE);
//            SearchType.PLAYLISTS.setOrderAdapter(nameDateAdapter);

            direction.setAdapter(new SpinnerArrayAdapter(getContext(), SearchDirection.values()));
        }
    }

    public void setTypes(Spinner types) {
        this.types = types;
        updateSpinnerContent(types);
    }

    private void updateSpinnerContent(AdapterView<?> adapterView) {
        SearchType type = (SearchType) types.getSelectedItem();

        if (adapterView != orders) {
            SpinnerArrayAdapter orderAdapter = type.getOrderAdapter();
            if (orders.getAdapter() != orderAdapter) {
                orders.setAdapter(orderAdapter);
            }

            SearchOrder lastOrder = type.getLastOrder();
            if (lastOrder != null) {
                int pos = orderAdapter.getPosition(lastOrder);
                orders.setSelection(pos == -1 ? 0 : pos);
            } else {
                orders.setSelection(0);
            }
        }

        SearchOrder order = (SearchOrder) orders.getSelectedItem();

        if (adapterView != timeframes) {
            SpinnerArrayAdapter timeframeAdapter = order.getTimeframeAdapter();
            if (timeframes.getAdapter() != timeframeAdapter) {
                timeframes.setAdapter(timeframeAdapter);
                timeframes.setVisibility(timeframeAdapter == null ? View.GONE : View.VISIBLE);
            }

            SearchTimeframe lastTimeframe = type.getLastTimeFrame();
            if (lastTimeframe != null && timeframeAdapter != null) {
                timeframes.setSelection(timeframeAdapter.getPosition(lastTimeframe));
            }
        }

        if (adapterView != direction) {
            direction.setVisibility(order.getDirectionVisibility());
        }

        type.setLastOrder(order);
        type.setLastTimeFrame((SearchTimeframe) timeframes.getSelectedItem());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        updateSpinnerContent(adapterView);
        if(changeListener != null)
            changeListener.onChange();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public TrackOrder getTrackOrder() {
        SearchOrder order = (SearchOrder) orders.getSelectedItem();
        switch (order) {
            case BUZZ_RATE:
                return TrackOrder.BUZZRATE;
            case POPULARITY:
                switch((SearchTimeframe)timeframes.getSelectedItem()) {
                    case TOTAL:
                        return TrackOrder.POPULARITY_TOTAL;
                    case MONTH:
                        return TrackOrder.POPULARITY_MONTH;
                    case WEEK:
                        return TrackOrder.POPULARITY_WEEK;
                }
                break;
            case DOWNLOADS:
                switch((SearchTimeframe)timeframes.getSelectedItem()) {
                    case TOTAL:
                        return TrackOrder.DOWNLOADS_TOTAL;
                    case MONTH:
                        return TrackOrder.DOWNLOADS_MONTH;
                    case WEEK:
                        return TrackOrder.DOWNLOADS_WEEK;
                }
                break;
            case LISTENS:
                switch((SearchTimeframe)timeframes.getSelectedItem()) {
                    case TOTAL:
                        return TrackOrder.LISTENS_TOTAL;
                    case MONTH:
                        return TrackOrder.LISTENS_MONTH;
                    case WEEK:
                        return TrackOrder.LISTENS_WEEK;
                }
                break;
            case NAME:
                switch((SearchDirection)direction.getSelectedItem()) {
                    case ASC:
                        return TrackOrder.NAME;
                    case DESC:
                        return TrackOrder.NAME_DESC;
                }
                break;
            case DATE:
                switch((SearchDirection)direction.getSelectedItem()) {
                    case ASC:
                        return TrackOrder.RELEASEDATE;
                    case DESC:
                        return TrackOrder.RELEASEDATE_DESC;
                }
                break;
        }
        return null;
    }

    public ArtistOrder getArtistOrder() {
        SearchOrder order = (SearchOrder) orders.getSelectedItem();
        switch (order) {
            case POPULARITY:
                switch((SearchTimeframe)timeframes.getSelectedItem()) {
                    case TOTAL:
                        return ArtistOrder.POPULARITY_TOTAL;
                    case MONTH:
                        return ArtistOrder.POPULARITY_MONTH;
                    case WEEK:
                        return ArtistOrder.POPULARITY_WEEK;
                }
                break;
            case NAME:
                switch((SearchDirection)direction.getSelectedItem()) {
                    case ASC:
                        return ArtistOrder.NAME;
                    case DESC:
                        return ArtistOrder.NAME_DESC;
                }
                break;
            case DATE:
                switch((SearchDirection)direction.getSelectedItem()) {
                    case ASC:
                        return ArtistOrder.JOINDATE;
                    case DESC:
                        return ArtistOrder.JOINDATE_DESC;
                }
                break;
        }
        return null;
    }

    public AlbumOrder getAlbumOrder() {
        SearchOrder order = (SearchOrder) orders.getSelectedItem();
        switch (order) {
            case POPULARITY:
                switch((SearchTimeframe)timeframes.getSelectedItem()) {
                    case TOTAL:
                        return AlbumOrder.POPULARITY_TOTAL;
                    case MONTH:
                        return AlbumOrder.POPULARITY_MONTH;
                    case WEEK:
                        return AlbumOrder.POPULARITY_WEEK;
                }
                break;
            case NAME:
                switch((SearchDirection)direction.getSelectedItem()) {
                    case ASC:
                        return AlbumOrder.NAME;
                    case DESC:
                        return AlbumOrder.NAME_DESC;
                }
                break;
            case DATE:
                switch((SearchDirection)direction.getSelectedItem()) {
                    case ASC:
                        return AlbumOrder.RELEASEDATE;
                    case DESC:
                        return AlbumOrder.RELEASEDATE_DESC;
                }
                break;
        }
        return null;
    }

    public void setChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
