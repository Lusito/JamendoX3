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
import info.lusito.jamendo.android.adapter.AutoCompleteAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SearchRow extends LinearLayout implements AdapterView.OnItemSelectedListener {
    private Spinner types;
    private AutoCompleteTextView searchField;
    private AutoCompleteAdapter completeAdapter;
    private SearchSortRow sortRow;

    public SearchRow(Context context) {
        super(context);
        setup();
    }

    public SearchRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public SearchRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.widget_search_row, this);

        types = (Spinner) findViewById(R.id.typeSpinner);
        searchField = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        if (!isInEditMode()) {
            types.setOnItemSelectedListener(this);
            types.setAdapter(new SpinnerArrayAdapter(getContext(), SearchType.values()));
        }
    }

    public AutoCompleteTextView getEdit() {
        return searchField;
    }

    public void initAdapter() {
//        completeAdapter = new AutoCompleteAdapter();
//        searchField.setAdapter(completeAdapter);
//
//        completeAdapter.setType((SearchType) types.getSelectedItem());
    }

    public void setSortRow(SearchSortRow sortRow) {
        this.sortRow = sortRow;
        sortRow.setTypes(types);
    }

    public SearchType getSearchType() {
        return (SearchType) types.getSelectedItem();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SearchType type = (SearchType) types.getSelectedItem();
        if (completeAdapter != null)
            completeAdapter.setType(type);

        sortRow.onItemSelected(adapterView, view, i, l);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getText() {
        return searchField.getText().toString();
    }
}
