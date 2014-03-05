package com.example.andraft;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basegeo.andraft.SharedPreferenceSaver;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.services.andraft.Services;
import com.statica.andraft.Utils;

public class MainActivity extends Activity {

	final int DIALOG_EXIT = 1;
	AsyncHttpPost mt;
	ProgressBar pb;
	TextView tv;
	String image_str;
	Button b;
	private LinearLayout view;
	EditText secret;
	SharedPreferences sf;
	SharedPreferenceSaver sharedPreferenceSaver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("myLogs","onCreateActivity");
		setContentView(R.layout.activity_main);
		b = (Button) findViewById(R.id.button1);
		tv = (TextView) findViewById(R.id.textView1);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.GONE);
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
		sharedPreferenceSaver = PlatformSpecificImplementationFactory.getSharedPreferenceSaver(this);
		String test = sf.getString(Utils.SAVED_SECRET, "lalka");
		if(test.equals("lalka")){
			Log.d("myLogs","sf = "+test);
			showDialog(DIALOG_EXIT);}
		else {
			Log.d("myLogs",test);
		}   
			
		


	}

	public void changeSecret(View v) {
		showDialog(DIALOG_EXIT);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_EXIT) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(R.string.changeSecretText);
			adb.setMessage(R.string.save_data);
			view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog,
					null);
			adb.setView(view);
			adb.setCancelable(false);
			secret = (EditText) view.findViewById(R.id.Secret);
			adb.setOnKeyListener(new DialogInterface.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						
						saveData();
						dialog.cancel();
						return true;
					}
					return false;
				}

			});
			return adb.create();
		}
		return super.onCreateDialog(id);
	}

	void saveData() {
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE,MODE_PRIVATE);
		Editor ed = sf.edit();
        ed.putString(Utils.SAVED_SECRET,secret.getText().toString());
        ed.putBoolean(Utils.BOOT, true);
        sharedPreferenceSaver.savePreferences(ed, false);
        Log.d("myLogs", secret.getText().toString());
		startService(new Intent(this, Services.class));

	}

	@Override
	protected void onResume() {
		super.onResume();
		
	// postRequest();
		}

	void postRequest() {
		pb.setVisibility(View.VISIBLE);
		tv.setText("Отправка запроса");
		mt = new AsyncHttpPost();
		mt.execute();
	}

	public void readWebpage(View view) {
		sf = getPreferences(MODE_PRIVATE);
		Toast.makeText(this, sf.getString(Utils.SAVED_SECRET, "lalka"), Toast.LENGTH_LONG).show();
		// postRequest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}


	class AsyncHttpPost extends AsyncTask<String, Integer, String> {
		private static final String LOG_TAG = "myLogs";
		String status = "1";
		MyRequest myRequest;
		List<NameValuePair> nameValuePairs;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// отправляем запрос с параметрами на адрес
			// Log.d(LOG_TAG,params[0]);

			myRequest = new MyRequest(Utils.url, Utils.file);

			status = myRequest.getStatus();
			Log.d(LOG_TAG, status + "");

			// TODO Auto-generated method stub
			String s = myRequest.getOrigResponse();
			return s;
		}

		protected void onPostExecute(String result) {
			if (result.equals("null")) {
				tv.setText("Ошибка подключения");
				pb.setVisibility(View.GONE);
			} else {
				tv.setText(status);

				pb.setVisibility(View.GONE);
				Log.d(LOG_TAG, result);
				Log.d(LOG_TAG, "Запрос получен");
			}
		}


		/*
		 * @Override protected void onProgressUpdate(Integer... progress) {
		 * pb.setProgress(progress[0]); }
		 */

	}

}