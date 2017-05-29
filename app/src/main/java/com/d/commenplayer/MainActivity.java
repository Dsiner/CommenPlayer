package com.d.commenplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.d.commenplayer.widget.media.AndroidMediaController;
import com.d.commenplayer.widget.media.IjkVideoView;
import com.d.commenplayer.widget.preference.Settings;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private AndroidMediaController mediaController;
    private IjkVideoView player;
    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPlayer();
    }

    private void initPlayer() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mSettings = new Settings(this);
        player = (IjkVideoView) findViewById(R.id.player);
        player.setVideoPath("http://vpls.cdn.videojj.com/scene/video02_720p.mp4");
        player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                player.start();
            }
        });
    }
}
