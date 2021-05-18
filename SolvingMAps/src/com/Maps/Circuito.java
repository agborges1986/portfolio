package com.Maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Circuito extends Activity implements OnClickListener{

	
	String solution;
	Bitmap[] my_circuit;
	static int[][] matriz_sumandos;
	int num_var,entrada,num_sumandos;
	static int escala;
	boolean formofmap,and_or;
	Button nand_or_nor,closeMaps;
	LinearLayout parent,Viewcontainer,ButtonContainer;
	LinearLayout lay;
	ScrollView viewcircuit;
	View circuit_view;
	Dibujo circuit;
	private int compHEIGTH;
	private int compWIDTH;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		//Obtengo los datos de la activity anterior
		Bundle getdata=getIntent().getExtras();
		solution=getdata.getString("res");
		num_var=getdata.getInt("variables");
		entrada = getdata.getInt("entrada");
		formofmap=getdata.getBoolean("minormax");
		and_or=true;
		calcSumandos();
		if(this.num_sumandos>8)
			Circuito.escala=1;
		else
			Circuito.escala=0;
		
		loadImages(and_or);
		setContentView(R.layout.circuit);
						
		closeMaps=(Button)findViewById(R.id.bCloseMAps);
		closeMaps.setOnClickListener(this);
		nand_or_nor=(Button)findViewById(R.id.bNANDNOR);
		nand_or_nor.setOnClickListener(this);
		if(formofmap)
			nand_or_nor.setText("TO  NAND FORM");
		else
			nand_or_nor.setText("TO NOR FORM");	
				
		circuit=(Dibujo)findViewById(R.id.viewCircuit);
		
		circuit.setParams(my_circuit, num_sumandos, num_var, compHEIGTH, compWIDTH, formofmap, solution, matriz_sumandos,escala);
		
		
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.bCloseMAps:
			this.finish();
			break;
		case R.id.bNANDNOR:
			
			if(and_or)
			{
				this.loadImages(false);
				nand_or_nor.setText("TO AND-OR FORM");
				and_or=false;
			}
			else
			{
				this.loadImages(true);
				if (formofmap)
					nand_or_nor.setText("TO  NAND FORM");
				else
					nand_or_nor.setText("TO  NOR FORM");
				and_or=true;
			}
			circuit.setParams(my_circuit, num_sumandos, num_var, compHEIGTH, compWIDTH, formofmap, solution, matriz_sumandos,escala);
			break;
		}
		
	}
	private void calcSumandos() {
		// TODO Auto-generated method stub
		if (formofmap)
		{
			//Inicializo las variables necesarias
			int cont=0;
			for(int i=0;i<solution.length();i++)//cuento cuantos sumandos hay
			{
				if(solution.charAt(i)=='+')
					cont++;
			}
			int[][] sumandos=new int[cont+1][num_var+1];
			num_sumandos=cont+1;
			if(num_sumandos>1 && num_sumandos<6)
				my_circuit=new Bitmap[num_sumandos+1];
			else
			{
				if(num_sumandos>5 && num_sumandos<11)
					my_circuit=new Bitmap[num_sumandos+3];
				else
					if(num_sumandos==1)
						my_circuit=new Bitmap[num_sumandos];
			}
					
			
			for (int i=0;i<cont+1;i++)//Se inicializa con el valor 3 toda la matriz de manera que solo se cambie despues, si aparecen las variables
				for(int j=0;j<num_var+1;j++)//el ultimo valor de la fila esta reservado para contar cuantas variables tiene el sumando.
					if(j==num_var)
						sumandos[i][j]=0;
					else
						sumandos[i][j]=3;
			
			char[]variables=new char[num_var];
			for(int i=0;i<num_var;i++)
		       {
		           variables[i]=(char)('A'+i);
		       }
			//Fin de la Inicializacion
			cont=0;
			for(int i=0;i<solution.length();i++)
			{
				
				String neg="'";
				if(solution.charAt(i)!=neg.charAt(0))
				{
					if(solution.charAt(i)=='+')
						cont++;
					else
					{
						//Aqui encuentro las variables que se encuentran en el sumando
						for(int z=0;z<variables.length;z++)
							if (solution.charAt(i)==variables[z])
								if(i<solution.length()-1 && solution.charAt(i+1)==neg.charAt(0))
								{
									sumandos[cont][z]=0;
									sumandos[cont][num_var]++;
								}
								else
								{
									sumandos[cont][z]=1;
									sumandos[cont][num_var]++;
								}
					}
				}
			}
			matriz_sumandos=sumandos;
		}
		else
		{//Aqui para el caso de max terms
			int cont=0;
			for(int i=0;i<solution.length();i++)//cuento cuantos sumandos hay
			{
				if(solution.charAt(i)==')')
					cont++;
			}
			int[][] sumandos=new int[cont][num_var+1];
			num_sumandos=cont;
			if(num_sumandos>1 && num_sumandos<6)
				my_circuit=new Bitmap[num_sumandos+1];
			else
			{
				if(num_sumandos>5 && num_sumandos<11)
					my_circuit=new Bitmap[num_sumandos+3];
				else
					if(num_sumandos==1)
						my_circuit=new Bitmap[num_sumandos];
			}
					
			for (int i=0;i<num_sumandos;i++)//Se inicializa con el valor 3 toda la matriz de manera que solo se cambie despues, si aparecen las variables
				for(int j=0;j<num_var+1;j++)//el ultimo valor de la fila esta reservado para contar cuantas variables tiene el sumando.
					if(j==num_var)
						sumandos[i][j]=0;
					else
						sumandos[i][j]=3;
			char[]variables=new char[num_var];
			for(int i=0;i<num_var;i++)
		       {
		           variables[i]=(char)('A'+i);
		       }
			//Fin de la Inicializacion
			cont=-1;
			for(int i=0;i<solution.length();i++)
			{
				
				String neg="'";
				if(solution.charAt(i)!=neg.charAt(0) && solution.charAt(i)!='+' && solution.charAt(i)!=')')
				{
					if(solution.charAt(i)=='(')
						cont++;
					else
					{
						//Aqui encuentro las variables que se encuentran en el sumando
						for(int z=0;z<variables.length;z++)
							if (solution.charAt(i)==variables[z])
								if(i<solution.length()-1 && solution.charAt(i+1)==neg.charAt(0))
								{
									sumandos[cont][z]=0;
									sumandos[cont][num_var]++;
								}
								else
								{
									sumandos[cont][z]=1;
									sumandos[cont][num_var]++;
								}
					}
				}
			}
			matriz_sumandos=sumandos;
		}
		
	}
	private void loadImages(boolean and_or) {
		// TODO Auto-generated method stub
		if (num_sumandos>1)
		{
			if (Circuito.escala==0)
			{
				for (int i=0;i<num_sumandos;i++)
				{
					
					switch(matriz_sumandos[i][num_var])
					{
					case 0:
						break;
					case 1:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.and1);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nand1);
						}
						else
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.or1);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nor1);
						}
						break;
					case 2:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
						}
						break;
					case 3:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.and3);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nand3);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.or3);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nor3);
						}
						break;
					case 4:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.and4);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nand4);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.or4);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nor4);
						}
						break;
					case 5:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
						}
						break;
					}
							
				}
				switch(num_sumandos)
				{
				case 1:
					break;
				case 2:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
					}
					break;
				case 3:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or3);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand3);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and3);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor3);
					}
					break;
				case 4:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or4);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand4);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and4);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor4);
					}
					break;
				case 5:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
					}
					break;
				case 6:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.or1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.and1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.and1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.or1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
						}
							
					}
					break;
				case 7:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
						}
							
					}
					break;
				case 8:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.or3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nand3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.and3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nor3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
						}
							
					}
					break;
				case 9:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.or4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nand4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.and4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nor4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
						}
							
					}
					break;
				case 10:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nand5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.nor5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.nor2);
						}
							
					}
					break;
				
				}
			}
			else //AQUI PARA DIBUJAR CON UNA ESCALA MENOR
			{

				for (int i=0;i<num_sumandos;i++)
				{
					
					switch(matriz_sumandos[i][num_var])
					{
					case 0:
						break;
					case 1:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and1);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand1);
						}
						else
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or1);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor1);
						}
						break;
					case 2:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
						}
						break;
					case 3:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and3);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand3);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or3);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor3);
						}
						break;
					case 4:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and4);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand4);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or4);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor4);
						}
						break;
					case 5:
						if(formofmap)
						{
							if(and_or)
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
						}
						else
						{
							if(and_or)
							{
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							}
							else
								my_circuit[i]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
						}
						break;
					}
							
				}
				switch(num_sumandos)
				{
				case 1:
					break;
				case 2:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
					}
					break;
				case 3:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or3);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand3);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and3);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor3);
					}
					break;
				case 4:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or4);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand4);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and4);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor4);
					}
					break;
				case 5:
					if(formofmap)
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
					}
					else
					{
						if(and_or)
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
						else
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
					}
					break;
				case 6:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or1);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
						}
							
					}
					break;
				case 7:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
						}
							
					}
					break;
				case 8:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor3);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
						}
							
					}
					break;
				case 9:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor4);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
						}
							
					}
					break;
				case 10:
					if(formofmap)
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_or2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nand2);
						}
					}
					else
					{
						if(and_or)
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_and2);
						}
						else
						{
							my_circuit[num_sumandos]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
							my_circuit[num_sumandos+1]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor5);
							my_circuit[num_sumandos+2]=BitmapFactory.decodeResource(getResources(), R.raw.scale_nor2);
						}
							
					}
					break;
				
				}
			
			}
		}
		else// Aqui para si existe un solo sumando, mostrar solamente una compuerta
			if(num_sumandos==1)
			{			
				switch(matriz_sumandos[0][num_var])
				{
				case 0:
					break;
				case 1:
					if(formofmap)
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.and1);
					else
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.or1);
					break;
				case 2:
					if(formofmap)
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.and2);
					else
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.or2);
					break;
				case 3:
					if(formofmap)
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.and3);
					else
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.or3);
					break;
				case 4:
					if(formofmap)
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.and4);
					else
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.or4);
					break;
				case 5:
					if(formofmap)
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.and5);
					else
						my_circuit[0]=BitmapFactory.decodeResource(getResources(), R.raw.or5);
					break;
				}
		
			
		}
		compHEIGTH=my_circuit[0].getHeight();
		compWIDTH=my_circuit[0].getWidth();
	}
	

}
