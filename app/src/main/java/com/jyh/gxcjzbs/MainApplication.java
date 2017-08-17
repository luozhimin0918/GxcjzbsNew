package com.jyh.gxcjzbs;

import android.app.Application;

import com.socks.library.KLog;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(BuildConfig.LOG_DEBUG);
    }
}
