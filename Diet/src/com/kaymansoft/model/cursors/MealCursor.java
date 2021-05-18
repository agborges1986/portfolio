package com.kaymansoft.model.cursors;

import com.kaymansoft.model.elements.Meal;

import android.database.Cursor;

public interface MealCursor extends Cursor {
	Meal getMeal();
}
