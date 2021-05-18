package com.Maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class tabla_verdad extends Activity implements OnClickListener, OnItemClickListener,OnItemSelectedListener {
	
	GridView tabla,cabeceratabla;
	TextView cabecera;
	Button solveTable;
	static int num_var;
	private Mapa_Karnaough mapa;
	boolean formofmap;
	String[] list,listcabecera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.true_table);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
		
		//Inicializo las vistas de los objetos con la vista del layout
		tabla=(GridView)findViewById(R.id.gVTrueTable);
		cabeceratabla=(GridView)findViewById(R.id.gVTablaCabecera);
		cabecera=(TextView)findViewById(R.id.tVTrueTable);
		solveTable=(Button) findViewById(R.id.bSolveTable);
		
		//Extraigo los datos pasados por la activity anterior 
		Bundle getdata=getIntent().getExtras();
		num_var=getdata.getInt("variables");
		formofmap=getdata.getBoolean("minormax");
		mapa=new Mapa_Karnaough(tabla_verdad.num_var);
		if(formofmap)
			for(int i=0;i<mapa.table.length;i++)
				mapa.table[i]=0;
		else
			for(int i=0;i<mapa.table.length;i++)
				mapa.table[i]=1;
		
		//Inicializo los manejadores de Eventos
		solveTable.setOnClickListener(this);
		tabla.setOnItemClickListener(this);
		tabla.setOnItemSelectedListener(this);
		setTabla();
		tabla.setNumColumns(num_var+1);
		cabeceratabla.setNumColumns(num_var+1);
		ArrayAdapter<String> adapter_cabecera=new ArrayAdapter<String>(tabla_verdad.this,android.R.layout.simple_gallery_item,listcabecera);
		cabeceratabla.setAdapter(adapter_cabecera);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(tabla_verdad.this,android.R.layout.simple_gallery_item,list);
		tabla.setAdapter(adapter);
		
		
		
		
	}

	private void setTabla() {
		// TODO Auto-generated method stub
		
		
		list=new String[(tabla_verdad.num_var+1)*((int)Math.pow(2, tabla_verdad.num_var))];
		
		listcabecera=new String[tabla_verdad.num_var+1];
		String temp="1";
		int cont=0; //para ir contando las las filas de la tabla
		int des=tabla_verdad.num_var-1,a=0;//para desplazar binariamente el cont y lograr el valor binario de cada posicion
		
		if(formofmap)
			temp="0";
		for(int i=0;i<list.length;i++)
		{
			if(a==num_var)
			{
				cont++;
				des=num_var-1;
				a=0;
				list[i]=temp;
			}
			else
			{
				
				if(((cont>>des)& 1)==1)
					list[i]="1";
				else
					list[i]="0";
				a=a+1;
				des=des-1;
			}
		}
		for(int i=0;i<listcabecera.length;i++)
			if (i<num_var)
			{	
				listcabecera[i]=""+(char)('A'+i);
				
			}
			else
				listcabecera[i]="F(x)";
				
					
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
		case R.id.gVTrueTable:		
			if((arg2+1)%(num_var+1)==0)
			{
				int temp=0;
				if (list[arg2]=="1")
					temp=1;
				else if(list[arg2]=="*")
					temp=2;
				else if(list[arg2]=="0")
					temp=0;
				switch(temp)
				{
				case 0:
					list[arg2]="1";
					mapa.SetOnesFunction(((arg2+1)/(num_var+1)-1));
					//escribir aqui los unos en el mapa;
					break;
				case 1:
					list[arg2]="*";
					mapa.SetDontCareFunction(((arg2+1)/(num_var+1))-1);
					break;
				case 2:
					list[arg2]="0";
					mapa.SetZerosFunction(((arg2+1)/(num_var+1))-1);
					break;
				}
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(tabla_verdad.this,android.R.layout.simple_gallery_item,list);
				tabla.setAdapter(adapter);
			}
			
			break;
		}
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.bSolveTable:
			String res=new String("ert");
			if(formofmap)
				res=mapa.Total_Solution(1);
			else
				res=mapa.Total_Solution(0);
			
			Intent a=new Intent(tabla_verdad.this,resultado.class);
			Bundle extras=new Bundle();
			extras.putString("res", res);
			extras.putInt("variables",num_var);
			extras.putBoolean("minormax", formofmap);
			extras.putInt("entrada", 1);//Este extra se utiliza para saber en la activity resultado
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
	
	

}

