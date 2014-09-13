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

import android.util.Log;

public class JLog {
    public static String TAG = "Jamendo";

    public static int v(String msg) {
        return Log.v(TAG, msg);
    }

    public static int v(String msg, Throwable tr) {
        return Log.v(TAG, msg, tr);
    }

    public static int d(String msg) {
        return Log.d(TAG, msg);
    }

    public static int d(String msg, Throwable tr) {
        return Log.d(TAG, msg, tr);
    }

    public static int i(String msg) {
        return Log.i(TAG, msg);
    }

    public static int i(String msg, Throwable tr) {
        return Log.i(TAG, msg, tr);
    }

    public static int w(String msg) {
        return Log.w(TAG, msg);
    }

    public static int w(String msg, Throwable tr) {
        return Log.w(TAG, msg);
    }

    public static int w(Throwable tr) {
        return Log.w(TAG, tr);
    }

    public static int e(String msg) {
        return Log.e(TAG, msg);
    }

    public static int e(String msg, Throwable tr) {
        return Log.e(TAG, msg, tr);
    }

    public static int wtf(String msg) {
        return Log.wtf(TAG, msg);
    }

    public static int wtf(Throwable tr) {
        return Log.wtf(TAG, tr);
    }

    public static int wtf(String msg, Throwable tr) {
        return Log.wtf(TAG, msg, tr);
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    public static int println(int priority, String msg) {
        return Log.println(priority, TAG, msg);
    }
}
