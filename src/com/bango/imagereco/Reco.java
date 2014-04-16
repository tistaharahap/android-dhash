package com.bango.imagereco;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class Reco {
	
	private static final int HASH_SIZE = 8;
	
	public static String getDhash(Bitmap img) {
		// Resize & Grayscale
		img = Utils.toGrayscale(img);
		img = Bitmap.createScaledBitmap(img, HASH_SIZE + 1, HASH_SIZE, true);
		
		List<Boolean> difference = new ArrayList<Boolean>();
		for(int row=0; row<HASH_SIZE; row++) {
			for(int col=0; col<HASH_SIZE; col++) {
				int pixel_left = img.getPixel(col, row);
				int pixel_right = img.getPixel(col + 1, row);
				
				difference.add(pixel_left > pixel_right);
			}
		}
		
		img = null;
		System.gc();
		
		int decimalValue = 0;
		String result = "";
		for(int i=0,max=difference.size(); i<max; i++) {
			boolean value = difference.get(i);
			if(value)
				decimalValue += 2 ^ (i % 8);
			
			if(i % 8 == 7) {
				String hex = String.format("%02x", decimalValue);
				result = String.format("%s%s", result, hex);
				decimalValue = 0;
			}
		}
		
		return result;
	}

}
