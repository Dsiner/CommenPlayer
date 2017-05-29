package com.d.commenplayer.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.d.commenplayer.listener.OnNetChangeListener;

/**
 * NetChangeReceiver
 * Created by D on 2017/5/28.
 */
public class NetChangeReceiver extends BroadcastReceiver {
    private OnNetChangeListener netChangeListener;

    public NetChangeReceiver(Context context) {
        if (context != null) {
            resetNetStatus(context.getApplicationContext(), false);
        }
    }

    public void setNetChangeListener(OnNetChangeListener netChangeListener) {
        this.netChangeListener = netChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        resetNetStatus(context, true);
    }

    private void resetNetStatus(Context context, boolean listener) {
        int networkType = getNetworkType(context.getApplicationContext());
        switch (networkType) {
            case 1:
                if (Constans.NET_STATUS != Constans.UN_CONNECTED) {
                    Constans.NET_STATUS = Constans.UN_CONNECTED;
                    if (listener && netChangeListener != null) {
                        netChangeListener.onUnConnected();
                    }
                }
                break;
            case 2:
            case 4:
                if (Constans.NET_STATUS != Constans.CONNECTED_MOBILE) {
                    Constans.NET_STATUS = Constans.CONNECTED_MOBILE;
                    if (listener && netChangeListener != null) {
                        netChangeListener.onMobile();
                    }
                }
                break;
            case 3:
                if (Constans.NET_STATUS != Constans.CONNECTED_WIFI) {
                    Constans.NET_STATUS = Constans.CONNECTED_WIFI;
                    if (listener && netChangeListener != null) {
                        netChangeListener.onWifi();
                    }
                }
                break;
            default:
                if (Constans.NET_STATUS != Constans.NO_AVAILABLE) {
                    Constans.NET_STATUS = Constans.NO_AVAILABLE;
                    if (listener && netChangeListener != null) {
                        netChangeListener.onNoAvailable();
                    }
                }
                break;
        }
    }

    /**
     * 判断当前网络类型-1为未知网络0为没有网络连接1网络断开或关闭2为以太网3为WiFi4为2G5为3G6为4G
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            /** 没有任何网络 */
            return 0;
        }
        if (!networkInfo.isConnected()) {
            /** 网络断开或关闭 */
            return 1;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            /** 以太网网络 */
            return 2;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            /** wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接 */
            return 3;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            /** 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭 */
            switch (networkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    /** 2G网络 */
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    /** 3G网络 */
                case TelephonyManager.NETWORK_TYPE_LTE:
                    /** 4G网络 */
                    return 4;
            }
        }
        /** 未知网络 */
        return -1;
    }
}
