package com.ashomok.eNumbers.ocr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

class TessExtractor {
	private final Bitmap photo;
	private final TessFactory tessFactory;
	private TessBaseAPI baseApi;
	private String extractedText;
	
	public TessExtractor(AssetManager assetManager, Bitmap photo) {
		this.photo = photo;
		extractedText = "";
		tessFactory = new TessFactory(assetManager);
	}
	
	public String getText(){
		if(extractedText.equals(""))
			return extractText();
		return extractedText;
	}
	
	private String extractText(){
		try{
			baseApi = tessFactory.getTess();
		} catch (TesseractNotInitializedException e){
			Log.e(this.getClass().getCanonicalName(), e.getMessage());
			if(baseApi == null){
				Log.e(this.getClass().getCanonicalName(), "TessBaseAPI is null... TessFactory not returning tess object...");
			}
		} catch (DirectoryNotCreatedException ee){
			Log.e(this.getClass().getCanonicalName(), ee.getMessage());
		}

		System.out.println("in ocr function");
		baseApi.init(TessFactory.ASSETS_PATH, TessFactory.lang);

		System.out.println("training file loaded");
		baseApi.setImage(photo);
		try{
			extractedText = baseApi.getUTF8Text();
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Error in recognizing text...");
		}
		baseApi.end();
		return extractedText;
	}
	
}
