package com.kaymansoft.graph;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.ValueDependentColor;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class GraphProffActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphViewSeriesStyle style=new GraphViewSeriesStyle(Color.BLUE,2);
        style.setValueDependentColor(new ValueDependentColor() {
			
			public int get(GraphViewData data) {
				// TODO Auto-generated method stub
				if (data.valueY>100)
					return Color.RED;
				else
					return Color.GREEN;
			}
		});
        GraphViewSeries exampleSeries = new GraphViewSeries("Nueva",style,new GraphViewData[]{
        		new GraphViewData(1, 1d),
        		new GraphViewData(2, 60d),
        		new GraphViewData(3, 70d),
        		new GraphViewData(4, 80d),
        		new GraphViewData(5, 10d),
        		new GraphViewData(6, 90d),
//        		new GraphViewData(5, 13d),
//        		new GraphViewData(6, 15d),
        });
//        GraphViewSeries exampleSeries1 = new GraphViewSeries(new GraphViewData[]{
//        		new GraphViewData(1, 4d),
//        		new GraphViewData(2, 4d),
//        		new GraphViewData(3, 4d),
//        		new GraphViewData(4, 4d),
//        		new GraphViewData(5, 4d),
//        		new GraphViewData(6, 4d),
//        });
//        
       BarGraphView graf=new BarGraphView(this, "Title");
        
//        graf.addSeries(exampleSeries1);
        graf.addSeries(exampleSeries);
//        graf.setManualYAxis(true);
//        graf.setManualYAxisBounds(100, 50);
//        graf.setManualXAxis(true);
//        graf.setManualXAxisBounds(40, 0);
//        graf.setVerticalLabels(new String[]{
//        		"first"
//        		,"second"
//        		,"Trith"
//        });
//        graf.setyNameLabel("OTRO");
//        graf.setXNameLabel("mios");
        
        graf.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLUE, Color.DKGRAY ));
        graf.setBackgroundColor(Color.WHITE);
        graf.setShowLegend(true);
        DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		graf.addGraph(this, metrics.widthPixels, metrics.heightPixels*1/2);
		
		graf.setHorizontalLabels(new String[]{
				"0",
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7"
		});
        setContentView(R.layout.main);
        LinearLayout l1=(LinearLayout)findViewById(R.id.l1);
        l1.addView(graf);
        
        
       
    }
}