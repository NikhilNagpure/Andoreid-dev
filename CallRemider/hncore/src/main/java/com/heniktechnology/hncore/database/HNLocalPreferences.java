package com.heniktechnology.hncore.database;

import android.content.Context;
import android.content.SharedPreferences;

public class HNLocalPreferences
{
	private static String PREFS_NAME = "prefs_name";

	public static String KEY_ROW_ID = "RowId";

	public static String KEY_CURRENT_MINUTES = "CurrentMinutes";

	public static final String KEY_CLIENT_ID = "clientId";

	public static boolean setValueInt(Context ctx, String key, int value)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
		return true;

	}

	public static int getValueInt(Context ctx, String key)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getInt(key, 0);

	}

	public static boolean setValueBoolean(Context ctx, String key, boolean value)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;

	}

	public static boolean getValueBoolean(Context ctx, String key)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key, false);

	}

	public static boolean setValueString(Context ctx, String key, String value)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
		return true;

	}

	public static String getValueString(Context ctx, String key)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getString(key,null);

	}

	public static boolean setValueLong(Context ctx, String key, long value)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		editor.commit();
		return true;

	}

	public static long getValueLong(Context ctx, String key)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getLong(key, 0);

	}


	public static boolean Contains(Context ctx, String key)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.contains(key);

	}

	public static boolean Remove(Context ctx, String key)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		return editor.commit();
	}

}
