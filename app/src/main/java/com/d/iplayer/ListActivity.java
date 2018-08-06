package com.d.iplayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.d.commenplayer.CommenPlayer;
import com.d.commenplayer.adapter.AdapterPlayer;
import com.d.commenplayer.listener.IPlayerListener;
import com.d.commenplayer.listener.OnNetListener;
import com.d.commenplayer.ui.ControlLayout;
import com.d.commenplayer.util.MLog;
import com.d.commenplayer.util.MUtil;
import com.d.iplayer.adapter.PlayerAdapter;
import com.d.iplayer.model.PlayerModel;
import com.d.iplayer.net.NetConstans;
import com.d.iplayer.net.NetEvent;
import com.d.lib.xrv.LRecyclerView;
import com.d.lib.xrv.adapter.CommonHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ListActivity extends Activity {

    private Context context;
    private LRecyclerView list;
    private ViewGroup itemContainer;
    private FrameLayout container;
    private CommenPlayer player;
    private boolean ignoreNet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ListActivity.this;
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_list);
        player = new CommenPlayer(context);
        initPlayer();
        list = (LRecyclerView) findViewById(R.id.lrv_list);
        container = (FrameLayout) findViewById(R.id.player_container);
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
                if (!ignoreNet && NetConstans.NET_STATUS == NetConstans.CONNECTED_MOBILE) {
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

    private List<PlayerModel> getDatas() {
        int[] urls = new int[]{R.string.url1, R.string.url2, R.string.url3, R.string.url4,
                R.string.url5, R.string.url6, R.string.url7, R.string.url8,};
        ArrayList<PlayerModel> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PlayerModel model = new PlayerModel();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            container.setVisibility(View.VISIBLE);
            itemContainer = (ViewGroup) player.getParent();
            MUtil.peelInject(player, container);
        } else {
            container.setVisibility(View.GONE);
            MUtil.peelInject(player, itemContainer);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetEvent(NetEvent event) {
        if (event == null || isFinishing()) {
            return;
        }
        MLog.d("dsiner: Net_" + event.status);
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
