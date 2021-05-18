package com.Maps;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class map extends Activity implements OnClickListener, OnItemSelectedListener {

	GridView mapa;
	Mapa_Karnaough map_values;
	Spinner choose_var,min_or_max;
	TextView spinText,expl_text;
	Button calc_map;
	static int num_var;
	ListAdapter adap=new ListAdapter();
	String[] variables={"2","3","4","5"};
	String[] type_form={"min terms","max terms"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		initialize();
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.bcalcmap:
			//Aqui para calcular el mapa
			break;
		}
	}
	public void initialize(){
		spinText=(TextView)findViewById(R.id.tvspin);
		expl_text=(TextView)findViewById(R.id.tvexplmap);
		calc_map=(Button)findViewById(R.id.bcalcmap);
		mapa=(GridView)findViewById(R.id.gridmap);
		choose_var=(Spinner)findViewById(R.id.sp_choose_var);
		min_or_max=(Spinner)findViewById(R.id.spmin_or_max);
		choose_var.setOnItemSelectedListener(this);
		min_or_max.setOnItemSelectedListener(this);
		mapa.setOnItemSelectedListener(this);
		calc_map.setOnClickListener(this);
		num_var=4;
		expl_text.setText(""+choose_var.getId());
		setMapa(num_var);
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(map.this,android.R.layout.simple_gallery_item, adap.toArray());
		mapa.setAdapter(adapter);
		ArrayAdapter<String> spinAdapter=new ArrayAdapter<String>(map.this,android.R.layout.simple_spinner_item,variables);
		choose_var.setAdapter(spinAdapter);
		//choose_var.setSelection(num_var);
		ArrayAdapter<String> spadapter=new ArrayAdapter<String>(map.this,android.R.layout.simple_spinner_item,type_form);
		min_or_max.setAdapter(spadapter);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		//PARA EL SPINNER DE BUSCAR LAS VARIABLES
		
		switch(arg0.getId()){
		case R.id.sp_choose_var:
			{
				int pos=choose_var.getSelectedItemPosition();
				switch(pos)
				{
				case 0:
					setMapa(2);
					mapa.setNumColumns(2);
					ArrayAdapter<String> adapter= new ArrayAdapter<String>(map.this,android.R.layout.simple_gallery_item, adap.toArray());
					mapa.setAdapter(adapter);
					break;
				case 1:
					setMapa(3);
					mapa.setNumColumns(4);
					ArrayAdapter<String> adapter1= new ArrayAdapter<String>(map.this,android.R.layout.simple_gallery_item, adap.toArray());
					mapa.setAdapter(adapter1);
					break;
				case 2:
					setMapa(4);
					mapa.setNumColumns(4);
					ArrayAdapter<String> adapter2= new ArrayAdapter<String>(map.this,android.R.layout.simple_gallery_item, adap.toArray());
					mapa.setAdapter(adapter2);
					break;
				case 3:
					setMapa(5);
					mapa.setNumColumns(8);
					ArrayAdapter<String> adapter3= new ArrayAdapter<String>(map.this,android.R.layout.simple_gallery_item, adap.toArray());
					mapa.setAdapter(adapter3);
					break;	
				}
			break;
			}
			//AQUI SE ACABA PARA EL SPINNER DE LAS VARIABLES
			
			//AQUI PARA ESCOGER SI ES MIN OR MAX TERMS
		case R.id.spmin_or_max:
			{
				switch(min_or_max.getSelectedItemPosition())
				{
				case 0:
					expl_text.setText("MIN TERMS");
					break;

				case 1:
					expl_text.setText("MAX TERMS");
					break;
				
				}
				break;
			}
			//AQUI SE TERMINA DE ESCOGER SI ES MIN OR MAX TERMS
		case R.id.gridmap:
			{
				break;
			}
		}
		
		
		
		
		

	
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void setMapa(int var){
		adap.clear();
		map_values=new Mapa_Karnaough(var);
		for(int i=0;i<map_values.height;i++){
			for(int j=0;j<map_values.witdh;j++){
				adap.add(""+map_values.mapa[i][j]);
			}
		}
		
	}
	public class ListAdapter extends ArrayList<String> 
	{

		/**
		 * //Para hacer un adaptador que se pueda utilizar con String []
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String[] toArray() {
			// TODO Auto-generated method stub
			String[] temp=new String[this.size()];
			for (int i=0;i<this.size();i++)
			{
				temp[i]=(String) this.get(i);
			}
			return temp;
		}
		
	}

}

