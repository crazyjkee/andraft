package com.statica.andraft;

import android.app.AlarmManager;

public class GeoConstants {
	  

	  public static boolean DEVELOPER_MODE = true;
	  

	  

	  
	  public static int MAX_DISTANCE = 10;
	  //время до обновления координат
	  public static long MAX_TIME = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	  

	  public static int PASSIVE_MAX_DISTANCE = MAX_DISTANCE;

	  public static long PASSIVE_MAX_TIME = MAX_TIME;
	  
	  public static boolean USE_GPS_WHEN_ACTIVITY_VISIBLE = true;

	  public static boolean DISABLE_PASSIVE_LOCATION_WHEN_USER_EXIT = false;
	  
	  
	  
	  // Prefetching place details is useful but potentially expensive. The following
	  // values lets you disable prefetching when on mobile data or low battery conditions.
	  // Only prefetch on WIFI?
	  public static boolean PREFETCH_ON_WIFI_ONLY = false;
	  // Disable prefetching when battery is low?
	  public static boolean DISABLE_PREFETCH_ON_LOW_BATTERY = true;
	  
	  // How long to wait before retrying failed checkins.
	  public static long CHECKIN_RETRY_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	  
	  // The maximum number of locations to prefetch for each update.
	  public static int PREFETCH_LIMIT = 5;
	 
	  
	  /**
	   * These values are constants used for intents, exteas, and shared preferences.
	   * You shouldn't need to modify them.
	   */
	  public static String SHARED_PREFERENCE_FILE = "SHARED_PREFERENCE_FILE";
	  public static String SP_KEY_FOLLOW_LOCATION_CHANGES = "SP_KEY_FOLLOW_LOCATION_CHANGES";
	  public static String SP_KEY_LAST_LIST_UPDATE_TIME = "SP_KEY_LAST_LIST_UPDATE_TIME";
	  public static String SP_KEY_LAST_LIST_UPDATE_LAT = "SP_KEY_LAST_LIST_UPDATE_LAT";
	  public static String SP_KEY_LAST_LIST_UPDATE_LNG = "SP_KEY_LAST_LIST_UPDATE_LNG";
	  public static String SP_KEY_LAST_CHECKIN_ID = "SP_KEY_LAST_CHECKIN_ID";
	  public static String SP_KEY_LAST_CHECKIN_TIMESTAMP = "SP_KEY_LAST_CHECKIN_TIMESTAMP";
	  public static String SP_KEY_RUN_ONCE = "SP_KEY_RUN_ONCE";
	  
	  public static String EXTRA_KEY_REFERENCE = "reference";
	  public static String EXTRA_KEY_ID = "id";
	  public static String EXTRA_KEY_LOCATION = "location";
	  public static String EXTRA_KEY_RADIUS = "radius";
	  public static String EXTRA_KEY_TIME_STAMP = "time_stamp";
	  public static String EXTRA_KEY_FORCEREFRESH = "force_refresh";
	  public static String EXTRA_KEY_IN_BACKGROUND = "EXTRA_KEY_IN_BACKGROUND";
	  
	  public static String ARGUMENTS_KEY_REFERENCE = "reference";
	  public static String ARGUMENTS_KEY_ID = "id";
	  
	  public static String NEW_CHECKIN_ACTION = "com.radioactiveyak.places.NEW_CHECKIN_ACTION";
	  public static String RETRY_QUEUED_CHECKINS_ACTION = "com.radioactiveyak.places.retry_queued_checkins";
	  public static String ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED = "com.radioactiveyak.places.active_location_update_provider_disabled";
	  public static String PASSIVE_LOCATION_UPDATE="com.andraft.passive.update_location";
	  
	  public static boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
	  public static boolean SUPPORTS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	  public static boolean SUPPORTS_FROYO = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;
	  public static boolean SUPPORTS_ECLAIR = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR;
	  
	  public static String CONSTRUCTED_LOCATION_PROVIDER = "CONSTRUCTED_LOCATION_PROVIDER";
	  
	  public static int CHECKIN_NOTIFICATION = 0;
	}

