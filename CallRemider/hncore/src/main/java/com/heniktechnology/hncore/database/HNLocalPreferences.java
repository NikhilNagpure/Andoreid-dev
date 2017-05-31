package com.heniktechnology.hncore.database;

import android.content.Context;
import android.content.SharedPreferences;

public class HNLocalPreferences {
    public static final String KEY_CLIENT_ID = "clientId";
    public static String KEY_ROW_ID = "RowId";

    public static String KEY_CURRENT_MINUTES = "CurrentMinutes";
    private static String PREFS_NAME = "prefs_name";

    public static boolean setValueInt(Context ctx, String key, int value) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(key, value);
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static int getValueInt(Context ctx, String key)
    {
        return getValueInt( ctx,  key, 0);
    }
    public static int getValueInt(Context ctx, String key, Integer defaultValue) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean setValueBoolean(Context ctx, String key, boolean value) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(key, value);
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean getValueBoolean(Context ctx, String key) {
        return getValueBoolean(ctx, key, false);
    }

    public static boolean getValueBoolean(Context ctx, String key, Boolean defaultValue) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;

    }

    public static boolean setValueString(Context ctx, String key, String value) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static String getValueString(Context ctx, String key) {
        return getValueString(ctx, key, null);
    }

    public static String getValueString(Context ctx, String key, String defaultValue) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean setValueLong(Context ctx, String key, long value) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value);
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static long getValueLong(Context ctx, String key) {
        return getValueLong(ctx, key, 0l);
    }

    public static long getValueLong(Context ctx, String key, Long defaultValue) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getLong(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;

    }


    public static boolean Contains(Context ctx, String key) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean Remove(Context ctx, String key) {
        try {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(key);
            return editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
