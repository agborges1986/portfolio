package com.kaymansoft.model.cursors;

import android.database.Cursor;

import com.kaymansoft.model.elements.Weight;

public interface WeightCursor extends Cursor{
	
	Weight getWeight();

}
