package com.example.senon.nancyclass.base;

import android.app.Application;
import android.content.Context;


public class BaseApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        //  初始化多dex配置，放置三方包dex文件生成超过65536
//        MultiDex.install(base);
//    }
}
