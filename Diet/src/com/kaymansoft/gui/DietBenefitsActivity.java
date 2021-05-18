package com.kaymansoft.gui;

import com.kaymansoft.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DietBenefitsActivity extends Activity {
	
	Button ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diet_benefits);
		 
		cacheViews();
		initComponents(); 
		
	}
	
	private void cacheViews() {
		ok = (Button) findViewById(R.id.button1);		
	}

	private void initComponents() {
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});		
	}
	
}
