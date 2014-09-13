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

import info.lusito.jamendo.android.R;
import info.lusito.jamendo.android.utils.ImageDownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class RemoteImageView extends LinearLayout implements ImageDownloader.Listener {
    private ImageView imageView;

    private ProgressBar progressBar;
    private String expectedUrl;
    private int fallbackResId;

    public RemoteImageView(Context context) {
        super(context);
        setup();
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.widget_remote_image, this);

        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void setUrl(String url, int fallbackResId) {
        this.fallbackResId = fallbackResId;
        if (url == null || url.isEmpty()) {
            applyFallbackImage();
            expectedUrl = null;
        } else {
            expectedUrl = url;
            ImageDownloader.requestImage(url, this);
        }
    }

    public void setLoading() {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void onImageScheduled(String url) {
        setLoading();
    }

    @Override
    public void onImageReceived(String url, Bitmap bitmap) {
        //onImageError(url); // to test fallback images
        if (url.equals(expectedUrl)) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            expectedUrl = null;
        }
    }

    @Override
    public void onImageError(String url) {
        if (url.equals(expectedUrl)) {
            applyFallbackImage();
            expectedUrl = null;
        }
    }

    private void applyFallbackImage() {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(this.fallbackResId);
        imageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
