package com.kaymannsoft.card;

import android.content.Context;
import android.graphics.Typeface;

public class UtilsTextFace {

//	Nombre de los asset que contienen las letras 
	final static String font_text="FRADMCN.TTF";
	final static String font_time="FRADM.TTF";
	final static String font_check="STENCIL.TTF";
	
	static public Typeface getTextFont(Context cxt){
		return Typeface.createFromAsset(cxt.getAssets(), font_text);
	}
	
	static public Typeface getTimeFont(Context cxt){
		return Typeface.createFromAsset(cxt.getAssets(), font_time);
	}
	
	static public Typeface getTextCheck(Context cxt){
		return Typeface.createFromAsset(cxt.getAssets(), font_check);
	}

	
}
