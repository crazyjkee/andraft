package com.getgeo.andraft;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import com.basegeo.andraft.ILastLocationFinder;
import com.basegeo.andraft.LocationUpdateRequester;
import com.basegeo.andraft.SharedPreferenceSaver;
import com.statica.andraft.GeoConstants;



public class PlatformSpecificImplementationFactory {
	/**
	   * Create a new LastLocationFinder instance
	   * @param context Context
	   * @return LastLocationFinder
	   */
	  public static ILastLocationFinder getLastLocationFinder(Context context) {
		  if(GeoConstants.SUPPORTS_GINGERBREAD ){
			  Log.d("myLogs","ILastLocationFinder return GingerbreadLoastLocation");
	    return  new GingerbreadLastLocationFinder(context);}
		  else{
			  Log.d("myLogs","ILastLocationFinder return LegacyLastLocation");
			  return new LegacyLastLocationFinder(context);}
	  }
	  
	  /**
	   * Create a new StrictMode instance.
	   * @return StrictMode
	   */
	 /* public static IStrictMode getStrictMode() {
		if (GeoConstants.SUPPORTS_HONEYCOMB)
	      return new HoneycombStrictMode();
		else if (GeoConstants.SUPPORTS_GINGERBREAD)
	      return new LegacyStrictMode(); 
		else
	      return null;
	  }
	  
	  /**
	   * Create a new LocationUpdateRequester
	   * @param locationManager Location Manager
	   * @return LocationUpdateRequester
	   */
	  public static LocationUpdateRequester getLocationUpdateRequester(LocationManager locationManager) {
	    return GeoConstants.SUPPORTS_GINGERBREAD ? new GingerbreadLocationUpdateRequester(locationManager) : new FroyoLocationUpdateRequester(locationManager);    
	  }
	  
	  /**
	   * Create a new SharedPreferenceSaver
	   * @param context Context
	   * @return SharedPreferenceSaver
	   */
	  public static SharedPreferenceSaver getSharedPreferenceSaver(Context context) {
	    return  GeoConstants.SUPPORTS_GINGERBREAD ? 
	       new GingerbreadSharedPreferenceSaver(context) : 
	       GeoConstants.SUPPORTS_FROYO ? 
	           new FroyoSharedPreferenceSaver(context) :
	           new LegacySharedPreferenceSaver(context);
	  }
	}
