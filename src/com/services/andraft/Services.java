package com.services.andraft;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.basegeo.andraft.ILastLocationFinder;
import com.basegeo.andraft.LocationUpdateRequester;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.receiver.andraft.LocationChangedReceiver;
import com.receiver.andraft.PassiveLocationChangedReceiver;
import com.statica.andraft.GeoConstants;



public class Services extends Service {
	LocationManager locationManager;
protected ILastLocationFinder lastLocationFinder;
private PendingIntent locationListenerPendingIntent;
private PendingIntent locationListenerPassivePendingIntent;
private LocationUpdateRequester locationUpdateRequester;
private Criteria criteria;
	@Override
	public void onCreate() {
		super.onCreate();
		Intent activeIntent = new Intent(this, LocationChangedReceiver.class);
	    locationListenerPendingIntent = PendingIntent.getBroadcast(this, 0, activeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    Intent passiveIntent = new Intent(this, PassiveLocationChangedReceiver.class);
	    locationListenerPassivePendingIntent = PendingIntent.getBroadcast(this, 0, passiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Log.d("myLogs","onCreate");
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		if (GeoConstants.USE_GPS_WHEN_ACTIVITY_VISIBLE)
		      criteria.setAccuracy(Criteria.ACCURACY_FINE);
		    else
		      criteria.setPowerRequirement(Criteria.POWER_LOW);
	    lastLocationFinder = PlatformSpecificImplementationFactory.getLastLocationFinder(this);
	    lastLocationFinder.setChangedLocationListener(locationListener);
	    locationUpdateRequester = PlatformSpecificImplementationFactory.getLocationUpdateRequester(locationManager);
	    Location location = lastLocationFinder.getLastBestLocation(GeoConstants.MAX_DISTANCE, 
	            GeoConstants.MAX_TIME);
		updateWithNewLocation(location);
		//locationManager.requestLocationUpdates(provider, GeoConstants.MAX_TIME, 10, locationListener);
		requestLocationUpdates();
		}
	 private void updateWithNewLocation(Location location) {
		Log.d("myLogs","Долгота: "+location.getLongitude()+" Широта:"+location.getLatitude());
	}
	 protected void requestLocationUpdates() {
		 Log.d("myLogs","requestLocationUpdates");
		    // Normal updates while activity is visible.
		    locationUpdateRequester.requestLocationUpdates(GeoConstants.MAX_TIME, GeoConstants.MAX_DISTANCE, criteria, locationListenerPendingIntent);

		    // Passive location updates from 3rd party apps when the Activity isn't visible.
		    locationUpdateRequester.requestPassiveLocationUpdates(GeoConstants.PASSIVE_MAX_TIME, GeoConstants.PASSIVE_MAX_DISTANCE, locationListenerPassivePendingIntent);
		    
		    // Register a receiver that listens for when the provider I'm using has been disabled. 
		    IntentFilter intentFilter = new IntentFilter(GeoConstants.ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED);
		    registerReceiver(locProviderDisabledReceiver, intentFilter);

		    // Register a receiver that listens for when a better provider than I'm using becomes available.
		    String bestProvider = locationManager.getBestProvider(criteria, false);
		    String bestAvailableProvider = locationManager.getBestProvider(criteria, true);
		    if (bestProvider != null && !bestProvider.equals(bestAvailableProvider)) {
				 Log.d("myLogs","locationManager.requestLocationUpdates");
		      locationManager.requestLocationUpdates(bestProvider, 0, 0, bestInactiveLocationProviderListener, getMainLooper());
		    }
	 }
		    protected LocationListener bestInactiveLocationProviderListener = new LocationListener() {
		        public void onLocationChanged(Location l) {
		        	Log.d("myLogs","bestInactive Долгота:"+l.getLongitude()+" Широта:"+l.getLatitude());
		        }
		        public void onProviderDisabled(String provider) {
		        	Log.d("myLogs","bestInactive provider disabled:"+provider);
		        }
		        public void onStatusChanged(String provider, int status, Bundle extras) {}
		        public void onProviderEnabled(String provider) {
		          Log.d("myLogs","bestProvider enabled:"+provider);
		          requestLocationUpdates();
		        }
		      };
		      protected BroadcastReceiver locProviderDisabledReceiver = new BroadcastReceiver() {
		    	    @Override
		    	    public void onReceive(Context context, Intent intent) {
		    	      boolean providerDisabled = !intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
		    	      Log.d("myLogs","providerDisabled:"+providerDisabled);
		    	      if (providerDisabled)
		    	        requestLocationUpdates();
		    	    }
		    	  };
	private final LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			Log.d("myLogs","onLocationChanged");
			updateWithNewLocation(location);
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			Log.d("myLogs", "onProviderDisabled:"+arg0);
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			Log.d("myLogs", "onProviderEnabled:"+arg0);
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			Log.d("myLogs", "onStatusChanged:"+arg0+" int "+arg1+" ");
			
		}
		 
	 };

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("myLogs","onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
