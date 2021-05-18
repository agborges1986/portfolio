package com.kaymannsoft.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		
		Thread splash=new Thread() {
			
			public void run() {
				
			 try {
				Thread.sleep(500);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}finally{
				Intent i=new Intent("com.kaymannsoft.card.FLASHCARD");
				startActivity(i);
			}
			}
			
		};
		splash.start();
			
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		finish();
	}
	
	

	
}
