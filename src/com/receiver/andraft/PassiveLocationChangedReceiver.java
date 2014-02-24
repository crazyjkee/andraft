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
			location = (Location) intent.getExtras().get(key);
			Intent passiveintent = new Intent(
					GeoConstants.PASSIVE_LOCATION_UPDATE);
			Log.d("myLogs", "sendBroadcast(passiveintent)");
			passiveintent.putExtra("lal", location);
			context.sendBroadcast(passiveintent);

		}

	}
}