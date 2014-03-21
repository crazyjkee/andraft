package com.example.andraft;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera.PreviewCallback;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
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
import com.camera.andraft.OverlayView;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.services.andraft.Services;
import com.statica.andraft.Utils;

public class MainActivity extends Activity implements View.OnTouchListener,
		CameraView.CameraReadyCallback, OverlayView.UpdateDoneCallback {
	ConnectivityManager cm;
	NetworkInfo nInfo;
	final int DIALOG_EXIT = 1;
	ProgressBar pb;
	TextView wait,your_link,your_pass,watchers,watchered,sends;
	String image_str;
	Button privat,but_stop;
	SharedPreferences sf;
	SharedPreferenceSaver sharedPreferenceSaver;
	Editor ed;
	private CameraView cameraView_;
	private OverlayView overlayView_;
	public static ByteArrayOutputStream bf;
	FrameLayout frLayout;
	View view;
	ArrayList<View> arView;
	String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("myLogs", "onCreate()");
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		nInfo = cm.getActiveNetworkInfo();
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
		String test = sf.getString(Utils.SAVED_SECRET, "lalka");
		if (test.equals("lalka")) {
			Log.d("myLogs", "sf = " + test);
			showDialog(DIALOG_EXIT);
		} else {
			Log.d("myLogs", test);
		}
		registerReceiver(receiver, new IntentFilter(Services.NOTIFICATION));

		// initCamera();
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

		// System.exit(0);
	}

	private void initCamera() {
		SurfaceView cameraSurface = (SurfaceView) findViewById(R.id.surface_camera);
		cameraView_ = CameraView.getInstance(cameraSurface);
		cameraView_.setCameraReadyCallback(this);
		overlayView_ = (OverlayView) findViewById(R.id.surface_overlay);
		overlayView_.setOnTouchListener(this);
		overlayView_.setUpdateDoneCallback(this);
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
			// view = (LinearLayout)
			// getLayoutInflater().inflate(R.layout.dialog,
			// null);
			// adb.setView(view);
			adb.setCancelable(false);
			// secret = (EditText) view.findViewById(R.id.Secret);
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

		initCamera();

		// postRequest();
	}

	public void startOn(View view) {
		Log.d("myLogs", "StartOn");
		if (nInfo != null && nInfo.isConnected()) {
			frLayout.removeView(arView.get(0));
			frLayout.addView(arView.get(1));
			pb.setVisibility(View.VISIBLE);
			startService(new Intent(this, Services.class));

		} else
			Toast.makeText(this, "Can't connect to Network", Toast.LENGTH_SHORT)
					.show();
	}
	
	public void stop(View view){
		stopService(new Intent(this, Services.class));
	}

	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
	}
	

	@Override
	protected void onDestroy() {
	
        Log.d("myLogs","onDestroy");
		super.onDestroy();
	}

	public void readPassword(View view) {
		Toast.makeText(this, sf.getString(Utils.SAVED_SECRET, "lalka"),
				Toast.LENGTH_LONG).show();
		// postRequest();
	}
	 private BroadcastReceiver receiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	
		      Bundle bundle = intent.getExtras();
		      if (bundle != null) {
		        int resultCode = bundle.getInt(Services.RESULT);
		        if (resultCode == 1) {
		        	pb.setVisibility(View.GONE);
		          wait.setText(wait.getText()+" OK");//your_link,your_pass,watchers,watchered,sends;
		          your_link.setText(your_link.getText()+" "+sf.getString(Utils.YOUR_LINK, "lalla"));
		          your_pass.setText(your_pass.getText()+" "+sf.getString(Utils.YOUR_PASS, "123"));
		          watchers.setText(watchers.getText()+" "+sf.getInt(Utils.LOOK_NOW, 22));
		          watchered.setText(watchered.getText()+" "+sf.getInt(Utils.LOOKED, 123123));
		          sends.setText(sends.getText()+" "+sf.getLong(Utils.SEND, 0));
		          frLayout.removeView(arView.get(1));
		          frLayout.addView(arView.get(2));
		          
		          
		        }
		      }
		      context.unregisterReceiver(receiver);
		    }
		    
		  };


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	

	@Override
	public void onUpdateDone() {
		// TODO Auto-generated method stub

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