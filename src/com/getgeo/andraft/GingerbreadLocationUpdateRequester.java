package com.getgeo.andraft;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;


public class GingerbreadLocationUpdateRequester extends FroyoLocationUpdateRequester{
	

		  public GingerbreadLocationUpdateRequester(LocationManager locationManager) {
		    super(locationManager);
		  }

	
		  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		  public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, PendingIntent pendingIntent) {
		    // Gingerbread supports a location update request that accepts criteria directly.
		    // Note that we aren't monitoring this provider to check if it becomes disabled - this is handled by the calling Activity.
		    locationManager.requestLocationUpdates(minTime, minDistance, criteria, pendingIntent);
		  }
		}
