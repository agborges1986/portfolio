package com.kaymansoft.graph;

import com.kaymansoft.R;

import android.content.Context;

public class HorizontalLabelsParams {
	
	
	private double coeff;
	public String[] horlabels;
	public double manualXmin;
	public double manualXmax;
	public boolean manualXaxis=true;
	public boolean isWeigthLabels=true;
	
	public HorizontalLabelsParams (Context cxt, boolean isWeightLabels){
		this.isWeigthLabels=isWeightLabels;
		generateHorLabels(cxt);
	}
	
	public void setIsWeigthLabels(boolean set){
		this.isWeigthLabels=set;
	}
	
	public boolean getIsWeigthLabel(){
		return this.isWeigthLabels;
	}
	
	public void setHorLabels(String[] labels){
		this.horlabels=labels;
	}
	
	public void generateHorLabels(Context cxt){
		
		if(isWeigthLabels){
			if(this.horlabels==null){
				double days=GraphUtils.getDaysToLast(cxt);
				if(days<=10){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_10days)
					};
					manualXmin=0;
					manualXmax=10;
					setCoeff(1/10d);
				}
				else if(days<=20){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_10days),
							cxt.getString(R.string.graph_weigth_labels_20days)
					};
					manualXmin=0;
					manualXmax=20;
					setCoeff(1/20d);
				}
				else if(days<=30){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_10days),
							cxt.getString(R.string.graph_weigth_labels_20days),
							cxt.getString(R.string.graph_weigth_labels_30days)
					};
					manualXmin=0;
					manualXmax=30;
					setCoeff(1/30d);
				}
				else if(days<=60){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_1month),
							cxt.getString(R.string.graph_weigth_labels_2month)
					};
					manualXmin=0;
					manualXmax=60;
					setCoeff(1/60d);
				}
				else if(days<=90){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_1month),
							cxt.getString(R.string.graph_weigth_labels_2month),
							cxt.getString(R.string.graph_weigth_labels_3month)
					};
					manualXmin=0;
					manualXmax=90;
					setCoeff(1/90d);
				}
				else if(days<=180){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_3month),
							cxt.getString(R.string.graph_weigth_labels_6month)
					};
					manualXmin=0;
					manualXmax=180;
					setCoeff(1/180d);
				}
				else if(days<=240){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_4month),
							cxt.getString(R.string.graph_weigth_labels_8month)
					};
					manualXmin=0;
					manualXmax=240;
					setCoeff(1/240d);
				}
				else if(days<=360){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_6month),
							cxt.getString(R.string.graph_weigth_labels_1year)
					};
					manualXmin=0;
					manualXmax=360;
					setCoeff(1/360d);
				}
				else if(days<=720){
					this.horlabels=new String[]{
							cxt.getString(R.string.graph_weigth_labels_begin),
							cxt.getString(R.string.graph_weigth_labels_1year),
							cxt.getString(R.string.graph_weigth_labels_2year)
					};
					manualXmin=0;
					manualXmax=720;
					setCoeff(1/720d);
				}
				
			}
		}
		else
		{
//			TODO for set horlabels for consumed calories graph
			if(this.horlabels==null){
				this.horlabels=new String[]{
						"",
						"6",
						"5",
						"4",
						"3",
						"2",
						cxt.getString(R.string.graph_calories_labels_last),
						cxt.getString(R.string.graph_calories_labels_today),
						""
						
				};
//				manualXmin=0;
//				manualXmax=7;
//				setCoeff(1/7);
//				}
//				else if(days==5){
//					this.horlabels=new String[]{
//							"5Days",
//							"4Days",
//							"3Days",
//							"2Days",
//							"yesterday",
//							"Today"
//							
//					};
//					manualXmin=0;
//					manualXmax=6;
//					setCoeff(1/6);
//				}
//				else if(days==4){
//					this.horlabels=new String[]{
//							"4Days",
//							"3Days",
//							"2Days",
//							"yesterday",
//							"Today"
//							
//					};
//					manualXmin=0;
//					manualXmax=5;
//					setCoeff(1/5);
//				}
//				else if(days==3){
//					this.horlabels=new String[]{
//							"3Days",
//							"2Days",
//							"yesterday",
//							"Today"
//							
//					};
//					manualXmin=0;
//					manualXmax=4;
//					setCoeff(1/4);
//				}
//				else if(days==2){
//					this.horlabels=new String[]{
//							"2Days",
//							"yesterday",
//							"Today"
//							
//					};
//					manualXmin=0;
//					manualXmax=3;
//					setCoeff(1/3);
//				}
//				else if(days==1){
//					this.horlabels=new String[]{
//							"yesterday",
//							"Today"
//							
//					};
//					manualXmin=0;
//					manualXmax=2;
//					setCoeff(1/2);
//				}
//				else if(days==0){
//					this.horlabels=new String[]{
//							"Today"
//							
//					};
//					manualXmin=0;
//					manualXmax=1;
//					setCoeff(1);
//				}
			}
		}
		
	}

	public double getCoeff() {
		return coeff;
	}

	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}

}
