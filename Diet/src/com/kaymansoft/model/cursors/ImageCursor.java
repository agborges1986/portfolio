package com.kaymansoft.model.cursors;

import android.database.Cursor;

import com.kaymansoft.model.elements.Image;

public interface ImageCursor extends Cursor {
	
	Image getImage();

}
