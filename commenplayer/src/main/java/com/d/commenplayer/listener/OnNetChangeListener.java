package com.d.commenplayer.listener;

/**
 * OnNetChangeListener
 * Created by D on 2017/5/28.
 */
public interface OnNetChangeListener {
    // wifi
    void onWifi();

    // 手机
    void onMobile();

    // 网络断开
    void onUnConnected();

    // 网路不可用
    void onNoAvailable();
}
