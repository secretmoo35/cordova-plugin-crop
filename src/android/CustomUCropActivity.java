package com.jeduan.crop;

import android.os.Bundle;

import androidx.core.view.WindowCompat;

import com.yalantis.ucrop.UCropActivity;

/**
 * Custom UCropActivity that fixes the edge-to-edge overlap issue on Android SDK 35+.
 *
 * Android 15 (API 35) enforces edge-to-edge display by default, meaning app content
 * is drawn behind the system status bar and navigation bar. This causes the UCrop
 * toolbar buttons (Cancel ✕ and Done ✓) to be hidden under the status bar.
 *
 * WindowCompat.setDecorFitsSystemWindows(window, true) tells the system to
 * keep content within the "safe" area (below the status bar, above the nav bar).
 * This call uses AndroidX Core which is already bundled with UCrop, so no extra
 * dependency is needed. It works regardless of compileSdkVersion.
 */
public class CustomUCropActivity extends UCropActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Must be called BEFORE super.onCreate() so the window flag is set
        // before UCropActivity inflates and places its toolbar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        super.onCreate(savedInstanceState);
    }
}
