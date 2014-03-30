package com.getgeo.andraft;

import android.content.Context;

import com.base.andraft.SharedPreferenceSaver;
import com.statica.andraft.Utils;



public class PlatformSpecificImplementationFactory {
	
	  public static SharedPreferenceSaver getSharedPreferenceSaver(Context context) {
	    return  Utils.SUPPORTS_GINGERBREAD ? 
	       new GingerbreadSharedPreferenceSaver(context) : 
	       Utils.SUPPORTS_FROYO ? 
	           new FroyoSharedPreferenceSaver(context) :
	           new LegacySharedPreferenceSaver(context);
	  }
	}
