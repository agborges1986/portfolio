package com.Maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Dibujo extends View {

	Bitmap[] my_circuit;
	static int[][] matriz_sumandos;
	Paint paint_line=new Paint();
	Paint paint_circle_not,text_paint;
	int num_sumandos,num_var,zones;
	String res;
	String variables=new String();
	boolean formofmap;
	private Paint paint_circle_not1;
	static int compHEIGTH,compWIDTH,width,height;
	boolean validate;
	private Display mDisplay;
	public Dibujo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		Para calcular la matriz sumandos que va a permitir dibujar el circuito
//		INICIALIZACION DE LOS PAINT
		mDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		paint_line=new Paint();
		paint_circle_not=new Paint();
		paint_circle_not1=new Paint();
		paint_line=new Paint();
		paint_line.setColor(Color.BLACK);
		paint_line.setStrokeWidth(2);
		paint_circle_not.setStrokeWidth(2);
		paint_circle_not.setColor(Color.BLACK);
		paint_circle_not1.setColor(Color.WHITE);
		paint_circle_not1.setStrokeWidth(1);
		text_paint=new Paint();
		text_paint.setTextSize(24);
		text_paint.setTextAlign(Align.CENTER);
	}
	
	public Dibujo(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		paint_line=new Paint();
		paint_circle_not=new Paint();
		paint_line=new Paint();
		paint_circle_not1=new Paint();
		paint_line.setColor(Color.BLACK);
		paint_line.setStrokeWidth(3);
		paint_circle_not.setStrokeWidth(2);
		paint_circle_not.setColor(Color.BLACK);
		paint_circle_not1.setColor(Color.WHITE);
		paint_circle_not1.setStrokeWidth(1);
		text_paint=new Paint();
		text_paint.setTextSize(20);
		text_paint.setTextAlign(Align.LEFT);
		
	}

	

	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
//		Inicializacion de las variables
		
		canvas.setDensity(240);
		canvas.drawColor(Color.WHITE);
		dibujar(canvas,this.validate);
		invalidate();
	}
	private void dibujar(Canvas canvas, boolean validate2) {
		// TODO Auto-generated method stub
		if(validate2)
		{
			int div=3;
			if(num_sumandos==1)
				div=2;
			else
				div=3;
			for (int i=0;i<num_var;i++)
			{
				this.variables=""+(char)('A'+i);
				if(num_sumandos<6)
				{
					canvas.drawText(this.variables, i*(((canvas.getWidth()/div-20)-10)/(this.num_var+1))+10, 35, text_paint);
				}
				else
				{
					text_paint.setTextSize(18);
					canvas.drawText(this.variables, i*(((canvas.getWidth()/div-20)-10)/(this.num_var+1)-5)+10, 35, text_paint);
				}
			}
			
//			if(num_sumandos>4)
//			{
				
				if(num_sumandos>4)
				{
					if(res.length()<60)
						{
						canvas.drawText(res.substring(0, res.length()/2)+"  ", canvas.getWidth()/2,45, text_paint);
						canvas.drawText(res.substring(res.length()/2+1,res.length() )+"  ", canvas.getWidth()/2,70, text_paint);
						}
					else
						if(res.length()>60)
						{
							
							canvas.drawText(res.substring(0, (int) Math.ceil(res.length()/3))+"  ", canvas.getWidth()/2,45, text_paint);
							canvas.drawText(res.substring((int)( Math.ceil(res.length()/3)),(int) Math.ceil(res.length()*2/3) )+"  ", canvas.getWidth()/2,70, text_paint);
							canvas.drawText(res.substring((int) (Math.ceil(res.length()*2/3)),res.length())+"  ", canvas.getWidth()/2,95, text_paint);
								
						}
				}
				
//				
			drawHandler(canvas);
		}
	}

	private void drawHandler(Canvas canvas) {
		// TODO Auto-generated method stub
		int canHeigth=canvas.getHeight();
		int canWidth=canvas.getWidth();
		float left1=6,left2,left3,left4, ychange,join1,join2;
		float[][] change_var_pos=new float[num_var][2];
		int text_size=25;
		
//		zones define la cantidad de franjas que va a tener el dibujo segun la cascada
//		de compuertas
//		AQUI HAY QUE  MEJORAR EL CODIGO
		int zones=3;
		join1=join2=0;
		
		ychange=(canvas.getHeight()-(num_sumandos-1)*5-Dibujo.compHEIGTH*num_sumandos)/2;
		if(this.num_sumandos==1)
			zones=2;
		else if (this.num_sumandos<=5)
		{
			zones=3;
			join1=(canHeigth-compHEIGTH)/2;
			join2=0;
		}
		else if(this.num_sumandos>5 && this.num_sumandos<11)
		{
			zones=4;
			join1=ychange+(5*compHEIGTH+20-compHEIGTH)/2;
			join2=(ychange+5*compHEIGTH+25)+((num_sumandos-5)*compHEIGTH+(num_sumandos-5)*5-compHEIGTH)/2;
			
		}
		
		left2=canWidth/zones-20;
		left3=2*left2+40;
		left4=3*left2+60;
		
		
//		Defino las posiciones de los primenos nodos de las variables
		for(int i=0;i<num_var;i++)
		{
			change_var_pos[i][0]=left1+(left2-left1)*i/num_var;
			change_var_pos[i][1]=text_size+10;
		}
		
		
//			**************************************************************************************************
//		(1) DISPOSICION DE LAS COMPUERTAS DENTRO DE CANVAS PARA TRES ZONAS: VARIABLES++ANDs+OR
//			**************************************************************************************************
			
			
			if(num_sumandos>1 && num_sumandos<6) //PARA LA DISPOSICON DE LAS COMPUERTAS CON CTDAD DE SUMANDOS MENOR DE SEIS
			{
				for (int i=0;i<my_circuit.length-1;i++)
				{
					canvas.drawBitmap(my_circuit[i],left2 ,ychange, null);
					ychange=ychange+Dibujo.compHEIGTH+5;
				}
				canvas.drawBitmap(my_circuit[num_sumandos], left3, join1, null);
			}
			else if(this.num_sumandos==1)
			{
				canvas.drawBitmap(my_circuit[0],left2 ,ychange, null);
			}
			else if(this.num_sumandos<11 && this.num_sumandos>5)//PARA DISPOCISION DE LAS COMPUERTAS CON CTDAD DE SUMANDOS MAYOR QUE CINCO Y MENOR DE 10
			{
				for (int i=0;i<my_circuit.length-1;i++)
				{
					if(i<num_sumandos)
					{
						canvas.drawBitmap(my_circuit[i],left2 ,ychange, null);
						ychange=ychange+Dibujo.compHEIGTH+5;
					}
					else
					{
						canvas.drawBitmap(my_circuit[i], left3, join1, null);
						canvas.drawBitmap(my_circuit[i+1], left3, join2, null);
						i++;
					}
					
				}
				canvas.drawBitmap(my_circuit[num_sumandos+2], left4, (canHeigth-compHEIGTH)/2, null);
			}
				
			ychange=(canHeigth-(num_sumandos-1)*5-compHEIGTH*num_sumandos)/2;
			
//			*************************************************************************************************
//			FIN DE (1)
//			*************************************************************************************************
			
//			*************************************************************************************************
//		(2)	PARA PINTAR LAS LINEAS QUE UNEN LA ZONA TRES CON LA ZONA 2 
//			*************************************************************************************************
			if (num_sumandos<=3)//AQUI SOLO DOS LINEAS POR LA DISTRIBUCION DE LAS COMPUERTAS(2.1)
			{
				for (int i=0;i<my_circuit.length-1;i++)
				{
					canvas.drawLine(left2+Dibujo.compWIDTH,ychange+compHEIGTH/2,left3,ychange+compHEIGTH/2, paint_line);
					canvas.drawLine(left3,ychange+compHEIGTH/2 , left3,((canHeigth-compHEIGTH)/2)+compHEIGTH*(i+1)/(num_sumandos+1) ,paint_line);
					ychange=ychange+Dibujo.compHEIGTH+5;
				}
			}
			else//AQUI EL ALGORITMO ANTERIOR(2.1) NO SIRVE PORQUE LAS LINEAS SE SUPERPONEN
			{
//					(2.2)
				float left3_corrector;
				float step_corrector;
				if(num_sumandos<6)	
				{
					left3_corrector=0;
					step_corrector=(left3-left2-Dibujo.compWIDTH)/((num_sumandos/2)+1);
				}
				else
				{
					left3_corrector=0;
					step_corrector=(left3-left2-Dibujo.compWIDTH)/((5/2)+1);
				}
					for (int i=0;i<my_circuit.length-1;i++)
					{
						if(num_sumandos<6)
						{
							//para pintar la tercera linea
							canvas.drawLine(left2+Dibujo.compWIDTH,ychange+compHEIGTH/2,left3-left3_corrector,ychange+compHEIGTH/2, paint_line);
							canvas.drawLine(left3-left3_corrector,ychange+compHEIGTH/2 , left3-left3_corrector,(join1)+compHEIGTH*(i+1)/(num_sumandos+1) ,paint_line);
							canvas.drawLine(left3-left3_corrector,(join1)+compHEIGTH*(i+1)/(num_sumandos+1), left3,(join1)+compHEIGTH*(i+1)/(num_sumandos+1) ,paint_line);
							ychange=ychange+Dibujo.compHEIGTH+5;
							if(i<=((num_sumandos/2)-1))
								left3_corrector=left3_corrector+step_corrector;
							else
								left3_corrector=left3_corrector-step_corrector;
						}
						else if(num_sumandos>5 && num_sumandos<11)
						{
							if(i<5)
							{
								//para pintar la tercera linea
								canvas.drawLine(left2+Dibujo.compWIDTH,ychange+compHEIGTH/2,left3-left3_corrector,ychange+compHEIGTH/2, paint_line);
								canvas.drawLine(left3-left3_corrector,ychange+compHEIGTH/2 , left3-left3_corrector,(join1)+compHEIGTH*(i+1)/(5+1) ,paint_line);
								canvas.drawLine(left3-left3_corrector,(join1)+compHEIGTH*(i+1)/(5+1), left3,(join1)+compHEIGTH*(i+1)/(5+1) ,paint_line);
								ychange=ychange+Dibujo.compHEIGTH+5;
								if(i<=1.5)
									left3_corrector=left3_corrector+step_corrector;
								else
									left3_corrector=left3_corrector-step_corrector;
							}
							else if(i<my_circuit.length-3 && i>4)
							{
								int sumandos_rest=num_sumandos-5;
								step_corrector=(left3-left2-Dibujo.compWIDTH)/((sumandos_rest/2)+1);
								if (i==5)
									left3_corrector=0;
								//para pintar la tercera linea
								canvas.drawLine(left2+Dibujo.compWIDTH,ychange+compHEIGTH/2,left3-left3_corrector,ychange+compHEIGTH/2, paint_line);
								canvas.drawLine(left3-left3_corrector,ychange+compHEIGTH/2 , left3-left3_corrector,(join2)+compHEIGTH*(i-5+1)/(sumandos_rest+1) ,paint_line);
								canvas.drawLine(left3-left3_corrector,(join2)+compHEIGTH*(i-5+1)/(sumandos_rest+1), left3,(join2)+compHEIGTH*(i-5+1)/(num_sumandos-5+1) ,paint_line);
								ychange=ychange+Dibujo.compHEIGTH+5;
								if(i<=6.5)
									left3_corrector=left3_corrector+step_corrector;
								else
									left3_corrector=left3_corrector-step_corrector;
							}
//							
//							PARA PINTAR LAS LINEAS QUE UNEN LOS ELEMENTOS DE LA ZONA 3 CON LA CUATRO
							canvas.drawLine(left3+Dibujo.compWIDTH,join1+compHEIGTH/2,left4,join1+compHEIGTH/2, paint_line);
							canvas.drawLine(left4,join1+compHEIGTH/2,left4,(canHeigth-compHEIGTH)/2+compHEIGTH/3, paint_line);
							canvas.drawLine(left3+Dibujo.compWIDTH,join2+compHEIGTH/2,left4,join2+compHEIGTH/2, paint_line);
							canvas.drawLine(left4,join2+compHEIGTH/2,left4,(canHeigth-compHEIGTH)/2+2*compHEIGTH/3, paint_line);
							
						}
					}
					
	//					FIN DE (2.2)
			}
			
//			**************************************************************************************************
//			FIN DE (2)
//			**************************************************************************************************
//		
//			**************************************************************************************************
//			PARA PINTAR LAS LINEAS QUE UNEN LAS VARIABLES CON LAS COMPUERTAS (3)
//			**************************************************************************************************
			float[] xvariablespos=new float[this.num_var];//INICIALIZO LAS POSICIONES DE LAS VARIABLES
			float[] yvariablespos=new float[this.num_var];
			
			for(int i=0;i<xvariablespos.length;i++)
			{
				xvariablespos[i]=i*((left2-10)/(this.num_var+1))+10;
				yvariablespos[i]=40;
			}
			
			ychange=(canHeigth-(num_sumandos-1)*5-Dibujo.compHEIGTH*num_sumandos)/2;
			
			for(int i=0;i<this.num_sumandos;i++)
			{
				float stepy=Dibujo.compHEIGTH/(Dibujo.matriz_sumandos[i][this.num_var]+1);
				for(int j=0;j<this.num_var;j++)
				{
					if(Dibujo.matriz_sumandos[i][j]!=3)
					{
						canvas.drawLine(xvariablespos[j],yvariablespos[j],xvariablespos[j], ychange+stepy, paint_line);
						ychange=ychange+stepy;
						yvariablespos[j]=ychange;
						canvas.drawLine(xvariablespos[j],yvariablespos[j],left2,yvariablespos[j] , paint_line);
						canvas.drawCircle(xvariablespos[j],yvariablespos[j], 4, paint_line);
						if(Dibujo.matriz_sumandos[i][j]==0)
						{
							canvas.drawCircle(left2,yvariablespos[j], 6, paint_circle_not);
							canvas.drawCircle(left2, yvariablespos[j], 3, paint_circle_not1);
						}
					}
				}
				ychange=ychange+stepy+5;
			}
			
//			**************************************************************************************************
//			FIN DE (3)
//			**************************************************************************************************
	}
	@Override
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	 width = measureDim(widthMeasureSpec, mDisplay.getWidth());
	 height = measureDim(heightMeasureSpec, mDisplay.getHeight());
	 setMeasuredDimension(width, height);
	 }
	 
	private int measureDim(int measureSpec, int size) {
	        int result = 0;
	        int specMode = MeasureSpec.getMode(measureSpec);
	        int specSize = MeasureSpec.getSize(measureSpec);

	        if (specMode == MeasureSpec.EXACTLY) {
	            result = specSize;
	        } else {
	            result = size;
	            if (specMode == MeasureSpec.AT_MOST) {
	               result = Math.min(result, specSize);
	            }
	        }
	        return result;
	  }
	void setParams(Bitmap[] compuertas,int sumandos, int num_var, int compheigth, int compwidth, 
			boolean formmap, String res, int[][] matriz_sumandos,int escala)
	{
		this.my_circuit=compuertas;
		this.num_sumandos=sumandos;
		this.num_var=num_var;
		Dibujo.compHEIGTH=compheigth;
		Dibujo.compWIDTH=compwidth;
		this.formofmap=formmap;
		this.res=res;
		Dibujo.matriz_sumandos=matriz_sumandos;
		validate=true;
		
		
	}
	
	

}
