package com.getgeo.andraft;

import java.util.List;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import com.basegeo.andraft.ILastLocationFinder;


public class GingerbreadLastLocationFinder implements ILastLocationFinder {
	  
	  protected static String TAG = "LastLocationFinder";
	  protected static String SINGLE_LOCATION_UPDATE_ACTION = "com.radioactiveyak.places.SINGLE_LOCATION_UPDATE_ACTION";
	  
	  protected PendingIntent singleUpatePI;
	  protected LocationListener locationListener;
	  protected LocationManager locationManager;
	  protected Context context;
	  protected Criteria criteria;
	  
	  /**
	   * Construct a new Gingerbread Last Location Finder.
	   * @param context Context
	   */
	  public GingerbreadLastLocationFinder(Context context) {
	    this.context = context;
	    locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_LOW);
	    
	    //Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);  
	   //singleUpatePI = PendingIntent.getBroadcast(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	  }
	  
	  
	  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public Location getLastBestLocation(int minDistance, long minTime) {
	    Location bestResult = null;
	    float bestAccuracy = Float.MAX_VALUE;
	    long bestTime = Long.MIN_VALUE;
	    

	    List<String> matchingProviders = locationManager.getAllProviders();
	    for (String provider: matchingProviders) {
	      Location location = locationManager.getLastKnownLocation(provider);
	      if (location != null) {
	        float accuracy = location.getAccuracy();
	        long time = location.getTime();
	        
	        if ((time > minTime && accuracy < bestAccuracy)) {
	          bestResult = location;
	          bestAccuracy = accuracy;
	          bestTime = time;
	        }
	        else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
	          bestResult = location;
	          bestTime = time;
	        }
	      }
	    }
	    
	    if (locationListener != null && (bestTime < minTime || bestAccuracy > minDistance)) { 
	      IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
	      context.registerReceiver(singleUpdateReceiver, locIntentFilter);
	      //получения единоразового обновления местоположения
	      locationManager.requestSingleUpdate(criteria, singleUpatePI);
	    }
	    
	    return bestResult;
	  }

	 
	  protected BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	      context.unregisterReceiver(singleUpdateReceiver);
	      Log.d("myLogs",this.getClass().getSimpleName()+" singleUpdateReceiver");
	      String key = LocationManager.KEY_LOCATION_CHANGED;
	      Location location = (Location)intent.getExtras().get(key);
	      
	      if (locationListener != null && location != null)
	        locationListener.onLocationChanged(location);
	      
	      locationManager.removeUpdates(singleUpatePI);
	    }
	  };


	  public void setChangedLocationListener(LocationListener l) {
	    locationListener = l;
	  }

	  public void cancel() {
	    locationManager.removeUpdates(singleUpatePI);
	  }
	}
