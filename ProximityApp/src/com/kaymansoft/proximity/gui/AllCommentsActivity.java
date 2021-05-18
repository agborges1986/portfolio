package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.adapters.PlaceAdapter;
import com.kaymansoft.proximity.model.PlaceDesc;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.app.Activity;

public class AllCommentsActivity extends Activity {
	PlaceAdapter adapter = null;
	ListView listView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_comments);

		listView = (ListView) findViewById(R.id.listView1);
		int index = getIntent().getIntExtra("index", -1);
		PlaceDesc[] places = new PlaceDesc[] { CS.places[index] };
		CS.sortEachPlace(places);
		adapter = new PlaceAdapter(this, places, index);
		listView.setAdapter(adapter);
	}
}
