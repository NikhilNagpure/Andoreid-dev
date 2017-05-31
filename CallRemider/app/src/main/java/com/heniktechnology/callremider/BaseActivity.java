package com.heniktechnology.callremider;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.heniktechnology.hncore.utility.HNLoger;

/**
 * Created by NikhilNagpure on 31-05-2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(ApplicationController.ENABLE_LOGS)
        {
            HNLoger.enableLogging(1);
        }
        else
        {
            HNLoger.enableLogging(0);
        }
    }
}
