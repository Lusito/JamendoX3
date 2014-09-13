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

import android.view.View;

enum SearchOrder {
    BUZZ_RATE("Buzz Rate"),
    POPULARITY("Popularity"),
    DOWNLOADS("Downloads"),
    LISTENS("Listens"),
    NAME("Name"),
    DATE("Date");

    private final String description;
    private SpinnerArrayAdapter timeframeAdapter;
    private int directionVisibility = View.GONE;

    private SearchOrder(String description) {
        this.description = description;
    }

    SpinnerArrayAdapter getTimeframeAdapter() {
        return timeframeAdapter;
    }

    void setTimeframeAdapter(SpinnerArrayAdapter timeframeAdapter) {
        this.timeframeAdapter = timeframeAdapter;
    }

    public int getDirectionVisibility() {
        return directionVisibility;
    }

    public void setDirectionVisibility(int value) {
        directionVisibility = value;
    }

    @Override
    public String toString() {
        return description;
    }
}
