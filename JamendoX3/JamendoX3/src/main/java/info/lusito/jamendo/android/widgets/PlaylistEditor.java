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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.JamendoServiceConnectionListener;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.adapter.PlaylistAdapter;
import info.lusito.jamendo.android.player.MusicPlayerService;

public class PlaylistEditor extends LinearLayout implements JamendoServiceConnectionListener {
    private PlaylistAdapter playlist;

    public PlaylistEditor(Context context) {
        super(context);
        setup();
    }

    public PlaylistEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public PlaylistEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        MainActivity.getInstance().addJamendoServiceConnectionListener(this);
        inflate(getContext(), R.layout.widget_playlist_editor, this);

        if(!isInEditMode()) {
            setupButton(R.id.prevButton, (View v) -> playlist.prev());
            setupButton(R.id.playButton, (View v) -> playlist.togglePause());
            setupButton(R.id.nextButton, (View v) -> playlist.next());
        }
    }

    private void setupButton(int id, OnClickListener listener) {
        ImageButton button = (ImageButton)findViewById(id);
        button.setOnClickListener(listener);
    }

    @Override
    public void onMusicPlayerServiceConnected(MusicPlayerService service) {
        playlist = service.playlist;
    }
}
