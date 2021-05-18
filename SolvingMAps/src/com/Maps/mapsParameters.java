package com.Maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class mapsParameters extends Activity implements OnClickListener, OnItemSelectedListener {
	
	
	TextView introduction,varnumber,minormax,explmapsparam;
	Button accept;
	Spinner choosevar,chooseminormax;
	String[] variables={"2","3","4","5"};
	String[] type_form={"MIN","MAX"};
	int numberofvariables=2,entrada;
	boolean typeofmap=true;  ///type of map is true if the map will be express in min term
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.karnoughmap);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		introduction=(TextView)findViewById(R.id.tvIntroductionMaps);
		varnumber=(TextView)findViewById(R.id.tvNumVAr);
		minormax=(TextView)findViewById(R.id.tvMinorMax);
		explmapsparam=(TextView)findViewById(R.id.tvMAPSexpl);
		accept=(Button)findViewById(R.id.bAceptMapsVar);
		choosevar=(Spinner)findViewById(R.id.sp_choose_var_new);
		chooseminormax=(Spinner)findViewById(R.id.spMinorMax);
		
		Bundle getdata=getIntent().getExtras();
		entrada=getdata.getInt("entrada");
		
		
		accept.setOnClickListener(this);
		choosevar.setOnItemSelectedListener(this);
		chooseminormax.setOnItemSelectedListener(this);
		ArrayAdapter<String> spinAdapter=new ArrayAdapter<String>(mapsParameters.this,android.R.layout.simple_spinner_item,variables);
		spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		choosevar.setAdapter(spinAdapter);
		//choose_var.setSelection(num_var);
		ArrayAdapter<String> spadapter=new ArrayAdapter<String>(mapsParameters.this,android.R.layout.simple_spinner_item,type_form);
		spadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		chooseminormax.setAdapter(spadapter);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.sp_choose_var_new:
			{
				switch(choosevar.getSelectedItemPosition())
				{
				case 0:
					numberofvariables =2;
					break;
				case 1:
					numberofvariables =3;
					break;
				case 2:
					numberofvariables =4;
					break;
				case 3:
					numberofvariables =5;
					break;
				}
			}
			break;
		case R.id.spMinorMax:
			if (chooseminormax.getSelectedItemPosition()==0)
				typeofmap= true;
			else
				typeofmap= false;
			break;
		}
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.bAceptMapsVar:
			////Aqui empiezo la activity para el mapa de Karnough solo teniendo en cuenta que esta es la variante de Mondeja
			///para las ventanas.
			
			switch(entrada)
			{
			case 0:
				Bundle data=new Bundle();
				data.putBoolean("minormax", typeofmap);
				data.putInt("variables", numberofvariables);
				Intent a =new Intent(mapsParameters.this,gridMap.class);
				a.putExtras(data);
				startActivity(a);
				break;
			case 1:						
				Bundle data1=new Bundle();
				data1.putBoolean("minormax", typeofmap);
				data1.putInt("variables", numberofvariables);
				Intent a1 =new Intent(mapsParameters.this,tabla_verdad.class);
				a1.putExtras(data1);
				startActivity(a1);
				break;
			case 2:
				Bundle data2=new Bundle();
				data2.putBoolean("minormax", typeofmap);
				data2.putInt("variables", numberofvariables);
				Intent a2 =new Intent(mapsParameters.this,ecuacion.class);
				a2.putExtras(data2);
				startActivity(a2);
				break;
			}
				
			break;
		}
	}

}
