package com.d.commenplayer.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.d.commenplayer.R;
import com.d.commenplayer.adapter.PlayerAdapter;
import com.d.commenplayer.model.MediaModel;
import com.d.commenplayer.netstate.NetBus;
import com.d.commenplayer.netstate.NetCompat;
import com.d.commenplayer.netstate.NetState;
import com.d.lib.commenplayer.CommenPlayer;
import com.d.lib.commenplayer.adapter.AdapterPlayer;
import com.d.lib.commenplayer.listener.IPlayerListener;
import com.d.lib.commenplayer.listener.OnNetListener;
import com.d.lib.commenplayer.ui.ControlLayout;
import com.d.lib.commenplayer.util.ULog;
import com.d.lib.commenplayer.util.Util;
import com.d.lib.xrv.LRecyclerView;
import com.d.lib.xrv.adapter.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ListActivity extends Activity implements NetBus.OnNetListener {

    private ViewGroup itemContainer;
    private FrameLayout container;
    private CommenPlayer player;
    private boolean ignoreNet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        NetBus.getIns().addListener(this);
        initPlayer();
        initView();
    }

    private void initView() {
        container = (FrameLayout) findViewById(R.id.player_container);
        LRecyclerView list = (LRecyclerView) findViewById(R.id.lrv_list);
        list.setAdapter(new PlayerAdapter(this, getDatas(), R.layout.adapter_player, player));
        list.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                AdapterPlayer aplayer = ((CommonHolder) holder).getView(R.id.aplayer);
                aplayer.recycle(true);
            }
        });
    }

    private void initPlayer() {
        player = new CommenPlayer(this);
        player.setLive(false);
        player.setOnNetListener(new OnNetListener() {
            @Override
            public void onIgnoreMobileNet() {
                ignoreNet = true;
            }
        }).setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onLoading() {
                player.getControl().setState(ControlLayout.STATE_LOADING);
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                player.getControl().setState(ControlLayout.STATE_COMPLETION);
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (!ignoreNet && NetCompat.getStatus() == NetState.CONNECTED_MOBILE) {
                    player.pause();
                    player.getControl().setState(ControlLayout.STATE_MOBILE_NET);
                } else {
                    player.getControl().setState(ControlLayout.STATE_PREPARED);
                }
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                player.getControl().setState(ControlLayout.STATE_ERROR);
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
    }

    private List<MediaModel> getDatas() {
        int[] urls = new int[]{R.string.url1, R.string.url2, R.string.url3, R.string.url4,
                R.string.url5, R.string.url6, R.string.url7, R.string.url8,};
        ArrayList<MediaModel> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MediaModel model = new MediaModel();
            model.url = getResources().getString(urls[i % urls.length]);
            datas.add(model);
        }
        return datas;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (player != null) {
            player.onPause();
        }
        super.onPause();
    }

    @Override
    public void onNetChange(int state) {
        if (isFinishing()) {
            return;
        }
        ULog.d("dsiner: Network state--> " + state);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            container.setVisibility(View.VISIBLE);
            itemContainer = (ViewGroup) player.getParent();
            Util.peelInject(player, container);
        } else {
            container.setVisibility(View.GONE);
            Util.peelInject(player, itemContainer);
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
    public void finish() {
        if (player != null) {
            player.onDestroy();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        NetBus.getIns().removeListener(this);
        super.onDestroy();
    }
}
