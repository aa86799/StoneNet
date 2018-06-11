package com.stone.stonenet.util;

import android.os.Handler;
import android.widget.Toast;

import com.stone.stonenet.BuildConfig;
import com.stone.stonenet.MainApplication;

public class ShowUtils {

    /**
     * 显示Toast
     *
     * @param message
     */
    private static Toast toast;

    public static void toast(String message) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(MainApplication.getInstance(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示Toast
     *
     * @param resID
     */
    public static void toast(int resID) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(MainApplication.getInstance(), resID, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void toastTest(String message) {
        if (BuildConfig.DEBUG) {
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(MainApplication.getInstance(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private static Handler handler;

    /**
     * 在主线程 显示Toast
     *
     * @param message
     */
    public static void toastMain(final String message) {
        if (handler == null) {
            handler = new Handler(MainApplication.getInstance().getMainLooper());
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(MainApplication.getInstance(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}
