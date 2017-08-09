package com.d.iplayer.net;

/**
 * 网络状态
 * Created by D on 2017/8/28.
 */
public class NetEvent {
    /**
     * 当前网络状态  <=1：无网络 2：移动网络 3：Wifi
     */
    public int status;

    public NetEvent(int status) {
        this.status = status;
    }
}
