package com.d.commenplayer.commen;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.commenplayer.R;

/**
 * Depositer
 * Created by D on 2017/5/28.
 */
public class Depositer {
    private View vLoading;//loading态
    private View vControl;//播放控制态
    private ImageView ivControl;
    private TextView tvControl;
    private Button btnControl;
    private View vVideo;//视频态

    public Depositer(View vLoading, View vControl, View vVideo) {
        this.vLoading = vLoading;
        this.vControl = vControl;
        this.vVideo = vVideo;
        ivControl = (ImageView) vControl.findViewById(R.id.start);
        tvControl = (TextView) vControl.findViewById(R.id.tv_des);
        btnControl = (Button) vControl.findViewById(R.id.btn_button);
    }

    public void setPlayerVisibility(int visibility0, int visibility1, int visibility2) {
        vLoading.setVisibility(visibility0);
        vControl.setVisibility(visibility1);
        vVideo.setVisibility(visibility2);
    }

    public void setControl(String text, String button, View.OnClickListener l) {
        if (ivControl != null) ivControl.setVisibility(View.GONE);
        if (tvControl != null) {
            tvControl.setText(text);
            tvControl.setVisibility(View.VISIBLE);
        }
        if (btnControl != null) {
            btnControl.setText(button);
            btnControl.setVisibility(View.VISIBLE);
            btnControl.setOnClickListener(l);
        }
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
