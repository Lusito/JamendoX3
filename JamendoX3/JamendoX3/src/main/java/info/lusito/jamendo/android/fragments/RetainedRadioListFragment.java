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

import info.lusito.jamendo.android.utils.ExecuteQueryTask;
import info.lusito.jamendo.android.utils.QueryTaskListener;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.RadioOrder;
import info.lusito.jamendo.api.enums.RadioType;
import info.lusito.jamendo.api.query.radio.RadioQuery;
import info.lusito.jamendo.api.query.radio.RadioStreamQuery;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.radio.Radio;
import info.lusito.jamendo.api.results.radio.RadioStream;

import java.util.LinkedList;

import android.app.Fragment;
import android.os.Bundle;

// Todo: remove, since the cached queries should solve this now
public class RetainedRadioListFragment extends Fragment implements QueryTaskListener<Radio> {
    private LinkedList<QueryTaskListener<Radio>> listeners = new LinkedList<QueryTaskListener<Radio>>();
    private LinkedList<Radio> radios;
    private Header header;
    private RadioStream radioStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        queryUpdate();
    }

    public void queryUpdate() {
        RadioQuery query = new RadioQuery().type(RadioType.WWW).order(RadioOrder.NAME).imagesize(ImageSize._150).limit(100);

        ExecuteQueryTask.execute(this, query, Radio.class);
    }

    @Override
    public void onResult(LinkedList<Radio> radios, Header header) {
        this.radios = radios;
        this.header = header;
        for (QueryTaskListener<Radio> listener : listeners) {
            listener.onResult(radios, header);
        }
        listeners.clear();
    }

    public void requestUpdate(QueryTaskListener<Radio> listener) {
        if (radios != null) {
            listener.onResult(radios, header);
        } else {
            listeners.push(listener);
        }
    }

    public RadioStream getRadioStream() {
        return radioStream;
    }

    public void requestStreamUpdate(QueryTaskListener<RadioStream> listener, int radioId) {
        RadioStreamQuery query = new RadioStreamQuery().id(radioId).ignoreCache(true);

        ExecuteQueryTask.execute(listener, query, RadioStream.class);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}