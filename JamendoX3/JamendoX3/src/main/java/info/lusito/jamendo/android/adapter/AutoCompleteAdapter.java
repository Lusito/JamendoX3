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

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.utils.ExecuteQueryTask;
import info.lusito.jamendo.android.utils.QueryTaskListener;
import info.lusito.jamendo.android.widgets.search.SearchType;
import info.lusito.jamendo.api.enums.AutoCompleteEntity;
import info.lusito.jamendo.api.query.autocomplete.AutoCompleteQuery;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.autocomplete.AutoComplete;

import java.util.LinkedList;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutoCompleteAdapter extends BaseAdapter implements QueryTaskListener<AutoComplete>, Filterable, Runnable {
    private static final int LIMIT = 10;
    private static final long TYPE_DELAY = 1000;

    private LinkedList<AutoComplete.Match> results;
    private final AutoCompleteQuery query = new AutoCompleteQuery().limit(LIMIT);

    private final Handler delayHandler = new Handler();
    private long delayNextTime;
    private boolean delayRunning;
    private boolean abortSearch;

    private class ViewHolderItem {
        private final TextView title;

        public ViewHolderItem(View convertView) {
            title = (TextView) convertView.findViewById(R.id.title);
        }

        public void onCreate(AutoComplete.Match match) {
            title.setText(match.match);
        }
    }

    @Override
    public int getCount() {
        if (results == null)
            return 0;
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        if (results == null)
            return 0;
        return results.get(position).match;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_completion, null);
            holder = new ViewHolderItem(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderItem) convertView.getTag();
        }

        holder.onCreate(results.get(position));
        return convertView;
    }

    public boolean setResults(LinkedList<AutoComplete.Match> matches) {
        if (matches != null && !matches.isEmpty()) {
            results = matches;
            return true;
        }
        return false;
    }

    private void setResults(AutoComplete autoComplete) {
        boolean dummy = setResults(autoComplete.tracks) ||
                setResults(autoComplete.artists) ||
                setResults(autoComplete.albums) ||
                setResults(autoComplete.tags);

        notifyDataSetChanged();
    }

    @Override
    public void onResult(LinkedList<AutoComplete> result, Header header) {
        if (result != null && !result.isEmpty())
            setResults(result.get(0));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                search(charSequence.toString());
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            }
        };
    }

    @Override
    public void run() {
        synchronized (delayHandler) {
            long delay = delayNextTime - System.currentTimeMillis();
            if (abortSearch) {
                delayRunning = false;
                return;
            }
            if (delay > 10) {
                delayHandler.postDelayed(this, delay);
                return;
            }

            // Run the query
            ExecuteQueryTask.execute(this, query, AutoComplete.class);
            delayRunning = false;
        }
    }

    private void search(String prefix) {
        synchronized (delayHandler) {
            if (prefix.length() < 3) {
                abortSearch = true;
                return;
            }
            abortSearch = false;

            query.prefix(prefix);

            delayNextTime = System.currentTimeMillis() + TYPE_DELAY;
            if (!delayRunning) {
                delayHandler.postDelayed(this, TYPE_DELAY);
                delayRunning = true;
            }
        }
    }

    public void setType(SearchType type) {
        query.clearEntities();
        switch (type) {
            case TRACKS:
                query.entity(AutoCompleteEntity.TRACKS);
                break;
            case ARTISTS:
                query.entity(AutoCompleteEntity.ARTISTS);
                break;
            case ALBUMS:
                query.entity(AutoCompleteEntity.ALBUMS);
                break;/*
            case TAGS:
            case FUZZY_TAGS:
                query.entity(AutoCompleteEntity.TAGS);
                break;*/
        }
    }
}