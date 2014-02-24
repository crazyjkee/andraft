package com.example.andraft;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
	String secret_get;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b = (Button) findViewById(R.id.button1);
		tv = (TextView) findViewById(R.id.textView1);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.GONE);
		startService(new Intent(this, Services.class));

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
			secret = (EditText) view.findViewById(R.id.Secret);
			adb.setOnKeyListener(new DialogInterface.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						secret_get = secret.getText().toString();
					Log.d("myLogs", secret_get);
						Log.d("myLogs", "enter");
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
		Toast.makeText(this, R.string.saved + "text " + secret,
				Toast.LENGTH_SHORT).show();
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