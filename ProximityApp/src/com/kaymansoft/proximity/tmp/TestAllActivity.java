package com.kaymansoft.proximity.tmp;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.gui.CS;
import com.kaymansoft.proximity.gui.WhatHotListActivity;
import com.kaymansoft.proximity.model.HeatResponse;
import com.kaymansoft.proximity.model.PlaceDesc;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TestAllActivity extends Activity {
	ListView listView;
	String []names; //= {"Elemento 1", "Elemento 2", "Elemento 3"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_all);

		listView = (ListView) findViewById(R.id.listView1);
		// TODO Parametros de prueba
		CS.client.whatIsHot(new int[] { 1, 2, 3, 4 }, 100, 100, 0, 0,
				new Reportable<HeatResponse>() {

					public void setRemainingReportsNumber(int cant) {
						// do nothing
					}

					public void report(HeatResponse report, String errorMessage) {
						if (report == null) {
							Toast.makeText(TestAllActivity.this, errorMessage,
									Toast.LENGTH_LONG).show();
						} else {
							CS.places = report.places;
							
							names = new String[CS.places.length];
							for (int i = 0; i < names.length; i++) {
								names[i] = CS.places[i].name;
							}
							
							Toast.makeText(TestAllActivity.this, "names.length = "+names.length, Toast.LENGTH_LONG).show();
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestAllActivity.this, 
											android.R.layout.simple_list_item_1, names);
						    listView.setAdapter(adapter);

						}

					}
				});

		if(names == null){
			Toast.makeText(TestAllActivity.this, "names == null", Toast.LENGTH_LONG).show();
		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestAllActivity.this, 
//										android.R.layout.simple_list_item_1, names);
//		listView.setAdapter(adapter);

	}


}
