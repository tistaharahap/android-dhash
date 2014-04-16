package com.bango.imagereco;

import java.io.IOException;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
	
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	
	private int camWidth, camHeight;
	
	private static final float PREVIEW_SIZE_FACTOR = 1.3f;
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		
		this.camera = camera;
		
		this.surfaceHolder = getHolder();
		this.surfaceHolder.addCallback(this);
		
		Camera.Parameters params = camera.getParameters();
		
		this.camera.setDisplayOrientation(90);
		
		final Size size = getOptimalSize();
		
		this.camWidth = size.width;
		this.camHeight = size.height;
		
		params.setPreviewSize(this.camWidth, this.camHeight);
		this.camera.setParameters(params);
	}
	
	private Size getOptimalSize() {
		Camera.Size result = null;
	    final Camera.Parameters parameters = camera.getParameters();
	    for (final Camera.Size size : parameters.getSupportedPreviewSizes()) {
	        if (size.width <= getWidth() * PREVIEW_SIZE_FACTOR && size.height <= getHeight() * PREVIEW_SIZE_FACTOR) {
	            if (result == null) {
	                result = size;
	            } else {
	                final int resultArea = result.width * result.height;
	                final int newArea = size.width * size.height;

	                if (newArea > resultArea) {
	                    result = size;
	                }
	            }
	        }
	    }
	    if (result == null) {
	        result = parameters.getSupportedPreviewSizes().get(0);
	    }
	    
	    return result;
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
            camera.startPreview();
        } 
        catch(IOException e) {
            e.printStackTrace();
        }
    }
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if(surfaceHolder.getSurface() == null)
			return;
		
		try {
            camera.stopPreview();
        } 
		catch (Exception e){}
		
		try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this);
            camera.startPreview();
        } catch (Exception e){}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera cam) {
		Parameters parameters = camera.getParameters();
	    int imageFormat = parameters.getPreviewFormat();
	    
	    if(imageFormat == ImageFormat.NV21) {
	    	System.gc();
	    	
	    	Bitmap b = Utils.getBitmapImageFromYUV(data, camWidth, camHeight);
	    	String dhash = Reco.getDhash(b);
	    	
	    	b = null;
	    	System.gc();
	    	
	    	int distance = HammingDistance.getDistance(dhash, "4e766b69f5f363ae");
	    	Utils.log(String.format("Distance: %d", distance));
	    }
	}
	
}
