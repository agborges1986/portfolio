package com.kaymansoft.gui;

import com.kaymansoft.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AgreementActivity extends Activity {

	CheckBox agreementCheck;
	Button ok,cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement);
		
		cacheViews();
		initComponents();	

	}	

	private void cacheViews() {
		agreementCheck = (CheckBox) findViewById(R.id.checkBox1);
		ok = (Button) findViewById(R.id.button1);
		cancel = (Button) findViewById(R.id.button3);
	}

	private void initComponents() {
		ok.setEnabled(false);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				acceptAgrement();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		agreementCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ok.setEnabled(isChecked);
			}
		});
		
	}

	private void acceptAgrement() {
		setResult(RESULT_OK);
		finish();
	}
	
}
