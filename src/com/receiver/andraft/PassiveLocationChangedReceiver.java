package com.receiver.andraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

import com.getgeo.andraft.LegacyLastLocationFinder;
import com.statica.andraft.GeoConstants;

public class PassiveLocationChangedReceiver extends BroadcastReceiver {
	  
	  protected static String TAG = "PassiveLocationChangedReceiver";
	  
	  /**
	   * When a new location is received, extract it from the Intent and use
	   * it to start the Service used to update the list of nearby places.
	   * 
	   * This is the Passive receiver, used to receive Location updates from 
	   * third party apps when the Activity is not visible. 
	   */
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    String key = LocationManager.KEY_LOCATION_CHANGED;
	    Location location = null;
	    
	    if (intent.hasExtra(key)) {
	      // This update came from Passive provider, so we can extract the location
	      // directly.
	      location = (Location)intent.getExtras().get(key);      
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