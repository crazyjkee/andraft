package com.example.andraft;

import static com.statica.andraft.Utils.LOG_TAG;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MyRequest {
	
	private String url;
	private String status;
	private String origResponse;
	private boolean isConnected=true;
	byte[] file;
	private HttpResponse response;
	private HttpEntity rp;
	private Long id = 22l;
	private String pass;
	private String message;
	private JSONObject jsonResponse,jsonRequest;

	public Long getId() {
		return id;
	}

	public MyRequest(String url) {
		super();
		Log.d("myLogs",url);
		this.url = url;

	}
	public String getPass() {
		return pass;
	}

	public String getMessage() {
		return message;
	}

	public String getUrl() {
		return url;
	}

	public String getStatus() {
		return status;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getOrigResponse() {
		return origResponse;
	}
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void disconnect() {
		if (rp != null) {

			try {
				response.getEntity().consumeContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
			setConnected(rp.isStreaming());
			Log.d(LOG_TAG, "Соединение " + rp.isStreaming() + "");
			Log.d(LOG_TAG, "!=null");
			//Log.d(LOG_TAG, origResponse);
		} else {
			Log.d(LOG_TAG, "непрокатило");
		}
	}

	public void executeMultipartPost(byte[] file,String device_Id,String time, String message) {
		this.file = file;
		Log.d("myLogs", id + "");

		MultipartEntity multipartEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			multipartEntity.addPart("json", new StringBody(writeJSON(id,device_Id, time, message,new String(Base64.encodeBase64(file)))));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.d("myLogs", this.url);

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(this.url);
			httppost.setEntity(multipartEntity);
			response = httpclient.execute(httppost);
			rp = response.getEntity();
			Log.d(LOG_TAG, "Соединение " + rp.isStreaming());
			status = response.getStatusLine().getStatusCode() + "";
			Log.d("myLogs","status:"+status);
			origResponse = EntityUtils.toString(rp);
			Log.d("myLogs","origResponse:"+origResponse);
			
		} catch (Exception e) {
			Log.d(LOG_TAG, "Error in http connection " + e.toString());
			origResponse = "null";
		}
		parseJSON(origResponse);
	}
	

	private void parseJSON(String result) {
		try {
			if(!result.equals("null")){
			jsonResponse = new JSONObject(result);
				id = Long.parseLong(jsonResponse.getString("id").toString());
				pass = jsonResponse.getString("pass").toString();
				message = jsonResponse.getString("message").toString();
				Log.d("myLogs", "JSONPARSE RESULT:"+jsonResponse.toString());
		}} catch (JSONException e) {

			e.printStackTrace();
		}
	}
	
	private String writeJSON(Long id,String device_Id,String time,String message,String bytearray){
		jsonRequest = new JSONObject();
		try {
			jsonRequest.put("googleid",device_Id);
			jsonRequest.put("time", time);
			jsonRequest.put("message", message);
			jsonRequest.put("file", bytearray);
			jsonRequest.put("id", id);
			Log.d("myLogs","JSONWRITE REQUEST:"+jsonRequest);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonRequest.toString();
		
	}

	
}