package com.kaymansoft.gui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.cursors.ImageCursor;
import com.kaymansoft.model.elements.Image;

public class ImageListActivity extends Activity {

	GridView					images;
	CursorAdapter				adapter;
	AppDBOpenHelper				db;
	ImageButton					cameraButton;

	static final String			SELECTED_IMAGE_ID	= "com.kaymansoft.diet.image_id";

	private static final int	NEW_IMAGE_CODE		= 1029;

	private class InternalAdapter extends CursorAdapter {

		public InternalAdapter(Context context, Cursor c) {
			super(context, c, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
			Image image = ((ImageCursor) cursor).getImage();
			if (image.getImage() != null) {
				InputStream is = new ByteArrayInputStream(image.getImage());
				BitmapDrawable bmp = new BitmapDrawable(is);
				iv.setImageDrawable(bmp);
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			return LayoutInflater.from(context).inflate(R.layout.image_list_item, null);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.images_list);

		cacheViews();
		initComponents();

		Toast.makeText(this, getResources().getString(R.string.long_click_to_select_text), Toast.LENGTH_SHORT).show();

	}

	private void cacheViews() {
		images = (GridView) findViewById(R.id.gridView1);
		cameraButton = (ImageButton) findViewById(R.id.imageButton1);
	}

	private void initComponents() {

		images.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				getIntent().putExtra(SELECTED_IMAGE_ID, id);
				setResult(Activity.RESULT_OK, getIntent());
				finish();
				return true;
			}

		});

		cameraButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent camersIntent = new Intent(ImageListActivity.this, CameraActivity.class);
				startActivityForResult(camersIntent, NEW_IMAGE_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			byte[] byteArray = data.getExtras().getByteArray(CameraActivity.PICTURE);

			db = new AppDBOpenHelper(this);
			db.addImage(byteArray);
			db.close();

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		db = new AppDBOpenHelper(this);

		ImageCursor c = db.getImages();
		//startManagingCursor(c);
		adapter = new InternalAdapter(this, c);
		images.setAdapter(adapter);
	}

	@Override
	public void onPause() {
		super.onPause();
		db.close();
		db = null;
	}

}
