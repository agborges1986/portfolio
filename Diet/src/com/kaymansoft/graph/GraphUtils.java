package com.kaymansoft.graph;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.kaymansoft.R;
import com.kaymansoft.calories.CaloriesConsumptionUtils;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.cursors.WeightCursor;
import com.kaymansoft.model.elements.Weight;
import com.kaymansoft.settings.UserSettings;
import com.kaymansoft.settings.UserSettingsDBOpenHelper;

/**
 * @author Ariel
 *
 */
/**
 * @author Ariel
 *
 */
public class GraphUtils {
	
/*//
 * This function return in GraphViewSeries with the
 * calories consumed for the user in the last 7 days
*/	
	public static GraphViewData[] getCaloriesConsumedLastDays(Context cxt){
		
		final int days_graf=7;
		
		double calories[]=CaloriesConsumptionUtils.getConsumedCaloriesLastDays(cxt,  days_graf);
		GraphViewData[] calorieslastdays=new GraphViewData[days_graf];
		
		int diff=days_graf-calories.length;
		
		for(int i=0;i<days_graf;i++){
			if(i>=diff){
			calorieslastdays[i]=new GraphViewData(i, calories[calories.length-1-i+diff]);
			}
			else
				calorieslastdays[i]=new GraphViewData(i,0);
		}
		
		return calorieslastdays;
		
		
	}
	
	public static GraphViewData[] getWeigthLastDays(Context cxt){
		
		AppDBOpenHelper db = new AppDBOpenHelper(cxt);		
		WeightCursor cur = db.getLastWeightsFirst();
		
		List<GraphViewData> lastWeigthSeries =new ArrayList<GraphViewData>();
		
		Calendar timestamp=Calendar.getInstance();
		
		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(cxt);
		final UserSettings us = usDB.getSettings();
		long dietbegin=0;
		try {
			dietbegin = UserSettingsDBOpenHelper.DATE_FORMATTER.parse(us.getDietStartDay()).getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		usDB.close();
		
		if(cur.moveToLast()){
			
			int length= (int)cur.getCount();
			
			
//			lastWeigthSeries=new GraphViewData[length];
			
			for(int i=0; i<length;i++){
				Weight lastWeight = cur.getWeight();
				try {
					timestamp.setTime(UserSettingsDBOpenHelper.DATE_FORMATTER.parse(lastWeight.getDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Log.d(GraphUtils.class.getName(), "Error parseando el tiempo desde la BD");
					e.printStackTrace();
				}
				if(dietbegin <=timestamp.getTimeInMillis()){
					long days=(timestamp.getTimeInMillis()-dietbegin)/86400000;
					lastWeigthSeries.add(new GraphViewData((double)days,lastWeight.getWeightValue()));
	//				lastWeigthSeries[i]=new GraphViewData((double)days,lastWeight.getWeightValue());
				}
				cur.moveToPrevious();
				}
			cur.close();
			db.close();
			
			GraphViewData[] data=new GraphViewData[lastWeigthSeries.size()];
			for(int i=0;i<lastWeigthSeries.size();i++){
				data[i]=lastWeigthSeries.get(i);
			}
			return data;
		}
		else{
			lastWeigthSeries.add(new GraphViewData(0,0));
//			lastWeigthSeries= new GraphViewData[0];
			cur.close();
			db.close();
			GraphViewData[] data=new GraphViewData[lastWeigthSeries.size()];
			for(int i=0;i<lastWeigthSeries.size();i++){
				data[i]=lastWeigthSeries.get(i);
			}
			return data;
		}
		
	}
	
	
	
//	Cantidad de dias que separan a el ultimo pesaje del que se realizo la primera vez
	
	public static double getDaysToLast(Context cxt){

		AppDBOpenHelper db = new AppDBOpenHelper(cxt);		
		WeightCursor cur = db.getLastWeightsFirst();
		
		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(cxt);
		final UserSettings us = usDB.getSettings();
		final String start=us.getDietStartDay();
		
		Calendar now=Calendar.getInstance();
		Calendar last=Calendar.getInstance();
		
		if(cur.moveToFirst()){
			Weight lastweight=cur.getWeight();
					
			try {
				now.setTime(UserSettingsDBOpenHelper.DATE_FORMATTER.parse(lastweight.getDate()));
				cur.moveToLast();
								
				lastweight=cur.getWeight();
				last.setTime(UserSettingsDBOpenHelper.DATE_FORMATTER.parse(start));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.close();
			cur.close();
		}
		return (now.getTimeInMillis()-last.getTimeInMillis())/86400000;
	}
	
	public static GraphViewData[] getWeigthDesire(Context cxt){
		
		UserSettings us = new UserSettingsDBOpenHelper(cxt).getSettings();
		
		final GraphViewData[] temp=getWeigthLastDays(cxt);
		GraphViewData[] desireweigthdata=new GraphViewData[temp.length];
		for (int i=0;i<desireweigthdata.length;i++){
			desireweigthdata[i]= new GraphViewData(temp[i].valueX,us.getDesiredWeightInKg());
			
		}
		return desireweigthdata;
		
	}
	
	
	
	
//	
	public static GraphView getWeigthGraph(Context cxt){
		
		GraphView graf=new LineGraphView(cxt, "");
//		TODO arreglar el estilo para que este en la clase que extends Linear Layout
						
		GraphViewSeries weigthseries=getWeigthSeries(cxt);
		GraphViewSeries weigthdesireseries=getDesireWeigthSeries(cxt);
		
		graf.addSeries(weigthseries);
		graf.addSeries(weigthdesireseries);
		
		graf.setManualYAxis(true);
		int ymax=(int) (GraphUtils.getMaxY(GraphUtils.getWeigthLastDays(cxt))*1.1);
		int ymin=(int) (GraphUtils.getMinY(GraphUtils.getWeigthDesire(cxt))*0.9);
		graf.setManualYAxisBounds(ymax,ymin);
				
		return graf;
	}
	
	public static GraphViewSeries getWeigthSeries(Context cxt){
		
//		Styles of series
		GraphViewSeriesStyle style1=new GraphViewSeriesStyle(Color.RED,1);
		GraphViewData[] weigthdata=GraphUtils.getWeigthLastDays(cxt);
		GraphViewSeries weigthseries= new GraphViewSeries(cxt.getString(R.string.graph_weigth), style1,weigthdata);
		
		return weigthseries;
		
	}
	
	public static GraphViewSeries getDesireWeigthSeries(Context cxt){
		
		GraphViewSeriesStyle style2=new GraphViewSeriesStyle(Color.GREEN,1);
		GraphViewData[] desireweigthdata=GraphUtils.getWeigthDesire(cxt);
		GraphViewSeries weigthdesireseries= new GraphViewSeries(cxt.getString(R.string.graph_weigthdesire), style2,desireweigthdata);
		
		return weigthdesireseries;
	}
	
	public static GraphView getCaloriesConsumedGraph(Context cxt){
		
		BarGraphView caloriesgraf=new BarGraphView(cxt, "");
		
		UserSettingsDBOpenHelper usDB = new UserSettingsDBOpenHelper(cxt);
		UserSettings us = usDB.getSettings();
		final double maxCalories = CaloriesConsumptionUtils.getRecommendedDailyCaloriesConsumption(us);
		usDB.close();
				
//		caloriesgraf.setGraphViewStyle(GraphUtils.stylegraph);
		
		GraphViewSeriesStyle style1=new GraphViewSeriesStyle(Color.GREEN,1);
		style1.setValueDependentColor(new ValueDependentColor() {
			
			public int get(GraphViewData arg0) {
				// TODO Auto-generated method stub
				if(arg0.valueY>maxCalories)
					return Color.argb(220, 255, 41,38);
				else
					return Color.argb(220, 75, 255,76);
			}
		});
		
		GraphViewSeries calories_consumed_series=new GraphViewSeries(cxt.getString(R.string.graph_calories_legend), style1, GraphUtils.getCaloriesConsumedLastDays(cxt));
		caloriesgraf.addSeries(calories_consumed_series);
		
		caloriesgraf.setManualYAxis(true);
		int ymax=(int) (GraphUtils.getMaxY(GraphUtils.getCaloriesConsumedLastDays(cxt))*1.1);
		caloriesgraf.setManualYAxisBounds(ymax,0);
		
		return caloriesgraf;
	}
	
	public static double getMaxY(GraphViewData[] data){
		double max=Double.MIN_VALUE;
		
		for(int i=0;i<data.length;i++){
			if(data[i].valueY>max)
				max=data[i].valueY;
		}
		return max;
	}
	public static double getMinY(GraphViewData[] data){
		double min=Double.MAX_VALUE;
		
		for(int i=0;i<data.length;i++){
			if(data[i].valueY<min)
				min=data[i].valueY;
		}
		return min;
	}
	

	

}