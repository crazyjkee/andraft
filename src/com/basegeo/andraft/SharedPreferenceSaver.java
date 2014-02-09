package com.basegeo.andraft;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class SharedPreferenceSaver {
	  
	  protected Context context;
	  
	  protected SharedPreferenceSaver(Context context) {
	    this.context = context;
	  }
	  

	  public void savePreferences(SharedPreferences.Editor editor, boolean backup) {}
	}