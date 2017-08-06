package com.d.commenplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Surface;

public class Util {
    public final static int SEEKBAR_MAX = 1000;

    /**
     * 获取屏幕宽度和高度
     */
    public static int[] getScreenSize(Activity activity) {
        int[] size = new int[2];
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        size[0] = metric.widthPixels;
        size[1] = metric.heightPixels;
        return size;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dpValue * (metrics.densityDpi / 160f));
    }

    public static String generateTime(int time) {
        int totalSeconds = time / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 获取SeekBar进度
     *
     * @param position:当前播放时间
     * @param duration:播放总时间
     */
    public static int getProgress(int position, int duration) {
        int progress = (int) (1.0f * Util.SEEKBAR_MAX * position / duration);
        progress = Math.min(progress, duration);
        progress = Math.max(progress, 0);
        return progress;
    }

    /**
     * 获取SeekBar缓冲进度
     *
     * @param bufferPercentage:缓冲进度bufferPercentage,0-100
     */
    public static int getSecondaryProgress(int bufferPercentage) {
        int secondaryProgress = (int) (1.0f * bufferPercentage / 100 * Util.SEEKBAR_MAX);
        secondaryProgress = Math.min(secondaryProgress, Util.SEEKBAR_MAX);
        secondaryProgress = Math.max(secondaryProgress, 0);
        return secondaryProgress;
    }

    /**
     * 获取当前播放位置
     *
     * @param progress:seekbar当前进度
     * @param duration:播放总时间
     */
    public static int getPosition(int progress, int duration) {
        int position = (int) (1.0f * progress / Util.SEEKBAR_MAX * duration);
        position = Math.min(position, duration);
        position = Math.max(position, 0);
        return position;
    }

    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
                && height > width
                || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
                && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }
        return orientation;
    }
}
