package com.d.commenplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.d.commenplayer.R;
import com.d.commenplayer.listener.IMediaPlayerControl;
import com.d.commenplayer.util.Util;

public class ControlLayout extends RelativeLayout {
    public final static int STATE_LOADING = 0;
    public final static int STATE_PREPARED = 1;
    public final static int STATE_MOBILE_NET = 2;
    public final static int STATE_COMPLETION = 3;
    public final static int STATE_ERROR = 4;

    /**
     * bottom
     */
    private ImageView playPause;
    private TextView current;
    private SeekBar seekBar;
    private TextView total;
    private ImageView fullscreen;

    /**
     * center-loading
     */
    private ProgressBar loading;

    /**
     * center-tips
     */
    private LinearLayout tips;
    private TextView tipsText;
    private TextView tipsBtn;

    private int duration;
    private IMediaPlayerControl listener;
    private boolean isPrepare = true;
    private boolean isSupportGesture = true;
    private boolean portrait = false;

    public ControlLayout(Context context) {
        this(context, null);
    }

    public ControlLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_control, this);
        initView(root);
        playPause.setOnClickListener(onClickListener);
        fullscreen.setOnClickListener(onClickListener);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                current.setText(Util.generateTime(Util.getPosition(progress, duration)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (listener != null) {
                    listener.lockProgress(true);//加锁
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (listener != null) {
                    listener.seekTo(Util.getPosition(seekBar.getProgress(), duration));
                    listener.lockProgress(false);//释放锁
                }
            }
        });
    }

    private void initView(View root) {
        //bottom
        playPause = (ImageView) root.findViewById(R.id.iv_player_play_pause);
        current = (TextView) root.findViewById(R.id.tv_player_current);
        seekBar = (SeekBar) root.findViewById(R.id.seek_player_progress);
        seekBar.setMax(Util.SEEKBAR_MAX);
        total = (TextView) root.findViewById(R.id.tv_player_total);
        fullscreen = (ImageView) root.findViewById(R.id.iv_player_fullscreen);

        //center-loading
        loading = (ProgressBar) root.findViewById(R.id.prb_player_loading);

        //center-tips
        tips = (LinearLayout) root.findViewById(R.id.layout_player_tips);
        tipsText = (TextView) root.findViewById(R.id.tv_player_tips_text);
        tipsBtn = (TextView) root.findViewById(R.id.tv_player_tips_btn);
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                setPlayerVisibility(VISIBLE, GONE, GONE);
                break;
            case STATE_PREPARED:
                setPlayerVisibility(GONE, GONE, VISIBLE);
                setControlVisibility(listener == null || listener.isLive() ? INVISIBLE : VISIBLE);
                break;
            case STATE_MOBILE_NET:
                setPlayerVisibility(GONE, VISIBLE, GONE);
                setControlVisibility(INVISIBLE);
                setControl("当前为移动网络，是否继续播放？", "继续播放", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPlayerVisibility(GONE, GONE, VISIBLE);
                        setControlVisibility(listener == null || listener.isLive() ? INVISIBLE : VISIBLE);
                        if (listener != null) {
                            listener.start();
                        }
                    }
                });
                break;
            case STATE_COMPLETION:
                setPlayerVisibility(GONE, VISIBLE, GONE);
                setControlVisibility(INVISIBLE);
                setControl("播放结束，是否重新播放？", "重新播放", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPlayerVisibility(VISIBLE, GONE, GONE);
                        if (listener != null) {
                            listener.play(listener.getUrl());
                        }
                    }
                });
                break;
            case STATE_ERROR:
                setPlayerVisibility(GONE, VISIBLE, GONE);
                setControlVisibility(INVISIBLE);
                setControl("播放失败，是否重试？", "重试", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPlayerVisibility(VISIBLE, GONE, GONE);
                        if (listener != null) {
                            listener.play(listener.getUrl());
                        }
                    }
                });
                break;
        }
    }

    private void setPlayerVisibility(int visibility0, int visibility1, int visibility2) {
        loading.setVisibility(visibility0);
        tips.setVisibility(visibility1);
        if (listener != null) {
            listener.setPlayerVisibility(visibility2);
        }
    }

    private void setControlVisibility(int visibility) {
        playPause.setVisibility(visibility);
        current.setVisibility(visibility);
        seekBar.setVisibility(visibility);
        total.setVisibility(visibility);
        fullscreen.setVisibility(visibility);
    }

    private void setControl(String text, String button, View.OnClickListener l) {
        tips.setVisibility(VISIBLE);
        tipsText.setText(text);
        tipsText.setVisibility(View.VISIBLE);
        tipsBtn.setText(button);
        tipsBtn.setVisibility(View.VISIBLE);
        tipsBtn.setOnClickListener(l);
    }

    public void setProgress(int position, int duration, int bufferPercentage) {
        position = Math.min(position, duration);
        position = Math.max(position, 0);
        if (duration > 0) {
            seekBar.setProgress(Util.getProgress(position, duration));
            seekBar.setSecondaryProgress(Util.getSecondaryProgress(bufferPercentage));
            current.setText(Util.generateTime(position));
            total.setText(Util.generateTime(duration));
            this.duration = duration;
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_player_play_pause) {
                if (listener == null) {
                    return;
                }
                if (listener.isPlaying()) {
                    listener.pause();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_play));
                } else {
                    listener.start();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
                }
            } else if (id == R.id.iv_player_fullscreen) {
                if (listener != null) {
                    listener.toggleOrientation();
                }
            }
        }
    };

    public void setIMediaPlayerControl(IMediaPlayerControl l) {
        this.listener = l;
    }
}
