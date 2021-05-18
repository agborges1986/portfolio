package com.kaymansoft.graph;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewStyle;
import com.kaymansoft.R;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeigthGraph extends LinearLayout {
	
	private static HorizontalLabelsParams horlabels;
	private static String[] verlabel;
	private static GraphViewStyle stylegraph;
	private static GraphViewStyle styleLabels;
	public GraphView graf;
	private int legendWidth;
	TextView title;

	public WeigthGraph(Context context) {
		super(context);
		
		horlabels=new HorizontalLabelsParams(context,true);
		stylegraph=new GraphViewStyle(Color.BLACK,Color.BLACK,Color.BLACK);
		styleLabels=new GraphViewStyle();
		title=new TextView(context);
		legendWidth=100;
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.setPadding(10, 10, 10, 10);
		this.setGravity(Gravity.CENTER);
		this.setBackgroundResource(R.drawable.c82_round_both);
		
	}
	
	public void addGraph(Context cxt, int witdh, int heigth){
		
		initTitle();
		graf=GraphUtils.getWeigthGraph(cxt);
		graf.setBackgroundResource(R.drawable.cf1_round_both);
		graf.setGravity(Gravity.CENTER);
		graf.setPadding(5, 5, 5, 5);
		graf.setGraphViewStyle(WeigthGraph.getStylegraph());
		
		setLabels();
		
		graf.setLegendWidth(this.legendWidth);
		graf.setShowLegend(true);
		this.addView(title);
		this.addView(graf, witdh, heigth);
	}
	
	private void setLabels() {
		// TODO Auto-generated method stub
		graf.setManualXAxis(horlabels.manualXaxis);
		graf.setManualXAxisBounds(horlabels.manualXmax,horlabels.manualXmin);
		graf.setHorizontalLabels(horlabels.horlabels);
	}

	public void resetSeries(Context cxt){
		
		AsyncTask<Context,Void,Void> refresh_graph=new AsyncTask<Context,Void, Void>(){
			@Override
			protected Void doInBackground(Context... params) {
				graf.removeSeries(1);
				graf.removeSeries(0);
				graf.addSeries(GraphUtils.getWeigthSeries(params[0]));
				graf.addSeries(GraphUtils.getDesireWeigthSeries(params[0]));
				graf.setManualYAxis(true);
				int ymax=(int) (GraphUtils.getMaxY(GraphUtils.getWeigthLastDays(params[0]))*1.1);
				int ymin=(int) (GraphUtils.getMinY(GraphUtils.getWeigthDesire(params[0]))*0.9);
				graf.setManualYAxisBounds(ymax,ymin);
				
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
		
		final HorizontalLabelsParams labels=new HorizontalLabelsParams(cxt, true);
		WeigthGraph.setHorlabels(labels);
		
		graf.invalidate();		
		graf.viewVerLabels.invalidate();
		
		
		
		
		
	}
	
	public void addGraph(Context cxt){
		
		graf=GraphUtils.getWeigthGraph(cxt);
		initTitle();
		graf.setBackgroundResource(R.drawable.cf1_round_both);
		graf.setGraphViewStyle(WeigthGraph.getStylegraph());
		
		setLabels();
		
		graf.setLegendWidth(this.legendWidth);
		graf.setShowLegend(true);
		
		this.addView(title);
		this.addView(graf);
	}
	
	public void addGraph(Context cxt, LayoutParams params){
		
		graf=GraphUtils.getWeigthGraph(cxt);
		initTitle();
		graf.setBackgroundResource(R.drawable.cf1_round_both);
		
		graf.setGraphViewStyle(WeigthGraph.getStylegraph());
		setLabels();
		graf.setLegendWidth(this.legendWidth);
		graf.setShowLegend(true);
		
		this.addView(title);
		this.addView(graf,params);
	}

	public static HorizontalLabelsParams getHorlabels() {
		return horlabels;
	}

	public static void setHorlabels(HorizontalLabelsParams horlabels) {
		WeigthGraph.horlabels = horlabels;
	}

	public static String[] getVerlabel() {
		return verlabel;
	}

	public static void setVerlabel(String[] verlabel) {
		WeigthGraph.verlabel = verlabel;
	}

	public static GraphViewStyle getStylegraph() {
		return stylegraph;
	}

	public static void setStylegraph(GraphViewStyle stylegraph) {
		WeigthGraph.stylegraph = stylegraph;
	}

	public static GraphViewStyle getStyleLabels() {
		return styleLabels;
	}

	public static void setStyleLabels(GraphViewStyle styleLabels) {
		WeigthGraph.styleLabels = styleLabels;
	}
	
	public void initTitle(){
		title.setText(R.string.graph_title_weigth);
		title.setTextSize(20);
		title.setPadding(10,5, 10, 5);
		title.setGravity(Gravity.CENTER);
		
	}
//	TODO hacer reset para ambos graficos
	

}
