/**
 * 
 */
package com.kaymannsoft.card;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * @author Ariel
 *
 */
public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 PreferenceManager prefMgr = getPreferenceManager();
	     prefMgr.setSharedPreferencesName("appPreferences");
	     addPreferencesFromResource(R.xml.preferences);
		
	}
	
	

}
