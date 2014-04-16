package com.bango.imagereco;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	
	private Camera camera;
	private CameraPreview preview;
	private FrameLayout fl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fl = (FrameLayout) findViewById(R.id.camera_preview);
		
		detectCamera();
	}
	
	private void initCamera() {
		camera = Utils.getCameraInstance();
		
		preview = new CameraPreview(this, camera);
		fl.addView(preview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(camera != null)
			Camera.open();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if(camera != null) {
			camera.release();
			camera = null;
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	private void detectCamera() {
		if(!Utils.cameraExists(this)) {
			Utils.toastLong(this, "No camera detected, exiting..");
			finish();
		}
		
		initCamera();
	}

}
