package com.Maps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ecuacion extends Activity implements OnClickListener{

	TextView header,expl;
	EditText ecuacion;
	Button solve;
	int num_var;
	boolean formofmap;
	private Mapa_Karnaough mapa;
	Logical_Function function;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecuacion);
		initialize();
		
	}

	private void initialize() {
		// TODO Auto-generated method stub
		expl=(TextView)findViewById(R.id.tvEcuaExpl);
		ecuacion=(EditText)findViewById(R.id.eTecuacion);
		header=(TextView)findViewById(R.id.tvEcuacionHeader);
		solve=(Button) findViewById(R.id.bSolveEcuacion);
		ecuacion.setInputType(InputType.TYPE_CLASS_TEXT);
		//Extraigo los datos del Intent anterior
		Bundle getdata=getIntent().getExtras();
		num_var=getdata.getInt("variables");
		formofmap=getdata.getBoolean("minormax");
		//
		//Aplico el listener para el click sobre el boton
		solve.setOnClickListener(this);
	}

	
	

	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bSolveEcuacion:
			String solution;
			boolean validator=validate(ecuacion.getText().toString().trim());
			if(validator)
			{
				function=new Logical_Function(ecuacion.getText().toString().trim(),num_var);
				Logical_Function.setMin_or_max(formofmap);
				mapa=function.toMapa(num_var);
				
				if(formofmap)
					solution=mapa.Total_Solution(1);
				else
					solution=mapa.Total_Solution(0);
				
				
				Intent a=new Intent(ecuacion.this,resultado.class);
				Bundle extras=new Bundle();
				extras.putString("res", solution);
				extras.putInt("variables",num_var);
				extras.putInt("entrada",2);
				extras.putBoolean("minormax", formofmap);
				a.putExtras(extras);
				startActivity(a);
			}
			else
			{
				Dialog d=new Dialog(this);
				d.setTitle("ERROR IN EQUATION FORM");
				TextView tv=new TextView(this);
				tv.setText("Please re-write equation");
				tv.setGravity(Gravity.CENTER);
				d.setContentView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT));
				d.show();
			}
			break;
			
		}
	}

	private boolean validate(String entrada) {
		// TODO Auto-generated method stub
		//Para validar la ecuacion
		
		String neg="'";
		boolean val=true;
		if(entrada.length()>0)
		{
			if(this.formofmap)
			{
				for(int i=0;i<entrada.length();i++)
					if(((entrada.charAt(i)>='A') && entrada.charAt(i)<(char)'A'+num_var) || entrada.charAt(i)==neg.charAt(0)||entrada.charAt(i)=='+')
					{
						if(i<entrada.length()-1)
						{
							if(entrada.charAt(i)=='+'&& (entrada.charAt(i+1)<'A' ||entrada.charAt(i+1)>='A'+num_var))
								val = false;
							if(entrada.charAt(i)==neg.charAt(0) && ((entrada.charAt(i+1)<'A' ||entrada.charAt(i+1)>='A'+num_var) && entrada.charAt(i+1)!='+'))
								val=false;
						}
						if(i==entrada.length()-1 && entrada.charAt(i)=='+')
							val=false;
					}
					else
						val=false;
			}
			else
			{
				for(int i=0;i<entrada.length();i++)
					if(((entrada.charAt(i)>='A') && entrada.charAt(i)<(char)'A'+num_var) || entrada.charAt(i)==neg.charAt(0)||entrada.charAt(i)=='+'||entrada.charAt(i)==')'||entrada.charAt(i)=='(')
					{
						if(i<entrada.length()-1)
						{
							if(entrada.charAt(i)=='+'&& (entrada.charAt(i+1)<'A' && entrada.charAt(i+1)>='A'+num_var))
								val = false;
							if(entrada.charAt(i)==neg.charAt(0) && (entrada.charAt(i+1)!='+' && entrada.charAt(i+1)!=')' && entrada.charAt(i+1)!='('))
								val=false;
							if(entrada.charAt(i)==')'&& (entrada.charAt(i+1)!='('))
								val=false;
							if(entrada.charAt(i)=='('&& (entrada.charAt(i+1)<'A'&& entrada.charAt(i+1)>='A'+num_var))
								val=false;
						}
	//					if(i==entrada.length()-1 && entrada.charAt(i)!=')')
	//						val=false;
					}
					else
						val=false;
			}
		}
		else
			val=false;
		
		return val;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		
	}
	

}
