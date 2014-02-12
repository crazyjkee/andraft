package com.getgeo.andraft;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.LocationManager;


public class GingerbreadLocationUpdateRequester extends FroyoLocationUpdateRequester{
	

		  public GingerbreadLocationUpdateRequester(LocationManager locationManager) {
		    super(locationManager);
		  }

	
		@Override
		  public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, PendingIntent pendingIntent) {
		    //Ginger поддерживает работу с критериями
		    locationManager.requestLocationUpdates(minTime, minDistance, criteria, pendingIntent);
		  }
		}
