package com.Maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class gridMap extends Activity implements OnClickListener, OnItemClickListener, OnItemSelectedListener {

	
	GridView mapa,cabecera,cabecera_derecha,cabeceravariables;
	TextView title;
	Button solve;
	private Mapa_Karnaough map_values;
	static int num_var,entrada;
	static boolean formofmap;
	static String[]cabeceralist,derechalist,unos,variableslist;//Unos es el string que se utiliza para pasarle al Adapter los valores del mapa
	int[] list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_k);
		initialize();
		
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
		mapa=(GridView)findViewById(R.id.gVMap);
		cabecera=(GridView)findViewById(R.id.gVCABECERA);
		cabecera_derecha=(GridView)findViewById(R.id.gvDERECHA);
		cabeceravariables=(GridView)findViewById(R.id.gvVariablesCabecera);
		title=(TextView)findViewById(R.id.tvTitleMap);
		solve=(Button)findViewById(R.id.bSolveMap);
		Bundle getdata=getIntent().getExtras();
		num_var=getdata.getInt("variables");
		formofmap=getdata.getBoolean("minormax");
		map_values=new Mapa_Karnaough(num_var);
		
		if(formofmap)
			for(int i=0;i<map_values.table.length;i++)
				map_values.table[i]=0;
		else
			for(int i=0;i<map_values.table.length;i++)
				map_values.table[i]=1;
		
		mapa.setOnItemClickListener(this);
		solve.setOnClickListener(this);
		//Inicializo las listas de los adapters y la magnitud del mapa con setMap
		setMap();
		
		GridAdapter adapter = new GridAdapter(unos);
		mapa.setAdapter(adapter);
		cabeceraderechaAdapter cabecera_adapter= new cabeceraderechaAdapter(cabeceralist);
		cabecera.setAdapter(cabecera_adapter);
		cabeceraderechaAdapter derecha_adapter= new cabeceraderechaAdapter(derechalist);
		cabecera_derecha.setAdapter(derecha_adapter);
		cabeceravariables variablesAdapter=new cabeceravariables(variableslist);
		cabeceravariables.setAdapter(variablesAdapter);
	}

	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		switch(arg0.getId())
		{
		case R.id.gVMap:
			int temp=0;
			
			if(unos[arg2]=="1")
				temp=1;
			else if(unos[arg2]=="0")
				temp=0;
			else if(unos[arg2]=="*")
				temp=2;
			switch(temp)
			{
			case 0:
				gridMap.unos[arg2]="1";
				map_values.SetOnesFunction(list[arg2]);
				break;
			case 1:
				gridMap.unos[arg2]="*";
				map_values.SetDontCareFunction(list[arg2]);
				break;
			case 2:
				gridMap.unos[arg2]="0";
				map_values.SetZerosFunction(list[arg2]);
				break;
			}
			GridAdapter adapter = new GridAdapter(unos);
			mapa.setAdapter(adapter);
			break;
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.bSolveMap:
			String res=new String();
			if(formofmap)
				res=map_values.Total_Solution(1);
			else
				res=map_values.Total_Solution(0);
			Intent a=new Intent(gridMap.this,resultado.class);
			Bundle extras=new Bundle();
			extras.putString("res", res);
			extras.putInt("variables",num_var);
			extras.putBoolean("minormax", formofmap);
			extras.putInt("entrada", 0);//Este extra se utiliza para saber en la activity resultado
												//hacia que activity debe virar cuando se le presiona el boton Volver atras
												// 0 para mapa, 1 para tabla, 2 para funcion.
			a.putExtras(extras);
			startActivity(a);
			break;
		
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		
		
	}

	public void setMap(){

		int z=0;
		list=new int[map_values.height*map_values.witdh];
		unos=new String[map_values.height*map_values.witdh];
		String temp="1";
		this.cabecera_derecha.setNumColumns(1);
		this.cabeceravariables.setNumColumns(1);
		if(formofmap)
		{
			 temp="0";
		}
		for(int i=0;i<map_values.height;i++){
			for(int j=0;j<map_values.witdh;j++){
				list[z]=map_values.mapa[i][j];
				unos[z]=temp;		
				z++;
			}
		}
		
		switch(num_var){
		case 2:
			mapa.setNumColumns(2);
			cabecera.setNumColumns(2);
			cabeceralist=new String[2];
			variableslist=new String[1];
			derechalist=new String[2];
			variableslist[0]="A\\B";
			cabeceralist[0]=derechalist[0]="0";
			cabeceralist[1]=derechalist[1]="1";
			break;
		case 3:
			mapa.setNumColumns(4);
		    cabecera.setNumColumns(4);
			cabeceralist=new String[4];
			derechalist=new String[2];
			variableslist=new String[1];
			cabeceralist[0]="00";
			cabeceralist[1]="01";
			cabeceralist[2]="11";
			cabeceralist[3]="10";
			
			variableslist[0]="A\\BC";
			derechalist[0]="0";
			derechalist[1]="1";
			break;
		case 4:
			mapa.setNumColumns(4);
		    cabecera.setNumColumns(4);
			cabeceralist=new String[4];
			derechalist=new String[4];
			variableslist=new String[1];
			cabeceralist[0]="00";
			cabeceralist[1]="01";
			cabeceralist[2]="11";
			cabeceralist[3]="10";
			
			variableslist[0]="AB\\CD";
			
			derechalist[0]="00";
			derechalist[1]="01";
			derechalist[2]="11";
			derechalist[3]="10";
			break;
		case 5:
			mapa.setNumColumns(8);
		    cabecera.setNumColumns(8);
		    cabeceralist=new String[16];
			derechalist=new String[4];
			variableslist=new String[2];
			
			variableslist[0]="A";
			variableslist[1]="BC\\DE";
			
			cabeceralist[0]="";
			cabeceralist[1]=cabeceralist[5]="A";
			cabeceralist[2]="=";
			cabeceralist[3]="0";
			cabeceralist[4]="";
			cabeceralist[6]="=";
			cabeceralist[7]="1";
			
			cabeceralist[8]=cabeceralist[12]="00";
			cabeceralist[9]=cabeceralist[13]="01";
			cabeceralist[10]=cabeceralist[14]="11";
			cabeceralist[11]=cabeceralist[15]="10";
			
			derechalist[0]="00";
			derechalist[1]="01";
			derechalist[2]="11";
			derechalist[3]="10";
			break;
		}
				
	}
	
	public class GridAdapter extends ArrayAdapter<String>
{

	GridAdapter(String[] lista){
		super(gridMap.this, android.R.layout.simple_gallery_item,lista);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=convertView;
		if (row==null){
			LayoutInflater inflater=getLayoutInflater();
			row=inflater.inflate(R.layout.cell,null);
		}
		
			
		
			((TextView)row.findViewById(R.id.tvItem)).setText(unos[position]);
		
			((TextView)row.findViewById(R.id.tvSUBItem)).setText(""+list[position]);
			if (num_var==5)
				((TextView)row.findViewById(R.id.tvItem)).setTextSize(16);
				((TextView)row.findViewById(R.id.tvSUBItem)).setTextSize(8);
		return row;
	}
	
}
	public class cabeceraAdapter extends ArrayAdapter<String>{

		public cabeceraAdapter(String[] lista) {
			super(gridMap.this,android.R.layout.simple_gallery_item,lista);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=convertView;
			if(view==null)
			{
				LayoutInflater inflater=getLayoutInflater();
				view=inflater.inflate(R.layout.cell,null);
			}
			((TextView)view.findViewById(R.id.tvItem)).setText(cabeceralist[position]);
			if(num_var==3)
				((TextView)view.findViewById(R.id.tvItem)).setTextSize(18);
			
			if (num_var==5)
			{
				((TextView)view.findViewById(R.id.tvItem)).setTextSize(14);
				
			}
			return view;
		}
		
		
	}
	public class cabeceraderechaAdapter extends ArrayAdapter<String>{
		String[] list;

		public cabeceraderechaAdapter(String[] lista) {
			super(gridMap.this,android.R.layout.simple_gallery_item,lista);
			list=lista;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=convertView;
			if(view==null)
			{
				LayoutInflater inflater=getLayoutInflater();
				view=inflater.inflate(R.layout.cell,null);
			}
			
			((TextView)view.findViewById(R.id.tvItem)).setText(list[position]);
			
			if (num_var==4)
			{
				if(position==0)
					((TextView)view.findViewById(R.id.tvItem)).setTextSize(18);
				else
					((TextView)view.findViewById(R.id.tvItem)).setTextSize(18);
			}
			if(num_var==5)
				((TextView)view.findViewById(R.id.tvItem)).setTextSize(16);
			return view;
		}
		
		
	}
	public class cabeceravariables extends ArrayAdapter<String>{
		String[] list;

		public cabeceravariables(String[] lista) {
			super(gridMap.this,android.R.layout.simple_gallery_item,lista);
			list=lista;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=convertView;
			if(view==null)
			{
				LayoutInflater inflater=getLayoutInflater();
				view=inflater.inflate(R.layout.cell,null);
			}
			
			((TextView)view.findViewById(R.id.tvItem)).setText(list[position]);
			
			if (num_var==4)
			{
				if(position==0)
					((TextView)view.findViewById(R.id.tvItem)).setTextSize(16);
				else
					((TextView)view.findViewById(R.id.tvItem)).setTextSize(18);
			}
			if(num_var==5)
				((TextView)view.findViewById(R.id.tvItem)).setTextSize(16);
			return view;
		}
		
		
	}
}
