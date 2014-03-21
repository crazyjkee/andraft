package com.example.andraft;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
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
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.base.andraft.SharedPreferenceSaver;
import com.camera.andraft.CameraView;
import com.camera.andraft.OverlayView;
import com.getgeo.andraft.PlatformSpecificImplementationFactory;
import com.statica.andraft.Utils;

public class MainActivity extends Activity implements View.OnTouchListener,
		CameraView.CameraReadyCallback, OverlayView.UpdateDoneCallback {
	final int DIALOG_EXIT = 1;
	AsyncHttpPost mt;
/*	ProgressBar pb;
	TextView tv;
	String image_str;
	Button b;
	private LinearLayout view;
	EditText secret;*/
	SharedPreferences sf;
	SharedPreferenceSaver sharedPreferenceSaver;
	private CameraView cameraView_;
	private OverlayView overlayView_;
	ByteArrayOutputStream bf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Log.d("myLogs", "onCreateActivity");
		setContentView(R.layout.activity_main);
		LayoutInflater ltInflater = getLayoutInflater();
		View view = ltInflater.inflate(R.layout.start, null,false);
		FrameLayout frLayout = (FrameLayout) findViewById(R.id.FrameLayout1);
		frLayout.addView(view);
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
		sharedPreferenceSaver = PlatformSpecificImplementationFactory
				.getSharedPreferenceSaver(this);
		String test = sf.getString(Utils.SAVED_SECRET, "lalka");
		if (test.equals("lalka")) {
			Log.d("myLogs", "sf = " + test);
			showDialog(DIALOG_EXIT);
		} else {
			Log.d("myLogs", test);
		}

		initCamera();
	}
	
	public void onToggleButtonClick(View button){
		Toast.makeText(
                getApplicationContext(), 
                Boolean.toString(((ToggleButton) button).isChecked()),
                Toast.LENGTH_SHORT).show();
    }
	

	@Override
	public void onPause() {
		super.onPause();

		cameraView_.StopPreview();
		cameraView_.Release();

		// System.exit(0);
		finish();
	}

	private void initCamera() {
		SurfaceView cameraSurface = (SurfaceView) findViewById(R.id.surface_camera);
		cameraView_ = new CameraView(cameraSurface);
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
				try{
				YuvImage image = new YuvImage(frame, ImageFormat.NV21, picWidth,
						picHeight, null);
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
			//view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog,
				//	null);
			//adb.setView(view);
			adb.setCancelable(false);
			//secret = (EditText) view.findViewById(R.id.Secret);
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
		sf = getSharedPreferences(Utils.SHARED_PREFERENCE_FILE, MODE_PRIVATE);
		Editor ed = sf.edit();
		//ed.putString(Utils.SAVED_SECRET, secret.getText().toString());
		ed.putBoolean(Utils.BOOT, true);
		sharedPreferenceSaver.savePreferences(ed, false);
		//Log.d("myLogs", secret.getText().toString());
		// startService(new Intent(this, Services.class));

	}

	@Override
	protected void onResume() {
		super.onResume();

		// postRequest();
	}

	public void connect(View view) {
	//	pb.setVisibility(View.VISIBLE);
		//tv.setText("Отправка запроса");
		mt = new AsyncHttpPost();
		mt.execute();
	}

	public void readPassword(View view) {
		sf = getPreferences(MODE_PRIVATE);
		Toast.makeText(this, sf.getString(Utils.SAVED_SECRET, "lalka"),
				Toast.LENGTH_LONG).show();
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

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		public String getBucketId(String path) {
			return String.valueOf(path.toLowerCase().hashCode());
		}
		@Override
		protected String doInBackground(String... params) {
			// отправляем запрос с параметрами на адрес
			// Log.d(LOG_TAG,params[0]);
			myRequest = new MyRequest(
					"http://wildfly8-makli.rhcloud.com/upload/22");
			do {
				// Log.d("myLogs","size:"+bf.size());
				// byte[] b = getBitmap(bf.toByteArray());
				myRequest.executeMultipartPost(bf.toByteArray());
				status = myRequest.getStatus();
				Log.d(LOG_TAG, status + ""+"\n origResponse:"+myRequest.getOrigResponse());
				if (myRequest.getOrigResponse().equals("null")) {
					myRequest.disconnect();
					break;
				}
			} while (myRequest.getStatus().equals("200"));

			myRequest.disconnect();

			// }

			// myRequest = new MyRequest(Utils.url, Utils.file);

			status = myRequest.getStatus();
			Log.d(LOG_TAG, status + "");
			// TODO Auto-generated method stub
			String s = myRequest.getOrigResponse();

			return s;
		}

		protected void onPostExecute(String result) {
		/*	if (result.equals("null")) {
				tv.setText("Ошибка подключения");
				pb.setVisibility(View.GONE);
			} else {
				tv.setText(status);

				pb.setVisibility(View.GONE);
				Log.d(LOG_TAG, result);
				Log.d(LOG_TAG, "Запрос получен");
			}*/
		}

		/*
		 * @Override protected void onProgressUpdate(Integer... progress) {
		 * pb.setProgress(progress[0]); }
		 */

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