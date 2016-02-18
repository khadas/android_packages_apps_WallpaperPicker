package com.android.wallpaperpicker.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.gallery3d.common.BitmapCropTask;
import com.android.wallpaperpicker.R;

/**
 * Utility class used to show dialogs for things like picking which wallpaper to set.
 */
public class DialogUtils {
    //TODO: @TargetApi(Build.VERSION_CODES.N)
    /**
     * Prompts user to select "Home screen," "Lock screen," or "Home screen and lock screen."
     *
     * Note: This method must be called from the UI thread.
     */
    public static void showWhichWallpaperDialog(Context context,
            DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.which_wallpaper_title)
                .setCancelable(false)
                .setItems(R.array.which_wallpaper_options, onClickListener)
                .show();
    }

    /**
     * Calls cropTask.execute(), once the user has selected which wallpaper to set. On pre-N
     * devices, the prompt is not displayed since there is no API to set the lockscreen wallpaper.
     *
     * TODO: Don't use BitmapCropTask on N+, because the new API will handle cropping instead.
     */
    public static void executeCropTaskAfterPrompt(Context context, final BitmapCropTask cropTask) {
        if (Utilities.isAtLeastN()) {
            showWhichWallpaperDialog(context, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int whichWallpaper;
                    if (which == 0) {
                        whichWallpaper = WallpaperManagerCompat.FLAG_SET_SYSTEM;
                    } else if (which == 1) {
                        whichWallpaper = WallpaperManagerCompat.FLAG_SET_LOCK;
                    } else {
                        whichWallpaper = WallpaperManagerCompat.FLAG_SET_SYSTEM
                                | WallpaperManagerCompat.FLAG_SET_LOCK;
                    }
                    cropTask.execute(whichWallpaper);
                }
            });
        } else {
            cropTask.execute(WallpaperManagerCompat.FLAG_SET_SYSTEM);
        }
    }
}