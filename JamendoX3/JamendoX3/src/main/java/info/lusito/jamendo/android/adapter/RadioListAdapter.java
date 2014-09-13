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
import info.lusito.jamendo.android.widgets.RadioChangedListener;
import info.lusito.jamendo.android.widgets.RemoteImageView;
import info.lusito.jamendo.api.results.radio.Radio;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class RadioListAdapter extends AbstractAdapter<Radio> {
    private RadioChangedListener radioChangedListener;
    private int selection = -1;
    private GridView listView;

    private static class ViewHolder extends AbstractViewHolder<Radio> {
        final TextView textView;
        final RemoteImageView imageView;

        ViewHolder(View convertView) {
            super(convertView);

            textView = (TextView) convertView.findViewById(R.id.grid_item_text);
            imageView = (RemoteImageView) convertView.findViewById(R.id.grid_item_image);
        }

        @Override
        public void onCreate(Radio entry) {
            imageView.setUrl(entry.image, R.drawable.unknown_radio);
            textView.setText(entry.dispname.replace(" Radio", ""));
        }
    }

    public void setupView(GridView listView) {
        this.listView = listView;
        super.connectView(listView);
    }

    @Override
    protected void onViewCreated(View convertView, int position) {
        convertView.setBackgroundResource(selection == position ? R.drawable.border_selected : R.drawable.border);
    }

    @Override
    protected AbstractViewHolder createViewHolder(LayoutInflater inflater) {
        return new ViewHolder(inflater.inflate(R.layout.grid_item_radio, null));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        removeSelections();
        setSelection(position);
        if (view != null) {
            view.setBackgroundResource(R.drawable.border_selected);
        }
        if (radioChangedListener != null) {
            radioChangedListener.onRadioChanged(getRadio(position));
        }
    }

    private void removeSelections() {
        for (int i = 0; i <= listView.getLastVisiblePosition(); i++) {
            View convertView = listView.getChildAt(i);
            if (convertView != null) {
                convertView.setBackgroundResource(R.drawable.border);
            }
        }
    }

    public int getRadioIndex(String name) {
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (entries.get(i).name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public Radio getRadio(int position) {
        return entries.get(position);
    }

    public void setSelection(int position) {
        selection = position;
    }

    public void setRadioChangedListener(RadioChangedListener listener) {
        radioChangedListener = listener;
    }
}