package com.coremacasia.learnat;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class LearnAt extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
