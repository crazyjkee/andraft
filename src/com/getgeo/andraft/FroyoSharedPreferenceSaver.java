package com.getgeo.andraft;

import android.annotation.TargetApi;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;


public class FroyoSharedPreferenceSaver extends LegacySharedPreferenceSaver {

	  protected BackupManager backupManager;
		
	  @TargetApi(Build.VERSION_CODES.FROYO)
	public FroyoSharedPreferenceSaver(Context context) {
	    super(context);
	    backupManager = new BackupManager(context);
	  }


	  @TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	  public void savePreferences(Editor editor, boolean backup) {
	    editor.commit();    
	    backupManager.dataChanged();
	  }
}