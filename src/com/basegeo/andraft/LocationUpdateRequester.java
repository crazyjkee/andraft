package com.basegeo.andraft;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.LocationManager;

/**
 * Abstract base class that can be extended to provide active and passive location updates 
 * optimized for each platform release.
 * 
 * Uses broadcast Intents to notify the app of location changes.
 */
public abstract class LocationUpdateRequester {
  
  protected LocationManager locationManager;
  
  protected LocationUpdateRequester(LocationManager locationManager) {
    this.locationManager = locationManager;
  }
  

  public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, PendingIntent locationListenerPendingIntent) {}
  

  public void requestPassiveLocationUpdates(long minTime, long minDistance,PendingIntent locationListenerPassivePendingIntent) {}


}

