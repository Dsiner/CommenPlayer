package com.d.iplayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.d.commenplayer.CommenPlayer;
import com.d.commenplayer.listener.IPlayerListener;
import com.d.commenplayer.listener.OnNetChangeListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends Activity {
    private String url1 = "http://vpls.cdn.videojj.com/scene/video02_720p.mp4";
    private String url2 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String url3 = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
    private CommenPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = (CommenPlayer) findViewById(R.id.player);
        initPlayer();
    }

    private void initPlayer() {
        player.setLive(false);
        player.setListenNetChange(true).setNetChangeListener(new OnNetChangeListener() {
            @Override
            public void onWifi() {

            }

            @Override
            public void onMobile() {

            }

            @Override
            public void onUnConnected() {

            }

            @Override
            public void onNoAvailable() {

            }
        }).setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onCompletion(IMediaPlayer mp) {

            }

            @Override
            public void onPrepared(IMediaPlayer mp) {

            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {

            }
        });
        player.play(url1);
    }

    @Override
    protected void onResume() {
        if (player != null) {
            player.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (player != null) {
            player.onPause();
        }
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams lp = player.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            player.setLayoutParams(lp);
        } else {
            lp.height = dip2px(getApplicationContext(), 180);
            player.setLayoutParams(lp);
        }
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.onDestroy();
        }
        super.onDestroy();
    }

    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dpValue * (metrics.densityDpi / 160f));
    }
}
