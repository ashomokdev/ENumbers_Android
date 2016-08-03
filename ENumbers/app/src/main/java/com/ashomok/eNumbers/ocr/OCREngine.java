package com.ashomok.eNumbers.ocr;

import android.content.res.AssetManager;

import java.util.Set;

/**
 * Created by Iuliia on 24.11.2015.
 */
public interface OCREngine {

    /**
     * This method retrieve text from img using ocr tools (located in assets)
     * @param assetMgr
     * @param _path path to image
     * @return
     */
   String RetrieveText(AssetManager assetMgr, String _path);

    /**
     *
     * @param input Text obtained from OCR process
     * @return      Set of E-numbers
     */
    Set<String> parseResult(String input);
}
