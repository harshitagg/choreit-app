package com.pineapps.choreit;

import android.app.Application;

public class ChoreItApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ChoreItContext context = ChoreItContext.getInstance();
        context.updateApplicationContext(this);
    }
}
