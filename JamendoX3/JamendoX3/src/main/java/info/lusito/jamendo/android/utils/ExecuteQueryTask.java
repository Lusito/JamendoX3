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

package info.lusito.jamendo.android.utils;

import java.util.LinkedList;
import java.util.List;

import info.lusito.jamendo.api.query.AbstractQuery;
import info.lusito.jamendo.api.results.Header;

import android.os.AsyncTask;
import android.util.LruCache;

public class ExecuteQueryTask<QT extends AbstractQuery<RT, QT>, RT> extends AsyncTask<Object, Object, LinkedList<RT>> {
    private static final int CACHE_SIZE = 4 * 1024;
    private static final LruCache<String, Object> cachedQueries = new LruCache<String, Object>(CACHE_SIZE) {
        protected int sizeOf(String key, Object value) {
            if (value instanceof List) {
                return ((List) value).size();
            }
            return 1;
        }
    };

    private QueryTaskListener<RT> listener;
    Header header = new Header();

    private QT query;

    private Class<RT> clazz;

    public ExecuteQueryTask(QueryTaskListener<RT> listener, QT query, Class<RT> clazz) {
        this.listener = listener;
        this.query = query;
        this.clazz = clazz;
    }

    @Override
    protected LinkedList<RT> doInBackground(Object... params) {
        String url = null;

        // Only cache
        if (!query.isIgnoreCache()) {
            try {
                url = query.createUrl();
            } catch (Exception e) {
                JLog.wtf("Trying to get query url", e);
            }

            synchronized (cachedQueries) {
                Object result = cachedQueries.get(url);
                if (result != null) {
                    JLog.v("Cached query returned: " + url);
                    return (LinkedList<RT>) result;
                }
            }
        }

        try {
            LinkedList<RT> result = query.get(header, clazz);
            if (result != null && url != null) {
                synchronized (cachedQueries) {
                    cachedQueries.put(url, result);
                }
            }
            return result;
        } catch (Exception e) {
            JLog.e(query.getClass().getSimpleName() + ".get()", e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
    }

    @Override
    protected void onPostExecute(LinkedList<RT> result) {
        listener.onResult(result, header);
    }

    public static <QT extends AbstractQuery<RT, QT>, RT> void execute(QueryTaskListener<RT> listener, QT query, Class<RT> clazz) {
        new ExecuteQueryTask<QT, RT>(listener, query, clazz).execute();
    }
}
