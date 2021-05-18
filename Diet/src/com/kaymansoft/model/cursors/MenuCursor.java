package com.kaymansoft.model.cursors;

import com.kaymansoft.model.elements.Menu;

import android.database.Cursor;

public interface MenuCursor extends Cursor {
	Menu getMenu();
}
