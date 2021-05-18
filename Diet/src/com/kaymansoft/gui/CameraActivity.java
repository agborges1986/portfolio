package com.kaymansoft.gui;

import java.io.IOException;
import java.util.List;

import com.kaymansoft.R;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CameraActivity extends Activity {
	
	Camera camera;
	Button newButton,takeButton;
	SurfaceView sv;
	
	boolean takingPicture = false;
	byte pic[];
	
	public static final String PICTURE = "com.kaymansoft.diet.picture";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.camera);
		
		cacheViews();
		initComponents();
		
	}
	
	private void cacheViews() {
		newButton 	= (Button) findViewById(R.id.button1);
		takeButton 	= (Button) findViewById(R.id.button3);
		
		sv = (SurfaceView) findViewById(R.id.surfaceView1);
	}

	private void initComponents() {
		doCameraStuff();
		
		newButton.setEnabled(false);
		newButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				newButton.setEnabled(false);
				takeButton.setText(R.string.take_picture_text);
				takingPicture = true;
			}
		});
		
		takeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				camera.stopPreview();
				newButton.setEnabled(true);
				if(takingPicture) {
					takeButton.setText(R.string.accept_text);
					takeButton.setEnabled(false);
					newButton.setEnabled(false);
					camera.takePicture(null, null, new PictureCallback() {
						public void onPictureTaken(byte[] data, Camera camera) {
							pic = data;
							takeButton.setEnabled(true);
							newButton.setEnabled(true);
						}
					});
					takingPicture = false;
				} else { //salvar
					Intent intent = getIntent();
					intent.putExtra(PICTURE, pic);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	private void doCameraStuff() {
		camera = Camera.open();
		Parameters parameters = camera.getParameters();
		List<Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
		Size size = supportedPictureSizes.get(supportedPictureSizes.size()-1);
		parameters.setPictureSize(size.width,size.height);
		camera.setParameters(parameters);
		
		try {
			camera.setPreviewDisplay(sv.getHolder());
		} catch (IOException e) {
			Log.e(getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
		
		camera.startPreview();
		takingPicture = true;
	}
	
	@Override
	protected void onResume() {
		doCameraStuff();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		camera.release();
		super.onPause();
	}

}
