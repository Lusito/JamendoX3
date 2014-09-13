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

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.utils.QueryTaskListener;
import info.lusito.jamendo.android.utils.ShareInfo;
import info.lusito.jamendo.android.utils.ToastUtil;
import info.lusito.jamendo.android.utils.UiRunner;
import info.lusito.jamendo.android.widgets.RadioChangedListener;
import info.lusito.jamendo.android.widgets.RadioInfo;
import info.lusito.jamendo.android.widgets.RadioList;
import info.lusito.jamendo.api.results.Header;
import info.lusito.jamendo.api.results.radio.Radio;
import info.lusito.jamendo.api.results.radio.RadioStream;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RadioFragment extends AbstractTabFragment implements QueryTaskListener<Radio>, RadioChangedListener {
    private RetainedRadioListFragment radioListFragment;

    private RadioList radioList;
    private RadioInfo radioInfo;
    private UiRunner radioRefreshRunner;
    private Radio currentRadio;
    private SharedPreferences sharedPrefs;
    private RadioStream currentRadioStream;

    private class RefreshRadioRequest implements Runnable {
        @Override
        public void run() {
            //fixme: if radio was stopped, do not update ?
            refreshRadio();
        }
    }

    @Override
    public String getTitle() {
        return "Radio";
    }

    @Override
    public int getImageResource() {
        return R.drawable.start_radio;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPrefs = MainActivity.getInstance().getSharedPrefs();
        View v = inflater.inflate(R.layout.fragment_radio, container, false);

        radioInfo = (RadioInfo) v.findViewById(R.id.radioInfo);
        radioList = (RadioList) v.findViewById(R.id.radioList);
        radioList.setupList(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        radioListFragment = (RetainedRadioListFragment) fm.findFragmentByTag("radioList");

        if (radioListFragment == null) {
            radioListFragment = new RetainedRadioListFragment();
            fm.beginTransaction().add(radioListFragment, "radioList").commit();
        }

        radioListFragment.requestUpdate(this);

        RadioStream radioStream = radioListFragment.getRadioStream();
        if (radioStream != null) {
            updateStream(radioStream);
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        radioRefreshRunner = new UiRunner(radioInfo, executorService);
    }

    private void updateStream(final RadioStream radioStream) {
        currentRadioStream = radioStream;
        radioInfo.updateStreamInfo(radioStream);
        MainActivity.getInstance().playRadio(radioStream);

        radioRefreshRunner.schedule(new RefreshRadioRequest(), radioStream.callmeback, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onResult(LinkedList<Radio> radios, Header header) {
        if (radios == null || radios.isEmpty()) {
            ToastUtil.showLong("Error getting radios");
        } else {
            String lastRadioName = sharedPrefs.getString("radio", "rock");
            radioList.updateRadios(radios, header, lastRadioName);
        }
    }

    private void refreshRadio() {
        radioInfo.setLoading(currentRadio.dispname);
        QueryTaskListener<RadioStream> listener = new QueryTaskListener<RadioStream>() {
            @Override
            public void onResult(LinkedList<RadioStream> radioStreams, Header header) {
                if (radioStreams == null|| radioStreams.isEmpty()) {
                    ToastUtil.showLong("Error getting radiostream");
                } else {
                    updateStream(radioStreams.get(0));
                }
            }
        };
        radioListFragment.requestStreamUpdate(listener, currentRadio.id);
    }

    @Override
    public void onRadioChanged(Radio radio) {
        currentRadio = radio;
        sharedPrefs.edit().putString("radio", radio.name).commit();
        refreshRadio();
    }

    @Override
    public ShareInfo getShareInfo() {
        if (currentRadioStream != null && currentRadioStream.playingnow != null)
            return new ShareInfo(currentRadioStream, radioInfo.getTrack());
        if (currentRadio != null)
            return new ShareInfo(currentRadio);
        return null;
    }
}