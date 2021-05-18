package com.kaymansoft.model.cursors;

import com.kaymansoft.model.elements.Food;

import android.database.Cursor;

public interface FoodCursor extends Cursor {
	
	Food getFood();

}
