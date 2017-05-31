package com.heniktechnology.callremider.register_user;

import android.content.BroadcastReceiver;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.heniktechnology.callremider.R;
import com.heniktechnology.callremider.pojo.UserRegister;
import com.heniktechnology.hncore.utility.HNLoger;

public class RegisterActivity extends AppCompatActivity {
    String TAG = AppCompatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        HNLoger.debug(TAG,"Working proparly");

        UserRegister userRegister = new UserRegister();
        userRegister.setUserFirstName("FIRST");
        userRegister.setUserLastName("LAST");
        userRegister.setUserLoginId("LOGINID");
        userRegister.setUserPassword("PASSWORD");

        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("xxx.db").create();
        ActiveAndroid.initialize(dbConfiguration);



    }
}
