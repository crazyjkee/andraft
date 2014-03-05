package com.receiver.andraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.services.andraft.Services;
import com.statica.andraft.Utils;

public class MyBroadReceiv extends BroadcastReceiver {

	private String LOG_TAG = "myLogs";
	SharedPreferences sf;
	boolean boot;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOG_TAG ,"onReceive "+intent.getAction());
		 sf =  context.getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
		boot = sf.getBoolean(Utils.BOOT, false);
		Log.d(LOG_TAG,boot+"");
		if(boot){
			Log.d("myLogs","boot=true");
		context.startService(new Intent(context,Services.class));}
		
	}

}
