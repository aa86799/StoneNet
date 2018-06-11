package com.stone.stonenet.rx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.stone.stonenet.MainApplication;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/11 18 39
 */
public class NetInfoUtil {

    /**
     * 检查网络是否可用
     * @return true/false
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager conn = (ConnectivityManager) MainApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn == null) return false;
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) return false;
        return true;
    }
}
