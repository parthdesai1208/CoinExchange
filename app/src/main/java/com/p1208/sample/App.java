package com.p1208.sample;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        SessionManager.getInstance().initSessionManager(this.getApplicationContext());
    }

}
