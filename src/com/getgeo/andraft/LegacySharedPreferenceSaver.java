package com.getgeo.andraft;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.basegeo.andraft.SharedPreferenceSaver;


public class LegacySharedPreferenceSaver extends SharedPreferenceSaver {

	  public LegacySharedPreferenceSaver(Context context) {
	    super(context);
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void savePreferences(Editor editor, boolean backup) {
	    editor.commit();
	  }
	}