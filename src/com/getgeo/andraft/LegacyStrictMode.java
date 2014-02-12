package com.getgeo.andraft;

import android.os.StrictMode;

import com.basegeo.andraft.IStrictMode;

public class LegacyStrictMode implements IStrictMode {

		  /**
		   * Enable {@link StrictMode}
		   * TODO Set your preferred Strict Mode features.
		   */
		   public void enableStrictMode() {
		     StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		     .detectDiskReads()
		     .detectDiskWrites()
		     .detectNetwork()
		     .penaltyLog()
		     .build());
		   }
		}
