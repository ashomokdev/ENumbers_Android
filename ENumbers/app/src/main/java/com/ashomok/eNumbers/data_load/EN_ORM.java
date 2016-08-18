package com.ashomok.eNumbers.data_load;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by iuliia on 8/16/16.
 */
public class EN_ORM {

    private static final String TAG = EN_ORM.class.getSimpleName();
    private static EN_ORM ourInstance;
    private List<EN> allEnumbs;

    private EN_ORM(Context context) {

        ENumbersSQLiteAssetHelper db = new ENumbersSQLiteAssetHelper(context);

        allEnumbs = new ArrayList<>();

        Cursor cursor = db.selectAllData();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                EN enumb = new EN(cursor);
                allEnumbs.add(enumb);
                cursor.moveToNext();
            }
        }
    }

    public static EN_ORM getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new EN_ORM(context);
        }
        return ourInstance;
    }

    /**
     * Get all data.
     *
     * @return
     */
    public List<EN> getAllEnumbs() {
        return allEnumbs;
    }

    /**
     * Get data by codes. Example: E100, E123
     *
     * @param codes
     * @return
     */
    public List<EN> getEnumbsByCodeArray(String[] codes) {
        final List<String> codesList = new ArrayList<>(Arrays.asList(codes));
        List<EN> result = new ArrayList<>();

        for (EN en : allEnumbs) {
            if (codesList.contains(en.getCode())) {
                result.add(en);
            }
        }

        return result;
    }

    /**
     * Get data by code range. Example: 100-150
     *
     * @param startValue
     * @param endValue
     * @return
     */
    public List<EN> getEnumbsByCodeRange(int startValue, int endValue) {
        List<EN> result = new ArrayList<>();

        for (EN en : allEnumbs) {

            //Get the number part. matcher.group(0) will be the result. Example E123d - 123.
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(en.getCode());

            if (matcher.find()) {
                if (startValue <= Integer.parseInt(matcher.group(0)) && Integer.parseInt(matcher.group(0)) <= endValue) {
                    result.add(en);
                }
            }
        }
        return result;
    }

    /**
     * Get data by inputing contains both codes and names. Example E100, Curcumin, Alcanin, E123
     *
     * @param codesOrNames
     * @return
     */
    public List<EN> getEnumbsbyCodeAndNameArray(String[] codesOrNames) {
        List<EN> result = new ArrayList<>();
        final List<String> inputList = new ArrayList<>(Arrays.asList(codesOrNames));

        for (EN en : allEnumbs) {

            if (inputList.contains(en.getCode()) || inputList.contains(en.getName())) {
                result.add(en);
            }
        }
        return result;
    }
}
