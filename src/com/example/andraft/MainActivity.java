package com.example.andraft;



import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.basegeo.andraft.ILastLocationFinder;
import com.basegeo.andraft.LocationUpdateRequester;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.statica.andraft.GeoConstants;
import com.statica.andraft.Utils;

public class MainActivity extends Activity {
	Context mContext=this;
	AsyncHttpPost mt;
	 ProgressBar pb;
    TextView tv;
    String image_str;
    Button b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 b= (Button) findViewById(R.id.button1);
		tv = (TextView) findViewById(R.id.textView1);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.GONE);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		postRequest();
	}
	void postRequest(){
		pb.setVisibility(View.VISIBLE);
		tv.setText("Отправка запроса");
		mt = new AsyncHttpPost();
		mt.execute();
	}

	public void readWebpage(View view) {
		postRequest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class AsyncHttpPost extends AsyncTask<String, Integer, String> {
		private static final String LOG_TAG = "myLogs";
		String status="1";
		MyRequest myRequest;
		List<NameValuePair> nameValuePairs; 
		protected ILastLocationFinder lastLocationFinder;
		  protected LocationUpdateRequester locationUpdateRequester;
		  protected PendingIntent locationListenerPendingIntent;
		  protected PendingIntent locationListenerPassivePendingIntent;
		  Location lastKnownLocation;
		private LocationManager locationManager;
		
		
		@Override
		protected void onPreExecute() {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		    lastLocationFinder = PlatformSpecificImplementationFactory.getLastLocationFinder(mContext);
		    locationUpdateRequester = PlatformSpecificImplementationFactory.getLocationUpdateRequester(locationManager);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			//отправляем запрос с параметрами на адрес
			//Log.d(LOG_TAG,params[0]);
			lastKnownLocation = lastLocationFinder.getLastBestLocation(GeoConstants.MAX_DISTANCE, 
		            System.currentTimeMillis()-GeoConstants.MAX_TIME);			
			myRequest = new MyRequest(Utils.url,Utils.file);
			
			status = myRequest.getStatus();
			Log.d(LOG_TAG,status+"");
			
			// TODO Auto-generated method stub
			String s = myRequest.getOrigResponse();
			return s;
		}

		protected void onPostExecute(String result) {
			if(result.equals("null")){
				tv.setText("Ошибка подключения");
				pb.setVisibility(View.GONE);
			}
			else{
			tv.setText(status);
			Log.d(LOG_TAG,"Получил провайдера "+lastKnownLocation.getProvider());
			Log.d(LOG_TAG,"Получил широту "+lastKnownLocation.getLatitude());
			Log.d(LOG_TAG,"Получил долготу "+lastKnownLocation.getLongitude());
			pb.setVisibility(View.GONE);
			Log.d(LOG_TAG,result);
			Log.d(LOG_TAG, "Запрос получен");}
		}
		
		
	   /* @Override
		protected void onProgressUpdate(Integer... progress) {
			pb.setProgress(progress[0]);
		}*/
		
	}

}