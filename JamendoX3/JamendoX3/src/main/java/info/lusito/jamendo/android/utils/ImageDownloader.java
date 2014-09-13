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

package info.lusito.jamendo.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

public class ImageDownloader {
    private static final int CACHE_SIZE = 24 * 1024 * 1024;
    private static final HashMap<String, HashSet<Listener>> listeners = new HashMap<String, HashSet<Listener>>();
    private static final LruCache<String, Bitmap> bitmaps = new LruCache<String, Bitmap>(CACHE_SIZE) {
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public static interface Listener {
        void onImageScheduled(String url);

        void onImageReceived(String url, Bitmap bitmap);

        void onImageError(String url);
    }

    public static void requestImage(String url, Listener listener) {
        synchronized (bitmaps) {
            Bitmap bitmap = bitmaps.get(url);
            if (bitmap != null) {
                listener.onImageReceived(url, bitmap);
                return;
            }
            HashSet<Listener> set = listeners.get(url);
            if (set == null) {
                set = new HashSet<Listener>();
                listeners.put(url, set);
            }
            set.add(listener);
            new DownloadTask(url).execute();
            listener.onImageScheduled(url);
        }
    }

    public static void addImage(String url, Bitmap bitmap) {
        synchronized (bitmaps) {
            if (bitmap != null)
                bitmaps.put(url, bitmap);

            HashSet<Listener> set = listeners.get(url);
            for (Listener listener : set) {
                if (bitmap == null) {
                    listener.onImageError(url);
                } else {
                    listener.onImageReceived(url, bitmap);
                }
            }
            set.clear();
            listeners.remove(set);
        }
    }

    private static class DownloadTask extends AsyncTask<Object, Object, Bitmap> {
        private String url;

        public DownloadTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap;

            synchronized (bitmaps) {
                bitmap = bitmaps.get(url);
                if (bitmap != null)
                    return bitmap;
            }

            InputStream in = null;
            BufferedOutputStream out = null;

            try {
                in = new BufferedInputStream(new URL(url.replaceAll(" ", "%20")).openStream());

                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream);
                StreamUtils.copy(in, out);
                out.flush();

                final byte[] data = dataStream.toByteArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inSampleSize = 1;

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                if (bitmap != null) {
                    synchronized (bitmaps) {
                        bitmaps.put(url, bitmap);
                    }
                }
            } catch (IOException e) {
                JLog.e("DownloadTask: " + url, e);
            } finally {
                StreamUtils.close(in);
                StreamUtils.close(out);
            }

            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageDownloader.addImage(url, bitmap);
        }
    }
}
