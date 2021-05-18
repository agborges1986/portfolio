package com.kaymannsoft.kmaps.gui;


import com.kaymannsoft.kmaps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash_Kmaps extends Activity{
	@Override
	protected void onCreate(Bundle back) {
		// TODO Auto-generated method stub
		super.onCreate(back);
		setContentView(R.layout.splash);
		Thread timer=new Thread(){
			public void run(){
				try{
					sleep(500);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openSolve=new Intent(Splash_Kmaps.this,null);
					startActivity(openSolve);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}


