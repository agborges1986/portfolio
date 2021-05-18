package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.R.layout;
import com.kaymansoft.proximity.R.menu;
import com.kaymansoft.proximity.client.Reportable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommentPlaceActivity extends Activity {
	EditText comment;
	RatingBar rating;
	private Button accept, cancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_place);   
        
        findViews();
        
        accept.setOnClickListener(new AcceptListener());
        cancel.setOnClickListener(new CancelListener());
    }
    
    private void findViews(){
    	comment = (EditText)findViewById(R.id.editText1);
    	rating = (RatingBar)findViewById(R.id.ratingBar1);
    	
    	accept = (Button)findViewById(R.id.button1);
    	cancel = (Button)findViewById(R.id.button2);
    }

    class AcceptListener implements OnClickListener{
		public void onClick(View v) {
//	        SharedPreferences settings = getSharedPreferences(CS.PREF_FILE, 0);        
//	        int index = settings.getInt("index", -1);
			
			int index = getIntent().getIntExtra("index", -1);

			CS.client.commentPlace(comment.getText().toString(), 
								   CS.places[index].id, 
								   rating.getProgress(), 
								   null, 
								   CS.sessionId,
								   2, 
								   2000000000, 
								   new Reportable<Object>() {
				
				public void setRemainingReportsNumber(int cant) {
					// do nothing
				}
				
				public void report(Object report, String errorMessage) {
					if(report == null){
						 Toast.makeText(CommentPlaceActivity.this, errorMessage, Toast.LENGTH_LONG).show();
					}else
						Toast.makeText(CommentPlaceActivity.this, report.toString(), Toast.LENGTH_LONG).show();
				}
			});	
			
			startActivity(new Intent(CommentPlaceActivity.this, HomeActivity.class));
			finish();
		}    	
    }
    
    class CancelListener implements OnClickListener{
		public void onClick(View v) {
			startActivity(new Intent(CommentPlaceActivity.this, WhatHotListActivity.class));	
		}    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_comment_place, menu);
        return true;
    }
} 
