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

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.widgets.dragdrop.DragDropListView;

/**
 * fixme: remember list positions
 */
public class JamFragment extends Fragment {
    private final LinkedList<ListView> storedListViews = new LinkedList<ListView>();
    private int lastPosition;
    private int lastTop;

    public ShareInfo getShareInfo() {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.getInstance().setCurrentFragment(this);
/*
        if(savedInstanceState != null) {
            int index = 0;
            for(ListView listView: storedListViews) {
                lastPosition = savedInstanceState.getInt("listpos_index_" + index);
                lastTop = savedInstanceState.getInt("listpos_top_" + index);
                if (lastPosition != AdapterView.INVALID_POSITION) {
                    if(lastTop == AdapterView.INVALID_POSITION)
                        lastTop = 0;
                    listView.setSelectionFromTop(lastPosition, lastTop);
                }
            }
        }*/
    }

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int index = 0;
        for(ListView listView: storedListViews) {
            lastPosition = listView.getSelectedItemPosition();
            int lastPositionInGroup = lastPosition - listView.getFirstVisiblePosition();
            lastTop = listView.getChildAt( lastPositionInGroup ).getTop();

            outState.putInt("listpos_index_" + index, lastPosition);
            outState.putInt("listpos_top_" + index, lastTop);
            index++;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for(ListView listView: storedListViews) {
            if (lastPosition != AdapterView.INVALID_POSITION) {
                if(lastTop == AdapterView.INVALID_POSITION)
                    lastTop = 0;
                listView.setSelectionFromTop(lastPosition, lastTop);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        int index = 0;
        for(ListView listView: storedListViews) {
            lastPosition = listView.getSelectedItemPosition();
            int lastPositionInGroup = lastPosition - listView.getFirstVisiblePosition();
            View view = listView.getChildAt( lastPositionInGroup );
            if(view != null)
                lastTop = view.getTop();
            else
                lastTop = 0;
        }
    }
*/
    protected final void addStoredList(ListView listView) {
        storedListViews.add(listView);
    }

    public DragDropListView getPlaylistView() {
        return null;
    }
}
