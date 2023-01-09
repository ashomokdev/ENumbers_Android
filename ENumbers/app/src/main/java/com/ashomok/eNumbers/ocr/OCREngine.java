package com.ashomok.eNumbers.ocr;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Iuliia on 24.11.2015.
 */
public abstract class OCREngine {
    private static final String REGEX_ENUMB = "E[ ]{0,2}[0-9]{3,4}[a-j]?";
    /**
     * @param input Text obtained from OCR process
     * @return Set of E-numbers
     */
    public Set<String> parseResult(String input) {
        final String E = "E";
        final int lengthOfWord = 8;

        Set<String> words = new HashSet<>();
        if (input.contains(E)) {
            //get possible E-numbers
            int fromIndex = 0;
            while (fromIndex < input.length()) {

                int wordStart = input.indexOf(E, fromIndex);
                if (wordStart >= 0) { //if E - numbers exist

                    int wordEnd = input.indexOf(E, fromIndex) + lengthOfWord;
                    if (wordEnd > input.length() - 1) {
                        wordEnd = input.length();
                    }
                    String word = input.substring(wordStart, wordEnd);

                    String result = parseWord(word);
                    if (result != null) {
                        words.add(result);
                    }
                    fromIndex = wordStart + 1;
                } else {
                    fromIndex = input.length();
                }
            }
        }
        return words;
    }

    @Nullable
    private String parseWord(String word) {
        Pattern pattern = Pattern.compile(REGEX_ENUMB);
        Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            return matcher.group(0).replaceAll("\\s", "");
        } else {
            return null;
        }
    }


    /**
     * This method retrieve text from img using ocr tools (located in assets)
     * @param path    path to image
     * @return
     */
    public abstract String retrieveText(Context context, String path) throws IOException;
}
