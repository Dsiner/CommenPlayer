package com.d.iplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.d.commenplayer.CommenPlayer;
import com.d.commenplayer.adapter.AdapterPlayer;
import com.d.commenplayer.listener.OnShowThumbnailListener;
import com.d.iplayer.R;
import com.d.iplayer.model.PlayerModel;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;

import java.util.List;

public class PlayerAdapter extends CommonAdapter<PlayerModel> {
    private CommenPlayer player;

    public PlayerAdapter(Context context, List<PlayerModel> datas, int layoutId, CommenPlayer player) {
        super(context, datas, layoutId);
        this.player = player;
    }

    @Override
    public void convert(int position, CommonHolder holder, PlayerModel item) {
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
