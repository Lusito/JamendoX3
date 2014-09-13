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

package info.lusito.jamendo.android.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.adapter.AbstractQueryAdapter;

public class QueryListView extends ListView {
    private View footer;
    private boolean isFooterVisible;

    public QueryListView(Context context) {
        super(context);
        setup(context);
    }

    public QueryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public QueryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        footer = inflate(context, R.layout.list_footer_query, null);
        showFooter();
    }

    public void setAdapter(AbstractQueryAdapter adapter) {
        showFooter();
        adapter.connectView(this);
        hideFooter();
    }

    public void hideFooter() {
        if(isFooterVisible) {
            removeFooterView(footer);
            isFooterVisible = false;
        }
    }
    public void showFooter() {
        if(!isFooterVisible) {
            addFooterView(footer, null, false);
            isFooterVisible = true;
        }
    }
}
