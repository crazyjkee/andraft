package com.getgeo.andraft;



import com.basegeo.andraft.LocationUpdateRequester;
import com.statica.andraft.GeoConstants;

import android.app.PendingIntent;
import android.location.LocationManager;


public class FroyoLocationUpdateRequester extends LocationUpdateRequester{

	public FroyoLocationUpdateRequester(LocationManager locationManager) {
	    super(locationManager);
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void requestPassiveLocationUpdates(long minTime, long minDistance, PendingIntent pendingIntent) {
	    // Froyo introduced the Passive Location Provider, which receives updates whenever a 3rd party app 
	    // receives location updates.
	    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, GeoConstants.MAX_TIME, GeoConstants.MAX_DISTANCE, pendingIntent);    
	  }
	}
