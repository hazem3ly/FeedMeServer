package com.neway.feedmeserver.model;

import android.app.Application;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class App extends Application {

    private static App instance;
    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static App getInstance() {
        return instance;
    }
}
