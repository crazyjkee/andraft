package com.receiver.andraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.getgeo.andraft.LegacyLastLocationFinder;
import com.statica.andraft.GeoConstants;

public class PassiveLocationChangedReceiver extends BroadcastReceiver {
	  
	  protected static String TAG = "PassiveLocationChangedReceiver";
	  
	 /*
	   * Работает в сервисе
	   */
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    String key = LocationManager.KEY_LOCATION_CHANGED;
	    Location location = null;
	    
	    if (intent.hasExtra(key)) {
	      location = (Location)intent.getExtras().get(key);  
	      Log.d("myLogs","Passive action:"+location.getLongitude());
	      Intent passiveintent = new Intent(GeoConstants.PASSIVE_LOCATION_UPDATE);
	      
	      passiveintent.putExtra("latitude", (long)location.getLatitude());
	      passiveintent.putExtra("longitude",(long)location.getLongitude());
	        Log.d("myLogs","sendBroadcast(passiveintent)");
	        passiveintent.putExtra("lal", location);
	        context.sendBroadcast(passiveintent);  
	      
	    }
	    else {
	      // This update came from a recurring alarm. We need to determine if there
	      // has been a more recent Location received than the last location we used.
	      
	      // Get the best last location detected from the providers.
	      LegacyLastLocationFinder lastLocationFinder = new LegacyLastLocationFinder(context);
	      location = lastLocationFinder.getLastBestLocation(GeoConstants.MAX_DISTANCE, System.currentTimeMillis()-GeoConstants.MAX_TIME);
	      SharedPreferences prefs = context.getSharedPreferences(GeoConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
	      
	      // Get the last location we used to get a listing.
	      long lastTime = prefs.getLong(GeoConstants.SP_KEY_LAST_LIST_UPDATE_TIME, Long.MIN_VALUE);
	      long lastLat = prefs.getLong(GeoConstants.SP_KEY_LAST_LIST_UPDATE_LAT, Long.MIN_VALUE);
	      long lastLng = prefs.getLong(GeoConstants.SP_KEY_LAST_LIST_UPDATE_LNG, Long.MIN_VALUE);
	      Location lastLocation = new Location(GeoConstants.CONSTRUCTED_LOCATION_PROVIDER);
	      lastLocation.setLatitude(lastLat);
	      lastLocation.setLongitude(lastLng);

	      // Check if the last location detected from the providers is either too soon, or too close to the last
	      // value we used. If it is within those thresholds we set the location to null to prevent the update
	      // Service being run unnecessarily (and spending battery on data transfers).
	      if ((lastTime > System.currentTimeMillis()-GeoConstants.MAX_TIME) ||
	         (lastLocation.distanceTo(location) < GeoConstants.MAX_DISTANCE))
	        location = null;
	    }
	    

	  }
	}