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

import java.util.LinkedList;

import android.view.View;

import info.lusito.jamendo.android.utils.ExecuteQueryTask;
import info.lusito.jamendo.android.utils.QueryTaskListener;
import info.lusito.jamendo.android.utils.ToastUtil;
import info.lusito.jamendo.android.widgets.QueryListView;
import info.lusito.jamendo.api.query.AbstractQuery;
import info.lusito.jamendo.api.results.Header;

public abstract class AbstractQueryAdapter<QT extends AbstractQuery<RT, QT>, RT> extends AbstractAdapter<RT> implements QueryTaskListener<RT> {

    public static final int PRELOAD_ITEMS = 10;
    private final Class<RT> clazz;
    private QT query;
    private int offset;
    private boolean listComplete = true;
    private boolean loading;
    private QueryListView listView;

    public AbstractQueryAdapter(Class<RT> clazz) {
        this.clazz = clazz;
    }

    public void setListView(QueryListView listView) {
        this.listView = listView;
        listView.setAdapter(this);
    }

    public void query(QT query) {
        if(!entries.isEmpty()) {
            entries.clear();
            notifyDataSetChanged();
        }
        listView.showFooter();
        listComplete = false;
        this.query = query;
        offset = 0;
        loadMore();
    }

    @Override
    protected final void onViewCreated(View convertView, int position) {
        if (!listComplete && !loading && position > entries.size() - PRELOAD_ITEMS)
            loadMore();
    }

    private void loadMore() {
        loading = true;
        query.offset(offset);
        ExecuteQueryTask.execute(this, query, clazz);
    }

    @Override
    public final void onResult(LinkedList<RT> entries, Header header) {
        if (entries == null) {
            ToastUtil.showLong("Error getting more entries");
        } else {
            if (entries.isEmpty()) {
                listComplete = true;
                listView.hideFooter();
            } else {
                offset += entries.size();
                this.entries.addAll(entries);
                notifyDataSetChanged();
            }
        }
        loading = false;
    }
}