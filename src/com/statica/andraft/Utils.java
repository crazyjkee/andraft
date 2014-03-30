package com.statica.andraft;


public class Utils {
	public static boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
	  public static boolean SUPPORTS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	  public static boolean SUPPORTS_FROYO = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;
	  public static boolean SUPPORTS_ECLAIR = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR;
	
	  public static final String SOCKET_URL = "ws://webcome.andraft.com:8000/up/bbb";
	public static final String REGISTER_URL = "http://webcome.andraft.com/up";
	public static final String URL = "http://webcome.andraft.com/";
	public static final String LOG_TAG = "myLogs";
	public static String SHARED_PREFERENCE_FILE = "SHARED_PREFERENCE_FILE";
	public static String BOOT = "BOOT";
	public static final String MESSAGE = "MESSAGE";
	public static final String YOUR_ID = "ID";
	public static final String YOUR_LINK = "LINK";
	public static final String YOUR_PASS = "PASS";
	public static final String LOOK_NOW = "NOW";
	public static final String LOOKED = "LOOKED";
	public static final String SEND = "SEND";

}
