package com.receiver.andraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.statica.andraft.GeoConstants;

public class LocationChangedReceiver extends BroadcastReceiver {
	  
	  protected static String TAG = "LocationChangedReceiver";
	  
	  /**
	   * When a new location is received, extract it from the Intent and use
	   * it to start the Service used to update the list of nearby places.
	   * 
	   * This is the Active receiver, used to receive Location updates when 
	   * the Activity is visible. 
	   */
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    String locationKey = LocationManager.KEY_LOCATION_CHANGED;
	    String providerEnabledKey = LocationManager.KEY_PROVIDER_ENABLED;
	    if (intent.hasExtra(providerEnabledKey)) {
	      if (!intent.getBooleanExtra(providerEnabledKey, true)) {
	        Intent providerDisabledIntent = new Intent(GeoConstants.ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED);
	        context.sendBroadcast(providerDisabledIntent);    
	      }
	    }
	    
	  }
	}