package com.kaymansoft.graph;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewStyle;
import com.kaymansoft.R;

public class CaloriesConsumedGraph extends LinearLayout {
	
	private static HorizontalLabelsParams horlabels;
	private static String[] verlabel;
	private static GraphViewStyle stylegraph;
	private static GraphViewStyle styleLabels;
	private int legendWidth;
	TextView title;
	public GraphView graf;
	
	public CaloriesConsumedGraph(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		horlabels=new HorizontalLabelsParams(context,false);
		stylegraph=new GraphViewStyle();
		styleLabels=new GraphViewStyle();
		title=new TextView(context);
		legendWidth=150;
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(10, 5, 10, 10);
		this.setGravity(Gravity.CENTER);
		this.setBackgroundResource(R.drawable.c82_round_both);
	}

	public void addGraph(Context cxt, int witdh, int heigth){
		
		initTitle();
		graf=GraphUtils.getCaloriesConsumedGraph(cxt);
		graf.setBackgroundResource(R.drawable.cf1_round_both);
		
		graf.setGraphViewStyle(WeigthGraph.getStylegraph());
		graf.setManualXAxis(horlabels.manualXaxis);
//		graf.setManualXAxisBounds(horlabels.manualXmax,horlabels.manualXmin);
		graf.setHorizontalLabels(horlabels.horlabels);
		graf.setLegendWidth(this.legendWidth);
		graf.setShowLegend(true);
		
		this.addView(title);
		this.addView(graf, witdh, heigth);
	}
	
	public void addGraph(Context cxt){
		
		initTitle();
		graf=GraphUtils.getCaloriesConsumedGraph(cxt);
		graf.setBackgroundResource(R.drawable.cf1_round_both);
		
		graf.setGraphViewStyle(WeigthGraph.getStylegraph());
		graf.setHorizontalLabels(horlabels.horlabels);
		graf.setLegendWidth(this.legendWidth);
		graf.setShowLegend(true);
		
		this.addView(title);
		this.addView(graf);
	}
	
	public void addGraph(Context cxt, LayoutParams params){
		
		initTitle();
		graf=GraphUtils.getCaloriesConsumedGraph(cxt);
		graf.setBackgroundResource(R.drawable.cf1_round_both);
		
		graf.setGraphViewStyle(WeigthGraph.getStylegraph());
		graf.setHorizontalLabels(horlabels.horlabels);
		graf.setLegendWidth(this.legendWidth);
		graf.setShowLegend(true);
		
		this.addView(title);
		this.addView(graf,params);
	}
	
	public void resetSeries(Context cxt){
		AsyncTask<Context,Void,Void> refresh_graph=new AsyncTask<Context,Void, Void>(){
			@Override
			protected Void doInBackground(Context... params) {
				graf.removeSeries(0);
				graf.addSeries(GraphUtils.getCaloriesConsumedGraph(params[0]).graphSeries.get(0));
				
				return null;
			}
			@Override
			protected void onProgressUpdate(Void... values) {
				
			}
			@Override
			protected void onPostExecute(Void result) {
				
							}
		};
		
		refresh_graph.execute(cxt);
		
	}

	public static HorizontalLabelsParams getHorlabels() {
		return horlabels;
	}

	public static void setHorlabels(HorizontalLabelsParams horlabels) {
		CaloriesConsumedGraph.horlabels = horlabels;
	}

	public static String[] getVerlabel() {
		return verlabel;
	}

	public static void setVerlabel(String[] verlabel) {
		CaloriesConsumedGraph.verlabel = verlabel;
	}

	public static GraphViewStyle getStylegraph() {
		return stylegraph;
	}

	public static void setStylegraph(GraphViewStyle stylegraph) {
		CaloriesConsumedGraph.stylegraph = stylegraph;
	}

	public static GraphViewStyle getStyleLabels() {
		return styleLabels;
	}

	public static void setStyleLabels(GraphViewStyle styleLabels) {
		CaloriesConsumedGraph.styleLabels = styleLabels;
	}

	public int getLegendWidth() {
		return legendWidth;
	}

	public void setLegendWidth(int legendWidth) {
		this.legendWidth = legendWidth;
	}
	public void initTitle(){
		
		title.setText(R.string.graph_title_calories);
		title.setTextSize(18);
		title.setPadding(10,5, 10, 0);
		title.setGravity(Gravity.CENTER);
		
	}

}
