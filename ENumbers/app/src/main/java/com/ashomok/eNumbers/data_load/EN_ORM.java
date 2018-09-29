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
    private static EN_ORM instance;
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
        if (instance == null) {
            instance = new EN_ORM(context);
        }
        return instance;
    }

    /**
     * Get all data.
     *
     * @return
     */
    List<EN> getAllEnumbs() {
        return allEnumbs;
    }

    /**
     * Get data by codes. Example: E100, E123
     *
     * @param codes
     * @return
     */
    public List<EN> getEnumbsByCodeArray(String[] codes) {
        final List<String> inputCodes = new ArrayList<>(Arrays.asList(codes));
        List<EN> result = new ArrayList<>();

        for (EN en : allEnumbs) {
            if (inputCodes.contains(en.getCode())) {
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
    List<EN> getEnumbsByCodeRange(int startValue, int endValue) {
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
    List<EN> getEnumbsbyCodeAndNameArray(String[] codesOrNames) {

        List<EN> result = new ArrayList<>();
        final List<String> inputList = new ArrayList<>();
        for (String s: codesOrNames)
        {
            inputList.add(s.toLowerCase());
        }

        for (EN en : allEnumbs) {

            if (inputList.contains(en.getCode()) || inputList.contains(en.getName().toLowerCase())) {
                result.add(en);
            }
        }
        return result;
    }

    /**
     * Get data by inputing contains one name. Example curcumin
     * @param name
     * @return
     */
    List<EN> getEnumbsByName(String name) {
        List<EN> result = new ArrayList<>();

        for (EN en : allEnumbs) {

            if (en.getName().toLowerCase().contains(name.toLowerCase())){
                result.add(en);
            }
        }
        return result;
    }
}
