package com.heniktechnology.hncore.utility;

/**
 * Created by NikhilNagpure on 16-01-2017.
 */

public class HNCoreFunction {
    public static boolean checkNullAndEmpty(Object object)
    {
        try {

                if (object != null && !object.toString().trim().equals(HNCoreConstants.EMPTY_STRING)) {
                    return true;
                }
                return false;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }


}
