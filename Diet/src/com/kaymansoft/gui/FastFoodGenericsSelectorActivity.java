package com.kaymansoft.gui;

import com.kaymansoft.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class FastFoodGenericsSelectorActivity extends Activity {
	
	ImageButton ff,gen;
	
	public static final String SELECTED_GROUP = "com.kaymansoft.selected_group";
	public static final int GENERIC_GROUP = 1;
	public static final int FAST_FOOD_GROUP = 2;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fast_food_generics_selector);
		
		cacheViews();
		initComponents();
		
	}

	private void cacheViews() {
		ff = (ImageButton) findViewById(R.id.imageButton2);
		gen = (ImageButton) findViewById(R.id.imageButton1);
		
	}

	private void initComponents() {
		ff.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = getIntent();
				intent.putExtra(SELECTED_GROUP, FAST_FOOD_GROUP);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		gen.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = getIntent();
				intent.putExtra(SELECTED_GROUP, GENERIC_GROUP);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
