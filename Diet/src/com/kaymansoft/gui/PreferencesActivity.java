package com.kaymansoft.gui;

import com.kaymansoft.alarm.AlarmUtils;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;

public class PreferencesActivity extends PreferenceActivity {

	private OnSharedPreferenceChangeListener onChange = new OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if("alarm_check".equals(key)) {
				UserSettingsDBOpenHelper db = new UserSettingsDBOpenHelper(PreferencesActivity.this);
				if(!sharedPreferences.getBoolean(key, false)) {//si se canceló las alarmas
					AlarmUtils.cancelNextAlarm(PreferencesActivity.this, db.getSettings());
				} else {
					AlarmUtils.setupNextAlarm(PreferencesActivity.this, db.getSettings());
				}
				db.close();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(com.kaymansoft.R.xml.preferences);		
		
	}

	@Override
	protected void onResume() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    prefs.registerOnSharedPreferenceChangeListener(onChange);
		super.onResume();
	}
	
	@Override
	public void onPanelClosed(int featureId, Menu menu) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    prefs.unregisterOnSharedPreferenceChangeListener(onChange);
		super.onPanelClosed(featureId, menu);
	}
	
	
}
