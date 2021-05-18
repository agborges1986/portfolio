package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.client.Reportable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PostPlaceActivity extends Activity {
	EditText name, description, heatDuration, latitude, longitude, rating;
	Button post, cancel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_place);
        
        findViews();
        
        post.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				//Chequear que los campos tengan valores.
				
				CS.client.postPlace(name.getText().toString(),
									description.getText().toString(), 
									Integer.parseInt(heatDuration.getText().toString()), 
									null, 
									Double.parseDouble(latitude.getText().toString()), 
									Double.parseDouble(longitude.getText().toString()), 
									Integer.parseInt(rating.getText().toString()), 
									1, //por defecto
									CS.sessionId, 
									new Reportable<Integer>() {
					
					public void setRemainingReportsNumber(int cant) {
						// do nothing
					}
					
					public void report(Integer report, String errorMessage) {
						if(report == null){
							Toast.makeText(PostPlaceActivity.this, errorMessage, Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(PostPlaceActivity.this, report.toString(), Toast.LENGTH_LONG).show();
							startActivity(new Intent(PostPlaceActivity.this, WhatHotListActivity.class));
							finish();
						}
					}
				});
			}
		});
        
        cancel.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				startActivity(new Intent(PostPlaceActivity.this, HomeActivity.class));				
			}
		});
    }
    
    private void findViews(){
    	name = (EditText)findViewById(R.id.editText1);
    	description = (EditText)findViewById(R.id.editText2);
    	heatDuration = (EditText)findViewById(R.id.editText3);
    	latitude = (EditText)findViewById(R.id.editText4);
    	longitude = (EditText)findViewById(R.id.editText5);
    	rating = (EditText)findViewById(R.id.editText6);   
    	
    	post = (Button)findViewById(R.id.button1);
    	cancel = (Button)findViewById(R.id.button2);
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_place, menu);
        return true;
    }
}
