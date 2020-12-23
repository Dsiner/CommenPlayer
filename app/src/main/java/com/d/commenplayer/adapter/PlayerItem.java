package com.d.commenplayer.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.d.lib.commenplayer.CommenPlayer;
import com.d.lib.commenplayer.R;
import com.d.lib.commenplayer.listener.OnShowThumbnailListener;

public class PlayerItem extends FrameLayout {
    private FrameLayout fl_thumbnail;
    private ImageView iv_thumbnail;
    private ImageView iv_thumbnail_play;

    private CommenPlayer mPlayer;
    private boolean mIsLive;
    private String mUrl;

    public PlayerItem(Context context) {
        super(context);
        init(context);
    }

    public PlayerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public static void peelInject(CommenPlayer player, ViewGroup root) {
        if (player == null || root == null || player.getParent() == root) {
            return;
        }
        if (player.getParent() != null) {
            if (player.getParent() instanceof PlayerItem) {
                ((PlayerItem) player.getParent()).recycle(false);
            } else {
                ((ViewGroup) player.getParent()).removeView(player);
            }
        }
        if (root instanceof PlayerItem) {
            ((PlayerItem) root).inject();
        } else {
            root.removeView(player);
            root.addView(player, 0);
        }
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.lib_player_layout_aplayer, this);
        initView(root);
    }

    protected void initView(View root) {
        fl_thumbnail = (FrameLayout) root.findViewById(R.id.fl_thumbnail);
        iv_thumbnail = (ImageView) root.findViewById(R.id.iv_thumbnail);
        iv_thumbnail_play = (ImageView) root.findViewById(R.id.iv_thumbnail_play);
        fl_thumbnail.setVisibility(VISIBLE);
        iv_thumbnail_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer == null) {
                    return;
                }
                mPlayer.pause();
                peelInject(mPlayer, PlayerItem.this);
                fl_thumbnail.setVisibility(GONE);
                mPlayer.play(mUrl);
            }
        });
    }

    public PlayerItem setThumbnail(OnShowThumbnailListener listener) {
        if (listener != null) {
            listener.onShowThumbnail(iv_thumbnail);
        }
        return this;
    }

    public void with(CommenPlayer player) {
        this.mPlayer = player;
    }

    public void inject() {
        fl_thumbnail.setVisibility(GONE);
        if (mPlayer != null) {
            removeView(mPlayer);
            addView(mPlayer, 0);
        }
    }

    public void recycle(boolean pause) {
        fl_thumbnail.setVisibility(VISIBLE);
        if (mPlayer != null) {
            if (pause && getChildAt(0) == mPlayer) {
                mPlayer.pause();
            }
            removeView(mPlayer);
        }
    }

    public void setLive(boolean live) {
        this.mIsLive = live;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
}
