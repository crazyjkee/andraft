package com.getgeo.andraft;

import android.content.Context;

import com.base.andraft.SharedPreferenceSaver;
import com.statica.andraft.GeoConstants;



public class PlatformSpecificImplementationFactory {
	
	  public static SharedPreferenceSaver getSharedPreferenceSaver(Context context) {
	    return  GeoConstants.SUPPORTS_GINGERBREAD ? 
	       new GingerbreadSharedPreferenceSaver(context) : 
	       GeoConstants.SUPPORTS_FROYO ? 
	           new FroyoSharedPreferenceSaver(context) :
	           new LegacySharedPreferenceSaver(context);
	  }
	}
