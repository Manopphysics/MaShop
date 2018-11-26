package com.example.manop.mashop;


import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;

/**
 * Created by Manop on 11/2/2018.
 */

public class MyApplication extends Application {
    public static final String  CHANNEL_1_ID = "chat_channel";
    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        MyApplication.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= 26) {
            NotificationChannel chat_channel = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel_chat",
                    NotificationManager.IMPORTANCE_HIGH
            );
            chat_channel.setDescription("New Chat!");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chat_channel);
        }
    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
