package com.services.andraft;

import static com.example.andraft.MainActivity.bf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;

import com.base.andraft.SharedPreferenceSaver;
import com.camera.andraft.CameraView;
import com.example.andraft.MyRequest;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.statica.andraft.Utils;

public class Services extends Service {

	public static final String NOTIFICATION = "notif";
	public static final String RESULT = "result";
	private final String LOG_TAG = "myLogs";
	private MyRequest myRequest;
	private String status;
	private SharedPreferences sf;
	private SharedPreferenceSaver sharedPreferenceSaver;
	private Editor ed;
	private SimpleDateFormat simpleDateFormat;
	private String strTime;
	private String device_id;
	public static boolean go = false;
	@Override
	public void onCreate() {
		
		Log.d("myLogs", "onPreExecute");
		simpleDateFormat = new SimpleDateFormat("dd MMMM hh:mm:ss");
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
		ed = sf.edit();
		sharedPreferenceSaver = PlatformSpecificImplementationFactory
				.getSharedPreferenceSaver(this);
		device_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		Log.d("myLogs", "device_id:" + device_id);
		

		super.onCreate();
	}

	private void doInBackground() {
		// отправляем запрос с параметрами на адрес
		// Log.d(LOG_TAG,params[0]);
		myRequest = new MyRequest(Utils.REGISTER_URL);
	           
		 
		do {
			// Log.d("myLogs","size:"+bf.size());

			strTime = simpleDateFormat.format(new Date());
			myRequest.executeMultipartPost(bf.toByteArray(), device_id,
					strTime, LOG_TAG);
			status = myRequest.getStatus();
			// Log.d(LOG_TAG,
			// status + "" + "\n origResponse:" + myRequest.getOrigResponse());

			if (myRequest.getOrigResponse().equals("null")) {
				myRequest.disconnect();
				// break;
			}

			ed.putString(Utils.YOUR_LINK, Utils.REGISTER_URL);
			ed.putString(Utils.YOUR_PASS, myRequest.getPass());
			ed.putInt(Utils.LOOK_NOW, 11);
			ed.putInt(Utils.LOOKED, 2);
			ed.putLong(Utils.SEND, 123);
			sharedPreferenceSaver.savePreferences(ed, false);

    
		} while (myRequest.getStatus().equals("200"));

		
		// }

		// myRequest = new MyRequest(Utils.url, Utils.file);

		status = myRequest.getStatus();
		Log.d(LOG_TAG, status + "");
		// TODO Auto-generated method stub
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("myLogs","onStartCommand");
		doInBackground();
		return super.onStartCommand(intent, flags, startId);
	}

	

	

}