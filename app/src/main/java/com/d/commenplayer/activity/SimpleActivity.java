package com.d.commenplayer.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.d.commenplayer.R;
import com.d.lib.commenplayer.CommenPlayer;
import com.d.lib.commenplayer.listener.IPlayerListener;
import com.d.lib.commenplayer.listener.OnNetworkListener;
import com.d.lib.commenplayer.util.ULog;
import com.d.lib.commenplayer.util.Util;
import com.d.lib.commenplayer.widget.ControlLayout;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.component.network.NetworkBus;
import com.d.lib.common.component.network.NetworkCompat;
import com.d.lib.common.component.statusbarcompat.StatusBarCompat;
import com.d.lib.common.util.NetworkUtils;
import com.d.lib.common.util.ViewHelper;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class SimpleActivity extends BaseActivity<MvpBasePresenter<MvpView>>
        implements View.OnClickListener {
    private CommenPlayer player;
    private boolean mIgnoreMobileData;
    private NetworkBus.OnNetworkTypeChangeListener mOnNetworkTypeChangeListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_history:
                startActivity(new Intent(SimpleActivity.this, ListActivity.class));
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_simple;
    }

    @Override
    public MvpBasePresenter<MvpView> getPresenter() {
        return null;
    }

    @Override
    protected MvpView getMvpView() {
        return null;
    }

    @Override
    protected void bindView() {
        player = (CommenPlayer) findViewById(R.id.player);

        ViewHelper.setOnClickListener(this, this, R.id.btn_view_history);
    }

    @Override
    protected void init() {
        StatusBarCompat.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.colorBlack));
        initPlayer();
        mOnNetworkTypeChangeListener = new NetworkBus.OnNetworkTypeChangeListener() {
            @Override
            public void onNetworkTypeChange(NetworkUtils.NetworkType networkType) {
                if (isFinishing()) {
                    return;
                }
                ULog.d("dsiner: Network state--> " + networkType);
            }
        };
        NetworkBus.getInstance().addListener(mOnNetworkTypeChangeListener);
    }

    private void initPlayer() {
        player.setLive(false);
        player.setOnNetListener(new OnNetworkListener() {
            @Override
            public void onIgnoreMobileData() {
                mIgnoreMobileData = true;
            }
        });
        player.setOnPlayerListener(new IPlayerListener() {
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
                if (!mIgnoreMobileData
                        && NetworkCompat.isMobileDataType(NetworkCompat.getType())) {
                    player.pause();
                    player.getControl().setState(ControlLayout.STATE_MOBILE_DATA);
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
        player.play(getResources().getString(R.string.url1));
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
        final ViewGroup.LayoutParams lp = player.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
                ? ViewGroup.LayoutParams.MATCH_PARENT
                : Util.dp2px(getApplicationContext(), 210);
        player.setLayoutParams(lp);
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
        NetworkBus.getInstance().removeListener(mOnNetworkTypeChangeListener);
        super.onDestroy();
    }
}
