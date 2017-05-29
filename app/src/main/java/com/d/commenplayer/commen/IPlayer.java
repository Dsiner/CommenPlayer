package com.d.commenplayer.commen;

/**
 * IPlayer
 * Created by D on 2017/5/27.
 */
public interface IPlayer {
    /**
     * init
     */
    void init();

    /**
     * control
     */
    void play(String url);

    void seekTo(long time);

    void pause();

    void stop();

    /**
     * display setting
     */
    void fullScreen();

    void setOrientation(int orientation);

    void setDisplayMode();

    /**
     * liftcycle
     */
    void onResume();

    void onPause();

    boolean onBackPress();

    void onDestroy();
}
