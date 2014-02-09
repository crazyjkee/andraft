package com.getgeo.andraft;

import android.content.Context;
import android.content.SharedPreferences;


public class GingerbreadSharedPreferenceSaver extends FroyoSharedPreferenceSaver {
    
	  public GingerbreadSharedPreferenceSaver(Context context) {
	    super(context);
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void savePreferences(SharedPreferences.Editor editor, boolean backup) {
	    editor.apply();
	    backupManager.dataChanged();
	  }
	}
