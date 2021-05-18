package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.R.layout;
import com.kaymansoft.proximity.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends Activity {
	ImageButton whatHot,
				postPlace,
				test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
    }

    private void findViews(){
    	whatHot = (ImageButton)findViewById(R.id.ImageButton03);
    	whatHot.setOnClickListener(new SeeWhatHotListener());
    	
    	postPlace = (ImageButton)findViewById(R.id.ImageButton04);
    	postPlace.setOnClickListener(new PostPlaceListener());
    	
    	test = (ImageButton)findViewById(R.id.ImageButton01);
    	test.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this, WhatHotMapActivity.class));
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    private class SeeWhatHotListener implements View.OnClickListener{
		public void onClick(View v) {
			startActivity(new Intent(HomeActivity.this, WhatHotListActivity.class));			
		}    	
    }
    
    private class PostPlaceListener implements View.OnClickListener{
		public void onClick(View v) {
			startActivity(new Intent(HomeActivity.this, PostPlaceActivity.class));			
		}    	
    }
}
