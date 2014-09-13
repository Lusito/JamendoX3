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

import android.view.View;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UiRunner implements Runnable {
    private final View view;
    private final ScheduledExecutorService executorService;
    private Runnable action;
    private ScheduledFuture<?> handle;

    public UiRunner(View view, ScheduledExecutorService executorService) {
        this.view = view;
        this.executorService = executorService;
    }

    public void schedule(Runnable action, int delay, TimeUnit timeUnit) {
        if (handle != null)
            handle.cancel(false);

        this.action = action;
        handle = executorService.schedule(this, delay, timeUnit);
    }

    public void scheduleAtFixedRate(Runnable action, int initialDelay, int delay, TimeUnit timeUnit) {
        if (handle != null)
            handle.cancel(false);

        this.action = action;
        handle = executorService.scheduleAtFixedRate(this, initialDelay, delay, timeUnit);
    }

    @Override
    public void run() {
        view.post(action);
        handle = null;
    }

    public boolean isRunning() {
        return handle != null;
    }

    public void cancel(boolean mayInterruptIfRunning) {
        if (handle != null)
            handle.cancel(mayInterruptIfRunning);
        handle = null;
    }
}