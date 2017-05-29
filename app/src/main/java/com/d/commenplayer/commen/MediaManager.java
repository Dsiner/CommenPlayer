package com.d.commenplayer.commen;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by D on 2017/5/27.
 */

public class MediaManager {
    IjkMediaPlayer player;

    public MediaManager() {
        player = new IjkMediaPlayer();
        player.release();
    }
}
