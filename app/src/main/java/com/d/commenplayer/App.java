package com.d.commenplayer;

import android.app.Application;

import com.d.lib.common.component.netstate.NetCompat;

/**
 * App
 * Created by D on 2018/9/22.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetCompat.init(getApplicationContext());
    }
}
