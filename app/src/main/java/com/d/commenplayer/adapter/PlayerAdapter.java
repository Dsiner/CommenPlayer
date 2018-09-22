package com.d.commenplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.d.commenplayer.R;
import com.d.commenplayer.model.MediaModel;
import com.d.lib.commenplayer.CommenPlayer;
import com.d.lib.commenplayer.adapter.AdapterPlayer;
import com.d.lib.commenplayer.listener.OnShowThumbnailListener;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;

import java.util.List;

public class PlayerAdapter extends CommonAdapter<MediaModel> {
    private CommenPlayer player;

    public PlayerAdapter(Context context, List<MediaModel> datas, int layoutId, CommenPlayer player) {
        super(context, datas, layoutId);
        this.player = player;
    }

    @Override
    public void convert(int position, CommonHolder holder, MediaModel item) {
        AdapterPlayer aPlayer = holder.getView(R.id.aplayer);
        aPlayer.setLive(false);
        aPlayer.setUrl(item.url);
        aPlayer.setThumbnail(new OnShowThumbnailListener() {
            @Override
            public void onShowThumbnail(ImageView ivThumbnail) {
                ivThumbnail.setBackgroundColor(Color.parseColor("#303F9F"));
            }
        });
        aPlayer.with(player);
    }
}
