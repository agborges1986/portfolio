package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.R.layout;
import com.kaymansoft.proximity.R.menu;
import com.kaymansoft.proximity.adapters.AvatarAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class SelectAvatarActivity extends Activity {
	GridView gridView = null;
	int index = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);

        gridView = (GridView)findViewById(R.id.gridView1);
        registerForContextMenu(gridView);
        AvatarAdapter adapter = new AvatarAdapter(this);
        gridView.setAdapter(adapter);
        
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				index = position;
				return false;
			}
		});
    }
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options:");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_select_avatar, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			startActivity(new Intent(SelectAvatarActivity.this, NewUserActivity.class).putExtra("index", index));
			finish();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

}
