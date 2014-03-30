package com.example.andraft;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera.PreviewCallback;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.base.andraft.SharedPreferenceSaver;
import com.camera.andraft.CameraView;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.statica.andraft.Utils;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends Activity implements View.OnTouchListener,
		CameraView.CameraReadyCallback{
	public enum sostoyanie {
		start, neprokatilo, proshlo, cant
	}
	MyRequest myRequest_;
	sostoyanie sost;
	ConnectivityManager cm;
	NetworkInfo nInfo;
	final int DIALOG_EXIT = 1;
	ProgressBar pb;
	TextView wait, your_link, your_pass, watchers, watchered, sends;
	String image_str;
	Button privat, but_stop;
	SharedPreferences sf;
	SharedPreferenceSaver sharedPreferenceSaver;
	Editor ed;
	private CameraView cameraView_;
	public static ByteArrayOutputStream bf;
	FrameLayout frLayout;
	View view;
	ArrayList<View> arView;
	String status;
	String device_id;
	SocketClient socket;

	private SurfaceHolder holder;
	private boolean start = false;
	AsyncHttpPost mt;
	private Handler handler;
	private final WebSocketConnection mConnection = new WebSocketConnection();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sostoyanie sost = sostoyanie.start;
		handler = new Handler();
        socket= new SocketClient(Utils.SOCKET_URL);
		Log.d("myLogs", "onCreate()");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Log.d("myLogs", "onCreateActivity");
		setContentView(R.layout.activity_main);
		arView = getViews();
		wait = (TextView) arView.get(1).findViewById(R.id.wait);
		pb = (ProgressBar) arView.get(1).findViewById(R.id.pb);
		your_link = (TextView) arView.get(2).findViewById(R.id.your_link);
		your_pass = (TextView) arView.get(2).findViewById(R.id.your_pass);
		watchers = (TextView) arView.get(2).findViewById(R.id.watchers);
		watchered = (TextView) arView.get(2).findViewById(R.id.watchered);
		sends = (TextView) arView.get(2).findViewById(R.id.sends);
		frLayout = (FrameLayout) findViewById(R.id.FrameLayout1);
		frLayout.addView(arView.get(0));
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
		sharedPreferenceSaver = PlatformSpecificImplementationFactory
				.getSharedPreferenceSaver(this);
		ed = sf.edit();
		/*
		 * String test = sf.getString(Utils.SAVED_SECRET, "lalka"); if
		 * (test.equals("lalka")) { Log.d("myLogs", "sf = " + test);
		 * showDialog(DIALOG_EXIT); } else { Log.d("myLogs", test); }
		 */

		device_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		initCamera();
	}

	public void onToggleButtonClick(View button) {
		Toast.makeText(getApplicationContext(),
				Boolean.toString(((ToggleButton) button).isChecked()),
				Toast.LENGTH_SHORT).show();
	}

	private ArrayList<View> getViews() {
		ArrayList<View> views = new ArrayList<View>();
		LayoutInflater ltInflater = getLayoutInflater();
		views.add(ltInflater.inflate(R.layout.start, null));
		views.add(ltInflater.inflate(R.layout.connect, null));
		views.add(ltInflater.inflate(R.layout.link, null));
		return views;

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("myLogs", "onPause");
		cameraView_.StopPreview();
		cameraView_.Release();
	}

	private void initCamera() {
		SurfaceView cameraSurface = (SurfaceView) findViewById(R.id.surface_camera);
		holder = cameraSurface.getHolder();
		cameraView_ = new CameraView(cameraSurface);
		cameraView_.setCameraReadyCallback(this);
	}

	private PreviewCallback previewCb_ = new PreviewCallback() {
		public void onPreviewFrame(byte[] frame, android.hardware.Camera c) {
			
			int picWidth = cameraView_.Width();
			int picHeight = cameraView_.Height();
			bf = new ByteArrayOutputStream();
			boolean ret;
			try {
				YuvImage image = new YuvImage(frame, ImageFormat.NV21,
						picWidth, picHeight, null);
				ret = image.compressToJpeg(new Rect(0, 0, picWidth, picHeight),
						30, bf);
			} catch (Exception ex) {
				ret = false;
			}
		}
	};

	

	void saveData() {

		// ed.putString(Utils.SAVED_SECRET, secret.getText().toString());
		ed.putBoolean(Utils.BOOT, true);
		sharedPreferenceSaver.savePreferences(ed, false);
		// Log.d("myLogs", secret.getText().toString());
		// startService(new Intent(this, Services.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("myLogs", "onResume()");
		// postRequest();
	}

	public void startOn(View view) {
		Log.d("myLogs", "StartOn");
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		nInfo = cm.getActiveNetworkInfo();
		if (nInfo != null && nInfo.isConnected()) {
			start();
			/*start = true;
			mt = new AsyncHttpPost();
			sost = sostoyanie.start;
			ready();
			mt.execute();*/
		} else
			Toast.makeText(this, "Can't connect to Network", Toast.LENGTH_SHORT)
					.show();
	}
	
	public void start() {
		Log.d("myLogs","START");

		try {
			mConnection.connect(Utils.SOCKET_URL, new WebSocketHandler() {

				@Override
				public void onOpen() {
					Log.d("myLogs", "Status: Connected to " + Utils.SOCKET_URL);
					mConnection.sendTextMessage("Hello, world!");
				}

				@Override
				public void onTextMessage(String payload) {
					Log.d("myLogs", "Got echo: " + payload);
				}

				@Override
				public void onClose(int code, String reason) {
					Log.d("myLogs", "Connection lost.");
				}
			});
		} catch (WebSocketException e) {

			Log.d("myLogs", e.toString());
		}
	}

	class AsyncHttpPost extends AsyncTask<String, Integer, String> {
		private static final String LOG_TAG = "myLogs";
		String status = "1";

		String strTime;
		SimpleDateFormat simpleDateFormat;
		String s;

		@Override
		protected void onPreExecute() {
			simpleDateFormat = new SimpleDateFormat("MM HH:mm:ss");
			super.onPreExecute();
		}

		public String getBucketId(String path) {
			return String.valueOf(path.toLowerCase().hashCode());
		}

		@Override
		protected String doInBackground(String... params) {


			if (start == true) {
				myRequest_ = new MyRequest(Utils.REGISTER_URL);

				do {

					strTime = simpleDateFormat.format(new Date());
					Log.d("myLogs", "size:" + bf.size());
					myRequest_.executeMultipartPost(bf.toByteArray(), device_id,
							strTime, LOG_TAG);

					status = myRequest_.getStatus();
					s = myRequest_.getOrigResponse();
					// Log.d(LOG_TAG,
					// status + "" + "\n origResponse:" +
					// myRequest.getOrigResponse());

					if (s.equals("null")) {
						myRequest_.disconnect();
						break;
					}
					if (!s.equals("null") && start == true) {
						sost = sostoyanie.proshlo;
						new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									//wait.setText(wait.getText()+" OK");

									Thread.sleep(1000);

								} catch (InterruptedException e) {

									e.printStackTrace();

								}

								handler.post(new Runnable() {

									@Override
									public void run() {
										ready();
										

									}

								});

							}

						}).start();

						start = false;
					}

					ed.putString(Utils.YOUR_LINK, Utils.REGISTER_URL);
					ed.putString(Utils.YOUR_PASS, myRequest_.getPass());
					ed.putInt(Utils.LOOK_NOW, 11);
					ed.putInt(Utils.LOOKED, 2);
					ed.putLong(Utils.SEND, 123);
					sharedPreferenceSaver.savePreferences(ed, false);

				} while (myRequest_.getStatus().equals("200")&&myRequest_.isConnected());
			}
			start = false;

			// }

			// myRequest = new MyRequest(Utils.url, Utils.file);

			status = myRequest_.getStatus();
			Log.d(LOG_TAG, status + "");

			return s;
		}

		protected void onPostExecute(String result) {
			if (result.equals("null")) {
				wait.setText("Ошибка подключения");
				myRequest_.disconnect();
				
				pb.setVisibility(View.GONE);
				sost = sostoyanie.cant;
			} else {
				wait.setText(status);

				pb.setVisibility(View.GONE);
				Log.d(LOG_TAG, result);
				Log.d(LOG_TAG, "Запрос получен");

				// frLayout.removeView(arView.get(1));
				// frLayout.addView(arView.get(2));
			}
		}
	}

	/*
	 * @Override protected void onProgressUpdate(Integer... progress) {
	 * pb.setProgress(progress[0]); }
	 */

	public void stop(View view) {
		if(myRequest_!=null){
			Log.d("myLogs","STOP !NULL");
			myRequest_.disconnect();
		}
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		switch (sost) {
		case cant:
			frLayout.removeView(arView.get(1));
			frLayout.addView(arView.get(0));
			break;
		}

	}

	public void ready() {
		switch (sost) {
		case start:
			frLayout.removeView(arView.get(0));
			frLayout.addView(arView.get(1));
			break;
		case proshlo:
			frLayout.removeView(arView.get(1));
			frLayout.addView(arView.get(2));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
if(myRequest_!=null)
	myRequest_.disconnect();
		Log.d("myLogs", "onDestroy");
		super.onDestroy();
	}

	public void readPassword(View view) {
		// Toast.makeText(this, sf.getString(Utils.SAVED_SECRET, "lalka"),
		// Toast.LENGTH_LONG).show();
		// postRequest();
	}

	private void fillPref() {

		Log.d("myLogs", "unRegister");
		your_link.setText(your_link.getText() + " "
				+ sf.getString(Utils.YOUR_LINK, "lalla"));
		your_pass.setText(your_pass.getText() + " "
				+ sf.getString(Utils.YOUR_PASS, "123"));
		watchers.setText(watchers.getText() + " "
				+ sf.getInt(Utils.LOOK_NOW, 22));
		watchered.setText(watchered.getText() + " "
				+ sf.getInt(Utils.LOOKED, 123123));
		sends.setText(sends.getText() + " " + sf.getLong(Utils.SEND, 0));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onCameraReady() {

		int wid = cameraView_.Width();
		int hei = cameraView_.Height();
		cameraView_.StopPreview();
		cameraView_.setupCamera(wid, hei, previewCb_);
		cameraView_.StartPreview();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

}
