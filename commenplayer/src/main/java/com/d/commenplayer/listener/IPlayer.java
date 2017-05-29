package com.d.commenplayer.listener;

import android.content.res.Configuration;

/**
 * IPlayer
 * Created by D on 2017/5/27.
 */
public interface IPlayer {
    /**
     * live mode
     */
    void setLive(boolean live);

    /**
     * control
     */
    void play(String url);

    void seekTo(int time);

    void pause();

    /**
     * display setting
     */
    void setScaleType(int scaleType);

    int toggleAspectRatio();

    void onConfigurationChanged(Configuration newConfig);

    void toggleOrientation();

    /**
     * liftcycle
     */
    void onResume();

    void onPause();

    boolean onBackPress();

    void onDestroy();
}
