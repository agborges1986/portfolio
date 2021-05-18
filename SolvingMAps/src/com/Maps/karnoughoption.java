package com.Maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class karnoughoption extends Activity implements OnClickListener, OnCheckedChangeListener {

	Button selectionAccept;
	RadioGroup selectFormMap;
	TextView backgroundMaps,forInputExpl;
	static int option=2;
	
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		forInputExpl.setText("hola");
		switch(checkedId){
			case R.id.Eq:
				option=1;
				forInputExpl.setText("Uses Logical Equation as input for KMap reduction");
				break;
			case R.id.Map:
				option=2;
				forInputExpl.setText("Uses a map as input for KMap reduction");
				break;
			case R.id.TrueTable:
				option=3;
				forInputExpl.setText("Uses Truth Table as input for KMap reduction");
				break;
		}

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){	
			case R.id.acceptFormInput:
				switch(option){
					case 1://Aqui abro la activity Equation
						Intent b=new Intent(karnoughoption.this,mapsParameters.class);
						Bundle extra2=new Bundle();
						extra2.putInt("entrada",2);
						b.putExtras(extra2);//Para entrada mapa, 0
											//Para entrada tabla, 1
											//Para entrada funcion, 2
						
						startActivity(b);
						break;
					case 2://Aqui abro la activity MAps Form
						Intent a=new Intent(karnoughoption.this,mapsParameters.class);
						Bundle extras=new Bundle();
						extras.putInt("entrada", 0);//Para entrada mapa, 0
													//Para entrada tabla, 1
													//Para entrada funcion, 2
						a.putExtras(extras);
						startActivity(a);
						break;
					case 3://Aqui abro la activity TruthTable
						Intent a1=new Intent(karnoughoption.this,mapsParameters.class);
						Bundle extras1=new Bundle();
						extras1.putInt("entrada", 1);//Para entrada mapa, 0
													//Para entrada tabla, 1
													//Para entrada funcion, 2
						a1.putExtras(extras1);
						startActivity(a1);
						break;
				}
				break;
			case R.id.FormsInputExpl:
				break;
			case R.id.text_background_maps:
				break;
			case R.id.SelectInputMaps:
				break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.karnoughoption);
		initialize();
	}
	public void initialize(){
		option=1;
		selectionAccept=(Button)findViewById(R.id.acceptFormInput);
		selectFormMap=(RadioGroup)findViewById(R.id.SelectInputMaps);
		backgroundMaps=(TextView)findViewById(R.id.text_background_maps);
		forInputExpl=(TextView)findViewById(R.id.FormsInputExpl);
		selectionAccept.setOnClickListener(this);
		backgroundMaps.setOnClickListener(this);
		forInputExpl.setOnClickListener(this);
		selectFormMap.setOnCheckedChangeListener(this);
		backgroundMaps.setText("Please choose your desire input type");
		forInputExpl.setText("Uses Logical Equation as input for KMap reduction");
	}
	

}
