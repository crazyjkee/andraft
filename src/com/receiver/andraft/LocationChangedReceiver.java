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

	  @Override
	  public void onReceive(Context context, Intent intent) {
	    String providerEnabledKey = LocationManager.KEY_PROVIDER_ENABLED;
	    if (intent.hasExtra(providerEnabledKey)) {
	      if (!intent.getBooleanExtra(providerEnabledKey, true)) {
	        Intent providerDisabledIntent = new Intent(GeoConstants.ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED);
	        Log.d("myLogs","sendBroadcast(providerDisabled)");
	        context.sendBroadcast(providerDisabledIntent);    
	      }
	    }
	    
	  }
	}