package com.kaymannsoft.card;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class GraphViewResult extends LinearLayout{

	int total_answers,good_answers;
	BarGraphView result_view;
	
	
	public GraphViewResult(Context context, int total_answers, int good_answers) {
		super(context);
		
		this.good_answers=good_answers;
		this.total_answers=total_answers;
		
		setGraph(context);
		this.addView(result_view);
	}
	
	
	
	public GraphViewResult(Context context, AttributeSet attrs,
			int total_answers, int good_answers, BarGraphView result_view) {
		super(context, attrs);
		this.total_answers = total_answers;
		this.good_answers = good_answers;
		this.result_view = result_view;
	}


	private void setGraph(Context cxt) {
		
		result_view=new BarGraphView(cxt,"");
		
		GraphViewSeriesStyle style=new GraphViewSeriesStyle(getResources().getColor(R.color.bad_series_color), 1);
		GraphViewSeriesStyle style1=new GraphViewSeriesStyle(getResources().getColor(R.color.good_series_color), 1);
		GraphViewSeriesStyle style2=new GraphViewSeriesStyle(getResources().getColor(R.color.total_series_color), 1);
		
		GraphViewSeries bad_series=new GraphViewSeries(
					cxt.getString(R.string.result_bad_answers_graf_legend), style,
					new GraphViewData[]{
						new GraphViewData(0, 0),
						new GraphViewData(0.5,total_answers-good_answers),
						new GraphViewData(1, 0)
					});
		
		GraphViewSeries good_series=new GraphViewSeries(
				cxt.getString(R.string.result_good_answers_graf_legend), style1,
				new GraphViewData[]{
					new GraphViewData(0, good_answers),
					new GraphViewData(0.5,0),
					new GraphViewData(1, 0)
				});
		
		GraphViewSeries total_series=new GraphViewSeries(
				cxt.getString(R.string.result_total_answers_graf_legend), style2,
				new GraphViewData[]{
					new GraphViewData(0, 0),
					new GraphViewData(0.5, 0),
					new GraphViewData(1, total_answers)
				});
		result_view.setHorizontalLabels(new String[]{
					"",
					"",
					"",
					"",
					""
		});
//		TODO Arreglar las etiquetas verticales
		
		String[] verlabels=getVerLabels(total_answers);
		
		if(verlabels!=null){
			result_view.setVerticalLabels(verlabels);
		}
		
		GraphViewStyle style3=new GraphViewStyle(Color.BLACK, Color.BLACK, Color.BLACK);
		
		result_view.addSeries(good_series);
		result_view.addSeries(bad_series);
		result_view.addSeries(total_series);
		
//		result_view.setLegendWidth(100);
//		result_view.setLegendAlign(LegendAlign.BOTTOM);
//		result_view.setShowLegend(true);
		result_view.setGraphViewStyle(style3);
		
		
	}


	/**
	 * Se generar'an las etiquetas verticales teniendo presente que
	 * la cantidad total de preguntas es siempre múltiplo de 5 y menor igual que 50
	 * @param total_answers2 Cantidad de preguntas
	 * @return String[] verlabels
	 */
	@SuppressWarnings("unused")
	private String[] getVerLabels(int total_answers2) {
		
		String[] verlabels;
		
		int divider=total_answers2/5;
		
		switch(divider){
			case 1:
			{
				verlabels=new String[total_answers+1];
				for(int i=0;i<total_answers+1;i++){
					verlabels[i]=""+i;
				break;
				}
			}
			case 2:
			{
				verlabels=new String[]{
						"0",
						"5",
						"10"
				};
				break;
			}
			case 3:
			{
				verlabels=new String[]{
						"0",
						"5",
						"10",
						"15"
				};
				break;
			}
			case 4:{
				verlabels=new String[]{
						"0",
						"5",
						"10",
						"15",
						"20"
				};
				break;
			}
			case 5:{
				verlabels=new String[]{
						"0",
						"5",
						"10",
						"15",
						"20",
						"25"
				};
				break;
			}
			case 6:{
				verlabels=new String[]{
						"0",
						"15",
						"30"
				};
				break;
			}
			case 7:{
				verlabels=new String[]{
						"0",
						"17",
						"35"
				};
				break;
			}
			case 8:{
				verlabels=new String[]{
						"0",
						"10",
						"20",
						"30",
						"40"
				};
				break;
			}
			case 9:{
				verlabels=new String[]{
						"0",
						"11",
						"23",
						"34",
						"45"
				};
				break;
			}
			case 10:{
				verlabels=new String[]{
						"0",
						"25",
						"50"
				};
				break;
			}
			default:
				verlabels=null;
		}
		
		
		return verlabels;
	}
	
		
		
	


}
