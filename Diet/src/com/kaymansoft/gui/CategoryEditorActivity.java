package com.kaymansoft.gui;

import com.kaymansoft.R;
import com.kaymansoft.model.AppDBOpenHelper;
import com.kaymansoft.model.ModelUtils;
import com.kaymansoft.model.cursors.CategoryCursor;
import com.kaymansoft.model.elements.Category;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CategoryEditorActivity extends Activity {
	
	public static final String CATEGORY_ID = "com.kayansoft.diet.category_id";
	public static final String INTENT_TYPE = "com.kayansoft.diet.intent_type";
	
	public static final int NEW_CATEGORY = 129;
	public static final int EDIT_CATEGORY = 123;
	
	static final int CHANGE_IMAGE_CODE = 1037;
	
	private boolean isNew = false;
	
	EditText categoryName, categoryDescription;
	Button save, back;	
	
	ImageView categoryImage;
	
	Category category;
	
	private Bundle data;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.category_editor);
		
		cacheViews();
		initComponents();
		
		handleIntent(getIntent());
		
	}
	
	public void cacheViews() {
		
		categoryName		= (EditText) findViewById(R.id.editText1);
		categoryDescription = (EditText) findViewById(R.id.editText2);
		
		categoryImage = (ImageView) findViewById(R.id.imageView1);
		
		save 	= (Button) findViewById(R.id.button1);
		back 	= (Button) findViewById(R.id.button3);
		
	}
	
	public void initComponents() {
		category = new Category();
		
		
		categoryImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onChangeImageRequest();								
			}
		});
		
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onSaveRequest();				
			}			
		});
	
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
		
	}
	
	private void onChangeImageRequest() {
		Intent intent = new Intent(CategoryEditorActivity.this, ImageListActivity.class);
		startActivityForResult(intent, CHANGE_IMAGE_CODE);		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
		super.onNewIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		data = intent.getExtras(); //obtener los datos
		if(data!=null) { //si se recibió algo			
			switch (data.getInt(INTENT_TYPE)) { //verificar el intento
				case NEW_CATEGORY://nueva categoría
					isNew = true;
					//reload.setVisibility(View.GONE);
					break;
				case EDIT_CATEGORY: //editar una categoría
					isNew = false;
					//reload.setVisibility(View.VISIBLE);
					long menuId = data.getLong(CATEGORY_ID); //obtener el id de la categoría a editar
					AppDBOpenHelper db = new AppDBOpenHelper(this); 
					CategoryCursor c = db.getCategoryById(menuId); 
					category = c.getCategory(); //obtener la categoría
					c.close();
										
					db.close();
					showData(); //mostrar los datos
					break;		
			}
		}
	}
	
	private void showImage() {
		long imageId = category.getImageId();
		if(imageId==-1) {
			categoryImage.setImageResource(R.drawable.default_category_icon);
		} else {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			//Image img = db.getImageByID(imageId).getImage();
			ModelUtils.findAndSetCategoryImage(category,categoryImage,db);
			db.close();
		}
	}
	
	private void showData() {
		//si se está editando
		if(!isNew) {
			categoryName.setText(category.getName());
			categoryDescription.setText(category.getDescription());
		}
	}
	
	private void setData() {
		category.setName(categoryName.getText().toString());
		category.setDescription(categoryDescription.getText().toString());				    
	}
	
	private void onSaveRequest() {
		//TODO mejorar el procesamiento de errores
		setData();//enviar los cambios al menu interno
		AppDBOpenHelper db = new AppDBOpenHelper(this);
		if(isNew) { //si es un nuevo menu
			long menuId = ModelUtils.addCategory(category, db); //agregarlo
			category.setId(menuId);
		} else {//si solo se editó
			ModelUtils.updateCategory(category, db);
		}
		db.close();
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			AppDBOpenHelper db = new AppDBOpenHelper(this);
			switch(requestCode) {
				case CHANGE_IMAGE_CODE:
					long imageId = data.getLongExtra(ImageListActivity.SELECTED_IMAGE_ID, -1);
					category.setImageId(imageId);
					showImage();
					break;				
			}
			db.close();
		}
	}

}
