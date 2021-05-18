package com.example.prueba_graf;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;

public class MainActivity extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dibujo dibujo=new dibujo(this);
		
		DisplayMetrics disp=new DisplayMetrics();
//		dibujo.gate.setParams(disp.widthPixels/2, disp.heightPixels/2, disp.heightPixels/4, disp.widthPixels/4);
		setContentView(dibujo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
