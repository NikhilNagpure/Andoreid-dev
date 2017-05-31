package com.heniktechnology.hncore.utility;

/**
 * Created by NikhilNagpure on 16-01-2017.
 */

public class HNVersionChecker {

    public static int versionChecker()
    {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= android.os.Build.VERSION_CODES.FROYO) {

        } else {


        }
        return 0;
    }


}
