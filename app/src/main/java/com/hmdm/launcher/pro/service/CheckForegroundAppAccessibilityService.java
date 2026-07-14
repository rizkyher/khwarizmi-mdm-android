/*
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hmdm.launcher.pro.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import com.hmdm.launcher.Const;
import com.hmdm.launcher.util.RemoteLogger;

import java.lang.ref.WeakReference;

/**
 * Open-source accessibility service used for remote screen gestures only.
 * Foreground app blocking remains a Pro feature.
 */
public class CheckForegroundAppAccessibilityService extends AccessibilityService {
    private static WeakReference<CheckForegroundAppAccessibilityService> activeService =
            new WeakReference<>(null);

    @Override
    protected void onServiceConnected() {
        activeService = new WeakReference<>(this);
        RemoteLogger.log(this, Const.LOG_INFO, "Accessibility gesture service connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Foreground app tracking is intentionally not implemented in the open-source build.
    }

    @Override
    public void onInterrupt() {
        // Nothing to interrupt.
    }

    @Override
    public boolean onUnbind(android.content.Intent intent) {
        activeService.clear();
        return super.onUnbind(intent);
    }

    public static boolean dispatchTap(float x, float y) {
        CheckForegroundAppAccessibilityService service = activeService.get();
        if (service == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return false;
        }

        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription gesture = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 60))
                .build();
        return service.dispatchGesture(gesture, null, null);
    }
}
