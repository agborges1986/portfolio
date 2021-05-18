package com.kaymannsoft.card;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Legend extends View{
	
	String[] labels;
	int[] colors;
	Context cxt;
	
	public Legend(Context context, String[] labels, int[] colors) {
		super(context);
		
		this.labels=labels;
		this.colors=colors;
		this.cxt=context;
		this.setBackgroundResource(R.drawable.legend_shape);
	}
	
	

	public Legend(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}



	public Legend(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}



	public Legend(Context context) {
		super(context);
	
	}



	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		float height=getHeight();
		float width=getWidth()-1;
		
		float marginLeft=5;
		float marginTop=5;
		float marginBetween=5;
		
		int legendLength=labels.length;
		
		float heigth_rect=(height-marginTop*2-((legendLength-1)*marginBetween))/legendLength;
		
		Paint paint=new Paint();
		Paint text_paint=new Paint();
		text_paint.setTypeface(UtilsTextFace.getTextFont(cxt));
		text_paint.setTextSize(14);
		text_paint.setAntiAlias(true);
		text_paint.setStyle(Style.FILL);
		
		for(int i=0;i<legendLength;i++){
			
			paint.setColor(this.colors[i]);
			
			canvas.drawRect(new RectF(marginLeft, i*(heigth_rect+marginBetween)+marginTop, heigth_rect, i*(heigth_rect+marginBetween)+marginTop+heigth_rect), paint);
			canvas.drawText(labels[i], marginLeft+heigth_rect+3,  i*(heigth_rect+marginBetween)+marginTop+heigth_rect*2/3 , text_paint);
		}
		}

	}
	
