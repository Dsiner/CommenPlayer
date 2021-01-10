package com.d.commenplayer.activity;

import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.d.commenplayer.R;
import com.d.commenplayer.adapter.PlayerAdapter;
import com.d.commenplayer.adapter.PlayerItem;
import com.d.commenplayer.model.MediaModel;
import com.d.lib.commenplayer.CommenPlayer;
import com.d.lib.commenplayer.listener.IPlayerListener;
import com.d.lib.commenplayer.listener.OnNetworkListener;
import com.d.lib.commenplayer.util.ULog;
import com.d.lib.commenplayer.widget.ControlLayout;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.component.network.NetworkBus;
import com.d.lib.common.component.network.NetworkCompat;
import com.d.lib.common.util.NetworkUtils;
import com.d.lib.common.widget.TitleLayout;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ListActivity extends BaseActivity<MvpBasePresenter<MvpView>> {
    private ViewGroup mItemContainer;
    private TitleLayout tl_title;
    private RecyclerView rv_list;
    private FrameLayout player_container;

    private CommenPlayer mPlayer;
    private boolean mIgnoreMobileData;
    private NetworkBus.OnNetworkTypeChangeListener mOnNetworkTypeChangeListener;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_list;
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
        tl_title = (TitleLayout) findViewById(R.id.tl_title);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        player_container = (FrameLayout) findViewById(R.id.player_container);
    }

    @Override
    protected void init() {
        initPlayer();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setHasFixedSize(true);
        rv_list.setAdapter(new PlayerAdapter(this,
                MediaModel.getDatas(this, 20),
                R.layout.adapter_player, mPlayer));
        rv_list.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                PlayerItem aplayer = ((CommonHolder) holder).getView(R.id.aplayer);
                aplayer.recycle(true);
            }
        });

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
        mPlayer = new CommenPlayer(this);
        mPlayer.setLive(false);
        mPlayer.setOnNetListener(new OnNetworkListener() {
            @Override
            public void onIgnoreMobileData() {
                mIgnoreMobileData = true;
            }
        });
        mPlayer.setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onLoading() {
                mPlayer.getControl().setState(ControlLayout.STATE_LOADING);
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                mPlayer.getControl().setState(ControlLayout.STATE_COMPLETION);
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (!mIgnoreMobileData
                        && NetworkCompat.isMobileDataType(NetworkCompat.getType())) {
                    mPlayer.pause();
                    mPlayer.getControl().setState(ControlLayout.STATE_MOBILE_DATA);
                } else {
                    mPlayer.getControl().setState(ControlLayout.STATE_PREPARED);
                }
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                mPlayer.getControl().setState(ControlLayout.STATE_ERROR);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer != null) {
            mPlayer.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (mPlayer != null) {
            mPlayer.onPause();
        }
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player_container.setVisibility(View.VISIBLE);
            mItemContainer = (ViewGroup) mPlayer.getParent();
            PlayerItem.peelInject(mPlayer, player_container);
        } else {
            player_container.setVisibility(View.GONE);
            PlayerItem.peelInject(mPlayer, mItemContainer);
        }
        if (mPlayer != null) {
            mPlayer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPlayer != null && mPlayer.onBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        if (mPlayer != null) {
            mPlayer.onDestroy();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        NetworkBus.getInstance().removeListener(mOnNetworkTypeChangeListener);
        super.onDestroy();
    }
}
