/**
 * *****************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p/>
 * Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * This is proprietary information of Mobicule Technologies Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 * *****************************************************************************
 *
 * @project MGL_LastMile
 */
package com.heniktechnology.hncore.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by NikhilNagpure on 09-08-2016.
 */
public class HNConnectionDetector {

    public static boolean isConnectedToInternet(final Context context) {

        return true;
    }


    private static boolean isConnectionChecker(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null)
            {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }




}
