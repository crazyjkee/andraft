package com.example.andraft;

import static com.statica.andraft.Utils.LOG_TAG;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class MyRequest {
	private String url;
	String status;
	String origResponse;
	SimpleDateFormat simpleDateFormat;
	String strTime;
	byte[] file;
	StringBuilder s;
	String image_str;
	HttpResponse response;
	HttpEntity rp;

	public MyRequest(String url) {
		super();
		this.url = url;

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

	public void disconnect() {
		if (rp != null) {

			try {
				response.getEntity().consumeContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(LOG_TAG, "Соединение " + rp.isStreaming() + "");
			Log.d(LOG_TAG, "!=null");
			Log.d(LOG_TAG, origResponse);
		} else {
			Log.d(LOG_TAG, "непрокатило");
		}
	}

	public void executeMultipartPost(byte[] file) {
		this.file = file;
		simpleDateFormat = new SimpleDateFormat("dd hh:mm:ss");
		strTime = simpleDateFormat.format(new Date());
		MultipartEntity multipartEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		multipartEntity.addPart("file", new InputStreamBody(
				new ByteArrayInputStream(file), "lalka"));

		try {
			multipartEntity.addPart("time",new StringBody(strTime));
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
			origResponse = EntityUtils.toString(rp);
		} catch (Exception e) {
			Log.d(LOG_TAG, "Error in http connection " + e.toString());
			origResponse = "null";
		}
	}

}