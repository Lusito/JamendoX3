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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public abstract class AbstractAdapter<T> extends BaseAdapter implements AdapterView.OnItemClickListener {
    protected LinkedList<T> entries;

    public AbstractAdapter() {
        entries = new LinkedList<T>();
    }

    public AbstractAdapter(LinkedList<T> entries) {
        this.entries = entries;
    }

    public void setList(LinkedList<T> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    @Override
    public final int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    protected abstract AbstractViewHolder createViewHolder(LayoutInflater inflater);
    protected void onViewCreated(View convertView, int position) {
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        AbstractViewHolder holder;
        if (convertView == null) {
            holder = createViewHolder(LayoutInflater.from(parent.getContext()));
            convertView = holder.convertView;
        } else {
            holder = (AbstractViewHolder) convertView.getTag();
        }

        holder.entry = entries.get(position);
        holder.updateBackground(position);
        holder.onCreate(holder.entry);

        onViewCreated(convertView, position);
        return convertView;
    }

    public void connectView(AdapterView<ListAdapter> listView) {
        listView.setOnItemClickListener(this);
        listView.setAdapter(this);
    }
}