package com.kaymansoft.model.cursors;

import com.kaymansoft.model.elements.FoodExtraInfo;

import android.database.Cursor;

public interface FoodExtraInfoCursor extends Cursor {
	
	FoodExtraInfo getExtraInfo();

}
