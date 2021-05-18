package com.Maps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class resultado extends Activity implements OnClickListener {

	Button volver,exit,circuito;
	TextView result;
	String res, mensaje_error;
	int num_var,entrada;
	boolean formofmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		volver=(Button)findViewById(R.id.bVolver);
		circuito=(Button)findViewById(R.id.bCircuit);
		exit=(Button)findViewById(R.id.bExit);
		result=(TextView)findViewById(R.id.tvResult);
		Bundle getdata=getIntent().getExtras();
		res=getdata.getString("res");
		num_var=getdata.getInt("variables");
		entrada = getdata.getInt("entrada");
		formofmap=getdata.getBoolean("minormax");
		result.setText(res);
		volver.setOnClickListener(this);
		exit.setOnClickListener(this);
		circuito.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.bExit:
			this.finish();
			break;
		case R.id.bCircuit:
			boolean val=validate();
			if(val)
			{
				Intent a=new Intent(resultado.this,Circuito.class);
				Bundle extras=new Bundle();
				extras.putString("res", res);
				extras.putInt("variables",num_var);
				extras.putInt("entrada",entrada);
				extras.putBoolean("minormax", formofmap);
				a.putExtras(extras);
				startActivity(a);
			}
			else
			{
				Dialog d=new Dialog(this);
				d.setTitle("WARNING");
				TextView tv=new TextView(this);
				tv.setText(mensaje_error);
				tv.setGravity(Gravity.CENTER);
				d.setContentView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT));
				d.show();
			}
				break;
		case R.id.bVolver:
			this.finish();
			break;
		}
	}

	private boolean validate() {
		// TODO Auto-generated method stub
		boolean val=true;
		int cont=0;
		if(this.res.length()<2)
		{
			val=false;
			mensaje_error="THIS RESULT DON'T NEED SHOW CIRCUIT";
		}
		if(this.res.length()<3 && res.endsWith("'"))
		{
			val=false;
			mensaje_error="THIS RESULT DON'T NEED SHOW CIRCUIT";
		}
		if(formofmap)
		{
			for(int i=0;i<this.res.length();i++)
				if(res.charAt(i)=='+')
					cont++;
					
		}
		else
		{
			for(int i=0;i<this.res.length();i++)
				if(res.charAt(i)=='(')
					cont++;
		}
		if(formofmap)
		{
			if(cont>9)
				{
				val=false;
				mensaje_error="THIS RESULT IS VERY LARGE";
				}
		}
		else
		{
			if(cont>10)
			{
				val=false;
				mensaje_error="THIS RESULT IS VERY LARGE";
			}
		}
				
		return val;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	

}
