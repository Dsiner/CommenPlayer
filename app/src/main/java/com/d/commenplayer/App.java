package com.d.commenplayer;

import android.app.Application;

import com.d.lib.common.component.network.NetworkCompat;

/**
 * App
 * Created by D on 2018/9/22.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkCompat.init(getApplicationContext());
    }
}
