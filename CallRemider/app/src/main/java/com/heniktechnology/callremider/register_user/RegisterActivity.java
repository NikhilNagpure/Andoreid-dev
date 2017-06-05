package com.heniktechnology.callremider.register_user;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.heniktechnology.callremider.BaseActivity;
import com.heniktechnology.callremider.R;
import com.heniktechnology.callremider.pojo.UserRegister;
import com.heniktechnology.hncore.dynamic_data_base.ActiveAndroid;
import com.heniktechnology.hncore.dynamic_data_base.query.Select;
import com.heniktechnology.hncore.utility.HNLoger;

import java.util.List;

public class RegisterActivity extends BaseActivity {
    String TAG = AppCompatActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActiveAndroid.initialize(this);

        HNLoger.debug(TAG,"Working proparly");

        UserRegister userRegister = new UserRegister();
        userRegister.setUserFirstName("FIRST");
        userRegister.setUserLastName("LAST");
        userRegister.setUserLoginId("LOGINID");
        userRegister.setUserPassword("PASSWORD");
        ActiveAndroid.beginTransaction();
        userRegister.save();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();


        List<UserRegister> userRegisters = getAll();


        for (int i = 0; i <userRegisters.size() ; i++) {
           HNLoger.debug(TAG,"userRegisters.get(i).toString(); = " + userRegisters.get(i).toString());
        }




   /*     Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("xxx.db").create();
        ActiveAndroid.initialize(dbConfiguration);*/



    }

    public  List<UserRegister> getAll()
    {
        return new Select()
                .from(UserRegister.class)
                .execute();
    }
}
