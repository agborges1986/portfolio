package com.kaymansoft.gui;

import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DietActivity extends ListActivity {
	
	//eso es para saber si se debe mostrar la lista de actividades o simplemente lanzar la aplicación
	boolean production = true;
	
	private String apps[] = {
			"CategoryEditorActivity",
			"CategoryListActivity",
			"ImageListActivity",
			"MenuEditorActivity",
			"TakeMealActivity",
			"MenuListActivity",
			"GeneralDietInfo",
			"UserResumeActivity",
			"DietInfoActivity",
			"MainAplicationActivity",
			"FoodListActivity",
			"FoodEditorActivity",
			"SplashActivity",
			"DietBenefitsActivity",
			"AgreementActivity",
			"AlarmWindowActivity",
			"AlarmProffActivity",
			"PreferencesActivity",
			"LunchTime",
			"PersonalInformationActivity",
			"TimesForDietActivity"
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		UserSettingsDBOpenHelper db = new UserSettingsDBOpenHelper(this);
		UserSettings us = db.getSettings();
		if(us.isFirstTime()) {
			startActivity(new Intent(this, SplashActivity.class));
		}
		db.close();
		
		if(production)
			finish();
		
		if(!production)
			setListAdapter(
					new ArrayAdapter<String>(
							this,
							android.R.layout.simple_list_item_1,
							apps)
					);
		
	}
	
	@Override
	public void onBackPressed() {
		//ignorar en caso de que sea ya en producción
		if(!production)
			super.onBackPressed();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String app = apps[position];
		try {
			Class<?> appClass = Class.forName("com.kaymansoft.gui." + app);
			Intent startThis = new Intent(this,appClass);
			startActivity(startThis);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}