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
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.activities.MainActivity;
import info.lusito.jamendo.android.fragments.AlbumInfoFragment;
import info.lusito.jamendo.android.fragments.ArtistInfoFragment;
import info.lusito.jamendo.android.fragments.TrackInfoFragment;
import info.lusito.jamendo.android.utils.ToastUtil;

public class ActionButtonList extends ScrollView {
    private LinearLayout buttonList;
    public final Data data = new Data();
    private ArrayList<ActionButton> buttons = new ArrayList<ActionButton>();

    public static class Data {
        public Integer artist_id;
        public Integer album_id;
        public Integer track_id;
        public String website;
        public String prourl;
        public String download;

        public void clear() {
            artist_id = null;
            album_id = null;
            track_id = null;
            prourl = null;
            download = null;
        }
    }

    public static enum ActionButtonType {

        TRACK(R.drawable.action_track),
        ARTIST(R.drawable.action_artist),
        ALBUM(R.drawable.action_album),
        FAVORITE(R.drawable.action_favorite),
        LIKE(R.drawable.action_like),
        DISLIKE(R.drawable.action_dislike),
        DOWNLOAD(R.drawable.action_download),
        BUY(R.drawable.action_buy),
        WEBSITE(R.drawable.action_website);

        final int resourceId;

        ActionButtonType(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public class ActionButton extends LinearLayout implements OnClickListener {
        private final ActionButtonType type;
        private ImageButton button;

        public ActionButton(Context context, ActionButtonType type) {
            super(context);
            this.type = type;
            setup();
        }

        public ActionButton(Context context) {
            super(context);
            this.type = null;
            setup();
        }

        public ActionButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.type = null;
            setup();
        }

        public ActionButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.type = null;
            setup();
        }

        private void setup() {
            inflate(getContext(), R.layout.widget_action_button, this);

            button = (ImageButton) findViewById(R.id.action_image_button);
            button.setOnClickListener(this);
            button.setImageResource(type.resourceId);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            this.setMeasuredDimension(parentWidth,parentWidth);
            this.setLayoutParams(new LayoutParams(parentWidth, parentWidth));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        public void onClick(View view) {
            hitButton(type, false);
        }

        public void show(boolean show) {
            if (show)
                setVisibility(VISIBLE);
            else
                setVisibility(GONE);
        }
    }

    public ActionButtonList(Context context) {
        super(context);
        setup();
    }

    public ActionButtonList(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
        parseAttributes(attrs);
    }

    public ActionButtonList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
        parseAttributes(attrs);
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ActionButtonList);
        String actions = a.getString(R.styleable.ActionButtonList_actions);
        if (actions != null && !actions.isEmpty()) {
            setupButtons(actions);
        }
        a.recycle();
    }

    public void setupButtons(String actions) {
        buttonList.removeAllViews();
        buttons.clear();
        String[] actionsList = actions.split(",");

        for (String action : actionsList) {
            ActionButtonType type = ActionButtonType.valueOf(action.trim().toUpperCase());
            if (type != null) {
                ActionButton button = new ActionButton(getContext(), type);
                buttonList.addView(button);
                buttons.add(button);
            }
        }
        dataChanged();
    }

    public void dataChanged() {
        for (ActionButton button : buttons) {
            button.show(hitButton(button.type, true));
        }
    }

    private void setup() {
        buttonList = new LinearLayout(getContext());
        buttonList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        buttonList.setOrientation(LinearLayout.VERTICAL);
        addView(buttonList);
    }


    public boolean hitButton(ActionButtonType type, boolean test) {
        // remove when they can be used
        switch (type) {
            case FAVORITE:
            case LIKE:
            case DISLIKE:
            case DOWNLOAD:
                return false;
        }
        switch (type) {
            case TRACK:
                if (data.track_id != null && data.album_id != null && data.artist_id != null) {
                    if (!test)
                        MainActivity.getInstance().showFragment(TrackInfoFragment.create(data.track_id, data.album_id, data.artist_id), true);
                    return true;
                }
                return false;
            case ARTIST:
                if (data.artist_id != null) {
                    if (!test)
                        MainActivity.getInstance().showFragment(ArtistInfoFragment.create(data.artist_id), true);
                    return true;
                }
                return false;
            case ALBUM:
                if (data.album_id != null) {
                    if (!test)
                        MainActivity.getInstance().showFragment(AlbumInfoFragment.create(data.album_id), true);
                    return true;
                }
                return false;
            case FAVORITE:
                if (data.track_id != null) {
                    if (!test)
                        ; // set favorite
                    return true;
                } else if (data.album_id != null) {
                    if (!test)
                        ; // set myalbum
                    return true;
                } else if (data.artist_id != null) {
                    if (!test)
                        ; // set fan
                    return true;
                }
                return false;
            case LIKE:
                if (data.track_id != null) {
                    if (!test)
                        ;// like
                    return true;
                }
                return false;
            case DISLIKE:
                if (data.track_id != null) {
                    if (!test)
                        ;// dislike
                    return true;
                }
                return false;
            case DOWNLOAD:
                if (data.download != null) {
                    if (!test)
                        ToastUtil.showShort(data.download); // download
                    return true;
                }
                break;
            case BUY:
                if (data.prourl != null) {
                    if (!test)
                        showUrl(data.prourl);
                    return true;
                }
                return false;
            case WEBSITE:
                if (data.website != null) {
                    if (!test)
                        showUrl(data.website);
                    return true;
                }
                return false;
        }
        return false;
    }

    public void showUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        MainActivity.getInstance().startActivity(intent);
    }
}
