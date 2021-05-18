package com.Maps;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class aplicationMenu extends Activity implements OnClickListener, OnCheckedChangeListener {

	TextView Menu_expl,option_expl;
	RadioGroup option;
	Button accept;
	static int press_menu=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialize();
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.accept:
					switch(press_menu){
						case 1:
							Intent a=new Intent(aplicationMenu.this,karnoughoption.class);
							startActivity(a);	break;
						case 2:	break;
						case 3:	break;
						case 4:	break;
					}
					
					break;
			case R.id.menu_expl:
					Menu_expl.setBackgroundColor(Color.CYAN);	break;
		}
	}
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch(arg1){
		case R.id.radio0:
			press_menu=1;
			Menu_expl.setText("Mapas de Karnough: explicacion ");
			break;
		case R.id.radio1:
			press_menu=2;
			Menu_expl.setText("CAlculadora: explicacion");
			break;
		case R.id.radio2:
			press_menu=3;
			Menu_expl.setText("Diseño de cache: explicación ");
			break;
		case R.id.radio3:
			press_menu=4;
			Menu_expl.setText("Diseño de RAM: explicación  ");
			break;
		}

	}
	public void initialize(){
		Menu_expl=(TextView)findViewById(R.id.menu_expl);
		option_expl=(TextView)findViewById(R.id.option_expl);
		option=(RadioGroup)findViewById(R.id.radioGroup1);
		accept=(Button)findViewById(R.id.accept);
		Menu_expl.setOnClickListener(this);
		option.setOnCheckedChangeListener(this);
		accept.setOnClickListener(this);
	}
	

}
