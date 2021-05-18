package com.kaymannsoft.card;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.kaymannsoft.card.R.drawable;



import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;


public class ExecirsesReaderUtil {
	
	public static boolean LOAD_EXERCISE=false;
	public static boolean LOAD_IMAGE=false;
	public static boolean SEARCH_CHOOSES=false;
	public static boolean LOAD_HEAD=false;
	public static boolean LOAD_COMMENT=false;
	public static boolean LOAD_RIGHT_ANSWER=false;
	public static boolean LOAD_N_CHOOSE=false;
	public static boolean LOAD_IMAGE_COMMENT=false;
	
	final static String headTagName="head";
	final static String excersiceTagName="Excersice";
	final static String imageTagName="image";
	final static String chooseTagName="option";
	final static String commentTagName="comment";
	final static String rigthTagName="solution";
	final static String pickTagName="item";
	final static String imagecommentTagName="image_comment";
	
	
	
	
	public static Exercise[] readContentExcercisesXML(Activity activity, int xmlDocumentId) 
			throws XmlPullParserException, IOException{
		
		List<String> chooses = new ArrayList<String>();
		List<Exercise> ejercicios=new ArrayList<Exercise>();;
		int exercise_number = 0;
		String excercise = null;
		String head=null;
		int imageID =-1;
		int imagecommentID =-1;
		int right = 0;
		String comment=new String();
		
		Resources res = activity.getResources();
		XmlResourceParser xpp = res.getXml(xmlDocumentId);
		xpp.next();
		int eventType = xpp.getEventType();
		
		   while (eventType != XmlPullParser.END_DOCUMENT){
			   switch(eventType){
			   case XmlPullParser.START_TAG:
			   {
				   if(xpp.getName().equals(excersiceTagName))
					   LOAD_EXERCISE=true;
				   else if(xpp.getName().equals(headTagName))
					   LOAD_HEAD=true;
				   else if(xpp.getName().equals(imageTagName))
					   LOAD_IMAGE=true;
				   else if(xpp.getName().equals(chooseTagName))
					   SEARCH_CHOOSES=true;
				   else if(xpp.getName().equals(commentTagName))
					   LOAD_COMMENT=true;
				   else if(xpp.getName().equals(rigthTagName))
					   LOAD_RIGHT_ANSWER=true;
				   else if(xpp.getName().equals(pickTagName))
				   {
					   LOAD_N_CHOOSE=true;
				   }
				   else if(xpp.getName().equals(imagecommentTagName))
					  LOAD_IMAGE_COMMENT=true;
			   }
				   break;
			   case XmlPullParser.END_TAG:
			   {
				   if(xpp.getName().equals(excersiceTagName))
				   {
					   LOAD_EXERCISE=false;
					   String[] opciones=new String[chooses.size()];
					   for(int i=0;i<chooses.size();i++)
						   opciones[i]=chooses.get(i);
					   ejercicios.add(new Exercise(exercise_number, head, excercise, imageID, imagecommentID, opciones, right, comment));
					   chooses.clear();
					   imagecommentID=-1;
					   imageID=-1;
				   }
				   else if(xpp.getName().equals(headTagName))
					   LOAD_HEAD=false;
				   else if(xpp.getName().equals(imageTagName))
					   LOAD_IMAGE=false;
				   else if(xpp.getName().equals(chooseTagName))
					   SEARCH_CHOOSES=false;
				   else if(xpp.getName().equals(commentTagName))
					   LOAD_COMMENT=false;
				   else if(xpp.getName().equals(rigthTagName))
					   LOAD_RIGHT_ANSWER=false;
				   else if(xpp.getName().equals(pickTagName))
					   LOAD_N_CHOOSE=false;
				   else if(xpp.getName().equals(imagecommentTagName))
					   LOAD_IMAGE_COMMENT=false;
			   }
				   break;
			   case XmlPullParser.TEXT:
//				   if anidados desde los tags mas profundos hasta los tags
//				   mas en superficie para garantizar que siempre se este leyendo
//				   el texto que corresponde a lo mas profundo del xml
				   
				   if(LOAD_N_CHOOSE){
					   chooses.add(xpp.getText());
				   }
				   else if(LOAD_HEAD){
					   excercise=xpp.getText();
				   }
				   else if(LOAD_IMAGE){
//					  Para parsear las imagenes con el nombre en string
				        try {
						    Class<drawable> res1 = R.drawable.class;
						    Field field = res1.getField(xpp.getText());
						    imageID = field.getInt(null);
						}
						catch (Exception e) {
						    Log.e("MyTag", "Failure to get drawable id.", e);
						    imageID=-1;
						}
				        
				   }
				   else if(LOAD_IMAGE_COMMENT){
//						  Para parsear las imagenes con el nombre en string
					        try {
							    Class<drawable> res1 = R.drawable.class;
							    Field field = res1.getField(xpp.getText());
							    imagecommentID = field.getInt(null);
							}
							catch (Exception e) {
							    Log.e("MyTag", "Failure to get drawable id.", e);
							    imagecommentID=-1;
							}
					        
					   }
				   else if(LOAD_COMMENT){
					   comment=xpp.getText();
				   }
				   else if(LOAD_RIGHT_ANSWER){
					   right=Integer.parseInt(xpp.getText());
				   }

				   else if(LOAD_EXERCISE){
					   exercise_number++;
					   head=xpp.getText();
					   LOAD_EXERCISE=false;
				   }

				   else if(SEARCH_CHOOSES){
//					   TODO Encabezado de Opciones
				   }
				   break;
			   
			   }
			   eventType = xpp.next();  
		   }
		   Exercise[] ejercicios_all=new Exercise[ejercicios.size()];
		   for(int i=0;i<ejercicios.size();i++)
			   ejercicios_all[i]=ejercicios.get(i);
		return ejercicios_all;
		
	}

}
