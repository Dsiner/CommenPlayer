package com.d.commenplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.d.commenplayer.R;
import com.d.commenplayer.model.MediaModel;
import com.d.lib.commenplayer.CommenPlayer;
import com.d.lib.commenplayer.listener.OnShowThumbnailListener;
import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;

import java.util.List;

public class PlayerAdapter extends CommonAdapter<MediaModel> {
    private CommenPlayer mPlayer;

    public PlayerAdapter(Context context, List<MediaModel> datas, int layoutId, CommenPlayer player) {
        super(context, datas, layoutId);
        this.mPlayer = player;
    }

    @Override
    public void convert(int position, CommonHolder holder, MediaModel item) {
        final PlayerItem aplayer = holder.getView(R.id.aplayer);
        aplayer.setLive(false);
        aplayer.setUrl(item.url);
        aplayer.setThumbnail(new OnShowThumbnailListener() {
            @Override
            public void onShowThumbnail(ImageView ivThumbnail) {
                ivThumbnail.setBackgroundColor(Color.parseColor("#303F9F"));
            }
        });
        aplayer.with(mPlayer);
    }
}
