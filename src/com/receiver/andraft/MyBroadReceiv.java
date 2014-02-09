package com.receiver.andraft;

import com.services.andraft.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadReceiv extends BroadcastReceiver {

	private String LOG_TAG = "myLogs";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOG_TAG ,"onReceive "+intent.getAction());
		context.startService(new Intent(context,Services.class));
		
	}

}
