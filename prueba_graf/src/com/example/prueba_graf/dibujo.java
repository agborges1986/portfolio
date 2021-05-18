package com.example.prueba_graf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class dibujo extends View{
	
	Gate gate=new Gate(3,false,false);
	int x,y;

	public dibujo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		x=canvas.getHeight();
		y=canvas.getWidth();
		gate.setParams(x/4, y/4, y/4, x/4);
		Paint paint=new Paint();
		paint.setStrokeWidth(1);
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		gate.drawGate(canvas, paint);
		
		gate.type=true;
		gate.setParams(x/4, y/4+y/4, y/4, x/4);
		gate.drawGate(canvas, paint);
	}
	
	

}
