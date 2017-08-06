package com.d.commenplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.d.commenplayer.listener.IMediaPlayerControl;
import com.d.commenplayer.listener.IPlayerListener;
import com.d.commenplayer.listener.OnNetChangeListener;
import com.d.commenplayer.listener.OnShowThumbnailListener;
import com.d.commenplayer.media.IjkVideoView;
import com.d.commenplayer.ui.ControlLayout;
import com.d.commenplayer.ui.TouchLayout;
import com.d.commenplayer.util.Constans;
import com.d.commenplayer.util.NetChangeReceiver;
import com.d.commenplayer.util.Util;

import java.lang.ref.WeakReference;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * CommenPlayer
 * Created by D on 2017/5/27.
 */
public class CommenPlayer extends FrameLayout implements IMediaPlayerControl {
    private Activity activity;

    private IjkVideoView player;
    private TouchLayout touchLayout;
    private ControlLayout control;
    private boolean isPortrait = true;//true:竖屏 false:横屏
    public boolean progressLock;//进度锁

    private IPlayerListener listener;
    private NetChangeReceiver netChangeReceiver;
    private OnNetChangeListener netChangeListener;//网络监听器
    private boolean isListenNetChange;//是否监听网络连接状态变化

    private boolean live;
    private String url;

    private Handler handler = new Handler();
    private ProgressTask progressTask;
    private boolean progressTaskRunning;

    private final int TASK_LOOP_TIME = 1000;

    static class ProgressTask implements Runnable {
        private final WeakReference<CommenPlayer> reference;

        ProgressTask(CommenPlayer layout) {
            this.reference = new WeakReference<CommenPlayer>(layout);
        }

        @Override
        public void run() {
            CommenPlayer layout = reference.get();
            if (layout == null || layout.getContext() == null || !layout.progressTaskRunning || layout.live) {
                return;
            }
            if (!layout.progressLock) {
                layout.progressTo(layout.getCurrentPosition(), layout.getBufferPercentage());
            }
            layout.handler.postDelayed(layout.progressTask, layout.TASK_LOOP_TIME);
        }
    }

    public void reStartProgressTask() {
        stopProgressTask();
        progressTaskRunning = true;
        handler.postDelayed(progressTask, 300);
    }

    public void stopProgressTask() {
        progressTaskRunning = false;
        handler.removeCallbacks(progressTask);
    }

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

    private void init(final Context context) {
        activity = (Activity) context;
        progressTask = new ProgressTask(this);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_player, this);
        player = (IjkVideoView) root.findViewById(R.id.ijkplayer);
        touchLayout = (TouchLayout) root.findViewById(R.id.tl_touch);
        control = (ControlLayout) root.findViewById(R.id.cl_control);
        touchLayout.setIMediaPlayerControl(this);
        control.setIMediaPlayerControl(this);
        player.setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onLoading() {
                //this func is not used here
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                if (getContext() == null || control == null) {
                    return;
                }
                control.setState(ControlLayout.STATE_COMPLETION);
                if (listener != null) {
                    listener.onCompletion(mp);
                }
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (getContext() == null || control == null) {
                    return;
                }
                if (isListenNetChange && Constans.NET_STATUS == Constans.CONNECTED_MOBILE) {
                    player.pause();
                    control.setState(ControlLayout.STATE_MOBILE_NET);
                } else {
                    control.setState(ControlLayout.STATE_PREPARED);
                }
                reStartProgressTask();
                if (listener != null) {
                    listener.onPrepared(mp);
                }
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (getContext() == null || control == null) {
                    return false;
                }
                control.setState(ControlLayout.STATE_ERROR);
                if (listener != null) {
                    listener.onError(mp, what, extra);
                }
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                if (getContext() == null || control == null) {
                    return false;
                }
                if (listener != null) {
                    listener.onInfo(mp, what, extra);
                }
                return false;
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                if (getContext() == null || control == null) {
                    return;
                }
                if (listener != null) {
                    listener.onVideoSizeChanged(mp, width, height, sarNum, sarDen);
                }
            }
        });
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
        this.live = live;
        player.setLive(live);
    }

    @Override
    public boolean isLive() {
        return live;
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
        control.setState(ControlLayout.STATE_LOADING);
        if (listener != null) {
            listener.onLoading();
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setPlayerVisibility(int visibility) {
        player.setVisibility(visibility);
    }

    @Override
    public void lockProgress(boolean lock) {
        progressLock = lock;
        if (progressLock) {
            stopProgressTask();
        } else {
            reStartProgressTask();
        }
    }

    @Override
    public void progressTo(int position, int bufferPercentage) {
        control.setProgress(position, getDuration(), bufferPercentage);
    }

    @Override
    public void seekTo(int pos) {
        pos = Math.max(pos, 0);
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return player.getBufferPercentage();
    }

    @Override
    public boolean canPause() {
        return !live;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int toggleAspectRatio() {
        return player.toggleAspectRatio();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        isPortrait = newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE;
        touchLayout.setVisibility(isPortrait ? GONE : VISIBLE);
    }

    @Override
    public void toggleOrientation() {
        if (Util.getScreenOrientation(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
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
        if (Util.getScreenOrientation(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        stopProgressTask();
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

    public CommenPlayer setOnPlayerListener(IPlayerListener listener) {
        this.listener = listener;
        return this;
    }

    public CommenPlayer setThumbnail(OnShowThumbnailListener listener) {
        if (listener != null) {
            // TODO: @Dsiner thumbnail 2017/7/17
            listener.onShowThumbnail(null);
        }
        return this;
    }
}
