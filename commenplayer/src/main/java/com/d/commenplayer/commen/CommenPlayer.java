package com.d.commenplayer.commen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.d.commenplayer.R;
import com.d.commenplayer.listener.IPlayListener;
import com.d.commenplayer.listener.IPlayer;
import com.d.commenplayer.listener.IPlayerListener;
import com.d.commenplayer.listener.OnNetChangeListener;
import com.d.commenplayer.media.IjkVideoView;
import com.d.commenplayer.util.Constans;
import com.d.commenplayer.util.NetChangeReceiver;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * CommenPlayer
 * Created by D on 2017/5/27.
 */
public class CommenPlayer extends RelativeLayout implements IPlayer, View.OnClickListener {
    private Activity activity;
    private IjkVideoView player;
    private Depositer depositer;
    private NetChangeReceiver netChangeReceiver;
    private boolean isListenNetChange;//是否监听网络连接状态变化
    private OnNetChangeListener netChangeListener;//网络监听器
    private IPlayListener listener;
    private String url;

    public CommenPlayer(Context context) {
        super(context);
        init(context);
    }

    public CommenPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommenPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommenPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        activity = (Activity) context;
        View root = LayoutInflater.from(context).inflate(R.layout.layout_player, this);
        View loading = root.findViewById(R.id.loading);
        View control = root.findViewById(R.id.control);
        View fullscreen = root.findViewById(R.id.fullscreen);
        player = (IjkVideoView) root.findViewById(R.id.ijkplayer);
        depositer = new Depositer(loading, control, player);
        player.setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                depositer.setPlayerVisibility(GONE, VISIBLE, GONE);
                depositer.setControl("播放完毕,是否重新播放？", "重新播放", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        depositer.setPlayerVisibility(VISIBLE, GONE, GONE);
                        play(url);
                    }
                });
                if (listener != null) {
                    listener.onCompletion(mp);
                }
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (isListenNetChange && Constans.NET_STATUS == Constans.CONNECTED_MOBILE) {
                    player.pause();
                    depositer.setPlayerVisibility(GONE, VISIBLE, GONE);
                    depositer.setControl("当前为移动网络，是否继续播放？", "继续播放", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            depositer.setPlayerVisibility(GONE, GONE, VISIBLE);
                            player.start();
                        }
                    });
                } else {
                    player.pause();
                    depositer.setPlayerVisibility(GONE, VISIBLE, GONE);
                    depositer.setControl("视频已加载，是否开始播放？", "开始播放", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            depositer.setPlayerVisibility(GONE, GONE, VISIBLE);
                            player.start();
                        }
                    });
                }
                if (listener != null) {
                    listener.onPrepared(mp);
                }
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                depositer.setPlayerVisibility(GONE, VISIBLE, GONE);
                depositer.setControl("播放失败，是否重试？", "重试", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        depositer.setPlayerVisibility(VISIBLE, GONE, GONE);
                        play(url);
                    }
                });
                if (listener != null) {
                    listener.onError(mp, what, extra);
                }
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (listener != null) {
                    listener.onInfo(mp, what, extra);
                }
                return false;
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                if (listener != null) {
                    listener.onVideoSizeChanged(mp, width, height, sarNum, sarDen);
                }
            }
        });
        fullscreen.setOnClickListener(this);
        registerNetReceiver();
    }

    /**
     * 注册网络监听器
     */
    private void registerNetReceiver() {
        if (netChangeReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            netChangeReceiver = new NetChangeReceiver(activity);
            netChangeReceiver.setNetChangeListener(netChangeListener);
            activity.registerReceiver(netChangeReceiver, filter);
        }
    }

    /**
     * 销毁网络监听器
     */
    private void unRegisterNetReceiver() {
        if (netChangeReceiver != null) {
            activity.unregisterReceiver(netChangeReceiver);
            netChangeReceiver = null;
        }
    }

    @Override
    public void setLive(boolean live) {
        player.setLive(live);
    }

    @Override
    public void setScaleType(int scaleType) {
        player.setScaleType(scaleType);
    }

    @Override
    public void play(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        this.url = url;
        player.setVideoPath(url);
        player.start();
        depositer.setPlayerVisibility(VISIBLE, GONE, GONE);
        if (listener != null) {
            listener.onLoading();
        }
    }

    @Override
    public void seekTo(int time) {
        player.seekTo(time);
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int toggleAspectRatio() {
        return player.toggleAspectRatio();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void toggleOrientation() {
        if (Depositer.getScreenOrientation(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onResume() {
        player.onResume();
    }

    @Override
    public void onPause() {
        player.onPause();
    }

    @Override
    public boolean onBackPress() {
        if (Depositer.getScreenOrientation(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        unRegisterNetReceiver();
        player.onDestroy();
    }

    public CommenPlayer setListenNetChange(boolean isListenNetChange) {
        this.isListenNetChange = isListenNetChange;
        return this;
    }

    public CommenPlayer setNetChangeListener(OnNetChangeListener netChangeListener) {
        this.netChangeListener = netChangeListener;
        if (netChangeReceiver != null) {
            netChangeReceiver.setNetChangeListener(netChangeListener);
        }
        return this;
    }

    public CommenPlayer setOnPlayListener(IPlayListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fullscreen) {
            toggleOrientation();
        }
    }
}
