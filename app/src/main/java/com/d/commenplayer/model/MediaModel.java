package com.d.commenplayer.model;

import android.content.Context;

import com.d.commenplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MediaModel {
    public String url = "http://vpls.cdn.videojj.com/scene/video02_720p.mp4";
    public int position;

    public static List<MediaModel> getDatas(Context context, int size) {
        final int[] urlResIds = new int[]{
                R.string.url1,
                R.string.url2,
                R.string.url3,
                R.string.url4,
                R.string.url5,
                R.string.url6,
                R.string.url7,
                R.string.url8,};
        final List<MediaModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MediaModel model = new MediaModel();
            model.url = context.getResources().getString(urlResIds[i % urlResIds.length]);
            list.add(model);
        }
        return list;
    }
}
