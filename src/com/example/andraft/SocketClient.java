package com.example.andraft;

import android.util.Log;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class SocketClient {
	private String uri;

	private static final String TAG = "de.tavendo.test1";

	private final WebSocketConnection mConnection = new WebSocketConnection();

	public SocketClient(String uri) {
		this.uri = uri;
	}

	public void start() {
		Log.d("myLogs","START");

		try {
			mConnection.connect(uri, new WebSocketHandler() {

				@Override
				public void onOpen() {
					Log.d(TAG, "Status: Connected to " + uri);
					mConnection.sendTextMessage("Hello, world!");
				}

				@Override
				public void onTextMessage(String payload) {
					Log.d(TAG, "Got echo: " + payload);
				}

				@Override
				public void onClose(int code, String reason) {
					Log.d(TAG, "Connection lost.");
				}
			});
		} catch (WebSocketException e) {

			Log.d(TAG, e.toString());
		}
	}

}
