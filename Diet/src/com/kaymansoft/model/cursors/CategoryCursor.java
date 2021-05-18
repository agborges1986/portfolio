package com.kaymansoft.model.cursors;

import com.kaymansoft.model.elements.Category;

import android.database.Cursor;

public interface CategoryCursor extends Cursor {
	Category getCategory();
}
