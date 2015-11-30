package com.ashomok.eNumbers.ocr;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;


class TessExtractor {
	
	public static final String TAG = "TessExtractor";

	private Bitmap photo;
	private TessFactory tessFactory;
	private TessBaseAPI baseApi;
	private String extractedText;
	
	public TessExtractor(AssetManager assetManager, Bitmap photo) {
		this.photo = photo;
		extractedText = "";

		long startTime = System.currentTimeMillis();
		tessFactory = new TessFactory(assetManager);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		Log.e(TAG, "tessFactory = new TessFactory(assetManager); elapsed" + elapsedTime);
	}
	
	public String getText(){
		if(extractedText.equals(""))
			return extractText();
		return extractedText;
	}
	
	private String extractText(){
		try{
			long startTime = System.currentTimeMillis();
			baseApi = tessFactory.getTess();
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;

			Log.e(TAG, "baseApi = tessFactory.getTess(); elapsed" + elapsedTime);

		} catch (TesseractNotInitializedException e){
			Log.e(TAG, e.getMessage());
			if(baseApi == null){
				Log.e(TAG, "TessBaseAPI is null... TessFactory not returning tess object...");
			}
		} catch (DirectoryNotCreatedException ee){
			Log.e(TAG, ee.getMessage());
		}

		long startTime = System.currentTimeMillis();
		System.out.println("in ocr function");
		baseApi.init(TessFactory.ASSETS_PATH, TessFactory.lang);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		Log.e(TAG, "baseApi.init(TessFactory.ASSETS_PATH, TessFactory.lang); elapsed" + elapsedTime);
        //For example if we want to only detect numbers
 //       baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "E1234567890");
//        baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
//                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");




		System.out.println("training file loaded");
		//photo = photo.copy(Bitmap.Config.ARGB_8888, true);
		baseApi.setImage(photo);
		try{
			extractedText = baseApi.getUTF8Text();
		} catch(Exception e){
			Log.i(TAG, "Error in recognizing text...");
		}
		baseApi.end();
		return extractedText;
	}
	
}
