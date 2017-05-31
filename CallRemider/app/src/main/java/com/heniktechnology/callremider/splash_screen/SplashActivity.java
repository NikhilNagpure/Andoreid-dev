package com.heniktechnology.callremider.splash_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.heniktechnology.callremider.ApplicationController;
import com.heniktechnology.callremider.BaseActivity;
import com.heniktechnology.callremider.R;
import com.heniktechnology.callremider.home_screen.HomeScreenActivity;
import com.heniktechnology.callremider.register_user.RegisterActivity;

public class SplashActivity extends BaseActivity {

    private Handler handler;
    private Runnable runnable;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        handler.postDelayed(runnable, ApplicationController.SPLASH_TIME);

    }

    private void init() {
        context = this;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(context, RegisterActivity.class);
                context.startActivity(homeIntent);
            }
        };
    }
}
