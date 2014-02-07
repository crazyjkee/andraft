package com.example.andraft;

import static com.statica.andraft.Utils.LOG_TAG;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.util.Log;

import com.statica.andraft.Utils;

public class MyRequest {	
	private String url;
	String status="";
	String origResponse;

	String file;
	List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
	StringBuilder s;
	String image_str;
	 HttpResponse response;
	

	public MyRequest(String url,String file) {
		super();
		this.url = url;
		this.file=file;
			origResponse=executeMultipartPost();
		
		
	}
	public List<NameValuePair> getNameValuePairs() {
		return nameValuePairs;
	}
	public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
		
		this.nameValuePairs = nameValuePairs;
	}
	public String getUrl() {
		return url;
	}
	public String getStatus() {
		return status;
	}
	public String getFile() {
		return file;
	}
	public String getOrigResponse() {
		return origResponse;
	}
	
	public String executeMultipartPost() {

         MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); 
         try {
			multipartEntity.addPart("id", new StringBody(Utils.id));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// multipartEntity.addPart("Nick", new StringBody("Nick"));
         multipartEntity.addPart("file", new FileBody(new File(Utils.file)));
         
         
         try{
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost(url);
             httppost.setEntity(multipartEntity);
             long k=System.currentTimeMillis();
             long m;
             //for(int i=0;i<1000;i++){
             
            response = httpclient.execute(httppost);
             HttpEntity rp = response.getEntity();
             Log.d(LOG_TAG,"Соединение "+response.getEntity().isStreaming());
 			status = response.getStatusLine().getStatusCode()+"";
 			if (rp != null) {
 				origResponse = EntityUtils.toString(rp);
 				response.getEntity().consumeContent();
 				Log.d(LOG_TAG,"Соединение "+response.getEntity().isStreaming()+"");
 				Log.d(LOG_TAG, "!=null");
 				Log.d(LOG_TAG,origResponse);
 			} else
 				Log.d(LOG_TAG, "непрокатило");
 			m=System.currentTimeMillis()-k;
 		    k=System.currentTimeMillis();
 		    Log.d(LOG_TAG,"Время запроса "+ m);//}
         }catch(Exception e){
               Log.d(LOG_TAG,"Error in http connection "+e.toString());
               return "null";    
         }
         
		return origResponse;
     }

        
     }
