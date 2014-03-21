package com.services.andraft;

import static com.example.andraft.MainActivity.bf;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

import com.base.andraft.SharedPreferenceSaver;
import com.camera.andraft.CameraView;
import com.example.andraft.MyRequest;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.statica.andraft.Utils;

public class Services extends Service implements CameraView.CameraReadyCallback {
	public static final String NOTIFICATION = "notif";
	public static final String RESULT = "result";
	private final String LOG_TAG = "myLogs";
	private MyRequest myRequest;
	private String status;
	private SharedPreferences sf;
	private SharedPreferenceSaver sharedPreferenceSaver;
	private Editor ed; 

	

	@Override
	public void onCreate() {
		Log.d("myLogs", "onPreExecute");
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
		ed= sf.edit();
		sharedPreferenceSaver = PlatformSpecificImplementationFactory.getSharedPreferenceSaver(this);
		doInBackground();
		super.onCreate();
	}
	
	
	
	
	private void doInBackground() {
		// отправляем запрос с параметрами на адрес
		// Log.d(LOG_TAG,params[0]);
		myRequest = new MyRequest(
				"http://wildfly8-makli.rhcloud.com/upload/22");
		do {
			// Log.d("myLogs","size:"+bf.size());
			myRequest.executeMultipartPost(bf.toByteArray());
			status = myRequest.getStatus();
			Log.d(LOG_TAG,
					status + "" + "\n origResponse:"
							+ myRequest.getOrigResponse());
			
			if (myRequest.getOrigResponse().equals("null")) {
				myRequest.disconnect();
				break;
			}
			if(status.equals("404")){
				ed.putString(Utils.YOUR_LINK, "LINK");
				ed.putString(Utils.YOUR_PASS, "PASS");
				ed.putInt(Utils.LOOK_NOW, 1);
				ed.putInt(Utils.LOOKED, 2);
				ed.putLong(Utils.SEND, 123);
				sharedPreferenceSaver.savePreferences(ed, false);
				publishResults(1);	
			}
		} while (myRequest.getStatus().equals("200"));

		myRequest.disconnect();

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

	private void publishResults(int result){
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(RESULT, result);
		sendBroadcast(intent);
	}




	@Override
	public void onCameraReady() {
		// TODO Auto-generated method stub
		
	}

}