package com.example.manop.mashop;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Manop on 11/2/2018.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
