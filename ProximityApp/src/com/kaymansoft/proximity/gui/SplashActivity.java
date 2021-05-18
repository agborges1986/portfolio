package com.kaymansoft.proximity.gui;

import com.kaymansoft.proximity.R;
import com.kaymansoft.proximity.client.ProximityClient;
import com.kaymansoft.proximity.client.ProximityClientImpl;
import com.kaymansoft.proximity.client.Reportable;
import com.kaymansoft.proximity.model.CategoryDesc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity que muestra el splash al inicio de la app y carga algunos datos iniciales.
 * 
 * @author Denis
 * 
 */
public class SplashActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// requestWindowFeature(Window.FEATURE_PROGRESS);
		// setProgressBarVisibility(true);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		Thread thread = new Thread(){
			@Override
			public void run(){
				try{
					sleep(2000);					
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally{					
					Intent intent = new Intent(SplashActivity.this, SelectUserActivity.class);
					startActivity(intent);
				}
			}
		};
		thread.start();

		CS.client.getCategoryList(new Reportable<CategoryDesc[]>() {

			public void setRemainingReportsNumber(int cant) {
				// do nothing
			}

			public void report(CategoryDesc[] report, String errorMessage) {
				if (report == null) {
					Toast.makeText(SplashActivity.this, errorMessage,
							Toast.LENGTH_LONG).show();
				} else {
					CS.categories = report;
					Toast.makeText(SplashActivity.this, "Categories number: "+CS.categories.length, Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}