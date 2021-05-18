package com.kaymansoft.proximity.gui;

import java.util.Date;
import java.text.*;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.adapters.PlaceAdapter;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.model.HeatResponse;
import com.kaymansoft.proximity.model.PlaceDesc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WhatHotListActivity extends Activity {
	PlaceAdapter adapter = null;
	ListView listView = null;
	PlaceDesc[] places = null;
	int index = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_what_hot);

		listView = (ListView) findViewById(R.id.listView1);
		registerForContextMenu(listView);

		seeWhatHot();
	}

	private void seeWhatHot() {
		// TODO Parametros de prueba
		CS.client.whatIsHot(new int[] { 1, 2, 3, 4 }, 100, 100, 0, 0,
				new Reportable<HeatResponse>() {

					public void setRemainingReportsNumber(int cant) {
						// do nothing    
					}

					public void report(HeatResponse report, String errorMessage) {
						if (report == null) {
							Toast.makeText(WhatHotListActivity.this,
									errorMessage, Toast.LENGTH_LONG).show();
						} else {
							CS.places = report.places;
							CS.sortEachPlace(CS.places);

							// TODO Hay que realizar las demás acciones aquí, de
							// lo contrario no funciona correctamente
							// pues esto se ejecuta de manera asíncrona.
							adapter = new PlaceAdapter(WhatHotListActivity.this, CS.places);
							listView.setAdapter(adapter);
							listView.setOnItemLongClickListener(new OnItemLongClickListener() {
								public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
									index = position;
									return false;
								}
							});
						}
					}
			});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options:");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_what_hot, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
//		// Usar preferencias para guardar el indice del lugar
//		SharedPreferences settings = getSharedPreferences(CS.PREF_FILE, 0);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putInt("index", index);
//		// Commit the edits!
//		editor.commit();
		
		switch (item.getItemId()) {
		case R.id.item1:
			startActivity(new Intent(WhatHotListActivity.this, CommentPlaceActivity.class).putExtra("index", index));
			return true;
		case R.id.item2:
			startActivity(new Intent(WhatHotListActivity.this, AllCommentsActivity.class).putExtra("index", index));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
