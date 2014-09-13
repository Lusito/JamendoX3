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

import java.util.LinkedList;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.adapter.RadioListAdapter;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.radio.Radio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class RadioList extends RelativeLayout {
    private RadioListAdapter adapter;

    private int numColumns = -1;

    private GridView grid;

    private ProgressBar progressBar;

    public RadioList(Context context) {
        super(context);
        setup();
    }

    public RadioList(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setup();
    }

    public RadioList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(attrs);
        setup();
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RadioList);
        numColumns = a.getInteger(R.styleable.RadioList_numColumns, -1);
        a.recycle();
    }

    private void setup() {
        inflate(getContext(), R.layout.widget_radio_list, this);

        grid = (GridView) findViewById(R.id.grid);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (!isInEditMode()) {
            if (numColumns > 0) {
                grid.setNumColumns(numColumns);
            }
        }
    }

    public void setupList(RadioChangedListener listener) {
        adapter = new RadioListAdapter();
        adapter.setupView(grid);
        adapter.setRadioChangedListener(listener);
    }

    public void updateRadios(LinkedList<Radio> radios, Header header, String selectedRadioName) {
        adapter.setList(radios);

        int position = adapter.getRadioIndex(selectedRadioName);
        if (position == -1)
            position = 0;
        adapter.setSelection(position);

        if (grid != null) {
            grid.setAdapter(adapter);
            grid.setSelection(position);
            grid.setVisibility(View.VISIBLE);
        }

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        if (position >= 0) {
            adapter.onItemClick(grid, grid.getChildAt(position), position, 0);
        }
    }
}
