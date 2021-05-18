package com.example.prueba_graf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Gate {
	
//	number of imputs
	int inputs;
	
//	Is true if de gate is AND and False is the gate is OR
	boolean type;
	
//	Is false is the output is negate and true is it not negate
	boolean output_form;
	
	int x,y,heigth,width; //(x,y) is the corner in the left, top of the gate
	
	float[] input_position;
	
	public Gate(int inputs, boolean type, boolean output_form){
		
		this.inputs=inputs;
		this.type=type;
		this.output_form=output_form;
		input_position=new float[inputs];
	}
	
	public void setParams(int x, int y, int heigth, int width){
		this.heigth=heigth;
		this.width=width;
		this.x=x;
		this.y=y;
		
	}
	
//	private void generateInputPos() {
//		// TODO Auto-generated method stub
//		for(int i=0;i<inputs;i++){
//			input_position[i]=
//		}
//	}

	public void drawGate(Canvas canvas, Paint paint){
		
		float oval_dimension= 0.8f;
		float oval_dim_complement=1-oval_dimension;
		float step=heigth/(inputs+1);
		Paint paint2=new Paint();
		
		RectF rect=new RectF((int)(x-oval_dim_complement*width), y, (int)(x+oval_dimension*width), y+heigth);
		paint.setAlpha(230);
		paint.setColor(Color.GRAY);
		canvas.drawRect(new RectF(x,y,x+width,y+heigth), paint);
		
		if(type){
//			For AND Gates
			
			paint.setColor(Color.BLACK);
			canvas.drawArc(rect, -90, 180, false, paint);
			if(!output_form){
				int radius=(int) (heigth*0.1);
				canvas.drawCircle((float)(x+oval_dimension*width+radius-1), y+heigth/2, radius, paint);
			}
			canvas.drawLine((float) (x+oval_dimension*width), y+heigth/2, x+width, y+heigth/2, paint);
			
			
			
			paint2.setColor(Color.CYAN);
			for(int i=0;i<inputs;i++){
				
				canvas.drawLine(x, y+(i+1)*step,  (float) (x+oval_dimension*width/2), y+(i+1)*step, paint);
				canvas.drawPoint(x, y+(i+1)*step, paint2);
			}
		}
		else{
//			For OR GATES
			RectF rect2=new RectF((int)(x+0.20*width-oval_dim_complement*width), y, (int) (x+0.70*oval_dimension*width), y+heigth);
			paint.setColor(Color.BLACK);
			canvas.drawArc(rect, -90, 180, false, paint);
			canvas.drawLine((float) (x+oval_dimension*width), y+heigth/2, x+width, y+heigth/2, paint);
			paint.setColor(Color.WHITE);
			canvas.drawArc(rect2, -90, 180, false, paint);
			
			if(!output_form){
				int radius=(int) (heigth*0.1);
				paint.setColor(Color.BLACK);
				canvas.drawCircle((float)(x+oval_dimension*width+radius-1), y+heigth/2, radius, paint);
			}
			
			for(int i=0;i<inputs;i++){
				canvas.drawLine(x, y+(i+1)*step,  (float) (x+oval_dimension*width/2), y+(i+1)*step, paint);
				canvas.drawPoint(x, y+(i+1)*step, paint2);
			}
			
		}
//		paint2.setColor(Color.BLUE);
////		arreglar
//		for (int i=-40;i<40;i++){
//			canvas.drawCircle(i+x, generateElipse(i+x),3, paint2);
//		}
//		
		
		
	}

	public float generateElipse(float x){
		return (float) Math.sqrt((40*40*+x*x));
	}

}
