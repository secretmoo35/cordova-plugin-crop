package com.jeduan.crop;

import android.os.Bundle;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yalantis.ucrop.R;
import com.yalantis.ucrop.UCropActivity;

/**
 * Custom UCropActivity that fixes the edge-to-edge toolbar overlap on Android
 * SDK 35+.
 *
 * ROOT CAUSE:
 * Android 15 (API 35) with targetSdkVersion >= 35 ENFORCES edge-to-edge display
 * at the
 * OS level. Calls like WindowCompat.setDecorFitsSystemWindows(true) are
 * silently ignored.
 *
 * THE FIX:
 * Instead of opting out of edge-to-edge, we ACCEPT it and properly handle the
 * system
 * window insets by applying the status bar height as top padding on the
 * toolbar's
 * parent container. This pushes the toolbar below the status bar without
 * affecting
 * the image crop area below it.
 */
public class CustomUCropActivity extends UCropActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fixToolbarEdgeToEdgeOverlap();
    }

    private void fixToolbarEdgeToEdgeOverlap() {
        // UCrop toolbar is identified by R.id.toolbar
        View toolbar = findViewById(R.id.toolbar);
        if (toolbar == null)
            return;

        // Apply inset to the toolbar's parent container so only the top bar area
        // is adjusted — the image crop view below is unaffected
        View container = (toolbar.getParent() instanceof View)
                ? (View) toolbar.getParent()
                : toolbar;

        // Save original padding to avoid stacking on repeated calls (e.g.,
        // configuration change)
        final int originalPaddingTop = container.getPaddingTop();
        final int originalPaddingLeft = container.getPaddingLeft();
        final int originalPaddingRight = container.getPaddingRight();
        final int originalPaddingBottom = container.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(container, (v, windowInsets) -> {
            Insets statusBar = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(
                    originalPaddingLeft,
                    originalPaddingTop + statusBar.top, // shift toolbar below status bar
                    originalPaddingRight,
                    originalPaddingBottom);
            return windowInsets; // pass insets down so other views can also consume
        });

        // Trigger inset dispatch so the listener fires immediately
        ViewCompat.requestApplyInsets(container);
    }
}
